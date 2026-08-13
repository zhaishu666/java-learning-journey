package week9.day04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ProxyUnit {
    /*
    *  方法的作用:
    *         给BigStar创建一个代理
    *  形参:
    *      要代理的对象
    *  返回值:
    *      给明星创建的代理
    *
    * */

    public static Star createProxy(BigStar bigStar){

        //为什么能强转成Star类型?因为new Class[]{Star.class}传入了Star接口,生成的代理类实现了该接口,可以向上强转
        Star proxy = (Star) Proxy.newProxyInstance(ProxyUnit.class.getClassLoader(),
                new Class[]{Star.class},
                new InvocationHandler()
                /*// 参数1：类加载器  负责把动态生成的代理类加载到内存中
                  // 参数2：代理要实现的接口   规定代理类必须实现哪些接口
                  // 参数3：调用处理器（核心逻辑）  代理对象的方法调用处理器，所有接口方法调用都会走到这里
                */

                {
                    @Override
                    //代理对象调用任何接口方法时，都会触发这个 invoke 方法，而不是直接调用真实对象的方法
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        /*
                         *  参数一: 当前生成的代理对象本身（几乎不用，因为调用它的方法会再次触发 invoke，造成死循环）
                         *  参数二: 当前被调用的方法的反射对象（比如调用 sing，这里就是 sing 方法的反射实例）
                         *  参数三: 调用方法时传入的参数数组
                         * */
                        if ("sing".equals(method.getName())) {
                            System.out.println("准备话筒,收钱");
                        } else if ("dance".equals(method.getName())) {
                            System.out.println("准备场地,收钱");
                        }

                        return method.invoke(bigStar, args);
                    }
                });
        return proxy;
    }
}
