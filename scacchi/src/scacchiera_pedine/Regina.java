package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta la regina nel gioco degli scacchi.
 * <p>
 * La regina combina i movimenti della torre e dell'alfiere: può spostarsi di
 * un numero arbitrario di caselle lungo righe, colonne e diagonali. Il
 * filtraggio delle destinazioni bloccate da pedine interposte è delegato alla
 * {@link Scacchiera}.
 * </p>
 *
 * @see Pedina
 * @see Scacchiera
 */
public class Regina extends Pedina {

    /** Valore materiale della regina. */
    public static final int MATERIALE = 9;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce una regina con il colore e la posizione indicati.
     * Le mosse valide vengono calcolate immediatamente.
     *
     * @param colore    colore della regina; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Regina(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea una nuova regina con lo stesso stato
     * dell'originale.
     *
     * @param originale la regina da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Regina(Regina originale) {
        super(originale);
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista delle mosse valide in base alla posizione
     * corrente della regina.
     * <p>
     * Le mosse generate comprendono:
     * <ul>
     *   <li>tutte le caselle della stessa riga e della stessa colonna
     *       (componente torre);</li>
     *   <li>tutte le caselle raggiungibili lungo le quattro diagonali
     *       (componente alfiere), fino al bordo della scacchiera.</li>
     * </ul>
     * Le destinazioni bloccate da pedine interposte vengono rimosse dalla
     * {@link Scacchiera} prima di presentare le mosse al giocatore.
     * </p>
     */
    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }

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
     * Restituisce una copia profonda e indipendente di questa regina.
     *
     * @return nuova istanza di {@code Regina} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Regina(this);
    }
}
