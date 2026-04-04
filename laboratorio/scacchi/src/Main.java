//TODO
// suoni
// controllo finale per ottimizzazione codice
// debug generale

import scacchiera_pedine.Scacchiera;
import javax.swing.*;
import java.awt.*;

void main() {
    System.setProperty("sun.java2d.uiScale", "1");

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    int width = screenSize.width;
    int height = screenSize.height - screenInsets.top - screenInsets.bottom;

    Color sfondo = new Color(120, 72, 48);
//    new Color(193, 154, 107) new Color(181, 140, 90) new Color(120, 72, 48)

    JFrame frame = new JFrame("Scacchi");
    frame.setLayout(null);
    frame.setSize(width, height);
    frame.setLocationRelativeTo(null);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.getContentPane().setBackground(sfondo);
    frame.setIconImage(new ImageIcon(new ImageIcon("img/chess.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());

    Scacchiera scacchiera = new Scacchiera();
    GestoreGrafico gestoreGrafico = new GestoreGrafico(scacchiera, (int) Math.round(height * 0.8), sfondo, new Color(240, 217, 181), new Color(161, 116, 79));
//    GestoreGrafico gestoreGrafico = new GestoreGrafico(scacchiera, 500, sfondo, new Color(240, 217, 181), new Color(161, 116, 79));
//    GestoreGrafico gestoreGrafico = new GestoreGrafico(scacchiera, (int) Math.round(height * 0.8));

    JPanel contenitore = new JPanel(null);

    Insets insets = frame.getInsets();
    int contentHeight = frame.getHeight() - insets.top - insets.bottom;

    contenitore.setBounds(gestoreGrafico.lunghezzaCasella, (contentHeight - gestoreGrafico.lunghezzaScacchiera) / 2, 2 * gestoreGrafico.lunghezzaScacchiera, gestoreGrafico.lunghezzaScacchiera + gestoreGrafico.lunghezzaCasella / 4 * 3);
    contenitore.setBackground(sfondo);
    gestoreGrafico.mettiASchermo(contenitore);

    frame.add(contenitore);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        String percorso = "partita.txt";
        Path p = Path.of(percorso).toAbsolutePath().normalize();
        if (p.startsWith(Path.of(System.getProperty("user.dir"))) && Files.exists(p)) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(percorso));
                writer.write("");
                writer.close();
            }
            catch (IOException _) {}
        }
    }));
}
