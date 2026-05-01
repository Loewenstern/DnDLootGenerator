import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class GUI extends JFrame implements ActionListener{

    private final JButton generateButton;
    private final JButton settingsButton;
    private final JButton closeSettingsButton;
    private final JButton showAddCategoryButton;
    private final JButton closeAddCategoryButton;
    private final JButton collapseAllButton;
    private final JButton expandAllButton;
    private final JButton addCategoryButton;
    private final JButton closeEditCategoryButton;
    private final JButton deleteCategoryButton;
    private final JButton addItemButton;
    private final JButton closeAddItemButton;
    private final JButton closeEditItemButton;
    private final JButton deleteItemButton;
    private final JButton searchButton;

    private final JFormattedTextField goldValueTextField;
    private final JTextField addCategoryNameTextField;
    private final JFormattedTextField addCategoryWeightTextField;
    private final JTextField editCategoryNameTextField;
    private final JFormattedTextField editCategoryWeightTextField;
    private final JTextField addiItemNameTextField;
    private final JFormattedTextField addItemPriceTextField;
    private final JTextField addItemUrlTextField;
    private final JTextField editItemNameTextField;
    private final JFormattedTextField editItemPriceTextField;
    private final JTextField editItemUrlTextField;
    private final JTextField searchTextField;

    private final JLabel editCategoryLabel;
    private final JLabel goldValueLabel;
    private final JLabel addCategoryNameLabel;
    private final JLabel addCategoryWeightLabel;
    private final JLabel editCategoryWeightLabel;
    private final JLabel editCategoryNameLabel;
    private final JLabel addItemNameLabel;
    private final JLabel addItemPriceLabel;
    private final JLabel addItemRarityLabel;
    private final JLabel editItemNameLabel;
    private final JLabel editItemPriceLabel;
    private final JLabel editItemRarityLabel;


    private final JComboBox<Item.Rarity> addItemRarityComboBox;
    private final JComboBox<Item.Rarity> editItemRarityComboBox;
    private final JComboBox<Locale> languageComboBox;

    private final JPanel moneyPanel;
    private final JPanel mainPageBodyPanel;
    private final JPanel itemPanel;
    private final JPanel editItemHeaderNamePanel;

    private final JPanel mainPage;
    private final JPanel itemPage;
    private final JPanel addCategoryPage;
    private final JPanel editCategoryPage;
    private final JPanel addItemPage;
    private final JPanel editItemPage;

    private ItemCategory category;
    private Item item;

    public GUI(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new OverlayLayout(getContentPane()));
        setSize(500, 700);

        mainPage = new JPanel();
        mainPage.setLayout(new BorderLayout());
        mainPage.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "generate");
        mainPage.getActionMap().put("generate", new KeyAction(KeyAction.Action.GENERATE, this));
        getContentPane().add(mainPage);

        JPanel mainPageHeaderPanel = new JPanel(new BorderLayout());
        mainPage.add(mainPageHeaderPanel, BorderLayout.NORTH);

        JPanel mainPageHeaderTopPanel = new JPanel();
        mainPageHeaderTopPanel.setLayout(new BoxLayout(mainPageHeaderTopPanel, BoxLayout.X_AXIS));
        mainPageHeaderTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        mainPageHeaderPanel.add(mainPageHeaderTopPanel, BorderLayout.NORTH);

        moneyPanel = new JPanel(new GridLayout());
        moneyPanel.add(new JLabel(" "));
        mainPageHeaderPanel.add(moneyPanel, BorderLayout.SOUTH);

        goldValueLabel = new JLabel(Main.getGuiBundle().getString("gold_value") + ":");
        mainPageHeaderTopPanel.add(goldValueLabel);

        mainPageHeaderTopPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        CustomNumberFormatter numberFormatter = new CustomNumberFormatter(intFormat);
        numberFormatter.setValueClass(Integer.class); //optional, ensures you will always get an int value
        numberFormatter.setAllowsInvalid(false); //this is the key!!
        numberFormatter.setMinimum(0);//Optional

        goldValueTextField = new JFormattedTextField(numberFormatter);
        goldValueTextField.setPreferredSize(new Dimension(60, 20));
        mainPageHeaderTopPanel.add(goldValueTextField);

        mainPageHeaderTopPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        generateButton = new JButton(Main.getGuiBundle().getString("generate_loot"));
        generateButton.addActionListener(this);
        mainPageHeaderTopPanel.add(generateButton);

        settingsButton = new JButton(new ImageIcon("icons/settings.png"));
        settingsButton.addActionListener(this);
        mainPageHeaderTopPanel.add(settingsButton);

        mainPageBodyPanel = new JPanel();
        mainPageBodyPanel.setLayout(new BoxLayout(mainPageBodyPanel, BoxLayout.Y_AXIS));
        mainPageBodyPanel.setAlignmentY(10);
        mainPageBodyPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        mainPageBodyPanel.setVisible(false);
        JPanel alignmentWrapper = new JPanel(new BorderLayout());
        alignmentWrapper.add(mainPageBodyPanel, BorderLayout.NORTH);

        JScrollPane scrollPane1 = new JScrollPane(alignmentWrapper,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        mainPage.add(scrollPane1);

        itemPage = new JPanel();
        itemPage.setVisible(false);
        itemPage.setLayout(new BorderLayout());
        itemPage.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "search");
        itemPage.getActionMap().put("search", new KeyAction(KeyAction.Action.SEARCH, this));
        getContentPane().add(itemPage);

        JPanel itemPageHeaderPanel = new JPanel(new BorderLayout());
        itemPage.add(itemPageHeaderPanel, BorderLayout.NORTH);

        JPanel itemPageHeaderButtonPanel = new JPanel(new BorderLayout());
        itemPageHeaderPanel.add(itemPageHeaderButtonPanel);

        closeSettingsButton = new JButton("<");
        closeSettingsButton.addActionListener(this);
        closeSettingsButton.setPreferredSize(new Dimension(50, itemPageHeaderButtonPanel.getHeight()));
        itemPageHeaderButtonPanel.add(closeSettingsButton, BorderLayout.WEST);

        showAddCategoryButton = new JButton(Main.getGuiBundle().getString("add_category"));
        showAddCategoryButton.addActionListener(this);
        itemPageHeaderButtonPanel.add(showAddCategoryButton, BorderLayout.CENTER);

        languageComboBox = new JComboBox<>(Main.getSupportedLocales());
        languageComboBox.setSelectedItem(Main.getGuiBundle().getLocale());
        languageComboBox.addActionListener(_ -> {
            Main.changeLanguage((Locale) languageComboBox.getSelectedItem());
            updateI18nComponents();
        });
        languageComboBox.setPreferredSize(new Dimension(94, languageComboBox.getHeight()));
        itemPageHeaderButtonPanel.add(languageComboBox, BorderLayout.EAST);

        JPanel itemPageHeaderSearchPanel = new JPanel(new BorderLayout());
        itemPageHeaderPanel.add(itemPageHeaderSearchPanel, BorderLayout.SOUTH);

        searchButton = new JButton(new ImageIcon("icons/magnifying-glass.png"));
        searchButton.addActionListener(this);
        itemPageHeaderSearchPanel.add(searchButton, BorderLayout.WEST);
        searchTextField = new JTextField();
        itemPageHeaderSearchPanel.add(searchTextField, BorderLayout.CENTER);

        JPanel collapseButtonPanel = new JPanel(new GridLayout());
        itemPageHeaderSearchPanel.add(collapseButtonPanel, BorderLayout.EAST);

        expandAllButton = new JButton("\uD83E\uDC7B");
        expandAllButton.addActionListener(this);
        collapseButtonPanel.add(expandAllButton);

        collapseAllButton = new JButton("\uD83E\uDC79");
        collapseAllButton.addActionListener(this);
        collapseButtonPanel.add(collapseAllButton);

        itemPanel = new JPanel();
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));
        JPanel alignmentWrapper2 = new JPanel(new BorderLayout());
        alignmentWrapper2.add(itemPanel, BorderLayout.NORTH);

        JScrollPane scrollPane2 = new JScrollPane(alignmentWrapper2,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.getVerticalScrollBar().setUnitIncrement(16);
        itemPage.add(scrollPane2, BorderLayout.CENTER);

        addCategoryPage = new JPanel();
        addCategoryPage.setVisible(false);
        addCategoryPage.setLayout(new BorderLayout());
        getContentPane().add(addCategoryPage);

        JPanel addCategoryHeaderPanel = new JPanel(new BorderLayout());
        addCategoryPage.add(addCategoryHeaderPanel, BorderLayout.NORTH);

        addCategoryButton = new JButton(Main.getGuiBundle().getString("create_new_category"));
        addCategoryButton.addActionListener(this);
        addCategoryHeaderPanel.add(addCategoryButton, BorderLayout.CENTER);


        closeAddCategoryButton = new JButton("<");
        closeAddCategoryButton.addActionListener(this);
        addCategoryHeaderPanel.add(closeAddCategoryButton, BorderLayout.WEST);

        JPanel addCategoryBodyPanel = new JPanel();
        addCategoryPage.add(addCategoryBodyPanel, BorderLayout.CENTER);

        addCategoryNameLabel = new JLabel(Main.getGuiBundle().getString("name") + ":");
        addCategoryBodyPanel.add(addCategoryNameLabel);
        addCategoryNameTextField = new JTextField();
        addCategoryNameTextField.setPreferredSize(new Dimension(100, 30));
        addCategoryBodyPanel.add(addCategoryNameTextField);

        addCategoryWeightLabel = new JLabel(Main.getGuiBundle().getString("weight") + ":");
        addCategoryBodyPanel.add(addCategoryWeightLabel);
        addCategoryWeightTextField = new JFormattedTextField(numberFormatter);
        addCategoryWeightTextField.setPreferredSize(new Dimension(100, 30));
        addCategoryBodyPanel.add(addCategoryWeightTextField);

        editCategoryPage = new JPanel(new BorderLayout());
        editCategoryPage.setVisible(false);
        getContentPane().add(editCategoryPage);

        JPanel editCategoryHeadPanel = new JPanel(new BorderLayout());
        editCategoryPage.add(editCategoryHeadPanel, BorderLayout.NORTH);

        closeEditCategoryButton = new JButton("<");
        closeEditCategoryButton.addActionListener(this);
        editCategoryHeadPanel.add(closeEditCategoryButton, BorderLayout.WEST);

        editCategoryLabel = new JLabel("", SwingConstants.CENTER);
        editCategoryLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
        editCategoryHeadPanel.add(editCategoryLabel, BorderLayout.CENTER);

        deleteCategoryButton = new JButton(new ImageIcon("icons/trash-can.png"));
        deleteCategoryButton.addActionListener(this);
        editCategoryHeadPanel.add(deleteCategoryButton, BorderLayout.EAST);

        JPanel editCategoryBodyPanel = new JPanel();
        editCategoryPage.add(editCategoryBodyPanel, BorderLayout.CENTER);

        editCategoryNameLabel = new JLabel(Main.getGuiBundle().getString("name") + ":");
        editCategoryBodyPanel.add(editCategoryNameLabel);
        editCategoryNameTextField = new JTextField();
        editCategoryNameTextField.setPreferredSize(new Dimension(100, 30));
        editCategoryBodyPanel.add(editCategoryNameTextField);

        editCategoryWeightLabel = new JLabel(Main.getGuiBundle().getString("weight") + ":");
        editCategoryBodyPanel.add(editCategoryWeightLabel);
        editCategoryWeightTextField = new JFormattedTextField(numberFormatter);
        editCategoryWeightTextField.setPreferredSize(new Dimension(100, 30));
        editCategoryBodyPanel.add(editCategoryWeightTextField);

        addItemPage = new JPanel(new BorderLayout());
        addItemPage.setVisible(false);
        getContentPane().add(addItemPage);

        JPanel addItemHeadPanel= new JPanel(new BorderLayout());
        addItemPage.add(addItemHeadPanel, BorderLayout.NORTH);

        closeAddItemButton = new JButton("<");
        closeAddItemButton.addActionListener(this);
        addItemHeadPanel.add(closeAddItemButton, BorderLayout.WEST);

        addItemButton = new JButton(Main.getGuiBundle().getString("add_new_item"));
        addItemButton.addActionListener(this);
        addItemHeadPanel.add(addItemButton, BorderLayout.CENTER);

        JPanel addItemBodyPanel = new JPanel();
        addItemBodyPanel.setLayout(new BoxLayout(addItemBodyPanel, BoxLayout.Y_AXIS));
        JPanel alignmentWrapper3 = new JPanel(new BorderLayout());
        alignmentWrapper3.add(addItemBodyPanel, BorderLayout.NORTH);
        addItemPage.add(alignmentWrapper3);

        JPanel addItemNamePanel = new JPanel();
        addItemBodyPanel.add(addItemNamePanel);
        addItemNameLabel = new JLabel(Main.getGuiBundle().getString("name") + ":");
        addItemNamePanel.add(addItemNameLabel);
        addiItemNameTextField = new JTextField();
        addiItemNameTextField.setPreferredSize(new Dimension(300, 30));
        addItemNamePanel.add(addiItemNameTextField);

        JPanel addItemPricePanel = new JPanel();
        addItemBodyPanel.add(addItemPricePanel);
        addItemPriceLabel = new JLabel(Main.getGuiBundle().getString("price") + ":");
        addItemPricePanel.add(addItemPriceLabel);
        addItemPriceTextField = new JFormattedTextField(numberFormatter);
        addItemPriceTextField.setPreferredSize(new Dimension(100, 30));
        addItemPricePanel.add(addItemPriceTextField);

        JPanel addItemRarityPanel = new JPanel();
        addItemBodyPanel.add(addItemRarityPanel);
        addItemRarityLabel = new JLabel(Main.getGuiBundle().getString("rarity") + ":");
        addItemRarityPanel.add(addItemRarityLabel);
        addItemRarityComboBox = new JComboBox<>(Item.Rarity.values());
        addItemRarityComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(Main.getGuiBundle().getString(value.toString()));
                setForeground(rarityColor((Item.Rarity) value));
                return this;
            }
        });
        addItemRarityPanel.add(addItemRarityComboBox);

        JPanel addItemUrlPanel = new JPanel();
        addItemBodyPanel.add(addItemUrlPanel);
        addItemUrlPanel.add(new JLabel("URL:"));
        addItemUrlTextField = new JTextField();
        addItemUrlTextField.setPreferredSize(new Dimension(300, 30));
        addItemUrlPanel.add(addItemUrlTextField);

        editItemPage = new JPanel(new BorderLayout());
        editItemPage.setVisible(false);
        getContentPane().add(editItemPage);

        JPanel editItemHeaderPanel = new JPanel(new BorderLayout());
        editItemPage.add(editItemHeaderPanel, BorderLayout.NORTH);
        closeEditItemButton = new JButton("<");
        closeEditItemButton.addActionListener(this);
        editItemHeaderPanel.add(closeEditItemButton, BorderLayout.WEST);
        editItemHeaderNamePanel = new JPanel(new GridLayout());
        editItemHeaderNamePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        editItemHeaderPanel.add(editItemHeaderNamePanel);
        deleteItemButton = new JButton(new ImageIcon("icons/trash-can.png"));
        deleteItemButton.addActionListener(this);
        editItemHeaderPanel.add(deleteItemButton, BorderLayout.EAST);

        JPanel editItemBodyPanel = new JPanel();
        editItemBodyPanel.setLayout(new BoxLayout(editItemBodyPanel, BoxLayout.Y_AXIS));
        JPanel alignmentWrapper4 = new JPanel(new BorderLayout());
        alignmentWrapper4.add(editItemBodyPanel, BorderLayout.NORTH);
        editItemPage.add(alignmentWrapper4);

        JPanel editItemNamePanel = new JPanel();
        editItemBodyPanel.add(editItemNamePanel);
        editItemNameLabel = new JLabel(Main.getGuiBundle().getString("name") + ":");
        editItemNamePanel.add(editItemNameLabel);
        editItemNameTextField = new JTextField("test");
        editItemNameTextField.setPreferredSize(new Dimension(300, 30));
        editItemNamePanel.add(editItemNameTextField);

        JPanel editItemPricePanel = new JPanel();
        editItemBodyPanel.add(editItemPricePanel);
        editItemPriceLabel = new JLabel(Main.getGuiBundle().getString("price") + ":");
        editItemPricePanel.add(editItemPriceLabel);
        editItemPriceTextField = new JFormattedTextField(numberFormatter);
        editItemPriceTextField.setPreferredSize(new Dimension(100, 30));
        editItemPricePanel.add(editItemPriceTextField);

        JPanel editItemRarityPanel = new JPanel();
        editItemBodyPanel.add(editItemRarityPanel);
        editItemRarityLabel = new JLabel(Main.getGuiBundle().getString("rarity") + ":");
        editItemRarityPanel.add(editItemRarityLabel);
        editItemRarityComboBox = new JComboBox<>(Item.Rarity.values());
        editItemRarityComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(Main.getGuiBundle().getString(value.toString()));
                setForeground(rarityColor((Item.Rarity) value));
                return this;
            }
        });
        editItemRarityPanel.add(editItemRarityComboBox);

        JPanel editItemUrlPanel = new JPanel();
        editItemBodyPanel.add(editItemUrlPanel);
        editItemUrlPanel.add(new JLabel("URL:"));
        editItemUrlTextField = new JTextField();
        editItemUrlTextField.setPreferredSize(new Dimension(300, 30));
        editItemUrlPanel.add(editItemUrlTextField);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();

        if (obj == generateButton){
            Main.generateRewards(getGoldValue());
        }
        else if (obj == settingsButton){
            mainPage.setVisible(false);
            itemPage.setVisible(true);
        }
        else if (obj == closeSettingsButton){
            itemPage.setVisible(false);
            mainPage.setVisible(true);
        }
        else if (obj == showAddCategoryButton){
            itemPage.setVisible(false);
            addCategoryPage.setVisible(true);
        }
        else if (obj == closeAddCategoryButton){
            addCategoryPage.setVisible(false);
            itemPage.setVisible(true);
        }
        else if (obj == addCategoryButton){
            if(!addCategoryNameTextField.getText().isEmpty() && !addCategoryWeightTextField.getText().isEmpty()){
                addCategoryPage.setVisible(false);
                Main.getCategories().add(new ItemCategory(addCategoryNameTextField.getText(), addCategoryWeightTextField.getText()));
                Main.save();
                displayItems();
                addCategoryNameTextField.setText(null);
                addCategoryWeightTextField.setText(null);
                itemPage.setVisible(true);
            }
        }
        else if(obj == collapseAllButton){
            for (ItemCategory category : Main.getCategories()){
                category.collapse();
            }
        }
        else if (obj == expandAllButton){
            for (ItemCategory category : Main.getCategories()){
                category.expand();
            }
        }
        else if (obj == closeEditCategoryButton){
            editCategoryPage.setVisible(false);
            category.setWeight(Integer.parseInt(editCategoryWeightTextField.getText().replace(".", "")));
            Main.getCategoryProperties().setProperty(category.getId(), editCategoryNameTextField.getText());
            Main.save();
            displayItems();
            itemPage.setVisible(true);
        }
        else if (obj == deleteCategoryButton){
            editCategoryPage.setVisible(false);
            Main.getCategories().remove(category);
            Main.getCategoryProperties().removeProperty(category.getId());
            for (Item item : category.getItems()){
                Main.getItemProperties().removeProperty(item.getId());
            }
            category = null;
            Main.save();
            displayItems();
            itemPage.setVisible(true);
        }
        else if (obj == addItemButton){
            if (!addiItemNameTextField.getText().isEmpty() && !addItemPriceTextField.getText().isEmpty()){
                addItemPage.setVisible(false);
                category.add(new Item(addiItemNameTextField.getText(), addItemPriceTextField.getText(), category, (Item.Rarity)addItemRarityComboBox.getSelectedItem(), addItemUrlTextField.getText()));
                Main.save();
                displayItems();
                addiItemNameTextField.setText(null);
                addItemPriceTextField.setText(null);
                addItemUrlTextField.setText(null);
                itemPage.setVisible(true);
            }

        }
        else if (obj == closeAddItemButton){
            addItemPage.setVisible(false);
            itemPage.setVisible(true);
        }
        else if (obj == closeEditItemButton){
            editItemPage.setVisible(false);
            item.setName(editItemNameTextField.getText());
            item.setPrice(editItemPriceTextField.getText());
            item.setRarity((Item.Rarity) editItemRarityComboBox.getSelectedItem());
            item.setUrl(editItemUrlTextField.getText());
            item.getCategory().getItems().sort(Comparator.comparing(Item::getName));
            item.getCategory().getItems().sort((o1, o2) -> Integer.compare(o2.getPrice(), o1.getPrice()));
            Main.save();
            if (searchTextField.getText().isEmpty()){
                displayItems();
            }
            else {
                search();
            }
            itemPage.setVisible(true);
        }
        else if (obj == deleteItemButton){
            editItemPage.setVisible(false);
            item.getCategory().getItems().remove(item);
            Main.getItemProperties().removeProperty(item.getId());
            item = null;
            Main.save();
            displayItems();
            itemPage.setVisible(true);
        }
        else if (obj == searchButton){
            search();
        }
        else if (obj.getClass() == CustomCheckBox.class){
            if (((CustomCheckBox) obj).isSelected()){
                if (((CustomCheckBox) obj).isItem()){
                    ((CustomCheckBox) obj).getItem().setActive(true);
                }
                else {
                    ((CustomCheckBox) obj).getItemCategory().setActive(true);
                }
                Main.save();
            }
            else {
                if (((CustomCheckBox) obj).isItem()){
                    ((CustomCheckBox) obj).getItem().setActive(false);
                }
                else {
                    ((CustomCheckBox) obj).getItemCategory().setActive(false);
                }
                Main.save();
            }
        }
        else if (obj.getClass() == CustomButton.class){
            if (((CustomButton) obj).isItem()){
                itemPage.setVisible(false);
                item = ((CustomButton) obj).getItem();
                JLabel editItemLabel = createUrlLabel(item.getName(), item);
                editItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
                editItemHeaderNamePanel.removeAll();
                editItemHeaderNamePanel.add(editItemLabel);
                editItemNameTextField.setText(item.getName());
                editItemPriceTextField.setValue(item.getPrice());
                editItemRarityComboBox.setSelectedItem(item.getRarity());
                editItemUrlTextField.setText(item.getUrlString());
                editItemPage.revalidate();
                editItemPage.repaint();
                editItemPage.setVisible(true);
            }
            else {
                if (((CustomButton) obj).getText().equals(Main.getGuiBundle().getString("add"))){
                    itemPage.setVisible(false);
                    category = ((CustomButton) obj).getItemCategory();
                    addItemPage.setVisible(true);
                }
                else {
                    itemPage.setVisible(false);
                    category = ((CustomButton) obj).getItemCategory();
                    editCategoryLabel.setText(category.getName());
                    editCategoryNameTextField.setText(category.getName());
                    editCategoryWeightTextField.setValue(category.getWeight());
                    editCategoryPage.setVisible(true);
                }
            }

        }
    }

    public void search(){
        if (!searchTextField.getText().isEmpty()){
            for (ItemCategory category : Main.getCategories()){
                for (Item item : category.getItems()){
                    if (item.getName().toLowerCase().contains(searchTextField.getText().toLowerCase())){
                        category.getFilteredItems().add(item);
                    }
                }
            }
            displayFilteredItems();
        }
        else {
            displayItems();
        }
    }

    public void displayItems(){
        itemPanel.removeAll();

        for (ItemCategory category : Main.getCategories()){
            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BorderLayout());
            category.setPanel(categoryPanel);
            itemPanel.add(categoryPanel);
            itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            JPanel categoryHeaderPanel = new JPanel();
            categoryHeaderPanel.setLayout(new BorderLayout());
            categoryHeaderPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            categoryHeaderPanel.add(new JLabel(" " + category.getName()), BorderLayout.WEST);
            categoryPanel.add(categoryHeaderPanel, BorderLayout.NORTH);

            JPanel categoryOptionsPanel = new JPanel();
            categoryOptionsPanel.add(new JLabel(Main.getGuiBundle().getString("active")));
            categoryOptionsPanel.add(new CustomCheckBox(category, this));
            categoryOptionsPanel.add(new CustomButton(Main.getGuiBundle().getString("add"), category, this));
            categoryOptionsPanel.add(new CustomButton(new ImageIcon("icons/settings.png"), category, this));
            categoryHeaderPanel.add(categoryOptionsPanel, BorderLayout.EAST);

            JPanel categoryContentPanel = new JPanel();
            if (category.isCollapsed()){
                categoryContentPanel.setVisible(false);
            }
            categoryContentPanel.setLayout(new BoxLayout(categoryContentPanel, BoxLayout.PAGE_AXIS));
            categoryContentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            for (Item item : category.getItems()){
                JPanel jPanel = new JPanel(new GridLayout());
                jPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                categoryContentPanel.add(jPanel);
                jPanel.add(createUrlLabel(" " + item.getName(), item));

                JPanel rightPanel = new JPanel(new BorderLayout());
                jPanel.add(rightPanel);
                rightPanel.add(new JLabel(item.getPrice() + " GP"));

                JPanel optionsPanel = new JPanel();
                rightPanel.add(optionsPanel, BorderLayout.EAST);
                optionsPanel.add(new JLabel(Main.getGuiBundle().getString("active")));
                optionsPanel.add(new CustomCheckBox(item, this));
                optionsPanel.add(new CustomButton(new ImageIcon("icons/settings.png"), item, this));
            }
            categoryPanel.add(categoryContentPanel, BorderLayout.CENTER);

            categoryHeaderPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    if (category.isCollapsed()){
                        category.expand();
                    }
                    else {
                        category.collapse();
                    }
                }
            });
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    public void displayFilteredItems(){
        itemPanel.removeAll();

        for (ItemCategory category : Main.getCategories()){
            if (!category.getFilteredItems().isEmpty()){
                JPanel categoryPanel = new JPanel();
                categoryPanel.setLayout(new BorderLayout());
                category.setPanel(categoryPanel);
                itemPanel.add(categoryPanel);
                itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));

                JPanel categoryHeaderPanel = new JPanel();
                categoryHeaderPanel.setLayout(new BorderLayout());
                categoryHeaderPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                categoryHeaderPanel.add(new JLabel(" " + category.getName()), BorderLayout.WEST);
                categoryPanel.add(categoryHeaderPanel, BorderLayout.NORTH);

                JPanel categoryOptionsPanel = new JPanel();
                categoryOptionsPanel.add(new JLabel(Main.getGuiBundle().getString("active")));
                categoryOptionsPanel.add(new CustomCheckBox(category, this));
                categoryOptionsPanel.add(new CustomButton(Main.getGuiBundle().getString("add"), category, this));
                categoryOptionsPanel.add(new CustomButton(new ImageIcon("icons/settings.png"), category, this));
                categoryHeaderPanel.add(categoryOptionsPanel, BorderLayout.EAST);

                JPanel categoryContentPanel = new JPanel();
                if (category.isCollapsed()){
                    categoryContentPanel.setVisible(false);
                }
                categoryContentPanel.setLayout(new BoxLayout(categoryContentPanel, BoxLayout.PAGE_AXIS));
                categoryContentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                for (Item item : category.getItems()){
                    if (category.getFilteredItems().contains(item)){
                        JPanel jPanel = new JPanel(new GridLayout());
                        jPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        categoryContentPanel.add(jPanel);
                        jPanel.add(createUrlLabel(" " + item.getName(), item));

                        JPanel rightPanel = new JPanel(new BorderLayout());
                        jPanel.add(rightPanel);
                        rightPanel.add(new JLabel(item.getPrice() + " GP"));

                        JPanel optionsPanel = new JPanel();
                        rightPanel.add(optionsPanel, BorderLayout.EAST);
                        optionsPanel.add(new JLabel(Main.getGuiBundle().getString("active")));
                        optionsPanel.add(new CustomCheckBox(item, this));
                        optionsPanel.add(new CustomButton(new ImageIcon("icons/settings.png"), item, this));
                    }
                }
                categoryPanel.add(categoryContentPanel, BorderLayout.CENTER);

                categoryHeaderPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent m) {
                        if (category.isCollapsed()){
                            category.expand();
                        }
                        else {
                            category.collapse();
                        }
                    }
                });
                category.getFilteredItems().clear();
            }
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    public JPanel getMoneyPanel(){
        return moneyPanel;
    }

    public JPanel getMainPageBodyPanel(){
        return mainPageBodyPanel;
    }

    public int getGoldValue(){
        if (!goldValueTextField.getText().isEmpty()){
            return Integer.parseInt(goldValueTextField.getText().replace(".", ""));
        }
        else {
            return 0;
        }
    }

    public void displayRewards(ArrayList<Item> items){
        mainPageBodyPanel.removeAll();
        items.sort(Comparator.comparing(Item::getName));
        items.sort((o1, o2) -> Integer.compare(o2.getPrice(), o1.getPrice()));
        for (Item item : items){
            JPanel panel = new JPanel(new GridLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            mainPageBodyPanel.add(panel);
            panel.add(createUrlLabel(" " + item.getAmount() + " x " + item.getName(), item));

            JPanel rightPanel = new JPanel(new BorderLayout());
            panel.add(rightPanel);
            rightPanel.add(new JLabel(item.getPrice() + " GP"));

            JPanel optionsPanel = new JPanel();
            rightPanel.add(optionsPanel, BorderLayout.EAST);
            optionsPanel.add(new JLabel(Main.getGuiBundle().getString("active")));
            optionsPanel.add(new CustomCheckBox(item, _ -> {
                item.setActive(!item.isActive());
                Main.save();
            }));
            CustomButton reRollButton = new CustomButton(new ImageIcon("icons/dice.png"), item, _ -> Main.reRoll(item, items));
            reRollButton.setToolTipText(Main.getGuiBundle().getString("reroll"));
            optionsPanel.add(reRollButton);
        }
    }

    public void displayMoney(int platinum, int gold, int silver, int copper){
        moneyPanel.removeAll();
        if (platinum != 0){
            JLabel label = new JLabel(platinum + " PP", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            label.setForeground(Color.gray);
            moneyPanel.add(label);
        }
        if (gold != 0){
            JLabel label = new JLabel(gold + " GP", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            label.setForeground(new Color(205, 165, 0));
            moneyPanel.add(label);
        }
        if (silver != 0){
            JLabel label = new JLabel(silver + " SP", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            label.setForeground(Color.gray);
            moneyPanel.add(label);
        }
        if (copper != 0){
            JLabel label = new JLabel(copper + " CP", SwingConstants.CENTER);
            label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            label.setForeground(new Color(184, 115, 51));
            moneyPanel.add(label);
        }
        moneyPanel.revalidate();
        moneyPanel.repaint();
    }

    public JLabel createUrlLabel(String text, Item item){
        JLabel label = new JLabel(text);
        label.setForeground(rarityColor(item.getRarity()));
        if (!item.getUrlString().isEmpty()){
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent m) {
                    if (Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)){
                            try {
                                desktop.browse(item.getUrl().toURI());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent m){
                    //label.setForeground(new Color(label.getForeground().getRed() + 55, label.getForeground().getGreen() + 55, label.getForeground().getBlue() + 55));
                    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), label.getFont().getSize() + 1));

                }

                @Override
                public void mouseExited(MouseEvent m){
                    //label.setForeground(rarityColor(item.getRarity()));
                    label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), label.getFont().getSize() - 1));
                }
            });
        }
        return label;
    }

    private Color rarityColor(Item.Rarity rarity){
        Color color = new Color(51, 51 , 51);
        if (rarity != null){
            switch (rarity){
                case UNCOMMON -> color = new Color(100, 200, 100);
                case RARE -> color = new Color(100, 100, 200);
                case VERY_RARE -> color = new Color(200, 100, 200);
                case LEGENDARY -> color = new Color(200, 150, 50);
                case ARTIFACT -> color = new Color(200, 100, 100);
            }
        }
        return color;
    }

    private void updateI18nComponents(){
        goldValueLabel.setText(Main.getGuiBundle().getString("gold_value") + ":");
        generateButton.setText(Main.getGuiBundle().getString("generate_loot"));
        showAddCategoryButton.setText(Main.getGuiBundle().getString("add_category"));
        addCategoryButton.setText(Main.getGuiBundle().getString("create_new_category"));
        addCategoryNameLabel.setText(Main.getGuiBundle().getString("name") + ":");
        addCategoryWeightLabel.setText(Main.getGuiBundle().getString("weight") + ":");
        editCategoryNameLabel.setText(Main.getGuiBundle().getString("name") + ":");
        editCategoryWeightLabel.setText(Main.getGuiBundle().getString("weight") + ":");
        addItemButton.setText(Main.getGuiBundle().getString("add_new_item"));
        addItemNameLabel.setText(Main.getGuiBundle().getString("name") + ":");
        addItemPriceLabel.setText(Main.getGuiBundle().getString("price") + ":");
        addItemRarityLabel.setText(Main.getGuiBundle().getString("rarity") + ":");
        editItemNameLabel.setText(Main.getGuiBundle().getString("name") + ":");
        editItemPriceLabel.setText(Main.getGuiBundle().getString("price") + ":");
        editItemRarityLabel.setText(Main.getGuiBundle().getString("rarity") + ":");
        displayItems();
    }
}
