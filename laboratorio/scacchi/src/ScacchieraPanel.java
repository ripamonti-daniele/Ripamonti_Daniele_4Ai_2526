import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ScacchieraPanel extends JPanel {
    private final Casella[][] casellePanel;
    public final int DIMENSIONE = 8;
    private ImageIcon pedoneW;
    private ImageIcon alfiereW;
    private ImageIcon cavalloW;
    private ImageIcon torreW;
    private ImageIcon reginaW;
    private ImageIcon reW;
    private ImageIcon pedoneB;
    private ImageIcon alfiereB;
    private ImageIcon cavalloB;
    private ImageIcon torreB;
    private ImageIcon reginaB;
    private ImageIcon reB;

    private final Map<Integer, String> numeroToLettera = new HashMap<>();

    public ScacchieraPanel(Pedina[][] scacchiera, ImageIcon pedoneW, ImageIcon alfiereW, ImageIcon cavalloW, ImageIcon torreW, ImageIcon reginaW, ImageIcon reW, ImageIcon pedoneB, ImageIcon alfiereB, ImageIcon cavalloB, ImageIcon torreB, ImageIcon reginaB, ImageIcon reB) {
        numeroToLettera.put(1, "A");
        numeroToLettera.put(2, "B");
        numeroToLettera.put(3, "C");
        numeroToLettera.put(4, "D");
        numeroToLettera.put(5, "E");
        numeroToLettera.put(6, "F");
        numeroToLettera.put(7, "G");
        numeroToLettera.put(8, "H");

        setPedoneW(pedoneW);
        setAlfiereW(alfiereW);
        setCavalloW(cavalloW);
        setTorreW(torreW);
        setReginaW(reginaW);
        setReW(reW);
        setPedoneB(pedoneB);
        setAlfiereB(alfiereB);
        setCavalloB(cavalloB);
        setTorreB(torreB);
        setReginaB(reginaB);
        setReB(reB);

        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        inizializza();
        aggiornaScacchiera(scacchiera);
    }

    public ScacchieraPanel(Pedina[][] scacchiera) {
        this(scacchiera, IconaPedina.PEDONE_WHITE.getImageIcon(100),
                         IconaPedina.ALFIERE_WHITE.getImageIcon(100),
                         IconaPedina.CAVALLO_WHITE.getImageIcon(100),
                         IconaPedina.TORRE_WHITE.getImageIcon(100),
                         IconaPedina.REGINA_WHITE.getImageIcon(100),
                         IconaPedina.RE_WHITE.getImageIcon(100),
                         IconaPedina.PEDONE_BLACK.getImageIcon(100),
                         IconaPedina.ALFIERE_BLACK.getImageIcon(100),
                         IconaPedina.CAVALLO_BLACK.getImageIcon(100),
                         IconaPedina.TORRE_BLACK.getImageIcon(100),
                         IconaPedina.REGINA_BLACK.getImageIcon(100),
                         IconaPedina.RE_BLACK.getImageIcon(100));
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
                        if (colore == Color.white) casellePanel[i][j].setImg(pedoneW);
                        else casellePanel[i][j].setImg(pedoneB);
                        break;

                    case "Alfiere":
                        if (colore == Color.white) casellePanel[i][j].setImg(alfiereW);
                        else casellePanel[i][j].setImg(alfiereB);
                        break;

                    case "Cavallo":
                        if (colore == Color.white) casellePanel[i][j].setImg(cavalloW);
                        else casellePanel[i][j].setImg(cavalloB);
                        break;

                    case "Torre":
                        if (colore == Color.white) casellePanel[i][j].setImg(torreW);
                        else casellePanel[i][j].setImg(torreB);
                        break;

                    case "Regina":
                        if (colore == Color.white) casellePanel[i][j].setImg(reginaW);
                        else casellePanel[i][j].setImg(reginaB);
                        break;

                    case "Re":
                        if (colore == Color.white) casellePanel[i][j].setImg(reW);
                        else casellePanel[i][j].setImg(reB);
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
            for (int j = 0; j < DIMENSIONE; j++) {
                copia[i][j] = new Casella(casellePanel[i][j]);
            }
        }
        return copia;
//        return casellePanel;
    }

    private void controllaImmagini(ImageIcon daControllare, ImageIcon img1, ImageIcon img2, ImageIcon img3, ImageIcon img4, ImageIcon img5, ImageIcon img6, ImageIcon img7, ImageIcon img8, ImageIcon img9, ImageIcon img10, ImageIcon img11) {
        if (daControllare == null) throw new IllegalArgumentException("Le immagini delle pedine non possono essere null");
        if (daControllare == img1 || daControllare == img2 || daControllare == img3 || daControllare == img4 || daControllare == img5 || daControllare == img6 || daControllare == img7 || daControllare == img8 || daControllare == img9 || daControllare == img10 || daControllare == img11) throw new IllegalArgumentException("Le immagini delle pedine devono essere diverse fra loro");
    }

    public ImageIcon getPedoneW() {
        return new ImageIcon(pedoneW.getImage());
    }

    private void setPedoneW(ImageIcon pedoneW) {
        controllaImmagini(pedoneW, alfiereW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneW = pedoneW;
    }

    public ImageIcon getAlfiereW() {
        return new ImageIcon(alfiereW.getImage());
    }

    private void setAlfiereW(ImageIcon alfiereW) {
        controllaImmagini(alfiereW, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.alfiereW = alfiereW;
    }

    public ImageIcon getCavalloW() {
        return new ImageIcon(cavalloW.getImage());
    }

    private void setCavalloW(ImageIcon cavalloW) {
        controllaImmagini(cavalloW, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.cavalloW = cavalloW;
    }

    public ImageIcon getTorreW() {
        return new ImageIcon(torreW.getImage());
    }

    private void setTorreW(ImageIcon torreW) {
        controllaImmagini(torreW, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.torreW = torreW;
    }

    public ImageIcon getReginaW() {
        return new ImageIcon(reginaW.getImage());
    }

    private void setReginaW(ImageIcon reginaW) {
        controllaImmagini(reginaW, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reginaW = reginaW;
    }

    public ImageIcon getReW() {
        return new ImageIcon(reW.getImage());
    }

    private void setReW(ImageIcon reW) {
        controllaImmagini(reW, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reW = reW;
    }

    public ImageIcon getPedoneB() {
        return new ImageIcon(pedoneB.getImage());
    }

    private void setPedoneB(ImageIcon pedoneB) {
        controllaImmagini(pedoneB, alfiereW, cavalloW, torreW, reginaW, reW, pedoneW, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneB = pedoneB;
    }

    public ImageIcon getAlfiereB() {
        return new ImageIcon(alfiereB.getImage());
    }

    private void setAlfiereB(ImageIcon alfiereB) {
        controllaImmagini(alfiereB, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereW, cavalloB, torreB, reginaB, reB);
        this.alfiereB = alfiereB;
    }

    public ImageIcon getCavalloB() {
        return new ImageIcon(cavalloB.getImage());
    }

    private void setCavalloB(ImageIcon cavalloB) {
        controllaImmagini(cavalloB, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloW, torreB, reginaB, reB);
        this.cavalloB = cavalloB;
    }

    public ImageIcon getTorreB() {
        return new ImageIcon(torreB.getImage());
    }

    private void setTorreB(ImageIcon torreB) {
        controllaImmagini(torreB, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreW, reginaB, reB);
        this.torreB = torreB;
    }

    public ImageIcon getReginaB() {
        return new ImageIcon(reginaB.getImage());
    }

    private void setReginaB(ImageIcon reginaB) {
        controllaImmagini(reginaB, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaW, reB);
        this.reginaB = reginaB;
    }

    public ImageIcon getReB() {
        return new ImageIcon(reB.getImage());
    }

    private void setReB(ImageIcon reB) {
        controllaImmagini(reB, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reW);
        this.reB = reB;
    }

    public void setListenerComune(casellaClickListener l) {
        if (l == null) throw new IllegalArgumentException("Il listener non può essere null");
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.setListener(l);
        }
    }

    public void setListener(int x, int y, casellaClickListener l) {
        if (x < 0 || y < 0 || x > DIMENSIONE - 1 || y > DIMENSIONE - 1) throw new IllegalArgumentException("Coordinate casella non valide");
        if (l == null) throw new IllegalArgumentException("Il listener non può essere null");
        casellePanel[y][x].setListener(l);
    }

    public void mettiASchermo(JPanel panel) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                panel.add(casellePanel[i][j]);
            }
        }
    }

    public void resetMosseValide() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.mossaValida = false;
        }
    }

    public void mostraMosseValide(List<int[]> mosseValide) {
        if (mosseValide == null) throw new IllegalArgumentException("Le mosse valide non possono essere null");
        resetMosseValide();
        for (int[] m : mosseValide) {
            casellePanel[m[0]][m[1]].mossaValida = true;
        }
    }

    public void disegna() {
        for (Casella[] y : casellePanel) {
            for (Casella x : y) x.repaint();
        }
    }
}
