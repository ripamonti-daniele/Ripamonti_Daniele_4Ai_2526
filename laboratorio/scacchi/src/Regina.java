import java.awt.Color;

public class Regina extends Pedina {
    public Regina(Color colore, int[] posizione) {
        super(colore, posizione, 9);
    }

    public Regina(Regina originale) {
        super(originale);
    }

    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }

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
        return new Regina(this);
    }
}
