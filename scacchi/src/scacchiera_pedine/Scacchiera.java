package scacchiera_pedine;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una scacchiera per il gioco degli scacchi.
 * <p>
 * Gestisce lo stato completo della partita, inclusi il posizionamento delle pedine,
 * il turno corrente, la selezione e il movimento delle pedine, le condizioni di
 * vittoria e pareggio, la promozione dei pedoni, l'arrocco e la cattura en passant.
 * </p>
 * <p>
 * Fornisce sia metodi di istanza (per il gioco interattivo) sia metodi statici
 * con la firma {@code *Caselle(...)} pensati per essere utilizzati da un bot o
 * da componenti esterni che lavorano direttamente sull'array di pedine.
 * </p>
 *
 * @see Pedina
 * @see StatoPartita
 * @see PartitaFileManager
 */
public class Scacchiera {

    /**
     * Dimensione del lato della scacchiera (numero di righe e di colonne).
     * Il valore è mutuato da {@link Pedina#DIMENSIONE_SCACCHIERA}.
     */
    public static final int DIMENSIONE = Pedina.DIMENSIONE_SCACCHIERA;

    /** Matrice bidimensionale che contiene le pedine presenti sulla scacchiera. */
    private final Pedina[][] caselle;

    /** Colore del giocatore di cui è il turno ({@link Color#white} o {@link Color#black}). */
    private Color turno;

    /** Numero totale di mezze-mosse (half-moves) effettuate dall'inizio della partita. */
    private int mosse;

    /**
     * Contatore delle mosse neutre, ossia mosse effettuate senza catture né movimenti
     * di pedoni. Usato per la regola delle 75 mosse (150 mezze-mosse).
     */
    private int mosseNeutre;

    /** Lista delle mosse valide calcolate per la pedina attualmente selezionata. */
    private List<int[]> mosseValide;

    /**
     * Coordinata {@code [riga, colonna]} della casella attualmente selezionata,
     * oppure {@code null} se nessuna pedina è selezionata.
     */
    private int[] casellaSelezionata;

    /** Separatore usato nella rappresentazione testuale della scacchiera. */
    private static final String SEP = ";";

    /**
     * Se {@code true}, il turno viene cambiato automaticamente dopo ogni mossa;
     * se {@code false}, il cambio di turno deve essere gestito manualmente.
     */
    private boolean autoCambioTurno;

    // -------------------------------------------------------------------------
    // Costruttori
    // -------------------------------------------------------------------------

    /**
     * Costruisce una nuova scacchiera configurando il comportamento del cambio turno.
     *
     * @param autoCambioTurno {@code true} per abilitare il cambio automatico del turno
     *                        dopo ogni mossa, {@code false} per gestirlo manualmente
     */
    public Scacchiera(boolean autoCambioTurno) {
        caselle = new Pedina[DIMENSIONE][DIMENSIONE];
        mosseValide = new ArrayList<>();
        setAutoCambioTurno(autoCambioTurno);
        reset();
    }

    /**
     * Costruttore di copia: crea una nuova {@code Scacchiera} con lo stesso stato
     * della scacchiera fornita.
     * <p>
     * La matrice delle caselle viene copiata in profondità tramite
     * {@link #getCaselle()}, garantendo che le modifiche alla nuova istanza non
     * influenzino l'originale. La lista delle mosse valide e la casella selezionata
     * vengono anch'esse clonate elemento per elemento.
     * </p>
     *
     * @param s la scacchiera da copiare
     * @throws IllegalArgumentException se {@code s} è {@code null}
     */
    public Scacchiera(Scacchiera s) {
        if (s == null) throw new IllegalArgumentException("Il parametro di copia non può essere null");
        this.caselle = s.getCaselle();
        this.turno = s.turno;
        this.mosse = s.mosse;
        this.mosseNeutre = s.mosseNeutre;
        this.mosseValide = new ArrayList<>();
        for (int[] pos : s.mosseValide) this.mosseValide.add(pos.clone());
        this.casellaSelezionata = s.casellaSelezionata.clone();
        this.autoCambioTurno = s.autoCambioTurno;
    }

    /**
     * Costruisce una nuova scacchiera con il cambio automatico del turno abilitato.
     * Equivale a {@code new Scacchiera(true)}.
     */
    public Scacchiera() {
        this(true);
    }

    // -------------------------------------------------------------------------
    // Inizializzazione e reset
    // -------------------------------------------------------------------------

    /**
     * Posiziona tutte le pedine nella configurazione iniziale degli scacchi.
     * <p>
     * Riga 0 e riga {@code DIMENSIONE-1}: pezzi maggiori (Torre, Cavallo, Alfiere,
     * Regina, Re) rispettivamente neri e bianchi. Riga 1 e riga {@code DIMENSIONE-2}:
     * pedoni neri e bianchi. Tutte le altre caselle sono impostate a {@code null}.
     * </p>
     */
    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (i == 1) caselle[i][j] = new Pedone(Color.black, new int[]{i, j});
                else if (i == DIMENSIONE - 2) caselle[i][j] = new Pedone(Color.white, new int[]{i, j});
                else if (i == 0) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.black, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.black, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.black, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.black, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.black, new int[]{i, j});
                }
                else if (i == DIMENSIONE - 1) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.white, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.white, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.white, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.white, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.white, new int[]{i, j});
                }
                else caselle[i][j] = null;
            }
        }
    }

    /**
     * Ripristina la scacchiera allo stato iniziale.
     * <p>
     * Reimposta il turno al bianco, azzera i contatori delle mosse, svuota
     * la lista delle mosse valide, deseleziona la casella corrente, reinizializza
     * la disposizione delle pedine e salva lo stato su file tramite
     * {@link PartitaFileManager}.
     * </p>
     */
    public void reset() {
        turno = Color.white;
        mosse = 0;
        mosseNeutre = 0;
        mosseValide.clear();
        casellaSelezionata = null;
        inizializza();
        PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
    }

    // -------------------------------------------------------------------------
    // Getters e setters
    // -------------------------------------------------------------------------

    /**
     * Restituisce il numero totale di mezze-mosse effettuate dall'inizio della partita.
     *
     * @return numero di mosse effettuate
     */
    public int getMosse() {
        return mosse;
    }

    /**
     * Restituisce il contatore delle mosse neutre (senza catture né movimenti di pedoni).
     * Quando raggiunge o supera 150 la partita si conclude in pareggio per la regola
     * delle 75 mosse.
     *
     * @return numero di mosse neutre consecutive
     */
    public int getMosseNeutre() {
        return mosseNeutre;
    }

    /**
     * Restituisce una copia profonda della matrice di pedine fornita come parametro.
     * <p>
     * Ogni elemento non {@code null} viene duplicato tramite {@link Pedina#copy()},
     * garantendo che le modifiche alla copia non influenzino l'originale.
     * </p>
     *
     * @param caselle la matrice di pedine da copiare
     * @return una nuova matrice {@code DIMENSIONE × DIMENSIONE} con copie indipendenti
     *         delle pedine
     */
    public static Pedina[][] getCopiaCaselle(Pedina[][] caselle) {
        Pedina[][] copia = new Pedina[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < caselle.length; i++) {
            for (int j = 0; j < caselle[i].length; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = caselle[i][j].copy();
            }
        }
        return copia;
    }

    /**
     * Restituisce una copia profonda della matrice di caselle interna alla scacchiera.
     *
     * @return copia indipendente della matrice delle pedine
     * @see #getCopiaCaselle(Pedina[][])
     */
    public Pedina[][] getCaselle() {
        return getCopiaCaselle(caselle);
    }

    /**
     * Restituisce una copia della pedina nella posizione indicata.
     *
     * @param pos array {@code [riga, colonna]} della casella da leggere;
     *            può essere {@code null}
     * @return copia della pedina presente in quella posizione, oppure {@code null}
     *         se {@code pos} è {@code null} o la casella è vuota
     */
    public Pedina getPedina(int[] pos) {
        if (pos == null || caselle[pos[0]][pos[1]] == null) return null;
        return caselle[pos[0]][pos[1]].copy();
    }

    /**
     * Restituisce una copia della pedina attualmente selezionata.
     *
     * @return copia della pedina selezionata, oppure {@code null} se nessuna
     *         casella è selezionata
     */
    public Pedina getPedinaSelezionata() {
        if (casellaSelezionata == null) return null;
        return caselle[casellaSelezionata[0]][casellaSelezionata[1]].copy();
    }

    /**
     * Restituisce un clone delle coordinate della casella attualmente selezionata.
     *
     * @return array {@code [riga, colonna]} clonato, oppure {@code null} se nessuna
     *         casella è selezionata
     */
    public int[] getCasellaSelezionata() {
        if (casellaSelezionata == null) return null;
        return casellaSelezionata.clone();
    }

    /**
     * Restituisce il colore del giocatore di cui è il turno.
     *
     * @return {@link Color#white} o {@link Color#black}
     */
    public Color getTurno() {
        return turno;
    }

    /**
     * Imposta il colore del giocatore di cui è il turno.
     *
     * @param turno il nuovo turno; deve essere {@link Color#white} o {@link Color#black}
     * @throws IllegalArgumentException se {@code turno} è {@code null} oppure non è
     *                                  né bianco né nero
     */
    public void setTurno(Color turno) {
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere null");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il turno può essere solo bianco o nero");
        this.turno = turno;
    }

    /**
     * Restituisce il separatore usato nella rappresentazione testuale della scacchiera.
     *
     * @return stringa separatore (attualmente {@code ";"})
     */
    public static String getSEP() {
        return SEP;
    }

    /**
     * Restituisce l'impostazione corrente del cambio automatico del turno.
     *
     * @return {@code true} se il turno cambia automaticamente dopo ogni mossa,
     *         {@code false} altrimenti
     */
    public boolean getAutoCambioTurno() {
        return autoCambioTurno;
    }

    /**
     * Abilita o disabilita il cambio automatico del turno dopo ogni mossa.
     *
     * @param autoCambioTurno {@code true} per abilitare il cambio automatico,
     *                        {@code false} per gestirlo manualmente
     */
    public void setAutoCambioTurno(boolean autoCambioTurno) {
        this.autoCambioTurno = autoCambioTurno;
    }

    // -------------------------------------------------------------------------
    // Calcolo mosse valide
    // -------------------------------------------------------------------------

    /**
     * Filtra le mosse di un pedone applicando le regole specifiche del pezzo:
     * avanzamento frontale (di una o due caselle), cattura diagonale e cattura
     * en passant.
     *
     * @param caselle     la matrice delle pedine
     * @param pos         posizione {@code [riga, colonna]} del pedone
     * @param mosseValide lista delle mosse candidate generate dalla pedina
     * @return lista delle mosse effettivamente legali per il pedone
     * @throws IllegalArgumentException se {@code pos} o {@code mosseValide} sono
     *                                  {@code null}, oppure se la casella indicata
     *                                  non contiene un {@link Pedone}
     */
    private static List<int[]> filtraMossePedone(Pedina[][] caselle, int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("Puoi fare questi controlli solo sui pedoni");

        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) {
            //cattura diagonale
            if (mossa[1] != pos[1] && caselle[mossa[0]][mossa[1]] != null) mosseFiltrate.add(mossa);
            //en passant
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore().equals(Color.black) && pos[0] == DIMENSIONE - 4 && caselle[mossa[0] - 1][mossa[1]] instanceof Pedone pedone && pedone.getEnPassant()) mosseFiltrate.add(mossa);
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore().equals(Color.white) && pos[0] == 3 && caselle[mossa[0] + 1][mossa[1]] instanceof Pedone pedone && pedone.getEnPassant()) mosseFiltrate.add(mossa);
            //avanzamento frontale
            else if (mossa[1] == pos[1] && caselle[mossa[0]][mossa[1]] == null) {
                if (Math.abs(mossa[0] - pos[0]) == 1) mosseFiltrate.add(mossa);
                //movimento di due caselle
                else if (caselle[pos[0]][pos[1]].getColore().equals(Color.white) && caselle[mossa[0] + 1][mossa[1]] == null || caselle[pos[0]][pos[1]].getColore().equals(Color.black) && caselle[mossa[0] - 1][mossa[1]] == null) mosseFiltrate.add(mossa);
            }
        }

        return mosseFiltrate;
    }

    /**
     * Filtra le mosse di un alfiere (o della componente diagonale di una regina)
     * rimuovendo le destinazioni bloccate da pedine interposte lungo le diagonali.
     * <p>
     * Per ciascuna delle quattro direzioni diagonali viene determinata la prima
     * casella occupata (o il bordo della scacchiera); le mosse che la superano
     * vengono scartate.
     * </p>
     *
     * @param caselle     la matrice delle pedine
     * @param pos         posizione {@code [riga, colonna]} dell'alfiere o della regina
     * @param mosseValide lista delle mosse candidate generate dalla pedina
     * @return lista delle mosse diagonali effettivamente raggiungibili
     * @throws IllegalArgumentException se {@code pos} o {@code mosseValide} sono
     *                                  {@code null}, oppure se la casella indicata
     *                                  non contiene un {@link Alfiere} né una
     *                                  {@link Regina}
     */
    private static List<int[]> filtraMosseAlfiere(Pedina[][] caselle, int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Alfiere || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sugli alfieri o sulle regine");

        List<int[]> mosseFiltrate = new ArrayList<>();
        int[][] vincoli = new int[4][2];
        for (int i = 0; i < 4; i++) vincoli[i] = null;
        int y = pos[0];
        int x = pos[1];
        //per ogni direzione imposta il vincolo se trova una casella occupata o se è arrivato in fondo alla scacchiera
        for (int i = 1; i < DIMENSIONE; i++) {
            if (vincoli[0] == null && y + i < DIMENSIONE && x + i < DIMENSIONE && (caselle[y + i][x + i] != null || y + i + 1 >= DIMENSIONE || x + i + 1 >= DIMENSIONE)) vincoli[0] = new int[]{y + i, x + i};
            if (vincoli[1] == null && y + i < DIMENSIONE && x - i >= 0 && (caselle[y + i][x - i] != null || y + i + 1 >= DIMENSIONE || x - i - 1 < 0)) vincoli[1] = new int[]{y + i, x - i};
            if (vincoli[2] == null && y - i >= 0 && x + i < DIMENSIONE && (caselle[y - i][x + i] != null || y - i - 1 < 0 || x + i + 1 >= DIMENSIONE)) vincoli[2] = new int[]{y - i, x + i};
            if (vincoli[3] == null && y - i >= 0 && x - i >= 0 && (caselle[y - i][x - i] != null || y - i - 1 < 0 || x - i - 1 < 0)) vincoli[3] = new int[]{y - i, x - i};
            boolean esci = true;
            for (int[] v : vincoli)
                if (v == null) {
                    esci = false;
                    break;
                }
            if (esci) break;
        }

        //accetta solo le mosse valide che rientrano nei vincoli
        for (int[] mossa : mosseValide) {
            if (vincoli[0] != null && mossa[0] > y && mossa[1] > x && mossa[0] <= vincoli[0][0] && mossa[1] <= vincoli[0][1]) mosseFiltrate.add(mossa);
            else if (vincoli[1] != null && mossa[0] > y && mossa[1] < x && mossa[0] <= vincoli[1][0] && mossa[1] >= vincoli[1][1]) mosseFiltrate.add(mossa);
            else if (vincoli[2] != null && mossa[0] < y && mossa[1] > x && mossa[0] >= vincoli[2][0] && mossa[1] <= vincoli[2][1]) mosseFiltrate.add(mossa);
            else if (vincoli[3] != null && mossa[0] < y && mossa[1] < x && mossa[0] >= vincoli[3][0] && mossa[1] >= vincoli[3][1]) mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    /**
     * Filtra le mosse di una torre (o della componente lineare di una regina)
     * rimuovendo le destinazioni bloccate da pedine interposte lungo righe e colonne.
     * <p>
     * Per ciascuna delle quattro direzioni ortogonali viene determinata la prima
     * casella occupata; le mosse oltre tale vincolo vengono scartate.
     * </p>
     *
     * @param caselle     la matrice delle pedine
     * @param pos         posizione {@code [riga, colonna]} della torre o della regina
     * @param mosseValide lista delle mosse candidate generate dalla pedina
     * @return lista delle mosse ortogonali effettivamente raggiungibili
     * @throws IllegalArgumentException se {@code pos} o {@code mosseValide} sono
     *                                  {@code null}, oppure se la casella indicata
     *                                  non contiene una {@link Torre} né una
     *                                  {@link Regina}
     */
    private static List<int[]> filtraMosseTorre(Pedina[][] caselle, int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Torre || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sulle torri o sulle regine");

        List<int[]> mosseFiltrate = new ArrayList<>();
        int YAlto = -1;
        int YBasso = DIMENSIONE - 1;
        int XSinistra = -1;
        int XDestra = DIMENSIONE - 1;

        //per ogni direzione imposta il vincolo se trova una casella occupata
        for (int i = 0; i < DIMENSIONE; i++) {
            if (i < pos[0] && i > YAlto && caselle[i][pos[1]] != null) YAlto = i;
            else if (i > pos[0] && i < YBasso && caselle[i][pos[1]] != null) YBasso = i;
            if (i < pos[1] && i > XSinistra && caselle[pos[0]][i] != null) XSinistra = i;
            else if (i > pos[1] && i < XDestra && caselle[pos[0]][i] != null) XDestra = i;
        }

        //accetta solo le mosse valide che rientrano nei vincoli
        for (int[] mossa : mosseValide) {
            if (mossa[1] == pos[1] && mossa[0] < pos[0] && mossa[0] >= YAlto) mosseFiltrate.add(mossa);
            if (mossa[1] == pos[1] && mossa[0] > pos[0] && mossa[0] <= YBasso) mosseFiltrate.add(mossa);
            if (mossa[0] == pos[0] && mossa[1] < pos[1] && mossa[1] >= XSinistra) mosseFiltrate.add(mossa);
            if (mossa[0] == pos[0] && mossa[1] > pos[1] && mossa[1] <= XDestra) mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    /**
     * Filtra le mosse del re rimuovendo quelle che lo lascerebbero sotto scacco
     * e gestendo la legalità dell'arrocco (corto e lungo).
     * <p>
     * Se {@code controllaScacco} è {@code true}, ogni mossa candidata viene
     * simulata e scartata se il re risulterebbe in scacco. Inoltre l'arrocco viene
     * annullato se il re è attualmente sotto scacco, se transita per una casa
     * controllata dal nemico, se le case intermedie non sono libere o se la torre
     * interessata ha perso il diritto di arrocco.
     * </p>
     *
     * @param caselle         la matrice delle pedine
     * @param pos             posizione {@code [riga, colonna]} del re
     * @param mosseValide     lista delle mosse candidate generate dalla pedina
     * @param controllaScacco {@code true} per escludere le mosse che esporrebbero
     *                        il re allo scacco
     * @return lista delle mosse legali per il re
     * @throws IllegalArgumentException se {@code pos} o {@code mosseValide} sono
     *                                  {@code null}, oppure se la casella indicata
     *                                  non contiene un {@link Re}
     */
    private static List<int[]> filtraMosseRe(Pedina[][] caselle, int[] pos, List<int[]> mosseValide, boolean controllaScacco) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Re)) throw new IllegalArgumentException("Puoi fare questi controlli solo sul re");

        List<int[]> mosseFiltrate = new ArrayList<>();
        boolean annullaArroccoSx = controllaScacco && isScaccoRe(caselle, pos);
        boolean annullaArroccoDx = annullaArroccoSx;

        for (int[] mossa : mosseValide) {
            //se il re sarebbe sotto scacco rimuove la mossa
            if (controllaScacco && isScaccoRe(caselle, mossa, caselle[pos[0]][pos[1]].getColore())) {
                //rimuove l'arrocco se la mossa intermedia comporterebbe uno scacco
                if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] + 1) annullaArroccoDx = true;
                else if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] - 1) annullaArroccoSx = true;
                continue;
            }

            //arrocco
            if (Math.abs(pos[1] - mossa[1]) == 2 && ((Re) caselle[pos[0]][pos[1]]).getArrocco()) {
                if (pos[1] - mossa[1] == 2 && caselle[pos[0]][pos[1] - 1] == null && caselle[pos[0]][pos[1] - 2] == null && caselle[pos[0]][pos[1] - 3] == null && caselle[pos[0]][0] instanceof Torre torre && torre.getArrocco()) mosseFiltrate.add(mossa);
                if (pos[1] - mossa[1] == -2 && caselle[pos[0]][pos[1] + 1] == null && caselle[pos[0]][pos[1] + 2] == null && caselle[pos[0]][DIMENSIONE - 1] instanceof Torre torre && torre.getArrocco()) mosseFiltrate.add(mossa);
            }
            //mossa normale
            else mosseFiltrate.add(mossa);
        }

        if (annullaArroccoDx) mosseFiltrate.removeIf(m -> m[1] - pos[1] == 2);
        if (annullaArroccoSx) mosseFiltrate.removeIf(m -> m[1] - pos[1] == -2);

        return mosseFiltrate;
    }

    /**
     * Rimuove dalla lista delle mosse candidate quelle che porterebbero la pedina
     * a catturare un proprio pezzo (stesso colore).
     *
     * @param caselle       la matrice delle pedine
     * @param mosseValide   lista delle mosse candidate
     * @param colorePedina  colore della pedina da muovere
     * @return lista delle mosse prive di destinazioni occupate dallo stesso colore
     * @throws IllegalArgumentException se {@code colorePedina} o {@code mosseValide}
     *                                  sono {@code null}
     */
    private static List<int[]> rimuoviMosseStessoColore(Pedina[][] caselle, List<int[]> mosseValide, Color colorePedina) {
        if (colorePedina == null || mosseValide == null) throw new IllegalArgumentException("Il colore e le mosse valide non possono essere parametri null");
        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) if (caselle[mossa[0]][mossa[1]] == null || !caselle[mossa[0]][mossa[1]].getColore().equals(colorePedina)) mosseFiltrate.add(mossa);
        return mosseFiltrate;
    }

    /**
     * Calcola le mosse legali della pedina in {@code pos} applicando tutti i filtri
     * specifici del tipo di pezzo (bloccaggio, arrocco, en passant, ecc.).
     * <p>
     * Il parametro {@code controllaScacco} consente di disabilitare il controllo
     * scacco durante la simulazione ricorsiva delle mosse avversarie, evitando
     * ricorsioni infinite.
     * </p>
     *
     * @param caselle         la matrice delle pedine
     * @param pos             posizione {@code [riga, colonna]} della pedina
     * @param controllaScacco {@code true} per filtrare le mosse che esporrebbero
     *                        il proprio re allo scacco
     * @return lista delle mosse legali per il pezzo in {@code pos}
     * @throws IllegalArgumentException se {@code pos} è {@code null} o la casella
     *                                  è vuota
     * @throws IllegalStateException    se il tipo di pedina non è riconosciuto
     */
    private static List<int[]> ottieniMosseFiltrate(Pedina[][] caselle, int[] pos, boolean controllaScacco) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        Pedina p = caselle[pos[0]][pos[1]];
        if (p == null) throw new IllegalArgumentException("Non puoi inserire una posizione che corrisponde a null nella scacchiera");
        List<int[]> mosseValide = rimuoviMosseStessoColore(caselle, p.getMosseValide(), p.getColore());
        switch (p) {
            case Pedone _ -> mosseValide = filtraMossePedone(caselle, pos, mosseValide);
            case Alfiere _ -> mosseValide = filtraMosseAlfiere(caselle, pos, mosseValide);
            case Torre _ -> mosseValide = filtraMosseTorre(caselle, pos, mosseValide);
            case Regina _ -> {
                List<int[]> mosseDiagonali = filtraMosseAlfiere(caselle, pos, mosseValide);
                List<int[]> mosseLineari = filtraMosseTorre(caselle, pos, mosseValide);
                mosseValide = new ArrayList<>();
                mosseValide.addAll(mosseDiagonali);
                mosseValide.addAll(mosseLineari);
            }
            case Re _ -> mosseValide = filtraMosseRe(caselle, pos, mosseValide, controllaScacco);
            case Cavallo _ -> {}
            default -> throw new IllegalStateException("Tipo pedina non valido: " + p.getClass().getSimpleName());
        }
        return mosseValide;
    }

    /**
     * Calcola le mosse legali della pedina in {@code pos} con il controllo scacco
     * abilitato. Equivale a {@code ottieniMosseFiltrate(caselle, pos, true)}.
     *
     * @param caselle la matrice delle pedine
     * @param pos     posizione {@code [riga, colonna]} della pedina
     * @return lista delle mosse legali per il pezzo in {@code pos}
     */
    private static List<int[]> ottieniMosseFiltrate(Pedina[][] caselle, int[] pos) {
        return ottieniMosseFiltrate(caselle, pos, true);
    }

    /**
     * Trova la posizione del re del colore indicato nella matrice fornita.
     *
     * @param caselle la matrice delle pedine in cui cercare
     * @param c       il colore del re da trovare
     * @return array {@code [riga, colonna]} della posizione del re, oppure
     *         {@code null} se il re non è presente nella scacchiera
     * @throws IllegalArgumentException se {@code c} è {@code null}
     */
    private static int[] trovaPosRe(Pedina[][] caselle, Color c) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p instanceof Re && p.getColore().equals(c)) return p.getPosizione();
            }
        }
        return null;
    }

    /**
     * Verifica se una casella è attaccata da almeno una pedina avversaria,
     * simulando tutte le mosse possibili dell'avversario.
     * <p>
     * Questo metodo statico è usato internamente per determinare se il re sarebbe
     * in scacco in una certa posizione, anche ipotetica.
     * </p>
     *
     * @param caselle   la matrice delle pedine
     * @param posRe     posizione {@code [riga, colonna]} da controllare
     * @param coloreRe  colore del re; se {@code null} viene dedotto dalla casella
     *                  in {@code posRe} (che deve contenere un {@link Re})
     * @return {@code true} se la posizione è sotto attacco avversario
     * @throws IllegalArgumentException se {@code posRe} è {@code null}, se
     *                                  {@code coloreRe} è {@code null} e la casella
     *                                  non contiene un re, oppure se il colore non
     *                                  è né bianco né nero
     */
    private static boolean isScaccoRe(Pedina[][] caselle, int[] posRe, Color coloreRe) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può essere un parametro null");
        if (coloreRe == null) {
            //se non viene fornito il colore del re e la casella nella posizione indicata non è un re lancia eccezione, altrimenti prende il colore della casella indicata
            if (caselle[posRe[0]][posRe[1]] == null || !(caselle[posRe[0]][posRe[1]] instanceof Re)) throw new IllegalArgumentException("Non è stato fornito il colore del re");
            coloreRe = caselle[posRe[0]][posRe[1]].getColore();
        }
        if (!coloreRe.equals(Color.white) && !coloreRe.equals(Color.black)) throw new IllegalArgumentException("Il colore del re può essere solo bianco o nero");

        //simula ogni mossa possibile dell'avversario per vedere se il re sarebbe in scacco
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (caselle[i][j] == null || caselle[i][j].getColore().equals(coloreRe)) continue;
                for (int[] mossa : ottieniMosseFiltrate(caselle, new int[]{i, j}, false)) {
                    if (mossa[0] == posRe[0] && mossa[1] == posRe[1] && !(caselle[i][j] instanceof Pedone && mossa[1] == j)) return true;
                }
            }
        }

        return false;
    }

    /**
     * Verifica se il re del colore indicato è attualmente sotto scacco,
     * cercando automaticamente la sua posizione nella scacchiera.
     *
     * @param caselle  la matrice delle pedine
     * @param coloreRe colore del re da controllare
     * @return {@code true} se il re è sotto scacco
     */
    private static boolean isScaccoRe(Pedina[][] caselle, Color coloreRe) {
        return isScaccoRe(caselle, trovaPosRe(caselle, coloreRe), coloreRe);
    }

    /**
     * Verifica se il re che si trova nella posizione indicata è sotto scacco.
     * Il colore viene dedotto dalla pedina presente in {@code posRe}.
     *
     * @param caselle la matrice delle pedine
     * @param posRe   posizione {@code [riga, colonna]} del re
     * @return {@code true} se il re è sotto scacco
     * @throws IllegalArgumentException se {@code posRe} è {@code null}
     */
    private static boolean isScaccoRe(Pedina[][] caselle, int[] posRe) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può essere un parametro null");
        return isScaccoRe(caselle, posRe, caselle[posRe[0]][posRe[1]].getColore());
    }

    /**
     * Verifica se il re nella posizione indicata è sotto scacco, usando la
     * scacchiera interna dell'istanza.
     *
     * @param posRe    posizione {@code [riga, colonna]} da controllare
     * @param coloreRe colore del re
     * @return {@code true} se la posizione è sotto scacco
     */
    public boolean isScaccoRe(int[] posRe, Color coloreRe) {
        return isScaccoRe(caselle, posRe, coloreRe);
    }

    /**
     * Verifica se il re del colore indicato è sotto scacco nella scacchiera
     * corrente.
     *
     * @param coloreRe colore del re da controllare
     * @return {@code true} se il re è sotto scacco
     */
    public boolean isScaccoRe(Color coloreRe) {
        return isScaccoRe(caselle, coloreRe);
    }

    /**
     * Verifica se il re nella posizione indicata è sotto scacco nella scacchiera
     * corrente. Il colore viene dedotto dalla pedina in {@code posRe}.
     *
     * @param posRe posizione {@code [riga, colonna]} del re
     * @return {@code true} se il re è sotto scacco
     */
    public boolean isScaccoRe(int[] posRe) {
        return isScaccoRe(caselle, posRe);
    }

    /**
     * Filtra ulteriormente una lista di mosse valide eliminando quelle che
     * lascerebbero il proprio re sotto scacco.
     * <p>
     * Per ogni mossa candidata, la pedina viene spostata temporaneamente nella
     * casella di destinazione, si verifica se il re risulterebbe in scacco e
     * successivamente lo stato viene ripristinato.
     * </p>
     *
     * @param caselle     la matrice delle pedine (viene modificata e ripristinata
     *                    durante i controlli)
     * @param pos         posizione {@code [riga, colonna]} della pedina da muovere
     * @param mosseValide lista delle mosse da filtrare
     * @return lista delle mosse che non espongono il re allo scacco
     * @throws IllegalArgumentException se {@code pos} o {@code mosseValide} sono
     *                                  {@code null}, oppure se la casella in
     *                                  {@code pos} è {@code null}
     */
    private static List<int[]> filtraMosseScacco(Pedina[][] caselle, int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null) throw new IllegalArgumentException("La posizione della pedina da controllare non può essere null nella scacchiera");

        List<int[]> mosseFiltrate = new ArrayList<>();
        Color c = caselle[pos[0]][pos[1]].getColore();

        for (int[] mossa : mosseValide) {
            //simula lo spostamento della pedina in ogni mossa disponibile e filtra le mosse che lascerebbero il re sotto scacco
            Pedina temp1 = caselle[pos[0]][pos[1]];
            Pedina temp2 = caselle[mossa[0]][mossa[1]];

            caselle[mossa[0]][mossa[1]] = temp1;
            caselle[pos[0]][pos[1]] = null;

            if (!(temp1 instanceof Re) && !isScaccoRe(caselle, c) || temp1 instanceof Re && !isScaccoRe(caselle, mossa, c)) mosseFiltrate.add(mossa);

            caselle[pos[0]][pos[1]] = temp1;
            caselle[mossa[0]][mossa[1]] = temp2;
        }

        return mosseFiltrate;
    }

    // -------------------------------------------------------------------------
    // Gestione gioco utente
    // -------------------------------------------------------------------------

    /**
     * Verifica che la matrice {@code caselle} sia valida (non {@code null},
     * dimensione 8×8, nessun sotto-array {@code null}).
     *
     * @param caselle la matrice da controllare
     * @throws IllegalArgumentException se la matrice è {@code null}, non è 8×8
     *                                  o contiene righe {@code null}
     */
    private static void controlliCaselle(Pedina[][] caselle) {
        if (caselle == null) throw new IllegalArgumentException("Le caselle non possono essere null");
        if (caselle.length != 8) throw new IllegalArgumentException("Caselle deve essere un array 8x8");
        for (Pedina[] riga : caselle) {
            if (riga == null) throw new IllegalArgumentException("I sotto array di caselle non possono essere null");
            if (riga.length != 8) throw new IllegalArgumentException("Caselle deve essere un array 8x8");
        }
    }

    /**
     * Calcola le mosse legali per la pedina in {@code pos} di turno {@code turno},
     * operando sulla matrice fornita.
     * <p>
     * La selezione ha esito positivo solo se la casella contiene una pedina del
     * colore del turno e non ci sono promozioni in sospeso.
     * </p>
     *
     * @param caselle la matrice delle pedine
     * @param pos     posizione {@code [riga, colonna]} della pedina da selezionare
     * @param turno   colore del giocatore corrente
     * @return lista delle mosse legali, oppure {@code null} se la selezione non
     *         è valida (casella vuota o pedina di colore sbagliato)
     * @throws IllegalArgumentException se {@code turno} è {@code null} o non è
     *                                  né bianco né nero, se {@code pos} è
     *                                  {@code null} o fuori dai limiti
     * @throws IllegalStateException    se è presente una promozione in sospeso
     */
    private static List<int[]> selezionaPedina(Pedina[][] caselle, int[] pos, Color turno) {
        controlliCaselle(caselle);
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere un parametro null");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");
        if (promozioneInSospeso(caselle) != null) throw new IllegalStateException("Impossibile selezionare una pedina: ci sono dei pedoni in fondo alla scacchiera non promossi");
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (pos[0] < 0 || pos[0] >= DIMENSIONE || pos[1] < 0 || pos[1] >= DIMENSIONE) throw new IllegalArgumentException("Posizione non valida");
        Pedina p = caselle[pos[0]][pos[1]];
        if (p == null || !p.getColore().equals(turno)) return null;

        return filtraMosseScacco(caselle, pos, ottieniMosseFiltrate(caselle, pos));
    }

    /**
     * Seleziona la pedina nella posizione indicata per il turno specificato,
     * aggiornando lo stato interno della scacchiera ({@code casellaSelezionata}
     * e {@code mosseValide}).
     *
     * @param pos   posizione {@code [riga, colonna]} della pedina da selezionare
     * @param turno colore del giocatore corrente
     * @return lista clonata delle mosse legali disponibili, oppure {@code null}
     *         se la selezione non è valida
     */
    public List<int[]> selezionaPedina(int[] pos, Color turno) {
        mosseValide = selezionaPedina(caselle, pos, turno);
        if (mosseValide == null) {
            casellaSelezionata = null;
            return null;
        }
        casellaSelezionata = pos.clone();
        List<int[]> copia = new ArrayList<>();
        for (int[] mossa : mosseValide) copia.add(mossa.clone());
        return copia;
    }

    /**
     * Seleziona la pedina specificata per il turno indicato.
     *
     * @param p     la pedina da selezionare; non può essere {@code null}
     * @param turno colore del giocatore corrente
     * @return lista delle mosse legali, oppure {@code null} se la selezione non
     *         è valida
     * @throws IllegalArgumentException se {@code p} è {@code null}
     */
    public List<int[]> selezionaPedina(Pedina p, Color turno) {
        if (p == null) throw new IllegalArgumentException("La pedina non può essere null");
        return selezionaPedina(caselle, p.getPosizione(), turno);
    }

    /**
     * Seleziona la pedina specificata usando il turno corrente della scacchiera.
     *
     * @param p la pedina da selezionare; non può essere {@code null}
     * @return lista delle mosse legali, oppure {@code null} se la selezione non
     *         è valida
     */
    public List<int[]> selezionaPedina(Pedina p) {
        return selezionaPedina(p, turno);
    }

    /**
     * Seleziona la pedina in {@code pos} usando il turno corrente della scacchiera.
     *
     * @param pos posizione {@code [riga, colonna]} della pedina da selezionare
     * @return lista delle mosse legali, oppure {@code null} se la selezione non
     *         è valida
     */
    public List<int[]> selezionaPedina(int[] pos) {
        return selezionaPedina(pos, turno);
    }

    /**
     * Deseleziona la pedina correntemente selezionata e svuota la lista delle
     * mosse valide.
     */
    public void deSelezionaPedina() {
        casellaSelezionata = null;
        if (mosseValide != null) mosseValide.clear();
    }

    /**
     * Esegue il movimento della pedina nella casella selezionata verso {@code pos},
     * operando sulla matrice fornita come parametro.
     * <p>
     * Gestisce i casi speciali di en passant e arrocco (corto e lungo).
     * Non aggiorna il turno né i contatori interni dell'istanza.
     * </p>
     *
     * @param caselle           la matrice delle pedine da modificare
     * @param mosseValide       lista delle mosse legali per la pedina selezionata
     * @param casellaSelezionata posizione {@code [riga, colonna]} della pedina
     *                          da muovere
     * @param pos               posizione di destinazione {@code [riga, colonna]}
     * @return {@code true} se la mossa è stata eseguita con successo,
     *         {@code false} se {@code pos} non è tra le mosse valide o
     *         {@code mosseValide} è {@code null}
     * @throws IllegalArgumentException se {@code pos} è {@code null} o fuori
     *                                  dai limiti della scacchiera
     */
    private static boolean muoviPedina(Pedina[][]caselle, List<int[]> mosseValide, int[] casellaSelezionata, int[] pos) {
        controlliCaselle(caselle);
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (pos[0] < 0 || pos[0] >= DIMENSIONE || pos[1] < 0 || pos[1] >= DIMENSIONE) throw new IllegalArgumentException("Posizione non valida");
        if (mosseValide == null) return false;

        //si assicura che la mossa scelta sia valida
        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == pos[0] && mossa[1] == pos[1]) {
                valido = true;
                break;
            }
        }

        if (valido) {
            Pedina p = caselle[casellaSelezionata[0]][casellaSelezionata[1]];
            Color c = p.getColore();

            //en passant
            if (p instanceof Pedone && pos[1] != casellaSelezionata[1] && caselle[pos[0]][pos[1]] == null) {
                if (p.getColore().equals(Color.white)) caselle[pos[0] + 1][pos[1]] = null;
                else if (p.getColore().equals(Color.black)) caselle[pos[0] - 1][pos[1]] = null;
            }

            for (Pedina[] riga : caselle) for (Pedina ped : riga) if (ped instanceof Pedone && !ped.getColore().equals(c)) ((Pedone) ped).rimuoviEnPassant();

            //arrocco
            if (p instanceof Re && casellaSelezionata[1] - pos[1] == 2) {
                //sinistra
                caselle[pos[0]][0].muovi(new int[]{pos[0], pos[1] + 1});
                caselle[pos[0]][pos[1] + 1] = caselle[pos[0]][0];
                caselle[pos[0]][0] = null;
            }
            else if (p instanceof Re && casellaSelezionata[1] - pos[1] == -2) {
                //destra
                caselle[pos[0]][DIMENSIONE - 1].muovi(new int[]{pos[0], pos[1] - 1});
                caselle[pos[0]][pos[1] - 1] = caselle[pos[0]][DIMENSIONE - 1];
                caselle[pos[0]][DIMENSIONE - 1] = null;
            }

            p.muovi(pos);
            caselle[pos[0]][pos[1]] = p;
            caselle[casellaSelezionata[0]][casellaSelezionata[1]] = null;
        }
        return valido;
    }

    /**
     * Sposta la pedina attualmente selezionata verso la posizione {@code pos},
     * aggiornando lo stato interno della scacchiera.
     * <p>
     * Se la mossa è valida: aggiorna i contatori {@code mosse} e
     * {@code mosseNeutre}, salva lo stato su file tramite
     * {@link PartitaFileManager}, e — a meno che non sia avviata una promozione —
     * chiama {@link #cambiaTurno()}. Se la mossa avvia una promozione, la
     * scacchiera verrà aggiornata solo quando verrà chiamato
     * {@link #promuoviPedone(int[], int)}.
     * </p>
     *
     * @param pos posizione di destinazione {@code [riga, colonna]}
     * @return {@code true} se la mossa è stata eseguita con successo,
     *         {@code false} se non è presente una pedina selezionata o la
     *         destinazione non è valida
     * @throws IllegalArgumentException se {@code pos} è {@code null} o fuori
     *                                  dai limiti della scacchiera
     */
    public boolean muoviPedina(int[] pos) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (pos[0] < 0 || pos[0] >= DIMENSIONE || pos[1] < 0 || pos[1] >= DIMENSIONE) throw new IllegalArgumentException("Posizione non valida");
        if (casellaSelezionata == null || caselle[casellaSelezionata[0]][casellaSelezionata[1]] == null) return false;
        Pedina pSel = caselle[casellaSelezionata[0]][casellaSelezionata[1]];
        Pedina pPos = caselle[pos[0]][pos[1]];
        boolean azzeraMosseNeutre = pPos != null;
        Color c = pSel.getColore();

        boolean temp = muoviPedina(caselle, mosseValide, casellaSelezionata, pos);
        if (temp) {
            if (!(mosseNeutre == 0 && c.equals(Color.black))) mosseNeutre++;
            if (azzeraMosseNeutre || pSel instanceof Pedone) mosseNeutre = 0;
            //se la mossa inizia una promozione la scacchiera verrà aggiornata solo quando verrà chiamato il metodo promuoviPedone
            if (!(pSel instanceof Pedone && (pos[0] == DIMENSIONE - 1 || pos[0] == 0))) {
                mosse++;
                PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
                cambiaTurno();
            }
            mosseValide.clear();
        }
        casellaSelezionata = null;
        return temp;
    }

    /**
     * Controlla se esiste un pedone in fondo alla scacchiera in attesa di
     * promozione, operando sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine da controllare
     * @return una copia del pedone in attesa di promozione, oppure {@code null}
     *         se non ve ne sono
     * @throws IllegalArgumentException se la matrice è invalida (vedi
     *                                  {@link #controlliCaselle(Pedina[][])})
     */
    private static Pedina promozioneInSospeso(Pedina[][] caselle) {
        controlliCaselle(caselle);
        for (Pedina ped : caselle[0]) if (ped instanceof Pedone) return ped.copy();
        for (Pedina ped : caselle[DIMENSIONE - 1]) if (ped instanceof Pedone) return ped.copy();
        return null;
    }

    /**
     * Controlla se esiste un pedone in fondo alla scacchiera interna in attesa di
     * promozione.
     *
     * @return una copia del pedone in attesa di promozione, oppure {@code null}
     *         se non ve ne sono
     */
    public Pedina promozioneInSospeso() {
        return promozioneInSospeso(caselle);
    }

    /**
     * Promuove il pedone in {@code pos} al pezzo indicato da {@code numeroPedina},
     * operando sulla matrice fornita come parametro.
     * <p>
     * La mappatura del numero è: {@code 1} (o qualsiasi valore non riconosciuto)
     * → Regina; {@code 2} → Torre; {@code 3} → Alfiere; {@code 4} → Cavallo.
     * </p>
     *
     * @param caselle      la matrice delle pedine
     * @param pos          posizione {@code [riga, colonna]} del pedone da promuovere
     * @param numeroPedina numero che identifica il pezzo scelto per la promozione
     * @throws IllegalArgumentException se {@code pos} è {@code null}, oppure se
     *                                  la casella non contiene un pedone in fondo
     *                                  alla scacchiera
     */
    private static void promuoviPedone(Pedina[][] caselle, int[] pos, int numeroPedina) {
        controlliCaselle(caselle);
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (!((pos[0] == 0 || pos[0] == DIMENSIONE - 1) && caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("La pedina che hai scelto non è un pedone in fondo alla scacchiera");
        Color c = caselle[pos[0]][pos[1]].getColore();

        //1 -> regina; 2 -> torre; 3 -> alfiere; 4 -> cavallo; default -> regina
        switch (numeroPedina) {
            case 2 -> caselle[pos[0]][pos[1]] = new Torre(c, pos);
            case 3 -> caselle[pos[0]][pos[1]] = new Alfiere(c, pos);
            case 4 -> caselle[pos[0]][pos[1]] = new Cavallo(c, pos);
            default -> caselle[pos[0]][pos[1]] = new Regina(c, pos);
        }
    }

    /**
     * Promuove il pedone in {@code pos} al pezzo scelto, aggiornando i contatori
     * interni e il file di stato tramite {@link PartitaFileManager}, e cambia il
     * turno.
     *
     * @param pos          posizione {@code [riga, colonna]} del pedone da promuovere
     * @param numeroPedina numero che identifica il pezzo scelto per la promozione
     *                     (mappatura: {@code 2}→Torre, {@code 3}→Alfiere,
     *                     {@code 4}→Cavallo, altrimenti Regina)
     */
    public void promuoviPedone(int[] pos, int numeroPedina) {
        promuoviPedone(caselle, pos, numeroPedina);
        //aggiorna la scacchiera
        mosse++;
        PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
        cambiaTurno();
    }

    /**
     * Promuove la pedina specificata al pezzo scelto.
     *
     * @param p            il pedone da promuovere; non può essere {@code null}
     * @param numeroPedina numero che identifica il pezzo scelto per la promozione
     * @throws IllegalArgumentException se {@code p} è {@code null}
     */
    public void promuoviPedone(Pedina p, int numeroPedina) {
        if (p == null) throw new IllegalArgumentException("La pedina non può essere null");
        promuoviPedone(p.getPosizione(), numeroPedina);
    }

    /**
     * Cambia il turno dal colore corrente all'altro, ma solo se
     * {@link #autoCambioTurno} è abilitato.
     */
    private void cambiaTurno() {
        if (!autoCambioTurno) return;
        if (turno.equals(Color.white)) turno = Color.black;
        else turno = Color.white;
    }

    // -------------------------------------------------------------------------
    // Condizioni di vittoria / pareggio
    // -------------------------------------------------------------------------

    /**
     * Determina lo stato corrente della partita per il giocatore del colore
     * indicato, operando sulla matrice fornita.
     * <p>
     * Controlla nell'ordine: promozione in sospeso, assenza di mosse legali
     * (scaccomatto o stallo), materiale insufficiente. Non controlla pareggio
     * per mosse neutre né per ripetizioni (questi controlli aggiuntivi sono
     * eseguiti nell'overload {@link #getStatoPartita(Color)}).
     * </p>
     *
     * @param caselle la matrice delle pedine
     * @param turno   colore del giocatore corrente
     * @return lo {@link StatoPartita} corrispondente alla situazione attuale
     * @throws IllegalArgumentException se {@code turno} è {@code null} o non è
     *                                  né bianco né nero
     */
    private static StatoPartita getStatoPartita(Pedina[][] caselle, Color turno) {
        controlliCaselle(caselle);
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere un parametro null");
        if (!turno.equals(Color.black) && !turno.equals(Color.white)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");

        if (promozioneInSospeso(caselle) != null) return StatoPartita.PROMOZIONE_IN_SOSPESO;

        //controlla se il giocatore del colore del turno scelto non ha più mosse disponibili
        boolean noMosse = true;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p == null || !p.getColore().equals(turno)) continue;
                if (!filtraMosseScacco(caselle, p.getPosizione(), ottieniMosseFiltrate(caselle, p.getPosizione())).isEmpty()) {
                    noMosse = false;
                    break;
                }
            }
            if (!noMosse) break;
        }

        //se non ci sono mosse disponibili e il re è sotto scacco indica il vincitore, altrimenti è stallo
        if (noMosse) {
            if (isScaccoRe(caselle, turno)) {
                if (turno.equals(Color.white)) return StatoPartita.VITTORIA_NERO;
                else return StatoPartita.VITTORIA_BIANCO;
            }
            return StatoPartita.STALLO;
        }
        if (materialeInsufficiente(caselle, Color.black) && materialeInsufficiente(caselle, Color.white)) return StatoPartita.MATERIALE_INSUFFICIENTE;
        return StatoPartita.IN_CORSO;
    }

    /**
     * Determina lo stato corrente della partita per il giocatore del colore
     * indicato, aggiungendo i controlli per pareggio per mosse neutre e per
     * ripetizione di posizione.
     *
     * @param turno colore del giocatore corrente
     * @return lo {@link StatoPartita} corrispondente alla situazione attuale
     */
    public StatoPartita getStatoPartita(Color turno) {
        StatoPartita sp = getStatoPartita(caselle, turno);
        if (sp == StatoPartita.IN_CORSO) {
            if (mosseNeutre >= 150) return StatoPartita.PAREGGIO_MOSSE_NEUTRE;
            if (pareggioRipetizioni()) return StatoPartita.PAREGGIO_RIPETIZIONI;
        }
        return sp;
    }

    /**
     * Determina lo stato corrente della partita usando il turno corrente
     * dell'istanza.
     *
     * @return lo {@link StatoPartita} corrispondente alla situazione attuale
     */
    public StatoPartita getStatoPartita() {
        return getStatoPartita(turno);
    }

    /**
     * Calcola il valore materiale totale del giocatore del colore indicato,
     * sommando il materiale di tutti i suoi pezzi (escluso il re).
     *
     * @param caselle la matrice delle pedine
     * @param c       colore del giocatore
     * @return somma dei valori materiali
     * @throws IllegalArgumentException se {@code c} è {@code null} o non è né
     *                                  bianco né nero
     */
    private static int getMateriale(Pedina[][] caselle, Color c) {
        controlliCaselle(caselle);
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        int materiale = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) if (p != null && p.getColore().equals(c)) materiale += p.getMateriale();
        }
        return materiale;
    }

    /**
     * Calcola il valore materiale totale del giocatore del colore indicato
     * nella scacchiera corrente.
     *
     * @param c colore del giocatore
     * @return somma dei valori materiali
     */
    public int getMateriale(Color c) {
        return getMateriale(caselle, c);
    }

    /**
     * Restituisce il valore materiale del giocatore del colore indicato
     * alla mossa specificata, leggendo lo stato archiviato su file.
     * <p>
     * Se {@code mossa} coincide con il numero di mosse corrente, il calcolo
     * viene effettuato direttamente sulla scacchiera in memoria.
     * </p>
     *
     * @param c     colore del giocatore
     * @param mossa indice della mossa (compreso tra {@code 0} e {@link #getMosse()})
     * @return valore materiale alla mossa indicata, oppure {@code -1} se il
     *         file corrispondente non è disponibile
     * @throws IllegalArgumentException se {@code c} è {@code null} o se
     *                                  {@code mossa} è fuori dall'intervallo
     *                                  {@code [0, mosse]}
     */
    public int getMaterialeMossa(Color c, int mossa) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (mossa < 0 || mossa > mosse) throw new IllegalArgumentException("Il valore della mossa non può essere maggiore di quella corrente o minore di 0");
        if (mossa == mosse) return getMateriale(c);
        int materiale = 0;
        String s = PartitaFileManager.leggiScacchiera(mossa);
        if (s == null) return -1;
        //prende la stringa della scacchiera alla mossa indicata come parametro e restituisce il materiale del giocatore del colore passato come parametro
        for (String riga : s.split("\n")) {
            for (String pedina : riga.split(SEP)) {
                if (pedina.length() < 2) continue;
                if (!(pedina.charAt(1) == 'B' && c.equals(Color.white) || pedina.charAt(1) == 'N' && c.equals(Color.black))) continue;
                switch (pedina.charAt(0)) {
                    case 'Q' -> materiale += Regina.MATERIALE;
                    case 'T' -> materiale += Torre.MATERIALE;
                    case 'A' -> materiale += Alfiere.MATERIALE;
                    case 'C' -> materiale += Cavallo.MATERIALE;
                    case 'P' -> materiale += Pedone.MATERIALE;
                    default -> {}
                }
            }
        }
        return materiale;
    }

    /**
     * Determina se il giocatore del colore indicato ha materiale insufficiente
     * per dare scaccomatto.
     * <p>
     * Un giocatore ha materiale insufficiente se oltre al re possiede soltanto
     * un cavallo o un alfiere (valore materiale 3), oppure non possiede alcun
     * altro pezzo.
     * </p>
     *
     * @param caselle la matrice delle pedine
     * @param c       colore del giocatore
     * @return {@code true} se il giocatore ha materiale insufficiente
     */
    //se un giocatore oltre al re non ha pedine o ha solo un cavallo o solo un alfiere allora ha materiale insufficiente per vincere
    private static boolean materialeInsufficiente(Pedina[][] caselle, Color c) {
        controlliCaselle(caselle);
        int materiale = 0;
        int pedineRimaste = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p != null && p.getColore().equals(c) && !(p instanceof Re)) {
                    pedineRimaste++;
                    materiale += p.getMateriale();
                }
            }
        }
        return pedineRimaste == 1 && materiale == 3 || pedineRimaste == 0;
    }

    /**
     * Determina se il giocatore del colore indicato ha materiale insufficiente
     * per dare scaccomatto nella scacchiera corrente.
     *
     * @param c colore del giocatore
     * @return {@code true} se il giocatore ha materiale insufficiente
     */
    public boolean materialeInsufficiente(Color c) {
        return materialeInsufficiente(caselle, c);
    }

    /**
     * Determina se il giocatore del colore indicato aveva materiale insufficiente
     * alla mossa specificata, leggendo lo stato archiviato su file.
     *
     * @param c     colore del giocatore
     * @param mossa indice della mossa (compreso tra {@code 0} e {@link #getMosse()})
     * @return {@code true} se il giocatore aveva materiale insufficiente alla
     *         mossa indicata, {@code false} se l'archivio non è disponibile
     * @throws IllegalArgumentException se {@code c} è {@code null} o non è né
     *                                  bianco né nero, o se {@code mossa} è fuori
     *                                  dall'intervallo {@code [0, mosse]}
     */
    public boolean materialeInsufficienteMossa(Color c, int mossa) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        if (mossa < 0 || mossa > mosse) throw new IllegalArgumentException("Il valore della mossa non può essere maggiore di quella corrente o minore di 0");
        int materiale = getMaterialeMossa(c, mossa);
        if (materiale == 0) return true;
        if (materiale == 3) {
            String s = PartitaFileManager.leggiScacchiera(mossa);
            if (s == null) return false;
            for (String riga : s.split("\n")) {
                for (String pedina : riga.split(SEP)) {
                    if (pedina.length() < 2) continue;
                    if (!(pedina.charAt(1) == 'B' && c.equals(Color.white) || pedina.charAt(1) == 'N' && c.equals(Color.black))) continue;
                    if (pedina.charAt(0) == 'C' || pedina.charAt(0) == 'A') return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica se la partita dovrebbe concludersi in pareggio per ripetizione
     * di posizione.
     * <p>
     * Secondo le regole implementate, se la stessa coppia di posizioni
     * (mossa del bianco + risposta del nero) si è ripetuta almeno 4 volte
     * (corrispondenti alla quinta ripetizione complessiva), la partita è
     * considerata patta. Il confronto include anche le informazioni sulle
     * possibilità di arrocco e di en passant (per garantire che le posizioni
     * siano effettivamente identiche).
     * </p>
     *
     * @return {@code true} se le condizioni per il pareggio per ripetizione
     *         sono soddisfatte
     */
    //se una mossa del bianco seguita da una mossa del nero è identica per 5 volte allora è automaticamente pareggio
    public boolean pareggioRipetizioni() {
        if (mosse < 17) return false;
        String mossaCorrente = PartitaFileManager.leggiScacchiera(mosse, true);
        String mossaAvversario = PartitaFileManager.leggiScacchiera(mosse - 1, true);
        if (mossaCorrente == null || mossaAvversario == null) return false;
        int mosseRipetute = 0;

        for (int i = mosse - 4; i > 0; i -= 2) {
            String m1 = PartitaFileManager.leggiScacchiera(i, true);
            String m2 = PartitaFileManager.leggiScacchiera(i - 1, true);
            if (m1 != null && m2 != null && m1.equals(mossaCorrente) && m2.equals(mossaAvversario)) mosseRipetute++;
        }
        return mosseRipetute >= 4;
    }

    // -------------------------------------------------------------------------
    // Metodi con caselle come parametro (per bot)
    // -------------------------------------------------------------------------

    /**
     * Variante statica di {@link #selezionaPedina(int[], Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita senza
     * modificare lo stato interno dell'istanza.
     *
     * @param caselle la matrice delle pedine
     * @param pos     posizione {@code [riga, colonna]} della pedina
     * @param turno   colore del giocatore corrente
     * @return lista clonata delle mosse legali, oppure {@code null}
     */
    public static List<int[]> selezionaPedinaCaselle(Pedina[][] caselle, int[] pos, Color turno) {
        List<int[]> mosseValide = selezionaPedina(caselle, pos, turno);
        if (mosseValide == null) return null;
        List<int[]> copia = new ArrayList<>();
        for (int[] mossa : mosseValide) copia.add(mossa.clone());
        return copia;
    }

    /**
     * Variante statica di {@link #muoviPedina(int[])} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle            la matrice delle pedine
     * @param mosseValide        lista delle mosse legali per la pedina selezionata
     * @param casellaSelezionata posizione della pedina da muovere
     * @param pos                posizione di destinazione
     * @return {@code true} se la mossa è stata eseguita con successo
     */
    public static boolean muoviPedinaCaselle(Pedina[][] caselle, List<int[]> mosseValide, int[] casellaSelezionata, int[] pos) {
        return muoviPedina(caselle, mosseValide, casellaSelezionata, pos);
    }

    /**
     * Variante statica di {@link #promuoviPedone(int[], int)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle      la matrice delle pedine
     * @param pos          posizione del pedone da promuovere
     * @param numeroPedina numero che identifica il pezzo scelto per la promozione
     */
    public static void promozionePedoneCaselle(Pedina[][] caselle, int[] pos, int numeroPedina) {
        promuoviPedone(caselle, pos, numeroPedina);
    }

    /**
     * Variante statica di {@link #getStatoPartita(Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @param turno   colore del giocatore corrente
     * @return lo {@link StatoPartita} corrispondente alla situazione attuale
     */
    public static StatoPartita statoPartitaCaselle(Pedina[][] caselle, Color turno) {
        return getStatoPartita(caselle, turno);
    }

    /**
     * Variante statica di {@link #promozioneInSospeso()} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @return copia del pedone in attesa di promozione, oppure {@code null}
     */
    public static Pedina promozioneInSospesoCaselle(Pedina[][] caselle) {
        return promozioneInSospeso(caselle);
    }

    /**
     * Variante statica di {@link #getMateriale(Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @param c       colore del giocatore
     * @return valore materiale totale del giocatore
     */
    public static int getMaterialeCaselle(Pedina[][] caselle, Color c) {
        return getMateriale(caselle, c);
    }

    /**
     * Variante statica di {@link #materialeInsufficiente(Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @param c       colore del giocatore
     * @return {@code true} se il giocatore ha materiale insufficiente
     */
    public static boolean materialeInsufficienteCaselle(Pedina[][] caselle, Color c) {
        return materialeInsufficiente(caselle, c);
    }

    /**
     * Variante statica di {@link #getStringaScacchiera(boolean)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @param info    {@code true} per includere le informazioni su arrocco ed
     *                en passant (necessarie per il rilevamento delle ripetizioni)
     * @return rappresentazione testuale della scacchiera
     */
    public static String getStringaScacchieraCaselle(Pedina[][] caselle, boolean info) {
        return getStringaScacchiera(caselle, info);
    }

    /**
     * Variante statica di {@link #isScaccoRe(int[], Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle  la matrice delle pedine
     * @param posRe    posizione del re
     * @param coloreRe colore del re
     * @return {@code true} se la posizione è sotto scacco
     */
    public static boolean isScaccoReCaselle(Pedina[][] caselle, int[] posRe, Color coloreRe) {
        return isScaccoRe(caselle, posRe, coloreRe);
    }

    /**
     * Variante statica di {@link #isScaccoRe(Color)} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle  la matrice delle pedine
     * @param coloreRe colore del re da controllare
     * @return {@code true} se il re è sotto scacco
     */
    public static boolean isScaccoReCaselle(Pedina[][] caselle, Color coloreRe) {
        return isScaccoRe(caselle, coloreRe);
    }

    /**
     * Variante statica di {@link #isScaccoRe(int[])} per l'uso esterno
     * (es. da parte di un bot). Opera direttamente sulla matrice fornita.
     *
     * @param caselle la matrice delle pedine
     * @param posRe   posizione del re
     * @return {@code true} se il re è sotto scacco
     */
    public static boolean isScaccoReCaselle(Pedina[][] caselle, int[] posRe) {
        return isScaccoRe(caselle, posRe);
    }

    // -------------------------------------------------------------------------
    // Scrittura e lettura scacchiera su file
    // -------------------------------------------------------------------------

    /**
     * Genera la rappresentazione testuale della scacchiera nel formato CSV a righe,
     * usando {@link #SEP} come separatore di colonne.
     * <p>
     * Ogni pedina è codificata con due caratteri: il primo identifica il tipo
     * ({@code Q}=Regina, iniziale della classe per gli altri pezzi), il secondo
     * il colore ({@code B}=bianco, {@code N}=nero). Le caselle vuote sono
     * rappresentate da {@code --}.
     * </p>
     * <p>
     * Se {@code info} è {@code true}, alla rappresentazione della griglia viene
     * aggiunta una riga con le informazioni sulle possibilità di en passant e
     * di arrocco, necessarie per il corretto rilevamento delle ripetizioni di
     * posizione.
     * </p>
     *
     * @param caselle la matrice delle pedine
     * @param info    {@code true} per includere le informazioni aggiuntive su
     *                arrocco ed en passant
     * @return stringa testuale che rappresenta lo stato della scacchiera
     * @throws IllegalArgumentException se la matrice è invalida (vedi
     *                                  {@link #controlliCaselle(Pedina[][])})
     */
    private static String getStringaScacchiera(Pedina[][] caselle, boolean info) {
        controlliCaselle(caselle);
        StringBuilder scacchiera;
        scacchiera = new StringBuilder();
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p == null) scacchiera.append("--");
                else {
                    if (p instanceof Regina) scacchiera.append("Q");
                    else scacchiera.append(p.getClass().getSimpleName().charAt(0));
                    if (p.getColore().equals(Color.white)) scacchiera.append("B");
                    else scacchiera.append("N");
                }
                scacchiera.append(SEP);
            }
            scacchiera.deleteCharAt(scacchiera.length() - 1);
            scacchiera.append("\n");
        }
        //per avere un pareggio per ripetizione le condizioni di arrocco e di en passant devono essere sempre uguali, queste vengono scritte se info == true
        if (info) {
            StringBuilder infoRipetizioni = new StringBuilder();
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) {
                    if (p == null) continue;
                    int[] pos = p.getPosizione();
                    String col = "B";
                    if (!p.getColore().equals(Color.white)) col = "N";
                    if (p instanceof Pedone) {
                        for (int[] mossa : filtraMosseScacco(caselle, pos, ottieniMosseFiltrate(caselle, pos))) {
                            if (mossa[1] == pos[1] - 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("sx").append(col). append(" ");
                            if (mossa[1] == pos[1] + 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("dx").append(col).append(" ");
                        }
                    }
                    else if (p instanceof Re) {
                        for (int[] mossa : filtraMosseScacco(caselle, pos, ottieniMosseFiltrate(caselle, pos))) {
                            if (mossa[1] == pos[1] - 2) infoRipetizioni.append("asx").append(col).append(" ");
                            if (mossa[1] == pos[1] + 2) infoRipetizioni.append("adx").append(col).append(" ");
                        }
                    }
                }
            }
            scacchiera.append(infoRipetizioni);
        }
        return scacchiera.toString();
    }

    /**
     * Genera la rappresentazione testuale della scacchiera corrente.
     *
     * @param info {@code true} per includere le informazioni su arrocco ed
     *             en passant
     * @return stringa testuale dello stato della scacchiera
     */
    public String getStringaScacchiera(boolean info) {
        return getStringaScacchiera(caselle, info);
    }

    /**
     * Genera la rappresentazione testuale della scacchiera corrente senza le
     * informazioni aggiuntive su arrocco ed en passant.
     * Equivale a {@code getStringaScacchiera(false)}.
     *
     * @return stringa testuale dello stato della scacchiera
     */
    public String getStringaScacchiera() {
        return getStringaScacchiera(false);
    }

    /**
     * Restituisce la rappresentazione testuale della scacchiera corrente senza
     * informazioni aggiuntive.
     *
     * @return stringa testuale dello stato della scacchiera
     * @see #getStringaScacchiera()
     */
    @Override
    public String toString() {
        return getStringaScacchiera();
    }
}