package week3.day06;

public class AAbstractTest {
    static void main() {
        //Person p = new Person();  //报错,因为抽象类不能创建对象
        AStudent s = new AStudent("zhangsan",18);
        APerson p = new AStudent("lisi",20);
        s.work();
        s.print();
    }
}
