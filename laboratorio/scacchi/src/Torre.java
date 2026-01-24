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

        if (arrocco && posizione[1] == DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0], posizione[1] - 2});
        else if (arrocco && posizione[1] == 0) mosseValide.add(new int[]{posizione[0], posizione[1] + 3});

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }
    }

    @Override
    public void muovi(int[] posizione) {
        if (!mosseValide.contains(posizione)) throw new IllegalArgumentException("Questa mossa non è valida");
        setPosizione(posizione);
        arrocco = false;
    }

    @Override
    public Pedina copy() {
        return new Torre(this);
    }

    //da implementare bene l'arrocco
}
