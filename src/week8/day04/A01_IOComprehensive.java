package week8.day04;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class A01_IOComprehensive {
    /*
     * 目的:
     *     遍历zip文件中的所有条目,读取文本内容,统计所有单词出现的次数
     *     并输出Top10的高频词
     * 重点: ZipInputStream是扁平化迭代,所有条目一次性枚举,不用写递归函数
     * */
    static void main() throws IOException {
        TreeMap<String, Integer> countMap = new TreeMap<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream("C:\\Users\\翟曙\\Desktop\\IOComprehensive.zip"))) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                try {
                    if (!ze.isDirectory() && ze.getName().endsWith(".txt")) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(zis, StandardCharsets.UTF_8));
                            //这里需要用到转换流将ZipInputStream包装成缓冲流
                            //不要用try-with-resources包裹这里的br,否则br.close()会级联关闭zis
                            List<String> lines = br.lines().toList();
                            lines.stream()
                                    .flatMap(line -> Arrays.stream(line.split("[\\s\\p{Punct}]+")))
                                    // \\s表示空白\\p{Punct}表示所有的标点符号
                                    .filter(word -> !word.isEmpty())
                                    .forEach(word -> countMap.merge(word.toLowerCase(), 1, Integer::sum));
                            //Map的方法merge,对于这个word,没有存在就设置value为1,存在了就通过Integer的sum方法+1
                        }
                } finally {
                    zis.closeEntry();
                }
            }
        }
        countMap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .limit(10)
                .forEach(entry -> {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                });
    }
    /*
     * 遇到的一些错误总结:
     * 1(重大问题). 当用try-with-resources时,try块结束时会自动调用br.close(),但装饰器模式流的特性使得外层的br关闭后,内层包装的zis也一同关闭
     * 这就使得代码在执行finally块时zis已经关闭了,抛出IOException,信息为Stream closed
     * 2. 忘记zis.closeEntry(): 每获取一个ZipEntry处理完成,必须调用; 不调用,再次getNextEntry报Stream closed
     * BufferedReader/try-with-resources关闭br不会自动调用closeEntry()
     * 3. TreeMap完全没必要,因为它是按键排序的,最后还是依靠stream.sorted进行排序
     * 4. 忘记写toLowerCase()导致区分大小写
     * */
}



