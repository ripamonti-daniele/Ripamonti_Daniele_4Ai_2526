import scacchiera_pedine.PartitaFileManager;
import scacchiera_pedine.Scacchiera;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Avvia l'applicazione degli scacchi inizializzando la finestra principale
 * e tutti i componenti grafici necessari alla partita.
 * <p>
 * La finestra viene aperta in modalità massimizzata; la dimensione minima
 * consentita è 700×500 pixel. Lo shutdown hook garantisce che i file
 * di partita vengano svuotati alla chiusura dell'applicazione.
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        int width = screenSize.width;
        int height = screenSize.height - screenInsets.top - screenInsets.bottom;

        Color sfondo = new Color(120, 72, 48);

        JFrame frame = new JFrame("Scacchi");
        frame.setLayout(null);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(sfondo);
        frame.setIconImage(new ImageIcon(new ImageIcon(Objects.requireNonNull(IconaPedina.class.getResource("/img/chess.png"))).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());

        GestoreGrafico gestoreGrafico = GestoreGrafico.getInstance(new Scacchiera(), (int) Math.round(height * 0.8), sfondo, new Color(240, 217, 181), new Color(161, 116, 79));

        JPanel contenitore = new JPanel(null);

        Insets insets = frame.getInsets();
        int contentHeight = frame.getHeight() - insets.top - insets.bottom;

        contenitore.setBounds(gestoreGrafico.lunghezzaCasella, (contentHeight - gestoreGrafico.lunghezzaScacchiera) / 2, 2 * gestoreGrafico.lunghezzaScacchiera, gestoreGrafico.lunghezzaScacchiera + gestoreGrafico.lunghezzaCasella / 4 * 3);
        contenitore.setBackground(sfondo);
        gestoreGrafico.mettiASchermo(contenitore);

        frame.add(contenitore);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread(PartitaFileManager::eliminaFile));
    }
}
