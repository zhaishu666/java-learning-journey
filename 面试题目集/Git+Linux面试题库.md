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
