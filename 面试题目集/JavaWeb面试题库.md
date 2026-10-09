## Day 9 (2026-10-01) —— Maven 与单元测试

### 题目1：Maven 的依赖传递、依赖调解与循环依赖处理
> 在大型 Java 项目中，Maven 的依赖管理是核心。请回答：
> 1. Maven 的依赖范围（scope）有哪些？compile、provided、runtime、test、system 分别适用于什么场景？对打包结果有何影响？
> 2. Maven 的依赖传递机制是什么？当 A 依赖 B（1.0），C 依赖 B（2.0），且项目同时依赖 A 和 C 时，最终引入哪个版本的 B？请说明 Maven 的依赖调解原则（最短路径优先、第一声明优先）。
> 3. 如何排除传递性依赖？请写出 exclusions 的用法。如果项目中出现循环依赖（A 依赖 B，B 依赖 A），Maven 会如何处理？如何排查和解决？
     > 追问：dependencyManagement 与 dependencies 的区别是什么？为什么在父 POM 中常用 dependencyManagement 统一版本？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 依赖范围（scope）：
    - compile（默认）：编译、测试、运行都有效，打包时包含。
    - provided：编译和测试有效，运行由容器提供（如 Servlet API），打包时不包含。
    - runtime：编译无效，测试和运行有效（如 JDBC 驱动），打包时包含。
    - test：仅测试有效（如 JUnit），打包不包含。
    - system：类似 provided，但需显式指定本地路径，不推荐使用。
- 依赖传递与调解：
    - 依赖传递：A 依赖 B，B 依赖 C，则 A 自动引入 C（受 scope 限制，如 test 范围不会传递）。
    - 依赖调解原则：
        1. 最短路径优先：路径短的版本胜出。
        2. 第一声明优先：路径长度相同时，POM 中先声明的依赖胜出。
    - 上例中，A→B（1.0）路径长度 2，C→B（2.0）路径长度 2，若 A 在 POM 中先声明，则引入 B（1.0）。
- 排除依赖：
  <dependency>
  <groupId>com.example</groupId>
  <artifactId>A</artifactId>
  <version>1.0</version>
  <exclusions>
  <exclusion>
  <groupId>com.example</groupId>
  <artifactId>B</artifactId>
  </exclusion>
  </exclusions>
  </dependency>
- 循环依赖：Maven 不允许模块间循环依赖（A 依赖 B，B 依赖 A），构建时会报错。排查：使用 mvn dependency:tree 查看依赖树，或使用 IDE 的依赖分析工具。解决：抽取公共模块、重构代码消除循环。
- 追问：
    - dependencyManagement：仅声明依赖版本，不实际引入依赖，子模块需显式声明依赖但不写版本。
    - dependencies：直接引入依赖。
    - 父 POM 用 dependencyManagement 统一版本，避免子模块版本冲突。
</details>

**我的初答**：
1. Maven的依赖范围主要有test,compile,provided,runtime,system;test主要适用于单元测试的场景,不会被打包进jar;其他的不了解
2. 依赖传递机制: 当传入一个依赖时,这个依赖可能还依赖着其他的依赖,Maven会将这些依赖也一并传入.
3. 最终会引入1.0版本的B,因为A是最先声明的且A和C对B的依赖路径一样长
4. 通过<exclusions>标签可以指定要排除的依赖,只需要在<dependencies>内部用<exclusions>声明即可.
5. 当出现循环依赖时若pom文件中已存在如B依赖的A,就直接复用该依赖,不会再引入新的依赖;排除和处理不了解
6. dependencyManagement用于统一管理子项目的依赖,防止出现依赖冲突等情况,于dependencies的区别主要就是dependencies只负责管理本项目的依赖

**错漏点**：
<details>
<summary><strong>点击展开错漏点</strong></summary>

你的初答点评

- **scope**：只答了 test，且"不会被打包"这点对。其余四个空白——provided 和 runtime 是部署题高频考点。

- **依赖传递 + 调解**：**满分答案**，两条原则用得准确，结论正确（选 B 1.0）。

- **exclusions**：描述太含糊（"通过标签……用声明"），面试里要能写出具体 XML 结构。

- **循环依赖**：**答错了**。Maven 对项目级循环依赖不会"直接复用"，而是**直接构建失败报错**。你描述的"复用已有依赖"是 Maven 对**依赖图中重复节点**的处理（防止无限递归），不是对循环依赖的容忍。

- **dependencyManagement**：说了"统一版本"，但没答出最关键的区别——**它只声明版本、不实际引入依赖**，子模块必须自己在 dependencies 里写 GAV（可省略 version）才会真正引入。


---

## 参考答案

### 1. 六种 scope 及打包影响

表格

| scope | 编译期 | 测试期 | 运行期 | 打入最终包？ | 典型场景 |
| --- | --- | --- | --- | --- | --- |
| **compile**（默认） | ✔   | ✔   | ✔   | **会** | 绝大多数业务依赖，如 spring-core、fastjson |
| **provided** | ✔   | ✔   | ✘   | **不会** | 运行环境已提供的 API：**servlet-api、lombok**——Tomcat 自带 servlet 包，打进去会类冲突 |
| **runtime** | ✘   | ✔   | ✔   | **会** | 编译不需要、运行才需要：**JDBC 驱动**（mysql-connector）——代码面向 DriverManager 编程，编译期不引用驱动类 |
| **test** | ✘   | ✔   | ✘   | **不会** | JUnit、Mockito，只在测试 classpath |
| **system** | ✔   | ✔   | ✘   | 不会  | 本地指定 `systemPath` 的 jar（已废弃用法，**官方不推荐**，应安装到本地仓库代替） |

答题主线：**scope 决定依赖出现在哪个 classpath（编译/测试/运行），进而决定是否打进包**。provided 和 test 不打包，compile 和 runtime 打包。

### 2. 依赖传递与调解

**传递机制**：你声明依赖 A 时，Maven 会读取 A 的 POM，把 A 的 compile/runtime 依赖递归拉进来，构成依赖树。（注意：scope 为 test/provided 的依赖**不传递**。）

**两条调解原则**（你答对了，规范化表述）：

1. **最短路径优先**：B(1.0) 路径是 `项目→A→B`（深度 2），B(2.0) 路径是 `项目→C→B`（深度 2）——深度相同，进入第二条；

2. **第一声明优先（first declaration wins）**：POM 中先声明的 A 胜出，最终引入 **B 1.0**。


若路径不同（如 B 2.0 是 `项目→B` 直接依赖，深度 1），则无论声明顺序都选 2.0。实战技巧：**想强制锁定版本，就在自己的 POM 里直接声明该依赖**（路径最短，必然生效）。

### 3. 排除传递依赖

xml复制

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>A</artifactId>
    <version>1.0</version>
    <exclusions>
        <exclusion>
            <!-- 只需 groupId + artifactId，不写 version -->
            <groupId>com.example</groupId>
            <artifactId>B</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

典型场景：排除低版本有漏洞的依赖、排除冲突的日志实现（如排除 spring-boot 里的 logback 换 log4j2）。

**循环依赖处理（纠正你的答案）：**

- Maven 对**依赖图中的环**（A→B→A）不会无限递归，会按已访问节点去重；

- 但**项目/模块级循环依赖**（模块 A 依赖模块 B、B 又依赖 A），Maven **直接构建失败**，报 `The projects in the reactor contain a cyclic reference`；

- **排查**：`mvn dependency:tree` 看依赖树找冲突；循环依赖用 IDE（IDEA 的 Maven 面板）或 `mvn dependency:analyze`；

- **解决**：本质是设计问题——**抽取公共模块 C**（A、B 共同依赖的部分下沉），或改用接口 + 依赖注入/事件机制解耦，而不是靠 Maven 技巧绕过。


### 追问：dependencyManagement vs dependencies

表格

|     | dependencies | dependencyManagement |
| --- | --- | --- |
| 作用  | **实际引入**依赖到本项目及所有继承者 | **只锁定版本/scope，不引入任何依赖** |
| 生效条件 | 声明即生效 | 子模块必须在 dependencies 中声明 GAV（version 可省略）才真正引入 |
| 典型位置 | 任意 POM | 父 POM / BOM |

**为什么父 POM 用它统一版本：**

1. **版本集中管控**：20 个子模块的 mysql-connector 版本在父 POM 改一处即可，杜绝"各模块版本漂移"导致的冲突；

2. **按需引入**：子模块声明什么才引入什么，避免父 POM 强制把依赖塞给所有模块（如果用 dependencies 写，所有子模块会被迫继承）；

3. 这也是 **BOM（如 spring-boot-dependencies）的原理**——你 `import` 它的 dependencyManagement，只获得一整套兼容的版本表，依赖还是自己声明。
</details>

### 题目2：JUnit 5 的核心注解、生命周期与参数化测试（Java 后端单元测试规范）
> JUnit 5 是目前 Java 单元测试的主流框架。请回答：
> 1. @Test、@BeforeEach、@AfterEach、@BeforeAll、@AfterAll、@DisplayName、@Disabled 分别是什么含义？执行顺序如何？
> 2. 参数化测试 @ParameterizedTest 如何使用？@ValueSource、@CsvSource、@MethodSource 分别适用于什么场景？请写出一个参数化测试的例子。
> 3. 断言方法 assertEquals、assertThrows、assertAll、assertTimeout 分别用于什么场景？与 JUnit 4 相比，JUnit 5 有哪些改进（如支持 Lambda、断言消息延迟加载）？
     > 追问：@TestInstance(Lifecycle.PER_CLASS) 的作用是什么？它如何影响 @BeforeAll 和 @AfterAll 的访问修饰符要求？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 核心注解与执行顺序：
    - @BeforeAll（静态）：所有测试方法执行前执行一次。
    - @BeforeEach：每个测试方法执行前执行。
    - @Test：测试方法。
    - @AfterEach：每个测试方法执行后执行。
    - @AfterAll（静态）：所有测试方法执行后执行一次。
    - @DisplayName：自定义测试显示名称。
    - @Disabled：禁用测试。
    - 顺序：@BeforeAll → @BeforeEach → @Test → @AfterEach → @AfterAll。
- 参数化测试：
    - @ParameterizedTest 替代 @Test，配合参数来源注解使用。
    - @ValueSource：提供简单类型数组（如 ints、strings）。
    - @CsvSource：提供 CSV 格式的多参数。
    - @MethodSource：提供方法返回的 Stream 作为参数源，适合复杂对象。
    - 示例：
      @ParameterizedTest
      @CsvSource({"1, 2, 3", "4, 5, 9"})
      void testAdd(int a, int b, int expected) {
      assertEquals(expected, calculator.add(a, b));
      }
- 断言方法：
    - assertEquals：值相等。
    - assertThrows：断言抛出指定异常。
    - assertAll：分组断言，所有断言都执行，最后汇总失败信息。
    - assertTimeout：断言方法在指定时间内完成。
    - JUnit 5 改进：支持 Lambda 表达式、断言消息延迟加载（使用 Supplier）、@DisplayName、@Nested、@Tag 等。
- 追问：
    - @TestInstance(Lifecycle.PER_CLASS)：测试类只实例化一次，@BeforeAll 和 @AfterAll 无需 static，可访问实例变量。
</details>

**我的初答**：
**错漏点**：


### 题目3：Mockito 与 Spring Boot Test 在单元测试与集成测试中的应用
> 在 Spring Boot 项目中，经常使用 Mockito 和 Spring Boot Test 进行测试。请回答：
> 1. Mockito 的 @Mock、@InjectMocks、@Spy、@Captor 分别是什么含义？@Mock 与 @Spy 有何区别？
> 2. 如何使用 Mockito 的 when().thenReturn() 和 doReturn().when() 进行打桩？两者有何区别？verify() 和 verifyNoInteractions() 的作用是什么？
> 3. Spring Boot Test 中，@SpringBootTest、@WebMvcTest、@DataJpaTest、@MockBean、@SpyBean 分别适用于什么测试场景？为什么单元测试推荐不启动 Spring 容器？
     > 追问：在测试 Controller 层时，MockMvc 的 perform() 方法如何使用？如何模拟 POST 请求并校验 JSON 响应？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- Mockito 注解：
    - @Mock：创建 Mock 对象（所有方法返回默认值，如 null、0、false）。
    - @InjectMocks：创建实例，并将 @Mock 对象注入其中。
    - @Spy：创建 Spy 对象（默认调用真实方法，可选择性打桩）。
    - @Captor：捕获方法参数，用于后续断言。
    - @Mock vs @Spy：@Mock 所有方法均被模拟，不调用真实逻辑；@Spy 默认调用真实方法，仅被打桩的方法被模拟。
- 打桩方式：
    - when(mock.method()).thenReturn(value)：适用于有返回值的方法。
    - doReturn(value).when(mock).method()：适用于 void 方法或 Spy 对象。
    - 区别：doReturn 不会执行真实方法，when 会先执行真实方法（对 Spy 有副作用）。
    - verify(mock, times(1)).method()：验证方法被调用一次。
    - verifyNoInteractions(mock)：验证该 Mock 对象从未被交互。
- Spring Boot Test 注解：
    - @SpringBootTest：启动完整 Spring 容器，适合集成测试。
    - @WebMvcTest：仅启动 MVC 相关组件（Controller、Filter 等），适合 Controller 层测试。
    - @DataJpaTest：仅启动 JPA 相关组件（嵌入式数据库），适合 Repository 层测试。
    - @MockBean：将 Mock 对象注入 Spring 容器，替换真实 Bean。
    - @SpyBean：将 Spy 对象注入 Spring 容器，默认调用真实方法。
    - 单元测试推荐不启动 Spring 容器（使用纯 Mockito），因为启动容器慢，且依赖真实环境。
- 追问：MockMvc 示例：
  mockMvc.perform(post("/api/user")
  .contentType(MediaType.APPLICATION_JSON)
  .content("{\"name\":\"张三\"}"))
  .andExpect(status().isOk())
  .andExpect(jsonPath("$.code").value(200));
</details>

**我的初答**：
**错漏点**：

---

## Day 10 (2026-10-03) —— Web基础与Spring Boot Web入门

### 题目1：HTTP协议核心、状态码与RESTful接口设计
> 请简述HTTP协议中GET与POST的核心区别（至少4点），并说明常见HTTP状态码200、301、302、400、401、403、404、500的含义。在Spring Boot中设计RESTful接口时，如何用@GetMapping、@PostMapping、@PutMapping、@DeleteMapping对应CRUD？追问：HTTP是无状态协议，Spring Boot中如何保持用户登录状态？Session和Token（JWT）方案有何区别？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- GET vs POST核心区别：
  1. 语义：GET用于获取资源，POST用于提交数据。
  2. 参数位置：GET参数在URL查询字符串中，POST参数在请求体中。
  3. 幂等性：GET是幂等的（多次请求结果相同），POST通常不是幂等的。
  4. 缓存：GET可被浏览器缓存，POST默认不缓存。
  5. 安全性：两者都不安全，敏感数据必须用HTTPS。
- 常见状态码：
  - 200 OK：请求成功。
  - 301 Moved Permanently：永久重定向。
  - 302 Found：临时重定向。
  - 400 Bad Request：请求参数错误。
  - 401 Unauthorized：未认证。
  - 403 Forbidden：已认证但无权限。
  - 404 Not Found：资源不存在。
  - 500 Internal Server Error：服务器内部错误。
- RESTful CRUD对应：
  - GET /users：查询用户列表。
  - GET /users/{id}：查询单个用户。
  - POST /users：新增用户。
  - PUT /users/{id}：全量更新用户。
  - PATCH /users/{id}：部分更新用户。
  - DELETE /users/{id}：删除用户。
- 无状态与登录保持：
  - Session方案：服务端存储Session，客户端通过Cookie携带SessionId。集群环境需Session共享（如Redis）。
  - JWT方案：客户端存储Token，服务端不保存状态。适合分布式、跨域，但无法主动失效（需黑名单或短过期+刷新Token）。
</details>

**我的初答**：
**错漏点**：


### 题目2：Spring Boot Web 参数绑定注解与JSON处理
> 请说明@Controller与@RestController的区别。在Spring Boot中，如何分别接收以下请求参数：
> 1. URL查询参数 ?name=张三&age=20
> 2. 路径参数 /users/100
> 3. JSON请求体 {"name":"张三","age":20}
> 4. 请求头 User-Agent
     > 请写出对应注解，并说明@RequestParam的required和defaultValue属性的作用。追问：@RequestBody和@RequestParam能同时使用吗？日期类型参数如何接收（@DateTimeFormat / @JsonFormat）？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- @Controller vs @RestController：
  - @Controller：返回视图名称，通常配合视图解析器（如Thymeleaf、JSP）。
  - @RestController：等于@Controller + @ResponseBody，所有方法返回JSON/XML数据，适合前后端分离。
- 参数接收：
  1. 查询参数：@RequestParam("name") String name，@RequestParam("age") Integer age。
  2. 路径参数：@PathVariable("id") Long id，对应@GetMapping("/users/{id}")。
  3. JSON请求体：@RequestBody User user，请求头Content-Type: application/json。
  4. 请求头：@RequestHeader("User-Agent") String userAgent。
- @RequestParam属性：
  - required：默认true，参数缺失时抛MissingServletRequestParameterException。
  - defaultValue：参数缺失时使用默认值，同时隐含required=false。
- 追问：
  - @RequestBody和@RequestParam可以同时使用，但@RequestBody只能有一个（请求体只能读一次）。
  - 日期接收：
    - GET查询参数：@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date。
    - JSON请求体：@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") LocalDateTime time。
</details>

**我的初答**：
**错漏点**：


### 题目3：Spring Boot 内嵌Tomcat、Web自动配置与三层架构
> 请说明spring-boot-starter-web起步依赖主要包含哪些内容？Spring Boot如何实现内嵌Tomcat的自动启动？如何修改内嵌Tomcat端口和上下文路径？在前后端分离项目中，Controller、Service、Mapper三层架构的职责分别是什么？追问：Spring Boot中DispatcherServlet如何被自动注册？为什么Spring Boot项目通常不需要web.xml？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- spring-boot-starter-web主要包含：
  - spring-web、spring-webmvc：MVC核心。
  - tomcat-embed-core、tomcat-embed-el：内嵌Tomcat。
  - jackson-databind：JSON序列化/反序列化。
  - spring-boot-starter-json、validation等。
- 内嵌Tomcat自动启动：
  - Spring Boot启动时，通过ServletWebServerFactoryAutoConfiguration创建TomcatServletWebServerFactory。
  - 在refresh阶段，调用createWebServer()创建Tomcat实例，并启动。
  - 无需手动部署WAR到外部Tomcat。
- 修改配置：
  - 端口：server.port=8081
  - 上下文路径：server.servlet.context-path=/api
  - 也可用application.yml配置。
- 三层架构职责：
  - Controller：接收请求、参数校验、调用Service、返回响应。
  - Service：业务逻辑、事务控制、组装数据。
  - Mapper/DAO：数据库访问，执行SQL。
- 追问：
  - DispatcherServlet由DispatcherServletAutoConfiguration自动注册，通过ServletRegistrationBean绑定到内嵌容器。
  - 不需要web.xml因为Servlet 3.0+支持注解和ServletContainerInitializer，Spring Boot通过自动配置和Java Config替代了web.xml。
</details>

**我的初答**：
**错漏点**：

---

## Day 11 (2026-10-04) —— 分层思想与 IoC（控制反转）

### 题目1：三层架构的职责划分与依赖关系，为什么 Controller 不能直接调用 Mapper？
> 在 Spring Boot Web 项目中，通常分为 Controller、Service、Mapper（DAO）三层。请回答：
> 1. Controller 层、Service 层、Mapper 层各自的职责是什么？三层之间的调用关系是怎样的？为什么不能跨层调用（如 Controller 直接调 Mapper）？
> 2. Service 层为什么通常定义为接口 + 实现类？直接写一个类不行吗？在 MyBatis-Plus 或 JPA 中，Service 接口还有必要吗？
> 3. DTO、VO、Entity（PO）三种对象在三层架构中分别承担什么角色？为什么不能直接用 Entity 接收前端参数并返回给前端？
     > 追问：如果 Controller 直接调用 Mapper，在小型项目中似乎也能跑，这种写法在什么情况下会出问题？请从可维护性、事务控制和代码复用的角度分析。

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 三层职责与调用关系：
  - Controller：接收 HTTP 请求、参数校验、调用 Service、封装响应。不写业务逻辑，不直接访问数据库。
  - Service：业务逻辑的核心，负责事务控制、业务规则校验、调用多个 Mapper 组合数据。
  - Mapper/DAO：只负责数据库访问，执行 SQL，返回 Entity。
  - 调用关系：Controller → Service → Mapper，单向依赖，不可跨层。
- 不能跨层的原因：
  1. 业务逻辑散落：Controller 直接调 Mapper 会导致业务逻辑写在 Controller 中，难以复用（如定时任务也需要同样的逻辑）。
  2. 事务控制困难：@Transactional 通常加在 Service 层，Controller 层加事务会导致事务范围过大或失效。
  3. 可测试性差：Controller 依赖 Web 环境，单元测试复杂；Service 层可独立测试。
- Service 接口 + 实现类：
  - 面向接口编程，便于替换实现（如从 MySQL 换到 Redis 缓存）、AOP 代理（JDK 动态代理基于接口）。
  - 小型项目或使用 MyBatis-Plus 的 IService 时，可直接写实现类，但接口仍是主流规范。
- DTO/VO/Entity：
  - DTO（Data Transfer Object）：接收前端参数，字段与前端请求对应。
  - VO（View Object）：返回给前端的对象，可裁剪敏感字段。
  - Entity（PO）：与数据库表一一对应，包含所有字段，不直接暴露给前端。
  - 不能直接用 Entity 的原因：可能暴露敏感字段（如密码、手机号），且前端参数与数据库字段不一致时会导致校验混乱。
- 追问：Controller 直接调 Mapper 在小型项目中短期可行，但一旦出现多端调用（App、小程序）、定时任务、事务嵌套、缓存需求，就会导致代码重复、事务失效、难以维护。分层是规范，不是束缚。
</details>

**我的初答**：
**错漏点**：


### 题目2：IoC（控制反转）与 DI（依赖注入）的本质区别，以及 Spring 容器管理 Bean 的核心流程
> Spring 的核心是 IoC 容器。请回答：
> 1. 什么是 IoC（控制反转）？什么是 DI（依赖注入）？两者是什么关系？为什么说“IoC 是一种设计思想，DI 是它的实现方式”？
> 2. Spring 容器管理 Bean 的核心流程是什么？从启动到 Bean 可用，经历了哪些阶段（扫描、实例化、属性注入、初始化、放入单例池）？
> 3. Spring 提供了哪些依赖注入方式（构造器注入、Setter 注入、字段注入）？为什么 Spring 官方推荐构造器注入？字段注入（@Autowired 直接在字段上）有什么缺点？
     > 追问：如果一个类没有加 @Component/@Service 等注解，Spring 能管理它吗？@Bean 注解和 @Component 有什么区别？分别在什么场景下使用？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- IoC 与 DI 的关系：
  - IoC（Inversion of Control）：控制反转，是一种设计思想。对象的创建和依赖管理权从程序员手中反转给 Spring 容器。
  - DI（Dependency Injection）：依赖注入，是 IoC 的具体实现方式。容器在创建对象时，自动将其依赖的对象注入进去。
  - 关系：IoC 是目标，DI 是手段。Spring 通过 DI 实现 IoC。
- Bean 管理核心流程：
  1. 扫描：通过 @ComponentScan 扫描指定包下的类，识别 @Component、@Service、@Repository、@Controller 等注解。
  2. 注册 BeanDefinition：将扫描到的类信息封装为 BeanDefinition，注册到容器。
  3. 实例化：根据 BeanDefinition 通过反射创建对象（构造器）。
  4. 属性注入：为对象的依赖字段/构造器/Setter 注入值（DI）。
  5. 初始化：执行 @PostConstruct、InitializingBean.afterPropertiesSet()、init-method。
  6. 放入单例池：将完成初始化的 Bean 放入 singletonObjects 缓存（单例 Bean）。
  7. 使用：从容器中获取 Bean 并调用。
  8. 销毁：容器关闭时，执行 @PreDestroy、DisposableBean.destroy()、destroy-method。
- 注入方式对比：
  - 构造器注入：Spring 官方推荐。保证依赖不可变（final）、不为 null、便于单元测试、避免循环依赖（构造器循环依赖会直接报错，暴露问题）。
  - Setter 注入：适合可选依赖，但对象可能在注入前处于不完整状态。
  - 字段注入（@Autowired 在字段上）：代码最简洁，但缺点明显：
    1. 无法使用 final 修饰，依赖可变。
    2. 单元测试时无法直接 new 对象并传入 Mock（需反射或 Spring 容器）。
    3. 隐藏依赖关系，类可能有过多的 @Autowired 字段。
    4. 容易导致循环依赖（Spring 通过三级缓存解决，但设计上应避免）。
- 追问：
  - 没有加 @Component 等注解的类，Spring 默认不管理。但可通过 @Bean 在 @Configuration 类中手动注册。
  - @Bean：方法级别，适用于第三方类（无法修改源码加注解）、需要复杂初始化逻辑的 Bean。
  - @Component：类级别，适用于自己编写的类，配合组件扫描自动注册。
</details>

**我的初答**：
**错漏点**：


### 题目3：@Autowired 的注入原理与循环依赖的解决（三级缓存）
> 在 Spring Boot 项目中，@Autowired 是最常用的注入注解。请回答：
> 1. @Autowired 的注入规则是什么？它是按类型注入还是按名称注入？当存在多个同类型 Bean 时，Spring 如何处理？@Qualifier 和 @Primary 分别解决什么问题？
> 2. 什么是循环依赖？Spring 如何通过三级缓存（singletonObjects、earlySingletonObjects、singletonFactories）解决单例 Bean 的循环依赖？请描述 A 依赖 B、B 依赖 A 的创建过程。
> 3. 为什么构造器注入的循环依赖 Spring 无法解决？如果项目中出现了构造器循环依赖，应如何重构？
     > 追问：@Resource 与 @Autowired 有什么区别？@Resource 是 JDK 提供的还是 Spring 提供的？它们的注入顺序有何不同？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- @Autowired 注入规则：
  - 默认按类型（byType）注入。
  - 若存在多个同类型 Bean，再按名称（byName）匹配（字段名或参数名）。
  - 若仍无法确定，则抛出 NoUniqueBeanDefinitionException。
  - 解决方案：
    - @Qualifier("beanName")：指定注入的 Bean 名称。
    - @Primary：标记某个 Bean 为首选，当存在多个同类型 Bean 时优先注入。
- 循环依赖与三级缓存：
  - 循环依赖：A 依赖 B，B 依赖 A。
  - 三级缓存：
    1. singletonObjects（一级）：成品 Bean。
    2. earlySingletonObjects（二级）：半成品 Bean（已实例化但未完成属性注入）。
    3. singletonFactories（三级）：Bean 工厂，用于生成代理对象（如 AOP）。
  - 创建过程（A 依赖 B，B 依赖 A）：
    1. 创建 A：实例化 A（调用构造器），将 A 的工厂放入三级缓存。
    2. 注入 A 的属性：发现需要 B，于是创建 B。
    3. 创建 B：实例化 B，将 B 的工厂放入三级缓存。
    4. 注入 B 的属性：发现需要 A，从三级缓存中获取 A 的工厂，生成 A 的早期引用（放入二级缓存），注入给 B。
    5. B 完成初始化，放入一级缓存。
    6. A 继续注入 B（此时 B 已完成），A 完成初始化，放入一级缓存。
- 构造器循环依赖无法解决：
  - 构造器注入要求在实例化阶段就传入依赖，此时 Bean 尚未实例化，无法提前暴露引用，因此直接抛出 BeanCurrentlyInCreationException。
  - 解决方案：
    1. 使用 @Lazy 延迟注入（生成代理，实际使用时才创建）。
    2. 改用 Setter/字段注入。
    3. 重构代码，消除循环依赖（推荐）。
- 追问：
  - @Resource 是 JDK 提供的（JSR-250），@Autowired 是 Spring 提供的。
  - @Resource 默认按名称（byName）注入，若名称找不到则按类型（byType）。
  - @Autowired 默认按类型，可配合 @Qualifier 按名称。
</details>

**我的初答**：
**错漏点**：

---

## Day 12 (2026-10-05) —— JDBC 与 MyBatis 基础

### 题目1：JDBC 六步操作流程、PreparedStatement 与事务控制
> 请回答：
> 1. JDBC 操作数据库的六步标准流程是什么？每一步的作用是什么？
> 2. Statement 与 PreparedStatement 的区别是什么？为什么 PreparedStatement 能防止 SQL 注入？它的预编译机制在 MySQL 驱动中默认是否开启？
> 3. JDBC 如何实现事务控制？为什么必须关闭自动提交（setAutoCommit(false)）？如果不关闭，可能出现什么问题？
     > 追问：JDBC 中的事务和 Spring 的 @Transactional 有什么关系？为什么说 Spring 的事务管理器本质上是对 JDBC Connection 的封装？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 六步流程：
  1. 加载驱动（Class.forName 或 SPI 自动加载，JDBC 4.0+ 可省略）。
  2. 获取连接（DriverManager.getConnection 或 DataSource.getConnection）。
  3. 创建 Statement/PreparedStatement。
  4. 执行 SQL（executeQuery 返回 ResultSet，executeUpdate 返回影响行数）。
  5. 处理结果集（遍历 ResultSet，映射为 Java 对象）。
  6. 释放资源（关闭 ResultSet、Statement、Connection，推荐 try-with-resources）。
- Statement vs PreparedStatement：
  - Statement：SQL 拼接字符串，存在 SQL 注入风险，每次执行都需数据库解析。
  - PreparedStatement：使用 ? 占位符，参数作为纯数据传递，不参与 SQL 语法解析，可防止注入。
  - 预编译：MySQL 驱动默认 useServerPrepStmts=false，即客户端预编译（驱动内部拼接并转义），并非服务端预编译。
- 事务控制：
  - 默认 autoCommit=true，每条 SQL 自动提交。
  - 手动事务：conn.setAutoCommit(false) → 执行多条 SQL → conn.commit() 或 conn.rollback()。
  - 不关闭自动提交，无法回滚多条 SQL，也无法保证原子性。
- 追问：
  - Spring 的 @Transactional 底层通过 DataSourceTransactionManager 获取 Connection，设置 autoCommit=false，并在方法结束时 commit/rollback。
  - Spring 通过 TransactionSynchronizationManager 将 Connection 绑定到 ThreadLocal，保证同一事务使用同一连接。
</details>

**我的初答**：
**错漏点**：


### 题目2：MyBatis 核心组件与 Mapper 接口的动态代理机制
> 请回答：
> 1. MyBatis 的核心组件有哪些（SqlSessionFactory、SqlSession、Executor、MappedStatement、MapperProxy）？各自的作用是什么？
> 2. Mapper 接口没有实现类，MyBatis 是如何调用到 XML 中的 SQL 的？请说明 MapperProxy 和 MapperMethod 的底层原理。
> 3. MyBatis 中 #{} 和 ${} 的区别是什么？为什么 ${} 存在 SQL 注入风险？在什么场景下必须使用 ${}（如动态表名、排序字段）？如何安全使用？
     > 追问：MyBatis 的一级缓存和二级缓存分别是什么？一级缓存为什么在 Spring 整合后可能失效？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 核心组件：
  - SqlSessionFactory：全局单例，创建 SqlSession。
  - SqlSession：一次会话，封装 Executor 和 Connection，线程不安全，用完即关。
  - Executor：执行器，负责 SQL 执行和缓存管理（SimpleExecutor、ReuseExecutor、BatchExecutor）。
  - MappedStatement：封装一条 SQL 的所有信息（SQL 语句、参数映射、结果映射）。
  - MapperProxy：Mapper 接口的动态代理对象。
- Mapper 动态代理：
  - MyBatis 启动时，通过 MapperRegistry 注册 Mapper 接口。
  - 调用 sqlSession.getMapper(UserMapper.class) 时，通过 JDK 动态代理生成 MapperProxy。
  - 调用接口方法时，MapperProxy.invoke 拦截，根据方法全限定名（如 com.example.UserMapper.selectById）找到对应的 MappedStatement。
  - 再通过 MapperMethod 执行 SQL 并映射结果。
- #{} vs ${}：
  - #{}：预编译占位符，生成 ?，参数通过 PreparedStatement 传入，防止注入。
  - ${}：字符串拼接，直接替换 SQL，存在注入风险。
  - 必须用 ${}：动态表名（FROM ${tableName}）、动态排序字段（ORDER BY ${column}）。
  - 安全用法：对 ${} 参数进行白名单校验，避免直接使用用户输入。
- 追问：
  - 一级缓存：SqlSession 级别，默认开启，同一 SqlSession 中相同 SQL 复用结果。
  - 二级缓存：Mapper 级别，需手动开启（cache 标签），跨 SqlSession 共享。
  - Spring 整合后，一级缓存可能失效：因为 Spring 通过 SqlSessionTemplate 管理 SqlSession，每次查询可能获取新的 SqlSession（取决于事务范围），导致缓存不命中。同一事务内会复用 SqlSession。
</details>

**我的初答**：
**错漏点**：


### 题目3：MyBatis 参数传递、结果映射与动态 SQL
> 请回答：
> 1. MyBatis 中如何传递多个参数？@Param 注解的作用是什么？不写 @Param 时，多参数会如何处理（param1、param2 或 arg0、arg1）？
> 2. resultType 和 resultMap 的区别是什么？当数据库字段名（如 user_name）与 Java 属性名（userName）不一致时，有哪几种解决方案（别名、mapUnderscoreToCamelCase、resultMap）？
> 3. 动态 SQL 中 if、choose/when/otherwise、foreach、where、set、trim 标签分别解决什么问题？请写出一个使用 foreach 批量插入的示例。
     > 追问：MyBatis 的 @MapperScan 和 @Mapper 有什么区别？为什么推荐使用 @MapperScan？

<details>
<summary><strong>点击展开标准解析</strong></summary>

- 参数传递：
  - 单个参数：直接使用 #{name} 或 #{param1}。
  - 多个参数：推荐使用 @Param("name") 指定名称，如 selectByNameAndAge(@Param("name") String name, @Param("age") Integer age)。
  - 不写 @Param：MyBatis 会使用 arg0、arg1 或 param1、param2 作为参数名，可读性差，不推荐。
  - 对象参数：直接使用属性名 #{userName}。
- resultType vs resultMap：
  - resultType：简单映射，要求数据库列名与 Java 属性名一致（或开启驼峰映射）。
  - resultMap：自定义映射，处理复杂关系（一对一、一对多、多对多），指定 column 与 property 的对应关系。
  - 字段名不一致解决方案：
    1. SQL 别名：SELECT user_name AS userName FROM ...
    2. 开启驼峰映射：mybatis.configuration.map-underscore-to-camel-case=true。
    3. resultMap 显式映射。
- 动态 SQL 标签：
  - if：条件判断。
  - choose/when/otherwise：多分支选择，类似 switch。
  - foreach：遍历集合，常用于 IN 查询和批量插入。
  - where：自动处理 WHERE 关键字和多余的 AND。
  - set：自动处理 UPDATE 中的 SET 和多余的逗号。
  - trim：自定义前后缀。
  - 批量插入示例：
    <insert id="batchInsert">
    INSERT INTO user (name, age) VALUES
    <foreach collection="list" item="item" separator=",">
    (#{item.name}, #{item.age})
    </foreach>
    </insert>
- 追问：
  - @Mapper：标注在单个 Mapper 接口上，MyBatis 扫描时识别。
  - @MapperScan：标注在启动类上，指定包路径，批量扫描该包下所有 Mapper 接口。
  - 推荐 @MapperScan：避免在每个接口上重复写 @Mapper，且能统一配置。
</details>

**我的初答**：
**错漏点**：

---

## Day 13 (2026-10-07) —— 数据封装、前后端联调与 Nginx 反向代理

### 题目1：统一响应封装与全局异常处理在前后端联调中的作用
> 在前后端分离项目中，后端通常需要返回统一的响应格式（如 {code, message, data}）。请回答：
> 1. 为什么要做统一响应封装？如果不封装，前端联调时会遇到哪些问题？请从状态码、错误信息、数据格式三个角度说明。
> 2. 如何设计一个通用的 Result<T> 类？请写出核心字段和静态工厂方法（success、error）。如何结合 @RestControllerAdvice + @ExceptionHandler 实现全局异常处理？
> 3. 在全局异常处理中，如何区分业务异常（如余额不足）和系统异常（如 NullPointerException）？如何避免将异常堆栈直接返回给前端？
     > 追问：HTTP 状态码和业务状态码（如 code=500）应该同时使用吗？如果同时使用，前端应该优先判断哪个？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目2：前后端联调中的跨域问题及 CORS 与 Nginx 反向代理的解决方案
> 在前后端分离开发中，前端运行在 http://localhost:8080，后端运行在 http://localhost:8081，浏览器会因同源策略阻止请求。请回答：
> 1. 什么是同源策略？跨域请求在什么情况下会被浏览器阻止？简单请求和非简单请求在跨域处理上有何区别（预检请求 OPTIONS）？
> 2. Spring Boot 中如何配置 CORS？请写出两种方式（@CrossOrigin 注解和全局 WebMvcConfigurer 配置）。allowedOrigins、allowedMethods、allowCredentials 分别如何设置？
> 3. 为什么生产环境更推荐使用 Nginx 反向代理解决跨域？请描述 Nginx 配置：前端请求 /api 转发到后端服务，前后端同源部署。
     > 追问：allowCredentials = true 时，allowedOrigins 为什么不能设置为 *？如果必须允许多个域名携带 Cookie，应如何配置？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目3：Nginx 反向代理、负载均衡与动静分离在 Java 后端部署中的应用
> 在 Java 微服务部署中，Nginx 通常作为入口网关。请回答：
> 1. 正向代理与反向代理的区别是什么？Nginx 作为反向代理的核心作用有哪些（负载均衡、SSL 终止、静态资源服务、限流等）？
> 2. Nginx 支持哪些负载均衡策略（轮询、权重、IP Hash、最少连接、fair）？请写出 upstream 配置示例，并说明 proxy_pass 末尾带 / 与不带 / 的区别。
> 3. 什么是动静分离？如何配置 Nginx 让静态资源（HTML、CSS、JS、图片）由 Nginx 直接返回，动态请求（/api）转发给 Tomcat？这样做有什么好处？
     > 追问：Nginx 的 proxy_set_header 常用配置有哪些？为什么后端需要获取真实客户端 IP 时，必须设置 X-Real-IP 和 X-Forwarded-For？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：

---

## Day 14 (2026-10-08) —— 接收前端参数与 Logback/SLF4J 基础

### 题目1：Spring MVC 参数接收的完整体系与常见坑
> 在 Spring Boot Web 项目中，接收前端参数是最基础也最容易踩坑的环节。请回答：
> 1. 请列出接收前端参数的常用注解及其适用场景：@RequestParam、@PathVariable、@RequestBody、@RequestHeader、@CookieValue、@ModelAttribute。它们分别对应 HTTP 请求的哪一部分？
> 2. 当 @RequestParam 标注的参数未传且 required 默认 true 时，会抛什么异常？如何优雅处理（如设置默认值、全局异常处理）？若前端传参类型与后端不一致（如字符串传给了 Integer），会发生什么？
> 3. 接收日期类型参数时，@DateTimeFormat 与 @JsonFormat 分别适用于什么场景？时区问题如何解决？接收数组/集合参数（如 ?ids=1,2,3 和 ?ids=1&ids=2）时如何配置？
     > 追问：@RequestBody 和 @RequestParam 可以混用吗？如果请求体是 JSON，同时 URL 上又带查询参数，如何同时接收？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目2：SLF4J 门面模式与 Logback 的关系及日志配置详解
> 在 Java 后端项目中，SLF4J + Logback 是最常用的日志组合。请回答：
> 1. SLF4J 是什么？它和 Logback 是什么关系？为什么说 SLF4J 是“门面模式”？如果项目中同时引入了 Log4j、Logback 和 JUL，SLF4J 如何决定绑定哪一个？
> 2. Logback 的核心组件有哪些（Logger、Appender、Layout/Encoder）？logback-spring.xml 与 logback.xml 有什么区别？为什么 Spring Boot 推荐使用 logback-spring.xml？
> 3. 如何配置多环境日志（dev 输出到控制台，prod 输出到文件并按天滚动）？请说明 springProfile 标签和 RollingFileAppender 的核心配置项（fileNamePattern、maxHistory、totalSizeCap）。
     > 追问：日志级别 DEBUG、INFO、WARN、ERROR 的优先级是什么？一个 logger 的级别设置为 INFO，那么 DEBUG 日志会被输出吗？父子 logger 的级别继承规则是怎样的？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目3：日志占位符、MDC 与异步日志在 Spring Boot 项目中的实践
> 在 Spring Boot 项目中，日志不仅是调试工具，更是线上排查的核心手段。请回答：
> 1. 为什么推荐使用 log.info("user: {}", user) 而不是 log.info("user: " + user)？前者在性能上有什么优势？如果 user 为 null，两种写法分别会怎样？
> 2. MDC（Mapped Diagnostic Context）是什么？底层基于什么实现？如何用 MDC 在日志中打印 TraceId 实现链路追踪？在线程池和异步日志场景下，MDC 为什么会丢失？如何解决？
> 3. Logback 的 AsyncAppender 如何配置？异步日志的原理是什么？它可能带来哪些问题（如日志丢失、队列阻塞）？discardingThreshold 和 queueSize 参数如何调优？
     > 追问：在 Spring Boot 中如何动态修改日志级别（不重启应用）？actuator 的 loggers 端点如何实现这一点？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：

---

## Day 15 (2026-10-09) —— 分页查询、PageHelper 与动态 SQL

### 题目1：物理分页与逻辑分页的区别，以及 PageHelper 的底层实现原理
> 在 Java 后端开发中，分页查询是必备技能。请回答：
> 1. 什么是物理分页？什么是逻辑分页？两者在 SQL 执行、内存占用和性能上有何本质区别？为什么大数据量下必须使用物理分页？
> 2. PageHelper 是如何实现物理分页的？它的核心拦截器（PageInterceptor）在 MyBatis 的哪个执行阶段介入？请描述 PageHelper.startPage() 之后，MyBatis 执行 SQL 时经历了哪些步骤（如 COUNT 查询、LIMIT 拼接、ThreadLocal 清理）。
> 3. 使用 PageHelper 时，为什么 startPage() 必须紧跟在查询方法之前？如果中间插入了其他数据库操作，会发生什么？PageHelper 在分页查询后如果不调用 PageInfo 或 clearPage()，可能带来什么问题？
     > 追问：PageHelper 的 count 查询在什么情况下会被优化或跳过？如何配置 `pagehelper.supportMethodsArguments` 和 `reasonable` 参数？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目2：MyBatis 动态 SQL 的核心标签及在复杂查询中的应用
> 动态 SQL 是 MyBatis 的强项，用于根据条件拼接 SQL。请回答：
> 1. `<if>`、`<choose>/<when>/<otherwise>`、`<where>`、`<set>`、`<trim>` 分别解决什么问题？`<where>` 和 `<set>` 底层是如何处理多余的 AND、OR 或逗号的？
> 2. `<foreach>` 的 collection、item、index、open、close、separator 属性分别是什么含义？请分别写出用 `<foreach>` 实现 IN 查询和批量插入的示例片段。
> 3. 在动态 SQL 中，`#{}` 和 `${}` 的使用场景有何不同？动态表名、动态排序字段（ORDER BY）为什么必须用 `${}`？如何防止由此带来的 SQL 注入风险？
     > 追问：MyBatis 的 `<sql>` 和 `<include>` 标签如何实现 SQL 片段复用？`<bind>` 标签有什么作用？请举例说明。

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：


### 题目3：分页查询与动态 SQL 在前后端联调中的接口设计（综合实战）
> 假设你正在开发一个订单列表查询接口，前端传入以下参数：页码 pageNum、每页条数 pageSize、订单状态 status（可选）、下单时间范围 startTime/endTime（可选）、用户ID userId（可选）。请回答：
> 1. 请设计后端 Controller 接收参数的 DTO 类，并说明如何用 @RequestParam 或 @ModelAttribute 接收这些参数。
> 2. 请写出对应的 MyBatis Mapper XML 中的动态 SQL 查询片段（包含 WHERE 条件、分页由 PageHelper 处理），要求支持按创建时间降序排列。
> 3. 返回给前端的统一分页结果应该包含哪些字段（如 total、list、pageNum、pageSize、pages）？如何使用 PageInfo 封装？如果前端需要“上一页/下一页”的页码，PageInfo 中哪些属性可以直接使用？
     > 追问：当查询条件很多时，如何避免 Mapper 接口方法参数过多？@Param 注解和 Map 传参各有什么优缺点？

<details>
<summary><strong>点击展开标准解析</strong></summary>

（此处留空，自行补充）

</details>

**我的初答**：
**错漏点**：

---