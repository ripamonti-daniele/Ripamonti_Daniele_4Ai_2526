import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ScacchieraPanel extends JPanel {
    private final Casella[][] casellePanel;
    public final int DIMENSIONE = 8;

    public ScacchieraPanel(Pedina[][] scacchiera) {
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        inizializza();
        aggiornaScacchiera(scacchiera);
    }

    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if ((j + i) % 2 == 0) casellePanel[i][j] = new Casella(Color.red, 100);
                else casellePanel[i][j] = new Casella(Color.green, 100);
                casellePanel[i][j].setBounds(100, 100,100 * j, 100 * i);
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
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.PEDONE_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.PEDONE_BLACK.getImageIcon());

                    case "Re":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.RE_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.RE_BLACK.getImageIcon());

                    case "Regina":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.REGINA_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.REGINA_BLACK.getImageIcon());

                    case "Torre":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.TORRE_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.TORRE_BLACK.getImageIcon());

                    case "Alfiere":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.ALFIERE_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.ALFIERE_BLACK.getImageIcon());

                    case "Cavallo":
                        if (colore == Color.white) casellePanel[i][j].setImg(IconaPedina.CAVALLO_WHITE.getImageIcon());
                        else casellePanel[i][j].setImg(IconaPedina.CAVALLO_BLACK.getImageIcon());

                    case "null":
                        casellePanel[i][j].rimuoviImg();
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
}
