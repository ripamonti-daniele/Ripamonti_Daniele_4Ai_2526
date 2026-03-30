package scacchiera_pedine;
import java.awt.Color;

public class Torre extends Pedina {
    private boolean arrocco;
    public static final int MATERIALE = 5;

    public Torre(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        arrocco = true;
        trovaMosseValide();
    }

    protected Torre(Torre originale) {
        super(originale);
        arrocco = originale.arrocco;
    }

    public boolean getArrocco() {
        return arrocco;
    }

    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }
    }

    @Override
    public void muovi(int[] posizione) {
        arrocco = false;
        super.muovi(posizione);
    }

    @Override
    public Pedina copy() {
        return new Torre(this);
    }
}
