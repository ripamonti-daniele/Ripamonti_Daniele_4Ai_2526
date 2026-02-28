//TODO
// rotazione scacchiera
// condizioni vittoria/pareggio
// pareggi per ripetizione/mosse neutre
// rotazione scacchiera
// promozione pedoni
// condizioni vittoria/pareggio
// pulsante gioca ancora
// scrittura su file della situazione della scacchiera a ogni mossa fatta
// aggiornamento di scacchieraPanel con lettura file
// timer
// nomi giocatori
// materiale
// grafica migliore

import javax.swing.*;
import java.awt.*;

//void aggiungiStatoJSON(JSONObject)

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
