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
**错漏点**：


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

```

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
```

