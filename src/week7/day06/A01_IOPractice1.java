package week7.day06;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class A01_IOPractice1 {
    static void main() throws IOException {
        //拷贝一个文件夹,需要考虑子文件夹
        File src = new File("C:\\Users\\翟曙\\Desktop\\新建文件夹");
        File dest = new File("C:\\Users\\翟曙\\Desktop\\拷贝新建文件夹");
        copyFile(src,dest);
    }

    public static void copyFile(File src, File dest) throws IOException {
        if (!src.exists()) {   //防御判断,源不存在直接退出
            return;
        }
        if (src.isDirectory()) {  //如果是文件夹
            dest.mkdirs();    //dest创建多级文件夹
            File[] files = src.listFiles();  //获得src路径下的所有内容,放到File类型的数组中
            if (files == null) {
                throw new RuntimeException("权限不足,无法复制" + src);
            }
            for (File file : files) {
                copyFile(file, new File(dest, file.getName()));
            }
        }else {
            try (FileInputStream fis = new FileInputStream(src);
                 FileOutputStream fos = new FileOutputStream(dest)) {
                byte[] buffer = new byte[1024 * 1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        }
    }
}
