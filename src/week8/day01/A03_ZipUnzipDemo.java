package week8.day01;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class A03_ZipUnzipDemo {
    // 重点: File.separator 是静态字符串常量,会根据当前的操作系统,返回对应的间隔符
    //System.getProperty("user.home") 的作用是可跨平台获得用户主目录
    static void main() throws IOException {
        String zipFilePath = "C:"+ File.separator +"Users"+File.separator+"翟曙"+File.separator+"Desktop"+File.separator+"新建文件夹.zip";
        String destDirPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "解压结果";
        unzip(zipFilePath,destDirPath);
    }

    public static void unzip(String zipFilePath, String destDirPath) throws IOException {
        File destDir = new File(destDirPath);  //预先创建解压目标根目录,确保目录存在
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath), StandardCharsets.UTF_8)){ //显示指定UTF-8编码
            ZipEntry ze;  //压缩包的每个对象都是ZipEntry类型
            while((ze = zis.getNextEntry()) != null){
                String fileName = ze.getName();
                File destFile = new File(destDirPath, fileName);
                if(ze.isDirectory()){
                    destFile.mkdirs();
                }else{
                    //条目是文件: 先确保父目录存在(处理zip内部嵌套目录的场景)
                    destFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(destFile)){
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = zis.read(buffer)) != -1){
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }
}
