import javax.swing.*;
import java.awt.*;

//void aggiungiStatoJSON(JSONObject)

void main() {
    JFrame frame = new JFrame("Scacchi");
//    frame.setLayout(null);
    frame.setSize(1200, 1000);

    Scacchiera scacchiera = new Scacchiera();
    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera.getScacchiera());

    System.out.println(scacchiera);

    Casella[][] caselle = scacchieraPanel.getCasellePanel();

    JPanel board = new JPanel(null);

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            board.add(caselle[i][j]);
        }
    }

    frame.add(board);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
