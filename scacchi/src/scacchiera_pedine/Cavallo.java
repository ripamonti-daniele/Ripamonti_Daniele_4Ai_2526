package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta un cavallo nel gioco degli scacchi.
 * <p>
 * Il cavallo si sposta con una mossa a forma di «L»: due caselle in una
 * direzione ortogonale e una nella direzione perpendicolare (o viceversa).
 * È l'unico pezzo che può saltare le pedine interposte, quindi non richiede
 * alcun filtraggio aggiuntivo da parte della {@link Scacchiera}.
 * </p>
 *
 * @see Pedina
 * @see Scacchiera
 */
public class Cavallo extends Pedina {

    /** Valore materiale del cavallo. */
    public static final int MATERIALE = 3;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce un cavallo con il colore e la posizione indicati.
     * Le mosse valide vengono calcolate immediatamente.
     *
     * @param colore    colore del cavallo; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Cavallo(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea un nuovo cavallo con lo stesso stato
     * dell'originale.
     *
     * @param originale il cavallo da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Cavallo(Cavallo originale) {
        super(originale);
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista delle mosse valide in base alla posizione
     * corrente del cavallo.
     * <p>
     * Vengono generate tutte le destinazioni raggiungibili con la mossa a «L»:
     * scostamento di ±2 righe e ±1 colonna, oppure ±2 colonne e ±1 riga,
     * escludendo le caselle fuori dai limiti della scacchiera. Poiché il cavallo
     * salta i pezzi interposti, non è necessario alcun ulteriore filtraggio da
     * parte della {@link Scacchiera}.
     * </p>
     */
    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = -2; i <= 2; i += 4) {
            if (posizione[0] + i >= 0 && posizione[0] + i < DIMENSIONE_SCACCHIERA) {
                if (posizione[1] - 1 >= 0) mosseValide.add(new int[]{posizione[0] + i, posizione[1] - 1});
                if (posizione[1] + 1 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + i, posizione[1] + 1});
            }
            if (posizione[1] + i >= 0 && posizione[1] + i < DIMENSIONE_SCACCHIERA) {
                if (posizione[0] - 1 >= 0) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] + i});
                if (posizione[0] + 1 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] + i});
            }
        }
    }

    /**
     * Restituisce una copia profonda e indipendente di questo cavallo.
     *
     * @return nuova istanza di {@code Cavallo} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Cavallo(this);
    }
}