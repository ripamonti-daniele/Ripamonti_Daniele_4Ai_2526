import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//void aggiungiStatoJSON(JSONObject)

//int[] idToPos(String id) {
//    Map<String, Integer> letteraToNumero = new HashMap<>();
//    letteraToNumero.put("A", 1);
//    letteraToNumero.put("B", 2);
//    letteraToNumero.put("C", 3);
//    letteraToNumero.put("D", 4);
//    letteraToNumero.put("E", 5);
//    letteraToNumero.put("F", 6);
//    letteraToNumero.put("G", 7);
//    letteraToNumero.put("H", 8);
//
//    int[] pos = new int[2];
//    pos[0] = 8 - Integer.parseInt(id.substring(1, 2));
//    pos[1] = letteraToNumero.get(id.substring(0, 1)) - 1;
//
//    return pos;
//}

void aggiungiListener(Casella[][] caselle, Scacchiera s, ScacchieraPanel sp) {
    for (int i = 0; i < caselle.length; i++) {
        for (int j = 0; j < caselle[i].length; j++) {
            final int r = i;
            final int c = j;
            sp.setListener(j, i, () -> {
                sp.resetMosseValide();

                if (s.getCasella_selezionata() == null || !s.muoviPedina(new int[]{r, c})) { //se la casella selezionata non è null allora seleziona la pedina; se è null prova a spostarla e se non riesce seleziona la pedina dove si intendeva spostare quella selezionata precedentemente
                    List<int[]> mosseValide = s.selezionaPedina(new int[]{r, c}, s.getTurno());
                    if (mosseValide != null) sp.mostraMosseValide(mosseValide);
                }
                else s.cambiaTurno();

                sp.aggiornaScacchiera(s.getScacchiera());
                sp.disegna();
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

//    System.out.println(scacchiera);
    aggiungiListener(scacchieraPanel.getCasellePanel(), scacchiera, scacchieraPanel);
    JPanel board = new JPanel(null);
    scacchieraPanel.mettiASchermo(board); //da rifare

    frame.add(board);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
