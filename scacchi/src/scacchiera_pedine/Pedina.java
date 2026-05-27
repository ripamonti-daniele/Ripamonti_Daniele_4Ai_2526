package scacchiera_pedine;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una generica pedina del gioco degli scacchi.
 * <p>
 * Ogni pedina ha un colore, una posizione sulla scacchiera, un valore materiale
 * e una lista di mosse valide calcolate dalla sottoclasse tramite
 * {@link #trovaMosseValide()}. Le sottoclassi concrete devono implementare
 * {@link #trovaMosseValide()} e {@link #copy()}.
 * </p>
 *
 * @see Scacchiera
 */
public abstract class Pedina {

    /** Colore della pedina ({@link Color#white} o {@link Color#black}). */
    private Color colore;

    /**
     * Posizione corrente della pedina sulla scacchiera come array
     * {@code [riga, colonna]}.
     */
    protected int[] posizione;

    /** Valore materiale della pedina, usato per valutare il vantaggio in partita. */
    private int materiale;

    /** Lista delle mosse valide calcolate per la posizione corrente. */
    protected List<int[]> mosseValide;

    /**
     * Dimensione del lato della scacchiera (numero di righe e di colonne).
     * Usato come riferimento condiviso da tutte le pedine e dalla
     * {@link Scacchiera}.
     */
    public static final int DIMENSIONE_SCACCHIERA = 8;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce una pedina con il colore e la posizione indicati.
     * Il valore materiale viene lasciato a {@code 0} fino a quando non viene
     * impostato dalla sottoclasse.
     *
     * @param colore    colore della pedina; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, oppure se la posizione
     *                                  è fuori dai limiti della scacchiera
     */
    public Pedina(Color colore, int[] posizione) {
        setColore(colore);
        setPosizione(posizione);
        mosseValide = new ArrayList<>();
    }

    /**
     * Costruisce una pedina con il colore, la posizione e il valore materiale
     * indicati.
     *
     * @param colore    colore della pedina; deve essere {@link Color#white} o
     *                  {@link Color#black}
     * @param posizione posizione iniziale {@code [riga, colonna]} sulla
     *                  scacchiera
     * @param materiale valore materiale della pedina; deve essere maggiore di
     *                  {@code 0}
     * @throws IllegalArgumentException se {@code colore} o {@code posizione}
     *                                  sono {@code null}, se il colore non è
     *                                  né bianco né nero, se la posizione è
     *                                  fuori dai limiti della scacchiera, oppure
     *                                  se {@code materiale} è minore o uguale a
     *                                  {@code 0}
     */
    public Pedina(Color colore, int[] posizione, int materiale) {
        this(colore, posizione);
        setMateriale(materiale);
    }

    /**
     * Costruttore di copia protetto: crea una nuova pedina con lo stesso stato
     * dell'originale.
     * <p>
     * La posizione e la lista delle mosse valide vengono copiate in modo
     * indipendente per evitare condivisione di riferimenti.
     * </p>
     *
     * @param originale la pedina da copiare
     * @throws IllegalArgumentException se {@code originale} è {@code null}
     */
    protected Pedina(Pedina originale) {
        if (originale == null) throw new IllegalArgumentException("La pedina originale non può essere null");
        this.colore = originale.colore;
        this.materiale = originale.materiale;
        this.posizione = originale.posizione.clone();
        this.mosseValide = originale.getMosseValide();
    }

    // -------------------------------------------------------------------------
    // Getters e setters
    // -------------------------------------------------------------------------

    /**
     * Restituisce il colore della pedina.
     *
     * @return {@link Color#white} o {@link Color#black}
     */
    public Color getColore() {
        return colore;
    }

    /**
     * Imposta il colore della pedina.
     *
     * @param colore il colore da assegnare; deve essere {@link Color#white} o
     *               {@link Color#black}
     * @throws IllegalArgumentException se {@code colore} è {@code null} oppure
     *                                  non è né bianco né nero
     */
    private void setColore(Color colore) {
        if (colore == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!colore.equals(Color.white) && !colore.equals(Color.black)) throw new IllegalArgumentException("Colore non valido");
        this.colore = colore;
    }

    /**
     * Restituisce un clone della posizione corrente della pedina.
     *
     * @return array {@code [riga, colonna]} clonato
     */
    public int[] getPosizione() {
        return posizione.clone();
    }

    /**
     * Imposta la posizione della pedina, verificando che rientri nei limiti
     * della scacchiera.
     *
     * @param posizione nuova posizione {@code [riga, colonna]}
     * @throws IllegalArgumentException se {@code posizione} è {@code null} oppure
     *                                  le coordinate sono fuori dall'intervallo
     *                                  {@code [0, DIMENSIONE_SCACCHIERA - 1]}
     */
    private void setPosizione(int[] posizione) {
        if (posizione == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (posizione[0] < 0 || posizione[0] > 7 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Non esiste questa posizione nella scacchiera");
        this.posizione = posizione.clone();
    }

    /**
     * Restituisce il valore materiale della pedina.
     *
     * @return valore materiale (maggiore di {@code 0})
     */
    public int getMateriale() {
        return materiale;
    }

    /**
     * Imposta il valore materiale della pedina.
     *
     * @param materiale valore materiale da assegnare; deve essere maggiore di
     *                  {@code 0}
     * @throws IllegalArgumentException se {@code materiale} è minore o uguale
     *                                  a {@code 0}
     */
    private void setMateriale(int materiale) {
        if (materiale <= 0) throw new IllegalArgumentException("Il materiale deve essere maggiore di 0");
        this.materiale = materiale;
    }

    // -------------------------------------------------------------------------
    // Logica di movimento
    // -------------------------------------------------------------------------

    /**
     * Sposta la pedina nella posizione indicata, aggiornandone la posizione
     * interna e ricalcolando le mosse valide.
     * <p>
     * La destinazione deve essere presente nella lista delle mosse valide
     * correnti; in caso contrario viene sollevata un'eccezione.
     * </p>
     *
     * @param posizione posizione di destinazione {@code [riga, colonna]}
     * @throws IllegalArgumentException se {@code posizione} non è tra le mosse
     *                                  valide correnti, è {@code null} o fuori
     *                                  dai limiti della scacchiera
     */
    public void muovi(int[] posizione) {
        boolean valido = false;

        for (int[] mossa : mosseValide) {
            if (mossa[0] == posizione[0] && mossa[1] == posizione[1]) {
                valido = true;
                break;
            }
        }
        if (!valido) throw new IllegalArgumentException("Questa mossa non è valida");

        setPosizione(posizione);
        trovaMosseValide();
    }

    /**
     * Restituisce una copia della lista delle mosse valide correnti.
     * Ogni elemento è clonato per evitare modifiche esterne alla lista interna.
     *
     * @return lista di array {@code [riga, colonna]} delle destinazioni
     *         raggiungibili dalla posizione corrente
     */
    public List<int[]> getMosseValide() {
        List<int[]> copia = new ArrayList<>(mosseValide.size());
        for (int[] arr : mosseValide) copia.add(arr.clone());
        return copia;
    }

    // -------------------------------------------------------------------------
    // Metodi astratti
    // -------------------------------------------------------------------------

    /**
     * Calcola e aggiorna la lista {@link #mosseValide} in base alla posizione
     * corrente della pedina e alle regole di movimento del pezzo specifico.
     * <p>
     * Viene invocato automaticamente al termine di {@link #muovi(int[])} e
     * deve essere implementato da ogni sottoclasse concreta.
     * </p>
     */
    protected abstract void trovaMosseValide();

    /**
     * Restituisce una copia profonda e indipendente di questa pedina,
     * preservandone il tipo concreto.
     *
     * @return nuova istanza dello stesso tipo con lo stesso stato
     */
    public abstract Pedina copy();
}