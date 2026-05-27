package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta una torre nel gioco degli scacchi.
 * <p>
 * La torre si sposta di un numero arbitrario di caselle lungo righe e colonne.
 * Finché non si è ancora mossa, partecipa all'arrocco insieme al {@link Re};
 * il diritto di arrocco viene perso al primo movimento. Il filtraggio delle
 * destinazioni bloccate da pedine interposte è delegato alla {@link Scacchiera}.
 * </p>
 *
 * @see Pedina
 * @see Re
 * @see Scacchiera
 */
public class Torre extends Pedina {

    /**
     * Indica se la torre ha ancora il diritto di partecipare all'arrocco.
     * Diventa {@code false} al primo movimento.
     */
    private boolean arrocco;

    /** Valore materiale della torre. */
    public static final int MATERIALE = 5;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce una torre con il colore e la posizione indicati.
     * Il diritto di arrocco è abilitato per impostazione predefinita; le mosse
     * valide vengono calcolate immediatamente.
     *
     * @param colore    colore della torre; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Torre(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        arrocco = true;
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea una nuova torre con lo stesso stato
     * dell'originale, incluso il flag {@code arrocco}.
     *
     * @param originale la torre da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Torre(Torre originale) {
        super(originale);
        arrocco = originale.arrocco;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Indica se la torre ha ancora il diritto di partecipare all'arrocco.
     *
     * @return {@code true} se la torre non si è ancora mossa, {@code false}
     *         dopo il primo movimento
     */
    public boolean getArrocco() {
        return arrocco;
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista delle mosse valide in base alla posizione
     * corrente della torre.
     * <p>
     * Vengono generate tutte le caselle della stessa riga e della stessa colonna,
     * escludendo la casella occupata dalla torre stessa. Le destinazioni bloccate
     * da pedine interposte vengono rimosse dalla {@link Scacchiera} prima di
     * presentare le mosse al giocatore.
     * </p>
     */
    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        for (int i = 0; i < DIMENSIONE_SCACCHIERA; i++) {
            if (i != posizione[0]) mosseValide.add(new int[] {i, posizione[1]});
            if (i != posizione[1]) mosseValide.add(new int[] {posizione[0], i});
        }
    }

    /**
     * Sposta la torre nella posizione indicata, revocando il diritto di arrocco.
     * <p>
     * Prima di delegare il movimento alla superclasse imposta {@code arrocco}
     * a {@code false}: la torre perde definitivamente la possibilità di
     * partecipare all'arrocco al suo primo spostamento.
     * </p>
     *
     * @param posizione posizione di destinazione {@code [riga, colonna]}
     * @throws IllegalArgumentException se {@code posizione} non è tra le mosse
     *                                  valide correnti, è {@code null} o fuori
     *                                  dai limiti della scacchiera
     */
    @Override
    public void muovi(int[] posizione) {
        arrocco = false;
        super.muovi(posizione);
    }

    /**
     * Restituisce una copia profonda e indipendente di questa torre.
     *
     * @return nuova istanza di {@code Torre} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Torre(this);
    }
}
