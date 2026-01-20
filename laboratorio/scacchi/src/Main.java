import javax.swing.*;
import java.awt.*;

//void aggiungiStatoJSON(JSONObject)

void main() {
    JFrame frame = new JFrame("Scacchi");
//    frame.setLayout(null);
    frame.setSize(1200, 1000);

    Scacchiera scacchiera = new Scacchiera();
    ScacchieraPanel scacchieraPanel = new ScacchieraPanel(scacchiera.getScacchiera());

//    Path path = Path.of("src/Partita.txt");
//    try {
//        Files.writeString(path, scacchiera.toString() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//    }
//    catch (IOException e) {
//        System.out.println(e.getMessage());
//    }

    JPanel board = new JPanel(null);
    scacchieraPanel.mettiASchermo(board);


    frame.add(board);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
