package week8.day02;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class A01_PraCommons {
    static void main() throws IOException {

        File src = new File("C:/Users/翟曙/Desktop/新建文件夹");
        File dest = new File("C:\\Users\\翟曙\\Desktop\\拷贝新建文件夹");

        //commons-io的FileUtils工具类
        //FileUtils.copyDirectory(src,dest);

        //FileUtils.copyDirectoryToDirectory(src, dest);
        //FileUtils.deleteDirectory(dest);
        //FileUtils.cleanDirectory(dest);
    }
}
