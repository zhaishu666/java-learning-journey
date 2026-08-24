## Day 29 (2026-08-23) —— MySQL 基础与 DDL 语法

### 题目1：MySQL 中 CHAR 与 VARCHAR 的存储差异及 DDL 设计选择
> 在创建表时，`CHAR(20)` 与 `VARCHAR(20)` 在存储引擎（InnoDB）中的字节占用、尾部空格处理和性能上有何本质区别？若存储的字段是固定长度的业务编码（如订单号 `ORD20240823001`），应优先选择哪种？若存储用户昵称（最长 20 个字符，但多数仅 3~5 个字符），又该优先选择哪种？追问：`VARCHAR` 类型的最大长度受什么因素限制（行大小限制与编码）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 存储差异：
    - CHAR(n)：固定长度 n 个字符（不是字节），存储时始终占用 n 个字符的空间（尾部用空格填充至 n）。检索时默认去除尾部空格（取决于 SQL_MODE）。
    - VARCHAR(n)：可变长度，存储实际字符数 + 1~2 字节的长度前缀（若 n > 255 则 2 字节），不填充尾部空格。
- 性能对比：
    - CHAR：因长度固定，更新时不易产生页分裂（在 InnoDB 中行记录固定位置），适合频繁更新的固定长度字段。
    - VARCHAR：节省空间，但在更新变长数据时可能引发页分裂，需要额外维护行溢出页。
- 选型建议：
    - 固定编码（如订单号、身份证号、MD5 值）：优先选择 **CHAR**，避免可变长度额外开销。
    - 昵称、标题等不定长文本：优先选择 **VARCHAR**，节省存储空间（尤其是索引占用）。
- VARCHAR 最大长度限制：
    - 实际受单行最大字节数限制（InnoDB 为 65535 字节，包含所有列）。
    - 若使用 UTF-8（每个字符最多 3 字节），最大字符数约为 21845 个；若使用 UTF-8MB4（每个字符最多 4 字节），最大字符数约为 16383 个。
    - 此外，`VARCHAR` 长度前缀最大为 2 字节，因此理论上限为 65535 字节。
</details>

**我的初答**：
1. char(20)会固定占用20字节,尾部空格不会处理,性能较好.
2. varchar(20)会根据字段的实际长度调整占用的字节数,自动处理尾部空格,在性能上较差.
3. 若储存的字段是固定长度的业务编码,应使用char(..),对于非固定长度的用户名,优先选择varchar(20).
4. VARCHAR类型限制不了解

**错漏点**：

得分点：两处选型结论正确（定长编码选 CHAR、昵称选 VARCHAR），VARCHAR 按实际长度存储方向也对。

失分点（3 处硬伤 + 1 处空白）：

尾部空格处理说反了（高频送命点）。事实是：

CHAR: 存入时右侧补空格到定长,取出时剔除尾部空格(除非开启 PAD_CHAR_TO_FULL_LENGTH);

VARCHAR: 原样存,原样区3,尾部空格会保留,不存在"自动处理".

CHAR(20) 不是“固定 20 字节”。20 是**字符数不是字节数**。utf8mb4 下每字符最多 4 字节，CHAR(20) 实际占 20~80 字节；且 InnoDB 对多字节字符集的 CHAR 内部按变长存储，“真定长”只在单字节字符集下成立。

“char 性能较好”过于绝对。InnoDB 行本身是变长格式，utf8mb4 下 CHAR ≈ VARCHAR，读写差异可忽略；CHAR 在内存临时表/排序时补齐到定长，宽 CHAR 列反而更耗内存。
CHAR 的性能优势主要存在于 MyISAM 静态行格式等历史场景。

- 补充两点

比较语义：PAD SPACE 排序规则（如 utf8mb4_general_ci）下 'a' = 'a '；MySQL 8.0 默认的 utf8mb4_0900_ai_ci 是 NO PAD，尾部空格参与比较——这对唯一索引有实际影响。

性能：InnoDB 中两者差异基本可忽略，选型看存储效率与语义而非性能

三、追问：VARCHAR 最大长度受什么限制？
三层限制，由外到内：

1. 字符 ≠ 字节（语义层）
   VARCHAR(N) 的 N 是字符数，实际最大字节 = N × 字符集单字符最大字节数（utf8mb4=4，utf8mb3=3，gbk=2，latin1=1）。

2. 行大小限制 65,535 字节（server 层，超限报 ERROR 1118）
   一行所有列的最大字节数之和 + 各 VARCHAR 长度前缀 + NULL 位图 ≤ 65535。由此推出单列上限：

utf8mb4：(65535 − 2) / 4 ≈ 16,383 字符
utf8mb3：≈ 21,844 字符
latin1：≈ 65,532（可空）/ 65,533（NOT NULL）
3. InnoDB 页内限制（约 8,126 字节）
   InnoDB 页默认 16KB，行内本地存储上限约半页。单列 VARCHAR 声明超长 DDL 能过，实际数据超长时放溢出页、行内留 20 字节指针；但列数多且都长时 DDL 直接报 “Row size too large”。超长文本应改用 TEXT。
   
两个常考细节：

255 阈值：列最大字节数 ≤255 时长度前缀 1 字节。utf8mb4 下 VARCHAR(63) 前缀 1 字节，VARCHAR(64) 起就是 2 字节。

索引前缀限制：COMPACT 行格式索引前缀上限 767 字节（utf8mb4 下只能完整索引 191 字符）；DYNAMIC 行格式（5.7+ 默认）3072 字节——这就是老项目 VARCHAR(255)/VARCHAR(191) 满天飞的由来。

### 题目2：DDL 中的约束（PRIMARY KEY、UNIQUE、FOREIGN KEY）及其底层实现差异
> 在 `CREATE TABLE` 语句中，`PRIMARY KEY`、`UNIQUE`、`FOREIGN KEY` 三种约束在数据完整性保证和底层索引实现上有何区别？追问：InnoDB 中，主键索引与辅助索引在 B+ 树结构上的本质差异是什么？为什么建议使用自增主键（即 `AUTO_INCREMENT`）而非业务字段（如身份证号）作为主键？若使用 UUID 作为主键，会产生什么性能问题？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 约束差异：
    - PRIMARY KEY：唯一且非空，InnoDB 自动生成**聚集索引（Clustered Index）**，表数据按主键顺序存储（叶子节点直接存储整行数据）。每表仅一个。
    - UNIQUE：唯一（可包含 NULL，但仅允许一个 NULL），自动生成**辅助索引**，叶子节点存储主键值（需回表查询完整行）。
    - FOREIGN KEY：用于保证参照完整性（外键约束），关联父表的主键或唯一键。写入时会检查父表是否存在对应值，并可能产生锁和级联操作，影响性能。
- 主键 vs 辅助索引 B+ 树差异：
    - 主键索引（聚集）：叶子节点存储完整行数据，无需回表。
    - 辅助索引：叶子节点仅存储主键值，若需非索引字段则必须回表（二次查询）。
- 自增主键优势：
    1. 插入顺序递增，B+ 树仅在末尾追加，减少页分裂和碎片。
    2. 占用存储空间小（`INT` 4 字节，`BIGINT` 8 字节），辅助索引存储主键值，节省空间。
- UUID 作为主键缺陷：
    1. 随机插入导致频繁页分裂，索引碎片化严重，插入性能下降。
    2. 占用 16 字节，辅助索引空间膨胀（所有辅助索引均存储主键值）。
    3. 无顺序性，不利于范围查询。
</details>

**我的初答**：
**错漏点**：


### 题目3：ALTER TABLE 的常见操作及 TRUNCATE vs DELETE 的 DDL/DML 属性差异
> 请写出使用 `ALTER TABLE` 添加列、修改列数据类型、删除列的 DDL 语句。追问：`TRUNCATE TABLE` 和 `DELETE FROM` 在是否记录事务日志、是否触发触发器、是否重置自增计数器以及存储空间释放上有何本质区别？为什么 TRUNCATE 属于 DDL 而 DELETE 属于 DML？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- ALTER 典型语法：
    - 添加列：`ALTER TABLE student ADD COLUMN age INT DEFAULT 0 COMMENT '年龄';`
    - 修改数据类型：`ALTER TABLE student MODIFY COLUMN age TINYINT;`（注意类型转换风险）
    - 修改列名+类型：`ALTER TABLE student CHANGE COLUMN age user_age INT;`
    - 删除列：`ALTER TABLE student DROP COLUMN user_age;`
- TRUNCATE vs DELETE 对比：

| 维度 | TRUNCATE | DELETE |
  |------|----------|--------|
| 类型 | DDL（隐式提交，不可回滚） | DML（可回滚，支持事务） |
| 事务日志 | 仅记录数据页的释放操作（少量日志） | 逐条记录删除操作（大量日志） |
| 触发器 | 不触发 DELETE 触发器 | 触发 DELETE 触发器 |
| 自增计数器 | 重置为初始值 | 保持当前值不变 |
| 空间释放 | 立即释放数据页（高水位重置） | 仅标记删除，空间不释放（若使用 `OPTIMIZE` 可回收） |
| 速度 | 极快（直接删除表数据文件） | 慢（逐行删除，索引维护） |
- 归为 DDL 的原因：TRUNCATE 在内部通过直接重建表（或删除并重新创建表）实现，不涉及逐行数据操作，因此不能使用事务回滚。
</details>

**我的初答**：
**错漏点**：

---

## Day 30 (2026-08-24) —— DDL 进阶（约束管理、表结构变更、表删除/重命名）

### 题目1：MySQL 中的 CHECK 约束是否生效？如何保证数据逻辑一致性？
> 在 `CREATE TABLE` 中使用 `CHECK (age >= 18)` 约束，插入 `age = 16` 的记录会被阻止吗？请说明 MySQL 不同版本（5.7 vs 8.0）对 `CHECK` 约束的支持差异。若业务需要在数据库层面强制年龄大于等于 18，应该通过什么方式实现？若使用 `ENUM` 类型（如 `gender ENUM('M','F')`），插入非法值（如 'X'）会如何处理？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 5.7 及之前版本：`CHECK` 约束被**解析但忽略**（可写入 DDL 但不生效），不会阻止非法数据插入。
- 8.0+ 版本：`CHECK` 约束**默认生效**，插入 `age=16` 会被拒绝并抛出 `Check constraint violated` 错误。
- 替代方案（5.7 兼容）：
  1. 使用**触发器**（`BEFORE INSERT`）检查年龄并抛出异常（`SIGNAL SQLSTATE`）。
  2. 在应用层进行校验（不推荐作为唯一防线）。
- `ENUM` 插入行为：
  - 严格模式（`sql_mode = STRICT_TRANS_TABLES`）下，插入非法值（不在枚举列表中的）会报错。
  - 非严格模式下，插入非法值会触发警告，并插入空字符串（`''`），可能导致数据不一致。
- 最佳实践：新项目使用 MySQL 8.0+ 并开启严格模式；5.7 项目迁移至 8.0 或使用触发器/应用程序双重校验。
</details>

**我的初答**：
**错漏点**：


### 题目2：大表 ALTER 操作的风险与在线 DDL（Online DDL）原理
> 对一张 1TB 的表执行 `ALTER TABLE orders ADD INDEX idx_create_time (create_time);` 时，MySQL 会锁表吗？如果会，锁多长时间？请解释 Online DDL（`ALGORITHM=INPLACE` 与 `COPY`）两种模式的差异，以及如何安全地执行对大表的 DDL 变更（如使用 `pt-online-schema-change`）。追问：若在执行 `ALTER TABLE` 过程中杀进程，可能产生的表损坏如何恢复？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 旧版本（MySQL 5.5 及之前）：`ALTER TABLE` 会通过**复制表（COPY）** 方式，期间禁止写操作（`LOCK TABLE`），耗时极长。
- MySQL 5.6+（Online DDL）：
  - `ALGORITHM=COPY`：创建临时表复制数据，期间锁表（阻塞 DML），性能最差。
  - `ALGORITHM=INPLACE`：原地修改表结构（不复制全表），在构建索引期间允许并发 DML（但有短暂锁）。
  - 添加索引通常使用 `INPLACE` 且仅需元数据锁（MDL）在开始和结束阶段持锁，执行期间可读写。
- 安全执行策略：
  1. 优先使用 `ALGORITHM=INPLACE, LOCK=NONE`（若支持），避免业务中断。
  2. 使用 **pt-online-schema-change**（Percona Toolkit）：通过创建影子表、触发器同步数据，最终交换表名，对业务影响极小（仅最后切换时短暂锁）。
- 中途终止风险：`ALTER` 被 kill 后，事务可能回滚，但可能留下临时文件或损坏索引。恢复方法：通过 `SHOW ENGINE INNODB STATUS` 检查事务状态，若表不可用则需从备份恢复，或使用 `ALTER TABLE ... FORCE` 重建表。
</details>

**我的初答**：
**错漏点**：


### 题目3：DROP TABLE、TRUNCATE TABLE、RENAME TABLE 的底层实现与外键依赖陷阱
> 执行 `DROP TABLE orders;` 时，若有其他表的外键指向 `orders.id`（`FOREIGN KEY` 约束），该操作会失败吗？若要强制删除，应如何处理？`RENAME TABLE old_name TO new_name;` 在事务中的原子性如何？同时重命名多张表时，若中间步骤失败，是否会回滚？追问：`DROP TABLE` 后磁盘空间是否立即释放？若 InnoDB 使用独立表空间（`innodb_file_per_table=ON`），`DROP` 和 `TRUNCATE` 在空间释放上有何区别？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `DROP` 受外键限制：若存在外键引用，`DROP` 会失败并报 `Cannot delete or update a parent row: a foreign key constraint fails`。需先删除外键约束或子表（`DROP TABLE child;`），或使用 `SET FOREIGN_KEY_CHECKS=0;` 临时禁用检查（风险极高，可能导致孤儿记录）。
- RENAME 的原子性：
  - `RENAME TABLE` 是**原子操作**（在事务中执行时，表名更改在提交前对其他会话不可见）。
  - 同时重命名多张表（`RENAME TABLE a TO b, b TO c;`）也是原子的，若任何一步失败，全部回滚。
- 磁盘空间释放：
  - `DROP TABLE`：立即删除 `.ibd` 文件（如果 `innodb_file_per_table=ON`），空间立即释放回操作系统。
  - `TRUNCATE`：在 InnoDB 中，底层会创建新空表并删除旧表（类似 `DROP + CREATE`），空间立即释放（高水位重置），不逐行删除。
  - 对比：两者在独立表空间模式下都快速释放空间，但 `TRUNCATE` 不记录行级日志，性能更高且不可回滚。
</details>

**我的初答**：
**错漏点**：

---