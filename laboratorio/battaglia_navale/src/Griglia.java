import javax.print.attribute.standard.Finishings;
import java.io.IO;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Random;

public class Griglia {
    private int[][] caselle;
    private Nave[] navi;
    private int numNavi;
    private final int ascisse;
    private final int ordinate;

    public Griglia(int numNavi) {
        ascisse = 10;
        ordinate = 10;
        this.numNavi = numNavi;
        navi = new Nave[this.numNavi];
    }

    private void creaNave(int indice, int lunghezza, boolean direzione, int[] coordinate_iniziali) throws InvalidParameterException {
        if (direzione && coordinate_iniziali.length != 2 || coordinate_iniziali[0] > ordinate - lunghezza || !direzione && coordinate_iniziali.length != 2 || coordinate_iniziali[1] > ascisse - lunghezza) throw new InvalidParameterException("Coordinate iniziali non valide");
        navi[indice] = new Nave(lunghezza, coordinate_iniziali, direzione);
    }

    //da finire
    public void chiediAttributiNaviConsole() {
        for(int i = 0; i < numNavi; i++) {
            System.out.println("Inserisci la lunghezza della nave");
            int lunghezza = Integer.parseInt(IO.readln());
            System.out.println("Inserisci la coordinata x");
            int x = Integer.parseInt(IO.readln());
            System.out.println("Inserisci la coordinata y");
            int y = Integer.parseInt(IO.readln());
            int[] coordinate_iniziali = {y, x};
            String scelta;
            boolean direzione;
            do {
                System.out.println("Scegli la direzione (1 = orizzonale, 0 = vericale)");
                scelta = IO.readln();
            } while (!scelta.equals("0") && !scelta.equals("1"));
            direzione = scelta.equals("1");

            creaNave(i, lunghezza, direzione, coordinate_iniziali);
        }
    }

    private void inizializzaCaselle() {
        for (int i = 0; i < ordinate; i++) {
            for(int j = 0; j < ascisse; j++) caselle[i][j] = 0;
        }
    }

    public void inserisciNavi() {
        for (int i = 0; i < ordinate; i++) {
            for(int j = 0; j < ascisse; j++) {
                for (Nave n : navi) {
                    if (Arrays.asList(n.getAscisse()).contains(j) && Arrays.asList(n.getOrdinate()).contains(i)) caselle[i][j] = 1;
                }
            }
        }
    }
}
