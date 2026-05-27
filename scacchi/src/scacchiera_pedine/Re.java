package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta il re nel gioco degli scacchi.
 * <p>
 * Il re si sposta di una casella in qualsiasi direzione. Finché non si è ancora
 * mosso, può eseguire l'arrocco corto (verso destra) o lungo (verso sinistra),
 * spostandosi di due caselle; la legalità completa dell'arrocco (case libere,
 * assenza di scacco) è verificata dalla {@link Scacchiera}. Il diritto di
 * arrocco viene perso al primo movimento.
 * </p>
 *
 * @see Pedina
 * @see Scacchiera
 */
public class Re extends Pedina {

    /**
     * Indica se il re ha ancora il diritto di effettuare l'arrocco.
     * Diventa {@code false} al primo movimento.
     */
    private boolean arrocco;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce un re con il colore e la posizione indicati.
     * Il diritto di arrocco è abilitato per impostazione predefinita; le mosse
     * valide vengono calcolate immediatamente.
     *
     * @param colore    colore del re; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Re(Color colore, int[] posizione) {
        super(colore, posizione);
        arrocco = true;
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea un nuovo re con lo stesso stato
     * dell'originale, incluso il flag {@code arrocco}.
     *
     * @param originale il re da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Re(Re originale) {
        super(originale);
        this.arrocco = originale.arrocco;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Indica se il re ha ancora il diritto di effettuare l'arrocco.
     *
     * @return {@code true} se il re non si è ancora mosso, {@code false}
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
     * corrente del re.
     * <p>
     * Le mosse generate comprendono:
     * <ul>
     *   <li>tutte le caselle adiacenti (orizzontali, verticali e diagonali)
     *       raggiungibili senza uscire dalla scacchiera;</li>
     *   <li>le due destinazioni dell'arrocco corto ({@code colonna + 2}) e
     *       lungo ({@code colonna - 2}), se {@code arrocco} è {@code true} e
     *       le colonne di destinazione rientrano nei limiti della scacchiera;
     *       la legalità effettiva dell'arrocco è verificata dalla
     *       {@link Scacchiera}.</li>
     * </ul>
     * </p>
     */
    @Override
    protected void trovaMosseValide() {
        mosseValide.clear();

        if (arrocco) {
            if (posizione[1] + 2 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[] {posizione[0], posizione[1] + 2});
            if (posizione[1] - 2 >= 0) mosseValide.add(new int[] {posizione[0], posizione[1] - 2});
        }

        for (int i = -1; i <= 1; i++) {
            if (posizione[0] + i < 0 || posizione[0] + i >= DIMENSIONE_SCACCHIERA) continue;
            for (int j = -1; j <= 1; j++) {
                if (posizione[1] + j < 0 || posizione[1] + j >= DIMENSIONE_SCACCHIERA || i == 0 && j == 0) continue;
                mosseValide.add(new int[] {posizione[0] + i, posizione[1] + j});
            }
        }
    }

    /**
     * Sposta il re nella posizione indicata, revocando il diritto di arrocco.
     * <p>
     * Prima di delegare il movimento alla superclasse imposta {@code arrocco}
     * a {@code false}: il re perde definitivamente la possibilità di arroccare
     * al suo primo spostamento.
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
     * Restituisce una copia profonda e indipendente di questo re.
     *
     * @return nuova istanza di {@code Re} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Re(this);
    }
}
