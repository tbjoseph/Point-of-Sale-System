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
import java.io.IOException;
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
        private String name;
        private String mealType;
        private String description;
        private float priceSmall;
        private float priceMedium;
        private float priceLarge;

        /**
         * @param index       - the index of the menu item
         * @param name        - the name of the menu item
         * @param mealType    - the type of meal
         * @param description - the description of the menu item
         * @param priceSmall  - the price of the small size of the menu item
         * @param priceMedium - the price of the medium size of the menu item
         * @param priceLarge  - the price of the large size of the menu item
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
         * @return the index
         */
        public Long getIndex() {
            return index;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name - the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the meal type
         */
        public String getMealType() {
            return mealType;
        }

        /**
         * @param mealType - the meal type to set
         */
        public void setMealType(String mealType) {
            this.mealType = mealType;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description - the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return the small price
         */
        public float getPriceSmall() {
            return this.priceSmall;
        }

        /**
         * @param priceSmall - the small price to set
         */
        public void setPriceSmall(float priceSmall) {
            this.priceSmall = priceSmall;
        }

        /**
         * @return the medium price
         */
        public float getPriceMedium() {
            return this.priceMedium;
        }

        /**
         * @param priceMedium - the medium price to set
         */
        public void setPriceMedium(float priceMedium) {
            this.priceMedium = priceMedium;
        }

        /**
         * @return the large price
         */
        public float getPriceLarge() {
            return this.priceLarge;
        }

        /**
         * @param priceLarge - the large price to set
         */
        public void setPriceLarge(float priceLarge) {
            this.priceLarge = priceLarge;
        }
    }

    /**
     * Represents an inventory item
     * 
     * @author Daxton Gilliam
     */
    public class Inventory {
        private long inventoryId;
        private String itemName;
        private int quantity;
        private int shipmentSize;

        /**
         * @param inventoryId  - the id of the inventory item
         * @param itemName     - the name of the inventory item
         * @param quantity     - the quantity of the inventory item
         * @param shipmentSize - the size of the shipment of the inventory item
         */
        public Inventory(long inventoryId, String itemName, int quantity, int shipmentSize) {
            this.inventoryId = inventoryId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.shipmentSize = shipmentSize;
        }

        /**
         * @return the inventoryId
         */
        public long getInventoryId() {
            return inventoryId;
        }

        /**
         * @param inventoryId - the inventoryId to set
         */
        public void setInventoryId(long inventoryId) {
            this.inventoryId = inventoryId;
        }

        /**
         * @return the itemName
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * @param itemName - the itemName to set
         */
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        /**
         * @return the quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * @param quantity - the quantity to set
         */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        /**
         * @return the shipmentSize
         */
        public int getShipmentSize() {
            return shipmentSize;
        }

        /**
         * @param shipmentSize - the shipmentSize to set
         */
        public void setShipmentSize(int shipmentSize) {
            this.shipmentSize = shipmentSize;
        }
    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao - the DAO to use for this controller
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
            menu.setName(event.getNewValue());
            dao.updateMenu(menu);
        });

        mealTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        mealTypeCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setMealType(event.getNewValue());
            dao.updateMenu(menu);
        });

        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setDescription(event.getNewValue());
            dao.updateMenu(menu);
        });

        priceSmall.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceSmall.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setPriceSmall(event.getNewValue());
            dao.updateMenu(menu);
        });

        priceMedium.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceMedium.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setPriceMedium(event.getNewValue());
            dao.updateMenu(menu);
        });

        priceLarge.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceLarge.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setPriceLarge(event.getNewValue());
            dao.updateMenu(menu);
        });

        inventoryTable.setEditable(true);

        itemNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.setItemName(event.getNewValue());
            dao.updateInventory(inventory);
        });

        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.setQuantity(event.getNewValue());
            dao.updateInventory(inventory);
        });

        shipmentSizeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        shipmentSizeCol.setOnEditCommit(event -> {
            Inventory inventory = event.getRowValue();
            inventory.setShipmentSize(event.getNewValue());
            dao.updateInventory(inventory);
        });
    }

    /**
     * Updates the menu table with the new menu items from the database.
     * 
     * @param event - the event that triggered this method
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
     * @param event - the event that triggered this method
     */
    public void showInventoryTable(ActionEvent event) {
        menuTable.setVisible(false);
        menuTable.getItems().clear();
        inventoryTable.setVisible(true);
        inventoryTable.getItems().clear();
        try {
            String query = "SELECT * FROM inventory_items";
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
     * @param event - the event that triggered this method
     * @throws SQLException - if there is an error with the database
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
