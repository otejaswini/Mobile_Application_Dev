package INC1;
/*
 * - Assignment# In class Assignment 01
 * - MainPart3.java
 * - Tejaswini Oduru
 * */
import java.util.*;

public class MainPart3 {

    public static Comparator<String> stateComparator = new Comparator<String>() {
        public int compare(String u1, String  u2) {
            String[] str1 = u1.split(",");
            String[] str2 = u2.split(",");
            String c1 = str1[6];
            String c2 = str2[6];
            return c2.compareTo(c1);
        }};
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        Collections.addAll(set, Data.users);
        List<String> list = new ArrayList<>();
        //Compare and add the common users to the list
        for(String s: Data.otherUsers){
            if(set.contains(s))
                list.add(s);
        }
       // Print the list of users which exist in both Data.users and Data.otherUsers.
        Collections.sort(list, stateComparator);
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i).toString());
        }
    }
    }

