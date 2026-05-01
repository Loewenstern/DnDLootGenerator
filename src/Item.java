import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.MalformedURLException;
import java.net.URL;

public class Item {
    public enum Rarity{
        MUNDANE,
        COMMON,
        UNCOMMON,
        RARE,
        VERY_RARE,
        LEGENDARY,
        ARTIFACT
    }

    private final String id;
    private int price;
    private URL url;
    private Rarity rarity;
    private boolean active = true;


    @JsonIgnore
    private ItemCategory category;
    @JsonIgnore
    private int amount = 0;


    @JsonCreator
    private Item(@JsonProperty ("id") String id, @JsonProperty ("price") int price, @JsonProperty ("url") URL url, @JsonProperty ("active") boolean active, @JsonProperty ("rarity") Rarity rarity){
        this.id = id;
        this.price = price;
        this.url = url;
        this.active = active;
        this.rarity = rarity;
    }

    public Item(String id, String price, ItemCategory category, Rarity rarity, String url){
        int count = 1;
        for (ItemCategory itemCategory : Main.getCategories()){
            for (Item item : itemCategory.getItems()){
                if (id.equals(item.getId())){
                    count++;
                }
            }
        }
        if (count > 1){
            this.id = id + count;
            Main.getItemProperties().setProperty(id + count, id);
        }
        else {
            this.id = id;
            Main.getItemProperties().setProperty(id, id);
        }
        this.price = Integer.parseInt(price.replace(".", ""));
        this.category = category;
        this.rarity = rarity;
        setUrl(url);
    }

    @JsonIgnore
    public String getName(){
        return Main.getItemProperties().getProperty(id);
    }

    public void setName(String name){
        Main.getItemProperties().setProperty(id, name);
    }

    public int getPrice() {
        return price;
    }


    public void setPrice(String price){
        this.price = Integer.parseInt(price.replace(".", ""));
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increaseAmount(){
        amount++;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public int getWeight(){
        if (amount == 0){
            return price;
        }
        else {
            return price * 100;
        }
    }

    @JsonIgnore
    public String getUrlString() {
        if (url != null){
            return url.toString();
        }
        else {
            return "";
        }
    }

    public void setUrl(String urlString) {
        if (!urlString.isEmpty()){
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public URL getUrl(){
        return url;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity){
        this.rarity = rarity;
    }
}
