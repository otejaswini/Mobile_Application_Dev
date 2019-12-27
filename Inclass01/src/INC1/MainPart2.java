package INC1;
/*
 * - Assignment# In class Assignment 01
 * - MainPart2.java
 * - Tejaswini Oduru
 * */
import java.util.*;

public class MainPart2 {

    public static void main(String[] args) {
        Map<String, Integer> peopleAndCount = new HashMap<>();
        // Build hash table with count
        Set<Map.Entry<String, Integer>> entrySet = peopleAndCount.entrySet();
        // To access the Data.users array and count the number of people in each state
        for (String str : Data.users) {
            String[] sp = str.split(",");
            Integer count = peopleAndCount.get(sp[6]);
            if (count == null) {
                peopleAndCount.put(sp[6], 1);
            } else {
                peopleAndCount.put(sp[6], ++count);
            }
        }
        // Sort the list in ascending order by count
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(peopleAndCount.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        // Print the number of people living in each state by using hashmap
        HashMap<String, Integer> entry1 = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            entry1.put(aa.getKey(), aa.getValue());
            System.out.println("Number of people living in " + aa.getKey() + " are " + aa.getValue());
        }
    }
}
