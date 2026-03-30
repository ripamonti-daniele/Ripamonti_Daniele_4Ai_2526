package scacchiera_pedine;
import java.awt.Color;

public class Pedone extends Pedina {
    private boolean muoviDiDueCaselle;
    private boolean enPassant;
    public static final int MATERIALE = 1;

    public Pedone(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        muoviDiDueCaselle = true;
        enPassant = false;
        trovaMosseValide();
    }

    protected Pedone(Pedone originale) {
        super(originale);
        this.muoviDiDueCaselle = originale.muoviDiDueCaselle;
        this.enPassant = originale.enPassant;
    }

    public boolean getMuoviDiDueCaselle() {
        return muoviDiDueCaselle;
    }

    public boolean getEnPassant() {
        return enPassant;
    }

    public void rimuoviEnPassant() {
        enPassant = false;
    }

    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        if (getColore() == Color.white) {
            if (posizione[0] > 0) {
                mosseValide.add(new int[]{posizione[0] - 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle && posizione[0] - 2 >= 0) mosseValide.add(new int[]{posizione[0] - 2, posizione[1]});
        }

        else {
            if (posizione[0] < DIMENSIONE_SCACCHIERA - 1) {
                mosseValide.add(new int[]{posizione[0] + 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle && posizione[0] + 2 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + 2, posizione[1]});
        }
    }

    @Override
    public void muovi(int[] posizione) {
        boolean temp = Math.abs(posizione[0] - this.posizione[0]) == 2;
        muoviDiDueCaselle = false;
        super.muovi(posizione);
        enPassant = temp;
    }

    @Override
    public Pedina copy() {
        return new Pedone(this);
    }
}
