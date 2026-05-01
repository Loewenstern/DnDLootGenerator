import javax.swing.*;
import java.awt.event.ActionListener;

public class CustomCheckBox extends JCheckBox {
    private Item item;
    private ItemCategory itemCategory;
    private final boolean isItem;

    public CustomCheckBox(Item item, ActionListener actionListener){
        super();
        this.item = item;
        addActionListener(actionListener);
        if (item.isActive()){
            setSelected(true);
        }
        isItem = true;
    }

    public CustomCheckBox(ItemCategory itemCategory, ActionListener actionListener){
        super();
        this.itemCategory = itemCategory;
        addActionListener(actionListener);
        if (itemCategory.isActive()){
            setSelected(true);
        }
        isItem = false;
    }

    public Item getItem() {
        return item;
    }

    public boolean isItem(){
        return isItem;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }
}
