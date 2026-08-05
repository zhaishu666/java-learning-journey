package week8.day03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WeightedPicker {
    /*
     * 带权重的随机点名期:
     *      每次点名过后,该学生的权重变为原来的一半
     * */
    private static final ArrayList<Student> studentList = new ArrayList<>();
    private static final Random random = new Random();

    static void main() throws IOException {
        loadStudentFormFile("student.txt");

        if (studentList.isEmpty()) {
            System.out.println("没有读取到学生,程序已退出");
            return;
        }
        for (int i = 0; i < 5; i++) {
            Student thisStu = pickByWeight();
            System.out.println(thisStu);
            double oldWeight = thisStu.getWeight();
            thisStu.setWeight(oldWeight / 2);   //每次点完名,将该学生权重除以二
        }
    }

    /*
     * 从txt文件读取学生信息,存入studentList
     *  @param filepath 文件路径
     * */
    private static void loadStudentFormFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  //跳过空行

                String[] data = line.split("-");

                if (data.length != 4) {
                    System.out.println("格式错误" + line + "已跳过");
                    continue;
                }
                String name = data[0].trim();
                String gender = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                double weight = Double.parseDouble(data[3].trim());
                studentList.add(new Student(name, gender, age, weight));
            }
        } catch (IOException e) {
            System.out.println("读取文件失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * 加权随机抽取一名学生
     * 核心算法:
     * 1. 计算出所有学生的权重总和
     * 2. 生成一个[0, 权重总和)的随机数
     * 3. 点到某个学生后,将这个学生的权重减半
     * */
    private static Student pickByWeight() {
        //计算总权重
        int totalWeight = 0;
        for (Student stu : studentList) {
            totalWeight += stu.getWeight();
        }
        //获得随机数
        double randomWeight = random.nextDouble(0, totalWeight);

        //遍历加权,看权重落在哪个区间
        double currentWeight = 0;
        for (Student stu : studentList) {
            currentWeight += stu.getWeight();
            if (randomWeight < currentWeight) {
                return stu;
            }
        }
        return null;
    }
}
