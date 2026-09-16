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
**错漏点**：


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

