import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {

        ContactManager manager = new ContactManager();
        Scanner sc = new Scanner(System.in);
        manager.loadFromFile("src/Contactmsg.txt");

        while (true) {

            System.out.println("===================欢迎来到通讯管理系统======================");
            System.out.println("添加通讯录信息,请输入1");
            System.out.println("删除通讯录信息,请输入2");
            System.out.println("修改通讯录信息,请输入3");
            System.out.println("获取通讯录所有信息,请输入4");
            System.out.println("查询通讯人信息,请输入5");
            System.out.println("退出,请输入6");


            if (sc.hasNextInt()) {
                int choice  = sc.nextInt();
                switch (choice) {
                    case 1:
                        sc.nextLine();
                        System.out.println("请输入要添加的人的姓名");
                        String inputName = sc.nextLine();
                        if(!manager.checkName(inputName)){
                            break;
                        }
                        System.out.println("请输入要添加的人的电话号码");
                        String inputPhone = sc.nextLine();
                        if (!manager.checkPhone(inputPhone)) {
                            System.out.println("电话号码格式不正确!请重新输入");
                            break;
                        }
                        System.out.println("请输入要添加的人的邮箱");
                        String inputEmail = sc.nextLine();
                        System.out.println("请输入备注");
                        String inputRemark = sc.nextLine();

                        Contact c = new Contact(inputName, inputPhone, inputEmail, inputRemark);
                        manager.addContact(c);
                        break;
                    case 2:
                        sc.nextLine();
                        System.out.println("请输入要删除联系人的姓名");
                        boolean deleResult = manager.deleteContact(sc.nextLine());
                        System.out.println(deleResult);
                        break;
                    case 3:
                        sc.nextLine();
                        System.out.println("请输入要修改的人的姓名");
                        String changeName = sc.nextLine();
                        if(!manager.checkName(changeName)){
                            break;
                        }
                        System.out.println("请输入要修改的人的电话号码");
                        String changePhone = sc.nextLine();
                        if (!manager.checkPhone(changePhone)) {
                            System.out.println("电话号码格式不正确!请重新输入");
                            break;
                        }
                        System.out.println("请输入要修改的人的邮箱");
                        String changeEmail = sc.nextLine();
                        System.out.println("请输入修改后的备注");
                        String changeRemark = sc.nextLine();

                        boolean updateResult = manager.updateContact(changeName, new Contact(changeName, changePhone, changeEmail, changeRemark));
                        System.out.println(updateResult);
                        break;
                    case 4:
                        sc.nextLine();
                        List<Contact> all = manager.getAll();
                        for (Contact c1 : all) {
                            System.out.println(c1);
                        }
                        break;
                    case 5:
                        sc.nextLine();
                        System.out.println("请输入要查询的人的姓名");
                        Contact searchedContact = manager.searchByName(sc.nextLine());
                        if(searchedContact != null){
                            System.out.println(searchedContact);
                        }else {
                            System.out.println("未找到联系人");
                        }
                        break;
                    case 6:
                        manager.saveToFile("src/Contactmsg.txt");
                        return;
                    default:
                        System.out.println("不存在这个选项");
                }
            } else {
                System.out.println("非法输入");
                sc.nextLine();  //消费掉非数字输入,否则会死循环
            }
        }
    }
}
