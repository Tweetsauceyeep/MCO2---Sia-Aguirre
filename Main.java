import java.awt.*;
import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Font pokeFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/PressStart2P-Regular.ttf")).deriveFont(14f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pokeFont);
                setUIFont(new javax.swing.plaf.FontUIResource(pokeFont));
            } catch (Exception e) {
                // fallback handled in panels
            }
            PokemonModel model = new PokemonModel();
            PokemonController controller = new PokemonController(model);
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }

    // Utility to set default font for all Swing components
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
