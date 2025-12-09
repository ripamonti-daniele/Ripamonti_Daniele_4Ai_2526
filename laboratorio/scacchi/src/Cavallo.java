import java.awt.*;

public class Cavallo extends Pedina {
    public Cavallo(Color colore, int[] posizione) {
        super(colore, posizione, 3);
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (Math.abs(posizione_attuale[0] - posizione[0]) == 2 && Math.abs(posizione_attuale[1] - posizione[1]) == 1 || Math.abs(posizione_attuale[1] - posizione[1]) == 2 && Math.abs(posizione_attuale[0] - posizione[0]) == 1) setPosizione(posizione);
        else throw new IllegalArgumentException("Mossa non valida");
    }
}
