package week7.day04;

import java.io.FileOutputStream;
import java.io.IOException;

public class A02_IODemo3 {
    public static void main(String[] args) throws IOException {
        //测试换行与续写,以及怎样将字符串的内容进行输出,而不是每次都要写对应的ASCII码值
        //如果想要续写,不刷新文件,在 new对象的第二个参数写true就行,这是续写开关,默认是false
        FileOutputStream f1 = new FileOutputStream("C:\\Users\\翟曙\\Desktop\\测试.txt",true); //打开续写开关

        String str1 = "zhong guo ren neng fei";
        byte[] bytes = str1.getBytes(); //getBytes直接获取对应的字节数组
        f1.write(bytes);

        String str2 = "\r\n";   //想要换行,就打印对应的换行符的byte数组就行,不同操作系统的换行符不同
        f1.write(str2.getBytes());

        String str3 = "zhai shu shi zui shuai de";
        f1.write(str3.getBytes());
    }
}
