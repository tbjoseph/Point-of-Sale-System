package project.beta.types;

/**
 * Represents an inventory item
 * 
 * @author Daxton Gilliam
 */
public class InventoryItem {
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
     * The restock threshold of the inventory item
     */
    public int restockThreshold;

    /**
     * Constructor for the inventory item
     * 
     * @param inventoryId      the id of the inventory item
     * @param itemName         the name of the inventory item
     * @param quantity         the quantity of the inventory item
     * @param shipmentSize     the size of the shipment of the inventory item
     * @param restockThreshold the threshold to restock an inventory item
     */
    public InventoryItem(long inventoryId, String itemName, int quantity, int shipmentSize, int restockThreshold) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.shipmentSize = shipmentSize;
        this.restockThreshold = restockThreshold;
    }
}
