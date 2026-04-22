import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {
    private static Finestra instance = null;

    private Finestra() {
        super();
        setSize(912, 1125);
        setLocationRelativeTo(null);
        setTitle("tonatola");
        getContentPane().setBackground(Color.green);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon("src/noce.png"));
        add(label);
        setVisible(true);
    }

    public static Finestra getInstance() {
        if (instance == null) {
            instance = new Finestra();
        }
        return instance;
    }
}
