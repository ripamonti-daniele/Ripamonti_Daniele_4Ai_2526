//TODO
// impostazione timer da parte dell'utente
// velocità timer corretta
// l'enpassant mostra il cerchio vuoto
// grafica migliore
// controllo finale per ottimizzazione codice
// debug generale

import javax.swing.*;
import java.awt.*;

void main() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    int width = screenSize.width;
    int height = screenSize.height - screenInsets.top - screenInsets.bottom;
    Color sfondo = new Color(120, 72, 48);
//    new Color(193, 154, 107) new Color(181, 140, 90) new Color(120, 72, 48)

    JFrame frame = new JFrame("Scacchi");
    frame.setLayout(null);
    frame.setSize(width, height);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    frame.getContentPane().setBackground(sfondo);

    Scacchiera scacchiera = new Scacchiera();
    GestoreGrafico gestoreGrafico = new GestoreGrafico(scacchiera, (int) Math.round(height * 0.8), sfondo);
//    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera, 500);

    frame.setVisible(true);
    JPanel contenitore = new JPanel(null);
    gestoreGrafico.mettiASchermo(contenitore);

    Insets insets = frame.getInsets();
    int contentHeight = frame.getHeight() - insets.top - insets.bottom;

    contenitore.setBounds(gestoreGrafico.lunghezzaCasella, (contentHeight - gestoreGrafico.lunghezzaScacchiera) / 2, 2 * gestoreGrafico.lunghezzaScacchiera, gestoreGrafico.lunghezzaScacchiera + gestoreGrafico.lunghezzaCasella);
    contenitore.setBackground(sfondo);
    //    contenitore.setBounds(width / 10, (contentHeight - scacchieraPanel.lunghezzaScacchiera) / 2, scacchieraPanel.lunghezzaScacchiera * 2, scacchieraPanel.lunghezzaScacchiera * 2);
    frame.add(contenitore);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
