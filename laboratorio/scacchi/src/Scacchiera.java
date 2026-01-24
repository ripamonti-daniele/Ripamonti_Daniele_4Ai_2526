import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scacchiera {
    public final int DIMENSIONE = 8;
    private final Pedina[][] caselle;
    private int mosseNeutre;
    private final Map<Integer, String> numeroToLettera = new HashMap<>();

    public Scacchiera() {
        numeroToLettera.put(1, "A");
        numeroToLettera.put(2, "B");
        numeroToLettera.put(3, "C");
        numeroToLettera.put(4, "D");
        numeroToLettera.put(5, "E");
        numeroToLettera.put(6, "F");
        numeroToLettera.put(7, "G");
        numeroToLettera.put(8, "H");

        caselle = new Pedina[DIMENSIONE][DIMENSIONE];
        mosseNeutre = 0;
        inizializza();
    }

    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (i == 1) caselle[i][j] = new Pedone(Color.black, new int[]{i, j});
                else if (i == DIMENSIONE - 2) caselle[i][j] = new Pedone(Color.white, new int[]{i, j});
                else if (i == 0) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.black, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.black, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.black, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.black, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.black, new int[]{i, j});
                }
                else if (i == DIMENSIONE - 1) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.white, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.white, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.white, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.white, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.white, new int[]{i, j});
                }
                else caselle[i][j] = null;
            }
        }
    }

    public void reset() {
        mosseNeutre = 0;
        inizializza();
    }

    private Pedina copiaPedina(Pedina p) {
        if (p == null) return null;
        return switch (p.getClass().getSimpleName()) {
            case "Pedone" -> new Pedone((Pedone) p);
            case "Alfiere" -> new Alfiere((Alfiere) p);
            case "Cavallo" -> new Cavallo((Cavallo) p);
            case "Torre" -> new Torre((Torre) p);
            case "Regina" -> new Regina((Regina) p);
            case "Re" -> new Re((Re) p);
            default -> throw new IllegalArgumentException("Tipo di pedina non valido");
        };
    }

    public Pedina[][] getScacchiera() {
        Pedina[][] copia = new Pedina[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < caselle.length; i++) {
            for (int j = 0; j < caselle[i].length; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = copiaPedina(caselle[i][j]);
            }
        }
        return copia;
    }

    public String[][] getTipoPedine() {
        String[][] tipoPedine = new String[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < tipoPedine.length; i++) {
            for (int j = 0; j < tipoPedine[i].length; j++) {
                if (caselle[i][j] == null) tipoPedine[i][j] = null;
                else tipoPedine[i][j] = caselle[i][j].getClass().getSimpleName();
            }
        }
        return tipoPedine;
    }

    public void muoviPedina(int[] pos_inizio, int[] pos_fine) {
        if (pos_inizio[0] < 0 || pos_inizio[0] > DIMENSIONE - 1 || pos_inizio[1] < 0 || pos_inizio[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione iniziale non valida");
        if (pos_fine[0] < 0 || pos_fine[0] > DIMENSIONE - 1 || pos_fine[1] < 0 || pos_fine[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione finale non valida");
        if (caselle[pos_inizio[0]][pos_inizio[1]] == null) throw new IllegalArgumentException("La casella da cui vuoi prendere la pedina è vuota");

        Pedina p = caselle[pos_inizio[0]][pos_inizio[1]];
        List<int[]> mosseValide = p.getMosseValide();

        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (caselle[i][j] == null) continue;
                if (caselle[i][j].getColore() == p.getColore()) mosseValide.remove(new int[] {i, j});
            }
        }

        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa == pos_fine) {
                valido = true;
                break;
            }
        }

        if (!valido) throw new IllegalArgumentException("Mossa non valida");
        p.muovi(pos_fine);
        caselle[pos_inizio[0]][pos_inizio[1]] = null;
        caselle[pos_fine[0]][pos_fine[1]] = p;
    }

    private boolean mosseIntermedieValide(Pedina pedina, int[] posizione, boolean orizzontale, boolean verticale, int incrementoOrizzontale, int incrementoVerticale) {
        if (verticale && orizzontale) {
            int j = posizione[1] + incrementoOrizzontale;
            for (int i = posizione[0] + incrementoVerticale; i < pedina.getPosizione()[0]; i += incrementoVerticale) {
                if (caselle[i][j] != null) return false;
                j += incrementoOrizzontale;
            }
        }

        else if (verticale) {
            for (int i = posizione[0] + incrementoVerticale; i < pedina.getPosizione()[0]; i += incrementoVerticale) {
                if (caselle[i][posizione[1]] != null) return false;
            }
        }

        else if (orizzontale) {
            for (int i = posizione[1] + incrementoOrizzontale; i < pedina.getPosizione()[1]; i += incrementoOrizzontale) {
                if (caselle[posizione[0]][i] != null) return false;
            }
        }
        return true;
    }

    private void promuoviPedone(Pedina pedina) {

    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (caselle[i][j] == null) str += "null null " +  numeroToLettera.get(j + 1) + (i + 1) + "\n";
                else {
                    Color colore = caselle[i][j].getColore();
                    String c = colore.getRed() + "_" + colore.getGreen() + "_" + colore.getBlue();
                    if (c.equals("255_255_255")) c = "white";
                    else if (c.equals("0_0_0")) c = "black";

                    str += caselle[i][j].getClass().getSimpleName() + " " + c + " " + numeroToLettera.get(j + 1) + (DIMENSIONE - i) + "\n";
                }
            }
        }
        return str;
    }
}
