import java.awt.Color;

public class Pedina {
    private Color colore;
    private TipoPedina tipo;
    private int riga;
    private int colonna;

    public Pedina(Color colore, int riga, int colonna) {
        setColore(colore);
        tipo = TipoPedina.DAMA;
    }

    public Pedina(Pedina originale) {
        this.colore = originale.colore;
        this.riga = originale.riga;
        this.colonna = originale.colonna;
        this.tipo = originale.tipo;
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

    public void promuovi() {
        if (tipo == TipoPedina.DAMONE) return;
        if (colore == Color.black && colonna == 0 || colore == Color.white && colonna == 7) tipo = TipoPedina.DAMONE;
        else throw new IllegalStateException("la pedina non è arrivata in fondo alla scacchiera");
    }

    public void muovi(int riga, int colonna) {
        if (riga < 0 || colonna < 0 || riga > 7 || colonna > 7) throw new IllegalArgumentException("Questa posizione non esiste");

    }
}
