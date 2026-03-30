package scacchiera_pedine;
import java.awt.Color;

public class Alfiere extends Pedina {
    public static final int MATERIALE = 3;

    public Alfiere(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
    }

    protected Alfiere(Alfiere originale) {
        super(originale);
    }

    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        int y = posizione[0];
        int x = posizione[1];
        for (int i = 1; i < DIMENSIONE_SCACCHIERA; i++) {
            boolean esci = true;
            if (y + i < DIMENSIONE_SCACCHIERA && x + i < DIMENSIONE_SCACCHIERA) {
                mosseValide.add(new int[]{y + i, x + i});
                esci = false;
            }
            if (y + i < DIMENSIONE_SCACCHIERA && x - i >= 0) {
                mosseValide.add(new int[]{y + i, x - i});
                esci = false;
            }
            if (y - i >= 0 && x + i < DIMENSIONE_SCACCHIERA) {
                mosseValide.add(new int[]{y - i, x + i});
                esci = false;
            }
            if (y - i >= 0 && x - i >= 0) {
                mosseValide.add(new int[]{y - i, x - i});
                esci = false;
            }
            if (esci) break;
        }
    }

    @Override
    public Pedina copy() {
        return new Alfiere(this);
    }
}
