package project.beta.types;

import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

/**
 * OrderView is a class that displays the current order to the view box.
 * 
 * @author Joshua Downey, Matthew Gimlin
 */
public class OrderView {
    private ArrayList<OrderItem> order;

    /**
     * Constructs a new OrderView object.
     */
    public OrderView() {
        order = new ArrayList<OrderItem>();
    }

    /**
     * Add an item to the current order.
     * 
     * @param item Item to add to the order.
     */
    public void addOrderItem(OrderItem item) {
        order.add(item);
    }

    /**
     * Displays everything in the current order to the view box.
     * 
     * @param view Box to display the order.
     */
    public void updateView(VBox view) {
        view.getChildren().clear();

        for (OrderItem item : order) {
            // order blocks all have same structure
            HBox orderBlock = new HBox();
            VBox orderItems = new VBox();
            Button plusButton = new Button("+");
            Label amount = new Label(String.valueOf(item.amount));
            Button minusButton = new Button("-");

            // view must be updated after incrementing
            plusButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    item.amount++;
                    updateView(view);
                }
            });

            // view must be updated after decrementing
            minusButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    item.amount--;
                    if (item.amount < 1) {
                        order.remove(item);
                    }
                    updateView(view);
                }
            });

            orderBlock.getChildren().addAll(orderItems, plusButton, amount, minusButton);
            orderBlock.setPrefHeight(20);
            orderItems.setMaxWidth(Double.MAX_VALUE);
            orderItems.setMaxHeight(20);
            plusButton.setMaxHeight(20);
            amount.setMaxHeight(20);
            minusButton.setMaxHeight(20);
            orderBlock.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(orderItems, Priority.ALWAYS);

            // meals display the meal type with the sides and entrees indented below
            // a la carte items have no special indents
            Label typeLabel;
            switch (item.type) {
                case BOWL:
                    typeLabel = new Label("Bowl");
                    orderItems.getChildren().add(typeLabel);
                    break;

                case PLATE:
                    typeLabel = new Label("Plate");
                    orderItems.getChildren().add(typeLabel);
                    break;

                case BIGGER_PLATE:
                    typeLabel = new Label("Bigger Plate");
                    orderItems.getChildren().add(typeLabel);
                    break;

                default:
                    break;
            }

            // looping through food items of the order and printing them out as labels
            for (MenuItem i : item.menuItems) {
                Label foodItem = new Label(i.name);
                foodItem.setMaxWidth(Double.MAX_VALUE);
                foodItem.setPrefHeight(20);
                foodItem.setStyle("-fx-padding: 0 0 0 20;");
                orderItems.getChildren().add(foodItem);
            }

            view.getChildren().add(orderBlock);
        }
    }

    /**
     * Returns the current orders.
     * 
     * @return The current orders.
     */
    public ArrayList<OrderItem> getOrders() {
        return order;
    }
}
