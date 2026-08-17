package week10.day01;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("计算类测试")
public class A02_JunitTest1 {
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp(){
        calculator = new Calculator();
    }

    @Test
    @DisplayName("加法测试")
    void testAdd(){
        int result = calculator.add(10, 20);
        assertEquals(30, result);
    }

    @Test
    @DisplayName("除法测试,正常情况")
    void testSubtract(){
     int result = calculator.divide(2,1);
     assertEquals(2, result);
    }

    @Test
    @DisplayName("除法测试,非正常情况")
    void testSubtract2(){
        assertThrows(ArithmeticException.class, () -> calculator.divide(2,0));
    }

    @Disabled("该测试暂时跳过")
    @Test
    void skip(){
        //不会被执行
    }
}
