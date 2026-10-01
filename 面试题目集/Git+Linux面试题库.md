## Day 1 (2026-09-20) —— Linux 基础与 Java 后端实战

### 题目1：vi/vim 编辑器在服务器端修改配置文件的常用操作与高效技巧
> 在 Java 后端开发中，经常需要在 Linux 服务器上修改 Nginx、Tomcat 或 application.yml 等配置文件。请回答：
> 1. vim 有哪几种常用模式？如何进入编辑模式、命令模式、末行模式？如何保存、退出、强制退出、保存并退出？
> 2. 请列举 vim 中常用的查找、替换、跳转、删除、复制粘贴命令（至少各两个）。
> 3. 若修改配置文件时误操作，如何不保存直接退出？如何在不重启 vim 的情况下重新加载文件？
     > 追问：在 vim 中如何快速跳到文件首行、尾行、指定行？如何批量替换所有匹配的字符串？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 三种模式：
    - 命令模式（默认）：移动光标、删除、复制粘贴。
    - 编辑模式（插入模式）：按 i、a、o、I、A、O 进入，可编辑文本。
    - 末行模式：按 : 进入，执行保存、退出、替换等。
- 保存退出：
    - :w 保存
    - :q 退出
    - :wq 或 :x 保存并退出
    - :q! 强制退出不保存
    - ZZ（命令模式）保存并退出
- 常用命令：
    - 查找：/word 向下查找，?word 向上查找，n 下一个，N 上一个。
    - 替换：:s/old/new/g 替换当前行所有；:%s/old/new/g 替换全文；:%s/old/new/gc 替换前确认。
    - 跳转：gg 首行，G 尾行，:n 跳到第 n 行，0 行首，$ 行尾。
    - 删除：dd 删除整行，dw 删除单词，d$ 删除到行尾，dG 删除到文件尾。
    - 复制粘贴：yy 复制整行，p 粘贴到下一行，P 粘贴到上一行。
- 误操作不保存退出：:q!。重新加载文件：:e!。
- 追问：
    - 快速跳转：gg 首行，G 尾行，:100 跳到第 100 行。
    - 批量替换：:%s/old/new/g 替换全文，加 c 确认。
</details>

**我的初答**：
1. vim的常见模式为命令模式(该模式下所有输入都会以命令形式执行),通过'vim 指定文件'进入,编辑模式(该模式下可以对文件进行编辑),通过命令模式下按i进入,按esc退出到命令模式.
末行命令模式,用于保存或退出修改的内容,命令模式下输入:进入.输入:w保存,:q退出,:q!强制退出,:wq保存并退出
2. 查找: /进入查找模式,n向下查找,N向上查找,替换: 不知道.跳转: gg跳转到行首,G跳转到行尾.删除: dd删除整行,dw删除单词.yy复制一行,d粘贴
3. :q!退出.

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

- **模式部分**：答得完整，术语准确（命令模式/编辑模式/末行模式，切换键都对）。

- **查找**：`/ + n/N` 正确，漏了反向查找 `?`。

- **替换**：空白——这是必答点，至少记住 `:%s/old/new/g`。

- **跳转**：`gg`/`G` 正确，注意表述应为"文件首行/文件尾"，不是"行首/行尾"（行首行尾是 `0` 和 `$`）。

- **复制粘贴**：`yy` 正确，但**粘贴是 `p`，不是 `d`**——这个错误在面试里很扎眼。

- **不保存退出**：`:q!` 正确。

- **不重启 vim 重新加载文件**：没答，答案是 `:e!`

</details>

### 题目2：Linux 用户与用户组管理，以及为何不建议用 root 运行 Java 应用
> 在部署 Spring Boot 应用时，通常需要创建专用用户来运行服务。请回答：
> 1. 如何创建用户、设置密码、创建用户组、将用户加入组？请写出常用命令。
> 2. 如何修改文件的所有者和所属组？chmod 的权限数字表示法（如 755、644）分别代表什么？
> 3. 为什么不建议使用 root 用户运行 Java 应用？如果应用被入侵，使用专用用户能带来哪些安全隔离？
     > 追问：如何查看当前登录用户、所有用户列表、用户所属组？如何切换用户？su 和 sudo 有什么区别？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 用户与组管理：
    - 创建用户：useradd -m -s /bin/bash username（-m 创建家目录，-s 指定 shell）
    - 设置密码：passwd username
    - 创建组：groupadd groupname
    - 将用户加入组：usermod -aG groupname username（-a 追加，-G 指定组）
    - 查看用户所属组：groups username
- 文件权限：
    - 修改所有者：chown username:groupname file
    - 修改权限：chmod 755 file（所有者 rwx=7，组 r-x=5，其他 r-x=5）
    - 权限数字：r=4，w=2，x=1。755 = rwxr-xr-x；644 = rw-r--r--。
- 不用 root 运行 Java 应用的原因：
    1. 最小权限原则：应用只需读写特定目录和端口，无需系统级权限。
    2. 安全隔离：若应用被攻破，攻击者仅获得该用户权限，无法破坏系统核心文件或安装后门。
    3. 避免误操作：root 权限下误删系统文件风险极高。
    4. 审计与资源限制：可针对专用用户设置磁盘配额、CPU 限制等。
- 追问：
    - 查看当前用户：whoami
    - 所有用户：cat /etc/passwd
    - 切换用户：su - username
    - su 与 sudo：su 切换用户身份，需目标用户密码；sudo 以其他用户（默认 root）执行命令，需当前用户密码且在 sudoers 列表中。生产环境推荐使用 sudo 精细授权。
</details>

**我的初答**：
1. 创建用户: useradd [-g -d] 用户名. 其中-g表示指定用户的用户组,-d表示指定home目录 设置密码遗忘了
2. 创建用户组: groupadd 用户组名
3. 通过chown命令可以修改文件的所有者和所属组;语法: `chown [-R] [用户][:][用户组] 文件或文件夹` -R表示级联修改文件夹内部的所属
4. chmod命令中r权限为4,w权限为2,x权限为1,所以755就代表rwxr-xr-x,644代表rw-r--r--
5. root用户权限太大,容易误删或和误改从而破坏Linux系统.如果被入侵,专用用户一般只能操作直接home目录下的内容,这样最多只会损失一个home目录下的内容
6. 登录用户直接在命令行左侧就能看到,查看用户所属组: id [用户名]; 查看用户列表: `getent passwd`
7. su 用户名 :切换用户 sudo只是临时通过管理员权限执行命令,su则是切换用户使用

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

你的初答点评

- **创建用户**：`useradd -g -d` 参数正确，加分项可以补 `-m`（自动创建家目录，很多发行版默认不建）。

- **设置密码**：空白——答案是 `passwd 用户名`，不能忘。

- **用户加入组**：没答——这是题目明确要求的，用 `usermod -aG`。

- **chmod/chown**：完全正确，`755 → rwxr-xr-x`、`644 → rw-r--r--` 转换熟练，这部分可以满分。

- **不用 root**："最多损失一个 home 目录"说小了——隔离的价值不止目录范围，还包括无法动其他服务、无法读敏感文件、无法杀别的进程，以及权责可审计。

- **查看当前用户**："看命令行左侧"不是命令——标准答案是 `whoami`（或 `who` 看所有登录会话）。

- **su vs sudo**：核心区别答对了，再补"sudo 有授权边界和审计日志"就完整了

### 为什么不用 root 运行 Java 应用

**root 的风险：**

1. **无权限边界**：root 进程可以读写系统任何文件（`/etc/shadow`、其他应用的数据、系统配置）、杀掉任何进程、装软件、开端口。Java 应用一旦有漏洞（RCE、任意文件读写、反序列化），攻击者拿到的就是**整台机器的最高权限**——这就是"最小权限原则"。

2. **误操作代价大**：root 下一条 `rm -rf` 路径写错就是事故；普通用户会因权限不足被系统拦下，相当于一层保险。

3. **不可审计**：所有操作都是 root 干的，出问题分不清是哪个服务、哪个运维做的。


**专用用户的隔离价值（入侵场景下）：**

- 攻击者只能读写 `appuser` 有权限的文件——改不了系统配置、读不到其他应用的密钥和数据、装不了 rootkit；

- **横向移动被阻断**：拿不到 root，就无法轻易攻击同机的其他服务和其他用户；

- 配合文件权限（如应用目录 750、配置文件 640），把破坏范围锁死在应用自身目录内；

- 出问题时可通过该用户的日志、进程归属快速定位影响面。


### 追问答案

bash复制

```bash
whoami                 # 当前用户是谁
who / w                # 当前有哪些用户登录了系统
cat /etc/passwd        # 查看所有用户列表（或 getent passwd）
id appuser             # 查看用户的 uid、主组、所有附加组
groups appuser         # 只看所属组
su - appuser           # 切换用户（- 表示同时加载该用户的环境变量，推荐）
exit                   # 退回原用户
```

**su 与 sudo 的区别：**

表格

| 维度  | su  | sudo |
| --- | --- | --- |
| 本质  | **切换身份**，变成另一个用户（常切 root） | **以他人身份执行单条命令**，执行完就回来 |
| 密码  | 要输入**目标用户**的密码（root 密码扩散风险） | 输入**自己**的密码，目标密码不需要 |
| 授权粒度 | 一刀切——知道 root 密码就是完整 root | 可在 `/etc/sudoers` 精细控制：允许谁、执行哪些命令 |
| 审计  | 难追踪是谁切的 | 每条 sudo 命令都有日志记录，**可追责** |

结论：生产环境推荐 sudo（最小授权 + 可审计），避免把 root 密码给多个人。
</details>

### 题目3：文件查找与文本处理命令在 Java 后端日志排查中的应用
> Java 后端服务通常将日志输出到文件，排查问题时需要快速定位。请回答：
> 1. 如何实时查看日志文件的最新内容？如何查看文件末尾 100 行？如何持续跟踪文件并过滤关键字？
> 2. 如何使用 grep 查找包含异常堆栈的行？如何同时显示匹配行的前后 5 行？如何统计某个错误出现的次数？
> 3. 如何查找占用 CPU 最高的 Java 进程？如何查看该进程打开的端口和网络连接？
     > 追问：如何使用 find 查找 7 天前的日志文件并删除？如何使用 awk 统计日志中每个 IP 的访问次数？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 实时查看日志：
    - tail -f app.log（实时跟踪）
    - tail -n 100 app.log（查看末尾 100 行）
    - tail -f app.log | grep "ERROR"（实时过滤 ERROR）
- grep 用法：
    - grep -n "Exception" app.log（显示行号）
    - grep -C 5 "Exception" app.log（显示匹配行前后 5 行，-A 后，-B 前）
    - grep -c "ERROR" app.log（统计次数）
    - grep -i "error" app.log（忽略大小写）
- 进程与网络：
    - 查看 CPU 最高：top -c，然后按 P 排序；或 ps -ef | grep java，再 top -p PID。
    - 查看端口：netstat -tunlp | grep PID 或 ss -tunlp | grep PID
    - 查看网络连接：netstat -anp | grep PID
- 追问：
    - find /logs -name "*.log" -mtime +7 -exec rm {} \;
    - awk '{print $1}' access.log | sort | uniq -c | sort -nr | head -10
</details>

**我的初答**：
1. 跟踪文件:tail Linux路径 跟踪尾部100行:tail -100 Linux路径 持续跟踪并过滤:tail -f Linux路径 | grep "需要过滤的内容"
2. grep "Exception" 文件 查找包含Exception的行, 显示匹配前后5行:-C 5 统计次数: -c
3. 不了解

**错漏点**：

---

## Day 2 (2026-09-21) —— Linux 权限

> 题目 1：Linux 中文件与目录的 r、w、x 权限语义不同。请分别说明文件 rwx 和目录 rwx 的含义。结合 Java 后端部署场景：若 /opt/myapp/static 目录权限为 644，文件 index.html 权限为 777，Nginx 以 www-data 用户运行，能否读取该文件？为什么？删除该目录下的文件需要什么权限？如果目录设置了 sticky 位，删除规则如何变化？

<details>
<summary>标准解析</summary>

文件：
- r：读取文件内容。
- w：修改文件内容。
- x：执行文件，或对脚本/二进制由内核加载执行。

目录：
- r：列出目录项，即 ls 能读到文件名列表。
- w：在目录内创建、删除、重命名目录项。
- x：进入目录、查找目录项、访问目录内文件的 inode。路径解析每一级目录都需要 x。

场景：
/opt/myapp/static 为 644，即 rw-r--r--，没有 x。Nginx 即使知道 index.html 文件名，也无法完成路径解析到该 inode，因此不能打开文件。文件 index.html 为 777 也不解决问题，因为父目录缺少 x。若目录为 000 而文件 777，同样无法访问。

删除文件：
删除、重命名文件看父目录的 w 和 x，与文件自身权限无关。父目录需 w+x。若父目录设置 sticky 位，如 /tmp，只有文件所有者、目录所有者或 root 可以删除/重命名该文件。

追问：
- 目录只有 r 没有 x 时，ls 可能看到文件名，但 ls -l 无法获取详细属性。
- Java 进程写日志时，日志目录必须对运行用户有 w+x；日志文件本身有 w 即可，但创建新文件需要目录 w+x。

</details>

**我的初答：**
1. 文件rwx: r代表查看该文件内容的权限,w代表修改内容的权限,x代表执行该文件的权限
2. 目录rwx: r代表查看目录内容的权限,w代表在该目录下添加或删除目录及文件的权限,x代表切换到该工作目录或者执行该目录下内容的权限
3. 不能读取该文件,因为该用户虽然有文件x权限,但却没有其父目录的x权限,删除该目录下的文件需要/opt/myapp/static的w权限;通过chmod -R 645 /opt/myapp/static 可让文件执行
4. sticky位的知识不了解

**错漏点：**
<details>
<summary>点击展开错漏点</summary>

你的初答点评

- **文件 rwx**：正确。

- **目录 rwx**：大体正确，但 x 的表述要更精确——目录的 x 是**"进入/穿越"权限**（access 目录下的 inode），不只是"切换工作目录"；它同时也是"按文件名访问目录内任何文件"的前提。这个精确表述正是后面场景题的解题钥匙。

- **能否读取**：结论和理由都对——www-data 虽然对 index.html 有 777，但卡在父目录没有 x，**路径穿越就失败了**。可以再补一个细节：目录 644 意味着**连所有者自己都没有 x**，这目录实际上谁都进不去（root 除外），所以 644 对目录来说基本是个错误配置。

- **删除权限**：答了一半——删除文件需要**目录的 w + x**（w 才能改目录项，x 才能进入目录），跟你对文件本身有没有权限**无关**。漏了 x。

- **`chmod -R 645` 的写法有问题**：`-R` 会把目录里的文件也设成 645（文件多了个组内 x，没必要且不规范）；修复目录权限的标准做法是 `chmod 755 /opt/myapp/static`（或 `chmod o+x`）。另外"让文件执行"的说法不对，这里目的是让目录可进入，不是执行文件。

- **sticky 位**：空白，必须补——这是这题的压轴点。


---

## 参考答案

### 1. 文件与目录的 rwx 语义

表格

| 权限  | 对文件 | 对目录 |
| --- | --- | --- |
| r   | 读取文件内容（cat、less） | 列出目录里的**文件名**（ls） |
| w   | 修改文件内容 | 在目录内**创建、删除、重命名**文件/子目录（修改目录项） |
| x   | 作为程序/脚本执行 | **进入（cd）和穿越该目录**——按路径访问目录内任何文件的前提 |

经典辨析（面试爱考）：目录只有 r 没有 x 时，`ls` 能看到文件名，但 `cat` 里面任何文件都报 Permission denied——**看到 ≠ 能摸到**。

### 2. 场景分析：能否读取 index.html

**不能。** 访问 `/opt/myapp/static/index.html` 需要路径上**每一级目录都有 x 权限**。static 目录是 644（rw-r--r--），www-data 作为"其他人"只有 r 没有 x——无法穿越该目录，内核在做路径解析（name resolution）时就拒绝了，**根本轮不到检查 index.html 自身的 777**。

类比：目录是楼道的门，文件是房间里的东西。楼道门锁着，房间门敞着也没用。

修复：`chmod 755 /opt/myapp/static`（目录标准权限：所有者 rwx，组和其他人 r-x）。部署经验：Nginx 读取静态资源，**目录一律 755、文件一律 644**，并保证路径上每一级目录 others 都有 x。

### 3. 删除文件需要什么权限

删除（或重命名）目录下的文件，需要的是**目录的 w + x 权限**，与被删文件自身的权限**无关**——因为删除的本质是修改目录里的条目，不是改文件。

推论（很反直觉，常考）：一个文件即使设成 444 只读，只要所在目录对你是 w+x，你照样能删掉它（rm 会提示确认，加 -f 直接删）。**保护文件不被删，要靠锁目录权限，而不是锁文件权限。**

### 4. Sticky 位（粘滞位）

目录设置 sticky 位后（`chmod +t dir`，权限显示为 `drwxrwxrwt`，典型例子就是 `/tmp` 的 1777）：

- 目录对所有人可写，但**只有三种人能删除/重命名目录下的文件：文件的所有者、目录的所有者、root**；

- 其他用户即使有目录的 w 权限，也只能删自己的文件，删不了别人的。


应用场景：多人共享的临时目录（/tmp）、多应用共用上传目录——既保证大家都能写，又防止互相误删/恶意删除。它解决的是"可写目录里删除权失控"的问题。

---

这题的答题主线可以总结成一句话：**"文件的权限管内容，目录的权限管路径和增删"**——把这条主线说清楚，三个小问自然就都顺了。
</details>

> 题目 2：请解释 umask 的作用，并计算 umask 为 027 时新建普通文件和目录的默认权限。命令 chmod 2775 /var/log/myapp 中 2 是什么权限？对目录有何效果？SUID、SGID、Sticky 分别作用于什么对象？Java 后端服务以 app 用户运行，日志目录需要 app 可写、同组开发可读、其他用户无权限，且新日志文件自动继承该组，如何设置目录权限和 ACL？给出关键命令。

<details>
<summary>标准解析</summary>

umask 是权限掩码，创建文件/目录时从基础权限中去掉对应位。
基础权限：
- 普通文件：666，即 rw-rw-rw-，通常不会默认给 x。
- 目录：777，即 rwxrwxrwx。
  计算：
  umask 027 -> 文件 666 & ~027 = 640，即 rw-r-----；目录 777 & ~027 = 750，即 rwxr-x---。
  验证：
  umask
  touch a.txt
  mkdir d
  ls -ld a.txt d

chmod 2775：
2 是 SGID。作用于目录时，目录内新建的文件和子目录会继承该目录的组，而不是创建者的主组。2775 权限为 rwxrwsr-x。
SUID：4，作用于可执行文件，执行时有效用户 ID 变为文件所有者。对普通脚本通常无效。
SGID：2，作用于可执行文件时有效组 ID 变为文件所属组；作用于目录时继承组。
Sticky：1，作用于目录，限制删除/重命名，只有文件所有者、目录所有者或 root 可操作。

场景设置：
假设目录 /var/log/myapp，属主 app，属组 dev。
chown app:dev /var/log/myapp
chmod 2775 /var/log/myapp
若需要更细粒度，使用 ACL：
setfacl -m u:app:rwx /var/log/myapp
setfacl -m g:dev:r-x /var/log/myapp
setfacl -m o::- /var/log/myapp
setfacl -d -m g:dev:r-x /var/log/myapp
setfacl -d -m u:app:rwx /var/log/myapp
getfacl /var/log/myapp

追问：
- umask 只影响新建对象，不影响已有文件。
- 目录的 x 权限对 Java 服务写日志、创建临时文件至关重要。
- 不要用 chmod 777 掩盖权限问题，应最小权限。

</details>

**我的初答：**

**错漏点：**

> 题目 3：线上 Java 服务以 app 用户运行，启动时报日志目录 Permission denied。请给出系统性排查思路和关键命令。重点说明：如何检查 app 用户身份和所属组？如何沿路径逐级检查父目录 x 权限？如何检查 ACL、sudo 权限、systemd 服务运行用户、SELinux/AppArmor？如果应用需要绑定 80 端口但不想用 root，有哪些方案？请结合 Java 后端部署说明最小权限原则。

<details>
<summary>标准解析</summary>

排查顺序：
1. 确认进程运行用户：
   ps -ef | grep java
   systemctl status myapp
   systemctl cat myapp
   查看 systemd 单元中的 User=、Group=、WorkingDirectory=、Environment=。
2. 确认用户身份：
   id app
   groups app
3. 检查目标路径权限和每一级父目录：
   namei -l /var/log/myapp/app.log
   ls -ld /var /var/log /var/log/myapp
   每一级目录都需要对 app 有 x 权限；最终目录需要 w+x 才能创建文件。
4. 检查 ACL：
   getfacl /var/log/myapp
5. 检查 sudo 权限：
   sudo -l -U app
6. 检查安全模块：
   getenforce
   sestatus
   aa-status
   查看 audit.log 或 journalctl 中的 AVC 拒绝。
7. 检查端口和能力：
   ss -lntp
   getcap /path/to/java
   非 root 绑定 80：
  - 使用 Nginx 反向代理到 8080。
  - 授予能力：
    setcap cap_net_bind_service=+ep /path/to/java
    注意这会让该 Java 二进制具备绑定低端口能力，需评估风险。
  - 使用 systemd 的 AmbientCapabilities=CAP_NET_BIND_SERVICE。
8. 最小权限原则：
  - 专用 app 用户运行服务。
  - 日志、上传、临时目录只授予必要权限。
  - 使用 ACL 做细粒度授权。
  - 避免 chmod 777、避免直接 root 运行 Java 服务。
  - systemd 可配合 ProtectSystem、PrivateTmp、NoNewPrivileges 等加固。

追问：
- Permission denied 不一定只是文件权限，可能是父目录无 x、ACL 拒绝、SELinux、AppArmor、systemd 沙箱、能力不足。
- 如果目录权限正确但仍失败，优先看 journalctl -u myapp -e 和 audit 日志。

</details>

**我的初答：**

**错漏点：**

## Day 3 (2026-09-22) —— Linux systemctl、软连接、日期与时区

> 题目 1：systemd 管理 Java 服务。请说明 Type=simple、forking、notify 的区别，Restart=always 与 RestartSec、StartLimitIntervalSec 如何配合。给出一个 Spring Boot 应用的 systemd unit 示例，要求：以 app 用户运行、工作目录 /opt/myapp、JVM 参数 -Xms512m -Xmx512m、日志输出到 journal、开机自启、异常自动重启。说明 systemctl daemon-reload、enable、start、status、journalctl -u myapp -f 的作用。追问：Java 进程被 OOM killer 杀死后 systemd 会重启吗？KillMode=control-group 对 Java 进程树有什么影响？

<details>
<summary>标准解析</summary>

Type：
- simple：默认，ExecStart 启动的进程即主进程，systemd 认为启动完成即服务就绪。适合前台运行的 Java 进程。
- forking：ExecStart 进程 fork 后父进程退出，子进程成为主进程。适合传统守护进程。Java 通常不用。
- notify：服务通过 sd_notify 发送 READY=1 通知 systemd 就绪。Spring Boot 可集成，但非默认。
- oneshot：执行一次即退出。

Restart=always：无论正常退出还是异常退出都重启。on-failure 仅异常退出重启。RestartSec=5 重启前等待。StartLimitIntervalSec=60、StartLimitBurst=5 限制 60 秒内最多启动 5 次，超过则进入 failed 不再重启。

unit 示例：
[Unit]
Description=My Java App
After=network.target

    [Service]
    User=app
    Group=app
    WorkingDirectory=/opt/myapp
    ExecStart=/usr/bin/java -Xms512m -Xmx512m -jar /opt/myapp/app.jar
    SuccessExitStatus=143
    Restart=always
    RestartSec=5
    StartLimitIntervalSec=60
    StartLimitBurst=5
    StandardOutput=journal
    StandardError=journal
    Environment=JAVA_HOME=/usr/lib/jvm/java-17
    Environment=TZ=Asia/Shanghai

    [Install]
    WantedBy=multi-user.target

命令：
systemctl daemon-reload  重新加载 unit 文件
systemctl enable myapp   开机自启
systemctl start myapp    启动
systemctl status myapp   查看状态
journalctl -u myapp -f   查看日志

OOM killer：Java 进程被内核杀死，退出信号 SIGKILL，systemd 视为失败，Restart=always 会重启。但若频繁 OOM，可能触发 StartLimitBurst 限制。需排查内存限制、cgroup、JVM 堆外内存。

KillMode=control-group：systemd 停止服务时杀死整个 cgroup 内所有进程，包括 Java 派生的子进程。默认即 control-group。若设为 process，只杀主进程，可能留下子进程。Java 进程树需注意。

</details>

**我的初答：**

**错漏点：**

> 题目 2：软链接与硬链接。请解释硬链接和软链接在 inode、目录项、链接计数、跨文件系统、目录支持、删除源文件后的行为差异。Java 后端蓝绿发布常用 /opt/myapp/current -> /opt/myapp/releases/v1 的软链接切换。如何原子切换？如果 Java 进程已经打开了旧版本的 jar，切换软链接后旧进程是否受影响？为什么？给出关键命令。追问：软链接使用相对路径还是绝对路径？systemd 的 WorkingDirectory 与软链接结合时有哪些陷阱？

<details>
<summary>标准解析</summary>

硬链接：多个目录项指向同一 inode，链接计数增加。删除一个目录项只减少计数，inode 数据仍在直到计数为 0。不能跨文件系统，不能对目录创建硬链接，避免循环。
软链接：独立文件，inode 不同，内容是指向目标路径的字符串。可跨文件系统，可指向目录，目标删除后成为悬空链接。权限通常 777，但访问目标受目标权限限制。

蓝绿切换：
ln -sfn /opt/myapp/releases/v2 /opt/myapp/current
-f 强制，-n 把软链接当普通文件而非目录。更原子方式：创建临时链接再 mv：
ln -sfn /opt/myapp/releases/v2 /opt/myapp/current.tmp
mv -T /opt/myapp/current.tmp /opt/myapp/current
mv 在同一文件系统内是原子重命名。

Java 进程已打开旧 jar：进程启动时已打开文件描述符指向旧 inode，切换软链接不影响已打开的文件描述符，旧进程继续读旧版本。新进程通过 current 解析到新版本。可实现不停机切换。

命令：
ln target link
ln -s target link
ls -li
readlink -f /opt/myapp/current
stat

相对路径陷阱：软链接中的相对路径是相对于软链接所在目录，不是当前工作目录。systemd WorkingDirectory 设为 /opt/myapp，ExecStart 使用 current/app.jar，解析正常。若使用相对路径软链接，移动链接后可能失效。建议绝对路径。

追问：systemd 启动时解析 ExecStart 路径，若使用软链接，可能记录的是解析后的路径。WorkingDirectory 若为软链接，进程 cwd 可能显示为物理路径。Java 的 user.dir 可能受影响。

</details>

**我的初答：**

**错漏点：**

> 题目 3：日期、时区与 Java 应用。请说明 Linux 中 date、timedatectl、TZ 环境变量、/etc/localtime、/etc/timezone 的作用。Java 应用获取默认时区受哪些因素影响？优先级如何？容器中 Java 日志时间比北京时间少 8 小时，如何系统性排查和修复？MySQL 的 serverTimezone、JDBC URL 参数、数据库时区如何影响时间数据？给出关键命令。追问：NTP 同步、夏令时、分布式系统时间一致性对 Java 后端有哪些影响？

<details>
<summary>标准解析</summary>

Linux：
- date 显示或设置系统时间。
- timedatectl 查看和设置时区、NTP 同步状态。
- TZ 环境变量：进程级时区，如 TZ=Asia/Shanghai。
- /etc/localtime：系统时区文件，通常指向 /usr/share/zoneinfo/Asia/Shanghai。
- /etc/timezone：Debian/Ubuntu 记录时区名称，部分程序读取。

Java 默认时区：
- 若启动参数指定 -Duser.timezone=Asia/Shanghai，优先。
- 否则看 TZ 环境变量。
- 否则看 /etc/localtime。
- 否则可能回退到 UTC 或系统默认。
  JDK 内部 TimeZone.getDefault() 会缓存，修改系统时区后已运行 JVM 可能不更新。

容器时区问题：
- 容器默认 UTC，/etc/localtime 为 UTC。
- 修复：挂载 -v /etc/localtime:/etc/localtime:ro -v /etc/timezone:/etc/timezone:ro；或设置 TZ=Asia/Shanghai；或 JVM 参数 -Duser.timezone=Asia/Shanghai；或在 Dockerfile 中安装 tzdata 并设置。
- 排查：
  date
  timedatectl
  echo $TZ
  ls -l /etc/localtime
  cat /etc/timezone
  java -XshowSettings:properties -version 2>&1 | grep user.timezone
  jcmd <pid> VM.system_properties | grep user.timezone
  jinfo -sysprops <pid> | grep user.timezone

MySQL：
- 数据库有 time_zone 系统变量，影响 NOW()、TIMESTAMP 存储转换。
- JDBC URL 可加 serverTimezone=Asia/Shanghai 或 connectionTimeZone=Asia/Shanghai，旧驱动用 serverTimezone。
- TIMESTAMP 会随时区转换，DATETIME 不转换。建议统一 UTC 存储，展示层转换。
- 排查：
  mysql> SELECT @@global.time_zone, @@session.time_zone, NOW(), UTC_TIMESTAMP();
  SHOW VARIABLES LIKE '%time_zone%';

NTP：
- chronyd 或 systemd-timesync 同步时间，避免时钟漂移。
- 分布式系统：时间戳、雪花算法、TCC、消息顺序、日志排查依赖时间一致性。
- 夏令时：部分地区时区偏移变化，Java ZonedDateTime 可处理，但存储用 UTC 更安全。

</details>

**我的初答：**

**错漏点：**

## Day 4 (2026-09-23) —— Linux 固定 IP、端口、进程

> 题目 1：Linux 固定 IP。请说明 ip addr、ip route、ip link 的作用，以及临时配置与持久化配置的区别。主流发行版持久化方式有哪些（netplan、NetworkManager、/etc/network/interfaces、/etc/sysconfig/network-scripts）？CIDR、网关、DNS 分别配置在哪里？Java 后端高可用常用 Keepalived 虚拟 IP（VIP），请说明 VIP 漂移原理，以及主备切换时 Java 服务需要注意什么。追问：ip addr add 加的 IP 重启后为什么丢失？多网卡场景下 Java 服务如何只监听内网 IP？

<details>
<summary>标准解析</summary>

核心命令：
- ip addr：查看/配置网卡 IP 地址。ip addr add 192.168.1.10/24 dev eth0。
- ip route：查看/配置路由表。ip route add default via 192.168.1.1。
- ip link：查看/配置链路层。ip link set eth0 up。

临时 vs 持久化：
- ip addr add 是运行时配置，重启网络或重启系统后丢失。
- 持久化需写入配置文件：
    - Ubuntu 18.04+：netplan，/etc/netplan/*.yaml，应用 netplan apply。
    - CentOS 7+/RHEL：NetworkManager，nmcli 或 /etc/sysconfig/network-scripts/ifcfg-eth0。
    - Debian 旧版：/etc/network/interfaces。
    - DNS 可写 /etc/resolv.conf，但常被 NetworkManager 覆盖，应写网卡配置或 systemd-resolved。

CIDR：
192.168.1.10/24
网关：
ip route add default via 192.168.1.1
或配置文件中 GATEWAY=
DNS：
/etc/resolv.conf
nameserver 8.8.8.8

VIP 与 Keepalived：
- 主备两台机器，VIP 初始在主节点。Keepalived 通过 VRRP 协议心跳检测。
- 主节点故障，备节点接管 VIP，通过 arp 通告让交换机更新 MAC 映射。
- Java 服务本身不感知 VIP，客户端连接 VIP，由 Keepalived 转发/漂移。
- 注意：服务启动时若绑定 VIP 而非 0.0.0.0，备节点未持有 VIP 时绑定会失败。建议绑 0.0.0.0 或监听时动态判断。

追问：
- 临时 IP 丢失因为未写入配置文件，内核网络栈状态不持久。
- 只监听内网 IP：Java 中 ServerSocket 绑定指定地址，Spring Boot 可设 server.address=192.168.1.10。或 iptables 限制来源。

</details>

**我的初答：**

**错漏点：**

> 题目 2：端口与连接排查。请说明 ss、netstat、lsof 的差异及常用参数。Java 服务启动报 Address already in use，如何系统性定位？请解释 TIME_WAIT、CLOSE_WAIT、LISTEN、ESTABLISHED 状态含义。为什么服务重启后端口仍被占用，明明进程已退出？SO_REUSEADDR 和 SO_REUSEPORT 分别解决什么问题？Java 中如何设置？追问：如何查看本机端口范围和 TIME_WAIT 相关内核参数？firewalld 和 iptables 放行端口分别怎么做？

<details>
<summary>标准解析</summary>

工具差异：
- ss：socket statistics，替代 netstat，性能更好，内核直接读取。
- netstat：老工具，net-tools 包，逐渐淘汰。
- lsof：list open files，-i 可查网络连接，能定位到进程和文件描述符。

常用：
ss -lntp        监听中的 TCP 端口及进程
ss -antp        所有 TCP 连接
ss -s           汇总统计
lsof -i:8080    占用 8080 的进程
netstat -anp | grep 8080

端口占用排查：
ss -lntp | grep 8080
lsof -i:8080
fuser 8080/tcp
ps -ef | grep <pid>
确认是否残留进程、是否被其他服务占用、是否 TIME_WAIT 未释放。

状态含义：
- LISTEN：监听等待连接。
- ESTABLISHED：连接已建立，可数据传输。
- TIME_WAIT：主动关闭方进入，等待 2MSL，确保对方收到最后的 ACK，让旧报文消散。
- CLOSE_WAIT：被动关闭方收到 FIN 但本地未调用 close，通常代码未正确关闭连接，是泄漏信号。
- SYN_SENT、SYN_RECV：握手中间态。

重启后端口占用：
- TIME_WAIT 状态没有进程，但占用四元组，导致 bind 失败（尤其未设 SO_REUSEADDR）。
- 若有进程则可能未真正退出，检查 ps、systemd 是否自动重启。

SO_REUSEADDR：
- 允许绑定处于 TIME_WAIT 的地址，或绑定通配地址与特定地址共存。Java ServerSocket.setReuseAddress(true)。
  SO_REUSEPORT：
- 允许多个 socket 绑定同一端口，内核负载均衡。Java 原生不直接暴露，Netty 可用 SO_REUSEPORT。

内核参数：
/proc/sys/net/ipv4/ip_local_port_range
/proc/sys/net/ipv4/tcp_tw_reuse
/proc/sys/net/ipv4/tcp_max_tw_buckets
sysctl -w net.ipv4.tcp_tw_reuse=1

防火墙：
firewall-cmd --add-port=8080/tcp --permanent
firewall-cmd --reload
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT

</details>

**我的初答：**

**错漏点：**

> 题目 3：进程管理。请说明 ps aux 与 ps -ef 的区别，进程状态 R/S/D/Z/T 的含义。僵尸进程和孤儿进程的区别、产生原因、如何排查和清理？kill 的信号有哪些，-15、-9、-1、-3 分别是什么？为什么 kill -9 杀不掉 D 状态进程？Java 进程 CPU 飙高如何排查：从 top 到线程栈的完整链路。追问：nohup 与 & 与 systemd 的关系，后台进程为什么退出终端后可能被杀？

<details>
<summary>标准解析</summary>

ps 差异：
- ps aux：BSD 风格，显示 USER、PID、%CPU、%MEM、STAT、COMMAND。
- ps -ef：System V 风格，显示 UID、PID、PPID、C、STIME、TTY、TIME、CMD。
- 本质都是读 /proc。

进程状态：
- R：运行或可运行。
- S：可中断睡眠，等待事件。
- D：不可中断睡眠，通常等 IO，不能被信号打断。
- Z：僵尸，已退出但父进程未回收。
- T：停止，被 SIGSTOP 或调试暂停。

僵尸进程：
- 子进程退出，父进程未调用 wait/waitpid 回收，残留 task_struct。
- 不占 CPU/内存，但占 PID。大量僵尸会耗尽 PID。
- 排查：ps -ef | grep defunct，看 PPID 找父进程。
- 清理：无法直接 kill 僵尸，需让父进程回收，或 kill 父进程让 init/systemd 接管回收。

孤儿进程：
- 父进程先退出，子进程被 init/systemd 收养，正常运行，不会变成僵尸问题。

kill 信号：
- -15 SIGTERM：默认，优雅终止，可被捕获处理。
- -9 SIGKILL：强制杀死，不可捕获。
- -1 SIGHUP：挂起，常用于重载配置。
- -3 SIGQUIT：退出并 core dump，JVM 收到会打印线程栈。

D 状态：
- 进程在内核态等待不可中断 IO，信号需等系统调用返回才处理，所以 kill -9 也无效。
- 常见于磁盘故障、NFS 挂起。需解决底层 IO。

Java CPU 飙高排查：
top -Hp <pid>            找高 CPU 线程 TID
printf "%x\n" <tid>      转十六进制
jstack <pid> | grep -A 30 <hex>   找对应线程栈
jcmd <pid> Thread.print
或 arthas thread -n 3
定位到具体代码、死循环、GC 线程等。

nohup/&/systemd：
- &：后台运行，但终端关闭发 SIGHUP 可能被杀。
- nohup：忽略 SIGHUP，配合 & 使用。
- systemd：更规范，管理生命周期、重启、日志、cgroup。
- 生产建议用 systemd 而非 nohup。

</details>

**我的初答：**

**错漏点：**

## Day 5 (2026-09-24) —— Linux 环境变量、上传下载、压缩解压

### 题目1：Linux 环境变量的分类、加载顺序与 Java 后端部署实战
> 在部署 Spring Boot 应用时，常需要配置 JAVA_HOME、PATH 等环境变量。请回答：
> 1. Linux 中环境变量按作用范围分为哪几类？如何临时设置、永久设置？export 的作用是什么？
> 2. /etc/profile、/etc/bashrc、~/.bash_profile、~/.bashrc 这几个文件的加载顺序和适用场景分别是什么？为什么在服务器上修改环境变量后有时需要 source 才能生效？
> 3. 启动 Java 应用时，如何通过环境变量动态覆盖 application.yml 中的配置（如数据库密码）？与 -D 参数有何区别？
     > 追问：若多个 Spring Boot 应用需要不同版本的 JDK，如何在不修改全局 JAVA_HOME 的情况下实现？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 环境变量分类：
    - 临时：export VAR=value，仅当前会话有效，关闭终端失效。
    - 永久（用户级）：写入 ~/.bash_profile 或 ~/.bashrc，仅对当前用户生效。
    - 永久（系统级）：写入 /etc/profile 或 /etc/profile.d/，对所有用户生效。
- export 作用：将 shell 变量导出为环境变量，使子进程（如 Java 进程）能够继承。
- 加载顺序（登录式 shell）：
    1. /etc/profile
    2. /etc/profile.d/*.sh
    3. ~/.bash_profile
    4. ~/.bashrc（若被 .bash_profile 调用）
    5. /etc/bashrc（若被 .bashrc 调用）
    - 非登录式 shell 仅加载 ~/.bashrc 和 /etc/bashrc。
- source 原因：修改配置文件后，当前 shell 不会自动重新读取，需执行 source 或 . 使配置立即生效（否则需重新登录）。
- 环境变量覆盖 Spring 配置：
    - Spring Boot 支持通过环境变量覆盖配置，如 SPRING_DATASOURCE_PASSWORD=xxx，会自动映射为 spring.datasource.password。
    - 与 -D 区别：-D 设置的是 JVM 系统属性（System.getProperty），需 java -Dkey=value；环境变量是操作系统级别，优先级通常低于 -D 但高于 application.yml。
    - 优先级：命令行参数 > -D 系统属性 > 环境变量 > application.yml。
- 追问（多 JDK 版本）：
    - 方案1：不修改全局 JAVA_HOME，启动脚本中显式指定 /opt/jdk11/bin/java -jar app.jar。
    - 方案2：使用 alternatives 管理多版本 JDK，切换默认版本。
    - 方案3：在应用启动脚本开头 export JAVA_HOME=/opt/jdk11，仅对当前进程有效。
</details>

**我的初答**：
1. Linux中环境变量按照作用范围分为局部变量和全局变量.
- 临时设置: 直接执行export var_name=value即可
- 永久设置: 对于单个账户,配置在~/.bashrc文件中;对于所有账户,配置在/etc/profile文件中
- export的作用: 使用当前环境运行
2. etc/profile -> /etc/bashrc -> ~/.bash_profile -> ~/.bashrc;后两者适用于单个用户,前两者全局共享
- export是单向的,有时候子环境需要执行父环境的内容,source作用就是重新用全局执行
3. 不了解

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

你的初答点评

- **分类**：术语不准确。标准说法是**系统级（全局）/用户级/临时（会话级）**，"局部变量和全局变量"容易和 shell 局部变量混淆——而且 shell 里"局部变量"是指未 export、只在当前 shell 有效的变量，这正是 export 要解释的点，你自己却把 export 答成了"使用当前环境运行"，**说反了**：export 是把变量传给子进程/子 shell。

- **加载顺序**：顺序大致对，但漏了关键的区分逻辑——**登录 shell vs 非登录 shell** 走的加载链不一样，这才是考点。

- **source 的解释**："重新用全局执行"表述含混，本质是"在当前 shell 中执行而不是开子 shell"。

- **第 3 问 + 追问**：空白，而这是面试官区分"会背命令"和"真部署过"的部分，必须补上。


---

## 参考答案

### 1. 环境变量的分类、设置与 export

按作用范围分三类：

表格

| 类别  | 作用范围 | 设置方式 |
| --- | --- | --- |
| 临时（会话级） | 仅当前终端会话，关闭即失效 | `export JAVA_HOME=/usr/local/jdk17` |
| 用户级 | 仅对当前用户永久生效 | 写入 `~/.bashrc` 或 `~/.bash_profile` |
| 系统级 | 对所有用户永久生效 | 写入 `/etc/profile` 或 `/etc/profile.d/*.sh`（需 root） |

**export 的作用（纠正你的答案）：**

直接在 shell 里写 `VAR=value` 定义的只是**当前 shell 的局部变量，子进程看不到**。`export VAR=value` 把它提升为**环境变量**，使得该 shell 启动的所有子进程（比如你 `java -jar` 启动的 JVM）都能继承到它。验证方法：`VAR=a` 后开 `bash` 子 shell 里 `echo $VAR` 是空的；export 之后就有值。

### 2. 四个配置文件的加载顺序

**关键前提：登录 shell 和非登录 shell 加载链不同。**

- **登录 shell**（SSH 登录服务器、su - 切换用户）： `/etc/profile` →（其内部会遍历 `/etc/profile.d/*.sh`，并调用 `/etc/bashrc`）→ `~/.bash_profile` →（其内部通常调用 `~/.bashrc`）

- **非登录 shell**（登录后再开一个终端窗口、执行 `bash`）：
  只加载 `/etc/bashrc` → `~/.bashrc`，**不读 /etc/profile 和 ~/.bash_profile**


**适用场景：**

- `/etc/profile`：系统级、登录时生效，适合放全局 JAVA_HOME；

- `/etc/bashrc`：系统级、所有 bash 都生效，适合放全局 alias、umask；

- `~/.bash_profile`：用户级、仅登录时执行一次，适合放用户专属 PATH；

- `~/.bashrc`：用户级、每次开 shell 都执行，运维最常改的就是它（这也是 `/etc/profile.d/` 下建独立脚本的原因——好维护、不污染主文件）。


**为什么改完要 source：**

配置文件只在 **shell 启动时**读取一次，改文件不会通知已运行的 shell。`source ~/.bashrc`（等价于 `. ~/.bashrc`）的作用是**在当前 shell 中直接执行该脚本**，而不是像 `./script.sh` 那样开一个子 shell——子 shell 里 export 的变量随子 shell 退出就没了，当前会话照样没生效。所以改完必须 `source` 或重开终端。

### 3. Spring Boot 用环境变量覆盖配置

Spring Boot 有**外部化配置的优先级机制**，操作系统环境变量的优先级高于 `application.yml`，所以：

bash复制

```bash
# yml 中是 spring.datasource.password: dev_password
export SPRING_DATASOURCE_PASSWORD=prod_password
java -jar app.jar
```

启动后生效的就是 `prod_password`，无需改 jar 包。命名规则：**大写、点号换下划线、中划线换下划线**（`spring.datasource.password` → `SPRING_DATASOURCE_PASSWORD`）。Docker/K8s 部署时也正是靠这个机制注入配置（Dockerfile 的 `ENV`、K8s 的 `env`）。

**与 `-D` 参数的区别：**

- `-Dspring.datasource.password=xxx` 是 **JVM 系统属性**（System Property），通过 `System.getProperty()` 读取，Spring 也会识别，**优先级比环境变量更高**；

- 环境变量是**进程级**的，同一环境变量对该进程所有代码可见；`-D` 只在 JVM 内有效；

- 优先级链（由高到低，常考）：命令行参数 `--xxx` > `-D` JVM 参数 > OS 环境变量 > `application-prod.yml` > `application.yml`；

- 实践建议：**密码这类敏感信息用环境变量注入**（不落盘、不进启动脚本），JVM 调优参数（内存、GC）用 `-D` 或 `-X` 参数。


### 追问：多应用不同 JDK，不动全局 JAVA_HOME

思路是**把 JDK 版本绑定到进程，而不是绑定到系统**：

1. **启动脚本中临时指定**（最常用）：

bash

  ```bash
  #!/bin/bash
  export JAVA_HOME=/usr/local/jdk17
  export PATH=$JAVA_HOME/bin:$PATH
  nohup java -jar app-a.jar > app.log 2>&1 &
  ```

每个应用一个启动脚本，export 只对脚本启动的进程生效，互不影响、不改全局。

2. **直接用绝对路径启动**：`/usr/local/jdk11/bin/java -jar app-b.jar`，完全不碰 JAVA_HOME。

3. **容器化隔离**（现代标准做法）：每个应用打自己基础镜像的 Docker 镜像（`FROM eclipse-temurin:17` / `FROM eclipse-temurin:11`），JDK 版本随镜像走，彻底隔离。


---

这题的答题策略：前两问快速带过（是基础门槛分），第 3 问和追问要展开讲"优先级链"和"进程级隔离"——这是面试官判断你有没有真实部署经验的点。
</details>

### 题目2：Linux 文件上传下载工具（scp、rsync、sftp、wget/curl）的选型与实践
> 在 Java 后端部署与运维中，经常需要在服务器之间传输文件（如上传 JAR 包、下载日志）。请回答：
> 1. scp、rsync、sftp 三者在传输机制、增量传输、断点续传上有何区别？分别适合什么场景？
> 2. 如何使用 scp 从本地上传文件到远程服务器？如何从远程下载日志到本地？如何指定端口和密钥文件？
> 3. wget 和 curl 都能下载文件，它们有何区别？在只安装了其中之一的服务器上，如何用 curl 实现 wget 的下载功能（含断点续传、限速）？
     > 追问：rsync 的 --delete 参数有什么风险？如何用 rsync 实现服务器目录的实时同步？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- scp / rsync / sftp 区别：
    - scp：基于 SSH，全量传输，无增量，无断点续传。适合一次性小文件传输，简单易用。
    - rsync：支持增量传输（仅同步变化部分），支持断点续传（--partial），支持压缩传输（-z），适合大文件或目录同步。首次传输仍为全量。
    - sftp：交互式文件传输，基于 SSH，支持断点续传（reget/reput），适合手动交互操作。
- scp 用法：
    - 上传：scp -P 22 -i key.pem app.jar user@host:/opt/app/
    - 下载：scp -P 22 user@host:/var/log/app.log ./
    - 递归：scp -r dir user@host:/path/
- wget vs curl：
    - wget：专注下载，支持递归下载、断点续传（-c）、限速（--limit-rate），默认输出到文件。
    - curl：功能更广，支持多种协议（HTTP/FTP/SCP 等），可发送 POST 请求、自定义 Header，默认输出到标准输出，需 -o 保存文件。
    - curl 实现 wget 功能：curl -C - --limit-rate 1M -o file.zip http://example.com/file.zip（-C - 断点续传，--limit-rate 限速）。
- 追问：
    - rsync --delete 会删除目标目录中源目录不存在的文件，若源目录为空或路径写错，可能清空目标目录，风险极高，建议先加 --dry-run 预览。
    - 实时同步：rsync + inotify（或 lsyncd）监控文件变化后触发同步，或使用 rsync 定时任务（crontab）定期同步。
</details>

**我的初答**：
**错漏点**：


### 题目3：Linux 压缩与解压缩命令及 Java 后端部署场景
> Java 后端部署时，常使用 tar.gz、zip 等格式打包应用或备份日志。请回答：
> 1. tar、gzip、zip 三者有何区别？tar.gz 和 tar.bz2 有何不同？如何创建和解压这些格式的文件？
> 2. 如何使用 tar 打包时排除特定目录（如 logs、.git）？如何只解压压缩包中的某个文件？
> 3. 在服务器磁盘空间不足时，如何将大日志文件压缩并保留原始文件？如何查看压缩包内容而不解压？
     > 追问：zip 与 tar.gz 在跨平台（Windows/Linux）兼容性上有何差异？为什么很多 Java 项目发布包使用 tar.gz 而非 zip？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- tar / gzip / zip 区别：
    - tar：打包工具，将多个文件合并为一个文件（.tar），不压缩。
    - gzip：压缩工具，压缩单个文件（.gz），不能打包目录。
    - tar.gz：先 tar 打包再 gzip 压缩，是 Linux 最常见的压缩格式。
    - zip：打包+压缩一体，跨平台好，但压缩率通常低于 gzip。
- tar.gz vs tar.bz2：bz2 压缩率更高但速度更慢，gz 速度更快但压缩率略低。生产环境常用 tar.gz。
- 创建与解压：
    - 打包压缩：tar -czvf app.tar.gz app/
    - 解压：tar -xzvf app.tar.gz
    - 仅查看内容：tar -tzvf app.tar.gz
    - 解压单个文件：tar -xzvf app.tar.gz app/config/application.yml
    - 排除目录：tar -czvf app.tar.gz --exclude=logs --exclude=.git app/
- 磁盘空间不足处理：
    - 压缩并删除原文件：gzip access.log（生成 access.log.gz，原文件消失）。
    - 压缩但保留原文件：gzip -c access.log > access.log.gz（原文件保留）。
    - 查看压缩包内容：zcat access.log.gz | less 或 zless access.log.gz。
- 追问：
    - zip 在 Windows 和 Linux 上均能直接打开，兼容性更好；tar.gz 在 Windows 需第三方工具（如 7-Zip）。
    - Java 项目发布包用 tar.gz 的原因：Linux 服务器原生支持，保留文件权限（可执行位），且压缩率更高，适合脚本自动化处理。
</details>

**我的初答**：
**错漏点**：

---

## Day 6 (2026-09-25) —— Shell 变量（局部变量、全局变量、常量）

### 题目1：Shell 变量的分类、作用域与 export 的本质
> 在 Shell 脚本中，变量按作用域可分为局部变量、全局变量和环境变量。请回答：
> 1. 什么是局部变量、全局变量、环境变量？它们的生效范围有何区别？在函数中定义的变量默认是局部的还是全局的？
> 2. export 的作用是什么？为什么在脚本中定义的变量，有时在当前终端 source 后能生效，但子进程却读不到？
> 3. 如何在函数中声明真正的局部变量？local 关键字与直接赋值有何区别？如果不加 local，函数内修改同名全局变量会发生什么？
     > 追问：父 Shell 中定义的环境变量，在子 Shell 中修改后，父 Shell 能否感知？为什么？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 变量分类：
    - 局部变量：仅在函数内部有效，需用 local 声明。Shell 中默认不加 local 的变量均为全局变量。
    - 全局变量：当前 Shell 会话中有效，包括脚本内所有函数，但不会自动传给子进程。
    - 环境变量：通过 export 导出，会随进程传递给子进程（包括子 Shell、Java 进程等）。
- export 作用：将 Shell 变量标记为“环境变量”，使其能被 fork 出的子进程继承。未 export 的变量仅当前 Shell 可见。
- local 与直接赋值的区别：
    - local var=value：在当前函数栈中创建局部变量，函数返回后销毁，不影响外部同名变量。
    - 直接赋值 var=value：若函数外已有同名全局变量，会修改其值；若没有，则创建全局变量，函数外仍可访问。
- 追问：
    - 子 Shell 修改环境变量不会影响父 Shell，因为子 Shell 是独立的进程，拥有自己的一份变量副本。父 Shell 的环境变量被子 Shell 复制后，修改仅作用于副本。
    - 若要让修改生效，需通过文件（如写入临时文件）或 source 在同一 Shell 中执行。
</details>

**我的初答**：
**错漏点**：


### 题目2：Shell 常量的声明（readonly）与变量替换的高级用法
> 在编写部署脚本时，常需要定义常量（如 APP_HOME、LOG_DIR）以防止误修改。请回答：
> 1. 如何声明一个常量？readonly 与 declare -r 有何区别？若尝试修改 readonly 变量，会发生什么？
> 2. 如何删除一个变量？unset 能否删除 readonly 变量？
> 3. 请说明以下变量替换语法的含义与典型用途：${VAR:-default}、${VAR:=default}、${VAR:?error}、${VAR:+value}。
     > 追问：${VAR:-default} 与 ${VAR-default} 的区别是什么？在变量未定义和变量为空两种情况下，它们的行为分别如何？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 常量声明：
    - readonly VAR=value 或 declare -r VAR=value，两者等价。
    - 修改 readonly 变量会报错 bash: VAR: readonly variable，脚本继续执行（不会退出，除非 set -e）。
    - readonly 也可用于函数：readonly -f func_name，禁止函数被覆盖。
- 删除变量：unset VAR。但 unset 无法删除 readonly 变量，会报错。
- 变量替换语法：
    - ${VAR:-default}：若 VAR 未定义或为空，返回 default，但不修改 VAR。
    - ${VAR:=default}：若 VAR 未定义或为空，将 VAR 赋值为 default 并返回。
    - ${VAR:?error}：若 VAR 未定义或为空，输出 error 并退出脚本（常用于必填参数校验）。
    - ${VAR:+value}：若 VAR 已定义且非空，返回 value，否则返回空。
- 追问：
    - ${VAR:-default}：冒号表示“未定义或为空”均触发默认值。
    - ${VAR-default}：不带冒号，仅在 VAR 未定义时触发默认值；若 VAR 定义为空字符串，则返回空字符串。
    - 区别核心在于是否把“空字符串”视为未定义。
</details>

**我的初答**：
**错漏点**：


### 题目3：Shell 变量在 Java 后端部署脚本中的综合应用（命令替换、位置参数、脚本传参）
> 在编写 Spring Boot 应用启动脚本（如 start.sh）时，通常需要接收参数、拼接命令、传递变量。请回答：
> 1. 脚本中如何使用位置参数（$1、$2、$@、$#）？$@ 与 $* 有何区别？如何校验参数个数？
> 2. 命令替换 `$(command)` 与反引号（`command`）有何区别？为什么推荐使用 $()？
> 3. 如何将脚本中的变量传递给 Java 应用（如 -Dspring.profiles.active=$PROFILE）？若变量值包含空格或特殊字符，应如何处理（引号的作用）？
     > 追问：如何编写一个脚本，使其只能通过 source 执行而不能通过 sh 执行？用 $0 与 BASH_SOURCE 如何判断？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 位置参数：
    - $1、$2...：第 1、2 个参数。
    - $@：所有参数，作为独立字符串列表（推荐）。
    - $*：所有参数，作为单个字符串（若不加引号，效果与 $@ 相同；加引号后合并为一个字符串）。
    - $#：参数个数。
    - 校验：if [ $# -lt 1 ]; then echo "Usage: $0 start|stop"; exit 1; fi
- 命令替换：
    - $() 与 `` 均用于执行命令并获取输出。
    - $() 支持嵌套，可读性更好；反引号嵌套时需转义，易出错。
    - 推荐使用 $()。
- 变量传递与引号：
    - 传给 Java：java -Dspring.profiles.active="$PROFILE" -jar app.jar
    - 若变量含空格，必须加双引号，否则会被分词为多个参数。
    - 推荐统一使用双引号包裹变量：echo "$VAR"，除非明确需要分词。
- 追问：
    - 判断是否被 source 执行：比较 $0 与 ${BASH_SOURCE[0]}，若不同，则说明脚本被 source（因为 source 时 $0 是调用者的名称）。
    - 也可通过 (return 0 2>/dev/null) 判断：若在 source 中，return 成功；若在普通执行中，return 报错。
</details>

**我的初答**：
**错漏点**：

---

## Day 7 (2026-09-26) —— Shell 特殊变量与 Shell 环境类型

### 题目1：Shell 特殊变量 `$?`、`$$`、`$!`、`$0`、`$#`、`$@`、`$*` 在部署脚本中的含义与实战
> 在编写 Java 后端部署脚本时，经常需要判断上一条命令是否成功、获取脚本自身名称、处理参数等。请回答：
> 1. `$?`、`$$`、`$!`、`$0`、`$#`、`$@`、`$*` 分别代表什么？其中哪些是只读的？哪些会随上下文变化？
> 2. 如何用 `$?` 实现命令失败立即退出脚本？`set -e` 与手动判断 `$?` 有何区别？`set -o pipefail` 又解决了什么问题？
> 3. `$@` 与 `$*` 在不加引号和加引号时分别如何展开？为什么推荐使用 `"$@"` 传参？
     > 追问：`$0` 在 `source` 执行和 `bash` 执行时有何不同？如何编写一个脚本既能被 `source` 也能被直接执行，并正确获取自身路径？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 特殊变量含义：
    - `$?`：上一条命令的退出状态码，0 表示成功，非 0 表示失败。只读。
    - `$$`：当前 Shell 的进程 ID（PID）。只读。
    - `$!`：最近一个后台命令的 PID。只读。
    - `$0`：当前脚本或 Shell 的名称。在脚本中为脚本路径，在交互式 Shell 中为 `-bash`。
    - `$#`：参数个数。
    - `$@`：所有参数，作为独立字符串列表。
    - `$*`：所有参数，作为单个字符串（默认以空格分隔）。
- 失败退出：
    - 手动：`command || exit 1`
    - `set -e`：任何命令返回非 0 立即退出脚本（但不包括条件判断中的命令）。
    - `set -o pipefail`：管道中任一命令失败，整个管道返回失败（默认只返回最后一个命令的状态）。
- `$@` 与 `$*` 展开：
    - 不加引号：两者均按空格分词，效果相同。
    - 加引号：`"$@"` 展开为 `"$1" "$2" ...`，每个参数独立；`"$*"` 展开为 `"$1 $2 ..."` 单个字符串。
    - 推荐 `"$@"`，可保留参数中的空格和特殊字符。
- 追问：
    - `source` 执行时，`$0` 是调用者的 Shell 名称（如 `bash`）；`bash` 执行时，`$0` 是脚本路径。
    - 获取脚本自身路径：`SCRIPT_PATH="${BASH_SOURCE[0]}"`，再 `cd "$(dirname "$SCRIPT_PATH")"` 进入脚本目录。
    - 判断是否被 source：`if [[ "${BASH_SOURCE[0]}" != "${0}" ]]; then echo "被 source"; else echo "被直接执行"; fi`
</details>

**我的初答**：
**错漏点**：


### 题目2：交互式 Shell 与非交互式 Shell 的区别及 Java 部署中的影响
> Shell 分为交互式和非交互式，这对环境变量加载和脚本执行有直接影响。请回答：
> 1. 什么是交互式 Shell？什么是非交互式 Shell？`bash -i`、`bash script.sh`、`ssh user@host 'command'` 分别属于哪种？
> 2. 交互式 Shell 和非交互式 Shell 在启动时分别加载哪些配置文件（如 `~/.bashrc`、`~/.bash_profile`、`/etc/profile`）？为什么通过 `ssh` 远程执行命令时，`~/.bashrc` 可能不会被执行？
> 3. 在 Java 后端部署中，通过 `crontab` 定时执行脚本时，环境变量缺失导致 Java 启动失败，如何解决？`cron` 默认使用什么 Shell？如何确保脚本加载了正确的环境变量？
     > 追问：`#!/bin/bash` 与 `#!/bin/sh` 在非交互式脚本中有何区别？为何推荐使用 `#!/bin/bash`？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 交互式 vs 非交互式：
    - 交互式：用户可输入命令并立即看到结果，如登录终端、`bash -i`。
    - 非交互式：执行脚本或单条命令，无用户交互，如 `bash script.sh`、`ssh host 'ls'`。
- 配置文件加载：
    - 交互式登录 Shell：`/etc/profile` → `~/.bash_profile` → `~/.bashrc` → `/etc/bashrc`
    - 交互式非登录 Shell：`~/.bashrc` → `/etc/bashrc`
    - 非交互式 Shell（如执行脚本）：通常不加载任何配置文件，除非 `BASH_ENV` 指定。
    - `ssh user@host 'command'`：非交互式非登录，默认不加载 `~/.bashrc`，除非显式 `source`。
- crontab 环境变量问题：
    - `cron` 默认使用 `/bin/sh`，环境变量极简（仅 `HOME`、`PATH` 等少量变量）。
    - 解决：在脚本开头显式 `source /etc/profile` 或 `source ~/.bash_profile`，或直接写绝对路径，或在 crontab 中定义环境变量。
    - 推荐：脚本中使用绝对路径，并在脚本开头设置 `PATH` 和 `JAVA_HOME`。
- 追问：
    - `#!/bin/bash` 使用 Bash 解释器，支持数组、`[[ ]]`、`local` 等特性。
    - `#!/bin/sh` 可能是 `dash` 或其他精简 Shell，不支持 Bash 扩展，可移植性好但功能弱。推荐 `#!/bin/bash`。
</details>

**我的初答**：
**错漏点**：


### 题目3：登录 Shell 与非登录 Shell 的配置文件加载顺序及 Java 环境变量持久化
> 在服务器上安装 JDK 后，需要配置 `JAVA_HOME` 和 `PATH` 使所有用户和脚本都能使用。请回答：
> 1. 登录 Shell 与非登录 Shell 的启动流程有何不同？`/etc/profile`、`~/.bash_profile`、`~/.bashrc` 各自的加载时机是什么？
> 2. 若希望 `JAVA_HOME` 对所有用户生效，应写入哪个文件？若只对当前用户生效，应写入哪个文件？为什么有时写了 `~/.bashrc` 但 `ssh` 远程执行命令时读不到？
> 3. 如何验证当前 Shell 是登录 Shell 还是非登录 Shell？`shopt -q login_shell` 与 `echo $0` 哪种方式更可靠？
     > 追问：在 Docker 容器中运行 Java 应用时，环境变量应如何设置？`ENV` 指令与 `RUN export` 有何区别？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 登录 Shell 与非登录 Shell：
    - 登录 Shell：需要输入用户名密码，或 `ssh` 登录，或 `bash --login`。加载 `/etc/profile` → `~/.bash_profile` → `~/.bashrc`。
    - 非登录 Shell：打开新终端标签、执行脚本、`bash` 命令。通常只加载 `~/.bashrc`。
- 环境变量持久化：
    - 对所有用户生效：写入 `/etc/profile` 或 `/etc/profile.d/java.sh`。
    - 对当前用户生效：写入 `~/.bash_profile` 或 `~/.bashrc`。
    - `ssh` 远程执行命令属于非交互非登录，不加载 `~/.bashrc`，因此写入 `~/.bashrc` 的变量可能读不到。解决：写入 `~/.bash_profile` 并在其中 source `~/.bashrc`，或使用 `ssh host 'source ~/.bashrc; command'`。
- 判断登录 Shell：
    - `shopt -q login_shell && echo "登录 Shell" || echo "非登录 Shell"`
    - `echo $0` 输出 `-bash` 表示登录 Shell，`bash` 表示非登录 Shell。
- 追问：
    - Docker 中设置环境变量：`ENV JAVA_HOME=/usr/local/jdk`，在构建和运行时均生效。
    - `RUN export JAVA_HOME=...` 仅在当前构建层有效，后续层和运行时无效。推荐使用 `ENV`。
</details>

**我的初答**：
**错漏点**：

---

## Day 5 (2026-09-27) —— Shell 字符串、数组、常用内置命令

### 题目1：Shell 字符串处理的 ${} 扩展语法与实战陷阱
> 在 Java 后端部署脚本中，经常需要从路径、版本号、配置值中提取子串或替换内容。请回答：
> 1. 如何获取字符串长度、提取子串、替换内容？请说明 `${#VAR}`、`${VAR:offset:length}`、`${VAR/old/new}`、`${VAR//old/new}` 的含义。
> 2. `${VAR#pattern}`、`${VAR##pattern}`、`${VAR%pattern}`、`${VAR%%pattern}` 分别如何截断字符串？请举例说明（如从 `/opt/app/app-1.0.0.jar` 中提取文件名和版本号）。
> 3. 为什么在字符串拼接时推荐使用 `${VAR}` 而不是 `$VAR`？在什么情况下 `$VAR` 会导致歧义？
     > 追问：`${VAR:-default}` 与 `${VAR:=default}` 在字符串处理中如何配合使用？如何判断一个字符串是否为空或仅含空格？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 基本操作：
    - `${#VAR}`：字符串长度。
    - `${VAR:offset:length}`：从 offset 开始截取 length 个字符（offset 从 0 开始，可为负表示从末尾算起）。
    - `${VAR/old/new}`：替换第一个匹配的 old 为 new。
    - `${VAR//old/new}`：替换所有匹配的 old 为 new。
- 截断语法：
    - `${VAR#pattern}`：从左侧删除最短匹配。
    - `${VAR##pattern}`：从左侧删除最长匹配。
    - `${VAR%pattern}`：从右侧删除最短匹配。
    - `${VAR%%pattern}`：从右侧删除最长匹配。
    - 示例：FILE="/opt/app/app-1.0.0.jar"
        - 文件名：`${FILE##*/}` → app-1.0.0.jar
        - 目录：`${FILE%/*}` → /opt/app
        - 去掉 .jar：`${FILE%.jar}` → /opt/app/app-1.0.0
        - 版本号：`${FILE##*-}` 配合 `${FILE%.jar}` 可得 1.0.0
- `${VAR}` vs `$VAR`：
    - 当变量后紧跟字母、数字或下划线时，`$VAR_suffix` 会被解析为变量名 `VAR_suffix`，导致歧义。
    - 使用 `${VAR}_suffix` 明确变量边界。
- 追问：
    - `${VAR:-default}` 仅返回默认值不修改 VAR；`${VAR:=default}` 会同时给 VAR 赋值。
    - 判断空或仅空格：`if [[ -z "${VAR// /}" ]]; then echo "空或仅空格"; fi`（删除所有空格后判断长度）。
</details>

**我的初答**：
**错漏点**：


### 题目2：Shell 数组（索引数组与关联数组）的使用及与 Java 数组的对比
> 在批量处理多个服务或配置项时，Shell 数组非常实用。请回答：
> 1. 如何定义索引数组和关联数组？如何获取数组长度、遍历数组、访问单个元素？`${arr[@]}` 与 `${arr[*]}` 有何区别？
> 2. 关联数组（`declare -A`）的键可以是任意字符串吗？如何判断某个键是否存在？如何删除数组元素？
> 3. Shell 数组与 Java 数组在内存模型、长度可变性、类型约束上有何本质区别？在部署脚本中，如何用数组管理多个 Spring Boot 服务的启动与停止？
     > 追问：`for i in "${arr[@]}"` 与 `for i in ${arr[@]}` 有何区别？当数组元素包含空格时，哪种写法更安全？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 数组定义与操作：
    - 索引数组：`arr=("a" "b" "c")`，`arr[0]="x"`。
    - 关联数组：`declare -A map`，`map[key1]="value1"`。
    - 长度：`${#arr[@]}`。
    - 访问：`${arr[0]}`，`${map[key1]}`。
    - 遍历：`for item in "${arr[@]}"; do ...; done`。
- `${arr[@]}` vs `${arr[*]}`：
    - `"${arr[@]}"`：展开为独立元素列表，每个元素独立引号包裹。
    - `"${arr[*]}"`：展开为单个字符串，元素间以 IFS 分隔。
    - 推荐使用 `"${arr[@]}"`，保留元素边界。
- 关联数组：
    - 键可为任意非空字符串（Bash 4.0+）。
    - 判断键存在：`[[ -v map[key] ]]`（Bash 4.2+）或 `[[ -n "${map[key]+x}" ]]`。
    - 删除元素：`unset arr[1]`，`unset map[key1]`。
- 与 Java 数组对比：
    - Shell 数组长度可变（动态），元素类型无约束（均为字符串），无越界检查（访问不存在元素返回空）。
    - Java 数组长度固定，类型强约束，越界抛异常。
- 部署脚本示例：
  SERVICES=("user-service" "order-service" "pay-service")
  for svc in "${SERVICES[@]}"; do
  nohup java -jar "$svc.jar" > "$svc.log" 2>&1 &
  done
- 追问：
    - `for i in "${arr[@]}"`：每个元素独立，含空格的元素保持完整。
    - `for i in ${arr[@]}`：按 IFS 分词，含空格的元素会被拆开。推荐前者。
</details>

**我的初答**：
**错漏点**：


### 题目3：Shell 常用内置命令（read、printf、declare、eval、trap）在部署脚本中的综合应用
> 请回答以下内置命令的用途与典型场景：
> 1. `read` 如何从标准输入或文件读取数据？`read -p`、`read -s`、`read -r` 分别有什么作用？如何读取一行并按分隔符拆分为多个变量？
> 2. `printf` 与 `echo` 有何区别？为什么 `printf` 更适合格式化输出？`declare` 的 `-i`、`-a`、`-A`、`-r` 选项分别声明什么类型的变量？
> 3. `eval` 的作用是什么？为什么使用 `eval` 存在安全风险（如命令注入）？`trap` 如何在脚本退出或收到信号时执行清理操作（如删除临时文件、停止子进程）？
     > 追问：`exec` 命令在脚本中如何使用？`exec java -jar app.jar` 与直接 `java -jar app.jar` 有何区别？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- read：
    - `read var`：读取一行到 var。
    - `read -p "提示：" var`：带提示。
    - `read -s var`：隐藏输入（密码）。
    - `read -r var`：禁止反斜杠转义（读取原始内容）。
    - `IFS=',' read -r a b c`：按逗号分隔读取到多个变量。
- printf vs echo：
    - `echo` 简单输出，不同 Shell 行为不一致（如 `-n`、`-e` 兼容性差）。
    - `printf` 支持格式化（`%s`、`%d`、`\n`），行为一致，适合跨平台脚本。
- declare：
    - `-i`：整数。
    - `-a`：索引数组。
    - `-A`：关联数组。
    - `-r`：只读。
- eval：
    - 将字符串作为命令执行，如 `eval "cmd_$i"`。
    - 风险：若字符串包含用户输入，可能被注入恶意命令。
    - 避免使用，或用 `printf -v` 替代动态变量赋值。
- trap：
    - `trap 'rm -f $TMP_FILE' EXIT`：脚本退出时删除临时文件。
    - `trap 'kill $PID' SIGTERM SIGINT`：收到信号时停止子进程。
    - 常见信号：EXIT、ERR、SIGINT、SIGTERM。
- exec：
    - `exec command`：用 command 替换当前 Shell 进程（PID 不变）。
    - `exec java -jar app.jar`：Java 进程直接替换脚本进程，信号（如 SIGTERM）可直接传给 Java，便于优雅停机。
    - 直接 `java -jar`：脚本继续运行，Java 作为子进程，信号需手动转发。
</details>

**我的初答**：
**错漏点**：

---

## Day 6 (2026-09-28) —— Shell 流程控制与内置命令

### 题目1：Shell 条件判断 `[ ]`、`[[ ]]`、`test` 与 `(( ))` 的区别及部署脚本实战
> 在编写 Java 应用启动脚本时，经常需要判断文件是否存在、进程是否运行、端口是否被占用。请回答：
> 1. `[ ]`、`[[ ]]`、`test` 三者在语法和功能上有何区别？`[[ ]]` 支持哪些 `[ ]` 不具备的特性（如正则匹配 `=~`、模式匹配、逻辑运算符 `&&`/`||`）？
> 2. 数值比较、字符串比较、文件测试分别使用哪些运算符？请写出判断“文件存在且可读”和“进程未运行”的表达式。
> 3. `(( ))` 与 `[ ]` 在算术运算和条件判断上有何不同？为什么推荐在数值比较时使用 `(( ))`？
     > 追问：`[ $a == $b ]` 在变量为空时可能报什么错？如何避免？`[[ ]]` 是否解决了这个问题？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `[ ]` vs `[[ ]]` vs `test`：
    - `test` 和 `[ ]` 等价，是 POSIX 标准，功能有限，需对变量加引号防止分词和空值报错。
    - `[[ ]]` 是 Bash 关键字，支持 `=~` 正则、`==` 模式匹配、`&&`/`||` 逻辑运算，且不会对变量进行分词和通配符扩展，更安全。
- 常用运算符：
    - 数值：`-eq`、`-ne`、`-gt`、`-ge`、`-lt`、`-le`。
    - 字符串：`=`、`!=`、`-z`（空）、`-n`（非空）。
    - 文件：`-e`（存在）、`-f`（普通文件）、`-d`（目录）、`-r`（可读）、`-w`（可写）、`-x`（可执行）。
    - 进程判断：`pgrep -f app.jar > /dev/null` 或 `ps -ef | grep -v grep | grep app.jar`。
    - 端口判断：`ss -tunlp | grep :8080` 或 `netstat -tunlp | grep :8080`。
- `(( ))` 与 `[ ]`：
    - `(( ))` 专用于整数算术运算和比较，支持 `+ - * / % **`、`++`、`--`、三元运算符，语法更接近 C。
    - `[ ]` 数值比较需用 `-eq` 等，且不支持算术表达式直接求值。
    - 推荐：`if (( a > b )); then ...; fi`
- 追问：
    - `[ $a == $b ]` 若 `$a` 为空，会变成 `[ == b ]`，报 `unary operator expected` 或 `too many arguments`。
    - 避免：加引号 `[ "$a" == "$b" ]`，或使用 `[[ $a == $b ]]`（`[[ ]]` 内部不会分词，空变量也安全）。
</details>

**我的初答**：
**错漏点**：


### 题目2：Shell 循环（for、while、until）在 Java 批量服务启停脚本中的应用
> 在微服务部署中，常需要批量启动或停止多个 Spring Boot 服务。请回答：
> 1. `for`、`while`、`until` 三种循环的语法和适用场景分别是什么？`break`、`continue`、`break n` 的作用是什么？
> 2. 如何用 `while read line` 逐行读取文件？与 `for line in $(cat file)` 相比有何优势？当文件行含空格时，哪种方式更安全？
> 3. 请设计一个脚本片段，遍历服务名数组，检查每个服务是否在运行，若未运行则启动，并输出状态。要求使用 `for` 循环和条件判断。
     > 追问：`while` 循环中若在管道内修改外部变量，为什么变量值不会保留？如何解决（使用进程替换 `< <(...)` 或 `while read` 配合重定向）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 循环语法：
    - `for var in list; do ...; done`：遍历列表，适合已知集合。
    - `while condition; do ...; done`：条件为真时循环，适合不确定次数。
    - `until condition; do ...; done`：条件为假时循环，与 while 相反。
    - `break` 跳出当前循环，`continue` 跳过本次进入下一次，`break n` 跳出 n 层循环。
- `while read line` vs `for line in $(cat file)`：
    - `for line in $(cat file)`：按 IFS 分词，行中含空格会被拆分，且一次性加载整个文件到内存。
    - `while IFS= read -r line`：逐行读取，保留空格和特殊字符，内存友好。推荐。
- 批量服务启停示例：
  SERVICES=("user-service" "order-service" "pay-service")
  for svc in "${SERVICES[@]}"; do
  if pgrep -f "$svc.jar" > /dev/null; then
  echo "$svc 正在运行"
  else
  echo "$svc 未运行，启动中..."
  nohup java -jar "$svc.jar" > "$svc.log" 2>&1 &
  fi
  done
- 追问（管道内变量丢失）：
    - `cat file | while read line; do count=$((count+1)); done; echo $count` 输出为空。
    - 原因：管道会创建子 Shell，循环在子 Shell 中执行，变量修改不影响父 Shell。
    - 解决：使用进程替换 `while read line; do ...; done < <(cat file)`，或使用 `while read line; do ...; done < file`（重定向，非管道）。
</details>

**我的初答**：
**错漏点**：


### 题目3：`case` 语句与 `getopts` 解析命令行参数，编写标准 start/stop/restart/status 脚本
> 在 Java 后端部署中，标准的启动脚本通常支持 `start`、`stop`、`restart`、`status` 参数。请回答：
> 1. `case` 语句的语法是什么？与 `if-elif` 相比有何优势？请写出根据 `$1` 判断 start/stop/restart/status 的 `case` 结构。
> 2. `getopts` 如何解析带选项的参数（如 `-p 8080 -e prod`）？`OPTARG` 和 `OPTIND` 的含义是什么？如何处理未知选项？
> 3. 如何结合 `case` 与 `getopts`，编写一个支持 `-p` 指定端口、`-e` 指定环境、并执行 start/stop 的脚本框架？
     > 追问：`getopts` 与 `getopt` 有何区别？为什么 `getopts` 不支持长选项（如 `--port`）？若需支持长选项，应如何实现？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- `case` 语法：
  case "$1" in
  start)
  echo "启动服务"
  ;;
  stop)
  echo "停止服务"
  ;;
  restart)
  echo "重启服务"
  ;;
  status)
  echo "查看状态"
  ;;
  *)
  echo "用法: $0 {start|stop|restart|status}"
  exit 1
  ;;
  esac
    - 优势：多分支匹配更清晰，支持模式匹配（如 `start|run)`），比多层 if-elif 更易读。
- `getopts`：
    - 语法：`while getopts ":p:e:" opt; do ... done`
    - 前导冒号表示静默模式（不打印错误），选项后冒号表示该选项需要参数。
    - `$opt` 为当前选项，`$OPTARG` 为选项参数值，`$OPTIND` 为下一个参数索引。
    - 未知选项：`?` 分支处理。
- 综合脚本框架：
  PORT=8080
  ENV=dev
  while getopts ":p:e:" opt; do
  case $opt in
  p) PORT=$OPTARG ;;
  e) ENV=$OPTARG ;;
  \?) echo "未知选项: -$OPTARG"; exit 1 ;;
  :) echo "选项 -$OPTARG 需要参数"; exit 1 ;;
  esac
  done
  shift $((OPTIND-1))
  ACTION=$1
  case "$ACTION" in
  start) java -jar app.jar --server.port=$PORT --spring.profiles.active=$ENV & ;;
  stop) pkill -f app.jar ;;
  restart) pkill -f app.jar; sleep 2; java -jar app.jar --server.port=$PORT --spring.profiles.active=$ENV & ;;
  *) echo "用法: $0 [-p 端口] [-e 环境] {start|stop|restart}"; exit 1 ;;
  esac
- 追问：
    - `getopts` 是 Bash 内置，支持短选项，自动处理 `-p 8080` 和 `-p8080`。
    - `getopt` 是外部命令，支持长选项，但可移植性差。
    - `getopts` 不支持长选项。若需 `--port`，可手动解析 `$@`，或使用 `getopt -o p:e: -l port:,env: -- "$@"`。
</details>

**我的初答**：
**错漏点**：

---

## Day 7 (2026-09-29) —— cut、sed、sort、uniq 文本处理实战

### 题目1：使用 cut + sort + uniq 统计 Nginx 日志中访问量 Top 10 的 IP
> 现有 Nginx 访问日志 `access.log`，每行格式为：
> `192.168.1.1 - - [29/Sep/2026:10:00:00 +0800] "GET /api/user HTTP/1.1" 200 1024`
> 请写出完整的命令链，提取所有客户端 IP，统计每个 IP 的访问次数，并按访问量降序显示前 10 名。
> 追问：cut 命令的 `-d` 和 `-f` 分别是什么含义？如果日志字段之间是多个空格而不是单个空格，cut 能否正确处理？若不能，应改用哪个命令？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 完整命令链：
  awk '{print $1}' access.log | sort | uniq -c | sort -nr | head -10
  或者使用 cut（若字段以单空格分隔）：
  cut -d' ' -f1 access.log | sort | uniq -c | sort -nr | head -10

- 命令解释：
    - cut -d' ' -f1：以空格为分隔符，提取第 1 个字段（IP）。
    - sort：将 IP 按字典序排序，使相同 IP 相邻，便于 uniq 去重。
    - uniq -c：统计相邻重复行出现的次数，输出格式为“次数 IP”。
    - sort -nr：按数值降序排序（-n 数值排序，-r 逆序）。
    - head -10：取前 10 行。

- 追问：
    - cut 的 -d 指定分隔符，-f 指定提取的字段编号（从 1 开始），可指定多个字段如 -f1,3。
    - cut 只能处理“单个字符”作为分隔符，且不会将连续多个空格视为一个分隔符。对于多个空格的情况，cut 会提取出空字段，导致结果错误。
    - 多空格场景应使用 awk：awk '{print $1}' 自动按任意空白字符（空格、制表符）分割，并忽略连续空白。
</details>

**我的初答**：
**错漏点**：


### 题目2：sed 命令在 Java 日志脱敏与清洗中的实战（替换、删除、行范围）
> 在排查线上问题时，常需要从日志中提取信息并脱敏。请回答：
> 1. 如何用 sed 将日志中所有手机号（11 位数字）替换为 `****`？如何仅替换每行第一次出现的匹配？
> 2. 如何用 sed 删除所有空行？如何删除包含 `DEBUG` 字样的行？如何删除第 5 行到第 10 行？
> 3. `sed -i` 直接修改原文件有什么风险？如何先预览再修改？在 Java 后端脚本中，如何安全地使用 sed 修改配置文件（如替换 application.yml 中的端口号）？
     > 追问：sed 的 `-E` 与 `-r` 有何区别？`sed 's/old/new/g'` 中的 `g` 和 `p` 标志分别是什么含义？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 手机号脱敏：
  sed -E 's/1[3-9][0-9]{9}/****/g' app.log
  仅替换每行第一次：
  sed -E 's/1[3-9][0-9]{9}/****/' app.log

- 删除操作：
    - 删除空行：sed '/^$/d' app.log
    - 删除含 DEBUG 的行：sed '/DEBUG/d' app.log
    - 删除第 5 到第 10 行：sed '5,10d' app.log

- sed -i 风险：
    - 直接修改原文件，若命令写错可能导致数据无法恢复。
    - 安全做法：先不加 -i 预览输出，确认无误后再加 -i；或使用 -i.bak 备份原文件。
    - 修改配置文件示例：
      sed -i.bak 's/server.port: 8080/server.port: 9090/' application.yml

- 追问：
    - -E 和 -r 在 GNU sed 中等价，都表示使用扩展正则表达式（支持 +、?、|、() 等）。POSIX 标准用 -E，GNU sed 两者都支持。
    - g 表示全局替换（一行内所有匹配），不加 g 只替换第一个。p 通常与 -n 配合使用，打印匹配行，如 sed -n '/ERROR/p' 只输出含 ERROR 的行。
</details>

**我的初答**：
**错漏点**：


### 题目3：sort 多字段排序与 uniq 统计——分析 Java 接口耗时 Top N
> 现有应用日志 `api.log`，每行格式为：
> `2026-09-29 10:00:00 | /api/order | 320ms | 200`
> 字段以 ` | ` 分隔。请回答：
> 1. 如何用 sort 按耗时（第 3 个字段，数值）降序排列，显示耗时最高的 10 条请求？请说明 `-t`、`-k`、`-n`、`-r` 参数的作用。
> 2. 如何统计每个接口（第 2 个字段）的调用次数，并按次数降序排列？若接口名包含空格，cut 和 awk 应如何选择？
> 3. uniq 的 `-c`、`-d`、`-u` 分别是什么含义？uniq 为什么必须先 sort？若数据未排序直接 uniq 会发生什么？
     > 追问：如何统计状态码非 200 的请求中，各接口的异常次数？请写出命令链。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 按耗时降序取 Top 10：
  sort -t'|' -k3 -nr api.log | head -10
  注意：第 3 字段实际为 ` 320ms`，含单位 ms，需先去掉 ms 才能数值排序。正确做法：
  awk -F' \\| ' '{gsub(/ms/,"",$3); print $3, $0}' api.log | sort -nr | head -10
  或先提取耗时：
  awk -F' \\| ' '{print $3+0, $0}' api.log | sort -nr | head -10

- 参数说明：
    - -t'|'：指定字段分隔符为竖线（但实际有空格，需用 -t'|' 配合处理）。
    - -k3：按第 3 个字段排序。
    - -n：按数值排序（否则按字典序，"1000" 会排在 "200" 前面）。
    - -r：降序。

- 统计接口调用次数：
  awk -F' \\| ' '{print $2}' api.log | sort | uniq -c | sort -nr
  若接口名含空格，cut 无法正确处理（cut 只支持单字符分隔且不忽略连续空格），必须用 awk。

- uniq 选项：
    - -c：统计每行出现次数。
    - -d：仅显示重复行。
    - -u：仅显示不重复的行。
    - uniq 只能处理相邻重复行，因此必须先 sort 使相同内容相邻。若未排序，相同内容可能分散，uniq 无法正确统计。

- 追问：统计非 200 请求中各接口异常次数：
  awk -F' \\| ' '$4 != 200 {print $2}' api.log | sort | uniq -c | sort -nr
</details>

**我的初答**：
**错漏点**：

---

## Day 8 (2026-09-30) —— Shell 脚本综合实战与调试

### 题目1：Shell 函数的定义、参数传递与返回值，以及在 Java 服务启停中的复用
> 在编写部署脚本时，常将公共逻辑封装为函数。请回答：
> 1. Shell 函数如何定义？如何传递参数（$1、$@）？函数的返回值有哪两种方式？return 和 echo 有何本质区别？
> 2. 如何让函数返回字符串或复杂数据？为什么不能直接用 return 返回字符串？如何通过命令替换获取函数输出？
> 3. 请编写一个函数 check_service，接收服务名作为参数，判断该服务是否在运行，返回 0 表示运行中，1 表示未运行。并在主流程中调用该函数，根据结果决定是否启动服务。
     > 追问：函数内如何声明局部变量？local 关键字与直接赋值有何区别？若函数内修改了同名全局变量，外部会受影响吗？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 函数定义与参数：
  check_service() {
  local svc_name=$1
  if pgrep -f "$svc_name.jar" > /dev/null; then
  return 0
  else
  return 1
  fi
  }
    - 函数内 $1、$@、$# 与脚本参数类似，但属于函数自身的参数。
- 返回值方式：
    - return：返回 0~255 的整数状态码，通常用于表示成功/失败。0 成功，非 0 失败。
    - echo：输出字符串到标准输出，调用者用 $(function_name) 捕获。
    - 本质区别：return 是退出函数并返回状态码，echo 是输出数据。不能直接 return 字符串（会报错：numeric argument required）。
- 获取函数输出：
  get_version() {
  echo "1.0.0"
  }
  VERSION=$(get_version)
- check_service 完整示例：
  check_service() {
  local svc_name=$1
  if pgrep -f "${svc_name}.jar" > /dev/null; then
  echo "${svc_name} 正在运行"
  return 0
  else
  echo "${svc_name} 未运行"
  return 1
  fi
  }
  主流程：
  if check_service "user-service"; then
  echo "无需启动"
  else
  nohup java -jar user-service.jar &
  fi
- 追问：
    - local 声明局部变量，作用域仅限函数内，函数退出后销毁。直接赋值则默认为全局变量，会影响外部同名变量。
    - 若函数内修改全局变量，外部会受影响（因为 Shell 变量默认全局）。
</details>

**我的初答**：
**错漏点**：


### 题目2：Shell 脚本调试与健壮性：set -e、set -u、set -x、set -o pipefail 的区别与生产实践
> 在生产环境部署脚本中，一个未捕获的错误可能导致服务状态不一致。请回答：
> 1. set -e、set -u、set -x、set -o pipefail 分别是什么含义？它们如何提升脚本的健壮性？
> 2. set -e 有哪些“失效”场景（如命令在 if 条件中、在 && 或 || 中、在管道中非最后命令）？如何避免？
> 3. 请写一个脚本头部的最佳实践模板，包含常用的 set 选项、trap 清理、日志输出重定向。如何在不修改脚本的情况下临时调试（bash -x script.sh）？
     > 追问：set -e 与 trap 'echo error' ERR 如何配合使用？trap 捕获 ERR 时如何获取出错行号和命令？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- set 选项含义：
    - set -e：命令返回非 0 时立即退出脚本（但某些上下文除外）。
    - set -u：使用未定义变量时报错并退出。
    - set -x：打印每条执行的命令及其参数（调试用）。
    - set -o pipefail：管道中任一命令失败，整个管道返回失败（默认只返回最后一个命令的状态）。
- set -e 失效场景：
    - 命令在 if、while、until 条件中：if false; then ...; fi 不会退出。
    - 命令在 && 或 || 中：false || true 不会退出。
    - 管道中非最后一个命令：cmd1 | cmd2，cmd1 失败不会退出（除非开启 pipefail）。
    - 命令前有 !：! false 不会退出。
    - 函数内命令失败，但函数调用在条件中。
- 最佳实践模板：
  #!/bin/bash
  set -euo pipefail
  LOG_FILE="/var/log/deploy.log"
  exec >> "$LOG_FILE" 2>&1
  trap 'echo "[ERROR] 脚本在第 $LINENO 行失败，命令: $BASH_COMMAND"' ERR
  # 业务逻辑
- 临时调试：bash -x script.sh 或 set -x 局部开启。
- 追问：
    - trap '...' ERR 在命令返回非 0 时触发（类似 set -e 但可自定义处理）。
    - 获取行号：$LINENO；获取命令：$BASH_COMMAND。
    - 注意：trap ERR 也会受 set -e 失效场景影响，需结合 set -E（使 trap 继承到子 Shell 和函数）。
</details>

**我的初答**：
**错漏点**：


### 题目3：综合实战——编写一个 Spring Boot 自动化部署脚本（含备份、启停、健康检查、回滚）
> 请设计一个部署脚本 deploy.sh，接收两个参数：服务名（如 user-service）和版本号（如 1.0.0）。要求实现：
> 1. 从 /opt/releases/${服务名}/${版本号}/ 目录下获取 app.jar，若不存在则报错退出。
> 2. 将当前运行中的服务停止（优雅停机：先 kill -15，等待 10 秒，若仍存在则 kill -9）。
> 3. 备份旧版本 jar 到 /opt/backup/${服务名}/ 下，以时间戳命名。
> 4. 将新 jar 复制到 /opt/apps/${服务名}/app.jar。
> 5. 启动新服务，重定向日志到 /opt/logs/${服务名}.log。
> 6. 等待 15 秒后，检查进程是否存在且端口是否监听（如 8080）。若健康检查失败，则用备份回滚，并重启旧版本。
     > 请写出完整脚本框架，并说明关键步骤的设计理由。追问：如何保证脚本重复执行时不产生副作用（幂等性）？回滚时如何确保旧版本能正常启动？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 脚本框架（关键部分，完整脚本需补充错误处理）：
  #!/bin/bash
  set -euo pipefail
  SERVICE=$1
  VERSION=$2
  APP_DIR="/opt/apps/${SERVICE}"
  RELEASE_DIR="/opt/releases/${SERVICE}/${VERSION}"
  BACKUP_DIR="/opt/backup/${SERVICE}"
  LOG_DIR="/opt/logs"
  PORT=8080
  # 1. 检查发布包
  if [[ ! -f "${RELEASE_DIR}/app.jar" ]]; then
  echo "发布包不存在: ${RELEASE_DIR}/app.jar"
  exit 1
  fi
  # 2. 停止旧服务（优雅停机）
  PID=$(pgrep -f "${SERVICE}.jar" || true)
  if [[ -n "$PID" ]]; then
  kill -15 "$PID"
  for i in {1..10}; do
  if ! kill -0 "$PID" 2>/dev/null; then break; fi
  sleep 1
  done
  if kill -0 "$PID" 2>/dev/null; then
  kill -9 "$PID"
  fi
  fi
  # 3. 备份旧版本
  mkdir -p "$BACKUP_DIR"
  if [[ -f "${APP_DIR}/app.jar" ]]; then
  cp "${APP_DIR}/app.jar" "${BACKUP_DIR}/app-$(date +%Y%m%d%H%M%S).jar"
  fi
  # 4. 部署新版本
  cp "${RELEASE_DIR}/app.jar" "${APP_DIR}/app.jar"
  # 5. 启动新服务
  nohup java -jar "${APP_DIR}/app.jar" > "${LOG_DIR}/${SERVICE}.log" 2>&1 &
  # 6. 健康检查与回滚
  sleep 15
  if ! pgrep -f "${SERVICE}.jar" > /dev/null || ! ss -tunlp | grep -q ":${PORT}"; then
  echo "健康检查失败，回滚..."
  # 停止可能启动失败的进程
  pkill -f "${SERVICE}.jar" || true
  # 恢复最新备份
  LATEST_BACKUP=$(ls -t "${BACKUP_DIR}"/*.jar | head -1)
  cp "$LATEST_BACKUP" "${APP_DIR}/app.jar"
  nohup java -jar "${APP_DIR}/app.jar" > "${LOG_DIR}/${SERVICE}.log" 2>&1 &
  echo "已回滚并重启旧版本"
  exit 1
  fi
  echo "部署成功: ${SERVICE} ${VERSION}"
- 设计理由：
    - set -euo pipefail 保证任何错误立即暴露。
    - 优雅停机避免强制 kill 导致数据丢失或端口未释放。
    - 时间戳备份保留历史版本，便于回滚。
    - 健康检查超时后回滚，保证可用性。
- 追问（幂等性）：
    - 脚本执行前检查进程是否已存在，若已运行且版本相同可跳过；备份时若文件已存在可覆盖或跳过。
    - 回滚时确保备份文件完整，启动参数与旧版本一致（如端口、JVM 参数），必要时在备份时记录启动命令或元数据。
</details>

**我的初答**：
**错漏点**：

---