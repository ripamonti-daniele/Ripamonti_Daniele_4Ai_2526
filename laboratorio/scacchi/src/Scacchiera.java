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

    public Pedina[][] getScacchiera() {
        Pedina[][] copia = new Pedina[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < caselle.length; i++) {
            for (int j = 0; j < caselle[i].length; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = caselle[i][j].copy();
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
            int avanzamento = -1;
            if (pedina.getColore() == Color.white) avanzamento = 1;
            if (Math.abs(posizione[1] - pedina.getPosizione()[1]) == 1 && posizione[0] == pedina.getPosizione()[0] + avanzamento) {
                //mossa normale
                if (caselle[posizione[0]][posizione[1]] != null && caselle[posizione[0]][posizione[1]].getColore() != pedina.getColore()) {
                    caselle[posizione[0]][posizione[1]] = null;
                    pedina.muovi(posizione);
                }
                //en passant
                else if (caselle[posizione[0]][posizione[1]] == null && caselle[posizione[0]][posizione[1] - avanzamento].getColore() != pedina.getColore() && caselle[posizione[0]][posizione[1] - avanzamento] instanceof Pedone && ((Pedone) caselle[posizione[0]][posizione[1] - avanzamento]).getEnpassant()) {
                    caselle[posizione[0]][posizione[1] - avanzamento] = null;
                    pedina.muovi(posizione);
                }
            }

            //mossa normale
            else if (mosseIntermedieValide(pedina, posizione, false, true, 0, avanzamento) && caselle[posizione[0]][posizione[1]] == null) {
                caselle[pedina.getPosizione()[0]][pedina.getPosizione()[0]] = null;
                pedina.muovi(posizione);
            }

            else throw new IllegalArgumentException("Mossa non valida");

            //promozione
            if (pedina.getColore() == Color.white && pedina.getPosizione()[0] == DIMENSIONE - 1 || pedina.getColore() == Color.black && pedina.getPosizione()[0] == 0) {
                promuoviPedone(pedina);
            }
        }

        else if (pedina instanceof Torre) {
            int avanzamento = 1;
            if (posizione[0] == pedina.getPosizione()[0]) {
                if (posizione[1] - pedina.getPosizione()[1] < 0) {
                    avanzamento = -1;

                }
            }
        }
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
}
