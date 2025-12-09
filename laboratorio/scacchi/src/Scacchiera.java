import java.awt.*;

public class Scacchiera {
    public final int DIMENSIONE = 8;
    private final Pedina[][] caselle;
    private int mosseNeutre;

    public Scacchiera() {
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
        System.gc();
    }

    public void muoviPedina(Pedina pedina, int[] posizione) {
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
        if (pedina instanceof Cavallo) {
            if (caselle[posizione[0]][posizione[1]].getColore() == pedina.getColore()) throw new IllegalArgumentException("Il cavallo non può essere messo in una casella dove si trova una pedina del suo stesso colore");
            else {
                caselle[posizione[0]][posizione[1]] = caselle[pedina.getPosizione()[0]][pedina.getPosizione()[1]];
                caselle[pedina.getPosizione()[0]][pedina.getPosizione()[1]] = null;
                pedina.muovi(posizione);
            }
        }
        else if (pedina instanceof Pedone) {
            //promozione
            //cattura
//            if (posizione[1] - pedina.getPosizione()[1] == 1 && posizione[0] == pedina.getPosizione()[0] && caselle[posizione[0]][posizione[1]] != null && caselle[posizione[0]][posizione[1]].getColore() != pedina.getColore()) {
//
//            }
            //enpassant
        }
    }
}
