package project.beta.types;

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

    /**
     * The menu items in the order.
     */
    public ArrayList<MenuItem> menuItems;
    /**
     * The quantity of the order.
     */
    public int amount;
    /**
     * The type of the order.
     */
    public OrderItemType type;

    /**
     * Constructs new OrderItem object.
     * 
     * @param menuItems menu items in the order
     * @param amount    the quantity of the order
     * @param type      the type of the order
     */
    public OrderItem(MenuItem[] menuItems, int amount, OrderItemType type) {
        this.menuItems = new ArrayList<MenuItem>(Arrays.asList(menuItems));
        this.amount = amount;
        this.type = type;
    }
}
