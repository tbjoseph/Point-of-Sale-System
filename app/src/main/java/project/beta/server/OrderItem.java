package project.beta.server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * OrderItem is a class that represents an item in the order.
 * 
 * @author Joshua Downey, Matthew Gimlin
 */
public class OrderItem {
    public enum OrderItemType {
        BOWL, PLATE, BIGGER_PLATE, A_LA_CARTE
    }

    ArrayList<String> menuItems;
    int amount;
    OrderItemType type;

    public OrderItem(String[] menuItems, int amount, OrderItemType type) {
        this.menuItems = new ArrayList<String>(Arrays.asList(menuItems));
        this.amount = amount;
        this.type = type;
    }
}
