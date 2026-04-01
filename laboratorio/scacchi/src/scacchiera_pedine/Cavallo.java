package scacchiera_pedine;
import java.awt.Color;

public class Cavallo extends Pedina {
    public static final int MATERIALE = 3;

    public Cavallo(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        trovaMosseValide();
    }

    protected Cavallo(Cavallo originale) {
        super(originale);
    }

    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = -2; i <= 2; i += 4) {
            if (posizione[0] + i >= 0 && posizione[0] + i < DIMENSIONE_SCACCHIERA) {
                if (posizione[1] - 1 >= 0) mosseValide.add(new int[]{posizione[0] + i, posizione[1] - 1});
                if (posizione[1] + 1 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + i, posizione[1] + 1});
            }
            if (posizione[1] + i >= 0 && posizione[1] + i < DIMENSIONE_SCACCHIERA) {
                if (posizione[0] - 1 >= 0) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] + i});
                if (posizione[0] + 1 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] + i});
            }
        }
    }

    @Override
    public Pedina copy() {
        return new Cavallo(this);
    }
}
