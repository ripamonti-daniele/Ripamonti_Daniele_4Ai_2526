import java.awt.*;

public class Scacchiera {
    private final Pedina[][] pedine;

    public Scacchiera() {
        pedine = new Pedina[8][8];
        reset();
    }

    public void reset() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) continue;
                if (i <= 2) pedine[i][j] = new Pedina(Color.black, j, i);
                else if (i >= 5) pedine[i][j] = new Pedina(Color.white, j, i);
            }
        }
    }

    private void controllaMossaValida (int riga_old, int colonna_old, int riga_new, int colonna_new) {
        if (riga_old < 0 || riga_old > 7 || colonna_old < 0 || colonna_old > 7) throw new IllegalArgumentException("La posizione iniziale non esiste");
        if (riga_new < 0 || riga_new > 7 || colonna_new < 0 || colonna_new > 7) throw new IllegalArgumentException("La posizione finale non esiste");
        if (pedine[colonna_old][riga_old] == null) throw new IllegalStateException("Non c'è nessuna pedina nella casella che hai selezionato");
        if (pedine[colonna_new][riga_new] != null) throw new IllegalStateException("La casella che hai selezionato è già occupata");
    }

    //se una pedina può mangiare allora è costretta a farlo --> da implementare
    public void muovi(int riga_old, int colonna_old, int riga_new, int colonna_new) {
        colonna_old--;
        riga_old--;
        colonna_new--;
        riga_new--;
        controllaMossaValida(riga_old, colonna_old, riga_new, colonna_new);

        pedine[colonna_old][riga_old].muovi(riga_new, colonna_new, false);
        pedine[colonna_new][riga_new] = pedine[colonna_old][riga_old];
        pedine[colonna_old][riga_old] = null;
    }

    public boolean mangia(int riga_old, int colonna_old, int riga_new, int colonna_new) {
        colonna_old--;
        riga_old--;
        colonna_new--;
        riga_new--;
        controllaMossaValida(riga_old, colonna_old, riga_new, colonna_new);

        float media_riga = (riga_new + riga_old) / 2f;
        float media_col = (colonna_new + colonna_old) / 2f;
        if ((media_riga != riga_old + 1 && media_riga != riga_old - 1) || (media_col != colonna_old + 1 && media_col != colonna_old - 1)) throw new IllegalArgumentException("La pedina non può mangiare in queste caselle");
        if (pedine[(int) media_col][(int) media_riga] == null) throw new IllegalArgumentException("La pedina non sta mangiano una pedina avversaria");
        if (pedine[(int) media_col][(int) media_riga].getColore() == pedine[colonna_old][riga_old].getColore()) throw new IllegalArgumentException("La pedina non può mangiare una pedina del suo stesso colore");

        pedine[colonna_old][riga_old].muovi(riga_new, colonna_new, true);
        pedine[colonna_new][riga_new] = pedine[colonna_old][riga_old];
        pedine[(int) media_col][(int) media_riga] = null;
        pedine[colonna_old][riga_old] = null;

        //ritorna true se la pedina può mangiare ancora
        return false;
    }

    public Pedina[][] getPedine() {
        Pedina[][] copia = new Pedina[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pedine[i][j] != null) {
                    copia[i][j] = new Pedina(pedine[i][j]);
                }
            }
        }
        return copia;
    }

    //0 la partita va avanti 1 vince il bianco 2 vince il nero
    public int controllaVittoria() {
        return 0;
    }
}
