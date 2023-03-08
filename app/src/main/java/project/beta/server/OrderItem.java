package project.beta.server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * OrderItem is a class that represents an item in the order.
 * 
 * @author Joshua Downey, Matthew Gimlin
 */
public class OrderItem {
    /**
     * Enum for the different types of order items.
     */
    public enum OrderItemType {
        /**
         * Represents a bowl item.
         */
        BOWL,
        /**
         * Represents a plate item.
         */
        PLATE,
        /**
         * Represents a bigger plate item.
         */
        BIGGER_PLATE,
        /**
         * Represents an a la carte item.
         */
        A_LA_CARTE
    }

    ArrayList<String> menuItems;
    int amount;
    OrderItemType type;

    /**
     * Constructs new OrderItem object.
     * 
     * @param menuItems menu items in the order
     * @param amount    the quantity of the order
     * @param type      the type of the order
     */
    public OrderItem(String[] menuItems, int amount, OrderItemType type) {
        this.menuItems = new ArrayList<String>(Arrays.asList(menuItems));
        this.amount = amount;
        this.type = type;
    }
}
