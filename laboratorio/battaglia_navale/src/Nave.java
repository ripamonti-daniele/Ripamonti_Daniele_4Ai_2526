import java.security.InvalidParameterException;

public class Nave {
    private int lunghezza;
    private int[] coordinate_iniziali;
    private int larghezza_griglia;
    private int altezza_griglia;
    private boolean direzione; //true = orizzontale, false = vericale
    private int[][] coordinate;

    public Nave(int lunghezza, int[] coordinate_iniziali, int altezza_griglia, int larghezza_griglia, boolean direzione) throws InvalidParameterException {
        if (lunghezza < 2 || lunghezza > 5) throw new InvalidParameterException("Lunghezza nave non valida");
        this.direzione = direzione;
        this.lunghezza = lunghezza;
        if (direzione) {
            if (coordinate_iniziali.length != 2 || coordinate_iniziali[0] > altezza_griglia - lunghezza) throw new InvalidParameterException("Coordinate iniziali non valide");
            coordinate = new int[this.lunghezza][1];
        }
        else {
            if (coordinate_iniziali.length != 2 || coordinate_iniziali[1] > larghezza_griglia - lunghezza) throw new InvalidParameterException("Coordinate iniziali non valide");
            coordinate = new int[1][this.lunghezza];
        }
    }

    // da finire
    public int[][] getCoordinate() {
        if (direzione) {
            coordinate = new int[1][lunghezza];
            for(int i = 0; i < lunghezza; i++) {
                coordinate[0][i] = coordinate_iniziali[1];
            }
        }

        return coordinate;
    }
}
