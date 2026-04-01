package scacchiera_pedine;
import java.awt.Color;

public class Regina extends Pedina {
    public static final int MATERIALE = 9;

    public Regina(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        trovaMosseValide();
    }

    protected Regina(Regina originale) {
        super(originale);
    }

    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }

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
        return new Regina(this);
    }
}
