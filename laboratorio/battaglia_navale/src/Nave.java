import java.security.InvalidParameterException;

public class Nave {
    private final int lunghezza;
    private final boolean direzione; //true = orizzontale, false = vericale
    private final int[] ascisse;
    private final int[] ordinate;
    private final int[] coordinate_iniziali;
    private final boolean[] stato_nave;

    public Nave(int lunghezza, int[] coordinate_iniziali, boolean direzione) {
        if (lunghezza < 2 || lunghezza > 5) throw new InvalidParameterException("Lunghezza nave non valida");
        this.direzione = direzione;
        this.lunghezza = lunghezza;
        this.coordinate_iniziali = coordinate_iniziali;
        stato_nave = new boolean[this.lunghezza];

        if (direzione) {
            ascisse = new int[lunghezza];
            ordinate = new int[1];
            ordinate[0] = coordinate_iniziali[1];
        }

        else {
            ordinate = new int[lunghezza];
            ascisse = new int[1];
            ascisse[0] = coordinate_iniziali[0];
        }

        inizializza();
    }

    private void inizializza() {
        for (int i = 0; i < lunghezza; i++) {
            stato_nave[i] = false;
            if (direzione) ascisse[i] = i + coordinate_iniziali[0];
            else ordinate[i] = i + coordinate_iniziali[1];
        }
    }

    public int[] getAscisse() {
        return ascisse.clone();
    }

    public int[] getOrdinate() {
        return ordinate.clone();
    }

    public boolean[] getStatoNave() {
        return stato_nave.clone();
    }

    public void colpisciNave(int pos) {
        if (stato_nave[pos]) throw new InvalidParameterException("Casella nave già colpita");
        stato_nave[pos] = true;
    }
}
