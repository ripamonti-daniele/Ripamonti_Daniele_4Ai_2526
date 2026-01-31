import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scacchiera {
    public final int DIMENSIONE = 8;
    private final Pedina[][] caselle;
    private int mosseNeutre;
    private final Map<Integer, String> numeroToLettera = new HashMap<>();
    private int[] casella_selezionata;
    private List<int[]> mosseValide;

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
        casella_selezionata = null;
        mosseValide = new ArrayList<>();
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

    public List<int[]> selezionaPedina(int[] pos) {
//        System.out.println(pos[0] + " " + pos[1]);
//        System.out.println(caselle[pos[0]][pos[1]]);
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");
        if (caselle[pos[0]][pos[1]] == null) throw new IllegalArgumentException("La casella da cui vuoi prendere la pedina è vuota");

        Pedina p = caselle[pos[0]][pos[1]];
        List<int[]> mosseValide = p.getMosseValide();

        //da implementare correttamente
        int x, y;
        for (int i = mosseValide.size() - 1; i >= 0; i--) {
            y = mosseValide.get(i)[0];
            x = mosseValide.get(i)[1];
            if (caselle[y][x] != null && caselle[y][x].getColore() == p.getColore()) mosseValide.remove(i);
        }

        if (mosseValide.isEmpty()) throw new IllegalStateException("Questa pedina non può essere mossa al momento");

        this.casella_selezionata = pos;
        this.mosseValide = mosseValide;
        return mosseValide;
    }

    public void muoviPedina(int[] pos) {
        if (casella_selezionata == null) throw new IllegalStateException("Devi prima selezionare una pedina per poterla muovere");
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");

        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == pos[0] && mossa[1] == pos[1]) {
                valido = true;
                break;
            }
        }

        if (!valido) {
            casella_selezionata = null;
            throw new IllegalArgumentException("Mossa non valida");
        }
        caselle[casella_selezionata[0]][casella_selezionata[1]].muovi(pos);
        caselle[pos[0]][pos[1]] = caselle[casella_selezionata[0]][casella_selezionata[1]];
        caselle[casella_selezionata[0]][casella_selezionata[1]] = null;
        casella_selezionata = null;
    }

    public int[] getCasella_selezionata() {
        if (casella_selezionata == null) return null;
        return casella_selezionata.clone();
    }

    private void promuoviPedone(Pedina pedina) {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pedina p = caselle[i][j];
                switch (p) {
                    case null -> sb.append(". "); // casella vuota
                    case Pedone pedone -> sb.append("P ");
                    case Torre torre -> sb.append("T ");
                    case Cavallo cavallo -> sb.append("C ");
                    case Alfiere alfiere -> sb.append("A ");
                    case Regina regina -> sb.append("Q ");
                    case Re re -> sb.append("K ");
                    default -> {
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
