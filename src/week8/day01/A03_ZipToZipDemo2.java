package week8.day01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class A03_ZipToZipDemo2 {
    static void main() throws IOException {
        //将一整个文件夹进行压缩
        String sourceFolderPath = "C:\\Users\\翟曙\\Desktop\\新建文件夹";
        String zipOutPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "zip文件夹拷贝.zip";
        zipFolder(sourceFolderPath, zipOutPath);
    }

    public static void zipFolder(String sourceFolderPath, String zipOutPath) throws IOException {
        File file = new File(sourceFolderPath);
        //参数校验:源必须是文件夹,而且必须存在
        if (!file.exists()) {
            throw new IOException("源文件夹不存在" + sourceFolderPath);
        }
        if (!file.isDirectory()) {
            throw new IOException("传入的不是文件夹" + sourceFolderPath);
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipOutPath))) {
            addFileToZip(zos, file, file.getName());
        }
    }

    private static void addFileToZip(ZipOutputStream zos, File file, String entryPath) throws IOException {
        if (file.isDirectory()) {
            //如果是文件夹,zip条目规范,结尾必须加上/
            ZipEntry zipEntry = new ZipEntry(entryPath + "/");
            zos.putNextEntry(zipEntry);  //开启一个压缩条目,后续所有添加的数据归属该条目
            zos.closeEntry();

            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    // 拼接zip内部相对路径，递归处理子文件、子文件夹
                    addFileToZip(zos, childFile, entryPath + "/" + childFile.getName());
                }
            }
        } else {
            //如果是普通文件,就创建一个新的条目
            ZipEntry entry = new ZipEntry(entryPath);
            zos.putNextEntry(entry);

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
        }
    }
}
