import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.util.ArrayList;

public class ItemCategory {
    private final String id;
    private int weight;
    private final ArrayList<Item> items;
    private boolean active = true;

    @JsonIgnore
    private boolean collapsed = false;
    @JsonIgnore
    private JPanel panel;
    @JsonIgnore
    private final ArrayList<Item> filteredItems = new ArrayList<>();

    @JsonCreator
    private ItemCategory(@JsonProperty("id") String id, @JsonProperty("weight") int weight, @JsonProperty("items") ArrayList<Item> items, @JsonProperty("active") boolean active){
        this.id = id;
        this.weight = weight;
        this.items = items;
        this.active = active;
    }

    public ItemCategory(String id, String weight){
        int count = 1;
        for (ItemCategory category : Main.getCategories()){
            if (id.equals(category.getId())){
                count++;
            }
        }
        if (count > 1){
            this.id = id + count;
            Main.getCategoryProperties().setProperty(id + count, id);
        }
        else {
            this.id = id;
            Main.getCategoryProperties().setProperty(id, id);
        }
        this.weight = Integer.parseInt(weight.replace(".", ""));
        items = new ArrayList<>();
    }

    @JsonIgnore
    public String getName() {
        return Main.getCategoryProperties().getProperty(id);
    }

    public String getId(){
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void add(Item newItem){
        boolean added = false;
        int index = 0;
        while (!added && index < items.size()){
            if (newItem.getPrice() > items.get(index).getPrice()){
                items.add(index, newItem);
                added = true;
            }
            index++;
        }
        if (!added){
            items.add(newItem);
        }
    }

    public int minValue() {
        int minValue = items.getFirst().getPrice();
        for (Item item : items){
            if (item.isActive() && item.getPrice() < minValue){
                minValue = item.getPrice();
            }
        }
        return minValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void collapse(){
        if (!collapsed){
            panel.getComponent(1).setVisible(false);
            ((JPanel)panel.getComponent(0)).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            collapsed = true;
        }
    }

    public void expand(){
        if (collapsed){
            panel.getComponent(1).setVisible(true);
            ((JPanel)panel.getComponent(0)).setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            collapsed = false;
        }
    }

    public ArrayList<Item> getFilteredItems() {
        return filteredItems;
    }
}
