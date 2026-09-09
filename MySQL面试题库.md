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

## Day 31 (2026-08-25) —— DML 基础（INSERT、UPDATE、DELETE）

### 题目1：INSERT INTO ... ON DUPLICATE KEY UPDATE 与 REPLACE INTO 的底层差异
> 现有表 `user(id INT PRIMARY KEY, name VARCHAR(50), age INT)`。若执行 `INSERT INTO user (id, name, age) VALUES (1, 'Alice', 20) ON DUPLICATE KEY UPDATE name='Alice', age=20;` 与 `REPLACE INTO user (id, name, age) VALUES (1, 'Alice', 20);` 在数据插入或更新时，底层操作有何本质区别？对自增主键（`AUTO_INCREMENT`）、触发器、以及 `ON DELETE CASCADE` 外键约束的影响分别是什么？追问：若表中有多个唯一键（如 `UNIQUE(name)`），两者在处理冲突时的行为有何不同？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `INSERT ... ON DUPLICATE KEY UPDATE`：当主键或唯一键冲突时，执行 `UPDATE` 部分（更新指定列）。若冲突键为唯一索引（非主键），也会触发更新，且不影响自增主键的当前值。
- `REPLACE INTO`：当冲突发生时，**先删除旧行，再插入新行**（相当于 `DELETE` + `INSERT`）。这会导致：
  - 自增主键值若冲突，会消耗新 ID（若有 `AUTO_INCREMENT`，插入时可能生成新的自增值，覆盖旧 ID，但若指定 ID 则不变）。
  - 触发器会分别触发 `BEFORE DELETE`、`AFTER DELETE`、`BEFORE INSERT`、`AFTER INSERT`（若存在）。
  - `ON DELETE CASCADE` 外键约束可能导致级联删除（若被引用），风险极大。
- 多唯一键冲突：
  - `ON DUPLICATE KEY UPDATE` 只处理**第一个**遇到的冲突键（按索引顺序），但会检测所有冲突。
  - `REPLACE` 会删除所有冲突行（若有多个唯一键冲突且涉及多行），可能导致意外删除。
- 建议：优先使用 `ON DUPLICATE KEY UPDATE` 以避免不必要的删除和级联风险。
</details>

**我的初答**：
1. replace into 是在出现主键/唯一键冲突时,先删除旧键,再将新键插入.
2. on duplicate key update 则是在出现冲突时更新指定的字段,如浏览量+1,题中则是更新name和age.
3. 对自增主键,触发器,外键约束的影响不了解.
4. 追问不了解

**错漏点**：
- 你的初答基本正确，再精确一点：
- **REPLACE INTO = DELETE + INSERT**（两个独立操作）。检测到主键/唯一键冲突时，先把冲突的旧行物理删除，再把新行作为一条全新的记录插入。即使新数据和旧数据完全一样，旧行也被删除重建了。
- INSERT ... ON DUPLICATE KEY UPDATE = INSERT，冲突时转为原地 UPDATE。检测到冲突时不删行，直接在原行上执行 UPDATE 子句，只更新指定字段，未指定的字段（包括其他列）保持原值。
- 一个直观推论：REPLACE 会**丢弃旧行中未在语句里列出的字段值（这些列变回默认值）**，而 ON DUPLICATE KEY UPDATE 会保留它们。
1. **自增主键(AUTO_INCREMENT)**
- **REPLACE**: 删除旧行+插入新行,新插入会消耗一个自增值(即使语句里显示给了id=1,AUTO_INCREMENT计数器也可能被推进;若不显示给id,则生成全新id).旧id对应的行已不存在.
- **ON DUPLICATE KEY UPDATE**: 不插入新行,主键值不变,通常不推进自增计数器(注意: 在`innodb_autoinc_lock_mode`某些模式下,INSERT阶段已预分配 的自增值会被浪费,但行本身的id不变).
2. **触发器(Trigger)**
这是面试最常考的差异点:
- REPLACE：触发 DELETE 触发器（BEFORE/AFTER DELETE）+ INSERT 触发器（BEFORE/AFTER INSERT）。不会触发 UPDATE 触发器。
- ON DUPLICATE KEY UPDATE：触发 INSERT 相关的 BEFORE INSERT，冲突后触发 UPDATE 触发器（BEFORE/AFTER UPDATE）。不会触发 DELETE 触发器。

如果业务在 DELETE 触发器里做了审计、归档，REPLACE 会产生误导性的"删除"记录。

3. 外键（ON DELETE CASCADE）
- REPLACE：删除旧行时会触发 ON DELETE CASCADE，**级联删除子表中的关联行**！然后插入的新行与原来的子表数据已无关联。这在有子表的场景下是灾难性的——你以为只是更新一行，结果子表数据被连带清空。
- ON DUPLICATE KEY UPDATE：是 UPDATE，不触发 DELETE CASCADE，子表数据安全（若更新的是被引用的键，则按 ON UPDATE 规则走）。

三、追问：多个唯一键时的行为差异（如 id 主键 + UNIQUE(name)）
- 假设已有行 (1, 'Alice', 20)，执行 (2, 'Alice', 25)（id 不冲突，name 冲突）：
- REPLACE INTO：

按检测到的冲突删除旧行再插入。危险在于：如果新行同时与多条旧行冲突（例如 name 撞上 A 行、id 撞上 B 行），REPLACE 会把 **A、B 两行都删掉**，只插入一行新记录。一条语句净删除多行，很容易误伤数据。

冲突检测顺序依赖索引扫描顺序，多个唯一键冲突时删哪些行可能不符合直觉。

- ON DUPLICATE KEY UPDATE：

冲突时只更新命中的那一行（按第一个检测到的唯一键冲突定位）。但在多个唯一键的情况下，
**MySQL 官方明确警告**：它只对第一个冲突的唯一索引对应的行执行 UPDATE，行为相当于"定位到哪行算哪行"，如果新行与多条旧行分别在不同唯一键上冲突，更新其中一行后可能仍与另一行冲突，导致语句报错（duplicate key error），不会"合并"多行。

因此在多唯一键表上，两者都不优雅，但 REPLACE 的破坏性（可能删多行）远大于 ON DUPLICATE KEY UPDATE。

### 题目2：UPDATE 语句的锁行为与事务隔离级别（行锁 vs 间隙锁 vs 表锁）
> 在 MySQL 的 `READ-COMMITTED` 和 `REPEATABLE-READ` 隔离级别下，执行 `UPDATE employees SET salary = salary * 1.1 WHERE department_id = 10;`（`department_id` 为非唯一索引），分别会产生哪些锁？若 `department_id` 无索引，又会发生什么？追问：在 `REPEATABLE-READ` 下，若执行 `UPDATE` 时带范围条件（如 `WHERE id BETWEEN 10 AND 20`），会锁住哪些间隙？如何避免锁表导致业务阻塞？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `READ-COMMITTED`（RC）：
  - 使用**行锁**（Record Lock），仅锁定符合条件的行。
  - 若有二级索引，会锁住匹配的二级索引记录及对应的主键记录。
  - 无索引时，会对所有行加锁（全表扫描），但会逐行释放不匹配的行（减少锁冲突），但仍需扫描全表，容易产生死锁。
- `REPEATABLE-READ`（RR）：
  - 使用**行锁 + 间隙锁**（Gap Lock）锁定索引记录间的间隙，防止幻读。
  - 非唯一索引上，除了锁住匹配的行，还会锁住该索引键值前后的间隙（包含左开右开区间）。
  - 范围条件（如 `BETWEEN`）会锁定整个扫描到的范围间隙。
- 无索引时：两种隔离级别均会锁定整个表（实际是锁住所有聚簇索引记录），导致全表阻塞。
- 避免锁表策略：
  1. 确保 `WHERE` 条件使用**高选择性索引**，避免全表扫描。
  2. 分批更新（`LIMIT`）配合循环，降低单次锁范围。
  3. 在低峰期执行批量更新，或使用 `pt-archiver` 工具分块操作。
</details>

**我的初答**：
**错漏点**：


### 题目3：DELETE 大表数据的高效策略（分批删除与磁盘空间回收）
> 需要在 `orders` 表（500GB，含 `created_at` 索引）中删除 3 年前的数据（约 200GB）。直接执行 `DELETE FROM orders WHERE created_at < '2021-01-01';` 会存在哪些风险？请设计一种安全的批次删除方案，并说明如何避免长事务和锁问题。删除完成后，磁盘空间未释放，应如何回收？追问：若表上有外键约束（`ON DELETE CASCADE`），在分批删除时需注意什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 直接删除风险：
  1. 长事务：单个 `DELETE` 会生成大量 Undo 日志，占用磁盘且严重影响性能。
  2. 锁持有时间长：可能锁定大量行，阻塞业务。
  3. 主从延迟：大量 binlog 传输导致延迟。
- 批次删除方案（伪代码，使用缩进表示循环）：
  SET autocommit=0;
  LOOP:
  DELETE FROM orders WHERE created_at < '2021-01-01' LIMIT 10000;
  COMMIT;
  -- 可选：休眠一小段时间（如 1 秒）减少负载
  UNTIL ROW_COUNT() = 0;
  END LOOP;
  关键点：利用 `LIMIT` 控制每批删除行数，事务短，减少锁持有。
- 空间回收：`DELETE` 仅标记删除，数据页不释放。需执行 `OPTIMIZE TABLE orders;`（会重建表，释放碎片，但会锁表）或使用 `ALTER TABLE orders ENGINE=InnoDB;`（重建表）。也可使用 `pt-online-schema-change` 在线回收空间。
- 外键约束影响：若存在 `ON DELETE CASCADE`，删除父表数据会级联删除子表，可能导致大批量子表数据被删除。需先评估子表数据量，或暂时禁用外键检查（`SET FOREIGN_KEY_CHECKS=0`）并在维护窗口操作。
</details>

**我的初答**：
**错漏点**：

---

## Day 32 (2026-08-26) —— DML 与 DQL 条件查询、聚合函数

### 题目1：条件查询 + 聚合函数 + 分组统计（GROUP BY 与 HAVING 的应用）
> 现有员工表 `employees` 包含字段：`id`（主键）、`name`、`department_id`、`salary`、`hire_date`。请写出一个 SQL 查询，统计每个部门（`department_id`）中入职日期在 2020-01-01 之后的员工人数（不包括离职员工，假设 `status = 1` 表示在职），并计算这些员工的平均薪资和最高薪资，最终只显示平均薪资大于 8000 的部门，按平均薪资降序排列。请写出完整的 SQL 语句，并说明 `WHERE` 与 `HAVING` 在该查询中的作用。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- SQL 示例（缩进表示）：
  SELECT department_id,
  COUNT(*) AS emp_count,
  AVG(salary) AS avg_salary,
  MAX(salary) AS max_salary
  FROM employees
  WHERE hire_date >= '2020-01-01' AND status = 1
  GROUP BY department_id
  HAVING AVG(salary) > 8000
  ORDER BY avg_salary DESC;

- WHERE 与 HAVING 的区别：
  - WHERE：对**原始行**进行过滤（在分组前执行），用于剔除不符合条件的记录（如入职日期和状态）。
  - HAVING：对**分组后**的结果进行过滤（在 GROUP BY 之后执行），用于筛选满足聚合条件的分组（如平均薪资 > 8000）。
  - 注意：HAVING 中可以使用聚合函数，WHERE 中不能直接使用聚合函数。

- 关键点：`COUNT(*)` 统计每组的行数（包括 NULL），`AVG(salary)` 自动忽略 NULL 值。
- 性能提示：在 `hire_date` 和 `status` 上建立联合索引可加速 WHERE 过滤。
</details>

**我的初答**：
1. select avg(salary),max(salary) from employees where hire_date > 2020-01-01 group by status having status = 1 order by desc;
2. where用于分组前过滤日期是否符合条件,having用于分组后选择在职人员

**错漏点**：

**错误 1**：hire_date > 2020-01-01 —— 日期没加引号（最隐蔽的坑）

不加引号时，MySQL 把 2020-01-01 当成数学减法来算：2020 - 1 - 1 = 2018。于是你的条件实际变成了：

    WHERE hire_date > 2018   -- 日期和数字比较，2018 会被转成 '2018-00-00' 之类的值
结果不一定报错，但查出来的数据范围完全不对。**日期、字符串常量必须加引号**

**错误 2**：SELECT 和 GROUP BY 都漏了 department_id

题目要求“统计每个部门的……”，这意味着结果必须按部门区分，输出列里必须有部门：

    SELECT avg(salary), max(salary) ...     -- ❌ 只有聚合值，看不出哪行是哪个部门
    GROUP BY status                          -- ❌ 按在职状态分组了，而不是按部门
规则：用了 GROUP BY 后，SELECT 里的列要么是分组列，要么是聚合函数，不能有其他裸列。这里分组列应该是 department_id。

**错误 3**：status = 1 放错了地方——这是全题的核心考点

问题有两个：

1. status 是行级属性，不是组级属性。“是不是在职”是每一行数据自己的特征，应该在分组之前就把离职的行踢掉——这是 WHERE 的职责
2. 按你的写法，其实是把全表按 status 分成了“在职组”和“离职组”两个大组，再各算一遍平均薪资——和题目要的“每个部门”完全对不上

判断标准一句话：这个条件是“**某一行满不满足**”（→ WHERE），还是“一组数据**算完之后**满不满足”（→ HAVING）。“入职在 2020 后”是逐行判断，“平均薪资 > 8000”是组算完才能判断——这就自然引出两种过滤的分工。

**错误 4**：ORDER BY desc —— 排序依据不能省

降序关键字 DESC 只是修饰，告诉数据库“按什么降序”，那个“什么”不能少。而且排序依据应该是题目要求的“平均薪资”：

    ORDER BY AVG(salary) DESC          -- ✅ 用聚合函数
    ORDER BY 平均薪资 DESC             -- ✅ 或用别名（MySQL 支持）

**错误 5**：漏了“员工人数”

题目要求三项统计：人数、平均薪资、最高薪资，你只写了后两项，缺 COUNT(*)。


### 题目2：聚合函数对 NULL 的处理（COUNT、SUM、AVG 的行为差异）
> 现有成绩表 `scores`（`student_id`、`subject`、`score`），其中 `score` 列允许 NULL（表示缺考）。请回答以下问题：
> 1. `SELECT COUNT(*) FROM scores;` 与 `SELECT COUNT(score) FROM scores;` 返回的结果可能不同吗？为什么？
> 2. `SELECT SUM(score), AVG(score) FROM scores;` 若存在 NULL 值，SUM 和 AVG 会如何处理？是否会将 NULL 视为 0 参与计算？
> 3. 若需要计算所有学生的平均成绩（缺考记为 0），应如何编写 SQL？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 问题1：结果可能不同。
  - `COUNT(*)` 计算表的总行数（包括所有行，不论字段是否为 NULL）。
  - `COUNT(score)` 仅计算 `score` 列中非 NULL 的行数。
  - 因此，若存在缺考（NULL），两者结果不同。

- 问题2：SUM 和 AVG 会**忽略 NULL 值**（不参与计算）。
  - `SUM(score)` 将所有非 NULL 值求和，忽略 NULL。
  - `AVG(score)` 等于 `SUM(score) / COUNT(score)`（只计非 NULL 行数），而不是除以总行数。
  - NULL 不会被当作 0 处理。

- 问题3：将缺考视为 0 的平均分。
  - 方法1：使用 `COALESCE(score, 0)` 将 NULL 转为 0。
    SELECT AVG(COALESCE(score, 0)) FROM scores;
  - 方法2：使用 `SUM(COALESCE(score,0)) / COUNT(*)`。
- 注意：`COALESCE` 会改变 AVG 的分母，因为 AVG 函数忽略 NULL，但 COALESCE 将 NULL 变为非 NULL 值，所以 AVG 会将其计入。
</details>

**我的初答**：
**错漏点**：


### 题目3：条件查询中的逻辑运算符优先级与索引使用陷阱（AND、OR 的组合）
> 现有订单表 `orders` 含索引 `idx_status_date` 在 `(status, created_at)` 上。执行以下两条 SQL，哪个能够利用该索引？为什么？
> SQL A：`SELECT * FROM orders WHERE status = 1 AND created_at >= '2025-01-01';`
> SQL B：`SELECT * FROM orders WHERE status = 1 OR created_at >= '2025-01-01';`
> 请从索引的最左前缀匹配和 OR 条件的优化器策略角度分析。若必须使用 OR 且希望走索引，应如何改写（提示：使用 UNION）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 索引利用情况：
  - SQL A：能利用索引 `idx_status_date`。因为条件遵循最左前缀匹配：`status` 为等值，`created_at` 为范围，符合索引顺序。
  - SQL B：无法有效利用该索引。因为 `OR` 前后两个条件分别涉及不同字段（或同一字段但无法合并），优化器通常选择全表扫描（除非分别使用索引后做索引合并，但 MySQL 索引合并策略有限，且该索引可能不被选择）。
- 原因：
  - 联合索引 `(status, created_at)` 支持先按 `status` 查找，再在结果内按 `created_at` 筛选。
  - 对于 `OR`，若两个条件均可独立使用索引，MySQL 可能采用索引合并（Index Merge），但该功能有局限性，且此处索引在 `(status, created_at)` 上，对于 `created_at` 单独条件，索引无法直接使用（因为最左前缀缺失）。
- 改写建议（使用 UNION）：
  SELECT * FROM orders WHERE status = 1
  UNION
  SELECT * FROM orders WHERE created_at >= '2025-01-01';
  这样每个 SELECT 可利用各自的索引（status 可用联合索引，created_at 可能需要单独索引），然后合并结果（需去重，若用 UNION ALL 可提高效率）。
- 注意：实际优化器行为受版本和数据分布影响，可使用 `EXPLAIN` 查看执行计划。
</details>

**我的初答**：
**错漏点**：

---

## Day 33 (2026-08-27) —— DQL 多表连接、子查询与查询执行顺序

### 题目1：INNER JOIN 与 LEFT JOIN 的结果差异及 ON 与 WHERE 对 NULL 的影响
> 现有两张表：`customers`（客户 id, name）和 `orders`（订单 id, customer_id, amount）。请说明以下两种查询的结果差异：
> 查询A：`SELECT c.name, o.amount FROM customers c INNER JOIN orders o ON c.id = o.customer_id;`
> 查询B：`SELECT c.name, o.amount FROM customers c LEFT JOIN orders o ON c.id = o.customer_id;`
> 追问：若将 LEFT JOIN 中的过滤条件 `o.amount > 100` 分别放在 ON 子句和 WHERE 子句中，结果会有什么不同？请解释为什么。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询A（INNER JOIN）：仅返回**有订单**的客户及其订单金额，无订单的客户不出现在结果集中。
- 查询B（LEFT JOIN）：返回**所有客户**，若客户没有订单，则 `o.amount` 为 NULL。
- ON vs WHERE 对 LEFT JOIN 的影响：
  - 条件放在 ON 子句：`LEFT JOIN orders o ON c.id = o.customer_id AND o.amount > 100`。
    结果：保留所有客户，但只有满足 `amount > 100` 的订单会关联上，不满足或没有订单的客户仍会出现，但订单字段为 NULL。
  - 条件放在 WHERE 子句：`LEFT JOIN orders o ON c.id = o.customer_id WHERE o.amount > 100`。
    结果：因为 `o.amount > 100` 会将 NULL 行过滤掉，实际效果等同于 INNER JOIN（只会显示有金额 > 100 订单的客户）。
- 核心结论：ON 决定如何连接表（保留左表所有行），WHERE 对连接后的结果集进行过滤。LEFT JOIN 时，对右表的条件应优先放在 ON 子句中，除非你确实想过滤掉无匹配的行。
</details>

**我的初答**：


**错漏点**：


### 题目2：IN 与 EXISTS 的执行逻辑差异及性能选型
> 现有 `departments`（部门 id, name）和 `employees`（员工 id, dept_id, name）。请写出两条等价的 SQL，分别使用 `IN` 和 `EXISTS` 查询「有员工」的部门。
> 追问：若 `departments` 表数据量小（100 条）而 `employees` 表数据量极大（1000 万条），哪个子查询性能更优？若反过来（departments 大，employees 小）呢？请从子查询的执行时机（驱动表与外层循环）角度分析。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 使用 IN：
  SELECT * FROM departments
  WHERE id IN (SELECT DISTINCT dept_id FROM employees);
- 使用 EXISTS（关联子查询）：
  SELECT * FROM departments d
  WHERE EXISTS (SELECT 1 FROM employees e WHERE e.dept_id = d.id);
- 性能原理：
  - IN 子查询通常先执行内层查询，将结果集物化（生成临时表），再与外层查询比较。适用于内层结果集较小的场景（如 departments 小，employees 大，但 dept_id 去重后基数小）。
  - EXISTS 是**关联子查询**，每扫描外层一行，就执行一次内层查询（判断是否存在匹配）。适用于外层结果集小、内层查询能快速利用索引（如 employees 表有 dept_id 索引）的场景。
- 最佳实践：
  - 若子查询结果集较小且不重复，使用 IN（优化器可能将其转换为半连接）。
  - 若外层查询较小且内层表较大且有索引，使用 EXISTS 更优（可提前终止）。
  - 现代 MySQL 优化器会对两者做等价改写，但仍有偏差，建议使用 `EXPLAIN` 实测。
</details>

**我的初答**：
**错漏点**：


### 题目3：SQL 查询中各子句的执行顺序（FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT）
> 现有 SQL：`SELECT department_id, AVG(salary) AS avg_sal FROM employees WHERE hire_date > '2020-01-01' GROUP BY department_id HAVING avg_sal > 8000 ORDER BY avg_sal DESC LIMIT 5;`
> 请按实际执行顺序列出各子句的执行阶段，并解释为什么 `WHERE` 中不能使用列别名 `avg_sal`，而 `ORDER BY` 和 `HAVING` 中却可以使用？若在 `WHERE` 中尝试使用 `avg_sal`，数据库会报什么错？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 逻辑执行顺序（按编号排序）：
  1. FROM（确定数据源，含 JOIN）
  2. WHERE（过滤原始行）
  3. GROUP BY（分组）
  4. HAVING（过滤分组后的结果）
  5. SELECT（计算表达式、投影列、列别名）
  6. ORDER BY（排序）
  7. LIMIT（限制返回行数）

- 为何 WHERE 不能使用别名：
  列别名是在 SELECT 阶段才生成的，WHERE 在 SELECT 之前执行，因此 `WHERE avg_sal > 8000` 会报错 `Unknown column 'avg_sal'`。
- 为何 ORDER BY 和 HAVING 可以使用别名：
  - HAVING 在 SELECT 之后执行（但部分数据库如 MySQL 允许 HAVING 使用别名，因为它在 SELECT 之后处理）。
  - ORDER BY 在 SELECT 之后执行，此时别名已经生成，可直接引用。
- 注意：虽然 MySQL 允许 HAVING 使用别名，但标准 SQL 中应使用聚合函数或原始表达式以保证可移植性。
</details>

**我的初答**：
1. FROM employees
2. WHERE hire_date > '2020-01-01'
3. GROUP BY department_id HAVING avg_sal > 8000
4. SELECT department_id,AVG(salary) AS avg_sal
5. ORDER BY avg_sal DESC
6. LIMIT 5
7. 因为再执行where时还未执行SELECT,所以别名avg_sal还未出现,所以无法使用.而order by和having都在select之后执行,故可以使用.
8. 在where中尝试avg_sal数据库会报未找到avg_sal.

**错漏点**：

**HAVING 能用别名的原因**（这是你初答中需要修正的地方）：
按逻辑执行顺序，HAVING 其实在 SELECT **之前**执行，所以严格按 SQL 标准，HAVING 里也应该写 HAVING AVG(salary) > 8000。MySQL 做了扩展，允许 HAVING 直接引用 SELECT 中定义的别名，这是语法糖，并非因为它"在 SELECT 之后执行"。在 PostgreSQL、Oracle 等严格遵循标准的数据库中，HAVING avg_sal > 8000 同样会报错。

MySQL 会报：

ERROR 1054 (42S22): Unknown column 'avg_sal' in 'where clause'

即"where 子句中存在未知列 avg_sal"。其他数据库报错文案类似，如 PostgreSQL 报 column "avg_sal" does not exist。


---

## Day 34 (2026-08-28) —— DCL 与 MySQL 函数

### 题目1：DCL 中 GRANT 与 REVOKE 的权限层级及角色管理
> 在 MySQL 中，数据库管理员需要为新员工 `zhangsan` 创建一个账号，该账号仅能从本地（localhost）连接，并拥有对 `order_db` 数据库中所有表的 `SELECT`、`INSERT`、`UPDATE` 权限，但无权删除数据（`DELETE`）或修改表结构（`ALTER`）。请写出完整的 `CREATE USER` 和 `GRANT` 语句。追问：若后续需要收回该用户的 `UPDATE` 权限，应执行什么命令？若该用户拥有 `WITH GRANT OPTION` 权限，存在什么安全风险？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 创建用户及授权语句（缩进表示）：
  CREATE USER 'zhangsan'@'localhost' IDENTIFIED BY 'SecurePass123';
  GRANT SELECT, INSERT, UPDATE ON order_db.* TO 'zhangsan'@'localhost';
- 权限层级说明：
  - 全局层级（`*.*`）：覆盖所有数据库。
  - 数据库层级（`order_db.*`）：覆盖指定数据库的所有表。
  - 表层级（`order_db.orders`）：覆盖指定表。
  - 列层级：可针对特定列授权（如 `UPDATE (name)`），但生产环境较少使用。
- 回收权限：
  REVOKE UPDATE ON order_db.* FROM 'zhangsan'@'localhost';
- `WITH GRANT OPTION` 风险：该权限允许用户将自己拥有的权限授予其他用户，可能导致权限扩散，违背最小权限原则。除非有明确委派需求，否则应禁止。
- 注意：修改权限后需执行 `FLUSH PRIVILEGES;`（但在 MySQL 中，GRANT/REVOKE 会自动刷新，通常不需要）。
</details>

**我的初答**：
1. create user 'zhangsan'@'localhost' identified by '123456';
2. grant select,insert,update on order_db@* to 'zhangsan'@'localhost';
3. revoke update on order_db@* from 'zhangsan'@'localhost';
4. 若该用户拥有with grant option 可能会给与他人权限,然后他人又赋予另外人权限.这样会导致管理员不知道到底谁拥有权限,造成权限管理混乱

**错漏点**：
- **你初答中的错误**：`order_db@*` 应为 `order_db.*`（点号，不是 @）。
- @ 用于分隔用户名和主机：'zhangsan'@'localhost'
- 权限对象（库.表）之间用 .：order_db.* 表示 order_db 库下的所有表 
- 这种写法在 MySQL 中会直接报语法错误。

三、追问 2：WITH GRANT OPTION 的安全风险
- `WITH GRANT OPTION` 允许被授权者把自己拥有的权限**再授予其他用户**。你的回答抓住了核心（权限扩散、管理混乱），可以更结构化地表述：
1. **权限失控扩散**：zhangsan 可以把 SELECT/INSERT/UPDATE 转授给任意其他账号，甚至对方再转授给第三人，形成链式扩散，管理员无法准确掌握"到底谁能访问数据"。
2. **审计困难**：权限来源链变长，出现数据泄露时难以追溯责任主体。
3. **违背最小权限原则**：DBA 原本只想给 zhangsan 一个人授权，GRANT OPTION 实际上让"授权决策权"也外包了出去。
4. **级联回收问题**：回收权限时如果带 `CASCADE` 语义，可能连带影响一串下游用户，引发业务故障（MySQL 的 REVOKE 不级联，但链条本身仍存在）。
  
实践建议：生产环境几乎不应给普通业务账号授予 WITH GRANT OPTION，授权操作应只由 DBA 集中执行。


### 题目2：字符串函数中 CHAR_LENGTH 与 LENGTH 的编码差异
> 现有表 `articles` 含字段 `title VARCHAR(100)`，存储字符串 `'你好MySQL'`（UTF-8 编码）。执行 `SELECT CHAR_LENGTH(title), LENGTH(title) FROM articles;` 分别返回什么结果？为什么？若想按字节长度截断字符串（如保留前 6 个字节），应使用哪个函数？在定义 `VARCHAR` 字段时，`VARCHAR(100)` 中的 100 是指字符数还是字节数？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 结果差异：
  - `CHAR_LENGTH(title)`：返回**字符数**，结果为 7（'你'、'好'、'M'、'y'、'S'、'Q'、'L'）。
  - `LENGTH(title)`：返回**字节数**。UTF-8 中，中文占 3 字节（'你'和'好'各 3 字节），英文字母和数字占 1 字节，总字节数为 3+3+5=11。
- 按字节截断函数：
  使用 `SUBSTRING(title, 1, 6)` 是按字符截断，无法精确按字节。若需按字节截断，应使用 `LEFT(title, 6)` 也是按字符，不适用。MySQL 没有直接按字节截取字符串的内置函数（除非转换字符集），通常需在应用层处理。若必须，可先用 `CONVERT(title USING latin1)` 强制转换再截取（但会乱码）。
- VARCHAR(100) 含义：
  在 MySQL 5.0+ 中，`VARCHAR(100)` 表示最多存储 **100 个字符**（而非字节），无论是英文、中文还是其他多字节字符，均按字符数计数。但实际存储时受单行最大 65535 字节的限制。
</details>

**我的初答**：
**错漏点**：


### 题目3：日期函数 DATEDIFF、DATE_ADD 与 TIMESTAMPDIFF 的实战应用
> 在用户成长值系统中，需要计算用户的「会员等级有效期」是否在 30 天内到期，并查询注册满 90 天的用户名单。现有 `users` 表，含 `registration_date`（注册日期）和 `vip_expiry_date`（会员到期日）。请写出以下查询：
> 1. 查询所有会员将在 30 天内到期的用户（包括已过期的）。
> 2. 查询注册时间恰好满 90 天的用户（精确到日）。
> 3. 解释 `DATEDIFF`、`TIMESTAMPDIFF` 与 `DATE_ADD` 在计算日期差值时的应用场景差异。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询1（会员在 30 天内到期，含已过期但未超过30天）：
  SELECT * FROM users
  WHERE DATEDIFF(vip_expiry_date, CURDATE()) BETWEEN 0 AND 30;
  或使用 TIMESTAMPDIFF：
  WHERE TIMESTAMPDIFF(DAY, CURDATE(), vip_expiry_date) BETWEEN 0 AND 30;
  注意：`DATEDIFF` 返回天数差（忽略时间部分），`CURDATE()` 返回当前日期。
- 查询2（注册满 90 天）：
  SELECT * FROM users
  WHERE DATEDIFF(CURDATE(), registration_date) = 90;
  或 `DATE_ADD(registration_date, INTERVAL 90 DAY) = CURDATE()`。
- 函数差异：
  - `DATEDIFF(end, start)`：仅返回天数差（整数），忽略时间。
  - `TIMESTAMPDIFF(unit, start, end)`：支持更精细的单位（SECOND、MINUTE、HOUR、DAY、MONTH、YEAR），且结果可正可负，适用于跨月/跨年精确计算（如年龄计算）。
  - `DATE_ADD(date, INTERVAL expr unit)`：用于日期增加/减少，常用于生成未来时间点。
- 提示：计算会员是否过期，建议统一使用 `vip_expiry_date < CURDATE()` 判断已过期，避免 BETWEEN 带来的边界问题。
</details>

**我的初答**：
**错漏点**：

---

## Day 35 (2026-08-29) —— MySQL 约束（主键、外键、唯一键、检查约束、默认值）

### 题目1：主键、唯一键、外键在数据完整性与底层索引上的本质差异
> 请从以下三个维度对比 `PRIMARY KEY`、`UNIQUE KEY` 和 `FOREIGN KEY` 约束：
> 1. 数据完整性保证（是否允许 NULL、是否允许重复、是否级联操作）。
> 2. 底层索引类型（聚集索引 vs 辅助索引，对查询性能的影响）。
> 3. 对外键约束的级联操作（`ON DELETE CASCADE` / `ON UPDATE CASCADE`）在实际生产环境中的风险。追问：为什么 InnoDB 强制要求外键列上必须有索引？若没有索引，InnoDB 会自动创建吗？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 数据完整性对比：
  - PRIMARY KEY：唯一且非空，每表仅一个。保证实体完整性。
  - UNIQUE KEY：唯一但允许 NULL（可多个 NULL），每表可多个。保证唯一性约束。
  - FOREIGN KEY：引用父表主键或唯一键，保证参照完整性。插入/更新子表时会检查父表是否存在对应值。
- 底层索引差异：
  - PRIMARY KEY：InnoDB 自动生成**聚集索引**，叶子节点存储完整行数据，表数据按主键顺序存储。查询最快（无需回表）。
  - UNIQUE KEY：生成**辅助索引**，叶子节点仅存储主键值。查询时需回表获取完整行（除非覆盖索引）。
  - FOREIGN KEY：InnoDB 不会自动创建索引，但强烈建议手动创建。若外键列无索引，删除父表行时会锁全表（因无法快速定位子表记录）。
- 外键级联操作风险（`ON DELETE CASCADE`）：
  删除父表行会**自动删除子表中所有引用该行的记录**，若误操作（如无 WHERE 条件的 DELETE）会导致大量子表数据丢失，极难恢复。生产环境通常**禁用外键级联**，由应用层逻辑管理。
- 外键索引追问：InnoDB **不会自动创建**外键列索引。但官方文档明确建议手动添加，否则在父表更新/删除时会触发全表扫描，严重拖累性能，甚至导致死锁。
</details>

**我的初答**：
1. PRIMARY KEY 

**错漏点**：


### 题目2：CHECK 约束在 MySQL 不同版本中的生效差异及替代方案（ENUM 的陷阱）
> 在 MySQL 5.7 中执行 `CREATE TABLE employee (id INT, age INT, CHECK (age >= 18));` 后，插入 `age=16` 的记录会被阻止吗？在 MySQL 8.0 中呢？若业务需要在 5.7 中强制年龄大于等于 18，除了应用层校验，还有哪些数据库层方案（如触发器）？若使用 `ENUM('M','F')` 约束性别，插入 'X' 在严格模式和非严格模式下分别会发生什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 版本差异：
  - MySQL 5.7 及之前：`CHECK` 约束**被解析但完全忽略**，不生效。插入 `age=16` 不会报错，数据成功写入。
  - MySQL 8.0+：`CHECK` 约束**默认生效**，插入 `age=16` 会报错 `Check constraint 'employee_chk_1' is violated.`
- 5.7 替代方案（数据库层强制）：
  1. 使用 **触发器**（`BEFORE INSERT` 和 `BEFORE UPDATE`）检查年龄，若 < 18 则通过 `SIGNAL SQLSTATE '45000'` 抛出异常，阻止操作。
  2. 使用 **ENUM 枚举**或 **SET** 类型（但仅适用于离散值，不适用于范围）。
  3. **存储过程**封装 DML 操作（业务强制调用过程）。
- ENUM 插入非法值行为：
  - **严格模式**（`STRICT_TRANS_TABLES` 或 `STRICT_ALL_TABLES`）：插入 'X' 直接报错，语句回滚。
  - **非严格模式**：插入 'X' 产生警告，但数据被截断为**空字符串**（`''`），导致数据不一致（无法区分合法空值 vs 非法值）。
- 最佳实践：升级到 MySQL 8.0 或使用触发器 + 严格模式；ENUM 已被广泛视为反模式，推荐使用 `CHECK`（8.0）或外键字典表。
</details>

**我的初答**：
**错漏点**：


### 题目3：NOT NULL 与 DEFAULT 的组合陷阱、自增字段的显式插入行为
> 现有表 `product`：`id INT PRIMARY KEY AUTO_INCREMENT`，`name VARCHAR(50) NOT NULL DEFAULT '未命名'`，`price DECIMAL(10,2) NOT NULL`。
> 1. 执行 `INSERT INTO product (name) VALUES (NULL);` 会发生什么？为什么？
> 2. 执行 `INSERT INTO product (price) VALUES (19.99);` 后，`id` 和 `name` 分别被赋予什么值？
> 3. 执行 `INSERT INTO product (id, name, price) VALUES (100, '键盘', 99.00);` 后，下一次 `AUTO_INCREMENT` 从多少开始？若再次插入不指定 id 的行，新 id 是 101 还是其他值？
     > 追问：若将 `id` 设为 `AUTO_INCREMENT` 且未指定值，但显式插入 `id = 0`，InnoDB 会如何处理（与 `sql_mode` 中的 `NO_AUTO_VALUE_ON_ZERO` 有关）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 问题1：插入失败，抛出 `Column 'name' cannot be null`。因为 `name` 字段有 `NOT NULL` 约束，显式插入 NULL 会违反约束（即使有 DEFAULT 值，DEFAULT 仅在**未指定该列**时生效，而非插入 NULL 时）。
- 问题2：`id` 自动生成为下一个自增值（如 1），`name` 使用 DEFAULT 值 `'未命名'`，`price` 为 19.99。INSERT 语句仅指定 `price`，未指定 `id` 和 `name`，因此它们各自使用 DEFAULT（AUTO_INCREMENT 生成自增 ID，字符串使用默认值）。
- 问题3：显式插入 `id=100` 后，InnoDB 的自增计数器会更新为 `max(id) + 1 = 101`。下次不指定 id 的插入，新 id 为 101（而非从 1 重新开始）。
- 关于插入 `id = 0` 的行为（受 `NO_AUTO_VALUE_ON_ZERO` 控制）：
  - 默认情况（`NO_AUTO_VALUE_ON_ZERO` 未设置）：插入 `id=0` 会被视为“未指定”，自动生成新自增值。
  - 若启用 `sql_mode='NO_AUTO_VALUE_ON_ZERO'`，插入 `id=0` 会**显式写入 0**（不触发自增），可用于保留特定占位值。
- 最佳实践：显式插入自增列时，务必确保该值大于当前最大值，否则可能引发主键冲突或自增计数器异常。
</details>

**我的初答**：
**错漏点**：

---

## Day 36 (2026-08-30) —— MySQL 多表查询（连接查询、子查询、联合查询）

### 题目1：INNER JOIN、LEFT JOIN 与 RIGHT JOIN 的结果集差异及 ON 与 WHERE 对 NULL 的过滤时机
> 现有两张表：`students`（学生 id, name）和 `scores`（成绩 id, student_id, subject, score）。请回答以下问题：
> 1. 查询所有学生及其成绩（包括无成绩的学生），应使用哪种连接？若使用 INNER JOIN，结果会缺少什么？
> 2. 若查询语句为：`SELECT s.name, sc.score FROM students s LEFT JOIN scores sc ON s.id = sc.student_id AND sc.subject = 'Math';` 与 `SELECT s.name, sc.score FROM students s LEFT JOIN scores sc ON s.id = sc.student_id WHERE sc.subject = 'Math';` 结果有何不同？为什么？
> 3. RIGHT JOIN 在什么场景下会优于 LEFT JOIN？请举例说明。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 问题1：应使用 **LEFT JOIN**（或 RIGHT JOIN），以保留左表（students）所有行。INNER JOIN 仅返回有成绩匹配的学生，无成绩的学生会被排除。
- 问题2：
  - 第一个查询（条件在 ON）：返回所有学生，若学生有 Math 成绩则显示分数，否则分数为 NULL。其他科目成绩不会干扰结果。
  - 第二个查询（条件在 WHERE）：先进行 LEFT JOIN 连接所有成绩，然后 WHERE 过滤 `subject = 'Math'`，这会**将无成绩的学生（分数为 NULL）也一并过滤掉**，结果等同于 INNER JOIN（仅显示有 Math 成绩的学生）。
  - 核心：ON 决定连接方式（保留左表全部行），WHERE 对连接后的结果集进行筛选。
- 问题3：RIGHT JOIN 在需要保留右表全部行时使用，但通常可通过调换表的顺序用 LEFT JOIN 实现相同效果，因此 RIGHT JOIN 使用较少。例如，查询所有科目及其有成绩的学生，若科目表在右，可用 RIGHT JOIN。
</details>

**我的初答**：
1. student左外连接scores表,若使用INNER JOIN 会缺失无scores成绩的学生信息.
2. ON..AND是同时满足这两个条件时才合并为一个表.
3. ON..where是先满足ON就合并,之后通过where筛选出sc.subject的行.
4. 不了解,right join与left join将两个表位置换一下后不是一样的吗?

**错漏点**：
1. 第一个语句对于'Math'未考的学生会依照LEFT JOIN的规则保留下来
2. WHERE 不关心 LEFT JOIN 的“保留承诺”，它只留下条件为 TRUE 的行。所以语句 B 实际上退化成了 **INNER JOIN**——这是外连接最常见的坑。
   
- ① 连接阶段（ON 起作用）
- 决定"右表哪一行能配上来"
- LEFT JOIN 承诺：配不上的左表行也保留（右表补 NULL）

- ② 过滤阶段（WHERE 起作用）
- 对连接结果逐行筛选
- 李四、王五的 sc.subject 是 NULL
- NULL = 'Math' → 结果是 UNKNOWN → 行被 WHERE 丢弃 ❌

一句话口诀

ON 决定“配不配得上”，WHERE 决定“留不留得下”。

对 LEFT JOIN 而言：右表的条件放 ON → 保留全部左表行；放 WHERE → 无匹配的左表行也被删光。

（顺带一提：左表的条件写在哪都一样会过滤，因为 WHERE 过滤左表列不会误伤 NULL 行——只有右表列有这个坑。）

对于RIGHT JOIN

|维度|	结论|
|---|---|
|功能|	完全等价，可互相转换|
|优于 LEFT JOIN 的场景|	主表在右 / 避免重排已有 SQL 时书写更顺手|
|工程实践|	通常仍推荐统一 LEFT JOIN，保证可读性|


### 题目2：自连接（Self Join）的应用场景——查询员工及其经理
> 现有 `employees` 表：`id`、`name`、`manager_id`（经理的员工 id，若为 NULL 表示顶级领导）。请写出 SQL 查询，返回每个员工的姓名及其经理的姓名，包括没有经理的员工（显示为 "无经理"）。若需查询所有经理及其下属人数，该如何编写？追问：自连接与非关联子查询在性能上有何差异？哪种更优？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询员工及经理姓名（含无经理）：
  SELECT e.name AS employee,
  COALESCE(m.name, '无经理') AS manager
  FROM employees e
  LEFT JOIN employees m ON e.manager_id = m.id;
  使用 LEFT JOIN 保留所有员工，经理表 m 可能无匹配（manager_id 为 NULL），用 COALESCE 替换为 '无经理'。
- 查询各经理及其下属人数：
  SELECT m.name AS manager, COUNT(e.id) AS subordinates
  FROM employees m
  INNER JOIN employees e ON m.id = e.manager_id
  GROUP BY m.id, m.name;
  注意：若经理无下属，则不会出现在此结果中（可使用 LEFT JOIN 和 COALESCE(COUNT(e.id), 0) 显示 0）。
- 自连接 vs 子查询性能：
  - 自连接（JOIN）通常比子查询（如标量子查询）更高效，因为数据库优化器可更灵活地选择驱动表和索引。
  - 对于查询每个员工的经理姓名，自连接只需一次表扫描，而子查询可能逐行执行多次。因此优先使用自连接。
</details>

**我的初答**：
**错漏点**：


### 题目3：UNION 与 UNION ALL 的区别及对结果排序的影响
> 现有两张结构相同的表：`orders_2023` 和 `orders_2024`，均含 `id`、`order_date`、`amount`。需要查询两年所有订单并按日期降序排列，请写出两种写法（分别使用 UNION 和 UNION ALL），并说明两者在去重、性能、以及最终排序上的差异。若两个表中有完全相同的记录（id、日期、金额均相同），UNION 和 UNION ALL 的结果会有什么不同？追问：若需要对最终结果去重，但希望保留重复记录中的某一条，应如何实现？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 使用 UNION（去重）：
  SELECT id, order_date, amount FROM orders_2023
  UNION
  SELECT id, order_date, amount FROM orders_2024
  ORDER BY order_date DESC;
- 使用 UNION ALL（保留全部，效率更高）：
  SELECT id, order_date, amount FROM orders_2023
  UNION ALL
  SELECT id, order_date, amount FROM orders_2024
  ORDER BY order_date DESC;
- 差异：
  - 去重：UNION 会去除完全相同的行（所有字段均相同）；UNION ALL 保留所有行。
  - 性能：UNION ALL 不执行去重操作，速度更快，内存占用更少。
  - 排序：两者均可在最后整体排序，但若对每个子查询单独排序再 UNION，会多一次排序开销，通常不推荐。
- 保留一条重复记录的方法：若需去重但保留指定字段（如保留最新 id），可先使用 `ROW_NUMBER()` 窗口函数（MySQL 8.0+）分组排序，再 UNION ALL 合并。
  示例（仅保留 2023 和 2024 各自最新的一条）：
  WITH combined AS (
  SELECT *, 1 AS year FROM orders_2023
  UNION ALL
  SELECT *, 2 AS year FROM orders_2024
  )
  SELECT id, order_date, amount FROM combined
  WHERE (year, order_date) IN (SELECT year, MAX(order_date) FROM combined GROUP BY year);
  （此方法较复杂，实际常用 GROUP BY + MAX 或临时表）
- 注意：MySQL 8.0 之前不支持窗口函数，可用子查询替代。
</details>

**我的初答**：
**错漏点**：

---

## Day 37 (2026-08-31) —— MySQL 综合复习（DQL、约束、事务）

### 题目1：多表连接 + 聚合函数 + 子查询的嵌套应用（统计各部门最高薪资员工信息）
> 现有三张表：`departments`（部门 id, name）、`employees`（员工 id, name, salary, dept_id, hire_date）、`salary_changes`（员工 id, change_date, new_salary，记录每次调薪）。请写出以下 SQL 查询：
> 1. 查询每个部门中薪资最高的员工姓名及薪资，并按部门名称排序。
> 2. 在上一个查询结果中，额外显示该员工的薪资相较于其入职时的初始薪资（即 `salary_changes` 表中该员工最早的记录）的增长率（百分比），若没有调薪记录则显示为 0。
> 3. 仅显示增长率大于 20% 的部门及员工信息。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询1（使用子查询或窗口函数）：
  使用子查询方式（兼容 5.7）：
  SELECT d.name AS dept_name, e.name AS emp_name, e.salary
  FROM employees e
  JOIN departments d ON e.dept_id = d.id
  WHERE (e.dept_id, e.salary) IN (
  SELECT dept_id, MAX(salary) FROM employees GROUP BY dept_id
  )
  ORDER BY d.name;

  使用窗口函数方式（8.0+）：
  SELECT d.name, e.name, e.salary
  FROM (
  SELECT *, RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rk
  FROM employees
  ) e
  JOIN departments d ON e.dept_id = d.id
  WHERE rk = 1
  ORDER BY d.name;

- 查询2（加入调薪增长率计算）：
  WITH max_sal_emp AS (
  SELECT e.id, e.name, e.salary, e.dept_id
  FROM employees e
  WHERE (e.dept_id, e.salary) IN (
  SELECT dept_id, MAX(salary) FROM employees GROUP BY dept_id
  )
  ),
  init_sal AS (
  SELECT id, new_salary AS init_salary
  FROM (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY id ORDER BY change_date) AS rn
  FROM salary_changes
  ) t WHERE rn = 1
  )
  SELECT d.name AS dept_name,
  m.name AS emp_name,
  m.salary AS current_salary,
  COALESCE(i.init_salary, m.salary) AS init_salary,
  ROUND((m.salary - COALESCE(i.init_salary, m.salary)) / COALESCE(i.init_salary, m.salary) * 100, 2) AS growth_rate
  FROM max_sal_emp m
  JOIN departments d ON m.dept_id = d.id
  LEFT JOIN init_sal i ON m.id = i.id;

- 查询3（过滤增长率 > 20%）：
  在前一个查询外层加 `WHERE growth_rate > 20` 即可。若增长率因除数 NULL 产生异常，需用 COALESCE 处理。
- 注意：未调薪员工增长率视为 0（因 init_salary = current_salary）。
</details>

**我的初答**：
**错漏点**：


### 题目2：约束综合设计（包含 PRIMARY KEY、FOREIGN KEY、UNIQUE、CHECK 及 ON DELETE 策略）
> 设计一个电商数据库订单模块，包含 `customers`（客户表）和 `orders`（订单表），满足以下要求：
> 1. `customers` 表：id 主键自增，name 非空，email 唯一且非空，age 要求 >= 18。
> 2. `orders` 表：id 主键自增，customer_id 外键引用 customers.id，order_date 默认当前日期，total_amount 必须大于 0。
> 3. 当删除客户时，若该客户有订单则禁止删除（`RESTRICT`）。
> 4. 若修改客户 id，自动更新订单表中的 customer_id（`ON UPDATE CASCADE`）。
     > 请写出完整的建表语句（MySQL 8.0 语法）。追问：若使用 MySQL 5.7，`CHECK` 约束不生效，应如何改造以保证 age >= 18？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 建表语句（MySQL 8.0）：
  CREATE TABLE customers (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  age INT CHECK (age >= 18)
  );

  CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  customer_id INT NOT NULL,
  order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  total_amount DECIMAL(10,2) CHECK (total_amount > 0),
  CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id)
  REFERENCES customers(id)
  ON DELETE RESTRICT
  ON UPDATE CASCADE
  );

- 5.7 替代方案（触发器模拟 CHECK）：
  DELIMITER //
  CREATE TRIGGER check_customer_age BEFORE INSERT ON customers
  FOR EACH ROW
  BEGIN
  IF NEW.age < 18 THEN
  SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Age must be at least 18';
  END IF;
  END;//
  DELIMITER ;
  同时需要创建 `BEFORE UPDATE` 触发器覆盖更新场景。

- 级联行为解析：
  - `ON DELETE RESTRICT`：阻止删除被引用的父行（默认行为），若必须先删除子表再删父表。
  - `ON UPDATE CASCADE`：父表 id 更新时，子表外键自动同步，避免手动维护。
- 注意：外键列 `customer_id` 必须添加索引（`CREATE INDEX idx_orders_customer ON orders(customer_id);`），否则删除父表时会锁全表。
</details>

**我的初答**：
**错漏点**：


### 题目3：事务隔离级别下的 UPDATE 丢失更新与间隙锁场景（RR vs RC）
> 现有 `products` 表：`id` 主键，`stock` 库存字段，`category` 分类字段（普通索引）。在以下场景中，分析 `REPEATABLE-READ` 和 `READ-COMMITTED` 隔离级别的表现：
> 1. 事务 A：`UPDATE products SET stock = stock - 1 WHERE category = 'electronics';` 假设该分类有 100 条记录。
> 2. 事务 B（同时执行）：`UPDATE products SET stock = stock - 1 WHERE category = 'electronics';`
     > 请问两个隔离级别下，事务 A 和 B 分别会锁定哪些行？是否存在间隙锁？若 `category` 列无索引，又会发生什么？如何避免这种锁冲突导致的业务阻塞？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- RR 隔离级别：
  - 有索引（`category` 普通索引）：事务 A 会锁定所有匹配 `category='electronics'` 的二级索引记录和对应的主键行，同时**在索引间隙加间隙锁**（Gap Lock），防止其他事务插入新的 `electronics` 记录（防幻读）。事务 B 将被阻塞，直到 A 提交。
  - 无索引：事务 A 会对全表所有行加锁（聚簇索引全部加锁），且间隙锁锁定全表间隙，事务 B 完全阻塞，甚至无法插入任何其他分类的记录。
- RC 隔离级别：
  - 有索引：事务 A 仅锁定匹配的二级索引记录和主键行（**无间隙锁**）。事务 B 可以正常执行（不会阻塞，因为两行互不冲突），但可能导致更新丢失（若 A 和 B 同时读取同一行并写回）需配合乐观锁或 `SELECT ... FOR UPDATE`
  - 无索引：事务 A 会扫描全表，对每一行加行锁，但会逐行释放不匹配的行，最终只锁定匹配的行，但扫描期间仍会产生大量锁，容易死锁。
- 避免阻塞策略：
  1. 确保 `WHERE` 条件使用高选择性索引，减少锁范围。
  2. 使用 `READ-COMMITTED` 配合显式行锁（`FOR UPDATE`）和版本号（乐观锁）避免更新丢失。
  3. 分批更新（`LIMIT`）降低单次锁持有时间。
</details>

**我的初答**：
**错漏点**：

---

## Day 38 (2026-09-01) —— MySQL 事务（ACID、隔离级别、并发问题）

### 题目1：事务的 ACID 特性及其在 MySQL 中的实现机制
> 请解释事务的四大特性（原子性、一致性、隔离性、持久性）分别由 MySQL 的哪些机制保证？追问：`redo log` 和 `undo log` 分别服务于哪个特性？若事务提交后数据库突然宕机，重启后如何保证已提交事务的修改不丢失？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 原子性（Atomicity）：由 **undo log** 保证。事务执行过程中记录修改前的旧值，若事务回滚，通过 undo log 将数据恢复至修改前的状态。
- 一致性（Consistency）：由 **应用层逻辑 + 数据库约束**（主键、外键、唯一键、CHECK 等）共同保证。事务执行前后，数据库状态必须满足所有约束和业务规则。
- 隔离性（Isolation）：由 **锁机制 + MVCC（多版本并发控制）** 保证。不同隔离级别通过不同的锁策略和快照读机制控制并发事务间的可见性。
- 持久性（Durability）：由 **redo log** 保证。事务提交时，修改先写入 redo log（磁盘顺序写），再异步刷新到数据页。即使宕机，重启时通过 redo log 重做已提交事务的修改，确保数据不丢失。
- redo log 与 undo log 定位：
  - redo log（重做日志）：服务持久性，记录物理修改（如“将页 X 的偏移 Y 处的值改为 Z”），用于宕机恢复。
  - undo log（回滚日志）：服务原子性和 MVCC，记录逻辑修改（如“将某行的某字段改回旧值”），用于回滚和提供一致性读视图。
</details>

**我的初答**：
1. 原子性: 由事务的commit和rollback保证.
2. 一致性: 不了解
3. 隔离性: 不了解
4. 持久性: 通过将表中的内容存放到文件中,以达成持久化
5. 不了解

**错漏点**：
1. ACID 各自的实现机制

| 特性                  | 含义                     | MySQL（InnoDB）实现机制                                                                          |
| ------------------- | ---------------------- | ------------------------------------------------------------------------------------------ |
| **原子性** Atomicity   | 事务中的操作要么全部成功，要么全部失败回滚  | **undo log（回滚日志）**：每次修改前记录旧值，回滚时用 undo log 逆向恢复                                            |
| **一致性** Consistency | 事务前后数据满足完整性约束（如转账总额不变） | 是**目标而非单一机制**：由 A、I、D 三者共同保障 + 数据库约束（主键、外键、唯一键、CHECK）+ 应用层业务逻辑                             |
| **隔离性** Isolation   | 并发事务互不干扰               | **MVCC（多版本并发控制）** + **锁机制**（行锁、间隙锁、next-key lock）+ 四种隔离级别（读未提交/读已提交/可重复读/串行化，InnoDB 默认 RR） |
| **持久性** Durability  | 已提交的事务，修改永久生效，宕机不丢     | **redo log（重做日志）** + WAL（Write-Ahead Logging，日志先行）                                         |
2. redo log 和 undo log 分别服务哪个特性
- undo log → 原子性：记录"修改前的样子"，事务回滚或崩溃恢复时用来撤销未提交的修改。（它同时也是 MVCC 读旧版本数据的来源。）
- redo log → 持久性：记录"修改后的物理变更"，保证已提交事务的修改在宕机后可以重放恢复。

3. 追问 2：**提交后宕机，重启如何保证不丢**

核心是 **redo log + WAL 机制**：

1.事务提交时，InnoDB **不要求立刻把数据页刷盘**（那样随机 I/O 太慢），而是先把修改记录写入 **redo log 并刷盘**（顺序写，很快），然后才返回提交成功。这就是"日志先行 WAL"。

2.宕机后内存中脏页丢失没关系——重启时 InnoDB 执行 **崩溃恢复**（crash recovery）：扫描 redo log，把已提交事务的修改**重放**（redo/replay）到数据页，数据恢复。

3.同时配合 **undo log**：对宕机时未提交的事务进行回滚，保证原子性。

4.补充（加分项）：与 binlog 之间通过**两阶段提交**（2PC）保证 redo log 和 binlog 一致，避免主从数据不一致。


### 题目2：事务并发带来的三大问题（脏读、不可重复读、幻读）及隔离级别的解决程度
> 请分别说明脏读、不可重复读、幻读的现象及本质区别。在 MySQL InnoDB 的 `READ UNCOMMITTED`、`READ COMMITTED`、`REPEATABLE READ`、`SERIALIZABLE` 四种隔离级别下，哪些问题被解决？追问：`REPEATABLE READ` 如何通过 MVCC 解决不可重复读？它是否完全解决了幻读？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 脏读（Dirty Read）：事务 A 读到事务 B 未提交的修改，若 B 回滚，A 读到的数据就是无效的。本质是**读到了未提交的脏数据**。
- 不可重复读（Non-Repeatable Read）：事务 A 内两次读取同一行数据，因事务 B 的更新并提交，导致两次结果不同。本质是**同一行数据被其他事务修改**。
- 幻读（Phantom Read）：事务 A 内两次范围查询，因事务 B 插入了符合条件的新行，导致结果集行数不同。本质是**其他事务插入了新行**。
- 各隔离级别解决情况（MySQL InnoDB）：
  - READ UNCOMMITTED：三种问题均未解决。
  - READ COMMITTED：防止脏读（读已提交），但仍有不可重复读和幻读。
  - REPEATABLE READ（MySQL 默认）：防止脏读和不可重复读（通过 MVCC 快照读），但 **幻读未完全解决**（快照读无幻读，但当前读（如 `SELECT ... FOR UPDATE`）仍需加间隙锁防止幻读）。
  - SERIALIZABLE：三种问题全部解决（通过强制串行执行或锁表）。
- RR 解决不可重复读的机制：基于 MVCC，事务开启时生成一个 `ReadView`（活跃事务列表），查询时只读取 `ReadView` 创建前已提交的数据版本，后续其他事务的提交不影响当前事务的快照。
- RR 对幻读的处理：快照读（普通 SELECT）无幻读；当前读（`SELECT ... FOR UPDATE` / `UPDATE` / `DELETE`）通过 **间隙锁（Gap Lock）** 锁定索引记录间的间隙，阻止其他事务插入新行，从而避免幻读。但严格来说，RR 并不完全符合 ANSI SQL 标准中的“完全解决幻读”（因快照读和当前读并存）。
</details>

**我的初答**：
**错漏点**：


### 题目3：事务隔离级别与锁机制的关系——当前读 vs 快照读
> 在 MySQL 的 `READ COMMITTED` 和 `REPEATABLE READ` 隔离级别下，执行以下操作时，分别使用什么类型的读（快照读或当前读），以及加锁情况如何：
> 1. `SELECT * FROM products WHERE id = 1;`
> 2. `SELECT * FROM products WHERE id = 1 FOR UPDATE;`
> 3. `UPDATE products SET stock = stock - 1 WHERE id = 1;`
     > 追问：为何在 `REPEATABLE READ` 下，`SELECT` 和 `SELECT ... FOR UPDATE` 的查询结果可能不一致？这种现象是设计缺陷还是有意为之？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 读类型与加锁情况：
  - `SELECT * FROM products WHERE id = 1;`（普通 SELECT）：
    - RC：快照读，无锁（每次查询生成新的 ReadView，可读到最新已提交数据）。
    - RR：快照读，无锁（首次查询生成 ReadView，后续复用，保证可重复读）。
  - `SELECT ... FOR UPDATE`（当前读）：
    - RC：行锁（Record Lock），锁定匹配的主键行。
    - RR：行锁 + 间隙锁（若条件非唯一索引或范围条件），锁定匹配行及间隙，防止幻读。
  - `UPDATE ... WHERE id = 1;`（当前写）：
    - RC：行锁（Record Lock）。
    - RR：行锁 + 间隙锁（基于主键等值查询时，只需锁主键行及索引间隙；若条件为范围或非唯一索引，则额外加间隙锁）。
- 为何 RR 下 SELECT 与 FOR UPDATE 可能结果不一致：
  这是 **有意设计**，并非缺陷。快照读（普通 SELECT）基于一致性视图（ReadView），用于高频读场景，避免加锁开销；当前读（FOR UPDATE / UPDATE / DELETE）需要读取最新数据并加锁，保证数据修改的安全性。两者服务于不同场景：快照读追求性能，当前读追求数据准确性和并发控制。
- 注意：若需在 RR 下始终读到最新数据，应使用 `SELECT ... FOR UPDATE` 或 `LOCK IN SHARE MODE`（共享锁），或显式提交事务后重建快照。
</details>

**我的初答**：
**错漏点**：

---

## Day 39 (2026-09-02) —— MySQL 存储引擎（InnoDB vs MyISAM、物理结构、索引差异）

### 题目1：InnoDB 与 MyISAM 在核心特性上的全面对比（事务、锁、缓存、崩溃恢复）
> 请从以下五个维度对比 InnoDB 和 MyISAM 存储引擎：
> 1. 事务支持（ACID 与 外键约束）。
> 2. 锁粒度（行锁 vs 表锁）及其对并发性能的影响。
> 3. 崩溃恢复能力（宕机后数据一致性保障）。
> 4. 缓存机制（数据缓存 vs 仅索引缓存）。
> 5. 适用场景（读多写少 vs 高并发写/OLTP）。
     > 追问：为何在 MySQL 5.5 之后，InnoDB 取代 MyISAM 成为默认存储引擎？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 事务与约束：InnoDB 支持事务（ACID）和外键约束，保证数据完整性；MyISAM 不支持事务和外键，适合简单查询。
- 锁粒度：InnoDB 使用行级锁（Record Lock + Gap Lock），支持高并发写入；MyISAM 使用表级锁，写操作会阻塞所有读写，并发性能差。
- 崩溃恢复：InnoDB 通过 redo log 在宕机后自动恢复已提交事务，保证数据不丢失；MyISAM 无日志机制，异常断电易导致表损坏，需手动修复（`repair table`）。
- 缓存机制：InnoDB 使用缓冲池（Buffer Pool）缓存数据和索引，大幅提升读写效率；MyISAM 仅缓存索引（Key Cache），数据文件依赖操作系统缓存。
- 适用场景：InnoDB 适用于 OLTP（高并发事务型系统，如电商订单）；MyISAM 适用于读多写少或数据仓库（如报表统计）。
- 取代原因：随着硬件发展，CPU 多核和内存变大，行级锁与事务成为刚需，InnoDB 更符合现代高并发业务需求。
</details>

**我的初答**：
1. InnoDB: 支持事务与外键,锁为行级锁,采用redo log程序崩溃后可恢复,缓存不了解,适用于高并发写/OLTP,DML遵循ACID,能保证数据的安全.
2. MyISAM: 不支持事务与外键,表级锁,无法在程序崩溃后恢复缓存不了解,适用于读多写少的情况
3. InnoDB支持事务,外键,行级锁等,能够保证数据的安全,且现代数据库有非常多的需要保证原子性,一致性的操作,如:减库存加订单.MyISAM不支持事务,操作中间出现错误无法回滚,必须手动修改错误

**错漏点**：
<details>
<summary><strong>点击展开错漏点补充</strong></summary>

一、初答问题诊断

| 问题                    | 说明                                                                                            |
| --------------------- | --------------------------------------------------------------------------------------------- |
| **缓存机制完全空缺**          | 这是题目明确要求的第四维度，面试中"不了解"会直接丢分                                                                   |
| **"程序崩溃后可恢复"表述不准**    | 不是"程序崩溃"，而是**数据库实例崩溃**；且恢复靠的是 **redo log + 崩溃恢复机制**，MyISAM 也不是"完全无法恢复"，而是只能恢复到上次备份/检查点，且容易表损坏 |
| **"现代数据库有非常多的……"偏口语** | 面试答追问时应从**官方决策的技术原因**角度回答（见下文）                                                                |
| 遗漏细节                  | InnoDB 的 MVCC、聚簇索引、自适应哈希、MyISAM 的 `count(*)` 优化等加分点未提                                         |

### 1. **事务支持（ACID 与外键）**

|      | InnoDB                                    | MyISAM                     |
| ---- | ----------------------------------------- | -------------------------- |
| 事务   | **支持 ACID**，有 commit / rollback           | **不支持**，每条 SQL 独立生效，出错无法回滚 |
| 外键   | **支持**，保证引用完整性                            | 不支持（语法上可写但被忽略）             |
| 底层机制 | redo log（持久性）、undo log（原子性 + MVCC）、锁（隔离性） | 无事务日志                      |

### 2. 锁粒度与并发性能

- **InnoDB：行级锁**（本质锁的是**索引记录**，不走索引的 UPDATE 会退化为锁全表，这是常见追问点）。配合 **MVCC**（多版本并发控制），普通 SELECT 不加锁、读写不互斥，高并发写场景下吞吐量远高于 MyISAM。

- **MyISAM：表级锁**。一个写操作锁整张表，其他读写全部排队。并发写几乎串行化，只适合读多写少。


### 3. 崩溃恢复能力

- **InnoDB**：靠 **redo log（WAL，先写日志再刷盘）+ 双写缓冲**。宕机重启后自动做崩溃恢复：已提交事务通过 redo log 重放，未提交事务通过 undo log 回滚，**保证已提交数据不丢、数据页不损坏**。

- **MyISAM**：无事务日志，只把数据写到文件系统缓存。宕机极易造成**索引/数据文件损坏**，重启后常需 `REPAIR TABLE` 修复，且**可能丢数据**；最多只能恢复到崩溃前的最后落盘点，无一致性保障。


### 4. 缓存机制（你空缺的部分，重点记）

- **InnoDB：`innodb_buffer_pool` 同时缓存数据页和索引页**（还包括插入缓冲、自适应哈希、锁信息等）。数据以页为单位直接在内存中读写，再异步刷盘——**内存利用率高，读写都受益**。建议设置为物理内存的 50%~70%。

- **MyISAM：key_buffer 只缓存索引块**，**数据文件依赖操作系统的文件系统缓存**（page cache）。数据缓存不可控，且 MySQL 进程崩溃 + OS 缓存未落盘 = 丢数据，这也是它崩溃恢复弱的原因之一。


### 5. 适用场景

表格

| InnoDB | MyISAM |
| --- | --- |
| 高并发读写、OLTP | 读多写少、几乎无写入 |
| 需要事务/外键的业务（订单、支付、库存） | 日志、统计归档类只读数据 |
| 有行级锁 + MVCC，写不阻塞读 | 表锁，有写就全表排队 |
| 主流默认选择 | MySQL 5.5+ 后基本被淘汰，仅遗留系统 |

补充加分点：MyISAM 曾有的优势——`SELECT COUNT(*)` O(1)、支持全文索引、表空间更小——在 MySQL 5.6+ InnoDB 已全部补齐（InnoDB 也支持全文索引了）。

### 三、追问：为什么 MySQL 5.5 之后 InnoDB 成为默认引擎？

从技术演进角度分四层回答：

1. **正确性是底线**：业务对事务（原子性、一致性）的需求是普遍的——如你说的"减库存 + 下订单"要么同时成功要么同时回滚。MyISAM 无事务、无外键、无崩溃恢复，数据安全靠应用层自己保证，风险不可接受。

2. **并发能力**：互联网业务以高并发读写为主，MyISAM 的表锁让写操作互相排队，无法支撑 OLTP；InnoDB 行锁 + MVCC 让读写并发成为可能。

3. **可靠性**：InnoDB 的 redo log 崩溃恢复 + 双写机制保证宕机不丢已提交数据；MyISAM 宕机即可能表损坏，运维成本高。

4. **官方演进**：Oracle 收购 MySQL 后，InnoDB（本就是 Oracle 旗下 InnoDB Oy 的产品）持续获得全部研发资源，buffer pool、在线 DDL、全文索引等短板逐一补齐，MyISAM 停止演进。把最可靠、功能最全的引擎设为默认，是对新手用户的保护——**默认即正确**。

</details>

### 题目2：InnoDB 的物理存储结构（表空间、段、区、页、行）及其对性能的影响
> InnoDB 的逻辑存储结构从大到小依次为：表空间（Tablespace）-> 段（Segment）-> 区（Extent）-> 页（Page）-> 行（Row）。请回答：
> 1. 独立表空间（`innodb_file_per_table=ON`）与系统表空间的区别是什么？为何推荐开启独立表空间？
> 2. 页的默认大小是多少（16KB）？为什么页大小对 B+ 树的层高和 I/O 性能至关重要？
> 3. 什么是“页分裂”？它如何影响插入性能？使用自增主键为何能减少页分裂？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 表空间差异：
  - 系统表空间（ibdata1）：存储所有表的共享数据、undo log 等，扩容不易且无法单独回收空间。
  - 独立表空间（`.ibd` 文件）：每张表单独一个文件，回收空间（`OPTIMIZE TABLE`）更方便，且支持 `TRUNCATE` 快速释放磁盘，推荐开启。
- 页大小与 B+ 树：
  - 默认页大小为 16KB（可编译时调整，但生产环境不建议修改）。B+ 树非叶子节点存储索引键和指针，页越大，单个节点能存储的键越多，树的层数越低（通常 2~4 层），减少磁盘 I/O。
  - I/O 性能：一次磁盘 I/O 读取一个页（16KB），若命中索引，仅需 3~4 次磁盘 I/O 即可定位百万级数据。
- 页分裂与自增主键：
  - 页分裂：当插入数据时，若当前页已满，InnoDB 会新建页并移动部分数据到新页，维护 B+ 树有序性，增加磁盘碎片和性能开销。
  - 自增主键插入顺序递增，数据仅追加在 B+ 树末尾，不会触发页分裂（仅页满时新分配页）。
  - 若使用 UUID 或随机主键，插入位置随机，频繁引发页分裂，导致索引碎片化和性能下降。
</details>

**我的初答**：
**错漏点**：


### 题目3：InnoDB 聚集索引与 MyISAM 非聚集索引的底层结构差异（回表 vs 直接偏移）
> 请从 B+ 树叶子节点存储的内容出发，说明 InnoDB 聚集索引（主键索引）和 MyISAM 非聚集索引（所有索引）在查询数据时的路径有何本质不同？追问：
> 1. InnoDB 的辅助索引（二级索引）为什么必须回表（通过主键再次查询）？为什么 MyISAM 的二级索引不需要回表，而是直接指向数据行偏移量？
> 2. 在 InnoDB 中，若表没有显式定义主键，InnoDB 会如何处理？这样做有什么潜在风险？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- InnoDB 聚集索引（Primary Key）：
  - B+ 树叶子节点直接存储整行数据（聚簇）。查询时直接命中主键索引即可获得所有字段，无需回表。
- InnoDB 辅助索引（Secondary Index）：
  - B+ 树叶子节点存储的不是行数据，而是**主键值**。查询时先遍历辅助索引获取主键 ID，再通过主键索引（回表）获取完整行数据。若查询字段均在辅助索引中（覆盖索引），则无需回表。
- MyISAM 非聚集索引（所有索引）：
  - 索引与数据分离，B+ 树叶子节点存储数据行的**物理偏移量（行指针）**。查询时直接根据偏移量从 `.MYD` 文件中读取行数据，无需回表，但数据文件是堆组织（无序），存在碎片。
- 无主键的 InnoDB 处理规则：
  1. 若存在非空的唯一索引，InnoDB 将其选为聚集索引。
  2. 若都没有，InnoDB 自动生成一个 6 字节的隐藏行 ID（`DB_ROW_ID`）作为聚集索引。
  3. 风险：自动生成的主键无序，若由用户显式指定且业务不依赖，可能导致随机插入，引发页分裂和性能问题。强烈建议手动定义自增整型主键。
</details>

**我的初答**：
**错漏点**：

---

## Day 40 (2026-09-03) —— 索引基础（概念、数据结构、优缺点、聚簇与非聚簇）

### 题目1：索引的本质与 B+ 树索引结构 —— 为什么数据库索引能加速查询？
> 请从以下角度解释索引的本质：
> 1. 索引是什么？（类比书的目录）。
> 2. MySQL InnoDB 默认使用什么数据结构作为索引？为什么不用二叉树、哈希表或 B 树？
> 3. B+ 树的叶子节点和非叶子节点分别存储什么？为什么 B+ 树适合磁盘 I/O？
     > 追问：如果查询条件为 `WHERE name LIKE '%张三'`，该索引还能生效吗？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 索引本质：一种**排好序的快速查找数据结构**，帮助数据库减少扫描行数，将随机 I/O 转为顺序 I/O。
- 数据结构：InnoDB 使用 **B+ 树**。原因：
  - 二叉树：树层数过高（百万级数据需约 20 层），磁盘 I/O 次数多。
  - 哈希表：仅支持等值查询（= 或 IN），不支持范围查询（>、<、BETWEEN）。
  - B 树：非叶子节点也存储数据，导致节点容量小、树层数高；且范围查询需中序遍历，效率低。
  - B+ 树：非叶子节点只存索引键（不存数据），可容纳更多键，树层数低（2~4 层）；叶子节点形成有序链表，极适合范围查询。
- 叶子/非叶子存储：
  - 非叶子节点：存储索引键 + 指向子节点的指针。
  - 叶子节点：存储完整数据（聚簇索引）或主键值（辅助索引）+ 双向链表。
- 前缀模糊查询（`LIKE '%张三'`）：索引失效。因为 B+ 树按**前缀**排序，`%` 开头的条件无法确定起始位置，只能全表扫描。
</details>

**我的初答**：
1. 索引是数据库中帮助加快查询的数据结构,类似于书的目录,能帮助我们更快定位到查询的数据
2. InnoDB默认使用B+Tree作为索引,二叉树的层数过高I/O次数过多,哈希表会打散数据,只支持精确查找.B树每个结点都存放数据信息,每次查询时I/O次数不平均,B+Tree更矮,每次I/O都查询叶子结点,I/O次数相等
3. B+树的叶子结点存放对应行的所有数据,非叶子结点只存放对应指针,B+树非叶子结点只存放指针的方式让它可以存放更多指针,使得B+树的高度相比更低,I/O次数更少.
4. 不能生效,指针无法判断该模糊匹配具体的位置

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

**修正：**

> 索引的本质是**一种以空间换时间的、有序的数据结构**（InnoDB 中即 B+ 树），它把无序的表数据按某些列组织成有序结构，让查询通过 O(log n) 的查找定位数据，而不是全表扫描 O(n)。  
> 就像书的目录：目录页按章节顺序排列，你先在目录里定位页码，再翻到那一页，而不是一页页翻着找。

### 第 2 问：为什么选 B+ 树？⚠️ 有概念错误

❌ **错误 1**：*“B 树每个结点都存放数据，查询时 I/O 次数不平均”*  
—— B 树确实所有节点都存数据，但问题不是“不平均”，而是：

- 存了数据 → 每个节点能放的**键和指针变少** → 树更高 → I/O 更多
- 范围查询需要中序遍历回溯，效率低

❌ **错误 2**：*“每次 I/O 都查询叶子结点，I/O 次数相等”*  
—— 表述混乱。正确说法是：B+ 树**任何一次查找的路径长度都相同**（必须走到叶子节点），性能稳定。

### 第 3 问：B+ 树存什么？⚠️ 遗漏关键区分

❌ 你说“叶子结点存放对应行的所有数据”——**这只对主键索引（聚簇索引）成立**。

追问：LIKE ‘%张三’ 能用索引吗？⚠️ 结论对，理由错

❌ 你说“指针无法判断该模糊匹配具体的位置”——这不是原因。

**正确解释：**

> B+ 树索引是**按索引列的值从左到右排序**的。`LIKE '张三%'`（前缀匹配）可以利用排序前缀定位到 `张三` 开头的连续区间；而 `'%张三'` 的**前缀不确定**，可能是“李张三”“王张三”……无法在有序结构中圈定一个范围，**索引失效，退化为全表扫描**。

**加分项（面试官会喜欢）：**

- 索引失效的口诀：**“最左前缀原则”** —— `LIKE '%xx'` 和 `LIKE '%_xx'` 左侧模糊都会失效
- 变通方案：① 存一份反转列（`张三'` → 反转后前缀匹配 `'%三张'` 变为 `'三张%'`）；② 使用全文索引 / ES；③ 若该列是二级索引，MySQL 至少可以做**覆盖扫描**（扫描索引树比全表扫描 I/O 少）


</details>

### 题目2：索引的优缺点与创建索引的原则（覆盖索引 vs 回表）
> 请列举索引的优缺点（至少各两点），并回答以下问题：
> 1. 什么是“回表查询”？为什么它会影响查询性能？
> 2. 什么是“覆盖索引”？为什么覆盖索引能避免回表？
> 3. 为什么经常更新的字段不适合建索引？
     > 追问：如果 `SELECT name, age FROM users WHERE age = 20;`，在 `age` 上建索引能覆盖查询吗？若不能，如何改造索引？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 优点：
  1. 加速查询（尤其是 WHERE、ORDER BY、GROUP BY）。
  2. 唯一索引可保证数据唯一性。
  3. 减少服务器扫描行数，降低磁盘 I/O。
- 缺点：
  1. 占用磁盘空间（索引本身需要存储）。
  2. 降低插入、更新、删除的速度（需同步维护索引）。
  3. 索引过多会增加优化器选择难度。
- 回表查询：
  - 辅助索引叶子节点仅存主键值，查询非索引字段时需先查辅助索引获得主键，再回到主键索引（聚簇索引）取完整行数据，该过程称为回表。回表会增加一次随机 I/O，影响性能。
- 覆盖索引：
  - 查询所需字段全部包含在索引中（即索引叶子节点已存储这些字段值），无需回表。例如：`(age, name)` 联合索引可覆盖 `SELECT name FROM users WHERE age = 20`。
- 更新频繁字段不宜建索引：索引需要随数据更新而重建（或调整 B+ 树），维护成本高。
- 针对查询 `SELECT name, age FROM users WHERE age = 20`：仅在 `age` 上建索引需要回表（因 `name` 不在索引中）。可建联合索引 `(age, name)` 实现覆盖索引，避免回表。
</details>

**我的初答**：
**错漏点**：


### 题目3：聚簇索引（Clustered Index）与非聚簇索引（Non-clustered Index）的区别
> 请从以下维度对比 InnoDB 的聚簇索引和 MyISAM 的非聚簇索引：
> 1. 索引叶子节点存储的内容。
> 2. 数据行与索引的物理存储关系（数据和索引是否分离）。
> 3. 主键索引与辅助索引在两种引擎下的查询路径差异。
     > 追问：InnoDB 中，如果没有显式定义主键，聚集索引会如何选择？如果选择了一个非自增列作为主键，会有什么性能风险？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 叶子节点内容：
  - InnoDB 聚簇索引：叶子节点存储**完整行数据**（聚簇）。
  - MyISAM 非聚簇索引：叶子节点存储**数据行物理偏移量（行指针）**。
- 物理存储：
  - 聚簇索引：表数据按主键顺序存储（数据和索引在一起），索引的叶子节点就是数据页。
  - 非聚簇索引：索引和数据分离（索引在 `.MYI`，数据在 `.MYD`），索引叶子节点仅指向数据位置。
- 查询路径：
  - InnoDB 主键查询：直接命中聚簇索引，1 次查询即可获得所有数据。
  - InnoDB 辅助索引查询：先查辅助索引得主键 ID，再回表查聚簇索引（2 次查询）。
  - MyISAM 任何索引查询：先查索引得偏移量，再根据偏移量读取数据文件（2 次操作，但无需回表）。
- 无主键时 InnoDB 处理：
  1. 优先选第一个非空的唯一索引作为聚簇索引。
  2. 若无唯一索引，自动生成隐藏的 6 字节 `DB_ROW_ID` 作为主键。
- 非自增主键风险（如 UUID）：
  - 插入随机无序，B+ 树频繁页分裂，索引碎片化严重，插入性能大幅下降。
  - 页分裂产生的空间碎片会浪费磁盘并增加碎片整理开销。
</details>

**我的初答**：
**错漏点**：

---

## Day 41 (2026-09-04) —— 索引分类与索引语法（创建、查看、删除）

### 题目1：MySQL 索引的分类及各自适用场景（普通索引、唯一索引、主键索引、联合索引、全文索引）
> 请说明以下索引类型的区别及典型使用场景：
> 1. 普通索引（INDEX）。
> 2. 唯一索引（UNIQUE INDEX）。
> 3. 主键索引（PRIMARY KEY）。
> 4. 联合索引（组合索引）。
> 5. 全文索引（FULLTEXT）。
     > 追问：若表中有大量重复值的列（如性别 `gender`），是否适合建索引？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 普通索引（INDEX）：最基本的索引，无唯一性约束，允许重复值。用于加速查询（WHERE、ORDER BY）。适用场景：查询条件中的列（如 `name`）。
- 唯一索引（UNIQUE INDEX）：要求列值唯一（允许 NULL，但仅允许一个 NULL）。保证数据唯一性，同时加速查询。适用场景：业务唯一标识（如身份证号、邮箱）。
- 主键索引（PRIMARY KEY）：特殊的唯一索引，不允许 NULL，每表仅一个。InnoDB 中作为聚集索引，直接决定数据物理存储顺序。适用场景：表的主键（推荐自增整型）。
- 联合索引（组合索引）：涉及多个列的索引（如 `(a, b, c)`），遵循最左前缀原则。适用场景：多条件查询、排序（ORDER BY）和分组（GROUP BY）场景。
- 全文索引（FULLTEXT）：针对文本字段（VARCHAR/TEXT）的关键词搜索（自然语言搜索），支持 `MATCH AGAINST` 语法。仅 MyISAM（5.6 前）和 InnoDB（5.6+）支持。适用场景：文章、博客、评论内容的搜索（如 `MATCH(title, content) AGAINST('MySQL')`）。
- 重复值高的列（如性别）：不适合建索引。因为索引选择性（`COUNT(DISTINCT col) / COUNT(*)`）极低，优化器可能放弃索引（走全表扫描），且维护索引开销大。
</details>

**我的初答**：
1. 普通索引: 只是给字段行加一个索引,无其他限制,对字段无任何要求时使用
2. 唯一索引: 该索引中的数据不能存在重复的,通常用于身份证号,手机号等信息
3. 主键索引: InnoDB中主键索引用于存放该行的所有字段信息,通常采用没有具体含义只用于标记的自增主键,默认自主创建
4. 联合索引: 用于需要同时查询多列数据时
5. 全文索引: 查找的是原文中的关键字,而不是比较索引中的值
6. 表中存在大量重复的列不适合加索引,原因:定位效率差

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

### 1. 普通索引 ✅ 基本正确

表述可更规范：

> 普通索引**唯一的职责就是加速查询**，对列的取值无任何约束（允许重复、允许 NULL）。适用于频繁出现在 `WHERE`、`JOIN ON`、`ORDER BY` 中，但值可重复的列，如订单表的商品名、用户的昵称等。

### 2. 唯一索引 ✅ 正确

可补充两个加分点：

> 唯一索引有**双重作用：加速查询 + 数据约束**（插入重复值直接报错，从数据库层面兜底防脏数据）。典型场景：手机号、身份证号、邮箱。  
> 细节：MySQL 中唯一索引列**允许多个 NULL**（NULL 不参与唯一性判断）——这是面试可能追问的细节。

### 3. 主键索引 ⚠️ 有错误，且有遗漏

❌ **错误**：“默认自主创建”表述不对。InnoDB 是聚簇索引表，**必须**有聚簇索引，选择顺序是：

1. 显式定义的主键；
2. 若无主键 → 第一个 `NOT NULL` 的 `UNIQUE` 索引；
3. 若都没有 → InnoDB 自动生成一个**隐藏的 6 字节 ROW_ID** 作为聚簇索引（该隐藏索引对你完全不可用）。

⚠️ **遗漏**：你提到“自增主键”但没解释**为什么推荐它**——这是高频追问：

> ① **避免页分裂**：自增值单调递增，新数据总是顺序追加到 B+ 树最右侧的页，若用随机值（如 UUID）做主键，插入位置随机，会频繁触发页分裂和数据页的随机写；  
> ② **让二级索引更小**：二级索引叶子节点存的是主键值，主键越短，所有二级索引就越小。

### 4. 联合索引 ❌ 核心考点缺失

“用于同时查询多列”只说对了一半。**必答的是最左前缀原则**：

> 联合索引 `(a, b, c)` 在逻辑上等价于建了三个索引：`(a)`、`(a,b)`、`(a,b,c)`。查询条件必须**从最左列开始连续命中**：
>
> - ✅ `WHERE a=1` / `WHERE a=1 AND b=2` / `WHERE a=1 AND b=2 AND c=3` 都能走索引
> - ❌ `WHERE b=2` 或 `WHERE b=2 AND c=3` 无法用索引定位（跳过了 a）
> - ⚠️ `WHERE a=1 AND c=3`：只有 a 参与索引定位；c 在 MySQL 5.6+ 可通过**索引条件下推（ICP）**在索引层过滤，减少回表
> - ⚠️ `WHERE a>1 AND b=2`：a 是范围查询，**b 停止走索引**

**设计原则（加分项）：**

- 区分度高的列放左边；
- 等值查询列在前，范围查询列在后；
- 用一个联合索引替代多个单列索引，减少索引数量（省空间、降低写放大）。

### 5. 全文索引 ⚠️ 方向对，太简略

> 全文索引基于**分词 + 倒排索引**（记录“词 → 出现在哪些行”的映射），解决 `LIKE '%关键词%'` 左模糊无法用普通 B+ 树索引的问题。使用 `MATCH(col) AGAINST('关键词')` 语法，适用于文章正文、商品描述等大文本搜索。  
> 注意：中文需要 ngram 分词器；生产环境中大规模搜索通常用 **Elasticsearch** 替代 MySQL 全文索引。

## 二、追问：性别列适合建索引吗？

你的结论“不适合”正确，但“定位效率差”没有说到本质。标准答案分三层：

> **① 本质原因——区分度低**  
> 索引的价值取决于**区分度（selectivity）= count(distinct col) / count(*)**，越接近 1 越有价值。gender 只有 2~3 个取值，区分度趋近于 0——B+ 树中该列的值极度堆积，索引几乎丧失了“二分定位”的能力。
>
> **② 回表代价——甚至更慢**  
> 走二级索引找到 `gender='男'` 的所有主键后需逐条回表。若该值占全表 50%，意味着几十万次**随机 I/O 回表**，代价远高于一次顺序的全表扫描。优化器基于成本估算会**直接放弃索引走全表扫描**——索引建了也白建。
>
> **③ 纯负资产**  
> 白白占用存储空间，且拖慢 INSERT / UPDATE / DELETE（每次写都要维护索引树）。

**加分项（面试官喜欢听到的“辩证思考”）：**

- 例外：如果某列大部分值占比较高，但**某个特定值占比极小**（如 `status` 中“失败”仅占 1%），查这个稀有值时优化器仍可能走索引；
- 变通：不单独建，而是放进联合索引，如 `INDEX(gender, age)` 配合覆盖索引（`SELECT` 只涉及索引列时无需回表，扫描整个索引树也比全表扫描便宜）。
</details>

### 题目2：联合索引的最左前缀原则（创建、使用与失效场景）
> 现有表 `orders` 创建了联合索引 `idx_order` 在 `(user_id, status, order_date)` 上。请判断以下查询能否使用该索引（能使用哪些列）：
> 1. `SELECT * FROM orders WHERE user_id = 1;`
> 2. `SELECT * FROM orders WHERE status = 1 AND user_id = 1;`
> 3. `SELECT * FROM orders WHERE status = 1;`
> 4. `SELECT * FROM orders WHERE user_id = 1 AND order_date > '2024-01-01';`
> 5. `SELECT * FROM orders WHERE user_id = 1 AND status = 1 ORDER BY order_date;`
     > 追问：若 `WHERE user_id = 1 AND status IN (1, 2) AND order_date > '2024-01-01'`，该索引能用到哪些列？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 最左前缀原则：联合索引 `(a, b, c)` 相当于创建了 `(a)`、`(a, b)`、`(a, b, c)` 三个索引。查询条件必须从索引最左侧列开始连续匹配，才能利用索引。
- 判定结果：
  1. `user_id = 1`：能使用索引的 `user_id` 列。
  2. `status = 1 AND user_id = 1`：能使用 `user_id` 和 `status`（顺序可交换，优化器自动调整）。
  3. `status = 1`：无法使用该索引（跳过了最左列 `user_id`）。
  4. `user_id = 1 AND order_date > '2024-01-01'`：仅能使用 `user_id`（`order_date` 在 `status` 之后，因 `status` 未等值匹配，索引在 `user_id` 后中断，`order_date` 无法参与索引查找，只能过滤）。
  5. `user_id = 1 AND status = 1 ORDER BY order_date`：能使用 `user_id` 和 `status` 进行等值匹配，并利用 `order_date` 排序（索引有序，避免文件排序）。
- 追问 `user_id = 1 AND status IN (1, 2) AND order_date > '2024-01-01'`：
  - 能使用 `user_id`（等值）和 `status`（范围查询 `IN` 被优化器视为范围条件）。因 `status` 是范围条件，后续 `order_date` 无法用于查询匹配（索引在 `status` 后中断），但若索引覆盖则可排序。
</details>

**我的初答**：
**错漏点**：


### 题目3：索引的创建与删除语法及查看索引的方式（CREATE INDEX、DROP INDEX、SHOW INDEX、ALTER TABLE）
> 请回答以下问题：
> 1. 使用 `CREATE INDEX` 在 `employees` 表的 `hire_date` 列上创建一个普通索引 `idx_hire_date`。
> 2. 使用 `ALTER TABLE` 在 `employees` 表的 `first_name` 和 `last_name` 上创建一个联合唯一索引 `idx_name_unique`。
> 3. 如何查看 `employees` 表上所有的索引？
> 4. 删除索引 `idx_hire_date`。
     > 追问：若在创建索引时指定 `USING BTREE`，与默认的 `USING BTREE` 有何不同？InnoDB 是否支持 `USING HASH`？若支持，适用什么场景？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 创建普通索引：
  CREATE INDEX idx_hire_date ON employees(hire_date);
- 创建联合唯一索引（使用 ALTER TABLE）：
  ALTER TABLE employees ADD UNIQUE INDEX idx_name_unique (first_name, last_name);
- 查看索引：
  SHOW INDEX FROM employees;
  或使用 `SHOW KEYS FROM employees;`
- 删除索引：
  DROP INDEX idx_hire_date ON employees;
  或 `ALTER TABLE employees DROP INDEX idx_hire_date;`
- 关于 `USING BTREE`：
  - InnoDB 索引默认使用 B+ 树（即 `USING BTREE`），显式指定与默认一致，无差异。
  - InnoDB **支持 `USING HASH`**，但仅用于自适应哈希索引（AHI），不支持用户手动创建哈希索引（若在建表时指定 `USING HASH`，MySQL 会忽略或转换为 B+ 树，取决于版本和存储引擎）。
  - 若需哈希索引，可使用 `MEMORY` 引擎表（支持 `USING HASH`），适用于等值查询极快的临时表或缓存表。
</details>

**我的初答**：
**错漏点**：

---

## Day 42 (2026-09-05) —— 索引性能分析（EXPLAIN、慢查询日志、profile）

### 题目1：EXPLAIN 执行计划的核心字段解读（type、key、rows、Extra、filtered）
> 使用 `EXPLAIN SELECT * FROM orders WHERE user_id = 100 AND status = 1;` 返回如下执行计划：
> - `type: ref`
> - `possible_keys: idx_user, idx_status`
> - `key: idx_user`
> - `key_len: 4`
> - `ref: const`
> - `rows: 150`
> - `filtered: 10.00`
> - `Extra: Using where`
    > 请解释以下内容：
> 1. `type` 为 `ref` 表示什么？比 `ALL`、`range`、`eq_ref` 更优还是更差？
> 2. `rows: 150` 和 `filtered: 10.00` 分别代表什么？它们如何共同影响最终扫描行数？
> 3. `Extra: Using where` 表示什么？与 `Using index` 有何区别？
     > 追问：若 `Extra` 中出现 `Using filesort`，表示什么？如何优化？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- type 解读：
  - `ref`：表示使用非唯一索引进行等值匹配（`=` 或 `<=>`），返回匹配的多行。性能优于 `ALL`（全表扫描）和 `range`（范围查询），但略逊于 `eq_ref`（唯一索引等值匹配，最多返回一行）。
  - 性能排序（由好到差）：`system` > `const` > `eq_ref` > `ref` > `range` > `index` > `ALL`。
- rows 与 filtered：
  - `rows`：优化器估算需扫描的行数（基于索引统计信息）。
  - `filtered`：表示满足 `WHERE` 条件（或 `JOIN` 条件）的估算行数占比（百分比）。实际最终扫描行数 ≈ `rows * filtered / 100`。本例中，扫描 150 行，其中约 15 行（10%）最终符合条件。
- Extra 字段：
  - `Using where`：表示 MySQL 存储引擎返回数据后，还需在 Server 层进行 `WHERE` 条件过滤（通常因无法完全在索引中完成过滤）。
  - `Using index`：表示覆盖索引（查询所需字段全部在索引中，无需回表），性能最佳。
  - `Using filesort`：表示需额外排序操作（文件排序），通常因 `ORDER BY` 字段未命中索引，应优化索引或改写查询。
</details>

**我的初答**：
1. ref具体意思不记得了,不过效率高低应该是ref>eq_ref>range>ALL
2. rows: 150 是判断该查询大概扫描了多少行,filtered: 10.00 不清楚
3. Using where不了解,Using index表示使用索引.
若Extra中出现Using filesort代表索引失效,或没有使用索引,如果没索引就建索引,有索引就查看索引是否存在问题.

**错漏点**：

<details>
<summary><strong>点击展开错漏点</strong></summary>
1. type: ref 的含义与效率排序

`ref` 表示使用了**非唯一索引**进行等值匹配（`user_id = 100`，普通索引可能匹配多行）。

效率从优到差（注意，你初答的顺序有误）：

plain复制

```plain
const > eq_ref > ref > range > index > ALL
```

- **eq_ref**：唯一索引等值匹配，最多返回一行（如主键 JOIN）

- **ref**：非唯一索引等值匹配，可能返回多行 —— 比 eq_ref **差**，比 range、ALL **好**

- **range**：索引范围扫描（>、<、BETWEEN）

- **ALL**：全表扫描，最差


本题 ref 说明走了 `idx_user` 普通索引，属于可接受水平。

### 2. rows 与 filtered 的含义及共同作用

- **rows: 150**：优化器**估算**需要扫描的行数——通过 `idx_user` 找到约 150 条 `user_id = 100` 的记录。

- **filtered: 10.00**：这 150 行中，预计有 **10%** 能满足其余过滤条件（即 `status = 1`）。


最终返回行数估算：

plain复制

```plain
150 × 10.00% = 15 行
```

二者相乘才是查询的真实成本估算。filtered 很低（如本题 10%）说明**大部分扫描是浪费的**——这是优化的重要信号：可以考虑建 `(user_id, status)` 联合索引，让扫描行数直接降到约 15。

### 3. Extra: Using where 与 Using index 的区别

- **Using where**：存储引擎按索引取出数据行后，**Server 层还要再过滤一次**（本例就是用索引定位 user_id=100 的行，再逐行检查 status=1）。说明索引只覆盖了一部分条件。

- **Using index**：**覆盖索引**——查询所需的列全部在索引中，无需回表读数据行，直接从索引返回结果，效率更高。


简单说：Using where 是"索引帮忙定位、还要回表再筛"；Using index 是"索引里全有，不用回表"。

### 4. 追问：Using filesort

**含义**：排序无法利用索引的有序性，MySQL 需要在内存（或磁盘）中**额外做一次排序**。数据量大时开销明显。

**优化方法**：

1. 让 ORDER BY 的列与查询条件走**同一个联合索引**，且排序列的顺序、方向与索引一致，例如建了 `(user_id, status, create_time)` 后 `ORDER BY create_time` 即可利用索引有序性，消除 filesort；

2. 排序列上不符合最左前缀或混用 ASC/DESC（MySQL 8.0 之前）会导致索引失效，需调整索引或 SQL；

3. 无法避免时，可适当调大 `sort_buffer_size` 让排序在内存完成，避免落盘。


⚠️ 注意：filesort 不一定意味着"索引失效"——过滤条件走了索引、但排序列无索引可用时也会出现。
</details>

### 题目2：慢查询日志的开启、分析及优化流程（mysqldumpslow 与 profile）
> 线上 MySQL 出现 CPU 飙升，疑似存在慢查询。请回答：
> 1. 如何开启慢查询日志？需要设置哪些参数（`slow_query_log`、`long_query_time`、`log_queries_not_using_indexes`）？
> 2. 如何分析慢查询日志文件？（至少列出两种工具或命令）
> 3. 若找到一条慢查询 `SELECT * FROM products WHERE category = 'electronics' ORDER BY price DESC;`，`category` 列上已有普通索引，但查询依然慢，可能是什么原因？如何进一步用 `EXPLAIN` 和 `PROFILE` 定位瓶颈？
     > 追问：为何 `log_queries_not_using_indexes` 参数在生产环境需谨慎开启？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 开启慢查询日志（MySQL 5.7+）：
  SET GLOBAL slow_query_log = ON;
  SET GLOBAL long_query_time = 1;   -- 记录执行时间超过 1 秒的 SQL
  SET GLOBAL log_queries_not_using_indexes = ON; -- 记录未使用索引的查询
  （持久化需写入 `my.cnf`：`slow_query_log=1`、`long_query_time=1`、`slow_query_log_file=/var/log/mysql/slow.log`）
- 分析工具：
  1. `mysqldumpslow /var/log/mysql/slow.log`：MySQL 自带工具，可汇总统计（如按时间排序 `-s t`）。
  2. `pt-query-digest /var/log/mysql/slow.log`：Percona Toolkit 中的高级分析工具，生成详细报告。
- 慢查询原因分析（`category` 已建索引但仍慢）：
  1. 索引选择性和回表问题：若 `category='electronics'` 占比过高（如 50%），优化器可能放弃索引（走全表扫描）。`EXPLAIN` 查看 `type` 是否为 `ALL`。
  2. `ORDER BY price` 导致文件排序：因索引 `category` 不包含 `price`，排序需要 `Using filesort`。可建联合索引 `(category, price DESC)` 覆盖排序。
  3. 使用 `PROFILE` 查看耗时细节：
     SET profiling = 1;
     SELECT * FROM products WHERE category = 'electronics' ORDER BY price DESC;
     SHOW PROFILE FOR QUERY 1;
     查看 `Sending data`、`Sorting result` 等阶段耗时。
- 谨慎开启 `log_queries_not_using_indexes`：会记录大量全表扫描查询（即使扫描行数很少），可能导致日志文件急速膨胀，影响磁盘 I/O，通常仅在调试阶段短期开启。
</details>

**我的初答**：
**错漏点**：


### 题目3：联合索引的索引选择性与扫描行数估算（SHOW INDEX 与 cardinality）
> 现有表 `employees` 有联合索引 `idx_dept_salary` 在 `(department_id, salary)` 上。使用 `SHOW INDEX FROM employees;` 输出：
> - `department_id` 的 `Cardinality` 为 10（部门总数为 10）。
> - `salary` 的 `Cardinality` 为 5000（薪资不同值较多）。
    > 请回答：
> 1. 执行 `SELECT * FROM employees WHERE department_id = 3 AND salary > 50000;`，优化器大概率会选择该索引吗？为什么？
> 2. 若执行 `SELECT * FROM employees WHERE salary > 50000;`，该索引是否能被使用？为什么？
> 3. 若 `department_id` 的 Cardinality 突然变为 1（所有记录 department_id 相同），优化器会怎么做？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 问题1：大概率会使用该索引。
  - 因 `department_id = 3` 是等值匹配且选择性较好（10 个部门中选 1 个，过滤后约 10% 数据），符合索引最左前缀原则。`salary > 50000` 为范围条件，可继续在索引的 `salary` 列上进行范围扫描（索引在 `department_id` 后连续，可部分利用 `salary`）。
- 问题2：无法使用该索引。
  - 查询条件跳过了最左列 `department_id`，直接对 `salary` 进行范围查询，不满足最左前缀原则。优化器大概率选择全表扫描（或扫描其他单列索引）。
- 问题3：若 Cardinality 降为 1（如所有记录 department_id 相同），优化器会认为该索引选择性极差，可能放弃索引，改为全表扫描（或扫描其他索引）。若需强制使用，可提示 `FORCE INDEX`，但需评估收益。
- Cardinality 本质：索引中不重复值的估算数量（基于采样），越高代表索引选择性越好，优化器越倾向使用该索引。
- 注意：`SHOW INDEX` 的 Cardinality 是抽样估算值，若统计信息不准确，可通过 `ANALYZE TABLE employees;` 重新采样更新。
</details>

**我的初答**：
**错漏点**：

---

## Day 43 (2026-09-06) —— 索引使用（索引失效场景、覆盖索引、索引下推）

### 题目1：索引失效的常见场景（隐式类型转换、函数操作、LIKE 前缀匹配、OR 条件）
> 现有表 `users`，在 `phone`（VARCHAR(11)）和 `email`（VARCHAR(100)）上分别建有普通索引。请判断以下 4 条查询是否能走索引，并说明原因：
> 1. `SELECT * FROM users WHERE phone = 13800138000;`（传入数值而非字符串）
> 2. `SELECT * FROM users WHERE YEAR(create_time) = 2024;`（`create_time` 有索引）
> 3. `SELECT * FROM users WHERE email LIKE '%@qq.com';`
> 4. `SELECT * FROM users WHERE phone = '13800138000' OR email = 'test@example.com';`
     > 追问：若第 4 条查询使用 `UNION` 改写，索引利用情况会如何变化？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询1：索引失效。`phone` 是 VARCHAR 类型，传入 INT 类型会发生**隐式类型转换**（MySQL 将字符串列转为数值），相当于在索引列上应用了函数（`CAST(phone AS SIGNED)`），导致索引无法使用。
- 查询2：索引失效。对 `create_time` 列使用了 `YEAR()` 函数，破坏了索引列的原值，MySQL 无法利用索引进行范围查找（除非创建函数索引，MySQL 8.0+ 支持）。
- 查询3：索引失效。`LIKE` 以 `%` 开头，导致 B+ 树无法确定起始搜索位置（必须全表扫描或全索引扫描）。若 `LIKE 'qq%@'`（常量前缀）则可走索引。
- 查询4：能走索引（但取决于优化器）。`phone` 和 `email` 各自有索引，MySQL 可能使用 **Index Merge（索引合并）**，分别扫描两个索引后取并集。但若优化器认为结果集过大，可能放弃索引走全表扫描。
- 改写为 UNION：
  SELECT * FROM users WHERE phone = '13800138000'
  UNION
  SELECT * FROM users WHERE email = 'test@example.com';
  UNION 会强制分别使用各自的索引（前提是索引有效），然后将结果合并去重。通常比 OR 的 Index Merge 更稳定，性能更可控（尤其是两个条件选择性都高时）。
</details>

**我的初答**：
1. 不能,传入的是非字符串类型,查询无法走索引.
2. 不会走索引,日期类型数据应加上引号,这种数字会被隐式转换成奇怪的结果
3. 不会走索引,开头模糊匹配,select索引不知道怎么查询
4. 会走索引,or的两边的查询都存在索引且符合规则
5. 两个索引都会使用,在合并后在进行去重

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

### 第 1 条：`WHERE phone = 13800138000`（phone 是 VARCHAR）✅ 结论对，❌ 理由错

你的理由“传入了非字符串类型”只描述了现象，没解释**失效机制**。关键在于**隐式转换发生在哪一侧**：

> **MySQL 的隐式转换规则**：字符串和数字比较时，**把字符串转成数字**。所以这条 SQL 实际等价于：

```
> WHERE CAST(phone AS SIGNED) = 13800138000
> 
```

引用

> 函数作用在**索引列 phone 上**，破坏了 B+ 树的有序性 → 索引失效，全表扫描。

**注意区分方向（高频追问点）：**

| 情况  | 转换发生在 | 结果  |
| --- | --- | --- |
| 列是 VARCHAR，传数字（本题） | **列上**（CAST(phone)） | ❌ 索引失效 |
| 列是 INT/BIGINT，传字符串 `'123'` | **常量上**（'123’→123） | ✅ 索引仍有效 |

所以准确说法不是“传了数字就失效”，而是“**当索引列需要被转换时才失效**”。

### 第 2 条：`WHERE YEAR(create_time) = 2024` ✅ 结论对，❌ 理由完全错

你说的“日期应加引号、数字被转换成奇怪结果”是**错误归因**——这里的问题与引号、隐式转换都无关。真正原因：

> **函数作用在索引列上**：`YEAR(create_time)` 对列做了函数运算，B+ 树是按 `create_time` 的原始值排序的，经过 `YEAR()` 加工后的结果在树上不再有序，无法二分定位 → 索引失效。

**标准修复方案（必答，体现工程能力）：** 改写为**范围查询**，让索引列保持“裸奔”状态：

sql
```
SELECT * FROM users 
WHERE create_time >= '2024-01-01 00:00:00' 
  AND create_time <  '2025-01-01 00:00:00';
```

范围查询可以直接利用 B+ 树的有序性定位到 2024 年区间的起点，然后沿叶子链表顺序扫描——两个查询语义完全等价，但一个全表扫描，一个走索引。

### 第 3 条：`LIKE '%@qq.com'` ✅ 结论对，⚠️ 理由太口语化

> B+ 树按 email **从左到右**排序。`'%@qq.com'` 的前缀不确定（可能是 `a@qq.com`、`zz@qq.com`……），**无法在有序结构中圈定一个连续区间** → 索引失效。  
> 对比：`LIKE 'test%'`（前缀匹配）可以定位到以 `test` 开头的连续区间 → 走索引（range 类型）。

**加分项——变通方案：**

- 因为 email 后缀种类少、区分度低，可以冗余一列 `email_suffix`（只存后缀），对它建索引后等值查询；
- 或存反转列 `reverse_email`，`'@qq.com'` 反转后变 `'moc.qq@'`，用前缀匹配；
- 若只是想让扫描更便宜：二级索引比整表窄，MySQL 可能选择**全索引扫描 + 回表**，仍比全表扫描 I/O 少（但不叫“走索引定位”）。

二、追问：改写成 UNION 后，索引利用如何变化？

你的回答“两个索引都会使用，合并后再去重”——结论框架对，但缺了**为什么要改写**这个核心动机。

**标准答案：**

> **1. 改写方式：**

sql

复制

```
> SELECT * FROM users WHERE phone = '13800138000'
> UNION
> SELECT * FROM users WHERE email = 'test@example.com';
> 
```

引用

> **2. 索引利用的变化：**
>
> - UNION 把一条混合条件的 SQL 拆成两条独立 SQL，**每条子查询各自干净地走自己的索引**（ref 类型），不再依赖优化器的 Index Merge 策略；
> - 两条子查询的结果在内存中合并去重（基于主键），无需再碰磁盘。
>
> **3. 为什么改写往往更快：**
>
> - Index Merge 在 MySQL 中是优化器“不太擅长”的场景：合并策略选择、去重成本、回表次数都可能出现不理想的执行计划，很多场景下优化器干脆放弃 Index Merge 走全表扫描；
> - UNION 的执行路径是确定的、可预测的。
>
> **4. 加分细节——UNION vs UNION ALL：**
>
> - `UNION` 需要去重（内存中建临时表比较主键），有额外成本；
> - 本题中 phone 和 email 属于不同字段，**理论上可能存在某行同时命中两个条件**，若业务上确认不会重复或允许重复出现，用 `UNION ALL` 省去去重开销；
> - 另一个附带收益：注意改写后 phone 条件用的是字符串 `'13800138000'`，**顺带修复了第 1 条的隐式转换问题**。

</details>

### 题目2：覆盖索引（Covering Index）与回表查询的代价比较
> 现有表 `products`，包含字段：`id`（主键）、`name`、`category`、`price`、`stock`。创建了联合索引 `idx_category_price` 在 `(category, price)` 上。
> 请分析以下两条查询的执行路径差异，并回答：
> 1. 查询A：`SELECT id, category, price FROM products WHERE category = 'Electronics' AND price > 100;`
> 2. 查询B：`SELECT name, category, price FROM products WHERE category = 'Electronics' AND price > 100;`
     > 追问：查询A 的 `Extra` 字段预期出现什么？若将 `id` 字段也加入索引，改为 `(category, price, id)`，会提升查询A 的性能吗？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询A（覆盖索引）：查询所需字段 `id`（主键）、`category`、`price` 全部包含在索引 `idx_category_price` 中（InnoDB 辅助索引叶子节点默认包含主键值）。因此无需回表，直接遍历索引即可返回结果，`Extra` 字段显示 `Using index`。
- 查询B（需要回表）：查询字段包含 `name`，该字段不在索引中。因此需要先扫描索引 `(category, price)` 获取匹配的 `id`，再通过主键索引（聚簇索引）回表查询 `name` 字段，增加随机 I/O 开销。`Extra` 字段显示 `Using index condition`（若开启 ICP）或 `Using where`。
- 关于 `(category, price, id)` 索引：
  对查询A 而言，索引 B+ 树中原本的叶子节点存储的是主键 `id`，但 `(category, price)` 叶子节点也已经包含了 `id`（InnoDB 机制），所以显式加入 `id` 并不会改变覆盖索引的性质，反而会增大索引体积（非叶子节点和叶子节点存储更多数据），降低缓存效率，因此**不建议**为了覆盖索引额外添加主键字段。
</details>

**我的初答**：
**错漏点**：


### 题目3：联合索引中的范围条件对后续列的影响（最左前缀的停止规则与 ICP 优化）
> 现有表 `employees`，联合索引 `idx_dept_salary_age` 在 `(department_id, salary, age)` 上。
> 查询语句：`SELECT * FROM employees WHERE department_id = 3 AND salary > 50000 AND age = 30;`
> 请回答：
> 1. 该查询能利用索引的哪些列进行索引查找（Index Seek）？哪些列仅用于索引过滤（Index Filter）？
> 2. MySQL 5.6 引入的 **Index Condition Pushdown（ICP，索引下推）** 对此查询有何优化？在没有 ICP 时，执行过程有何不同？
> 3. 若将该索引改为 `(department_id, age, salary)`，`age = 30` 等值匹配与 `salary > 50000` 范围顺序交换后，索引利用程度会发生什么变化？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 索引利用分析（当前索引 `(dept_id, salary, age)`）：
  - **索引查找（Index Seek）**：仅能使用 `department_id`（等值匹配）和 `salary`（范围条件，因 `>` 是范围）。B+ 树通过 `dept_id=3` 定位后，再按 `salary > 50000` 扫描范围。
  - **索引过滤（Index Filter）**：`age = 30` 无法用于索引查找（因为 `salary` 是范围条件，索引在 `salary` 列后中断），但若开启 ICP，`age` 的过滤会在索引层面（存储引擎层）提前执行，减少回表次数。
- ICP（索引下推）优化：
  - **无 ICP**：存储引擎将 `dept_id=3` 和 `salary>50000` 匹配的所有主键 ID 返回给 Server 层，Server 层再根据 `age=30` 过滤（需回表查完整行，增加 I/O）。
  - **有 ICP**：存储引擎在扫描索引时，直接利用索引中的 `age` 字段值判断是否等于 30，仅将符合条件的行 ID 返回给 Server 层，大幅减少回表次数（尤其当 `age=30` 选择性低时效果显著）。
- 索引顺序调整影响（改为 `(dept_id, age, salary)`）：
  - 因 `department_id` 和 `age` 均为等值匹配，`salary` 为范围条件，新顺序能让索引查找用到 `dept_id` 和 `age` 两列，`salary` 作为范围条件继续在后续索引中使用。索引利用程度更高，因为 `age` 不再是被迫过滤的列，而是直接参与精准定位，通常性能更优（建议将等值条件列放在范围条件之前）。
</details>

**我的初答**：
**错漏点**：

---

## Day 44 (2026-09-07) —— SQL 优化（查询优化、索引优化、分页优化）

### 题目1：分页查询优化（深分页问题）—— LIMIT 10000, 20 为什么慢？如何优化？
> 现有订单表 `orders`（500万行），执行 `SELECT * FROM orders ORDER BY create_time DESC LIMIT 10000, 20;` 时，即使 `create_time` 有索引，查询依然很慢。请从 MySQL 的 LIMIT 执行机制（先扫描全部 10020 行再丢弃前 10000 行）解释原因，并给出两种优化方案。
> 追问：若使用 `SELECT * FROM orders WHERE create_time < '2024-01-01' ORDER BY create_time DESC LIMIT 20;`（基于上一页最后一条记录的时间戳），索引利用情况如何？存在什么隐患（如时间戳重复）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 深分页根源：`LIMIT 10000, 20` 需要先扫描 10020 行，然后丢弃前 10000 行（即使命中索引，也需遍历 10020 个索引条目），导致 I/O 和排序开销巨大。
- 优化方案1（子查询优化，延迟关联）：
  先使用覆盖索引获取主键 ID，再通过主键回表获取完整行。
  SELECT * FROM orders
  INNER JOIN (
  SELECT id FROM orders ORDER BY create_time DESC LIMIT 10000, 20
  ) AS tmp ON orders.id = tmp.id;
  优点：子查询仅扫描索引（覆盖），减少回表次数。
- 优化方案2（记录上一页最大时间戳，基于游标的分页）：
  SELECT * FROM orders
  WHERE create_time < '2024-01-01 15:30:00'
  ORDER BY create_time DESC
  LIMIT 20;
  优点：直接利用索引过滤，无偏移量开销，速度极快。
- 隐患：若 `create_time` 有重复值，可能丢失数据或重复显示（如多个订单在同一毫秒创建）。解决方案：在 `WHERE` 和 `ORDER BY` 中加入唯一字段（如 `id`），即 `WHERE create_time < ? OR (create_time = ? AND id < ?)` 保证结果唯一且有序。
</details>

**我的初答**：
**错漏点**：


### 题目2：ORDER BY 与 GROUP BY 的索引优化（文件排序 vs 索引排序）
> 现有联合索引 `idx_age_salary` 在 `(age, salary)` 上。请分析以下查询能否利用索引排序，并解释原因：
> 1. `SELECT * FROM employees WHERE age = 25 ORDER BY salary;`
> 2. `SELECT * FROM employees WHERE age > 25 ORDER BY age, salary;`
> 3. `SELECT * FROM employees ORDER BY salary, age;`
> 4. `SELECT age, COUNT(*) FROM employees GROUP BY age;`
     > 追问：若 `ORDER BY` 和 `GROUP BY` 混合使用（如 `GROUP BY age ORDER BY salary`），如何优化索引？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 查询1：能利用索引排序。因为 `age = 25` 是等值条件，索引在 `age` 列定位后，`salary` 列天然有序，可直接利用，避免文件排序（`Extra` 为 `Using index condition` 或 `Using where`）。
- 查询2：不能利用索引排序。`age > 25` 是范围条件，索引在 `age` 列后中断，`salary` 列的无序性导致无法直接利用索引排序。但 `ORDER BY age, salary` 中的 `age` 本身是范围条件，排序可能仍需文件排序。
- 查询3：不能利用索引排序。因查询跳过了联合索引的最左列 `age`，直接对 `salary` 排序，不满足最左前缀原则，需文件排序。
- 查询4：能利用索引排序分组。`GROUP BY age` 本质上需要按 `age` 排序，若 `age` 在联合索引的最左列，索引本身有序，可避免临时表和文件排序（`Extra` 为 `Using index` 或 `Using index for group-by`）。
- GROUP BY + ORDER BY 混合优化：
  - 若 `GROUP BY age ORDER BY salary`，需在 `(age, salary)` 上建索引（符合最左前缀），且 `ORDER BY` 的列须与 `GROUP BY` 列一致或在其后。若不一致，必然产生文件排序。建议调整查询逻辑或将 `ORDER BY` 改为与 `GROUP BY` 同一列。
</details>

**我的初答**：
**错漏点**：


### 题目3：COUNT、DISTINCT、UNION 的性能优化（避免全表扫描与临时表）
> 现有用户表 `users`（500万行），执行 `SELECT COUNT(DISTINCT city) FROM users WHERE status = 1;` 时耗时超过 5 秒，`status` 和 `city` 均为普通索引。请分析慢的原因，并给出优化建议。
> 追问：若需统计活跃城市数和各城市用户数，如何用一条 SQL 既返回活跃城市数，又返回城市详情列表？是否应该将 `COUNT(DISTINCT city)` 与 `GROUP BY city` 分开执行？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 慢的原因：
  1. `COUNT(DISTINCT city)` 需要去重，若 `city` 无索引或索引选择性低，会创建临时表（`Using temporary`），增加 I/O 和内存开销。
  2. `WHERE status = 1` 与 `DISTINCT city` 组合，索引利用有限：若 `(status, city)` 联合索引存在，可快速过滤；若仅有单列索引，优化器可能选择扫描 `status` 索引再回表，或全表扫描。
- 优化方案：
  1. 创建联合索引 `(status, city)`，覆盖 `WHERE` 和 `DISTINCT`，避免回表。
  2. 若业务允许，使用近似值（如 `EXPLAIN` 的 `rows` 估算）替代精确 COUNT。
  3. 若必须精确，可单独维护一张 `city_stats` 统计表，通过触发器或定时任务更新城市计数。
- 返回活跃城市数 + 城市详情列表：
  - 方法1（子查询）：`SELECT (SELECT COUNT(DISTINCT city) FROM users WHERE status=1) AS total_cities, city, COUNT(*) FROM users WHERE status=1 GROUP BY city;`（但 `SELECT` 子查询会重复扫描，效率低）。
  - 方法2（分开查询更优）：先 `SELECT COUNT(DISTINCT city) FROM users WHERE status=1;` 再 `SELECT city, COUNT(*) FROM users WHERE status=1 GROUP BY city;`。原因：`GROUP BY` 需要扫描全部数据，而 `COUNT(DISTINCT)` 可利用索引快速统计，分开执行可分别利用各自最优路径，总体耗时更短。
</details>

**我的初答**：
**错漏点**：

---

## Day 45 (2026-09-08) —— MySQL 视图（View）及其在 Java 后端开发中的应用

### 题目1：视图的本质与使用场景（复杂查询封装 vs 性能陷阱）
> 在 Java 后端开发中，我们常将复杂的多表关联查询封装成视图。请回答：
> 1. 视图是什么？它存储数据吗？执行 `SELECT * FROM view_name` 时，MySQL 底层做了什么？
> 2. 相比在 MyBatis 的 XML 中直接编写复杂的 `JOIN` 语句，使用视图有哪些优点和缺点？
> 3. 为什么说滥用视图可能导致难以排查的性能问题（尤其是在涉及其他视图嵌套时）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 视图本质：视图是一个**虚拟表**，不存储实际数据。它只是保存了一条 `SELECT` 语句。当查询视图时，MySQL 会**动态合并（Merge）** 视图的定义 SQL 到查询语句中，或通过**临时表（Temptable）** 算法执行。
- 优点：
  1. 逻辑封装：隐藏底层表结构的复杂性，提供统一的数据接口（如报表统计视图），降低业务代码的耦合度。
  2. 安全性：可只暴露必要字段给不同角色（如不显示薪资字段）。
  3. 简化查询：避免在 Java 代码中拼接过长的多表 SQL。
- 缺点（Java 后端需警惕）：
  1. 性能黑盒：若视图定义涉及多张千万级大表的关联，每次查询都会实时计算，极耗资源。且视图嵌套多层时，执行计划极易混乱。
  2. 难以调试：如果在 MyBatis 中调用视图报错，错误信息往往难以定位到具体的底层表。
  3. 无法利用索引优化器：对于 `MERGE` 算法无效的视图（如包含 `GROUP BY`、`DISTINCT`），会物化为临时表，无法利用外层查询条件直接下推到基表索引。
- 后端开发建议：视图适用于**读多写少、逻辑固定且数据量可控**的统计场景。对于高频、大数据量的复杂查询，建议在 Java 层使用分步查询或 Elasticsearch，而非依赖 MySQL 视图。
</details>

**我的初答**：
**错漏点**：


### 题目2：视图的更新限制与 JDBC/MyBatis 操作中的常见陷阱（不可更新视图）
> 在 Spring Boot 项目中，使用 JPA 或 MyBatis 将数据更新到视图时，有时会报错。请回答：
> 1. 什么是“可更新视图（Updatable View）”？MySQL 中什么样的视图是**不可更新**的？（请列举至少 3 种具体 SQL 特征）。
> 2. 如果在 MyBatis 中执行 `UPDATE view_name SET ...` 更新了一个不可更新视图，会发生什么（客户端报什么错）？
> 3. 若视图是可更新的，更新视图的数据会影响基表吗？这种操作在微服务架构中存在什么风险？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 可更新视图定义：视图的 `SELECT` 语句必须满足：没有使用 `DISTINCT`、没有聚合函数（`SUM`/`AVG`/`COUNT`）、没有 `GROUP BY` / `HAVING`、没有子查询（某些情况下）、且必须包含基表中的所有 `NOT NULL` 列（若无默认值）。
- 不可更新视图的典型特征（涉及以下任一即不可更新）：
  1. 使用聚合函数（`SUM`, `MAX`, `COUNT` 等）。
  2. 使用了 `DISTINCT` 关键字。
  3. 存在 `GROUP BY` 或 `HAVING` 子句。
  4. 存在子查询（`SELECT ... FROM (SELECT ...)`）。
  5. 由多张表连接而成（`JOIN`）且未做特殊处理（通常不可更新）。
- 操作报错：Java 后端（JDBC/MyBatis）会抛出 `java.sql.SQLException: View 'xxx' is not updatable`，通常需要在业务代码中捕获并处理，或避免直接通过视图进行写操作。
- 更新视图的影响：更新可更新视图**实际上会修改基表**的数据。风险在于：如果视图只展示了基表的部分字段（如隐藏了敏感列），开发人员若误以为操作的是独立表，可能会导致数据被意外修改或逻辑不一致。在微服务中，不推荐直接通过视图进行 `INSERT`/`UPDATE`，应统一通过 Service 层的领域模型操作。
</details>

**我的初答**：
**错漏点**：


### 题目3：WITH CHECK OPTION 的作用与 Java 业务逻辑中的数据一致性保障
> 创建视图 `CREATE VIEW active_users AS SELECT id, name, status FROM users WHERE status = 'active';` 时，若加上 `WITH CHECK OPTION`。请回答：
> 1. `WITH CHECK OPTION` 的作用是什么？
> 2. 如果在 Java 代码中通过该视图执行 `UPDATE active_users SET status = 'inactive' WHERE id = 1;`，会发生什么？为什么？
> 3. 在 Java 后端业务开发中，视图的 `WITH CHECK OPTION` 能替代 Service 层的参数校验吗？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `WITH CHECK OPTION` 作用：防止通过视图对基表进行插入或更新操作时，导致结果行**脱离视图的可见范围**。即，更新后的数据必须依然满足视图定义的 `WHERE` 条件。
- 执行结果（`UPDATE active_users SET status = 'inactive'`）：MySQL 会拒绝该更新，抛出 `SQL Error [1369] [HY000]: CHECK OPTION failed 'test_db.active_users'`。因为 `status='inactive'` 不满足视图定义的 `status='active'` 条件，修改成功后该行会“消失”在视图中，违反了 `WITH CHECK OPTION` 的约束。
- 能否替代 Service 层校验：**绝对不能**。
  1. `WITH CHECK OPTION` 仅保证数据不“滑出”视图，无法保证其他复杂的业务逻辑（如唯一性、金额范围、状态机流转）。
  2. 数据库层的校验错误以 SQLException 抛出，Java 层需额外解析异常信息，增加了代码的脆弱性。
  3. 若应用直接使用基表（而非视图）进行更新，该约束完全无效。因此，**数据校验逻辑必须牢牢掌握在 Java Service 层**，视图仅作为查询辅助工具。
</details>

**我的初答**：
**错漏点**：

---

## Day 46 (2026-09-09) —— MySQL 存储过程（Stored Procedure）与 Java 后端开发

### 题目1：存储过程在 Java 微服务架构中为何被视为“反模式”？业务逻辑该放 Java 还是 DB？
> 在传统的单体项目中，存储过程常用于封装复杂计算。但在现代 Spring Boot 微服务架构中，阿里规范通常禁止使用存储过程。请从**数据库扩展性（水平分库）**、**应用层无状态设计**、**版本控制与 CI/CD** 三个维度，分析将业务逻辑写入存储过程对 Java 后端团队带来的运维痛点。
> 追问：若遇到必须使用存储过程才能优化的复杂报表统计（否则 Java 层查询效率极低），你会如何权衡决策？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 扩展性（水平分库）：存储过程强依赖特定的数据库（如 MySQL 语法与 Oracle 不兼容）。当业务量增长需水平分库（ShardingSphere 分表）时，存储过程无法跨库执行，导致必须重写代码，扩展性极差。
- 无状态设计：微服务要求应用层无状态，便于弹性伸缩。存储过程将状态（逻辑）固化在数据库层，使数据库成为“有状态”的计算节点，无法通过增加应用实例来分摊计算压力，数据库 CPU 极易成为瓶颈。
- 版本控制与 CI/CD：存储过程存储在数据库服务端，难以像 Java 代码一样使用 Git 管理版本、进行 Code Review 或实现灰度发布。更新存储过程需要 DBA 权限，容易引发线上事故，且回滚困难。
- 权衡决策（报表场景）：若 Java 层关联查询（JOIN）导致 OOM 或超时，可采用以下方案：
  1. 将复杂统计逻辑下沉到数据仓库（如 ClickHouse）或 Elasticsearch，而非 MySQL。
  2. 使用定时任务（如 XXL-JOB）提前计算报表结果存入中间表，Java 直接查询结果。
  3. 若迫不得已使用存储过程，必须严格封装在独立的 DAO 层，并在注释中注明替代方案，且确保不涉及分库键（Sharding Key）。
</details>

**我的初答**：
**错漏点**：


### 题目2：Java 中调用存储过程（JDBC / MyBatis）的异常捕获与事务传播机制
> 在 Spring 中，若一个 `@Transactional` 方法调用了存储过程，而存储过程中发生了错误（如 `SIGNAL SQLSTATE '45000'`），请分析：
> 1. 存储过程的报错是如何映射到 Java 的 `SQLException` 的？Java 层应如何捕获该特定业务错误码（如 45000）并转换为业务异常？
> 2. 存储过程内部开启事务（`START TRANSACTION` / `COMMIT`）与 Spring 的 `@Transactional` 一起使用时，可能导致什么隔离性问题？（提示：嵌套事务与 Savepoint）
> 3. 若存储过程执行时间过长，会如何影响 HikariCP 连接池的性能？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 异常映射：存储过程通过 `SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '库存不足';` 抛出自定义错误。JDBC 驱动将其封装为 `java.sql.SQLException`，携带 SQLState（45000）和错误信息。Java 层可通过 `catch (SQLException e)` 并判断 `e.getSQLState().equals("45000")` 转换为业务异常（如 `throw new BusinessException(e.getMessage())`）。
- 事务传播冲突：
  - 存储过程内部的 `COMMIT` 会强制提交事务，破坏 Spring 声明式事务的原子性（Spring 管理的是 JDBC Connection，存储过程提交会影响该连接的 autocommit 状态）。
  - 若 Spring 开启了事务，存储过程内的事务操作可能导致外层业务逻辑无法回滚。**最佳实践**：存储过程中避免事务操作，由 Spring 统一管理事务。
- 连接池影响：存储过程执行期间会长期持有数据库连接（JDBC Connection）。若执行耗时过长（如 5 秒），高并发下会导致 HikariCP 连接池耗尽（`Connection is not available`），阻塞其他业务线程。解决方案：设置 JBDC 超时（`statement.setQueryTimeout(30)`），并在存储过程逻辑中避免长循环。
</details>

**我的初答**：
**错漏点**：


### 题目3：存储过程的性能陷阱（预编译优势丧失与索引失效）
> 有人认为存储过程“更快”，因为减少了网络传输（少发 SQL 语句）。但在实际生产环境中，存储过程反而可能拖垮数据库。请结合 MySQL 的**执行计划缓存（Plan Cache）失效机制**和**索引统计信息更新延迟**，解释为何存储过程在高并发下性能可能大幅下降？对比使用 MyBatis 拼接 SQL 的方式，哪者更容易利用 MySQL 8.0 的查询优化器特性？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 预编译陷阱：MySQL 不支持存储过程的“预编译”跨会话重用执行计划（相较于 Oracle）。存储过程中的 SQL 会根据传入参数动态生成执行计划，若参数值变化导致“数据倾斜”（如参数为 '北京' 返回 100 行，参数为 '上海' 返回 100万 行），MySQL 无法为不同参数保留多套执行计划，重复硬解析消耗 CPU。
- 统计信息延迟：存储过程往往包含复杂的动态 SQL（`PREPARE` 语句），若底层表数据发生大量变更，统计信息未及时更新（`ANALYZE TABLE`），优化器会选择错误的索引（如走全表扫描），且存储过程内部的 SQL 难以通过 `EXPLAIN` 单独调试，排查难度远高于 Java 日志。
- 与 MyBatis 对比：
  - 现代 MyBatis 配合 `logback` 可以打印完整 SQL 并记录执行耗时，DBA 可直接通过 `EXPLAIN` 分析单条 SQL。
  - 存储过程与 Java 应用解耦导致 DBA 难以复现 Java 上下文。且 Java 应用更易于利用 `Hint` 或 `optimizer_switch` 进行细粒度调优，存储过程则难以传递外部优化指令。
- 结论：除非是极端复杂的“单次批量 ETL”任务，否则在 OLTP（在线事务处理）场景下，**严禁使用存储过程**，优先在 Java 层构建灵活可控的 SQL。
</details>

**我的初答**：
**错漏点**：

---

