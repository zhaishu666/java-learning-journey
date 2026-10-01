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
**错漏点**：


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