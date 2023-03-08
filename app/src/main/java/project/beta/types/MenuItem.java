package project.beta.types;

/**
 * Represents a menu item
 * 
 * @author Daxton Gilliam
 */
public class MenuItem {
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
    public MenuItem(long index, String name, String mealType, String description, float priceSmall, float priceMedium,
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
