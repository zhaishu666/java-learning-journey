## Day 55 (2026-9-14)
### 今天按照昨天的缺点,好好理了一下undo redo和binlog的区别,readView可见性4条规则.学习了Redis的命令客户端,通用,String,JSON结构Hash类型命令,跟着随想录学习了二叉树,写了LC144.
> 关于undo redo和binlog及readView可见性4条规则.

**我的回答**
1. redo log:
- 职责: 用来实现事务的持久性,保证崩溃后能恢复数据
- 写入方式: 循环写入,基于WAL先行日志机制,先写入Redo Log Buffer再写入磁盘,每次将脏页数据刷入磁盘时同步进行checkpoint刷脏
- 生命周期: 刷脏后数据立即清除
2. undo log:
- 职责: 用于事务回滚保证事务原子性和未MVCC提供版本链
- 写入方式: 写入的是事务提交前的旧值
- 生命周期: 事务提交后不会立即销毁,因为这些数据也可能存在于MVCC版本链中
3. binlog:
- 职责: 用于误删数据库时的数据恢复,主从复制及归档审计
- 写入方式: 写入的是所有的DML和DDL语句,但不包括查询语句
- 生命周期: 长期存在,可以无限追加
4. readView可见性4条规则
- 维护4个核心字段: m_ids: 当前活跃事务id集合. min_trx_id:最小活跃事务id. max_trx_id: 将要分配的事务id. creator_trx_id: readView创建者的事务id
- RC级别下的比较规则:
- trx_id == creator_trx_id? 可以访问->说明是当前这个事务更改的.
- trx_id < min_trx_id?可以访问->说明数据已提交
- trx_id > max_trx_id?不可访问->说用该事务是readView后开启的
- min_trx_id<=trx_id<=max_trx_id?如果trx_di不在m_ids,表示可以访问->数据已提交

**错漏点**

还有四处要修：

| 你的表述 | 修正  |
| --- | --- |
| binlog "写入的是所有的 DML 和 DDL **语句**" | 只在 STATEMENT 格式下成立。**​默认格式是 ROW——记的是每行的实际变更，不是语句**。Day 53 你刚写过三种格式，这里又退回去了 |
| redo log 刷脏后"数据立即**清除**" | 不是清除，是“**可被覆盖**"（checkpoint 推进，那段空间可重写）。循环写没有删除这个动作 |
| 比较规则标注"**RC 级别下**" | 比较规则与隔离级别**无关**，RC/RR 用的是同一套规则，区别只在 readView 的**生成时机**。这是第二次撞同一面墙 |
| "未 MVCC" | "为 MVCC"（错字） |

四条规则你默出了主干，但有两个漏洞需要补全成**完整版**（可直接抄进笔记）：

plaintext

```
若 trx_id == creator_trx_id  → 可见（自己改的）
若 trx_id <  min_trx_id      → 可见（早已提交）
若 trx_id >= max_trx_id      → 不可见（readView 之后才开启的）
若 min <= trx_id < max：
    在 m_ids 中  → 不可见（还没提交）        ← 你漏了这半边
    不在 m_ids   → 可见（已提交）
不可见 → 沿 DB_ROLL_PTR 找 undo 链上的上一版本，重新走一遍判断   ← 你漏了这一步
```

最后那行“**沿版本链回溯重判**”是算法的闭环——漏了它，不可见的版本就悬在半空没有出路。你写的 `trx_id > max` 严格来说应为 `>=`（max 是“将要分配”的 id，等于它的事务此刻不存在），实际效果等价，但笔记按 `>=` 记。

> Redis学习到的命令.

**我的回答**
1. 启动Redis: redis-cli [options] [commonds] 可以指定ip地址,端口和密码. help [commands] 可以查看命令的使用方法
2. 通用类型命令: KEYS(查看符合模板的所有key),DEL(删除指定key),EXISTS(查看key是否存在),EXPIRE(给key设置有效期),TTL(查看key有效期)
3. String类型命令: 
- set: 添加或修改一个String类型的键值对
- get: 根据key获取string类型的value
- mset/mget: 批量
- incr/decr: 让一个整形自增/自减一次
- INCRBY: 自增并指定步长
- SETNX: 添加个string类型的键值对,已存在则不添加
- SETEX: 添加个string类型的键值对,并指定存活时期
4. Redis支持多级key,中间用:隔开,如果Value是一个Java对象,可以序列化为JSON字符串后存储,但难以修改字符串中的内容,此时就可以使用Hash类型
5. Hash类型: 可以将对象中的每一个字段独立存储,格式为(key (field value)),常见命令有:
- HSET key field value: 添加或修改hash类型的key的field的值
- HGET key field: 获取hash类型的key的field的值
- HMSET/HMGET: 批量添加/获取
- HKEYS/HVALS: 获取所有的filed/value
- HINCRBY: 同String类型
- HSETNX: 同String类型

**错漏点**

1. **KEYS 在生产环境禁用**：它 O(n) 全量扫描且 Redis 命令执行是单线程，键多时直接阻塞所有请求——用 `SCAN` 游标分批替代。这是面试常问“KEYS 有什么危险”的标准答案。
2. `SET key value EX seconds NX` 一条命令同时完成 setex + setnx（2.6.12+ 推荐写法），将来学分布式锁（阶段五）会大量用到——**它能保证“加锁+设过期”的原子性**，两条命令分开写就有中间崩溃导致死锁的风险。
3. `DEL` 也是阻塞命令，删除大 key 用 `UNLINK`（异步删除）。

错字继续点名：common**ds**→commands、存活时**期**→时间、**filed**→field（两次）。

> 二叉树学习开始.

**我的初答**
1. 几个概念:
- 满二叉树: 一个二叉树只有度为0或2的节点,且度为0的节点都在同一层
- 完全二叉树: 除了最底层可能没填满,其它层都填满,且最下面的结点都集中在左边
- 二叉搜索树: 左节点小于根结点,右结点大于根结点
- 树的数据存储方式: 链表或数组
2. LC144前序遍历,只需要写个简单的递归就能解答,关键是要将判null放在最前面
3. 递归的三个原则:
- 确定递归函数的参数和返回值
- 确定终止条件
- 确定单层递归逻辑

**补充**

BST 的定义：你说“左节点小于根结点，右结点大于根结点”——不只是直接左右子节点，而是整棵左子树的所有节点 < 根 < 右子树的所有节点。这个精确度直接决定你后面 LC98（验证二叉搜索树）能不能一次做对，那题的典型错误就是只比较了父子节点。

## Day 56 (2026-9-15)
### 今天复习了List的迭代器修改原理,学习了Redis的List类型,Set类型和sortSet类型命令,LC写了145,94.
> 关于List的迭代器修改原理.

**我的回答**
1. 如果要对ArrayList,LinkedList这类fail-fast类型的集合进行迭代器或增强for循环遍历时,若对其进行结构性修改,如:remove.必须使用迭代器内部的方法,否则会抛出ConcurrentModificationException.普通for循环则不需要,但需要自己处理索引变化,可以使用反向遍历.
2. fail-fast机制: iterator内部存在一个expectedModCount == modCount的判断,ArrayList内部存在一个modCount变量用于记录修改集合结构的次数.创建迭代器时两个count相等,集合直接修改,会让modCount++,但不会修改expectedModCount,导致两者不相等,抛出异常.
这种机制用于防止迭代过程中结构修改导致获得错误结果,发现出现立即抛出异常失败.值得注意的是: modCount并非volatile,并发情况下依旧无法保证安全.

**错漏点**

三层都到位：机制（modCount/expectedModCount）、合法修改路径（迭代器内部方法）、并发局限（modCount 非 volatile）。最后那条尤其好——很多人只知道“会抛异常”，不知道 fail-fast 本质是**尽力而为（best-effort）的 bug 检测器**，JDK 文档原话是 "should be used only to detect bugs"，正确性从不靠它保证。

三个精度补充：

| 你的表述 | 补充  |
| --- | --- |
| “结构性修改，如 remove” | 结构性修改 = **改变集合大小**的操作（add/remove）。`set(i, e)` 改内容不算，不会触发 fail-fast |
| “必须使用迭代器内部的方法” | 第二层原因你没写全：`iterator.remove()` 之所以合法，是它删完会执行 `expectedModCount = modCount` **同步**——这才是“合法”的底层解释 |
| 普通for + 反向遍历 | 对。再加一条选型账：`iterator.remove()` 在 LinkedList 上 O(1)，在 ArrayList 上 O(n)（要搬移后续元素） |

还有一个你应该会喜欢的边界案例（“能过，但有没有问题？”那种）——**fail-fast 有一个著名的漏网之鱼**：

![fail-fast机制与漏网案例](img/fail_fast.svg)

补一条并发侧的闭环（呼应你 Day 35 学的 ConcurrentHashMap）：**fail-fast 的对立面是 fail-safe（弱一致迭代器**)​——`CopyOnWriteArrayList` 的迭代器持有创建瞬间的数组快照，遍历期间集合怎么改都不影响它（但它的迭代器不支持 remove，调了直接抛 UnsupportedOperationException）；`ConcurrentHashMap` 的迭代器同理。所以“遍历时修改”的正确答案分两条路：单线程用 iterator.remove()，多线程换并发集合。

> Redis的几种类型命令.

**我的回答**
1. list类型: 可以看作是一个双向链表,但底层更加复杂,支持正向检索和反向检索,通常用于存放有顺序的内容,常见命令:
- L/RPUSH key element...: 向左/右方向添加一个或多个元素
- L/RPOP key: 移除并返回左/右侧第一个元素,没有则返回null
- LRANGE key start end: 返回一段角标的元素(从0开始,闭区间)
- BL/BRPOP: 与L/RPOP类似,但没有元素等待指定时间
2. Set类型: 与Java中的HashSet结构类似,具有HashMap的特征: 无序,元素不允许重复.支持交集,并集,差集等功能,常见命令:
- SADD key member...: 添加一个或多个元素
- SREM key member...: 移除set中指定的元素
- SCARD key: 返回set中元素的个数
- SISMEMBER key member: 判断一个元素是否在set中
- SMEMBERS: 返回set中所有的元素
- SINTER key1 key2..:求交集
- SDIFF key1 key2..:求差集
- SUNION key1 key2..:求并集
3. SortSet类型: 可排序的set集合,SortSet的每一个元素都包含一个score字段,通过score对集合进行排序,底层是一个(SkipList)跳表加Hash表,常见命令:
- ZADD key score member: 添加一个或多个元素,如果存在则修改其内容
- ZREM key member: 删除指定元素
- ZSCORE key member: 获取指定元素的score
- ZRANK key member: 获取指定元素的排名
- ZCARD key: 获取指定sortSet的元素个数
- ZCOUNT key min max: 获取score在指定范围内的元素个数
- ZINCRBY key increment member: 让指定的元素自增,步长为increment的值
- ZRANGE key min max: 按照score排序后,获取指定排序范围内的元素
- ZRANGEBYSCORE key min max: 按照score排序后,获得指定score范围内的元素

**错漏点**

命令清单基本全对，ZSet 底层“跳表 + Hash 表”也对。逐个纠：

| 你的表述 | 修正/补充 |
| --- | --- |
| **SortSet** | 术语是 **ZSet / SortedSet**——今天进纠错本。命令前缀 Z 就是它 |
| ZADD “存在则修改其**内容**” | 修改的是 **score**（member 本身就是定位键，内容没得改） |
| BLPOP “没有元素等待指定时间” | 少了核心两个字：没有元素时**阻塞**等待——B = Blocking。`lpush + brpop` 就是阻塞队列，这是 List 做消息队列的根基 |
| Set “与 HashSet 结构类似” | 只对一半：Redis Set 底层是**双结构**——元素全为整数且数量少（默认 ≤128）时用 **intset**（有序整数数组，二分查找），否则转 hashtable |
| SDIFF “求差集” | 有方向：以**第一个 key 为被减数**，`SDIFF a b` = a − b，顺序换掉结果不同 |

场景这张表比命令更重要——面试问的是“这类型用在哪”：

| 类型  | 典型场景                         | 关键命令 |
| --- |------------------------------| --- |
| List | 消息队列（lpush+brpop）、朋友圈/最新动态列表 | LPUSH / BRPOP / LRANGE 0 -1 |
| Set | 点赞收藏标签（去重）、**共同关注（交集**)​、抽奖  | SADD / SISMEMBER / SINTER / SPOP |
| ZSet | **排行榜**、延迟队列（score=执行时间戳）    | ZINCRBY / ZREVRANGE |

**面试补充（必背标准答案）**​：“用 Redis 实现排行榜”——`ZINCRBY rank:2026w38 1 用户` 更新分数，`ZREVRANGE rank:2026w38 0 9 WITHSCORES` 取 TOP10。追问“为什么用跳表不用红黑树”：范围查询时跳表定位起点后**沿底层双向链表顺序扫**即可，红黑树要中序回溯上下横跳；且实现简单、层数可调（空间换时间）——这和你 HashMap 那套“为什么红黑树不用 AVL”是同一个问法，第三层答案的结构可以复用。

大 key 警告（和 KEYS/DEL 同源）：`SMEMBERS`、大集合全量 `ZRANGE` 是 O(n) 阻塞命令，生产用 `SSCAN`/`ZSCAN` 游标分批。

> LC145,94.

**我的回答**
1. 与前序遍历类似,只需要注意顺序即可.






