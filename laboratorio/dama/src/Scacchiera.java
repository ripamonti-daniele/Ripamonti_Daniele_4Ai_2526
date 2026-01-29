import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scacchiera {
    private final Pedina[][] pedine;
    private final List<int[]> mosseObbligatorie;

    public Scacchiera() {
        pedine = new Pedina[8][8];
        mosseObbligatorie = new ArrayList<>();
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

        boolean bianche = false;
        boolean nere = false;
        boolean trovato = false;
        for (int[] pos : mosseObbligatorie) {
            bianche = bianche || (pedine[pos[0]][pos[1]].getColore() == Color.white);
            nere = nere || (pedine[pos[0]][pos[1]].getColore() == Color.black);
            if (pos[0] == colonna_old && pos[1] == riga_old) {
                trovato = true;
                break;
            }
        }

        if (!trovato && (pedine[colonna_old][riga_old].getColore() == Color.white && bianche) || (pedine[colonna_old][riga_old].getColore() == Color.black && nere)) throw new IllegalArgumentException("Ci sono delle pedine obbligate a mangiare");
    }

    public void muovi(int riga_old, int colonna_old, int riga_new, int colonna_new) {
        colonna_old--;
        riga_old--;
        colonna_new--;
        riga_new--;
        controllaMossaValida(riga_old, colonna_old, riga_new, colonna_new);
        if (!mosseObbligatorie.isEmpty()) throw new IllegalStateException("Non puoi fare questa mossa: sei obbligato a mangiare una pedina");

        pedine[colonna_old][riga_old].muovi(riga_new, colonna_new, false);
        pedine[colonna_new][riga_new] = pedine[colonna_old][riga_old];
        pedine[colonna_old][riga_old] = null;
        trovaMosseObbligatorie(-1, -1);
    }

    public void mangia(int riga_old, int colonna_old, int riga_new, int colonna_new) {
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
        trovaMosseObbligatorie(colonna_new, riga_new);
    }

    private boolean controllaAltoSx(int colonna, int riga) {
        Pedina p = pedine[colonna][riga];
        if (p.getColonna() - 1 > 0 && p.getRiga() - 1 > 0) return (pedine[p.getColonna() - 1][p.getRiga() - 1] != null && pedine[p.getColonna()][p.getRiga()].getColore() != pedine[p.getColonna() - 1][p.getRiga() - 1].getColore() && pedine[p.getColonna() - 1][p.getRiga() - 1].getColore() != p.getColore() && pedine[p.getColonna() - 2][p.getRiga() - 2] == null);
        return false;
    }

    private boolean controllaAltoDx(int colonna, int riga) {
        Pedina p = pedine[colonna][riga];
        if (p.getColonna() - 1 > 0 && p.getRiga() + 1 < 7) return (pedine[p.getColonna() - 1][p.getRiga() + 1] != null && pedine[p.getColonna()][p.getRiga()].getColore() != pedine[p.getColonna() - 1][p.getRiga() + 1].getColore() && pedine[p.getColonna() - 1][p.getRiga() + 1].getColore() != p.getColore() && pedine[p.getColonna() - 2][p.getRiga() + 2] == null);
        return false;
    }

    private boolean controllaBassoSx(int colonna, int riga) {
        Pedina p = pedine[colonna][riga];
        if (p.getColonna() + 1 < 7 && p.getRiga() - 1 > 0) return (pedine[p.getColonna() + 1][p.getRiga() - 1] != null && pedine[p.getColonna()][p.getRiga()].getColore() != pedine[p.getColonna() + 1][p.getRiga() - 1].getColore() && pedine[p.getColonna() + 1][p.getRiga() - 1].getColore() != p.getColore() && pedine[p.getColonna() + 2][p.getRiga() - 2] == null);
        return false;
    }

    private boolean controllaBassoDx(int colonna, int riga) {
        Pedina p = pedine[colonna][riga];
        if (p.getColonna() + 1 < 7 && p.getRiga() + 1 < 7) return (pedine[p.getColonna() + 1][p.getRiga() + 1] != null && pedine[p.getColonna()][p.getRiga()].getColore() != pedine[p.getColonna() + 1][p.getRiga() + 1].getColore() && pedine[p.getColonna() + 1][p.getRiga() + 1].getColore() != p.getColore() && pedine[p.getColonna() + 2][p.getRiga() + 2] == null);
        return false;
    }

    private void trovaMosseObbligatoriePerPedina(int colonna, int riga) {
        Pedina p = pedine[colonna][riga];
        if (p.getTipo() == TipoPedina.DAMONE) {
            boolean esito = controllaAltoDx(colonna, riga);
            esito = esito || controllaAltoSx(colonna, riga);
            esito = esito || controllaBassoDx(colonna, riga);
            esito = esito || controllaBassoSx(colonna, riga);
            if (esito) mosseObbligatorie.add(new int[] {colonna, riga});
        }

        else if (p.getColore() == Color.white) {
            boolean esito = controllaAltoDx(colonna, riga);
            esito = esito ||  controllaAltoSx(colonna, riga);
            if (esito) mosseObbligatorie.add(new int[] {colonna, riga});
        }

        else {
            boolean esito = controllaAltoDx(colonna, riga);
            esito = esito ||  controllaAltoSx(colonna, riga);
            if (esito) mosseObbligatorie.add(new int[] {colonna, riga});
        }
    }

    private void trovaMosseObbligatorie(int ultimaMangianteY, int ultimaMangianteX) {
        mosseObbligatorie.clear();
        if (ultimaMangianteY < 8 && ultimaMangianteX < 8 && ultimaMangianteY >= 0 && ultimaMangianteX >= 0 && pedine[ultimaMangianteY][ultimaMangianteX] != null) {
            trovaMosseObbligatoriePerPedina(ultimaMangianteY, ultimaMangianteX);
        }

        else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (i + j % 2 != 0) trovaMosseObbligatoriePerPedina(i, j);
                }
            }
        }
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
