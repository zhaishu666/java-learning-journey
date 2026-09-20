## Day 1 (2026-09-15) —— Redis 基础：通用命令、String、Hash

### 题目1：KEYS 命令的生产风险与 SCAN 的替代原理
> 请解释 Redis 中 KEYS 命令的作用及为何在生产环境被禁止使用？替代方案 SCAN 的原理是什么，它如何避免阻塞？请说明 SCAN 的游标机制以及可能返回重复元素的原因。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- KEYS 命令：返回所有匹配给定模式的 key。时间复杂度 O(n)，会遍历整个键空间。
- 生产禁止原因：Redis 单线程执行命令，KEYS 会阻塞后续所有请求，直到遍历完成。若 key 数量巨大（百万级），将导致服务不可用。
- SCAN 替代：SCAN 使用游标分批迭代，每次只返回少量元素（默认 10 个），不阻塞主线程。语法：SCAN cursor [MATCH pattern] [COUNT count]。
- 游标机制：SCAN 返回一个新游标，下一次调用传入该游标继续遍历。当返回游标为 0 时表示遍历结束。
- 重复原因：在遍历过程中，如果哈希表发生扩容或缩容（rehash），元素可能被重复访问。SCAN 只保证在键空间稳定时返回所有元素，但不保证不重复。
- 补充：SCAN 家族还包括 HSCAN、SSCAN、ZSCAN，用于遍历 Hash、Set、ZSet 中的元素。
</details>

**我的初答**：
1. Redis中的KEYS命令的作用是输出符合指定样式的所有key,但其会阻塞其他内容,生产环境直接用直接让其他功能全部阻塞,性能爆减.后续所有任务全部一齐开始,缓存压力暴增.
2. SCAN采用游标查询的方式进行查询.避免阻塞,游标机制及可能返回重复元素的原因不了解.

**错漏点**：

<details>
<summary><strong>点击展开错漏点</strong></summary>

### 1. KEYS 是什么，为什么生产环境禁用

**作用**：`KEYS pattern` 返回所有匹配 pattern 的 key，如 `KEYS user:*`。时间复杂度 **O(N)**，N 是整个库的 key 总数。

**禁用的根因：Redis 执行命令是单线程的。**

- 所有命令由**一个线程**串行执行，这是 Redis 快的原因之一（无锁、无上下文切换）；

- 代价是：**任何一条慢命令都会堵死整个实例**。生产库 key 数量动辄千万级，`KEYS *` 要遍历完整个哈希表才能返回，可能耗时几秒甚至更久；

- 阻塞期间的连锁反应（你初答里隐约感觉到了，精确版本是）：

  1. 其他所有命令在队列里**干等**；

  2. 客户端等不到响应 → **超时** → 业务侧**重试**，请求越堆越多；

  3. 阻塞解除后，积压的请求瞬间涌入（你说的"全部一齐开始"），形成**请求风暴**；

  4. 如果阻塞超过主从心跳/哨兵判定时间，还可能被误判为宕机，**触发不必要的主从切换**——事故级别直接升级。


一句话：**KEYS 把"查询操作"变成了"全实例停机"，所以生产环境只允许用 SCAN。**

### 2. SCAN 的原理：为什么它不阻塞

```plain
SCAN cursor [MATCH pattern] [COUNT n]
```

核心思想一句话：**把"一次性遍历整个哈希表"拆成"多次小步遍历"，每次只走一小段就把执行权还给别人。**

- 每次调用 SCAN 只扫描哈希表上的**一小批桶**（默认约 10 个桶，由 COUNT 提示），耗时极短，返回后立即去执行其他客户端的命令；

- 下次调用带着上次返回的**游标**接着扫，直到游标回到 0 表示遍历完成；

- 单次调用之间，Redis 正常处理读写请求——**把 O(N) 的大阻塞摊平成 N 次 O(1) 的小开销**，这就是"避免阻塞"的本质：不是总工作量变小了，而是**不再独占线程**。

### 3. 游标机制（本题主菜，必须讲透）

需要一点前置知识：Redis 的 keyspace 底层是**哈希表（dict）**，由一堆**桶（bucket）**组成，且表会随数据量**动态扩容/缩容（rehash）**。

**游标 = 当前扫到哪个桶的编号。** 但 Redis 没有简单地用 `0, 1, 2, 3...` 递增，而是用了一种叫 **"反向二进制迭代"（reversed binary cursor）** 的算法：

以表大小 8（3 位二进制）为例，游标的推进顺序是：

plain复制

```plain
0(000) → 4(100) → 2(010) → 6(110) → 1(001) → 5(101) → 3(011) → 7(111) → 回到 0
```

**为什么这么绕？关键在扩容时的表现。**

假设表从 8 扩容到 16（多了一位二进制位）：普通递增游标在扩容瞬间会不知道自己扫到哪了，可能大面积重扫或漏扫；而反向二进制游标有一个数学性质：**扩容/缩容后，已访问过的桶集合和未访问的桶集合仍然被游标严格分开**，继续迭代下去，能保证：

- 只要一个 key **从头到尾都存在于库里**，SCAN 完整跑完一轮**一定会返回它至少一次**（不重不漏是目标）；

- 代价就是下面说的：可能重复。


你不需要在面试里背出迭代顺序，但要说得出：**"游标是哈希桶的编号，用反向二进制算法推进，这个设计是为了让遍历过程对哈希表的扩容/缩容保持兼容。"**

### 4. 为什么会返回重复元素

两个原因，都和"扫描过程中表在变"有关：

1. **rehash 导致桶的内容迁移**：SCAN 扫到一半时哈希表扩容或缩容了，原来在 3 号桶的 key 被重新散列到别的桶。如果那个桶**还没扫到**，这个 key 会被再扫到一次；如果客户端在两次之间都收到了它，就是重复。

2. **key 在扫描期间被修改/重哈希**：遍历过程中有其他写入导致键的桶位置变化，同一 key 可能被不同桶"各报到一次"。


**结论要记住：SCAN 保证"存在期间的 key 至少返回一次"，但不保证"只返回一次"**——所以客户端必须自己做**去重**（比如用 Set 收集结果）。

顺便补一个面试官常连环追问的点：**SCAN 也可能"漏"**——扫描期间**新增**的 key 不保证被扫到，扫描期间被删除的 key 可能根本不会出现。即 SCAN 是"**弱一致性快照**"，不锁表、不阻塞，换来的是结果不精确。
</details>

### 题目2：String 类型底层 SDS 与 C 字符串的差异及典型应用
> Redis 的 String 类型底层使用 SDS（简单动态字符串），而不是 C 字符串。请说明 SDS 相比 C 字符串的优点，并列举 String 类型的三个典型应用场景（如缓存、计数器、分布式锁），简述其实现命令。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- SDS 相比 C 字符串的优点：
    1. O(1) 获取字符串长度：SDS 有 len 字段，C 字符串需遍历。
    2. 二进制安全：SDS 可存储任意字节（包括 \0），C 字符串以 \0 结尾，无法存储二进制。
    3. 防止缓冲区溢出：SDS 修改前检查空间，自动扩容。
    4. 减少内存重分配：SDS 采用空间预分配和惰性释放。
- 典型应用场景：
    1. 缓存对象：SET user:1 '{"name":"张三","age":20}' EX 3600
    2. 计数器：SET counter 100，INCR counter，INCRBY counter 5
    3. 分布式锁：SET lock:order:123 true NX EX 10（NX 保证只有不存在时才设置，EX 设置过期时间）
- 其他：分布式 Session（SET session:token userInfo）、限流（INCR + EXPIRE）。
</details>

**我的初答**：
**错漏点**：


### 题目3：Hash 类型的编码转换及与 String 存储对象的对比
> Redis 的 Hash 类型有两种底层编码：ziplist 和 hashtable。请说明它们的转换条件（阈值参数），并解释为什么小 Hash 用 ziplist 更省内存。在 Java 后端开发中，存储用户对象时，用 Hash 和用 String（JSON 序列化）各有什么优缺点？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 编码转换条件（redis.conf 默认值）：
    - hash-max-ziplist-entries 512：当 Hash 中 field 数量超过 512 时，转为 hashtable。
    - hash-max-ziplist-value 64：当任意 field 或 value 的长度超过 64 字节时，转为 hashtable。
- ziplist 省内存原因：ziplist 是一块连续内存，元素紧凑排列，无指针开销。hashtable 需要维护哈希表结构（数组+链表），每个节点有额外指针，内存占用更大。小 Hash 用 ziplist 可显著节省内存。
- Hash vs String 存储对象：
    - Hash 优点：可单独更新某个字段（HSET），无需读取整个对象；内存占用通常更小（尤其字段少时）。
    - Hash 缺点：无法直接存储复杂嵌套结构；过期时间只能加在整个 key 上，不能给单个 field 设置。
    - String（JSON）优点：可存储任意复杂结构；与 Java 对象序列化/反序列化方便（如 Jackson）。
    - String 缺点：更新任一字段都需读取整个 JSON、修改、再写回，并发下易覆盖；内存占用相对较大。
- 选择建议：对象字段少且需频繁单独更新，用 Hash；对象结构复杂或整体读写，用 String JSON。
</details>

**我的初答**：
**错漏点**：

---

## Day 2 (2026-09-16) —— Redis List、Set、SortedSet

### 题目1：Redis List 的底层结构与消息队列实现
> Redis List 底层使用 quicklist 数据结构。请解释 quicklist 是什么？它如何结合 ziplist 和 linkedlist 的优点？在 Java 后端开发中，常用 List 实现消息队列（LPUSH + BRPOP）。请说明这种实现方式与专业消息队列（如 RabbitMQ）相比有哪些优缺点？如何用 List 实现一个简单的延时队列？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- quicklist：Redis 3.2 引入，是 ziplist 和 linkedlist 的混合体。每个节点是一个 ziplist，节点之间用双向指针连接。ziplist 存储多个元素，节省内存；linkedlist 提供快速插入删除。综合了内存效率和操作性能。
- List 实现消息队列：生产者 LPUSH 消息，消费者 BRPOP 阻塞获取。优点：简单、无需额外中间件、支持阻塞。缺点：不支持消息确认（ACK），消息可能丢失（消费者取出后宕机）；不支持多消费者组；不支持消息持久化（取决于 Redis 持久化）；无法回溯。专业 MQ 如 RabbitMQ 支持 ACK、持久化、路由、死信队列等。
- 延时队列实现：使用 ZSet 更合适，但 List 也可通过 BRPOP 配合定时任务轮询，或使用 ZSet 存时间戳。常见方案：用 ZSet 存储任务，score 为执行时间戳，定时用 ZRANGEBYSCORE 获取到期任务。也可以用 List 存任务，但需额外轮询检查时间。
</details>

**我的初答**：
1. quicklist是ziplist和linkedlist的结合,每个节点是一个ziplist,之间用双向链表连接,两边都可进行增删
2. List实现消息队列,通过LPUSH+BRPOP完成,优点就是实现简单,缺点就是宕机后会出现数据丢失
3. RabbitMQ不了解
4. 实现不懂

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

### 1. Quicklist 是什么

Quicklist 是 Redis 3.2 之后 List 的底层实现，它是 **ziplist（紧凑列表）和双向链表（linkedlist）的折中组合**：

- 整体结构是一个**双向链表**，但链表的每个节点不再是单个元素，而是一个 **ziplist**（或 6.2 之后的 listpack）。

- 每个 ziplist 内部是一段连续内存，存储多个元素。


**它如何结合两者优点：**

| 结构  | 优点  | 缺点  |
| --- | --- | --- |
| 纯 linkedlist | 两端增删 O(1)，增删任意位置不移动数据 | 每个节点两个指针，内存开销大；节点分散，缓存不友好 |
| 纯 ziplist | 连续内存，省空间，缓存友好 | 大列表中插入/删除要整体 memmove，极端情况级联更新，性能差 |

Quicklist 的折中思路：

- **把链表节点"变大"**：每个节点存一小段 ziplist，既保留了链表两端操作 O(1)、修改不影响全局的特性，又利用了 ziplist 连续内存、节省指针开销、缓存命中率高的优点。

- **插入删除只需操作单个 ziplist 内部**，memmove 的范围被限制在一个节点内，避免了整个大列表的级联更新。

- 每个节点 ziplist 的大小由 `list-max-ziplist-size` 控制（默认 128，正数表示元素个数上限，负数表示字节上限）。

- 支持对中间节点 **LZF 压缩**（`list-compress-depth`，如设为 1 表示首尾各留 1 个节点不压缩），进一步省内存，因为两端访问最频繁，中间数据冷。


一句话总结：**quicklist = 空间效率（ziplist 连续内存）+ 时间效率（链表局部修改）的平衡。

2. List 实现消息队列 vs RabbitMQ

`LPUSH + BRPOP` 模式：生产者 LPUSH 入队，消费者 BRPOP 阻塞拉取（空队列时阻塞等待，避免轮询浪费 CPU）。

**优点：**

- 实现简单，几行代码搞定，无额外中间件部署成本；

- Redis 性能极高，单机轻松支撑几万 QPS；

- 已有 Redis 基础设施时零成本复用。


**缺点（面试重点）：**

1. **可靠性差**：消息消费靠 BRPOP 弹出即删除，没有 ACK 机制。消费者拿到消息后宕机，消息就丢了。RabbitMQ 有 ACK 确认 + 重新投递机制。

2. **持久化弱**：Redis 默认是内存数据库，RDB/AOF 有丢失窗口；RabbitMQ 支持消息持久化到磁盘、队列镜像。

3. **无消息堆积能力**：Redis 内存有限，消息大量堆积会撑爆内存；RabbitMQ 设计上就支持海量堆积。

4. **功能单一**：没有路由（exchange）、主题订阅、死信队列、消息重试、优先级等能力。

5. **不支持多消费者组/广播**：一个 List 只能被竞争消费，做不到一条消息多个消费者各自消费一份（Redis 5.0 的 Stream 才补上这个能力）。


**结论**：对可靠性要求不高、吞吐量大、逻辑简单的场景（如异步日志、非核心通知）用 List 够了；涉及订单、支付等核心业务，必须用 RabbitMQ/Kafka 这类专业 MQ。

### 3. 用 List 实现简单延时队列

List 本身没有定时能力，常见思路是**用两个 List + 定时搬运**：

- **结构**：`delay_list`（存待延时消息，元素里带到期时间戳）+ `ready_list`（存已到期、可消费的消息）。

- **生产者**：`LPUSH delay_list {taskId, expireTime}`。

- **搬运线程**：定时（如每秒）轮询 `delay_list`，检查每条消息的到期时间，到期的就 `LPOP` 出来 `LPUSH` 到 `ready_list`。

- **消费者**：`BRPOP ready_list` 阻塞消费。


**缺陷**：List 不支持按时间排序，轮询检查要遍历全表，效率低；还要自己处理原子性（用 Lua 脚本把"检查+转移"合成原子操作）。

**更优实践（主动讲出来是加分项）**：延时队列用 **ZSET** 更合适——score 存到期时间戳，消费者用 `ZRANGEBYSCORE key 0 now` 取出到期任务，天然有序、O(logN) 定位。生产环境还可以直接用 Redisson 的 `DelayedQueue` 或 RabbitMQ 的延迟插件。
</details>

### 题目2：Set 类型的去重原理与共同好友实现
> Redis Set 底层使用 intset 或 hashtable。请说明两种编码的转换条件。在社交场景中，如何用 Set 实现“共同好友”和“可能认识的人”？请写出核心命令。另外，SRANDMEMBER 和 SPOP 都能随机取元素，它们有何区别？为什么 SMEMBERS 命令在生产环境要慎用？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 编码转换：intset 用于元素全为整数且数量不超过 set-max-intset-entries（默认512）；否则转为 hashtable。
- 共同好友：SINTER user:1:friends user:2:friends。可能认识的人：SDIFF user:1:friends user:2:friends（差集）或 SUNION 后排除自己。
- SRANDMEMBER：随机返回一个或多个元素，不删除元素。SPOP：随机弹出并删除一个或多个元素。前者用于抽奖但保留元素，后者用于抽奖并移除。
- SMEMBERS 慎用：返回集合所有元素，时间复杂度 O(n)。若集合很大（百万级），会阻塞 Redis 单线程，导致其他请求超时。应使用 SSCAN 分批遍历。
</details>

**我的初答**：
**错漏点**：


### 题目3：SortedSet 实现排行榜与跳表原理
> Redis SortedSet 底层使用 ziplist 或 skiplist + dict。请说明 skiplist 相比平衡树的优势，以及为什么 Redis 选择跳表。如何使用 SortedSet 实现一个游戏排行榜？如果分数相同，Redis 如何排序？如何用 SortedSet 实现延时队列？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- skiplist 优势：实现简单，范围查询高效（跳表天然有序），插入删除只需调整指针，无需旋转。平衡树（如红黑树）实现复杂，范围查询需中序遍历。跳表平均 O(log n)，与平衡树相当，但更易实现和调试。
- 排行榜实现：ZADD leaderboard score member 添加分数；ZREVRANGE leaderboard 0 9 WITHSCORES 获取前10名；ZRANK 获取排名；ZINCRBY 增加分数。
- 分数相同排序：SortedSet 按 score 升序排列，若 score 相同，则按 member 的字典序（lexicographical）排列。因此排行榜中若分数相同，用户名靠前的排前面。
- 延时队列：ZADD delay_queue timestamp task，时间戳为 score。消费者定时执行 ZRANGEBYSCORE delay_queue 0 now LIMIT 0 10 获取到期任务，然后 ZREM 删除。需配合定时任务轮询。
</details>

**我的初答**：
**错漏点**：

---

## Day 3 (2026-09-17) —— Jedis、Spring Data Redis 与缓存集成

### 题目1：Spring Cache 注解 @Cacheable 的工作原理与自调用失效场景
> 在 Spring Boot 中，添加 @EnableCaching 后，在 Service 方法上使用 @Cacheable、@CachePut、@CacheEvict 即可实现缓存。请回答：
> 1. @Cacheable 的底层是如何通过 AOP 和 CacheManager 实现的？方法执行前、执行后分别发生了什么？
> 2. 为什么同一个类中，方法 A 调用方法 B（B 上有 @Cacheable），缓存会失效？如何解决？
> 3. @Cacheable 的 key 默认如何生成？如何自定义 KeyGenerator？condition 和 unless 有什么区别？
> 追问：如果缓存中已经存在数据，但数据库数据被其他服务修改了，@Cacheable 能感知吗？应如何配合 @CacheEvict 或 Redis 发布订阅解决？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- @Cacheable 底层：
  - Spring 通过 CacheInterceptor 拦截被注解的方法。
  - 方法执行前：根据 cacheNames 和 key 生成缓存 key，调用 CacheManager 获取 Cache，再调用 cache.get(key)。若命中，直接返回缓存值，不执行目标方法。
  - 方法执行后：若未命中，执行目标方法，将返回值 cache.put(key, value)，再返回结果。
- 自调用失效：
  - Spring Cache 基于 AOP 代理。类内方法 A 调用方法 B 时，使用的是 this 引用，不经过代理对象，因此 CacheInterceptor 不会拦截 B。
  - 解决方案：
    1. 注入自身代理：@Autowired private UserService self; 通过 self.methodB() 调用。
    2. 使用 AopContext.currentProxy()，需开启 exposeProxy = true。
    3. 将方法 B 拆分到另一个 Service 中。
- key 生成：
  - 默认使用 SimpleKeyGenerator：无参数时返回 SimpleKey.EMPTY；一个参数时直接返回该参数；多个参数时返回包含所有参数的 SimpleKey。
  - 自定义：实现 KeyGenerator 接口，或在注解中指定 key = "#id" 使用 SpEL。
  - condition：满足条件时才走缓存（方法执行前判断）。unless：方法执行后，满足条件时不缓存（如返回值 null 不缓存）。
- 追问：
  - @Cacheable 无法感知其他服务对数据库的修改，缓存会一直命中旧值直到过期或被删除。
  - 解决方案：更新数据库时用 @CacheEvict 删除缓存；或订阅 MySQL binlog（Canal）异步删除；或使用 Redis 发布订阅通知所有节点清缓存。
</details>

**我的初答**：
**错漏点**：


### 题目2：RedisTemplate 默认序列化器的问题及生产环境配置
> Spring Data Redis 提供了 RedisTemplate 和 StringRedisTemplate。请回答：
> 1. RedisTemplate 默认使用什么序列化器？为什么直接使用它存储的 key 在 Redis 客户端中会看到乱码或二进制前缀？
> 2. StringRedisTemplate 与 RedisTemplate 的核心区别是什么？分别适合什么场景？
> 3. 在生产环境中，如何配置 RedisTemplate 使用 JSON 序列化？请说明 key 用 StringRedisSerializer、value 用 GenericJackson2JsonRedisSerializer 的原因。若存储 Hash 类型，序列化器应如何配置？
> 追问：GenericJackson2JsonRedisSerializer 会在 JSON 中写入 @class 类型信息，这有什么优缺点？如何避免跨语言反序列化问题？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 默认序列化器：
  - RedisTemplate 默认使用 JdkSerializationRedisSerializer，它要求存储的对象实现 Serializable 接口，序列化后是二进制字节。
  - 在 Redis 客户端中查看 key 时，会看到类似 \xac\xed\x00\x05t\x00\x04user 的二进制前缀，可读性极差。
- StringRedisTemplate：
  - key 和 value 都使用 StringRedisSerializer，存储的是可读字符串。
  - 适合存储字符串、JSON 文本、计数器等。RedisTemplate 适合存储 Java 对象，但需自定义序列化器。
- 生产配置：
  - key：StringRedisSerializer，保证 key 可读且跨语言。
  - value：GenericJackson2JsonRedisSerializer 或 Jackson2JsonRedisSerializer，存储 JSON。
  - Hash 类型：hashKey 用 StringRedisSerializer，hashValue 用 JSON 序列化器。
  - 示例配置思路（伪代码）：
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
- 追问：
  - GenericJackson2JsonRedisSerializer 会写入 @class 字段记录全限定类名，方便反序列化回具体类型。
  - 优点：自动还原类型。缺点：JSON 体积变大，且跨语言（如 Python、Go）读取时无法识别 @class，可能报错。
  - 避免跨语言问题：使用 Jackson2JsonRedisSerializer 指定具体类型，或统一使用纯 JSON 字符串，由调用方自行反序列化。
</details>

**我的初答**：
**错漏点**：


### 题目3：Jedis 与 Lettuce 的选型对比及连接池配置
> Spring Boot 2.x 默认使用 Lettuce 作为 Redis 客户端，而很多老项目仍在使用 Jedis。请回答：
> 1. Jedis 和 Lettuce 在连接模型、线程安全性、异步支持上有何本质区别？为什么 Jedis 需要连接池，而 Lettuce 可以共享一个连接？
> 2. 在 Spring Boot 中，如何配置 Lettuce 连接池？常用参数（max-active、max-idle、min-idle、max-wait）分别表示什么？
> 3. 什么场景下仍然推荐使用 Jedis？如果项目中需要 Jedis 的高级 API（如 Pipeline、事务），Lettuce 是否支持？
> 追问：Lettuce 的共享连接在 Redis 集群模式下是否安全？为什么说 Lettuce 更适合云原生和响应式编程？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- Jedis vs Lettuce：
  - Jedis：直连 Redis，一个连接对应一个 Redis 实例。Jedis 实例不是线程安全的，多线程并发需使用 JedisPool 连接池，每个线程获取独立连接。
  - Lettuce：基于 Netty，线程安全，多个线程可共享同一个连接。支持同步、异步、响应式（Reactive）API。连接可自动重连。
- 连接池配置（Lettuce）：
  - spring.redis.lettuce.pool.max-active：最大连接数。
  - max-idle：最大空闲连接数。
  - min-idle：最小空闲连接数。
  - max-wait：获取连接的最大等待时间。
  - 若未配置 pool，Lettuce 默认使用共享连接，不启用连接池。
- Jedis 适用场景：
  - 老项目已深度使用 Jedis API。
  - 需要 Jedis 特有的 Pipeline、事务、Lua 脚本等 API（Lettuce 也支持，但 API 不同）。
  - 团队更熟悉 Jedis。
- 追问：
  - Lettuce 在 Redis 集群模式下，共享连接是安全的，因为它内部维护了集群拓扑和连接管理。
  - Lettuce 基于 Netty，天然支持异步和响应式，适合 Spring WebFlux、云原生高并发场景。
</details>

**我的初答**：
**错漏点**：

---

## Day 4 (2026-09-18) —— 缓存更新策略与三大缓存问题

### 题目1：操作缓存的三种方式与并发一致性分析
> 在 Java 后端开发中，操作缓存（Redis）与数据库（MySQL）的常见方式有三种：
> 1. 先更新数据库，再删除缓存（Cache Aside 推荐方案）
> 2. 先删除缓存，再更新数据库
> 3. 先更新数据库，再更新缓存
     > 请分别说明三种方式的优缺点，并解释为什么推荐“先更新 DB 再删缓存”。追问：在极端并发下（读请求在删缓存后、更新 DB 前到达），仍然可能读到旧数据并回写缓存，如何用“延时双删”或“订阅 binlog”解决？延时双删的延迟时间如何确定？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 三种方式对比：
  1. 先更新 DB 再删缓存（Cache Aside）：推荐。删除缓存比更新缓存更简单，避免并发更新缓存导致脏数据。异常情况：若删缓存失败，可重试或依赖过期时间兜底。
  2. 先删缓存再更新 DB：不推荐。并发读请求可能在删缓存后、更新 DB 前读到旧数据并回写缓存，导致长时间不一致。
  3. 先更新 DB 再更新缓存：不推荐。并发更新时，两个线程可能以不同顺序更新缓存，导致缓存中留下旧值。且若缓存值需要复杂计算，更新成本高。

- 极端并发场景（先更新 DB 再删缓存）：
  - 线程 A 更新 DB，删除缓存。
  - 线程 B 在 A 删缓存后、A 更新 DB 前（或 A 删缓存后、事务提交前）发起读请求，发现缓存为空，从 DB 读到旧值（因为 A 的事务可能尚未提交或主从延迟），然后回写缓存。
  - 结果：缓存中又出现旧值。

- 解决方案：
  1. 延时双删：更新 DB 前删一次，更新 DB 后延迟再删一次。延迟时间应大于一次读请求的耗时（通常 500ms~1s），确保并发读请求回写的旧值被第二次删除。但延迟时间难以精确，且高并发下仍可能漏网。
  2. 订阅 binlog：使用 Canal 监听 MySQL binlog，当数据变更时异步删除缓存。业务代码只更新 DB，不操作缓存，保证最终一致性。这是目前最可靠的方案。

- 追问：延时双删的延迟时间一般设置为读请求平均耗时的 2~3 倍，或直接设置为 1 秒。但无法根治，仅降低概率。binlog 方案更彻底，但引入 Canal 增加运维复杂度。
</details>

**我的初答**：
**错漏点**：


### 题目2：缓存穿透、击穿、雪崩的区别及解决方案（结合 Spring Boot 实现）
> 请分别解释缓存穿透、缓存击穿、缓存雪崩的定义，并各给出至少两种解决方案。追问：在 Spring Boot 中，如何使用 RedisTemplate + 布隆过滤器防止缓存穿透？请描述布隆过滤器的原理、误判率及不支持删除的缺陷。若采用缓存空对象方案，空对象的过期时间应设置多长？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 缓存穿透：查询不存在的数据，缓存和 DB 都无，请求每次都打到 DB。解决方案：
  1. 缓存空对象：将查询结果为空也缓存，设置较短过期时间（如 60 秒），防止同一恶意 key 反复攻击。
  2. 布隆过滤器：在缓存前加一层布隆过滤器，快速判断 key 是否存在，不存在直接返回。
- 缓存击穿：热点 key 在失效瞬间，大量并发请求同时打到 DB。解决方案：
  1. 互斥锁：只允许一个线程去加载数据，其他线程等待。可用 Redis SETNX 实现分布式锁。
  2. 逻辑过期：不设置物理过期，value 中存逻辑过期时间，后台异步刷新。
  3. 热点 key 永不过期，定时任务更新。
- 缓存雪崩：大量 key 同时过期或 Redis 宕机，导致 DB 压力骤增。解决方案：
  1. 过期时间加随机值，避免同时失效。
  2. Redis 高可用集群（主从+哨兵或 Cluster），防止单点故障。
  3. 限流降级（Sentinel/Hystrix），保护 DB。

- 布隆过滤器原理：一个位数组 + 多个哈希函数。添加元素时，将多个哈希位置置 1；查询时，若所有哈希位置均为 1，则可能存在（有误判率）；若任一为 0，则一定不存在。误判率由位数组大小和哈希函数个数决定。不支持删除，因为删除可能影响其他元素。
- 空对象过期时间：通常设置 60~300 秒。太短则穿透仍可能反复，太长则可能因数据后续被创建而导致脏数据（用户注册后仍返回空）。
</details>

**我的初答**：
**错漏点**：


### 题目3：使用 Redis 分布式锁解决缓存击穿（避免锁误删与 Redisson 看门狗）
> 缓存击穿场景下，常用分布式锁保证只有一个线程加载数据。请回答：
> 1. 使用 Redis 的 SETNX + EXPIRE 实现分布式锁时，为什么必须用 Lua 脚本保证原子性？如何避免锁被其他线程误删（value 存唯一标识）？
> 2. 若业务执行时间超过锁过期时间，锁自动释放后其他线程可能获取锁，导致并发问题。Redisson 的看门狗（Watch Dog）机制如何解决？请说明其原理（默认续期时间、续期间隔）。
> 3. 在缓存击穿的代码实现中，获取锁失败后，通常采用自旋重试还是直接返回旧值？分别适用于什么场景？
     > 追问：Redis 分布式锁在主从切换时可能丢失锁（主节点宕机，从节点未同步锁），RedLock 算法如何解决？为什么 RedLock 仍存在争议？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- SETNX + EXPIRE 原子性：
  - 若先 SETNX 成功，再执行 EXPIRE 前宕机，锁将永不过期，导致死锁。
  - 应使用一条命令：SET lock_key unique_value NX EX 10。或者使用 Lua 脚本保证原子性。
- 避免误删：
  - value 存储当前线程的唯一标识（如 UUID）。
  - 解锁时先 GET 判断 value 是否匹配，匹配则 DEL。该判断+删除也需用 Lua 脚本保证原子性。
- Redisson 看门狗：
  - 默认锁过期时间 30 秒。若业务未执行完，Redisson 会启动一个后台线程，每隔 10 秒（1/3 过期时间）检查业务是否完成，若未完成则将锁续期至 30 秒。
  - 当业务执行完毕释放锁时，看门狗线程会被取消。
- 获取锁失败策略：
  - 自旋重试：适用于对响应时间要求不高、希望最终获取锁的场景，但需限制重试次数和间隔，避免 CPU 空转。
  - 直接返回旧值：适用于可以容忍短暂数据不一致的场景，提升响应速度，等缓存过期后自动更新。
- 追问（RedLock）：
  - RedLock 要求向多个独立 Redis 节点（通常 5 个）申请锁，多数成功才认为加锁成功。用于解决主从切换丢锁问题。
  - 争议：RedLock 依赖系统时钟，且若所有节点重启，仍可能丢锁；性能下降；实现复杂。Martin Kleppmann 与 Redis 作者 Antirez 曾有著名辩论。多数场景下，使用单节点 Redisson 锁 + 合理业务兜底已足够。
</details>

**我的初答**：
**错漏点**：

---

## Day 5 (2026-09-19) —— Redis 持久化 RDB、AOF 与混合持久化

### 题目1：RDB 与 AOF 的机制、优缺点及生产选型
> 请从触发方式、数据安全性、文件体积、恢复速度、对性能影响等维度对比 RDB 和 AOF。解释 save 和 bgsave 的区别，以及 bgsave 的 fork + copy-on-write 机制。生产环境如何选择？如果既要恢复快又要数据安全，应如何配置？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- RDB：二进制快照，保存某个时间点的全量数据。触发方式：手动 save（阻塞）、bgsave（fork 子进程）、配置 save 规则（如 save 900 1）、主从复制时自动触发。
- AOF：追加写命令日志，记录所有写操作。触发方式：appendonly yes 开启，实时写入。
- 对比：
  - 数据安全性：AOF 更高（everysec 最多丢 1 秒），RDB 可能丢较多（取决于 save 频率）。
  - 文件体积：RDB 小（压缩二进制），AOF 大（文本命令，重写后变小）。
  - 恢复速度：RDB 快（直接加载内存），AOF 慢（重放命令）。
  - 性能影响：RDB bgsave fork 时有短暂阻塞，AOF everysec 有后台刷盘开销。
- save vs bgsave：save 由主进程执行，阻塞所有请求；bgsave fork 子进程执行，主进程继续处理请求，仅 fork 瞬间阻塞。
- fork + COW：fork 创建子进程，父子共享内存页。子进程写时，内核复制被修改的页，父进程继续读写原页。因此 bgsave 期间内存可能增长（写操作多时）。
- 生产选型：通常同时开启 RDB 和 AOF。RDB 用于定期备份、快速恢复、主从复制；AOF 用于保证数据安全。Redis 4.0+ 推荐混合持久化。
- 配置建议：appendonly yes，appendfsync everysec，save 900 1 等。若允许丢数据，可只用 RDB。核心业务建议 AOF + RDB。
</details>

**我的初答**：
**错漏点**：


### 题目2：AOF 写回策略、重写机制与文件损坏修复
> 请解释 appendfsync 的 always、everysec、no 三种策略的区别及性能/安全权衡。AOF 重写（bgrewriteaof）的目的是什么？重写期间的新写命令如何处理（AOF 重写缓冲区）？如果 AOF 文件损坏，如何修复？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- appendfsync 策略：
  - always：每个写命令都 fsync 刷盘。数据最安全，几乎不丢，但性能最差（磁盘 I/O 频繁）。
  - everysec（默认）：每秒 fsync 一次。最多丢 1 秒数据，性能与安全平衡，生产推荐。
  - no：由操作系统决定何时刷盘（通常 30 秒）。性能最好，但宕机可能丢较多数据。
- AOF 重写目的：AOF 文件会不断膨胀（如多次 INCR），重写生成一个体积更小的新 AOF 文件，只保留恢复当前数据集所需的最小命令集。
- 重写触发：手动 bgrewriteaof；配置 auto-aof-rewrite-percentage（如 100%）和 auto-aof-rewrite-min-size（如 64MB）。
- 重写期间新命令：
  - 主进程将新写命令同时写入原 AOF 缓冲区和 AOF 重写缓冲区。
  - 子进程根据当前内存快照生成新 AOF 文件。
  - 子进程完成后，主进程将重写缓冲区内容追加到新 AOF 文件，然后原子替换旧文件。
- AOF 损坏修复：
  - 使用 redis-check-aof --fix appendonly.aof 修复。
  - 若 AOF 末尾不完整，可截断损坏部分；若严重损坏，可能需要从 RDB 恢复。
  - Redis 启动时若 AOF 损坏且未修复，会拒绝启动（可配置 aof-load-truncated yes 容忍末尾截断）。
</details>

**我的初答**：
**错漏点**：


### 题目3：混合持久化（RDB+AOF）与 Redis 7.0 Multi Part AOF
> Redis 4.0 引入混合持久化，Redis 7.0 又引入 Multi Part AOF。请解释混合持久化的原理（AOF 文件开头是 RDB 格式，后续是 AOF 命令），为什么它能兼顾恢复速度和数据安全？Multi Part AOF 如何解决 AOF 重写时的文件膨胀和原子替换问题？在 Java 后端生产环境中，如何配置持久化策略以平衡性能与数据安全？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 混合持久化（Redis 4.0+）：
  - 开启：aof-use-rdb-preamble yes（默认 yes）。
  - AOF 重写时，子进程将当前内存快照以 RDB 格式写入新 AOF 文件开头，再将重写期间的增量命令以 AOF 格式追加。
  - 恢复时：先加载 RDB 部分快速恢复大部分数据，再重放 AOF 部分增量命令，兼顾速度与安全。
- Redis 7.0 Multi Part AOF：
  - 将 AOF 分为基础文件（base，RDB 或 AOF 格式）和增量文件（incr，AOF 格式），用 manifest 文件管理。
  - 重写时只需生成新的 base 文件，增量文件继续追加，避免旧版重写时的大文件复制和原子替换开销。
  - 支持更灵活的文件管理和损坏恢复。
- 生产配置建议：
  - appendonly yes
  - appendfsync everysec
  - aof-use-rdb-preamble yes
  - save 900 1 300 10 60 10000（保留 RDB 备份）
  - 监控：AOF 文件大小、重写频率、fork 耗时、磁盘 I/O。
  - 若使用云 Redis，通常由云厂商管理持久化，但需了解其策略。
- 追问：Redis 宕机后恢复流程：
  1. 若 AOF 开启，优先加载 AOF（混合持久化时先 RDB 后 AOF）。
  2. 若 AOF 关闭，加载 RDB。
  3. 若两者都无，启动空数据库。
  4. 恢复时间取决于数据量和 AOF 大小。
</details>

**我的初答**：
**错漏点**：

---