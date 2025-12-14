import java.awt.Color;

public class Regina extends Pedina {
    public Regina(Color colore, int[] posizione) {
        super(colore, posizione, 9);
    }

    public Regina(Regina originale) {
        super(originale);
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (posizione[0] != posizione_attuale[0] && posizione[1] != posizione_attuale[1] && Math.abs(posizione[0] - posizione_attuale[0]) != Math.abs(posizione[1] - posizione_attuale[1])) throw new IllegalArgumentException("Mossa non valida");
        setPosizione(posizione);
    }

    @Override
    public Pedina copy() {
        return new Regina(this);
    }
}
