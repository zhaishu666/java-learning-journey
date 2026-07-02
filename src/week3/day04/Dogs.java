package week3.day04;

//实际开发中一个java文件中只写一个类
public class Dogs {
    public void eats(){
        System.out.println("吃狗粮");
    }
    public void drink(){
        System.out.println("狗在喝水");
    }
    public void lookHouse(){
        System.out.println("狗在看家");
    }
}

class Husky extends Dogs{
    @Override
    public void eats(){
        super.eats();
        System.out.println("啃沙发");
    }
    public void breakHouse(){
        System.out.println("哈士奇正在拆家");
    }
}

class SharPei extends Dogs{
    @Override
    public void eats(){
        System.out.println("吃狗粮,吃骨头");
    }
}

class chineseDogs extends Dogs{
    @Override
    public void eats(){
        System.out.println("吃剩饭");
    }
}