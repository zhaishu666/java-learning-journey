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
1. 

**错漏点**：


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
**错漏点**：

---