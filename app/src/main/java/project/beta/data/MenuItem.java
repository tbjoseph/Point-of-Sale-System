package project.beta.data;

public class MenuItem {
    public enum MenuItemType {
        ENTREE, SIDE, BOWL, PLATE, ALACARTE, OTHER
    }

    public String[] names;
    public MenuItemType type;
}
