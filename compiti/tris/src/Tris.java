import java.io.IO;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class Tris {
    private final char[][] griglia;
    public final int LUNGHEZZA_GRIGLIA;
    public final int ALTEZZA_GRIGLIA;
    public final char giocatoreX;
    public final char giocatoreO;
    public final char casellaVuota;
    public final HashMap<String, Integer> diozionario;

    public Tris() {
        LUNGHEZZA_GRIGLIA = 3;
        ALTEZZA_GRIGLIA = 3;
        giocatoreX = 'X';
        giocatoreO = 'O';
        casellaVuota = ' ';
        griglia = new char[LUNGHEZZA_GRIGLIA][ALTEZZA_GRIGLIA];
        inizializzaGriglia();
        diozionario = new HashMap<>();
        diozionario.put("A", 0);
        diozionario.put("B", 1);
        diozionario.put("C", 2);
    }

    private void inizializzaGriglia() {
        for(int y = 0; y < ALTEZZA_GRIGLIA; y++) {
            for(int x = 0; x < LUNGHEZZA_GRIGLIA; x++) {
                griglia[x][y] = casellaVuota;
            }
        }
    }

    public void addSegno(char segno, char x, int y) throws InvalidParameterException, Exception {
        if (segno != giocatoreO && segno != giocatoreX) throw new InvalidParameterException("segno non valido");
        if (x < 0 || x > LUNGHEZZA_GRIGLIA || (!diozionario.containsKey(y))) throw new InvalidParameterException("casella inesistente");
        if (griglia[x][y] != casellaVuota) throw new Exception("casella già occupata");
        griglia[x][diozionario.get(y)] = segno;
    }

    public int controlli(char segno) {
        //controlla se è piena
        for (char[] y : griglia) {
            for (char x : y) {
                if (x == casellaVuota) {
                    return 2;
                }
            }
        }

        //controllo orizzontale
        for(char[] x : griglia) {
            boolean tris = true;
            for(char y : x) {
               if (y != segno) {
                   tris = false;
                   break;
               }
            }
            if (tris) return 1;
        }

        //controllo verticale
        for (int x = 0; x < LUNGHEZZA_GRIGLIA; x++) {
            boolean tris = true;
            for (int y = 0; y < ALTEZZA_GRIGLIA; y++) {
                if (griglia[x][y] != segno) {
                    tris = false;
                    break;
                }
            }
            if (tris) return 1;
        }

        //controllo diagonale
        int x = 0;
        int y = 0;
        boolean tris = true;
        while (x < LUNGHEZZA_GRIGLIA && y < ALTEZZA_GRIGLIA) {
            if (griglia[y][x] != segno) {
                tris = false;
                break;
            }
            x++;
            y++;
        }
        if (tris) return 1;

        x = LUNGHEZZA_GRIGLIA - 1;
        y = 0;
        tris = true;
        while (x <= 0 && y < ALTEZZA_GRIGLIA) {
            if (griglia[y][x] != segno) {
                tris = false;
                break;
            }
            x--;
            y++;
        }
        if (tris) return 1;

        return 0;
    }

    public String getStringaGriglia() {
        StringBuilder s = new StringBuilder(" A   B   C \n");
        for (int x = 0; x < LUNGHEZZA_GRIGLIA; x++) {
            for (int y = 0; y < ALTEZZA_GRIGLIA; y++) {
                s.append(" ");
                s.append(griglia[y][x]);
                s.append(" ");

                if (y == ALTEZZA_GRIGLIA - 1) {
                    s.append(" ");
                    s.append(x + 1);
                    s.append("\n");
                }
                else s.append("|");
            }
            if (x < LUNGHEZZA_GRIGLIA - 1) s.append("---|---|---\n");
        }
        s.append("\n");
        return s.toString();
    }

    private void chiediCasella() {
        String casella = IO.readln().trim().toUpperCase();
        //controlli e aggiunta (usando il metodo)
    }

    public void giocaConsole() {
        System.out.println(getStringaGriglia());

        boolean gioco = false;
        while (gioco) {
            System.out.println(getStringaGriglia());
        }
    }

}
