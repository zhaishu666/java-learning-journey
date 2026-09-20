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
**错漏点**：


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
**错漏点**：


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
**错漏点**：

---