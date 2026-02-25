import javax.swing.*;
import java.awt.*;

//void aggiungiStatoJSON(JSONObject)

void main() {
    JFrame frame = new JFrame("Scacchi");
//    frame.setLayout(null);
    frame.setSize(1200, 1000);

    Scacchiera scacchiera = new Scacchiera();
    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera);

//    Path path = Path.of("partita.txt");
//    try {
//        Files.writeString(path, scacchiera.toString() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//    }
//    catch (IOException e) {
//        System.out.println(e.getMessage());
//    }

//    System.out.println(scacchiera);

    JPanel board = new JPanel(null);
    scacchieraPanel.mettiASchermo(board);

    frame.add(board);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
