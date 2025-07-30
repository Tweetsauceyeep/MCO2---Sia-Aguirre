import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel for Pokémon management
 */
public class PokemonPanel extends JPanel {
    private PokemonController controller;
    
    // Components
    private JTable pokemonTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextArea detailsArea;
    private JButton addButton, searchButton, saveButton, loadButton, cryButton;
    
    public PokemonPanel(PokemonController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        loadPokemonData();
    }
    
    private void initializeComponents() {
        // Create table model
        String[] columnNames = {"#", "Name", "Type", "Level", "HP", "Attack", "Defense", "Speed"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        pokemonTable = new JTable(tableModel);
        pokemonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pokemonTable.getTableHeader().setReorderingAllowed(false);
        
        // Create components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Pokémon");
        saveButton = new JButton("Save to CSV");
        loadButton = new JButton("Load from CSV");
        cryButton = new JButton("Pokémon Cry");
        
        detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
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
        topPanel.add(cryButton);
        
        // Create center panel with table and details
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Add table with scroll pane
        JScrollPane tableScrollPane = new JScrollPane(pokemonTable);
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
        searchButton.addActionListener(e -> searchPokemon());
        
        // Add button
        addButton.addActionListener(e -> showAddPokemonDialog());
        
        // Save button
        saveButton.addActionListener(e -> savePokemonToCSV());
        
        // Load button
        loadButton.addActionListener(e -> loadPokemonFromCSV());
        
        // Cry button
        cryButton.addActionListener(e -> playPokemonCry());
        
        // Table selection listener
        pokemonTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsArea();
            }
        });
        
        // Search field enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPokemon();
                }
            }
        });
    }
    
    private void loadPokemonData() {
        tableModel.setRowCount(0);
        List<Pokemon> pokemonList = controller.getAllPokemon();
        
        for (Pokemon pokemon : pokemonList) {
            Object[] row = {
                pokemon.getPokedexNumber(),
                pokemon.getName(),
                pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""),
                pokemon.getCurrentLevel(),
                pokemon.getCurrentHP(),
                pokemon.getCurrentAttack(),
                pokemon.getCurrentDefense(),
                pokemon.getCurrentSpeed()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchPokemon() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadPokemonData();
            return;
        }
        
        List<Pokemon> results = controller.searchPokemon(query);
        tableModel.setRowCount(0);
        
        for (Pokemon pokemon : results) {
            Object[] row = {
                pokemon.getPokedexNumber(),
                pokemon.getName(),
                pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""),
                pokemon.getCurrentLevel(),
                pokemon.getCurrentHP(),
                pokemon.getCurrentAttack(),
                pokemon.getCurrentDefense(),
                pokemon.getCurrentSpeed()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddPokemonDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Pokémon", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Pokémon types for dropdown
        String[] pokemonTypes = {
            "Normal", "Fire", "Water", "Electric", "Grass", "Ice", 
            "Fighting", "Poison", "Ground", "Flying", "Psychic", 
            "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"
        };
        
        // Form fields
        JTextField pokedexField = new JTextField(10);
        JTextField nameField = new JTextField(15);
        JComboBox<String> type1Combo = new JComboBox<>(pokemonTypes);
        JComboBox<String> type2Combo = new JComboBox<>(pokemonTypes);
        type2Combo.insertItemAt("None", 0);
        type2Combo.setSelectedIndex(0);
        JTextField levelField = new JTextField(5);
        JTextField evolvesFromField = new JTextField(5);
        JTextField evolvesToField = new JTextField(5);
        JTextField evolutionLevelField = new JTextField(5);
        JTextField hpField = new JTextField(5);
        JTextField attackField = new JTextField(5);
        JTextField defenseField = new JTextField(5);
        JTextField speedField = new JTextField(5);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Pokédex #:"), gbc);
        gbc.gridx = 1;
        formPanel.add(pokedexField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type 1:"), gbc);
        gbc.gridx = 1;
        formPanel.add(type1Combo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Type 2:"), gbc);
        gbc.gridx = 1;
        formPanel.add(type2Combo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Base Level:"), gbc);
        gbc.gridx = 1;
        formPanel.add(levelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Evolves From: (-1 if none)"), gbc);
        gbc.gridx = 1;
        formPanel.add(evolvesFromField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Evolves To: (-1 if none)"), gbc);
        gbc.gridx = 1;
        formPanel.add(evolvesToField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Evolution Level:"), gbc);
        gbc.gridx = 1;
        formPanel.add(evolutionLevelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Base HP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(hpField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Base Attack:"), gbc);
        gbc.gridx = 1;
        formPanel.add(attackField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 10;
        formPanel.add(new JLabel("Base Defense:"), gbc);
        gbc.gridx = 1;
        formPanel.add(defenseField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 11;
        formPanel.add(new JLabel("Base Speed:"), gbc);
        gbc.gridx = 1;
        formPanel.add(speedField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                int pokedexNumber = Integer.parseInt(pokedexField.getText());
                String name = nameField.getText();
                String type1 = (String) type1Combo.getSelectedItem();
                String type2 = (String) type2Combo.getSelectedItem();
                if ("None".equals(type2)) type2 = null;
                int baseLevel = Integer.parseInt(levelField.getText());
                int evolvesFrom = Integer.parseInt(evolvesFromField.getText());
                int evolvesTo = Integer.parseInt(evolvesToField.getText());
                int evolutionLevel = Integer.parseInt(evolutionLevelField.getText());
                int hp = Integer.parseInt(hpField.getText());
                int attack = Integer.parseInt(attackField.getText());
                int defense = Integer.parseInt(defenseField.getText());
                int speed = Integer.parseInt(speedField.getText());
                
                boolean success = controller.addPokemon(pokedexNumber, name, type1, type2,
                                                     baseLevel, evolvesFrom, evolvesTo, evolutionLevel,
                                                     hp, attack, defense, speed);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Pokémon added successfully!");
                    loadPokemonData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error: Pokémon already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for numeric fields!", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void savePokemonToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Pokémon Data");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.toLowerCase().endsWith(".csv")) {
                filename += ".csv";
            }
            
            try {
                controller.savePokemonToCSV(filename);
                JOptionPane.showMessageDialog(this, "Pokémon data saved successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadPokemonFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Pokémon Data");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                controller.loadPokemonFromCSV(filename);
                loadPokemonData();
                JOptionPane.showMessageDialog(this, "Pokémon data loaded successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void playPokemonCry() {
        int selectedRow = pokemonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Pokémon first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String pokemonName = (String) tableModel.getValueAt(selectedRow, 1);
        Pokemon pokemon = controller.findPokemonByName(pokemonName);
        
        if (pokemon != null) {
            pokemon.cry();
            JOptionPane.showMessageDialog(this, pokemonName + " makes its cry!", "Pokémon Cry", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateDetailsArea() {
        int selectedRow = pokemonTable.getSelectedRow();
        if (selectedRow == -1) {
            detailsArea.setText("");
            return;
        }
        
        String pokemonName = (String) tableModel.getValueAt(selectedRow, 1);
        Pokemon pokemon = controller.findPokemonByName(pokemonName);
        
        if (pokemon != null) {
            detailsArea.setText(pokemon.toString());
        }
    }
    
    /**
     * Refresh the data in the table
     */
    public void refreshData() {
        loadPokemonData();
    }
} 