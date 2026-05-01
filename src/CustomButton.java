import javax.swing.*;
import java.awt.event.ActionListener;

public class CustomButton extends JButton {
    private Item item;
    private ItemCategory itemCategory;
    private final boolean isItem;

    public CustomButton(String text, Item item, ActionListener actionListener){
        super(text);
        this.item = item;
        addActionListener(actionListener);
        isItem = true;
    }

    public CustomButton(Icon icon, Item item, ActionListener actionListener){
        super(icon);
        this.item = item;
        addActionListener(actionListener);
        isItem = true;
    }

    public CustomButton(String text, ItemCategory itemCategory, ActionListener actionListener){
        super(text);
        this.itemCategory = itemCategory;
        addActionListener(actionListener);
        isItem = false;
    }

    public CustomButton(Icon icon, ItemCategory itemCategory, ActionListener actionListener){
        super(icon);
        this.itemCategory = itemCategory;
        addActionListener(actionListener);
        isItem = false;
    }

    public Item getItem() {
        return item;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public boolean isItem(){
        return isItem;
    }
}
