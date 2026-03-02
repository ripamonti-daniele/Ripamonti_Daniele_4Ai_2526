//TODO
// rotazione scacchiera
// condizioni vittoria/pareggio
// pareggi per ripetizione/mosse neutre
// override dei metodi di JPanel di Casella in modo da renderli inutilizzabili dall'esterno
// funzione metti a schermo aggiunge a un JFrame o a un JPanel un JPanel con dentro le caselle
// pulsante gioca ancora
// scrittura su file della situazione della scacchiera a ogni mossa fatta
// aggiornamento di scacchieraPanel con lettura file
// nomi giocatori
// timer
// materiale
// possibilità di spostarsi avanti e indietro nello stato della partita
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
    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera, (int) Math.round(height * 0.8));
//    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera, 500);

    frame.setVisible(true);
    JPanel board = new JPanel(null);
    scacchieraPanel.mettiASchermo(board);

    Insets insets = frame.getInsets();
    int contentHeight = frame.getHeight() - insets.top - insets.bottom;

    board.setBounds(width / 10, (contentHeight - scacchieraPanel.lunghezzaScacchiera) / 2, scacchieraPanel.lunghezzaScacchiera + scacchieraPanel.lunghezzaScacchiera / 8, scacchieraPanel.lunghezzaScacchiera);
//    board.setBounds(width / 10, (contentHeight - scacchieraPanel.lunghezzaScacchiera) / 2, scacchieraPanel.lunghezzaScacchiera * 2, scacchieraPanel.lunghezzaScacchiera * 2);
    frame.add(board);
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
