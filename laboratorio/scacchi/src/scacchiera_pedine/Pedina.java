package scacchiera_pedine;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Pedina {
    private Color colore;
    protected int[] posizione;
    private int materiale;
    protected List<int[]> mosseValide;
    public final int DIMENSIONE_SCACCHIERA = 8;

    public Pedina(Color colore, int[] posizione) {
        setColore(colore);
        setPosizione(posizione);
        mosseValide = new ArrayList<>();
    }

    public Pedina(Color colore, int[] posizione, int materiale) {
        this(colore, posizione);
        setMateriale(materiale);
    }

    protected Pedina(Pedina originale) {
        if (originale == null) throw new IllegalArgumentException("La pedina originale non può essere null");
        this.colore = originale.colore;
        this.materiale = originale.materiale;
        this.posizione = originale.posizione.clone();
        this.mosseValide = originale.getMosseValide();
    }

    public Color getColore() {
        return colore;
    }

    private void setColore(Color colore) {
        if (colore == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!colore.equals(Color.white) && !colore.equals(Color.black)) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    public int[] getPosizione() {
        return posizione.clone();
    }

    private void setPosizione(int[] posizione) {
        if (posizione == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
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
        if (!valido) throw new IllegalArgumentException("Questa mossa non è valida");

        setPosizione(posizione);
        trovaMosseValide();
    }

    public List<int[]> getMosseValide() {
        List<int[]> copia = new ArrayList<>(mosseValide.size());
        for (int[] arr : mosseValide) copia.add(arr.clone());
        return copia;
    }

    protected abstract void trovaMosseValide(); //sistema quando vengono trovate le mosse valide

    public abstract Pedina copy();
}
