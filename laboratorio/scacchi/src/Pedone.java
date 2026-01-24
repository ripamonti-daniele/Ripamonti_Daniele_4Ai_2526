import java.awt.Color;

public class Pedone extends Pedina {
    private boolean muoviDiDueCaselle;
    private boolean enpassant;

    public Pedone(Color colore, int[] posizione) {
        super(colore, posizione, 1);
        muoviDiDueCaselle = true;
        enpassant = false;
    }

    public Pedone(Pedone originale) {
        super(originale);
        this.muoviDiDueCaselle = originale.muoviDiDueCaselle;
        this.enpassant = originale.enpassant;
    }

    public boolean getMuoviDiDueCaselle() {
        return muoviDiDueCaselle;
    }

    public boolean getEnpassant() {
        return enpassant;
    }

    public void rimuoviEnpassant() {
        enpassant = false;
    }

    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        if (colore == Color.white) {
            if (posizione[0] < DIMENSIONE_SCACCHIERA - 1) {
                mosseValide.add(new int[]{posizione[0] + 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle) mosseValide.add(new int[]{posizione[0] + 2, posizione[1]});
        }

        else {
            if (posizione[0] > 0) {
                mosseValide.add(new int[]{posizione[0] - 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle) mosseValide.add(new int[]{posizione[0] - 2, posizione[1]});
        }
    }

    @Override
    public void muovi(int[] posizione) {
        if (!mosseValide.contains(posizione)) throw new IllegalArgumentException("Questa mossa non è valida");
        if (colore == Color.white && posizione[0] == this.posizione[0] + 2 || colore == Color.black && posizione[0] == this.posizione[0] - 2) enpassant = true;
        muoviDiDueCaselle = false;
        setPosizione(posizione);
    }

    @Override
    public Pedina copy() {
        return new Pedone(this);
    }
}
