import java.awt.*;
import javax.swing.*;
import java.io.File;

/**
 * Main entry point for the Enhanced Pokédex System.
 * This class initializes the application, sets up custom fonts,
 * and launches the main GUI frame using the MVC architecture.
 * 
 * The application follows the Model-View-Controller design pattern
 * where the Model manages data, the View handles the user interface,
 * and the Controller coordinates between them.
 * 
 * @author Enhanced Pokédex Team
 * @version 1.0
 * @since 2024
 */
public class Main {
    
    /**
     * Main method that serves as the entry point for the application.
     * Initializes the system with custom fonts, creates the MVC components,
     * and launches the main GUI frame on the Event Dispatch Thread.
     * 
     * The method performs the following steps:
     * 1. Loads and registers custom Pokémon-themed fonts
     * 2. Creates the Model, Controller, and View components
     * 3. Launches the main application window
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Load custom Pokémon font
                Font pokeFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(14f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pokeFont);
                setUIFont(new javax.swing.plaf.FontUIResource(pokeFont));
            } catch (Exception e) {
                // Fallback to default fonts if custom font loading fails
                System.err.println("Warning: Could not load custom font, using system default");
            }
            
            // Initialize MVC components
            PokemonModel model = new PokemonModel();
            PokemonController controller = new PokemonController(model);
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }

    /**
     * Sets the default font for all Swing UI components.
     * This method iterates through all UI manager defaults and replaces
     * any FontUIResource instances with the specified font.
     * 
     * @param f The FontUIResource to set as the default UI font
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
