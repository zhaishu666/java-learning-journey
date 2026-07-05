package week3.day05.day05p1;

public class Person {
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }

   public void keepPet(Animal a ,String something){
       System.out.print("年龄为"+ this.age + "的" + this.name + "养了一只" +
               a.getColor() + "的" + a.getAge() +"岁的" + a.getName()+ "\n");
       if(a instanceof Dog d){
           d.eat(something);
       }else if(a instanceof Cat c){
           c.eat(something);
       }
   }
}
