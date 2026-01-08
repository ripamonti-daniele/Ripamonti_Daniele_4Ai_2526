import javax.swing.*;
import java.awt.*;

void main() {
    JFrame frame = new JFrame("Scacchi");

    Scacchiera scacchiera = new Scacchiera();
    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera.getScacchiera());

    Casella[][] caselle = scacchieraPanel.getCasellePanel();

    JPanel board = new JPanel(null);

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            board.add(caselle[i][j]);
        }
    }

    frame.add(board);

    frame.pack();
    frame.setSize(1200, 1000);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

}
