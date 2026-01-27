import javax.swing.*;
import java.awt.*;

//void aggiungiStatoJSON(JSONObject)

int[] idToPos(String id) {
    System.out.println(id);
    Map<String, Integer> letteraToNumero = new HashMap<>();
    letteraToNumero.put("A", 1);
    letteraToNumero.put("B", 2);
    letteraToNumero.put("C", 3);
    letteraToNumero.put("D", 4);
    letteraToNumero.put("E", 5);
    letteraToNumero.put("F", 6);
    letteraToNumero.put("G", 7);
    letteraToNumero.put("H", 8);

    int[] pos = new int[2];
    pos[0] = letteraToNumero.get(id.substring(0, 1));
    pos[1] = Integer.parseInt(id.substring(1, 2));

    return pos;
}

void aggiungiListener(Casella[][] caselle, Scacchiera s) {
    for (int i = 0; i < caselle.length; i++) {
        for (int j = 0; j < caselle[i].length; j++) {
            final int r = i;
            final int c = j;
            caselle[i][j].setListener(() -> {
                if (s.getCasella_selezionata() == null) {
                    try {
                        s.selezionaPedina(idToPos(caselle[r][c].getId()));
                    }
                    catch (Exception e) {
                        System.out.println("prova1");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    try {
                        s.muoviPedina(idToPos(caselle[r][c].getId()));
                    }
                    catch (Exception e) {
                        System.out.println("prova2");
                        System.out.println(e.getMessage());
                    }
                }
            });
        }
    }
}

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

    System.out.println(scacchiera);
    aggiungiListener(scacchieraPanel.getCasellePanel(), scacchiera);
    JPanel board = new JPanel(null);
    scacchieraPanel.mettiASchermo(board);

    frame.add(board);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
