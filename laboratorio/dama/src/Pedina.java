import java.awt.Color;

public class Pedina {
    private Color colore;
    private TipoPedina tipo;
    private int riga;
    private int colonna;

    public Pedina(Color colore, int riga, int colonna) {
        setColore(colore);
        setRiga(riga);
        setColonna(colonna);
        tipo = TipoPedina.DAMA;
    }

    public Pedina(Pedina originale) {
        this.colore = originale.colore;
        this.riga = originale.riga;
        this.colonna = originale.colonna;
        this.tipo = originale.tipo;
    }

    public int getColonna() {
        return colonna;
    }

    private void setColonna(int colonna) {
        if (colonna < 0 || colonna > 7) throw new IllegalArgumentException("Colonna non valida");
        this.colonna = colonna;
    }

    public int getRiga() {
        return riga;
    }

    private void setRiga(int riga) {
        if (riga < 0 || riga > 7) throw new IllegalArgumentException("Riga non valida");
        this.riga = riga;
    }

    public Color getColore() {
        return colore;
    }

    private void setColore(Color colore) {
        if (colore != Color.black && colore != Color.white) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    public TipoPedina getTipo() {
        return tipo;
    }

    private void promuovi() {
        if (tipo == TipoPedina.DAMONE) return;
        if (colore == Color.black && colonna == 0 || colore == Color.white && colonna == 7) tipo = TipoPedina.DAMONE;
    }

    public void muovi(int riga, int colonna, boolean mangia) {
        if (riga < 0 || colonna < 0 || riga > 7 || colonna > 7) throw new IllegalArgumentException("Questa posizione non esiste");

        if (tipo == TipoPedina.DAMONE) {
            if ((!mangia && Math.abs(riga - this.riga) == 1 && Math.abs(colonna - this.colonna) == 1) || (mangia && Math.abs(riga - this.riga) == 2 && Math.abs(colonna - this.colonna) == 2)) {
                this.riga = riga;
                this.colonna = colonna;
            }
            else throw new IllegalArgumentException("Mossa non valida");
        }

        else {
            int differenza = 1;
            if (colore == Color.black) differenza = -1;

            if ((!mangia && this.riga - riga == differenza && Math.abs(colonna - this.colonna) == 1) || (mangia && this.riga - riga == differenza * 2 && Math.abs(colonna - this.colonna) == 2)) {
                this.riga = riga;
                this.colonna = colonna;
            }
            else throw new IllegalArgumentException("Mossa non valida");
            promuovi();
        }
    }
}
