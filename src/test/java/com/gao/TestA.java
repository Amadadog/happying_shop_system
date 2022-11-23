package com.gao;

import com.gao.happying_shop_system.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: GaoWenQiang
 * @Date: 2022/10/07 16:57
 * @Description:
 */
public class TestA {
    public static void main(String[] args) {
        ArrayList<Annimal> list1 = new ArrayList<>();
        ArrayList<Dog> list2 = new ArrayList<>();
        ArrayList<Cat> list3 = new ArrayList<>();
        ArrayList<Son> list4 = new ArrayList<>();
        method(list1);
        method(list2);
        method(list3);
        Integer integer = new Integer(123);
        Integer integer2 = Integer.valueOf("123");
        System.out.println(integer2);

    }
    public static  void method(ArrayList<?extends Annimal> list){

    }
}

class Annimal {


}
class Dog extends Annimal {

}
class Cat extends Annimal {

}
class Son {

}

