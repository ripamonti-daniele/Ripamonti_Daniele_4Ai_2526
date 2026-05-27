import scacchiera_pedine.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rappresenta un giocatore artificiale per il gioco degli scacchi.
 * <p>
 * Il Bot utilizza l'algoritmo <b>Minimax con potatura Alpha-Beta</b> per determinare
 * la mossa ottimale entro una profondità di ricerca configurabile. La valutazione
 * delle posizioni combina il valore materiale dei pezzi con tabelle di vantaggio
 * posizionale ({@link #vantaggioPosizione}) e penalità strutturali sui pedoni
 * (pedoni doppi e isolati).
 * </p>
 * <p>
 * Opzionalmente, tramite la modalità di ricerca avanzata, il Bot aumenta
 * automaticamente la profondità di ricerca nelle fasi finali della partita,
 * quando il numero di mosse disponibili si riduce.
 * </p>
 * <p>
 * Per evitare la ripetizione infinita di posizioni, il Bot tiene traccia
 * delle ultime quattro stringhe di stato della scacchiera e penalizza
 * le mosse che porterebbero a una triplice ripetizione.
 * </p>
 *
 * @see Scacchiera
 * @see StatoPartita
 */
public class Bot {

    /** La scacchiera su cui il Bot opera. */
    private final Scacchiera scacchiera;

    /** Il colore ({@link Color#white} o {@link Color#black}) con cui il Bot gioca. */
    private Color colore;

    /**
     * La profondità massima di ricerca dell'albero Minimax.
     * Deve essere compresa tra 1 e 7 (inclusi).
     */
    private final int PROFONDITA;

    /**
     * Storico delle ultime quattro stringhe di stato della scacchiera,
     * usato per rilevare e prevenire la triplice ripetizione di posizione.
     * L'indice 0 contiene la mossa più recente.
     */
    private final String[] mossePrecedenti;

    /**
     * Se {@code true}, il Bot aumenta dinamicamente la profondità di ricerca
     * nelle fasi finali della partita, quando le mosse disponibili sono ridotte.
     */
    private boolean ricercaAvanzata;

    /**
     * Tabelle di vantaggio posizionale per ogni tipo di pezzo.
     * <p>
     * La mappa associa una chiave stringa al tipo di pezzo con una matrice
     * 8×8 di interi che rappresenta il bonus (o malus) posizionale per ogni
     * casella della scacchiera. Le chiavi utilizzate sono:
     * <ul>
     *   <li>{@code "P"} — Pedone</li>
     *   <li>{@code "C"} — Cavallo</li>
     *   <li>{@code "A"} — Alfiere</li>
     *   <li>{@code "T"} — Torre</li>
     *   <li>{@code "Q"} — Regina</li>
     *   <li>{@code "R"} — Re (fase di apertura/mediogioco)</li>
     *   <li>{@code "Rend"} — Re (fase finale / endgame)</li>
     * </ul>
     * Le tabelle sono orientate dal punto di vista del Bianco (riga 0 = traversa 8).
     * Per il Nero i valori vengono letti specularmente ({@code [7 - i][j]}).
     * </p>
     */
    private static final Map<String, int[][]> vantaggioPosizione;

    static {
        vantaggioPosizione = new HashMap<>();

        vantaggioPosizione.put("P", new int[][] {
                {99, 99, 99, 99, 99, 99, 99, 99},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                { 5,  5, 10, 25, 25, 10,  5,  5},
                { 0,  0,  0, 20, 20,  0,  0,  0},
                { 5, -5,-10,  0,  0,-10, -5,  5},
                { 5, 10, 10,-20,-20, 10, 10,  5},
                { 0,  0,  0,  0,  0,  0,  0,  0}
        });

        vantaggioPosizione.put("C", new int[][] {
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-30,-30,-30,-30,-40,-50}
        });

        vantaggioPosizione.put("A", new int[][] {
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        });

        vantaggioPosizione.put("T", new int[][] {
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 5, 10, 10, 10, 10, 10, 10,  5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                { 0,  0,  0,  5,  5,  0,  0,  0}
        });

        vantaggioPosizione.put("Q", new int[][] {
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                { -5,  0,  5,  5,  5,  5,  0, -5},
                {  0,  0,  5,  5,  5,  5,  0, -5},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20}
        });

        vantaggioPosizione.put("R", new int[][] {
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                { 20, 20,  0,  0,  0,  0, 20, 20},
                { 20, 30, 10,  0,  0, 10, 30, 20}
        });

        vantaggioPosizione.put("Rend", new int[][] {
                {-50,-40,-30,-20,-20,-30,-40,-50},
                {-30,-20,-10,  0,  0,-10,-20,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-30,  0,  0,  0,  0,-30,-30},
                {-50,-30,-30,-30,-30,-30,-30,-50}
        });
    }

    /**
     * Costruisce un nuovo Bot con i parametri specificati.
     *
     * @param scacchiera      la scacchiera su cui il Bot giocherà; non può essere {@code null}
     * @param colore          il colore del Bot ({@link Color#white} o {@link Color#black}); non può essere {@code null}
     * @param profondita      la profondità di ricerca Minimax; deve essere compresa tra 1 e 7 (inclusi)
     * @param ricercaAvanzata se {@code true}, la profondità viene incrementata automaticamente
     *                        nelle fasi finali della partita
     * @throws IllegalArgumentException se {@code scacchiera} è {@code null},
     *                                  se {@code colore} è {@code null} o non è bianco/nero,
     *                                  oppure se {@code profondita} non è nel range [1, 7]
     */
    public Bot(Scacchiera scacchiera, Color colore, int profondita, boolean ricercaAvanzata) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        if (profondita < 1 || profondita > 7) throw new IllegalArgumentException("Profondità non valida: max 7 min 1");
        PROFONDITA = profondita;
        setColore(colore);
        setRicercaAvanzata(ricercaAvanzata);
        mossePrecedenti = new String[4];
    }

    /**
     * Restituisce la profondità di ricerca Minimax configurata per questo Bot.
     *
     * @return la profondità di ricerca, compresa tra 1 e 7
     */
    public int getProfondita() {
        return PROFONDITA;
    }

    /**
     * Restituisce il colore con cui il Bot gioca.
     *
     * @return {@link Color#white} se il Bot gioca con i pezzi bianchi,
     *         {@link Color#black} se gioca con i pezzi neri
     */
    public Color getColore() {
        return colore;
    }

    /**
     * Imposta il colore del Bot.
     *
     * @param c il colore da assegnare; deve essere {@link Color#white} o {@link Color#black}
     * @throws IllegalArgumentException se {@code c} è {@code null} o non è bianco/nero
     */
    private void setColore(Color c) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere null");
        if (!(c.equals(Color.white) || c.equals(Color.black))) throw new IllegalArgumentException("Il colore può essere solo bianco o nero");
        colore = c;
    }

    /**
     * Indica se la modalità di ricerca avanzata è attiva.
     * <p>
     * Quando attiva, il Bot incrementa la profondità di ricerca nelle fasi finali
     * della partita per migliorare la qualità delle mosse in endgame.
     * </p>
     *
     * @return {@code true} se la ricerca avanzata è abilitata, {@code false} altrimenti
     */
    public boolean isRicercaAvanzata() {
        return ricercaAvanzata;
    }

    /**
     * Abilita o disabilita la modalità di ricerca avanzata.
     *
     * @param ricercaAvanzata {@code true} per abilitare la ricerca avanzata,
     *                        {@code false} per disabilitarla
     */
    public void setRicercaAvanzata(boolean ricercaAvanzata) {
        this.ricercaAvanzata = ricercaAvanzata;
    }

    /**
     * Calcola il numero totale di mosse legali disponibili per i pezzi del Bot
     * nella configurazione di scacchiera fornita.
     *
     * @param caselle la matrice 8×8 rappresentante lo stato corrente della scacchiera
     * @return il numero totale di mosse legali disponibili per il Bot
     */
    private int mosseTotali(Pedina[][] caselle) {
        int tot = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p != null && p.getColore().equals(colore)) {
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, p.getPosizione(), colore);
                    if (mosse == null) continue;
                    tot += mosse.size();
                }
            }
        }
        return tot;
    }

    /**
     * Aggiunge una nuova stringa di stato della scacchiera allo storico delle
     * mosse precedenti, scorrendo le posizioni esistenti di un posto verso destra
     * (LIFO). L'elemento più vecchio (indice 3) viene eliminato.
     *
     * @param mossa la stringa rappresentante lo stato della scacchiera dopo la mossa
     */
    private void aggiungiMossa(String mossa) {
        for (int i = mossePrecedenti.length - 1; i > 0; i--) mossePrecedenti[i] = mossePrecedenti[i - 1];
        mossePrecedenti[0] = mossa;
    }

    /**
     * Calcola e restituisce la mossa migliore per il Bot nella posizione corrente.
     * <p>
     * Il metodo esplora tutte le mosse legali dei pezzi del Bot applicando l'algoritmo
     * Minimax con potatura Alpha-Beta. Prima di esplorare ogni mossa, esegue la mossa
     * sulla matrice interna, valuta ricorsivamente la posizione risultante, e poi
     * ripristina lo stato originale (make-unmake).
     * </p>
     * <p>
     * Se la modalità di ricerca avanzata è attiva, la profondità può essere incrementata
     * di 1 o 2 livelli in base al numero di mosse disponibili e alla presenza di scacco.
     * Le mosse che porterebbero a una triplice ripetizione di posizione vengono ignorate.
     * La ricerca si interrompe anticipatamente se viene individuato un matto in una mossa.
     * </p>
     *
     * @return una matrice {@code int[2][2]} in cui {@code [0]} contiene le coordinate
     *         di partenza e {@code [1]} le coordinate di destinazione della mossa scelta;
     *         {@code null} se non è disponibile alcuna mossa legale
     */
    public int[][] getMossa() {
        int[][] mossa = null;
        int valoreMigliore = Integer.MIN_VALUE;
        boolean mattoInUno = false;
        Pedina[][] caselle = scacchiera.getCaselle();

        int profondita = PROFONDITA - 1;
        if (ricercaAvanzata) {
            int tot = mosseTotali(caselle);
            if (tot <= 20 && !Scacchiera.isScaccoReCaselle(caselle, colore)) profondita++;
            if (tot <= 5) profondita++;
        }

        String stringaMossaMigliore = "";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null || !caselle[i][j].getColore().equals(colore)) continue;

                int[] pos = new int[]{i, j};

                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, colore);
                if (mosse == null) continue;

                for (int[] m : mosse) {
                    Pedina pezzoMosso = caselle[pos[0]][pos[1]].copy();
                    Pedina pezzoMangiato = null;
                    if (caselle[m[0]][m[1]] != null) pezzoMangiato = caselle[m[0]][m[1]].copy();
                    Pedina torreSalvata = null;
                    if (pezzoMosso instanceof Re && Math.abs(pos[1] - m[1]) == 2) {
                        if (pos[1] - m[1] == 2) torreSalvata = caselle[pos[0]][0].copy();
                        else torreSalvata = caselle[pos[0]][7].copy();
                    }
                    int enPassant = 0;
                    if (pezzoMosso instanceof Pedone && pezzoMangiato == null && m[1] != pos[1]) {
                        if (m[1] == pos[1] - 1) enPassant = -1;
                        else enPassant = 1;
                    }

                    if (!Scacchiera.muoviPedinaCaselle(caselle, mosse, pos, m)) continue;

                    if (Scacchiera.promozioneInSospesoCaselle(caselle) != null) Scacchiera.promozionePedoneCaselle(caselle, m, 1);

                    String stringaScacchiera = Scacchiera.getStringaScacchieraCaselle(caselle, true);
                    int val = 0;
                    if (!(stringaScacchiera.equals(mossePrecedenti[1]) && stringaScacchiera.equals(mossePrecedenti[3]) && mossePrecedenti[0].equals(mossePrecedenti[2]))) {
                        val = miniMax(caselle, profondita, false, Integer.MIN_VALUE, Integer.MAX_VALUE, getColoreAvversario());
                        mattoInUno = (val == Integer.MAX_VALUE - 10000 + 10 * (PROFONDITA - 1));
                    }

                    caselle[pos[0]][pos[1]] = pezzoMosso;
                    caselle[m[0]][m[1]] = pezzoMangiato;
                    if (torreSalvata != null) {
                        if (pos[1] - m[1] > 0) caselle[pos[0]][pos[1] - 1] = null;
                        else caselle[pos[0]][pos[1] + 1] = null;
                        caselle[torreSalvata.getPosizione()[0]][torreSalvata.getPosizione()[1]] = torreSalvata;
                    }
                    if (enPassant != 0) {
                        Color c = Color.white;
                        if (pezzoMosso.getColore().equals(c)) c = Color.black;
                        caselle[pos[0]][pos[1] + enPassant] = new Pedone(c, new int[]{pos[0], pos[1] + enPassant});
                    }

                    if (val > valoreMigliore) {
                        valoreMigliore = val;
                        mossa = new int[][]{{pos[0], pos[1]}, {m[0], m[1]}};
                        stringaMossaMigliore = stringaScacchiera;
                    }
                    if (mattoInUno) break;
                }
                if (mattoInUno) break;
            }
            if (mattoInUno) break;
        }
        aggiungiMossa(stringaMossaMigliore);
        return mossa;
    }

    /**
     * Implementa l'algoritmo Minimax con potatura Alpha-Beta.
     * <p>
     * Se la profondità raggiunge zero o la partita è terminata, delega la
     * valutazione al metodo {@link #evaluation(Pedina[][], StatoPartita, int)}.
     * Altrimenti, a seconda del flag {@code massimizza}, invoca
     * {@link #simulaMosse} per esplorare le mosse del giocatore corrente.
     * </p>
     *
     * @param caselle    la matrice 8×8 rappresentante lo stato corrente della scacchiera
     * @param profondita i livelli di ricerca rimanenti
     * @param massimizza {@code true} se il nodo corrente è di massimizzazione (turno del Bot),
     *                   {@code false} se è di minimizzazione (turno dell'avversario)
     * @param alpha      il valore alpha corrente per la potatura (miglior punteggio per il massimizzatore)
     * @param beta       il valore beta corrente per la potatura (miglior punteggio per il minimizzatore)
     * @param turno      il colore del giocatore di cui si stanno esplorando le mosse
     * @return il valore euristico della posizione dal punto di vista del Bot
     */
    private int miniMax(Pedina[][] caselle, int profondita, boolean massimizza, int alpha, int beta, Color turno) {
        Color prossimoTurno = Color.white;
        if (turno.equals(Color.white)) prossimoTurno = Color.black;

        StatoPartita sp = Scacchiera.statoPartitaCaselle(caselle, turno);
        if (profondita == 0 || sp != StatoPartita.IN_CORSO) return evaluation(caselle, sp, profondita);

        if (massimizza) return simulaMosse(caselle, profondita, true, alpha, beta, turno, prossimoTurno, Integer.MIN_VALUE);
        else return simulaMosse(caselle, profondita, false, alpha, beta, turno, prossimoTurno, Integer.MAX_VALUE);
    }

    /**
     * Esplora tutte le mosse legali del giocatore di turno e ne valuta ricorsivamente
     * le posizioni risultanti tramite {@link #miniMax}.
     * <p>
     * Per ogni mossa, lo stato della scacchiera viene modificato (make), valutato
     * ricorsivamente e poi ripristinato (unmake). La potatura Alpha-Beta interrompe
     * la ricerca non appena {@code beta <= alpha}.
     * La promozione del pedone, se presente, viene risolta automaticamente
     * scegliendo il pezzo con indice 1 (tipicamente la Regina).
     * </p>
     *
     * @param caselle       la matrice 8×8 rappresentante lo stato corrente della scacchiera
     * @param profondita    i livelli di ricerca rimanenti
     * @param massimizza    {@code true} per il nodo massimizzante, {@code false} per il minimizzante
     * @param alpha         il valore alpha corrente per la potatura
     * @param beta          il valore beta corrente per la potatura
     * @param turno         il colore del giocatore che deve muovere
     * @param prossimoTurno il colore del giocatore che muoverà al livello successivo
     * @param val           il valore iniziale ({@link Integer#MIN_VALUE} per il massimizzatore,
     *                      {@link Integer#MAX_VALUE} per il minimizzatore)
     * @return il valore euristico migliore trovato tra tutte le mosse esplorate
     */
    private int simulaMosse(Pedina[][] caselle, int profondita, boolean massimizza, int alpha, int beta, Color turno, Color prossimoTurno, int val) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null || !caselle[i][j].getColore().equals(turno)) continue;

                int[] pos = new int[]{i, j};
                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, turno);
                if (mosse == null) continue;

                for (int[] m : mosse) {
                    Pedina pezzoMosso = caselle[pos[0]][pos[1]].copy();
                    Pedina pezzoMangiato = null;
                    if (caselle[m[0]][m[1]] != null) pezzoMangiato = caselle[m[0]][m[1]].copy();
                    Pedina torreSalvata = null;
                    if (pezzoMosso instanceof Re && Math.abs(pos[1] - m[1]) == 2) {
                        if (pos[1] - m[1] == 2) torreSalvata = caselle[pos[0]][0].copy();
                        else torreSalvata = caselle[pos[0]][7].copy();
                    }
                    int enPassant = 0;
                    if (pezzoMosso instanceof Pedone && pezzoMangiato == null && m[1] != pos[1]) {
                        if (m[1] == pos[1] - 1) enPassant = -1;
                        else enPassant = 1;
                    }

                    if (!Scacchiera.muoviPedinaCaselle(caselle, mosse, pos, m)) continue;
                    if (Scacchiera.promozioneInSospesoCaselle(caselle) != null) Scacchiera.promozionePedoneCaselle(caselle, m, 1);

                    int temp = miniMax(caselle, profondita - 1, !massimizza, alpha, beta, prossimoTurno);
                    if (massimizza) {
                        if (temp > val) val = temp;
                        if (val > alpha) alpha = val;
                    }
                    else {
                        if (temp < val) val = temp;
                        if (val < beta) beta = val;
                    }

                    caselle[pos[0]][pos[1]] = pezzoMosso;
                    caselle[m[0]][m[1]] = pezzoMangiato;
                    if (torreSalvata != null) {
                        if (pos[1] - m[1] > 0) caselle[pos[0]][pos[1] - 1] = null;
                        else caselle[pos[0]][pos[1] + 1] = null;
                        caselle[torreSalvata.getPosizione()[0]][torreSalvata.getPosizione()[1]] = torreSalvata;
                    }
                    if (enPassant != 0) {
                        Color c = Color.white;
                        if (pezzoMosso.getColore().equals(c)) c = Color.black;
                        caselle[pos[0]][pos[1] + enPassant] = new Pedone(c, new int[]{pos[0], pos[1] + enPassant});
                    }

                    if (beta <= alpha) return val;
                }
            }
        }
        return val;
    }

    /**
     * Restituisce il colore avversario rispetto al colore del Bot.
     *
     * @return {@link Color#black} se il Bot gioca con il bianco,
     *         {@link Color#white} se gioca con il nero
     */
    public Color getColoreAvversario() {
        if (colore.equals(Color.white)) return Color.black;
        else return Color.white;
    }

    /**
     * Calcola il valore euristico complessivo della posizione dal punto di vista del Bot.
     * <p>
     * Nei casi di vittoria o sconfitta, restituisce valori prossimi a
     * {@link Integer#MAX_VALUE} o {@link Integer#MIN_VALUE}, corretti dalla
     * profondità residua per preferire vittorie più rapide e sconfitte più lente.
     * In caso di pareggio restituisce {@code 0}.
     * Se la partita è ancora in corso, somma le valutazioni di ogni singola pedina
     * tramite {@link #evaluationCasella}, aggiungendo bonus/malus per lo scacco
     * nelle situazioni di endgame.
     * </p>
     *
     * @param caselle      la matrice 8×8 rappresentante lo stato corrente della scacchiera
     * @param statoPartita lo stato corrente della partita
     * @param profondita   la profondità residua nella ricerca, usata per calibrare
     *                     i valori di vittoria e sconfitta
     * @return il valore euristico della posizione; valori positivi indicano
     *         vantaggio per il Bot, valori negativi vantaggio per l'avversario
     */
    private int evaluation(Pedina[][] caselle, StatoPartita statoPartita, int profondita) {
        if (statoPartita == StatoPartita.VITTORIA_BIANCO) {
            if (colore.equals(Color.white)) return Integer.MAX_VALUE - 10000 + 10 * profondita;
            else return Integer.MIN_VALUE + 10000 - 10 * profondita;
        }
        if (statoPartita == StatoPartita.VITTORIA_NERO) {
            if (colore.equals(Color.black)) return Integer.MAX_VALUE - 10000 + 10 * profondita;
            else return Integer.MIN_VALUE + 10000 - 10 * profondita;
        }
        if (statoPartita != StatoPartita.IN_CORSO) return 0;

        int evalTotale = 0;
        boolean endgame = Scacchiera.getMaterialeCaselle(caselle, colore) <= 15 && Scacchiera.getMaterialeCaselle(caselle, getColoreAvversario()) <= 15;
        if (endgame && Scacchiera.isScaccoReCaselle(caselle, getColoreAvversario())) evalTotale += 30;
        if (endgame && Scacchiera.isScaccoReCaselle(caselle, colore)) evalTotale -= 30;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;
                int evalPedina = evaluationCasella(caselle, i, j, endgame);
                if (caselle[i][j].getColore().equals(colore)) evalTotale += evalPedina;
                else evalTotale -= evalPedina;
            }
        }

        return evalTotale;
    }

    /**
     * Calcola il valore euristico del pezzo presente nella casella {@code [i][j]}.
     * <p>
     * Il valore è composto da tre contributi:
     * <ol>
     *   <li><b>Materiale</b>: {@code materiale * 150}</li>
     *   <li><b>Vantaggio posizionale</b>: valore letto dalla tabella {@link #vantaggioPosizione}
     *       corrispondente al tipo di pezzo e alla casella occupata, moltiplicato per
     *       il valore materiale (raddoppiato per i pedoni). Per il Nero la tabella
     *       viene letta specularmente ({@code [7 - i][j]}).</li>
     *   <li><b>Penalità strutturali</b> (solo per i pedoni):
     *       <ul>
     *         <li>Pedone doppio: penalità per la presenza di un altro pedone dello stesso
     *             colore nella stessa colonna.</li>
     *         <li>Pedone isolato: penalità se non esistono pedoni alleati nelle colonne adiacenti.</li>
     *       </ul>
     *   </li>
     * </ol>
     * </p>
     *
     * @param caselle la matrice 8×8 rappresentante lo stato corrente della scacchiera
     * @param i       l'indice di riga (0–7) della casella da valutare
     * @param j       l'indice di colonna (0–7) della casella da valutare
     * @param endgame {@code true} se la partita è in fase finale, nel qual caso
     *                viene usata la tabella {@code "Rend"} per il Re anziché {@code "R"}
     * @return il valore euristico del pezzo in {@code caselle[i][j]};
     *         {@code 0} se la casella è vuota
     */
    private int evaluationCasella(Pedina[][] caselle, int i, int j, boolean endgame) {
        if (caselle[i][j] == null) return 0;
        Pedina p = caselle[i][j];
        int materiale = p.getMateriale();
        int vantaggioPosizione;
        String nomePedina = p.getClass().getSimpleName().substring(0, 1);
        if (p instanceof Regina) nomePedina = "Q";
        else if (p instanceof Re && endgame) nomePedina += "end";
        if (p.getColore().equals(Color.white)) vantaggioPosizione = Bot.vantaggioPosizione.get(nomePedina)[i][j];
        else vantaggioPosizione = Bot.vantaggioPosizione.get(nomePedina)[7 - i][j];
        vantaggioPosizione *= materiale;

        int pedoneDoppio = 0;
        int pedoneIsolato = 0;

        if (p instanceof Pedone) {
            vantaggioPosizione *= 2;
            boolean pedoneSx = false;
            boolean pedoneDx = false;
            for (int vert = 0; vert < 8; vert++) {
                if (!pedoneSx && j > 0 && caselle[vert][j - 1] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneSx = true;
                if (!pedoneDx && j < 7 && caselle[vert][j + 1] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneDx = true;
                if (vert != i && caselle[vert][j] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneIsolato += 20 * materiale;
            }
            if (!pedoneSx && !pedoneDx) pedoneIsolato = 25 * materiale;
        }

        return materiale * 150 + vantaggioPosizione - pedoneDoppio - pedoneIsolato;
    }
}