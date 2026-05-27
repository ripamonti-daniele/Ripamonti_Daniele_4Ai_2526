package scacchiera_pedine;
import java.awt.Color;

/**
 * Rappresenta un pedone nel gioco degli scacchi.
 * <p>
 * Il pedone avanza di una casella in avanti (o di due dalla posizione iniziale),
 * cattura in diagonale e supporta la cattura en passant. Alla prima mossa può
 * avanzare di due caselle; questa possibilità viene rimossa dopo il primo
 * movimento. Il flag en passant viene attivato se il pedone ha appena avanzato
 * di due caselle, rendendolo vulnerabile alla cattura en passant da parte
 * dell'avversario nel turno immediatamente successivo.
 * </p>
 *
 * @see Pedina
 * @see Scacchiera
 */
public class Pedone extends Pedina {

    /**
     * Indica se il pedone può ancora avanzare di due caselle.
     * Diventa {@code false} dopo il primo movimento.
     */
    private boolean muoviDiDueCaselle;

    /**
     * Indica se il pedone è vulnerabile alla cattura en passant.
     * Viene impostato a {@code true} quando il pedone avanza di due caselle
     * e a {@code false} al turno successivo.
     */
    private boolean enPassant;

    /** Valore materiale del pedone. */
    public static final int MATERIALE = 1;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce un pedone con il colore e la posizione indicati.
     * Il pedone viene inizializzato con la possibilità di avanzare di due
     * caselle e senza il flag en passant; le mosse valide vengono calcolate
     * immediatamente.
     *
     * @param colore    colore del pedone; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Pedone(Color colore, int[] posizione) {
        super(colore, posizione, MATERIALE);
        muoviDiDueCaselle = true;
        enPassant = false;
        trovaMosseValide();
    }

    /**
     * Costruttore di copia protetto: crea un nuovo pedone con lo stesso stato
     * dell'originale, inclusi i flag {@code muoviDiDueCaselle} ed
     * {@code enPassant}.
     *
     * @param originale il pedone da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Pedone(Pedone originale) {
        super(originale);
        this.muoviDiDueCaselle = originale.muoviDiDueCaselle;
        this.enPassant = originale.enPassant;
    }

    // -------------------------------------------------------------------------
    // Getters e setters
    // -------------------------------------------------------------------------

    /**
     * Indica se il pedone può ancora avanzare di due caselle.
     *
     * @return {@code true} se il pedone non si è ancora mosso, {@code false}
     *         dopo il primo movimento
     */
    public boolean getMuoviDiDueCaselle() {
        return muoviDiDueCaselle;
    }

    /**
     * Indica se il pedone è attualmente vulnerabile alla cattura en passant.
     *
     * @return {@code true} se il pedone ha appena avanzato di due caselle nel
     *         turno corrente
     */
    public boolean getEnPassant() {
        return enPassant;
    }

    /**
     * Rimuove la vulnerabilità en passant del pedone.
     * Viene chiamato dalla {@link Scacchiera} al turno successivo a quello in
     * cui il pedone ha avanzato di due caselle.
     */
    public void rimuoviEnPassant() {
        enPassant = false;
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista delle mosse valide in base alla posizione
     * corrente e al colore del pedone.
     * <p>
     * Le mosse generate comprendono:
     * <ul>
     *   <li>avanzamento di una casella in avanti;</li>
     *   <li>avanzamento di due caselle se {@code muoviDiDueCaselle} è
     *       {@code true} e la casella di destinazione è dentro la scacchiera;</li>
     *   <li>cattura diagonale sinistra e destra (usate anche per en passant,
     *       il cui filtraggio è delegato a {@link Scacchiera}).</li>
     * </ul>
     * La direzione di avanzamento è verso le righe decrescenti per il bianco e
     * verso le righe crescenti per il nero.
     * </p>
     */
    @Override
    public void trovaMosseValide() {
        mosseValide.clear();

        if (getColore() == Color.white) {
            if (posizione[0] > 0) {
                mosseValide.add(new int[]{posizione[0] - 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] - 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle && posizione[0] - 2 >= 0) mosseValide.add(new int[]{posizione[0] - 2, posizione[1]});
        }

        else {
            if (posizione[0] < DIMENSIONE_SCACCHIERA - 1) {
                mosseValide.add(new int[]{posizione[0] + 1, posizione[1]});
                if (posizione[1] > 0) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] - 1});
                if (posizione[1] < DIMENSIONE_SCACCHIERA - 1) mosseValide.add(new int[]{posizione[0] + 1, posizione[1] + 1});
            }
            if (muoviDiDueCaselle && posizione[0] + 2 < DIMENSIONE_SCACCHIERA) mosseValide.add(new int[]{posizione[0] + 2, posizione[1]});
        }
    }

    /**
     * Sposta il pedone nella posizione indicata, aggiornando i flag interni.
     * <p>
     * Prima di delegare il movimento alla superclasse, disabilita la possibilità
     * di avanzare di due caselle ({@code muoviDiDueCaselle = false}). Al termine
     * del movimento, imposta {@code enPassant} a {@code true} se il pedone ha
     * appena eseguito una mossa doppia, rendendolo vulnerabile alla cattura en
     * passant nel turno immediatamente successivo.
     * </p>
     *
     * @param posizione posizione di destinazione {@code [riga, colonna]}
     * @throws IllegalArgumentException se {@code posizione} non è tra le mosse
     *                                  valide correnti, è {@code null} o fuori
     *                                  dai limiti della scacchiera
     */
    @Override
    public void muovi(int[] posizione) {
        boolean mossaDoppia = Math.abs(posizione[0] - this.posizione[0]) == 2;
        muoviDiDueCaselle = false;
        super.muovi(posizione);
        enPassant = mossaDoppia;
    }

    /**
     * Restituisce una copia profonda e indipendente di questo pedone.
     *
     * @return nuova istanza di {@code Pedone} con lo stesso stato
     */
    @Override
    public Pedina copy() {
        return new Pedone(this);
    }
}
