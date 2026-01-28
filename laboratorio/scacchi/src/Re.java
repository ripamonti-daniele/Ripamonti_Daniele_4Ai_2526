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
            mosseValide.add(new int[] {posizione[0], posizione[1] + 2});
            mosseValide.add(new int[] {posizione[0], posizione[1] - 2});
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
        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == posizione[0] && mossa[1] == posizione[1]) {
                valido = true;
                break;
            }
        }
        if (!valido) {
            throw new IllegalArgumentException("Questa mossa non è valida");
        }
        setPosizione(posizione);
        arrocco = false;
    }

    @Override
    public Pedina copy() {
        return new Re(this);
    }
}
