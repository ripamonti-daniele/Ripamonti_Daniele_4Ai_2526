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
    protected void trovaMosseValide() {
        mosseValide.clear();

        if (arrocco) {
            if (posizione[1] + 2 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[] {posizione[0], posizione[1] + 2});
            if (posizione[1] - 2 >= 0) mosseValide.add(new int[] {posizione[0], posizione[1] - 2});
        }

        for (int i = -1; i <= 1; i++) {
            if (posizione[0] + i < 0 || posizione[0] + i >= DIMENSIONE_SCACCHIERA) continue;
            for (int j = -1; j <= 1; j++) {
                if (posizione[1] + j < 0 || posizione[1] + j >= DIMENSIONE_SCACCHIERA || i == 0 && j == 0) continue;
                mosseValide.add(new int[] {posizione[0] + i, posizione[1] + j});
            }
        }
    }

    @Override
    public void muovi(int[] posizione) {
        super.muovi(posizione);
        arrocco = false;
    }

    @Override
    public Pedina copy() {
        return new Re(this);
    }
}
