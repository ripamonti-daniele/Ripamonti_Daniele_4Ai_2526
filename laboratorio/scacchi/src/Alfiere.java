import java.awt.Color;

public class Alfiere extends Pedina {
    public Alfiere(Color colore, int[] posizione) {
        super(colore, posizione, 3);
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (Math.abs(posizione[0] - posizione_attuale[0]) != Math.abs(posizione[1] - posizione_attuale[1])) throw new IllegalArgumentException("Mossa non valida");
        setPosizione(posizione);
    }
}
