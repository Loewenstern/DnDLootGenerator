import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class Main {
    private static ArrayList<ItemCategory> categories;
    private static GUI gui;
    private static Locale locale = Locale.ENGLISH;
    private static final Locale[] supportedLocales = {
            Locale.ENGLISH,
            Locale.GERMAN,
    };
    private static ResourceBundle guiBundle;
    private static I18nProperties itemProperties;
    private static I18nProperties categoryProperties;
    private static int moneyValue;

    public static void main(String[] args) {
        guiBundle = ResourceBundle.getBundle("language", locale);
        itemProperties = new I18nProperties("item", locale);
        categoryProperties = new I18nProperties("category", locale);
        gui = new GUI();

        load();
        for (ItemCategory category : categories){
            for (Item item : category.getItems()){
                item.setCategory(category);
            }
        }

        gui.displayItems();
    }

    public static void generateRewards(int goldValue){

        float equipmentPercentage = randomFloat(0.5f, 0.9f);
        int equipmentValue = Math.round(goldValue * equipmentPercentage);
        moneyValue = goldValue - equipmentValue;
        ArrayList<Item> rewards = new ArrayList<>();

        int minValue = goldValue;
        for (ItemCategory category : categories){
            for (Item item : category.getItems()){
                item.setAmount(0);
                if (item.getPrice() < minValue && item.isActive()) {
                    minValue = item.getPrice();
                }
            }
        }
        int totalWeight = 0;
        for (ItemCategory t : categories){
            if (t.isActive()){
                totalWeight += t.getWeight();
            }
        }
        while (equipmentValue >= minValue && equipmentValue > 0){
            int roll = randomInt(1, totalWeight);
            int tmp = 0;
            // picks a category based on the weight
            for (ItemCategory category : categories){
                if(roll <= category.getWeight() + tmp && category.minValue() <= equipmentValue && category.isActive()){
                    ArrayList<Item> tmpCategory = new ArrayList<>();
                    int totalWeight2 = 0;
                    for (Item item : category.getItems()){
                        if (item.isActive() && item.getPrice() <= equipmentValue){
                            tmpCategory.add(item);
                            totalWeight2 += item.getWeight();
                        }
                    }
                    // picks an item based on the price
                    int roll2 = randomInt(1, totalWeight2);
                    int tmp2 = 0;
                    for (Item item : tmpCategory){
                        if (roll2 <= item.getWeight() + tmp2){
                            if (rewards.contains(item)){
                                item.increaseAmount();
                            }
                            else {
                                rewards.add(item);
                                item.increaseAmount();
                            }
                            equipmentValue -= item.getPrice();
                            break;
                        }
                        else {
                            tmp2 += item.getWeight();
                        }
                    }
                    break;
                }
                else {
                    tmp += category.getWeight();
                }
            }
        }
        moneyValue += equipmentValue;
        gui.displayRewards(rewards);
        splitMoney();

        gui.getMainPageBodyPanel().setVisible(false);
        gui.getMainPageBodyPanel().setVisible(true);
    }

    public static void reRoll(Item reRollItem, ArrayList<Item> rewards){
        reRollItem.setActive(false);
        rewards.remove(reRollItem);
        int equipmentValue = reRollItem.getPrice() * reRollItem.getAmount();
        reRollItem.setAmount(0);

        int minValue = equipmentValue;
        for (ItemCategory category : categories){
            for (Item item : category.getItems()){
                if (item.getPrice() < minValue && item.isActive()) {
                    minValue = item.getPrice();
                }
            }
        }
        int totalWeight = 0;
        for (ItemCategory t : categories){
            if (t.isActive()){
                totalWeight += t.getWeight();
            }
        }
        while (equipmentValue >= minValue && equipmentValue > 0){
            int roll = randomInt(1, totalWeight);
            int tmp = 0;
            // picks a category based on the weight
            for (ItemCategory category : categories){
                if(roll <= category.getWeight() + tmp && category.minValue() <= equipmentValue && category.isActive()){
                    ArrayList<Item> tmpCategory = new ArrayList<>();
                    int totalWeight2 = 0;
                    for (Item item : category.getItems()){
                        if (item.isActive() && item.getPrice() <= equipmentValue){
                            tmpCategory.add(item);
                            totalWeight2 += item.getWeight();
                        }
                    }
                    // picks an item based on the price
                    int roll2 = randomInt(1, totalWeight2);
                    int tmp2 = 0;
                    for (Item item : tmpCategory){
                        if (roll2 <= item.getWeight() + tmp2){
                            if (rewards.contains(item)){
                                item.increaseAmount();
                            }
                            else {
                                rewards.add(item);
                                item.increaseAmount();
                            }
                            equipmentValue -= item.getPrice();
                            break;
                        }
                        else {
                            tmp2 += item.getWeight();
                        }
                    }
                    break;
                }
                else {
                    tmp += category.getWeight();
                }
            }
        }
        reRollItem.setActive(true);
        gui.displayRewards(rewards);
        if (equipmentValue != 0){
            moneyValue += equipmentValue;
            splitMoney();
        }

        gui.getMainPageBodyPanel().setVisible(false);
        gui.getMainPageBodyPanel().setVisible(true);
    }

    private static void splitMoney(){
        gui.getMoneyPanel().removeAll();
        gui.getMoneyPanel().add(new JLabel(guiBundle.getString("gold_value") + ": " + moneyValue));

        float silverPercentage = (float)Math.pow(randomFloat(0.9f, 0.99f), moneyValue);
        int silverCoins = Math.round(silverPercentage * moneyValue) * 10;
        float copperPercentage = (float)Math.pow(randomFloat(0.9f, 0.99f), silverCoins);
        int copperCoins = Math.round(copperPercentage * silverCoins) * 10;
        silverCoins = silverCoins - copperCoins / 10;
        int goldCoins = moneyValue - Math.round(silverCoins / 10.0f + copperCoins / 100.0f);
        double platinumPercentage = randomFloat(-1.5f, -0.5f) * Math.pow(randomFloat(0.9f, 0.99f), goldCoins) + randomFloat(0.4f, 0.6f);
        if (platinumPercentage < 0){
            platinumPercentage = 0;
        }
        else {
            platinumPercentage = Math.round(platinumPercentage * 100.0) / 100.0;
        }
        int platinumCoins = (int)Math.round(moneyValue * platinumPercentage) / 10;
        goldCoins = goldCoins - platinumCoins * 10;
        if ((goldCoins * 100 + silverCoins * 10 + copperCoins + platinumCoins * 1000) != moneyValue * 100){
            gui.getMoneyPanel().add(new JLabel("Coins do not match Gold Value"));
        }
        else {
            gui.displayMoney(platinumCoins, goldCoins, silverCoins, copperCoins);
        }
    }

    public static void save(){
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileWriter fileWriter = new FileWriter("items.json")){
            fileWriter.write(objectMapper.writeValueAsString(categories));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        categoryProperties.save();
        itemProperties.save();
    }

    public static void load(){
        ObjectMapper objectMapper = new ObjectMapper();
        try (FileReader fileReader = new FileReader("items.json")){
            categories = objectMapper.readerForListOf(ItemCategory.class).readValue(fileReader);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static float randomFloat(float min, float max) {
        int randomInt = randomInt((int) (min * 100), (int) (max * 100));
        return randomInt / 100.00f;
    }

    private static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static ArrayList<ItemCategory> getCategories() {
        return categories;
    }

    public static ResourceBundle getGuiBundle(){
        return guiBundle;
    }

    public static I18nProperties getCategoryProperties(){
        return categoryProperties;
    }

    public static I18nProperties getItemProperties() {
        return itemProperties;
    }

    public static Locale[] getSupportedLocales(){
        return supportedLocales;
    }

    public static void changeLanguage(Locale locale){
        Main.locale = locale;
        guiBundle = ResourceBundle.getBundle("language", locale);
        itemProperties = new I18nProperties("item", locale);
        categoryProperties = new I18nProperties("category", locale);
    }

}
