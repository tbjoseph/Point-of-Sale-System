package project.beta.manager;

import project.beta.BackendDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import project.beta.manager.ManagerController;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller for the manager view. This class handles all of the actions that
 * can be performed by the manager.
 * 
 * @author Daxton Gilliam
 */
public class ManagerController {
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, Long> inventoryIdTableCol;
    @FXML
    private TableColumn<Inventory, String> itemNameCol;
    @FXML
    private TableColumn<Inventory, Integer> quantityCol;
    @FXML
    private TableColumn<Inventory, Integer> shipmentSizeCol;

    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, Long> index;
    @FXML
    private TableColumn<Menu, String> nameCol;
    @FXML
    private TableColumn<Menu, String> mealTypeCol;
    @FXML
    private TableColumn<Menu, String> descriptionCol;
    @FXML
    private TableColumn<Menu, Float> priceSmall;
    @FXML
    private TableColumn<Menu, Float> priceMedium;
    @FXML
    private TableColumn<Menu, Float> priceLarge;

    @FXML
    private TextField menuNameField;
    @FXML
    private TextField mealTypeField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceSmallField;
    @FXML
    private TextField priceMediumField;
    @FXML
    private TextField priceLargeField;

    @FXML
    private Label errorText;

    private BackendDAO dao;

    /**
     * Represents a menu item
     * 
     * @author Daxton Gilliam
     */
    public class Menu {
        private Long index;
        /**
         * the name of the menu item
         */
        public String name;
        /**
         * the meal type of the menu item
         */
        public String mealType;
        /**
         * the description of the menu item
         */
        public String description;
        /**
         * the small price of the menu item
         */
        public float priceSmall;
        /**
         * the medium price of the menu item
         */
        public float priceMedium;
        /**
         * the large price of the menu item
         */
        public float priceLarge;

        /**
         * Create a new menu item
         * 
         * @param index       the index of the menu item
         * @param name        the name of the menu item
         * @param mealType    the type of meal
         * @param description the description of the menu item
         * @param priceSmall  the price of the small size of the menu item
         * @param priceMedium the price of the medium size of the menu item
         * @param priceLarge  the price of the large size of the menu item
         */
        public Menu(Long index, String name, String mealType, String description, float priceSmall, float priceMedium,
                float priceLarge) {
            this.index = index;
            this.name = name;
            this.mealType = mealType;
            this.description = description;
            this.priceSmall = priceSmall;
            this.priceMedium = priceMedium;
            this.priceLarge = priceLarge;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the index
         */
        public Long getIndex() {
            return index;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the meal type
         */
        public String getMealType() {
            return mealType;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the small price
         */
        public float getPriceSmall() {
            return this.priceSmall;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the medium price
         */
        public float getPriceMedium() {
            return this.priceMedium;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the large price
         */
        public float getPriceLarge() {
            return this.priceLarge;
        }
    }

    /**
     * Represents an inventory item
     * 
     * @author Daxton Gilliam
     */
    public class Inventory {
        /**
         * The inventory id of the inventory item
         */
        public long inventoryId;
        /**
         * The item name of the inventory item
         */
        public String itemName;
        /**
         * The quantity of the inventory item
         */
        public int quantity;
        /**
         * The shipment size of the inventory item
         */
        public int shipmentSize;

        /**
         * Constructor for the inventory item
         * 
         * @param inventoryId  the id of the inventory item
         * @param itemName     the name of the inventory item
         * @param quantity     the quantity of the inventory item
         * @param shipmentSize the size of the shipment of the inventory item
         */
        public Inventory(long inventoryId, String itemName, int quantity, int shipmentSize) {
            this.inventoryId = inventoryId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.shipmentSize = shipmentSize;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the inventoryId
         */
        public long getInventoryId() {
            return inventoryId;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the itemName
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * Required for use in a PropertyValueFactory
         * 
         * @return the shipmentSize
         */
        public int getShipmentSize() {
            return shipmentSize;
        }
    }

    /**
     * A default constructor for the controller.
     */
    public ManagerController() {
    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }

    /**
     * Initializes both the inventory and menu tables, querying the database for
     * the most recent data.
     */
    public void initTable() {
        // Initialize the inventory table
        showInventoryTable(null);
        showMenuTable(null);

        menuTable.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.name = event.getNewValue();
            dao.updateMenu(menu);
        });

        mealTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        mealTypeCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.mealType = event.getNewValue();
            dao.updateMenu(menu);
        });

        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.description = event.getNewValue();
            dao.updateMenu(menu);
        });

        priceSmall.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceSmall.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.priceSmall = event.getNewValue();
            dao.updateMenu(menu);
        });

        priceMedium.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceMedium.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.priceMedium = event.getNewValue();
            dao.updateMenu(menu);
        });

        priceLarge.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceLarge.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.priceLarge = event.getNewValue();
            dao.updateMenu(menu);
        });

        inventoryTable.setEditable(true);

        itemNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.itemName = event.getNewValue();
            dao.updateInventory(inventory);
        });

        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.quantity = event.getNewValue();
            dao.updateInventory(inventory);
        });

        shipmentSizeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        shipmentSizeCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.shipmentSize = event.getNewValue();
            dao.updateInventory(inventory);
        });
    }

    /**
     * Updates the menu table with the new menu items from the database.
     * 
     * @param event the event that triggered this method
     */
    public void showMenuTable(ActionEvent event) {
        menuTable.setVisible(true);
        menuTable.getItems().clear();
        inventoryTable.setVisible(false);
        inventoryTable.getItems().clear();
        try {
            ResultSet rs = dao.getMenuItems();

            index.setCellValueFactory(new PropertyValueFactory<Menu, Long>("index"));
            nameCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            mealTypeCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("mealType"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("description"));
            priceSmall.setCellValueFactory(new PropertyValueFactory<Menu, Float>("priceSmall"));
            priceMedium.setCellValueFactory(new PropertyValueFactory<Menu, Float>("priceMedium"));
            priceLarge.setCellValueFactory(new PropertyValueFactory<Menu, Float>("priceLarge"));
            while (rs.next()) {
                Long index = rs.getLong("id");
                String name = rs.getString("name");
                String mealType = rs.getString("meal_type");
                String description = rs.getString("description");
                Float price_small = rs.getFloat("price_small");
                Float price_medium = rs.getFloat("price_med");
                Float price_large = rs.getFloat("price_large");
                Menu menu = new Menu(index, name, mealType, description, price_small, price_medium, price_large);
                menuTable.getItems().add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the inventory table in the UI with the current inventory items
     * in the database.
     * 
     * @param event the event that triggered this method
     */
    public void showInventoryTable(ActionEvent event) {
        menuTable.setVisible(false);
        menuTable.getItems().clear();
        inventoryTable.setVisible(true);
        inventoryTable.getItems().clear();
        try {
            ResultSet rs = dao.getInventoryItems();

            inventoryIdTableCol.setCellValueFactory(new PropertyValueFactory<Inventory, Long>("inventoryId"));
            itemNameCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("itemName"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("quantity"));
            shipmentSizeCol.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("shipmentSize"));
            while (rs.next()) {
                Long inventoryId = rs.getLong("inventory_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int shipmentSize = rs.getInt("shipment_size");
                Inventory item = new Inventory(inventoryId, itemName, quantity, shipmentSize);
                inventoryTable.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a menu item to the database
     * 
     * @param event the event that triggered this method
     * @throws SQLException if there is an error with the database
     */
    public void addMenuItem(ActionEvent event) throws SQLException {
        String menuNameString = this.menuNameField.getText();
        String mealTypeString = this.mealTypeField.getText();
        String descriptionString = this.descriptionField.getText();
        Float priceSmallFloat = Float.parseFloat(this.priceSmallField.getText());
        Float priceMediumFloat = Float.parseFloat(this.priceMediumField.getText());
        Float priceLargeFloat = Float.parseFloat(this.priceLargeField.getText());
        dao.addMenuItem(menuNameString, mealTypeString, descriptionString, priceSmallFloat, priceMediumFloat,
                priceLargeFloat);
        showMenuTable(event);
    }
}
