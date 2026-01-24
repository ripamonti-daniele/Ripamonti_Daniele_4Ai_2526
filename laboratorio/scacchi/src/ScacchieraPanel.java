import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ScacchieraPanel extends JPanel {
    private final Casella[][] casellePanel;
    public final int DIMENSIONE = 8;

    private final Map<Integer, String> numeroToLettera = new HashMap<>();

    public ScacchieraPanel(Pedina[][] scacchiera) {
        numeroToLettera.put(1, "A");
        numeroToLettera.put(2, "B");
        numeroToLettera.put(3, "C");
        numeroToLettera.put(4, "D");
        numeroToLettera.put(5, "E");
        numeroToLettera.put(6, "F");
        numeroToLettera.put(7, "G");
        numeroToLettera.put(8, "H");

        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        inizializza();
        aggiornaScacchiera(scacchiera);
    }

    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                Color c;
                if ((j + i) % 2 == 0) c = new Color(240, 217, 181);
                else c = new Color(161, 116, 79);

                casellePanel[i][j] = new Casella(c, 100, numeroToLettera.get(j + 1) + (DIMENSIONE - i));
                casellePanel[i][j].setBounds(100 * j, 100 * i,100, 100);
            }
        }
    }

    public void aggiornaScacchiera(Pedina[][] scacchiera) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                String classeOggetto = "null";
                Color colore = null;
                try {
                    classeOggetto = scacchiera[i][j].getClass().getSimpleName();
                    colore = scacchiera[i][j].getColore();
                }
                catch (NullPointerException _) {}

                switch (classeOggetto) {
                    case "Pedone":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.PEDONE_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.PEDONE_BLACK.getImageIcon(100));
                        break;

                    case "Re":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.RE_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.RE_BLACK.getImageIcon(100));
                        break;

                    case "Regina":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.REGINA_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.REGINA_BLACK.getImageIcon(100));
                        break;

                    case "Torre":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.TORRE_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.TORRE_BLACK.getImageIcon(100));
                        break;

                    case "Alfiere":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.ALFIERE_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.ALFIERE_BLACK.getImageIcon(100));
                        break;

                    case "Cavallo":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.CAVALLO_WHITE.getImageIcon(100));
                        else casellePanel[i][j].setImg(IconaPedina.CAVALLO_BLACK.getImageIcon(100));
                        break;

                    case "null":
                        casellePanel[i][j].rimuoviImg();
                        break;
                }
            }
        }
    }

    public Casella[][] getCasellePanel() {
        Casella[][] copia = new Casella[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < DIMENSIONE; i++) {
            copia[i] = Arrays.copyOf(casellePanel[i], casellePanel[i].length);
        }
        return copia;
    }

    public void mettiASchermo(JPanel panel) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                panel.add(casellePanel[i][j]);
            }
        }
    }
}
