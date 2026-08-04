package week8.day02;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class A02_myHutool {
    public static void main(String[] args) {
        //使用hutool包里的FileUtil类进行测试
        File file1 = FileUtil.file("C:/Users/翟曙/Desktop/测试2.txt"); //根据参数创建file对象
        System.out.println(file1);
        FileUtil.touch(file1);   //根据File对象创建文件

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list,"aaa","bbb","ccc","ddd");
        FileUtil.writeLines(list,file1, Charset.defaultCharset());  //将集合的元素添加到文件中,覆盖模式

        FileUtil.appendLines(list,file1, Charset.defaultCharset()); //将集合的元素添加到文件中,续写模式

        List<String> list1 = FileUtil.readLines(file1, StandardCharsets.UTF_8);
        System.out.println(list1);
    }
}
