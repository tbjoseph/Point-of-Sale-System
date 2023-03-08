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
import project.beta.types.MenuItem;

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
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, Long> index;
    @FXML
    private TableColumn<MenuItem, String> nameCol;
    @FXML
    private TableColumn<MenuItem, String> mealTypeCol;
    @FXML
    private TableColumn<MenuItem, String> descriptionCol;
    @FXML
    private TableColumn<MenuItem, Float> priceSmall;
    @FXML
    private TableColumn<MenuItem, Float> priceMedium;
    @FXML
    private TableColumn<MenuItem, Float> priceLarge;

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
     * Initializes both the inventory and MenuItem tables, querying the database for
     * the most recent data.
     */
    public void initTable() {
        // Initialize the inventory table
        showInventoryTable(null);
        showMenuTable(null);

        menuTable.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.name = event.getNewValue();
            dao.updateMenu(MenuItem);
        });

        mealTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        mealTypeCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.mealType = event.getNewValue();
            dao.updateMenu(MenuItem);
        });

        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.description = event.getNewValue();
            dao.updateMenu(MenuItem);
        });

        priceSmall.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceSmall.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceSmall = event.getNewValue();
            dao.updateMenu(MenuItem);
        });

        priceMedium.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceMedium.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceMedium = event.getNewValue();
            dao.updateMenu(MenuItem);
        });

        priceLarge.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceLarge.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceLarge = event.getNewValue();
            dao.updateMenu(MenuItem);
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
     * Updates the MenuItem table with the new MenuItem items from the database.
     * 
     * @param event the event that triggered this method
     */
    public void showMenuTable(ActionEvent event) {
        menuTable.setVisible(true);
        menuTable.getItems().clear();
        inventoryTable.setVisible(false);
        inventoryTable.getItems().clear();
        try {
            index.setCellValueFactory(new PropertyValueFactory<MenuItem, Long>("index"));
            nameCol.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
            mealTypeCol.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("mealType"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("description"));
            priceSmall.setCellValueFactory(new PropertyValueFactory<MenuItem, Float>("priceSmall"));
            priceMedium.setCellValueFactory(new PropertyValueFactory<MenuItem, Float>("priceMedium"));
            priceLarge.setCellValueFactory(new PropertyValueFactory<MenuItem, Float>("priceLarge"));
            for (MenuItem item : dao.getMenuItems()) {
                menuTable.getItems().add(item);
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
     * Add a MenuItem item to the database
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
