package week10.day01;

public class A01_loader {
    public static void main(String[] args) {

        ClassLoader appLoader = ClassLoader.getSystemClassLoader();
        ClassLoader platLoader = appLoader.getParent();
        ClassLoader BootLoader = platLoader.getParent();

        System.out.println("应用程序类加载器: " + appLoader);
        System.out.println("平台类加载器: " + platLoader);
        System.out.println("启动类加载器: " + BootLoader);
    }
}
