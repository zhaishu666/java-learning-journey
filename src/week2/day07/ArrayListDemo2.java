package week2.day07;

import java.util.ArrayList;

//定义一个集合,添加字符串,并进行遍历,格式为[元素1,元素2,元素3]
public class ArrayListDemo2 {
    static void main() {
        ArrayList<String> list = new ArrayList<>();
       // ArrayList<Integer> list2 = new ArrayList<>(); //实用int的包装类就可以向集合中放基本数据类型
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        for (int i = 0; i < list.size(); i++) {
            if(i == 0){
                System.out.print("[" + list.get(i) + ", ");
            } else if (i == list.size() -1) {
                System.out.print(list.get(i) + "]");
            }else{
                System.out.print(list.get(i) + ", ");
            }
        }
        System.out.println();
    }
}
