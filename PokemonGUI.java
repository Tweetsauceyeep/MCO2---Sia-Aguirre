import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class for the Enhanced Pokédex System
 * Follows MVC design pattern
 */
public class PokemonGUI {
    private static PokemonModel model;
    private static PokemonController controller;
    private static MainFrame view;
    
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