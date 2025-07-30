import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

/**
 * Panel for Moves management.
 * This class provides the user interface for managing move data,
 * including viewing, searching, adding, and saving move information.
 * 
 * The panel includes:
 * - A table displaying all moves with their basic information
 * - Search functionality to find specific moves
 * - Add button to create new move entries
 * - Save/Load buttons for CSV file operations
 * - A details area showing comprehensive move information
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class MovesPanel extends JPanel {
    /** Reference to the Controller component */
    private PokemonController controller;
    
    // Components
    /** Table displaying move data */
    private JTable movesTable;
    /** Model for the moves table */
    private DefaultTableModel tableModel;
    /** Text field for search input */
    private JTextField searchField;
    /** Text area for displaying detailed move information */
    private JTextArea detailsArea;
    /** Buttons for various operations */
    private JButton addButton, searchButton, saveButton, loadButton;
    
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
     * Constructor for the moves panel.
     * Initializes all components, sets up the layout,
     * and loads initial moves data.
     * 
     * @param controller The Controller component to use
     */
    public MovesPanel(PokemonController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setBackground(POKEDEX_BG);
        loadMovesData();
    }
    
    private void initializeComponents() {
        // Create table model
        String[] columnNames = {"Name", "Classification", "Type", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        movesTable = new JTable(tableModel);
        movesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movesTable.getTableHeader().setReorderingAllowed(false);
        movesTable.setFont(pokeFont(12f));
        movesTable.getTableHeader().setFont(pokeFont(12f));
        
        // Create components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Move");
        saveButton = new JButton("Save to CSV");
        loadButton = new JButton("Load from CSV");
        addButton.setBackground(BUTTON_RED);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(pokeFont(12f));
        searchButton.setBackground(BUTTON_YELLOW);
        searchButton.setFont(pokeFont(12f));
        searchField.setFont(pokeFont(12f));
        
        detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(pokeFont(10f));
        
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
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        
        // Create center panel with table and details
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Add table with scroll pane
        JScrollPane tableScrollPane = new JScrollPane(movesTable);
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
        searchButton.addActionListener(e -> searchMoves());
        
        // Add button
        addButton.addActionListener(e -> showAddMoveDialog());
        
        // Save button
        saveButton.addActionListener(e -> saveMovesToCSV());
        
        // Load button
        loadButton.addActionListener(e -> loadMovesFromCSV());
        
        // Table selection listener
        movesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsArea();
            }
        });
        
        // Search field enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchMoves();
                }
            }
        });
    }
    
    private void loadMovesData() {
        tableModel.setRowCount(0);
        List<Move> movesList = controller.getAllMoves();
        
        for (Move move : movesList) {
            Object[] row = {
                move.getName(),
                move.getClassification(),
                move.getType1() + (move.getType2() != null ? "/" + move.getType2() : ""),
                move.getDescription()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchMoves() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadMovesData();
            return;
        }
        
        List<Move> results = controller.searchMoves(query);
        tableModel.setRowCount(0);
        
        for (Move move : results) {
            Object[] row = {
                move.getName(),
                move.getClassification(),
                move.getType1() + (move.getType2() != null ? "/" + move.getType2() : ""),
                move.getDescription()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddMoveDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Move", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Move types for dropdown
        String[] moveTypes = {
            "Normal", "Fire", "Water", "Electric", "Grass", "Ice", 
            "Fighting", "Poison", "Ground", "Flying", "Psychic", 
            "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
        };
        
        // Form fields
        JTextField nameField = new JTextField(15);
        JTextField descriptionField = new JTextField(30);
        JComboBox<String> classificationCombo = new JComboBox<>(new String[]{"TM", "HM"});
        JComboBox<String> type1Combo = new JComboBox<>(moveTypes);
        JComboBox<String> type2Combo = new JComboBox<>(moveTypes);
        type2Combo.insertItemAt("None", 0);
        type2Combo.setSelectedIndex(0);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Classification:"), gbc);
        gbc.gridx = 1;
        formPanel.add(classificationCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Type 1:"), gbc);
        gbc.gridx = 1;
        formPanel.add(type1Combo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Type 2:"), gbc);
        gbc.gridx = 1;
        formPanel.add(type2Combo, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String classification = (String) classificationCombo.getSelectedItem();
            String type1 = (String) type1Combo.getSelectedItem();
            String type2 = (String) type2Combo.getSelectedItem();
            if ("None".equals(type2)) type2 = null;
            
            if (name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = controller.addMove(name, description, classification, type1, type2);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Move added successfully!");
                loadMovesData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error: Move already exists!", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void saveMovesToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Moves Data");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.toLowerCase().endsWith(".csv")) {
                filename += ".csv";
            }
            
            try {
                controller.saveMovesToCSV(filename);
                JOptionPane.showMessageDialog(this, "Moves data saved successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadMovesFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Moves Data");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                controller.loadMovesFromCSV(filename);
                loadMovesData();
                JOptionPane.showMessageDialog(this, "Moves data loaded successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateDetailsArea() {
        int selectedRow = movesTable.getSelectedRow();
        if (selectedRow == -1) {
            detailsArea.setText("");
            return;
        }
        
        String moveName = (String) tableModel.getValueAt(selectedRow, 0);
        List<Move> moves = controller.getAllMoves();
        
        for (Move move : moves) {
            if (move.getName().equals(moveName)) {
                detailsArea.setText(move.toString());
                break;
            }
        }
    }
    
    /**
     * Refresh the data in the table
     */
    public void refreshData() {
        loadMovesData();
    }
} 