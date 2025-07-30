import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class for the Enhanced Pokédex System.
 * This class serves as the entry point for the GUI application,
 * following the MVC (Model-View-Controller) design pattern.
 * 
 * The GUI class:
 * - Sets up the system look and feel
 * - Initializes the MVC components (Model, Controller, View)
 * - Launches the main application window
 * - Ensures proper GUI initialization on the Event Dispatch Thread
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class PokemonGUI {
    /** Reference to the Model component */
    private static PokemonModel model;
    /** Reference to the Controller component */
    private static PokemonController controller;
    /** Reference to the View component (MainFrame) */
    private static MainFrame view;
    
    /**
     * Main method that serves as the entry point for the GUI application.
     * Sets up the system look and feel, initializes MVC components,
     * and launches the main application window.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel if system look and feel fails
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Initialize MVC components
        model = new PokemonModel();
        controller = new PokemonController(model);
        view = new MainFrame(controller);
        
        // Start the GUI
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }
} 