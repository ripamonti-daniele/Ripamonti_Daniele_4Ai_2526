import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Pedina {
    protected Color colore;
    protected int[] posizione;
    protected int materiale;
    protected List<int[]> mosseValide;
    public final int DIMENSIONE_SCACCHIERA = 8;

    public Pedina(Color colore, int[] posizione) {
        setColore(colore);
        setPosizione(posizione);
        mosseValide = new ArrayList<>();
        trovaMosseValide();
    }

    public Pedina(Color colore, int[] posizione, int materiale) {
        setColore(colore);
        setPosizione(posizione);
        setMateriale(materiale);
        mosseValide = new ArrayList<>();
        trovaMosseValide();
    }

    protected Pedina(Pedina originale) {
        this.colore = originale.colore;
        this.posizione = originale.posizione;
        this.materiale = originale.materiale;
        this.mosseValide = originale.mosseValide;
    }

    public Color getColore() {
        return colore;
    }

    private void setColore(Color colore) {
        if (!colore.equals(Color.white) && !colore.equals(Color.black)) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    public int[] getPosizione() {
        return posizione.clone();
    }

    protected void setPosizione(int[] posizione) {
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
        if (this.posizione != null && this.posizione[0] == posizione[0] && this.posizione[1] == posizione[1]) throw new IllegalArgumentException("La pedina si trova già sulla casella che hai scelto");
        this.posizione = posizione;
    }

    public int getMateriale() {
        return materiale;
    }

    private void setMateriale(int materiale) {
        if (materiale <= 0) throw new IllegalArgumentException("Il materiale deve essere maggiore di 0");
        this.materiale = materiale;
    }

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
        trovaMosseValide();
    }

    public List<int[]> getMosseValide() {
        trovaMosseValide();
        List<int[]> copia = new ArrayList<>(mosseValide.size());
        for (int[] arr : mosseValide) copia.add(arr.clone());
        return copia;
    }

    protected abstract void trovaMosseValide(); //sistema quando vengono trovate le mosse valide

    public abstract Pedina copy();
}
