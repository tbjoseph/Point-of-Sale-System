package project.beta.types;

/**
 * Represents an association between an inventory item and a menu item
 * 
 * @author Daxton Gilliam
 */
public class Association{
    /**
     * The item name of the inventory item
     */
    public long menuId;
    /**
     * The inventory id of the inventory item
     */
    public long inventoryId;

    /**
     * The association id of the table
     */
    public long id;


    /**
     * Constructor for the association between menu item and inventory item
     * 
     * @param menuId    the if of the menu item
     * @param inventoryId  the id of the inventory item
     * @param id the id of the association item
     */
    public Association(long menuId, long inventoryId, long id) {
        this.menuId = menuId;
        this.inventoryId = inventoryId;
        this.id = id;
    }
}