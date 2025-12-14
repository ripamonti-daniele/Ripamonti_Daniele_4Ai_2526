import java.awt.Color;

public class Re extends Pedina {
    private boolean arrocco;

    public Re(Color colore, int[] posizione) {
        super(colore, posizione);
        arrocco = true;
    }

    public Re(Re originale) {
        super(originale);
        this.arrocco = originale.arrocco;
    }

    public boolean getArrocco() {
        return arrocco;
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (Math.abs(posizione_attuale[0] - posizione[0]) > 1 || Math.abs(posizione_attuale[1] - posizione[1]) > 1) throw new IllegalArgumentException("Mossa non valida");
        setPosizione(posizione);
        arrocco = false;
    }

    @Override
    public Pedina copy() {
        return new Re(this);
    }
}
