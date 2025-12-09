import java.awt.*;

public class Torre extends Pedina {
    private boolean arrocco;

    public Torre(Color colore, int[] posizione) {
        super(colore, posizione, 5);
        arrocco = true;
    }

    public boolean getArrocco() {
        return arrocco;
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (posizione[0] != posizione_attuale[0] && posizione[1] != posizione_attuale[1]) throw new IllegalArgumentException("Mossa non valida");
        setPosizione(posizione);
        arrocco = false;
    }

    //da implementare bene l'arrocco
}
