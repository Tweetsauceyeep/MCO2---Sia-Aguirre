import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

/**
 * Panel for Items management.
 * This class provides the user interface for managing item data,
 * including viewing, searching, and adding item information.
 * 
 * The panel includes:
 * - A table displaying all items with their basic information
 * - Search functionality to find specific items
 * - Add button to create new item entries
 * - A details area showing comprehensive item information
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class ItemsPanel extends JPanel {
    /** Reference to the Controller component */
    private PokemonController controller;
    
    // Components
    /** Table displaying item data */
    private JTable itemsTable;
    /** Model for the items table */
    private DefaultTableModel tableModel;
    /** Text field for search input */
    private JTextField searchField;
    /** Text area for displaying detailed item information */
    private JTextArea detailsArea;
    /** Buttons for various operations */
    private JButton addButton, searchButton;
    
    /** Background color for the Pokédex theme */
    private static final Color POKEDEX_BG = new Color(248, 248, 255);
    /** Red color for buttons */
    private static final Color BUTTON_RED = new Color(255, 0, 0);
    /** Yellow color for buttons */
    private static final Color BUTTON_YELLOW = new Color(255, 213, 0);
    /**
     * Creates a Pokémon-themed font with the specified size.
     * Attempts to load the custom PressStart2P font, falls back to
     * Monospaced Bold if the custom font cannot be loaded.
     * 
     * @param size The font size to use
     * @return A Font object with the specified size
     */
    private static Font pokeFont(float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            return new Font("Monospaced", Font.BOLD, (int)size);
        }
    }
    
    /**
     * Constructor for the items panel.
     * Initializes all components, sets up the layout,
     * and loads initial items data.
     * 
     * @param controller The Controller component to use
     */
    public ItemsPanel(PokemonController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setBackground(POKEDEX_BG);
    }
    
    private void initializeComponents() {
        // Create table model
        String[] columnNames = {"Name", "Category", "Buy Price", "Sell Price", "Effect"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        itemsTable = new JTable(tableModel);
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        itemsTable.setFont(pokeFont(12f));
        itemsTable.getTableHeader().setFont(pokeFont(12f));
        
        // Create components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Item");
        
        detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(pokeFont(10f));
        
        // Add components to form
        addButton.setBackground(BUTTON_RED);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(pokeFont(12f));
        searchButton.setBackground(BUTTON_YELLOW);
        searchButton.setFont(pokeFont(12f));
        searchField.setFont(pokeFont(12f));
        
        // Add listeners
        addListeners();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create top panel for controls
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);
        
        // Create center panel with table and details
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Add table with scroll pane
        JScrollPane tableScrollPane = new JScrollPane(itemsTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Add details area
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setPreferredSize(new Dimension(600, 200));
        centerPanel.add(detailsScrollPane, BorderLayout.SOUTH);
        
        // Add panels to main panel
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void addListeners() {
        // Search button
        searchButton.addActionListener(e -> searchItems());
        
        // Add button
        addButton.addActionListener(e -> showAddItemDialog());
        
        // Table selection listener
        itemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsArea();
            }
        });
        
        // Search field enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchItems();
                }
            }
        });
    }
    
    private void loadItemsData() {
        tableModel.setRowCount(0);
        List<Item> itemsList = controller.getAllItems();
        
        for (Item item : itemsList) {
            Object[] row = {
                item.getName(),
                item.getCategory(),
                item.getBuyingPrice() == 0 ? "Not sold" : "₽" + item.getBuyingPrice(),
                "₽" + item.getSellingPrice(),
                item.getEffect()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchItems() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadItemsData();
            return;
        }
        
        List<Item> results = controller.searchItems(query);
        tableModel.setRowCount(0);
        
        for (Item item : results) {
            Object[] row = {
                item.getName(),
                item.getCategory(),
                item.getBuyingPrice() == 0 ? "Not sold" : "₽" + item.getBuyingPrice(),
                "₽" + item.getSellingPrice(),
                item.getEffect()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddItemDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Item", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JTextField nameField = new JTextField(15);
        JTextField categoryField = new JTextField(15);
        JTextField descriptionField = new JTextField(30);
        JTextField effectField = new JTextField(30);
        JTextField buyPriceField = new JTextField(10);
        JTextField sellPriceField = new JTextField(10);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(categoryField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Effect:"), gbc);
        gbc.gridx = 1;
        formPanel.add(effectField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Buy Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(buyPriceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Sell Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sellPriceField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                String description = descriptionField.getText().trim();
                String effect = effectField.getText().trim();
                int buyPrice = Integer.parseInt(buyPriceField.getText().trim());
                int sellPrice = Integer.parseInt(sellPriceField.getText().trim());
                
                if (name.isEmpty() || category.isEmpty() || description.isEmpty() || effect.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = controller.addItem(name, category, description, effect, buyPrice, sellPrice);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Item added successfully!");
                    loadItemsData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error: Item already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for prices!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void updateDetailsArea() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow == -1) {
            detailsArea.setText("");
            return;
        }
        
        String itemName = (String) tableModel.getValueAt(selectedRow, 0);
        List<Item> items = controller.getAllItems();
        
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                detailsArea.setText(item.toString());
                break;
            }
        }
    }
    
    /**
     * Refresh the data in the table
     */
    public void refreshData() {
        loadItemsData();
    }
} 