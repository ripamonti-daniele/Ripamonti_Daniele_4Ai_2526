import java.awt.*;

public class Cavallo extends Pedina {
    public Cavallo(Color colore, int[] posizione) {
        super(colore, posizione, 3);
    }

    public Cavallo(Cavallo originale) {
        super(originale);
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (Math.abs(posizione_attuale[0] - posizione[0]) == 2 && Math.abs(posizione_attuale[1] - posizione[1]) == 1 || Math.abs(posizione_attuale[1] - posizione[1]) == 2 && Math.abs(posizione_attuale[0] - posizione[0]) == 1) setPosizione(posizione);
        else throw new IllegalArgumentException("Mossa non valida");
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
                if (posizione[0] - 1 >= 0) mosseValide.add(new int[]{posizione[1] + i, posizione[0] - 1});
                if (posizione[0] + 1 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[1] + i, posizione[0] + 1});
            }
        }
    }

    @Override
    public Pedina copy() {
        return new Cavallo(this);
    }
}
