import java.awt.Color;

public abstract class Pedina {
    private Color colore;
    private int[] posizione;
    private int materiale;
    public final int DIMENSIONE_SCACCHIERA = 8;

    public Pedina(Color colore, int[] posizione) {
        setColore(colore);
        setPosizione(posizione);
    }

    public Pedina(Color colore, int[] posizione, int materiale) {
        setColore(colore);
        setPosizione(posizione);
        setMateriale(materiale);
    }

    public Color getColore() {
        return colore;
    }

    private void setColore(Color colore) {
        if (colore != Color.white && colore != Color.black) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    public int[] getPosizione() {
        return posizione.clone();
    }

    protected void setPosizione(int[] posizione) {
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
        if (this.posizione == posizione) throw new IllegalArgumentException("La pedina si trova già sulla casella che hai scelto");
        this.posizione = posizione;
    }

    public int getMateriale() {
        return materiale;
    }

    private void setMateriale(int materiale) {
        if (materiale <= 0) throw new IllegalArgumentException("Il materiale deve essere maggiore di 0");
        this.materiale = materiale;
    }

    public abstract void muovi(int[] posizione);
}
