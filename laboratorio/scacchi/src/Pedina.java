import java.awt.Color;

public abstract class Pedina {
    private Color colore;
    private int[] posizione;
    private int materiale;

    public Pedina(Color colore, int[] posizione) {
        setColore(colore);
        setPosizione(posizione);
    }

    public Color getColore() {
        return colore;
    }

    public void setColore(Color colore) {
        if (colore != Color.white && colore != Color.black) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    public int[] getPosizione() {
        return posizione.clone();
    }

    private void setPosizione(int[] posizione) {
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > 7) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
        if (posizione[0] > 2 && posizione[0] < 6) throw new IllegalArgumentException("Posizione iniziale non valida");
        this.posizione = posizione;
    }

    public int getMateriale() {
        return materiale;
    }

    public abstract void muovi();
}
