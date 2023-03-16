package project.beta.types;

import java.util.ArrayList;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
    private ArrayList<OrderItem> orderItems;

    /**
     * Constructs a new OrderView object.
     */
    public OrderView() {
        orderItems = new ArrayList<OrderItem>();
    }

    /**
     * Add an item to the current order.
     * 
     * @param item Item to add to the order.
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    /**
     * Displays everything in the current order to the view box.
     * 
     * @param view Box to display the order.
     */
    public void updateView(VBox view) {
        view.getChildren().clear();

        boolean first = true;
        for (OrderItem item : orderItems) {
            // add a separator between order items
            if (first) {
                first = false;
            } else {
                Separator divider = new Separator();
                view.getChildren().add(divider);
            }

            // order blocks all have same structure
            HBox orderBlock = new HBox();
            VBox orderItemView = new VBox();
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
                        orderItems.remove(item);
                    }
                    updateView(view);
                }
            });

            orderBlock.getChildren().addAll(orderItemView, minusButton, amount, plusButton);
            orderBlock.getStyleClass().add("order-item");
            orderBlock.setPrefHeight(20);
            orderItemView.setMaxWidth(Double.MAX_VALUE);
            orderItemView.setMaxHeight(20);
            plusButton.setMaxHeight(20);
            amount.setMaxHeight(20);
            minusButton.setMaxHeight(20);
            orderBlock.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(orderItemView, Priority.ALWAYS);

            // meals display the meal type with the sides and entrees indented below
            // a la carte items have no special indents
            Label typeLabel = new Label();
            typeLabel.getStyleClass().add("order-item-type");
            switch (item.type) {
                case BOWL:
                    typeLabel.textProperty().set("Bowl");
                    orderItemView.getChildren().add(typeLabel);
                    break;

                case PLATE:
                    typeLabel.textProperty().set("Plate");
                    orderItemView.getChildren().add(typeLabel);
                    break;

                case BIGGER_PLATE:
                    typeLabel.textProperty().set("Bigger Plate");
                    orderItemView.getChildren().add(typeLabel);
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
                orderItemView.getChildren().add(foodItem);
            }

            view.getChildren().add(orderBlock);
        }
    }

    /**
     * Returns the items in this order.
     * 
     * @return The current order items.
     */
    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }
}
