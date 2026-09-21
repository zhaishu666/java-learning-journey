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

**错漏点：**

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