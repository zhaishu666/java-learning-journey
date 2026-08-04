package week8.day02;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A03_IOPractice1 {
    public static void main(String[] args) throws IOException {
        /*
        *   获取姓氏 https://hanyu.baidu.com/shici/detail?pid=0b2f26d4c0ddb3ee693fdb1137ee1b0d
        *   获取男生名字...
        *   获取女生名字
        * */

        //定义变量记录网址
        String familyNameNet = "https://hanyu.baidu.com/shici/detail?pid=0b2f26d4c0ddb3ee693fdb1137ee1b0d";
        String boyNameNet = "...";
        String girlNameNet = "...";

        String familyNameStr = webCrawler(familyNameNet);
        ArrayList<String> familyNameTempList = getData(familyNameStr, "([\\u4e00-\\u9fff]{4})(，|。)",1);
        System.out.println(familyNameTempList);
    }

    private static ArrayList<String> getData(String NameStr, String regex, int index){
        ArrayList<String> list = new ArrayList<>();
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(NameStr);
        while (matcher.find()) {
            list.add(matcher.group(index));
        }
        return list;
    }

    /*
     * 作用:爬取网站上的所有姓氏,拼接成字符串返回
     * 形参:
     *     网络地址
     * 返回值:
     *     拼接后的姓氏
     *
     * */
    public static String webCrawler(String net) throws IOException {
        StringBuilder sb = new StringBuilder();
        //创建一个URL对象
        URL url = new URL(net);
        //让URL对象能狗连接该网站  注意:必须保持网络通畅
        URLConnection urlConnection = url.openConnection();
        //通过流获取里面的内容
        try( InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream())){
            int ch;
            while((ch=isr.read())!=-1){
                sb.append((char) ch);
            }
        }
        return sb.toString();
    }
}
