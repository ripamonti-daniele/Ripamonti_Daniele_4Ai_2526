import java.awt.Color;

public class Torre extends Pedina {
    private boolean arrocco;

    public Torre(Color colore, int[] posizione) {
        super(colore, posizione, 5);
        arrocco = true;
    }

    public Torre(Torre originale) {
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
        super.muovi(posizione);
        arrocco = false;
    }

    @Override
    public Pedina copy() {
        return new Torre(this);
    }
}
