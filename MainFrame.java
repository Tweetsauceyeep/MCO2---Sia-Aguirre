import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Main GUI frame for the Enhanced Pokédex System.
 * This class serves as the main View component in the MVC design pattern,
 * providing the primary user interface for the application.
 * 
 * The MainFrame:
 * - Creates and manages the main application window
 * - Contains a tabbed interface for different sections (Pokémon, Moves, Items, Trainers)
 * - Handles window events and application lifecycle
 * - Manages the overall layout and appearance of the application
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class MainFrame extends JFrame {
    /** Reference to the Controller component */
    private PokemonController controller;
    /** Main tabbed pane containing all sections */
    private JTabbedPane tabbedPane;
    
    // Panels for different sections
    /** Panel for Pokémon management */
    private PokemonPanel pokemonPanel;
    /** Panel for move management */
    private MovesPanel movesPanel;
    /** Panel for item management */
    private ItemsPanel itemsPanel;
    /** Panel for trainer management */
    private TrainersPanel trainersPanel;
    
    /** Background color for the Pokédex theme */
    private static final Color POKEDEX_BG = new Color(248, 248, 255);
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
     * Constructor for the main application frame.
     * Initializes the GUI components, sets up the layout,
     * and configures the window properties.
     * 
     * @param controller The Controller component to use
     */
    public MainFrame(PokemonController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        refreshAllPanels();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Enhanced Pokédex System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(POKEDEX_BG);
        tabbedPane.setBackground(POKEDEX_BG);
        tabbedPane.setFont(pokeFont(14f));
        
        // Add window listener for save on quit
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveOnQuit();
            }
        });
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Initialize panels
        pokemonPanel = new PokemonPanel(controller);
        movesPanel = new MovesPanel(controller);
        itemsPanel = new ItemsPanel(controller);
        trainersPanel = new TrainersPanel(controller);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Pokémon", new ImageIcon(), pokemonPanel, "Manage Pokémon");
        tabbedPane.addTab("Moves", new ImageIcon(), movesPanel, "Manage Moves");
        tabbedPane.addTab("Items", new ImageIcon(), itemsPanel, "Manage Items");
        tabbedPane.addTab("Trainers", new ImageIcon(), trainersPanel, "Manage Trainers");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Add tabbed pane
        add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
        
        // Add menu bar
        setJMenuBar(createMenuBar());
    }
    
    private void refreshAllPanels() {
        if (pokemonPanel != null) pokemonPanel.refreshData();
        if (movesPanel != null) movesPanel.refreshData();
        if (itemsPanel != null) itemsPanel.refreshData();
        if (trainersPanel != null) trainersPanel.refreshData();
    }
    
    private void saveOnQuit() {
        try {
            // Save current data to CSV files
            controller.savePokemonToCSV("pokemon_data.csv");
            controller.saveMovesToCSV("moves_data.csv");
            System.out.println("Data saved successfully on exit.");
        } catch (Exception e) {
            System.err.println("Failed to save data on exit: " + e.getMessage());
        }
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem saveItem = new JMenuItem("Save Data");
        saveItem.addActionListener(e -> saveOnQuit());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            saveOnQuit();
            System.exit(0);
        });
        
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Enhanced Pokédex System - GUI Version\n\n" +
            "A Java Swing implementation following MVC design pattern.\n" +
            "Features manual save functionality for data persistence.\n\n" +
            "Data is saved to CSV files when you exit the application.",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 152, 219));
        panel.setPreferredSize(new Dimension(1000, 80));
        panel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Enhanced Pokédex System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Pokémon Database: Ethan Sia, Kyla Aguirre", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(titlePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236, 240, 241));
        panel.setPreferredSize(new Dimension(1000, 30));
        panel.setLayout(new BorderLayout());
        
        JLabel statusLabel = new JLabel("Ready | Data will be saved on exit");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(statusLabel, BorderLayout.WEST);
        
        return panel;
    }
} 