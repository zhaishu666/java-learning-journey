package week2.day06;

//给定一串字符串获得最后一个英文单词的长度
public class GetLastWord {
    static void main() {
        String str = "let's go brother";
        int count = 0;
        for (int i = str.length() -1; i >+ 0; i--) {
            char ch = str.charAt(i);
            if(ch == ' '){  //字符之间的比较是直接比较Unicode编码值,如: 'A' Unicode: 65
                break;
            }else{
                count++;
            }
        }
        System.out.println(count);
    }
}
