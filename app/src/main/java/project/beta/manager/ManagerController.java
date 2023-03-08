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

    public class Menu {
        private Long index;
        private String name;
        private String mealType;
        private String description;
        private float priceSmall;
        private float priceMedium;
        private float priceLarge;

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

        public Long getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMealType() {
            return mealType;
        }

        public void setMealType(String mealType) {
            this.mealType = mealType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public float getPriceSmall() {
            return this.priceSmall;
        }

        public void setPriceSmall(float priceSmall) {
            this.priceSmall = priceSmall;
        }

        public float getPriceMedium() {
            return this.priceMedium;
        }

        public void setPriceMedium(float priceMedium) {
            this.priceMedium = priceMedium;
        }

        public float getPriceLarge() {
            return this.priceLarge;
        }

        public void setPriceLarge(float priceLarge) {
            this.priceLarge = priceLarge;
        }
    }

    public class Inventory {
        private long inventoryId;
        private String itemName;
        private int quantity;
        private int shipmentSize;

        public Inventory(long inventoryId, String itemName, int quantity, int shipmentSize) {
            this.inventoryId = inventoryId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.shipmentSize = shipmentSize;
        }

        public long getInventoryId() {
            return inventoryId;
        }

        public void setInventoryId(long inventoryId) {
            this.inventoryId = inventoryId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getShipmentSize() {
            return shipmentSize;
        }

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

    public void initTable() {
        try {
            // Create an SQL statement to select the data
            String query = "SELECT * FROM menu_items";

            // Execute the SQL statement and retrieve the data
            ResultSet rs = dao.executeQuery(query);

            // Bind the data to the table

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
        try {
            String query = "SELECT * FROM inventory_items";
            ResultSet rs = dao.executeQuery(query);

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

    public void showMenuTable(ActionEvent event) {
        menuTable.setVisible(true);
        menuTable.getItems().clear();
        inventoryTable.setVisible(false);
        inventoryTable.getItems().clear();
        try {
            String query = "SELECT * FROM menu_items";
            ResultSet rs = dao.executeQuery(query);

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

    public void showInventoryTable(ActionEvent event) {
        menuTable.setVisible(false);
        menuTable.getItems().clear();
        inventoryTable.setVisible(true);
        inventoryTable.getItems().clear();
        try {
            String query = "SELECT * FROM inventory_items";
            ResultSet rs = dao.executeQuery(query);

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
     * Try to login with the form data
     * 
     * @param event - the event that triggered this method
     * @throws IOException
     * @throws SQLException
     */
    public void addMenuItem(ActionEvent event) throws IOException, SQLException {
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
