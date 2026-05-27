package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta un alfiere nel gioco degli scacchi.
 * <p>
 * L'alfiere si sposta di un numero arbitrario di caselle lungo le quattro
 * diagonali. Il filtraggio delle destinazioni bloccate da pedine interposte
 * è delegato alla {@link Scacchiera}.
 * </p>
 *
 * @see Pedina
 * @see Scacchiera
 */
public class Alfiere extends Pedina {

    /** Valore materiale dell'alfiere. */
    public static final int MATERIALE = 3;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce un alfiere con il colore e la posizione indicati.
     * Le mosse valide vengono calcolate immediatamente.
     *
     * @param colore    colore dell'alfiere; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Alfiere(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea un nuovo alfiere con lo stesso stato
     * dell'originale.
     *
     * @param originale l'alfiere da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Alfiere(Alfiere originale) {
        super(originale);
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista delle mosse valide in base alla posizione
     * corrente dell'alfiere.
     * <p>
     * Vengono generate tutte le caselle raggiungibili lungo le quattro diagonali
     * (in basso a destra, in basso a sinistra, in alto a destra, in alto a
     * sinistra), fino al bordo della scacchiera. Le destinazioni bloccate da
     * pedine interposte vengono rimosse dalla {@link Scacchiera} prima di
     * presentare le mosse al giocatore.
     * </p>
     */
    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        int y = posizione[0];
        int x = posizione[1];
        for (int i = 1; i < DIMENSIONE_SCACCHIERA; i++) {
            boolean esci = true;
            if (y + i < DIMENSIONE_SCACCHIERA && x + i < DIMENSIONE_SCACCHIERA) {
                mosseValide.add(new int[]{y + i, x + i});
                esci = false;
            }
            if (y + i < DIMENSIONE_SCACCHIERA && x - i >= 0) {
                mosseValide.add(new int[]{y + i, x - i});
                esci = false;
            }
            if (y - i >= 0 && x + i < DIMENSIONE_SCACCHIERA) {
                mosseValide.add(new int[]{y - i, x + i});
                esci = false;
            }
            if (y - i >= 0 && x - i >= 0) {
                mosseValide.add(new int[]{y - i, x - i});
                esci = false;
            }
            if (esci) break;
        }
    }

    /**
     * Restituisce una copia profonda e indipendente di questo alfiere.
     *
     * @return nuova istanza di {@code Alfiere} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Alfiere(this);
    }
}
