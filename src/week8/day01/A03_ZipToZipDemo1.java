package week8.day01;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class A03_ZipToZipDemo1 {
    public static void main(String[] args) throws IOException {
        String src = "E:\\develpment\\base-code\\java-learning-journey\\a.txt";
        String dest = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "a.zip";
        toZip(src, dest);
    }

    public static void toZip(String src, String dest) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
        ZipEntry ze = new ZipEntry("a.txt");
        byte[] buf = new byte[1024];
        zos.putNextEntry(ze);  //开启一个压缩条目,后续所有的数据都归属该条目
        FileInputStream fis = new FileInputStream(src);
        int len;
        while ((len = fis.read(buf)) != -1) {
            zos.write(buf, 0, len);
        }
        fis.close();
        zos.closeEntry();
        zos.close();
    }
}
