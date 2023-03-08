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
import javafx.util.converter.IntegerStringConverter;
import project.beta.manager.ManagerController;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerController {
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, Integer> inventoryIdTableCol;
    @FXML
    private TableColumn<Inventory, String> itemNameCol;
    @FXML
    private TableColumn<Inventory, Integer> quantityCol;
    @FXML
    private TableColumn<Inventory, Integer> shipmentSizeCol;

    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, Integer> menuIdCol;
    @FXML
    private TableColumn<Menu, String> nameCol;
    @FXML
    private TableColumn<Menu, Integer> inventoryIdCol;
    @FXML
    private TableColumn<Menu, String> mealTypeCol;
    @FXML
    private TableColumn<Menu, String> descriptionCol;

    @FXML
    private TextField menuIdField;
    @FXML
    private TextField menuNameField;
    @FXML
    private TextField inventoryIdField;
    @FXML
    private TextField mealTypeField;
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Label errorText;

    private BackendDAO dao;

    public class Menu {
        private int menuId;
        private String name;
        private int inventoryId;
        private String mealType;
        private String description;

        public Menu(int menuId, String name, int inventoryId, String mealType, String description) {
            this.menuId = menuId;
            this.name = name;
            this.inventoryId = inventoryId;
            this.mealType = mealType;
            this.description = description;
        }

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getInventoryId() {
            return inventoryId;
        }

        public void setInventoryId(int inventoryId) {
            this.inventoryId = inventoryId;
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
    }

    public class Inventory {
        private int inventoryTableId;
        private String itemName;
        private int quantity;
        private int shipmentSize;
    
        public Inventory(int inventoryTableId, String itemName, int quantity, int shipmentSize) {
            this.inventoryTableId = inventoryTableId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.shipmentSize = shipmentSize;
        }
        
        public int getInventoryTableId(){
            return inventoryTableId;
        }

        public void setInventoryTableId(int inventoryTableId){
            this.inventoryTableId = inventoryTableId;
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
    
        public int getShipmentSize(){
            return shipmentSize;
        }

        public void setShipmentSize(int shipmentSize){
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
            menuIdCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("menuId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            inventoryIdCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("inventoryId"));
            mealTypeCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("mealType"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("description"));

            while (rs.next()) {
                int menuId = rs.getInt("menu_id");
                String name = rs.getString("name");
                int inventoryId = rs.getInt("inventory_id");
                String mealType = rs.getString("meal_type");
                String description = rs.getString("description");
                Menu menu = new Menu(menuId, name, inventoryId, mealType, description);
                menuTable.getItems().add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        menuTable.setEditable(true);

        menuIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        menuIdCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setMenuId(event.getNewValue());
            dao.updateMenu(menu);
        });

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setName(event.getNewValue());
            dao.updateMenu(menu);
        });

        inventoryIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        inventoryIdCol.setOnEditCommit(event -> {
            Menu menu = event.getRowValue();
            menu.setInventoryId(event.getNewValue());
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
    }

    public void showMenuTable(ActionEvent event) {
        menuTable.setVisible(true);
        inventoryTable.setVisible(false);
        try {
            String query = "SELECT * FROM menu_items";
            ResultSet rs = dao.executeQuery(query);

            menuIdCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("menuId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("name"));
            inventoryIdCol.setCellValueFactory(new PropertyValueFactory<Menu, Integer>("inventoryId"));
            mealTypeCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("mealType"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<Menu, String>("description"));

            while (rs.next()) {
                int menuId = rs.getInt("menu_id");
                String name = rs.getString("name");
                int inventoryId = rs.getInt("inventory_id");
                String mealType = rs.getString("meal_type");
                String description = rs.getString("description");
                Menu menu = new Menu(menuId, name, inventoryId, mealType, description);
                menuTable.getItems().add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showInventoryTable(ActionEvent event){
        menuTable.setVisible(false);
        inventoryTable.setVisible(true);
        try {
            String query = "SELECT * FROM inventory_items";
            ResultSet rs = dao.executeQuery(query);

            inventoryIdTableCol.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("inventoryId"));
            itemNameCol.setCellValueFactory(new PropertyValueFactory<Inventory, String>("itemName"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("quantity"));
            shipmentSizeCol.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("shipmentSize"));
            while (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
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
        Integer menuIdString = Integer.parseInt(this.menuIdField.getText());
        String menuNameString = this.menuNameField.getText();
        Integer inventoryIdString = Integer.parseInt(this.inventoryIdField.getText());
        String mealTypeString = this.mealTypeField.getText();
        String descriptionString = this.descriptionField.getText();
        dao.addMenuItem(menuIdString, menuNameString, inventoryIdString, mealTypeString, descriptionString);
        initTable();
    }
}
