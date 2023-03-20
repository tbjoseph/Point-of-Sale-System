package project.beta.manager;

import project.beta.BackendDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import project.beta.manager.ManagerController;
import project.beta.reports.ReportsHomeController;
import project.beta.types.InventoryItem;
import project.beta.types.MenuItem;
import project.beta.types.Association;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Controller for the manager view. This class handles all of the actions that
 * can be performed by the manager.
 * 
 * @author Daxton Gilliam
 */
public class ManagerController {
    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, Long> inventoryIdTableCol;
    @FXML
    private TableColumn<InventoryItem, String> itemNameCol;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityCol;
    @FXML
    private TableColumn<InventoryItem, Integer> shipmentSizeCol;
    @FXML
    private TableColumn<InventoryItem, Integer> thresholdCol;

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
    private TableView<Association> assocTable;
    @FXML
    private TableColumn<Association, Long> id;
    @FXML
    private TableColumn<Association, Long> menuIndex;
    @FXML
    private TableColumn<Association, Long> inventoryIndex;

    @FXML
    private HBox addMenu;
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
    private HBox addInventory;
    @FXML
    private TextField itemNumberField;
    @FXML
    private TextField itemNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField shipmentField;
    @FXML
    private TextField restockField;

    @FXML
    private HBox addAssociation;
    @FXML
    private TextField menuIndexField;
    @FXML
    private TextField inventoryIndexField;

    @FXML
    private Button headerMenu;
    @FXML
    private Button headerInventory;
    @FXML
    private Button headerAssociations;

    @FXML
    private Label errorText;

    private BackendDAO dao;

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
     * Initializes both the inventoryItem and MenuItem tables, querying the database
     * for
     * the most recent data.
     */
    public void initTable() {
        // Initialize the inventoryItem table
        showInventoryTable(null);
        showMenuTable(null);

        menuTable.setEditable(true);

        // lambdas used to handle errors thrown by the DAO
        Consumer<MenuItem> updateMenu = (m) -> {
            try {
                dao.updateMenu(m);
            } catch (SQLException e) {
                handleError(e);
            }
        };
        Consumer<InventoryItem> updateInventory = (i) -> {
            try {
                dao.updateInventory(i);
            } catch (SQLException e) {
                handleError(e);
            }
        };
        Consumer<Association> updateAssociations = (j) -> {
            try{
                dao.updateAssociations(j);
            } catch (SQLException e) {
                handleError(e);
            }
        };

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.name = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        mealTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        mealTypeCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.mealType = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.description = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        priceSmall.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceSmall.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceSmall = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        priceMedium.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceMedium.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceMedium = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        priceLarge.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        priceLarge.setOnEditCommit(event -> {
            MenuItem MenuItem = event.getRowValue();
            MenuItem.priceLarge = event.getNewValue();
            updateMenu.accept(MenuItem);
        });

        inventoryTable.setEditable(true);

        itemNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameCol.setOnEditCommit(event -> {
            InventoryItem inventoryItem = event.getRowValue();
            inventoryItem.itemName = event.getNewValue();
            updateInventory.accept(inventoryItem);
        });

        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityCol.setOnEditCommit(event -> {
            InventoryItem inventoryItem = event.getRowValue();
            inventoryItem.quantity = event.getNewValue();
            updateInventory.accept(inventoryItem);
        });

        shipmentSizeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        shipmentSizeCol.setOnEditCommit(event -> {
            InventoryItem inventoryItem = event.getRowValue();
            inventoryItem.shipmentSize = event.getNewValue();
            updateInventory.accept(inventoryItem);
        });

        thresholdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        thresholdCol.setOnEditCommit(event -> {
            InventoryItem inventoryItem = event.getRowValue();
            inventoryItem.restockThreshold = event.getNewValue();
            updateInventory.accept(inventoryItem);
        });
    
        assocTable.setEditable(true);

        menuIndex.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        menuIndex.setOnEditCommit(event -> {
            Association association = event.getRowValue();
            association.menuId = event.getNewValue();
            updateAssociations.accept(association);
        });

        inventoryIndex.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        inventoryIndex.setOnEditCommit(event -> {
            Association association = event.getRowValue();
            association.inventoryId = event.getNewValue();
            updateAssociations.accept(association);
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
        addMenu.setVisible(true);
        headerMenu.getStyleClass().add("active");
        inventoryTable.setVisible(false);
        inventoryTable.getItems().clear();
        addInventory.setVisible(false);
        headerInventory.getStyleClass().remove("active");
        assocTable.setVisible(false);
        assocTable.getItems().clear();
        addAssociation.setVisible(false);
        headerAssociations.getStyleClass().remove("active");


        try {
            index.setCellValueFactory(new PropertyValueFactory<MenuItem, Long>("index"));
            nameCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().name));
            mealTypeCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().mealType));
            descriptionCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().description));
            priceSmall.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Float>(r.getValue().priceSmall));
            priceMedium.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Float>(r.getValue().priceMedium));
            priceLarge.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Float>(r.getValue().priceLarge));
            for (MenuItem item : dao.getMenuItems()) {
                menuTable.getItems().add(item);
            }
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Updates the inventoryItem table in the UI with the current inventoryItem
     * items
     * in the database.
     * 
     * @param event the event that triggered this method
     */
    public void showInventoryTable(ActionEvent event) {
        menuTable.setVisible(false);
        menuTable.getItems().clear();
        addMenu.setVisible(false);
        headerMenu.getStyleClass().remove("active");
        inventoryTable.setVisible(true);
        inventoryTable.getItems().clear();
        addInventory.setVisible(true);
        headerInventory.getStyleClass().add("active");
        assocTable.setVisible(false);
        assocTable.getItems().clear();
        addAssociation.setVisible(false);
        headerAssociations.getStyleClass().remove("active");



        try {
            ResultSet rs = dao.getInventoryItems();

            inventoryIdTableCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().inventoryId));
            itemNameCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().itemName));
            quantityCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().quantity));
            shipmentSizeCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().shipmentSize));
            thresholdCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().restockThreshold));
            while (rs.next()) {
                Long inventoryId = rs.getLong("inventory_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int shipmentSize = rs.getInt("shipment_size");
                int restockThreshold = rs.getInt("restock_threshold");
                InventoryItem item = new InventoryItem(inventoryId, itemName, quantity, shipmentSize, restockThreshold);
                inventoryTable.getItems().add(item);
            }
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Updates the inventoryItem table in the UI with the current inventoryItem
     * items
     * in the database.
     * 
     * @param event the event that triggered this method
     */
    public void showAssociationTable(ActionEvent event) {
        menuTable.setVisible(false);
        menuTable.getItems().clear();
        addMenu.setVisible(false);
        headerMenu.getStyleClass().remove("active");
        inventoryTable.setVisible(false);
        inventoryTable.getItems().clear();
        addInventory.setVisible(false);
        headerInventory.getStyleClass().remove("active");
        assocTable.setVisible(true);
        assocTable.getItems().clear();
        addAssociation.setVisible(true);
        headerAssociations.getStyleClass().add("active");


        try {
            ResultSet rs = dao.getAssociations();

            menuIndex.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().menuId));
            inventoryIndex.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().inventoryId));
            id.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().id));
            while (rs.next()) {
                Long menuIdVar = rs.getLong("menu_item_id");
                Long inventoryIdVar = rs.getLong("inventory_item_id");
                Long id = rs.getLong("id");
                Association item = new Association(menuIdVar, inventoryIdVar, id);
                assocTable.getItems().add(item);
            }
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Add a MenuItem item to the database
     * 
     * @param event the event that triggered this method
     */
    public void addMenuItem(ActionEvent event) {
        try {
            String menuNameString = this.menuNameField.getText();
            String mealTypeString = this.mealTypeField.getText();
            String descriptionString = this.descriptionField.getText();
            Float priceSmallFloat = Float.parseFloat(this.priceSmallField.getText());
            Float priceMediumFloat = Float.parseFloat(this.priceMediumField.getText());
            Float priceLargeFloat = Float.parseFloat(this.priceLargeField.getText());
            dao.addMenuItem(menuNameString, mealTypeString, descriptionString, priceSmallFloat, priceMediumFloat,
                    priceLargeFloat);
            showMenuTable(event);
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Add an inventory item to the database
     * 
     * @param event the event that triggered this method
     */
    public void addInventoryItem(ActionEvent event) {
        try {
            Long itemNumber = Long.parseLong(this.itemNumberField.getText());
            String itemNameString = this.itemNameField.getText();
            Integer quantityInt = Integer.parseInt(this.quantityField.getText());
            Integer shipmentSizeInt = Integer.parseInt(this.shipmentField.getText());
            Integer restockThreshold = Integer.parseInt(this.restockField.getText());
            dao.addInventoryItem(itemNumber, itemNameString, quantityInt, shipmentSizeInt, restockThreshold);
            showInventoryTable(event);
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Add an association to the database
     * 
     * @param event the event that triggered this method
     */
    public void addAssociation(ActionEvent event) {
        try {
            Long menuIndex = Long.parseLong(this.menuIndexField.getText());
            Long inventoryIndex = Long.parseLong(this.inventoryIndexField.getText());
            dao.addAssociation(menuIndex, inventoryIndex);
            showAssociationTable(event);
        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
     */
    private void handleError(SQLException exception) {
        errorText.textProperty().set("Warning: an error occurred with the database. See the log for details.");
        exception.printStackTrace();
    }

    public void openReports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../reports/home.fxml"));
        Parent root = loader.load();
        ReportsHomeController controller = loader.getController();
        controller.setDAO(dao);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
