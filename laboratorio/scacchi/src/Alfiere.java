import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Alfiere extends Pedina {
    public Alfiere(Color colore, int[] posizione) {
        super(colore, posizione, 3);
    }

    public Alfiere(Alfiere originale) {
        super(originale);
    }

    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        int i = posizione[0] + 1;
        int j = posizione[1] + 1;
        while (i < DIMENSIONE_SCACCHIERA && j < DIMENSIONE_SCACCHIERA) {
            mosseValide.add(new int[]{i, j});
            i++;
            j++;
        }

        i = posizione[0] + 1;
        j = posizione[1] - 1;
        while (i < DIMENSIONE_SCACCHIERA && j >= 0) {
            mosseValide.add(new int[]{i, j});
            i++;
            j--;
        }

        i = posizione[0] - 1;
        j = posizione[1] + 1;
        while (i >= 0 && j < DIMENSIONE_SCACCHIERA) {
            mosseValide.add(new int[]{i, j});
            i--;
            j++;
        }

        i = posizione[0] - 1;
        j = posizione[1] - 1;
        while (i >= 0 && j >= 0) {
            mosseValide.add(new int[]{i, j});
            i--;
            j--;
        }
    }

    @Override
    public Pedina copy() {
        return new Alfiere(this);
    }
}
