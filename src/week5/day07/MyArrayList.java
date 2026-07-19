package week5.day07;

import java.util.Arrays;

public class MyArrayList <E>{

    Object[] obj = new Object[10];
    int size = 0;

    public boolean add(E e){
        obj[size] = e;
        size++;
        return true;
    }

    public E get(int index){
        return (E)obj[index];
    }

    public E set(int index, E e){
        if(obj[index] == null){
            obj[index] = e;
            return null;
        }
        Object old = obj[index];
        obj[index] = e;
        return (E)old;
    }

    @Override
    public String toString() {
        return Arrays.toString(obj);
    }
}
