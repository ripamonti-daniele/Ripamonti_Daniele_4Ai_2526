//TODO
// pareggi per ripetizione/mosse neutre
// scrittura su file della situazione della scacchiera a ogni mossa fatta
// aggiornamento di scacchieraPanel con lettura file
// nomi giocatori
// timer
// materiale
// possibilità di spostarsi avanti e indietro nello stato della partita
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

    JFrame frame = new JFrame("Scacchi");
    frame.setLayout(null);
    frame.setSize(width, height);
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    Scacchiera scacchiera = new Scacchiera();
    GestoreGrafico gestoreGrafico = new GestoreGrafico(scacchiera, (int) Math.round(height * 0.8));
//    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera, 500);

    frame.setVisible(true);
    JPanel contenitore = new JPanel(null);
    gestoreGrafico.mettiASchermo(contenitore);

    Insets insets = frame.getInsets();
    int contentHeight = frame.getHeight() - insets.top - insets.bottom;

    contenitore.setBounds(gestoreGrafico.lunghezzaScacchiera / 8, (contentHeight - gestoreGrafico.lunghezzaScacchiera) / 2, 2 * gestoreGrafico.lunghezzaScacchiera, gestoreGrafico.lunghezzaScacchiera);
//    contenitore.setBounds(width / 10, (contentHeight - scacchieraPanel.lunghezzaScacchiera) / 2, scacchieraPanel.lunghezzaScacchiera * 2, scacchieraPanel.lunghezzaScacchiera * 2);
    frame.add(contenitore);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

//    Path path = Path.of("partita.txt");
//    try {
//        Files.writeString(path, scacchiera.toString() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//    }
//    catch (IOException e) {
//        System.out.println(e.getMessage());
//    }

//    System.out.println(scacchiera);
