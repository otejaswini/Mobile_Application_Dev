package INC1;
/*
 * - Assignment# In class Assignment 01
 * - MainPart4.java
 * - Tejaswini Oduru
 * */
import java.util.HashMap;
import java.util.Map;
public class MainPart4{

    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        for(String str: Data.shoppingCart){
            String[] list = str.split(",");
            int ID = Integer.parseInt(list[0]);
            int Quantity = Integer.parseInt(list[1]);
            map.put(ID, Quantity);
        }
        int total =0;
        System.out.println("ID    Name           Quantity     Price*Quantity");
        for(String str: Data.items){
            String[] item = str.split(",");
            String name = item[0];
            int ID = Integer.parseInt(item[1]);
            int price = Integer.parseInt(item[2]);
            if(map.containsKey(ID)){
                int item_price = map.get(ID) * price;
                System.out.println(ID+ "   " + name + "       " + map.get(ID) + "         " + item_price);
                total += item_price;
            }
            else
                continue;
        }
        System.out.println("\nTotal Bill: $" +total);
    }
}