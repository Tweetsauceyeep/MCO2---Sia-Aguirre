import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel for Trainers management
 */
public class TrainersPanel extends JPanel {
    private PokemonController controller;
    
    // Components
    private JTable trainersTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextArea detailsArea;
    private JButton addButton, searchButton, operationsButton;
    
    public TrainersPanel(PokemonController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        loadTrainersData();
    }
    
    private void initializeComponents() {
        // Create table model
        String[] columnNames = {"ID", "Name", "Hometown", "Money", "Lineup", "Storage", "Items"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        trainersTable = new JTable(tableModel);
        trainersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trainersTable.getTableHeader().setReorderingAllowed(false);
        
        // Create components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Trainer");
        operationsButton = new JButton("Trainer Operations");
        
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
        topPanel.add(operationsButton);
        
        // Create center panel with table and details
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Add table with scroll pane
        JScrollPane tableScrollPane = new JScrollPane(trainersTable);
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
        searchButton.addActionListener(e -> searchTrainers());
        
        // Add button
        addButton.addActionListener(e -> showAddTrainerDialog());
        
        // Operations button
        operationsButton.addActionListener(e -> showTrainerOperations());
        
        // Table selection listener
        trainersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailsArea();
            }
        });
        
        // Search field enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchTrainers();
                }
            }
        });
    }
    
    private void loadTrainersData() {
        tableModel.setRowCount(0);
        List<Trainer> trainersList = controller.getAllTrainers();
        
        for (Trainer trainer : trainersList) {
            Object[] row = {
                trainer.getTrainerID(),
                trainer.getName(),
                trainer.getHometown(),
                "₽" + trainer.getMoney(),
                trainer.getLineupCount() + "/6",
                trainer.getStorageCount(),
                trainer.getTotalItemCount()
            };
            tableModel.addRow(row);
        }
    }
    
    private void searchTrainers() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadTrainersData();
            return;
        }
        
        List<Trainer> results = controller.searchTrainers(query);
        tableModel.setRowCount(0);
        
        for (Trainer trainer : results) {
            Object[] row = {
                trainer.getTrainerID(),
                trainer.getName(),
                trainer.getHometown(),
                "₽" + trainer.getMoney(),
                trainer.getLineupCount() + "/6",
                trainer.getStorageCount(),
                trainer.getTotalItemCount()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddTrainerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Trainer", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form fields
        JTextField nameField = new JTextField(15);
        JTextField birthdateField = new JTextField(10);
        JComboBox<String> sexCombo = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField hometownField = new JTextField(15);
        JTextField descriptionField = new JTextField(30);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Birthdate (DD/MM/YYYY):"), gbc);
        gbc.gridx = 1;
        formPanel.add(birthdateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Sex:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sexCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Hometown:"), gbc);
        gbc.gridx = 1;
        formPanel.add(hometownField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String birthdate = birthdateField.getText().trim();
            String sex = (String) sexCombo.getSelectedItem();
            String hometown = hometownField.getText().trim();
            String description = descriptionField.getText().trim();
            
            if (name.isEmpty() || birthdate.isEmpty() || hometown.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = controller.addTrainer(name, birthdate, sex, hometown, description);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Trainer added successfully!");
                loadTrainersData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error adding trainer!", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void showTrainerOperations() {
        int selectedRow = trainersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trainer first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int trainerID = (Integer) tableModel.getValueAt(selectedRow, 0);
        List<Trainer> trainers = controller.getAllTrainers();
        Trainer selectedTrainer = null;
        
        for (Trainer trainer : trainers) {
            if (trainer.getTrainerID() == trainerID) {
                selectedTrainer = trainer;
                break;
            }
        }
        
        if (selectedTrainer != null) {
            showTrainerOperationsDialog(selectedTrainer);
        }
    }
    
    private void showTrainerOperationsDialog(Trainer trainer) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                    "Trainer Operations: " + trainer.getName(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Lineup tab
        JPanel lineupPanel = createLineupPanel(trainer);
        tabbedPane.addTab("Lineup", lineupPanel);
        
        // Storage tab
        JPanel storagePanel = createStoragePanel(trainer);
        tabbedPane.addTab("Storage", storagePanel);
        
        // Items tab
        JPanel itemsPanel = createItemsPanel(trainer);
        tabbedPane.addTab("Items", itemsPanel);
        
        // Operations tab
        JScrollPane operationsPanel = createOperationsPanel(trainer);
        tabbedPane.addTab("Operations", operationsPanel);
        
        dialog.add(tabbedPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            dialog.dispose();
            loadTrainersData(); // Refresh the main table
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel createLineupPanel(Trainer trainer) {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"#", "Name", "Type", "Level", "HP", "Attack", "Defense", "Speed"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // Load lineup data
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            Object[] row = {
                i + 1,
                pokemon.getName(),
                pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""),
                pokemon.getCurrentLevel(),
                pokemon.getCurrentHP(),
                pokemon.getCurrentAttack(),
                pokemon.getCurrentDefense(),
                pokemon.getCurrentSpeed()
            };
            model.addRow(row);
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton addPokemonButton = new JButton("Add Pokémon");
        JButton teachMoveButton = new JButton("Teach Move");
        JButton useItemButton = new JButton("Use Item");
        
        addPokemonButton.addActionListener(e -> showAddPokemonToLineupDialog(trainer));
        teachMoveButton.addActionListener(e -> showTeachMoveDialog(trainer));
        useItemButton.addActionListener(e -> showUseItemDialog(trainer));
        
        buttonPanel.add(addPokemonButton);
        buttonPanel.add(teachMoveButton);
        buttonPanel.add(useItemButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStoragePanel(Trainer trainer) {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"#", "Name", "Type", "Level", "HP", "Attack", "Defense", "Speed"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // Load storage data
        for (int i = 0; i < trainer.getStorageCount(); i++) {
            Pokemon pokemon = trainer.getStorage()[i];
            Object[] row = {
                i + 1,
                pokemon.getName(),
                pokemon.getType1() + (pokemon.getType2() != null ? "/" + pokemon.getType2() : ""),
                pokemon.getCurrentLevel(),
                pokemon.getCurrentHP(),
                pokemon.getCurrentAttack(),
                pokemon.getCurrentDefense(),
                pokemon.getCurrentSpeed()
            };
            model.addRow(row);
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton addPokemonButton = new JButton("Add Pokémon");
        JButton swapButton = new JButton("Swap with Lineup");
        
        addPokemonButton.addActionListener(e -> showAddPokemonToStorageDialog(trainer));
        swapButton.addActionListener(e -> showSwapPokemonDialog(trainer));
        
        buttonPanel.add(addPokemonButton);
        buttonPanel.add(swapButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createItemsPanel(Trainer trainer) {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Name", "Category", "Quantity", "Buy Price", "Sell Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // Load items data
        for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
            Item item = trainer.getUniqueItems()[i];
            int quantity = trainer.getItemQuantities()[i];
            Object[] row = {
                item.getName(),
                item.getCategory(),
                quantity,
                item.getBuyingPrice() == 0 ? "Not sold" : "₽" + item.getBuyingPrice(),
                "₽" + item.getSellingPrice()
            };
            model.addRow(row);
        }
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton buyItemButton = new JButton("Buy Item");
        JButton sellItemButton = new JButton("Sell Item");
        
        buyItemButton.addActionListener(e -> showBuyItemDialog(trainer));
        sellItemButton.addActionListener(e -> showSellItemDialog(trainer));
        
        buttonPanel.add(buyItemButton);
        buttonPanel.add(sellItemButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JScrollPane createOperationsPanel(Trainer trainer) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel infoLabel = new JLabel("Trainer: " + trainer.getName() + " | Money: ₽" + trainer.getMoney());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(infoLabel);
        
        JButton viewDetailsButton = new JButton("View Trainer Details");
        JButton buyItemButton = new JButton("Buy Item");
        JButton useItemButton = new JButton("Use Item on Pokémon");
        JButton addPokemonButton = new JButton("Add Pokémon to Lineup");
        JButton teachMoveButton = new JButton("Teach Move to Pokémon");
        JButton swapPokemonButton = new JButton("Swap Pokémon (Storage ↔ Lineup)");
        JButton releasePokemonButton = new JButton("Release Pokémon");
        
        viewDetailsButton.addActionListener(e -> showTrainerDetails(trainer));
        buyItemButton.addActionListener(e -> showBuyItemDialog(trainer));
        useItemButton.addActionListener(e -> showUseItemDialog(trainer));
        addPokemonButton.addActionListener(e -> showAddPokemonToLineupDialog(trainer));
        teachMoveButton.addActionListener(e -> showTeachMoveDialog(trainer));
        swapPokemonButton.addActionListener(e -> showSwapPokemonDialog(trainer));
        releasePokemonButton.addActionListener(e -> showReleasePokemonDialog(trainer));
        
        panel.add(viewDetailsButton);
        panel.add(buyItemButton);
        panel.add(useItemButton);
        panel.add(addPokemonButton);
        panel.add(teachMoveButton);
        panel.add(swapPokemonButton);
        panel.add(releasePokemonButton);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        return scrollPane;
    }
    
    // Helper methods for trainer operations
    private void showAddPokemonToLineupDialog(Trainer trainer) {
        List<Pokemon> availablePokemon = controller.getAllPokemon();
        String[] options = availablePokemon.stream()
                                         .map(p -> p.getName() + " (#" + p.getPokedexNumber() + ")")
                                         .toArray(String[]::new);
        
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Select Pokémon to add to lineup:", "Add Pokémon to Lineup",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            String pokemonName = selected.split(" \\(")[0];
            Pokemon pokemon = controller.findPokemonByName(pokemonName);
            if (pokemon != null) {
                boolean success = trainer.addPokemonToLineup(pokemon);
                if (success) {
                    JOptionPane.showMessageDialog(this, pokemonName + " added to lineup!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lineup is full!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void showAddPokemonToStorageDialog(Trainer trainer) {
        List<Pokemon> availablePokemon = controller.getAllPokemon();
        String[] options = availablePokemon.stream()
                                         .map(p -> p.getName() + " (#" + p.getPokedexNumber() + ")")
                                         .toArray(String[]::new);
        
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Select Pokémon to add to storage:", "Add Pokémon to Storage",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            String pokemonName = selected.split(" \\(")[0];
            Pokemon pokemon = controller.findPokemonByName(pokemonName);
            if (pokemon != null) {
                boolean success = trainer.addPokemonToStorage(pokemon);
                if (success) {
                    JOptionPane.showMessageDialog(this, pokemonName + " added to storage!");
                } else {
                    JOptionPane.showMessageDialog(this, "Storage is full!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void showBuyItemDialog(Trainer trainer) {
        List<Item> availableItems = controller.getAllItems();
        String[] options = availableItems.stream()
                                       .filter(item -> item.getBuyingPrice() > 0)
                                       .map(item -> item.getName() + " - ₽" + item.getBuyingPrice())
                                       .toArray(String[]::new);
        
        if (options.length == 0) {
            JOptionPane.showMessageDialog(this, "No items available for purchase!", "No Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Select item to buy:", "Buy Item",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            String itemName = selected.split(" - ")[0];
            Item item = controller.findItemByName(itemName);
            if (item != null) {
                String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity to buy:");
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    boolean success = trainer.buyItem(item, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Successfully bought " + quantity + " " + itemName + "(s)!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void showSellItemDialog(Trainer trainer) {
        if (trainer.getUniqueItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in inventory!", "No Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] options = new String[trainer.getUniqueItemCount()];
        for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
            Item item = trainer.getUniqueItems()[i];
            int quantity = trainer.getItemQuantities()[i];
            options[i] = item.getName() + " x" + quantity + " - ₽" + item.getSellingPrice();
        }
        
        String selected = (String) JOptionPane.showInputDialog(this, 
            "Select item to sell:", "Sell Item",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            String itemName = selected.split(" x")[0];
            Item item = controller.findItemByName(itemName);
            if (item != null) {
                String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity to sell:");
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    boolean success = trainer.sellItem(item, quantity);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Successfully sold " + quantity + " " + itemName + "(s)!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void showUseItemDialog(Trainer trainer) {
        if (trainer.getUniqueItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in inventory!", "No Items", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (trainer.getLineupCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Pokémon in lineup to use items on!", "No Pokémon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Select item
        String[] itemOptions = new String[trainer.getUniqueItemCount()];
        for (int i = 0; i < trainer.getUniqueItemCount(); i++) {
            Item item = trainer.getUniqueItems()[i];
            int quantity = trainer.getItemQuantities()[i];
            itemOptions[i] = item.getName() + " x" + quantity;
        }
        
        String selectedItem = (String) JOptionPane.showInputDialog(this, 
            "Select item to use:", "Use Item",
            JOptionPane.QUESTION_MESSAGE, null, itemOptions, itemOptions[0]);
        
        if (selectedItem != null) {
            String itemName = selectedItem.split(" x")[0];
            Item item = controller.findItemByName(itemName);
            
            if (item != null) {
                // Select Pokémon
                String[] pokemonOptions = new String[trainer.getLineupCount()];
                for (int i = 0; i < trainer.getLineupCount(); i++) {
                    Pokemon pokemon = trainer.getLineup()[i];
                    pokemonOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
                }
                
                String selectedPokemon = (String) JOptionPane.showInputDialog(this, 
                    "Select Pokémon to use item on:", "Select Pokémon",
                    JOptionPane.QUESTION_MESSAGE, null, pokemonOptions, pokemonOptions[0]);
                
                if (selectedPokemon != null) {
                    String pokemonName = selectedPokemon.split(" \\(")[0];
                    Pokemon pokemon = null;
                    for (int i = 0; i < trainer.getLineupCount(); i++) {
                        if (trainer.getLineup()[i].getName().equals(pokemonName)) {
                            pokemon = trainer.getLineup()[i];
                            break;
                        }
                    }
                    
                    if (pokemon != null) {
                        boolean success = trainer.useItem(item, pokemon);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Successfully used " + itemName + " on " + pokemonName + "!");
                        }
                    }
                }
            }
        }
    }
    
    private void showTeachMoveDialog(Trainer trainer) {
        if (trainer.getLineupCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Pokémon in lineup!", "No Pokémon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Select Pokémon
        String[] pokemonOptions = new String[trainer.getLineupCount()];
        for (int i = 0; i < trainer.getLineupCount(); i++) {
            Pokemon pokemon = trainer.getLineup()[i];
            pokemonOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
        }
        
        String selectedPokemon = (String) JOptionPane.showInputDialog(this, 
            "Select Pokémon to teach move to:", "Select Pokémon",
            JOptionPane.QUESTION_MESSAGE, null, pokemonOptions, pokemonOptions[0]);
        
        if (selectedPokemon != null) {
            String pokemonName = selectedPokemon.split(" \\(")[0];
            int pokemonIndex = -1;
            for (int i = 0; i < trainer.getLineupCount(); i++) {
                if (trainer.getLineup()[i].getName().equals(pokemonName)) {
                    pokemonIndex = i;
                    break;
                }
            }
            
            if (pokemonIndex != -1) {
                // Select move
                List<Move> availableMoves = controller.getAllMoves();
                String[] moveOptions = availableMoves.stream()
                                                   .map(m -> m.getName() + " (" + m.getClassification() + ")")
                                                   .toArray(String[]::new);
                
                String selectedMove = (String) JOptionPane.showInputDialog(this, 
                    "Select move to teach:", "Select Move",
                    JOptionPane.QUESTION_MESSAGE, null, moveOptions, moveOptions[0]);
                
                if (selectedMove != null) {
                    String moveName = selectedMove.split(" \\(")[0];
                    Move move = null;
                    for (Move m : availableMoves) {
                        if (m.getName().equals(moveName)) {
                            move = m;
                            break;
                        }
                    }
                    
                    if (move != null) {
                        boolean success = trainer.teachMove(pokemonIndex, move, -1);
                        if (success) {
                            JOptionPane.showMessageDialog(this, pokemonName + " learned " + moveName + "!");
                        } else {
                            JOptionPane.showMessageDialog(this, pokemonName + " cannot learn " + moveName + "!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }
    
    private void showSwapPokemonDialog(Trainer trainer) {
        if (trainer.getStorageCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Pokémon in storage to swap!", "No Storage Pokémon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (trainer.getLineupCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Pokémon in lineup to swap!", "No Lineup Pokémon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Select storage Pokémon
        String[] storageOptions = new String[trainer.getStorageCount()];
        for (int i = 0; i < trainer.getStorageCount(); i++) {
            Pokemon pokemon = trainer.getStorage()[i];
            storageOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
        }
        
        String selectedStorage = (String) JOptionPane.showInputDialog(this, 
            "Select Pokémon from storage:", "Select Storage Pokémon",
            JOptionPane.QUESTION_MESSAGE, null, storageOptions, storageOptions[0]);
        
        if (selectedStorage != null) {
            String storagePokemonName = selectedStorage.split(" \\(")[0];
            int storageIndex = -1;
            for (int i = 0; i < trainer.getStorageCount(); i++) {
                if (trainer.getStorage()[i].getName().equals(storagePokemonName)) {
                    storageIndex = i;
                    break;
                }
            }
            
            if (storageIndex != -1) {
                // Select lineup Pokémon
                String[] lineupOptions = new String[trainer.getLineupCount()];
                for (int i = 0; i < trainer.getLineupCount(); i++) {
                    Pokemon pokemon = trainer.getLineup()[i];
                    lineupOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
                }
                
                String selectedLineup = (String) JOptionPane.showInputDialog(this, 
                    "Select Pokémon from lineup to swap:", "Select Lineup Pokémon",
                    JOptionPane.QUESTION_MESSAGE, null, lineupOptions, lineupOptions[0]);
                
                if (selectedLineup != null) {
                    String lineupPokemonName = selectedLineup.split(" \\(")[0];
                    int lineupIndex = -1;
                    for (int i = 0; i < trainer.getLineupCount(); i++) {
                        if (trainer.getLineup()[i].getName().equals(lineupPokemonName)) {
                            lineupIndex = i;
                            break;
                        }
                    }
                    
                    if (lineupIndex != -1) {
                        boolean success = trainer.switchPokemonFromStorage(storageIndex, lineupIndex);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "Successfully swapped Pokémon!");
                        }
                    }
                }
            }
        }
    }
    
    private void showReleasePokemonDialog(Trainer trainer) {
        String[] options = {"Release from Lineup", "Release from Storage"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Select where to release Pokémon from:", "Release Pokémon",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice == 0) { // Lineup
            if (trainer.getLineupCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Pokémon in lineup!", "No Pokémon", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] lineupOptions = new String[trainer.getLineupCount()];
            for (int i = 0; i < trainer.getLineupCount(); i++) {
                Pokemon pokemon = trainer.getLineup()[i];
                lineupOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
            }
            
            String selected = (String) JOptionPane.showInputDialog(this, 
                "Select Pokémon to release from lineup:", "Release from Lineup",
                JOptionPane.QUESTION_MESSAGE, null, lineupOptions, lineupOptions[0]);
            
            if (selected != null) {
                String pokemonName = selected.split(" \\(")[0];
                int lineupIndex = -1;
                for (int i = 0; i < trainer.getLineupCount(); i++) {
                    if (trainer.getLineup()[i].getName().equals(pokemonName)) {
                        lineupIndex = i;
                        break;
                    }
                }
                
                if (lineupIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to release " + pokemonName + "?", "Confirm Release",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = trainer.releasePokemon(lineupIndex);
                        if (success) {
                            JOptionPane.showMessageDialog(this, pokemonName + " has been released!");
                        }
                    }
                }
            }
        } else if (choice == 1) { // Storage
            if (trainer.getStorageCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Pokémon in storage!", "No Pokémon", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] storageOptions = new String[trainer.getStorageCount()];
            for (int i = 0; i < trainer.getStorageCount(); i++) {
                Pokemon pokemon = trainer.getStorage()[i];
                storageOptions[i] = pokemon.getName() + " (Level " + pokemon.getCurrentLevel() + ")";
            }
            
            String selected = (String) JOptionPane.showInputDialog(this, 
                "Select Pokémon to release from storage:", "Release from Storage",
                JOptionPane.QUESTION_MESSAGE, null, storageOptions, storageOptions[0]);
            
            if (selected != null) {
                String pokemonName = selected.split(" \\(")[0];
                int storageIndex = -1;
                for (int i = 0; i < trainer.getStorageCount(); i++) {
                    if (trainer.getStorage()[i].getName().equals(pokemonName)) {
                        storageIndex = i;
                        break;
                    }
                }
                
                if (storageIndex != -1) {
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to release " + pokemonName + "?", "Confirm Release",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = trainer.releasePokemonFromStorage(storageIndex);
                        if (success) {
                            JOptionPane.showMessageDialog(this, pokemonName + " has been released from storage!");
                        }
                    }
                }
            }
        }
    }
    
    private void showTrainerDetails(Trainer trainer) {
        JTextArea detailsArea = new JTextArea(trainer.toString());
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Trainer Details: " + trainer.getName(), 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateDetailsArea() {
        int selectedRow = trainersTable.getSelectedRow();
        if (selectedRow == -1) {
            detailsArea.setText("");
            return;
        }
        
        int trainerID = (Integer) tableModel.getValueAt(selectedRow, 0);
        List<Trainer> trainers = controller.getAllTrainers();
        
        for (Trainer trainer : trainers) {
            if (trainer.getTrainerID() == trainerID) {
                detailsArea.setText(trainer.toString());
                break;
            }
        }
    }
    
    /**
     * Refresh the data in the table
     */
    public void refreshData() {
        loadTrainersData();
    }
} 