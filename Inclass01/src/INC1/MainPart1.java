package INC1;
/*
 * - Assignment# In class Assignment 01
 * - MainPart1.java
 * - Tejaswini Oduru
 * */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainPart1 {

    public static void main(String[] args) {

        //To access the Data.users array.
        ArrayList<user> list = new ArrayList<>();
        for (String str : Data.users) {
            String[] subp = str.split(",");
            list.add(new user(subp[0], subp[1], Integer.parseInt(subp[2]), subp[3], subp[4], subp[5], subp[6]));
        }
        //To sort the list according to the age
    Collections.sort(list, new Comparator<user>() {
        @Override
        public int compare(user t2, user t1) {
            return t1.age-t2.age;
        }

    });
        // Print the top 10 oldest people
    for(int i=0;i<10;i++)
    {
        System.out.println("Data "+list.get(i).toString());
    }
    }
}
