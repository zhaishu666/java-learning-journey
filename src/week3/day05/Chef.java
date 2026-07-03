package week3.day05;

public class Chef extends Worker{


    public Chef() {
    }

    public Chef(String id, String name, String salary) {
       super(id,name,salary);
    }

    @Override
    public void work(){
        super.work();
        System.out.println("炒菜");
    }

    public String toString() {
        return "Chef{}";
    }
}
