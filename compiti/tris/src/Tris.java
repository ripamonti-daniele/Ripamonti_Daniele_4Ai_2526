import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;

public class Tris {
    private char[][] griglia;
    public final int LUNGHEZZA_GRIGLIA;
    public final int ALTEZZA_GRIGLIA;
    public final char giocatoreX;
    public final char giocatoreO;
    public final char casellaVuota;
    public final HashMap<String, Integer> diozionario;

    public Tris() {
        LUNGHEZZA_GRIGLIA = 3;
        ALTEZZA_GRIGLIA = 3;
        griglia = new char[LUNGHEZZA_GRIGLIA][ALTEZZA_GRIGLIA];
        inizializzaGriglia();
        giocatoreX = 'X';
        giocatoreO = 'O';
        casellaVuota = ' ';
        diozionario = new HashMap<>();
        diozionario.put("A", 0);
        diozionario.put("B", 1);
        diozionario.put("C", 2);
    }

    private void inizializzaGriglia() {
        for (char[] chars : griglia) {
            Arrays.fill(chars, casellaVuota);
        }
    }

    public void addSegno(char segno, int x, int y) throws InvalidParameterException, Exception {
        if (segno != giocatoreO && segno != giocatoreX) throw new InvalidParameterException("segno non valido");
        if (x < 0 || x > 3 || y < 0 || y > 3) throw new InvalidParameterException("casella inesistente");
        if (griglia[x][y] != casellaVuota) throw new Exception("casella già occupata");
        griglia[x][y] = segno;
    }

    public void controlli() {

    }

}
