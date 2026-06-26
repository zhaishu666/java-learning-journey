package week2.day05;

//练习String.replace(旧值,新值)
public class StringDemo6 {
    static void main() {
        String talk = "你真是个傻逼,TMD";
        String[] badtalk = {"TMD","SB","傻逼","CNM"};
        for (int i = 0; i < badtalk.length; i++) {
            talk = talk.replace(badtalk[i], "***");
        }
        System.out.println(talk);
    }
}
