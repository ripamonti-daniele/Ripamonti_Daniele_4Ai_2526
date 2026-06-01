import scacchiera_pedine.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * Gestore grafico dell'applicazione scacchistica, implementato come Singleton.
 * <p>
 * Gestisce il rendering della scacchiera, l'interazione con l'utente, i timer,
 * la navigazione delle mosse, la promozione del pedone e la gestione del bot.
 * Tutti i componenti grafici vengono creati e posizionati alla costruzione;
 * per renderli visibili è necessario chiamare {@link #mettiASchermo(JPanel)}
 * o {@link #mettiASchermo(JFrame)}.
 * </p>
 * <p>
 * L'istanza unica si ottiene tramite uno degli overload di {@code getInstance}.
 * Solo la prima chiamata crea l'istanza; le successive restituiscono sempre
 * quella già creata ignorando i parametri.
 * </p>
 */
public class GestoreGrafico {

    /** Unica istanza della classe (pattern Singleton). */
    private static GestoreGrafico instance = null;

    /** Matrice dei pannelli grafici che rappresentano le caselle della scacchiera. */
    private final Casella[][] casellePanel;

    /** Array delle caselle usate per la scelta della pedina in caso di promozione. */
    private final Casella[] casellePromozione;

    /** Riferimento al modello logico della scacchiera. */
    private final Scacchiera scacchiera;

    /** {@code true} se una partita è attualmente in corso. */
    private boolean partitaInCorso;

    /** Indice della mossa attualmente visualizzata nella navigazione storico mosse. */
    private int mossaMostrata;

    /** {@code true} se la rotazione automatica della scacchiera è abilitata. */
    private boolean rotazioneScacchiera;

    /** {@code true} se è in corso una promozione del pedone in attesa di scelta. */
    private boolean promozione;

    /** Posizione sulla scacchiera del pedone in attesa di promozione. */
    private int[] posPromozione;

    /** Larghezza totale della scacchiera in pixel. */
    public final int lunghezzaScacchiera;

    /** Larghezza di una singola casella in pixel ({@code lunghezzaScacchiera / 8}). */
    public final int lunghezzaCasella;

    /** Dimensione della scacchiera (numero di righe e colonne). */
    public static final int DIMENSIONE = Scacchiera.DIMENSIONE;

    /** Array delle icone delle pedine, indicizzato per tipo e colore. */
    private ImageIcon[] immagini;

    /** Separatore usato nella stringa di rappresentazione della scacchiera. */
    private static final String SEP = Scacchiera.getSEP();

    /** Mappa da indice numerico di colonna alla lettera corrispondente (1→A, ..., 8→H). */
    private static final Map<Integer, String> numeroToLettera = Map.of(1, "A", 2, "B", 3, "C", 4, "D", 5, "E", 6, "F", 7, "G", 8, "H");

    /** Timestamp in millisecondi dell'ultimo clic su una casella, usato per rilevare il doppio clic. */
    private long ultimoClic;

    /** Intervallo massimo in millisecondi entro cui due clic vengono considerati un doppio clic. */
    private static final long SOGLIA_MS = 250;

    /** {@code true} se i componenti grafici sono già stati aggiunti a un container. */
    private boolean aggiuntoASchermo;

    /** Riferimento al bot avversario; {@code null} se la partita è tra due umani. */
    private Bot bot;

    /** Worker Swing usato per calcolare la mossa del bot in background. */
    private SwingWorker<int[][], Void> workerBot;

    /** Pannello laterale contenente i controlli di gioco (timer, bottoni, nomi, ecc.). */
    private final JPanel panelInfo;

    /** Bottone per avviare una partita tra due giocatori umani. */
    private final JButton btnGioca;

    /** Bottone per attivare/disattivare la rotazione automatica della scacchiera. */
    private final JButton btnRotazioneScacchiera;

    /** Etichetta che mostra il risultato finale della partita (vittoria, pareggio, abbandono). */
    private final JLabel labelVittoria;

    /** Area di testo per il nome del giocatore bianco. */
    private final JTextArea nomeBianco;

    /** Area di testo per il nome del giocatore nero. */
    private final JTextArea nomeNero;

    /** Bottone per configurare e avviare una partita contro il bot. */
    private final JButton btnBot;

    /** Bottone per aprire il dialog di configurazione dei timer. */
    private final JButton btnTimer;

    /** Timer grafico del giocatore bianco. */
    private final TimerGrafico timerBianco;

    /** Timer grafico del giocatore nero. */
    private final TimerGrafico timerNero;

    /** Etichetta che mostra il materiale e il vantaggio materiale del bianco. */
    private final JLabel materialeBianco;

    /** Etichetta che mostra il materiale e il vantaggio materiale del nero. */
    private final JLabel materialeNero;

    /**
     * Array dei bottoni opzione nella barra inferiore.
     * Indici: 0=prima mossa, 1=mossa precedente, 2=mossa successiva,
     * 3=ultima mossa, 4=rotazione manuale, 5=abbandona, 6=audio on/off.
     */
    private final BottoneOpzioni[] btnOpzioni;

    /** Colore delle caselle chiare della scacchiera. */
    private Color caselleChiare;

    /** Colore delle caselle scure della scacchiera. */
    private Color caselleScure;

    /** Lista degli ID già assegnati alle caselle, usata per garantirne l'unicità. */
    private final List<String> idUtilizzati;

    /** ID della casella attualmente selezionata dal giocatore, o {@code null} se nessuna. */
    private String casellaSelezionata;

    /** ID della casella di partenza dell'ultima mossa eseguita. */
    private String casellaPosIniziale;

    /** ID della casella di arrivo dell'ultima mossa eseguita. */
    private String casellaPosFinale;

    /** {@code true} se la scacchiera è attualmente orientata dal lato del nero. */
    private boolean scacchieraGirata;

    /**
     * Flag interno che abilita le modifiche grafiche alle caselle.
     * Quando {@code false}, tutti gli override dei metodi Swing in {@link Casella}
     * bloccano le chiamate esterne.
     */
    private boolean gestisciGrafica;

    /** {@code true} se è attualmente visualizzata l'ultima mossa della partita. */
    private boolean ultimaMossa;

    /**
     * ID della casella su cui si trova il pedone catturabile en passant,
     * usato per mostrare il cerchio di cattura sulla casella corretta; {@code null} se non applicabile.
     */
    private String idEnPassant;

    /**
     * Costruisce il gestore grafico con tutti i parametri esplicitamente specificati.
     * <p>
     * Inizializza la scacchiera grafica, i componenti UI, i listener e il pannello
     * laterale. Questo costruttore è privato: per ottenere l'istanza usare
     * {@link #getInstance(Scacchiera, int, ImageIcon[], Color, Color, Color)}.
     * </p>
     *
     * @param scacchiera       il modello logico della scacchiera; non può essere {@code null}
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel; deve essere {@code > 0}
     * @param immagini         array di 12 icone delle pedine (6 bianche + 6 nere); non può essere {@code null}
     * @param sfondo           colore di sfondo del pannello laterale; {@code null} per trasparente
     * @param caselleChiare    colore delle caselle chiare; non può essere {@code null}
     * @param caselleScure     colore delle caselle scure; non può essere {@code null}
     * @throws IllegalArgumentException se {@code scacchiera} è {@code null},
     *                                  {@code lunghezzaScacchiera} è ≤ 0,
     *                                  o i parametri delle immagini non sono validi
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo, Color caselleChiare, Color caselleScure) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        if (lunghezzaScacchiera <= 0) throw new IllegalArgumentException("La lunghezza della scacchiera deve essere maggiore di 0");
        this.lunghezzaScacchiera = lunghezzaScacchiera;
        lunghezzaCasella = lunghezzaScacchiera / 8;
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        casellePromozione = new Casella[4];
        partitaInCorso = false;
        mossaMostrata = 0;
        rotazioneScacchiera = true;
        promozione = false;
        posPromozione = null;
        ultimoClic = 0;
        aggiuntoASchermo = false;
        bot = null;
        workerBot = null;
        idUtilizzati = new ArrayList<>();
        casellaSelezionata = null;
        casellaPosIniziale = null;
        casellaPosFinale = null;
        scacchieraGirata = false;
        gestisciGrafica = false;
        ultimaMossa = true;
        idEnPassant = null;
        setImmagini(immagini);
        inizializzaCaselle(caselleChiare, caselleScure);
        aggiornaScacchiera(scacchiera.getStringaScacchiera());

        panelInfo = new JPanel();
        Font font = new Font("Segoe UI", Font.BOLD, lunghezzaCasella / 4);
        labelVittoria = new JLabelCustom(null, new Color(180,130,20), new Color(140,95,10), font);
        nomeBianco = new JTextAreaCustom("Giocatore 1", 0, lunghezzaScacchiera - lunghezzaCasella / 2,  lunghezzaCasella * 2 + lunghezzaCasella / 2 + lunghezzaCasella / 15, lunghezzaCasella / 2);
        nomeNero = new JTextAreaCustom("Giocatore 2", 0, 0,  lunghezzaCasella * 2 + lunghezzaCasella / 2 + lunghezzaCasella / 15, lunghezzaScacchiera / 16);
        timerBianco = new TimerGrafico(0, 10, 0, 0, Color.white, Color.black);
        timerNero = new TimerGrafico(0, 10, 0, 0, Color.black, Color.white);
        Font fontPiccolo = new Font("Segoe UI", Font.BOLD, lunghezzaScacchiera / 60);
        materialeBianco = new JLabelCustom(null, Color.white, fontPiccolo);
        materialeNero = new JLabelCustom(null, Color.black, fontPiccolo);
        btnTimer = new JButtonCustom("<html><div style='text-align:center;'>Imposta timer</div></html>", 0, lunghezzaCasella * 2 - lunghezzaCasella / 15 - lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(190, 30, 30), new Color(150, 15, 15), new Color(218, 55, 55), new Color(170, 25, 25), new Color(108, 8, 8), Color.white);
        btnGioca = new JButtonCustom("<html><div style='text-align:center;'>Gioca in persona</div></html>", 0, lunghezzaCasella * 3 - lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(30, 100, 210), new Color(15, 60, 160), new Color(50, 130, 240), new Color(25, 90, 190), new Color(10, 40, 120), Color.white);
        btnBot = new JButtonCustom("<html><div style='text-align:center;'>Gioca contro<br>un bot</div></html>", 0, lunghezzaCasella * 4 + lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(20, 110, 45), new Color(10, 75, 28), new Color(35, 140, 65), new Color(15, 95, 38), new Color(5, 50, 15), Color.white);
        btnRotazioneScacchiera = new JButtonCustom("<html><div style='text-align:center;'>Auto rotazione<br>On</div></html>", 0, lunghezzaCasella * 5 + lunghezzaCasella / 15 + lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(60, 60, 70), new Color(35, 35, 42), new Color(80, 80, 95), new Color(55, 55, 68), new Color(20, 20, 26), Color.white);
        btnOpzioni = new BottoneOpzioni[7];
        BottoneOpzioni.TipoImmagine[] tipi = { BottoneOpzioni.TipoImmagine.FRECCIASTART, BottoneOpzioni.TipoImmagine.FRECCIASX, BottoneOpzioni.TipoImmagine.FRECCIADX, BottoneOpzioni.TipoImmagine.FRECCIAEND, BottoneOpzioni.TipoImmagine.FRECCEROTAZIONE, BottoneOpzioni.TipoImmagine.BANDIERA, BottoneOpzioni.TipoImmagine.SOUNDON, BottoneOpzioni.TipoImmagine.SOUNDOFF };
        for (int i = 0; i < btnOpzioni.length; i++) {
            if (i < 4) btnOpzioni[i] = new BottoneOpzioni(tipi[i], lunghezzaScacchiera - lunghezzaCasella / 2 * (4 - i), lunghezzaScacchiera + lunghezzaCasella / 8, lunghezzaCasella / 2);
            else btnOpzioni[i] = new BottoneOpzioni(tipi[i], lunghezzaScacchiera - lunghezzaCasella * 2 - lunghezzaCasella / 2 * (i - 3), lunghezzaScacchiera + lunghezzaCasella / 8, lunghezzaCasella / 2);
        }

        panelInfo.setBounds(lunghezzaScacchiera + lunghezzaCasella * 5 / 4, 0, lunghezzaCasella * 6, lunghezzaScacchiera);
        labelVittoria.setBounds(lunghezzaCasella * 2 + lunghezzaCasella / 15, lunghezzaCasella * 4 - lunghezzaCasella / 4 * 3, lunghezzaCasella * 5 / 2, lunghezzaCasella * 3 / 2);
        timerBianco.setBounds(0, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        timerNero.setBounds(0, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        materialeBianco.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);
        materialeNero.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);

        panelInfo.setLayout(null);
        if (sfondo != null) {
            panelInfo.setOpaque(true);
            panelInfo.setBackground(sfondo);
        }

        labelVittoria.setOpaque(false);
        labelVittoria.setForeground(Color.white);

        timerBianco.setFont(font);
        timerNero.setFont(font);
        setListenerTimer(timerBianco);
        setListenerTimer(timerNero);

        materialeBianco.setForeground(Color.black);
        materialeNero.setForeground(Color.white);
        aggiornaLabelMateriale();

        setListenerSpostamenti();
        setListenerRotazioneManuale();
        setListenerAbbandona();
        setListenerAudio();
        aggiornaBtnSpostamento();
        btnOpzioni[5].disabilita();

        panelInfo.add(btnGioca);
        panelInfo.add(btnRotazioneScacchiera);
        panelInfo.add(nomeBianco);
        panelInfo.add(nomeNero);
        panelInfo.add(btnBot);
        panelInfo.add(labelVittoria);
        panelInfo.add(timerBianco);
        panelInfo.add(timerNero);
        panelInfo.add(materialeBianco);
        panelInfo.add(materialeNero);
        panelInfo.add(btnTimer);

        btnGioca.addActionListener(_ -> {
            SuoniScacchi.inizioPartita();
            if (bot != null) {
                rotazioneScacchiera = true;
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Auto rotazione<br>On</div></html>");
            }
            bot = null;
            if (nomeBianco.getText().startsWith("Bot ")) nomeBianco.setText("Giocatore 1");
            if (nomeNero.getText().startsWith("Bot ")) nomeNero.setText("Giocatore 2");
            gioca();
        });

        btnRotazioneScacchiera.addActionListener(_ -> {
            SuoniScacchi.menu();
            rotazioneScacchiera = !rotazioneScacchiera;
            if (rotazioneScacchiera) {
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Auto rotazione<br>On</div></html>");
                if (scacchiera.getStatoPartita() == StatoPartita.IN_CORSO) ruotaScacchiera(scacchiera.getTurno(), true);
            }
            else {
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Auto rotazione<br>Off</div></html>");
                if (bot != null) {
                    if (bot.getColore().equals(Color.white)) ruotaScacchiera(Color.black, true);
                    else if (scacchiera.getStatoPartita() == StatoPartita.IN_CORSO) ruotaScacchiera(Color.white, true);
                }
            }
        });

        btnTimer.addActionListener(_ -> {
            SuoniScacchi.menu();
            new DialogTimer(timerBianco, timerNero, lunghezzaCasella);
        });

        btnBot.addActionListener(_ -> {
            SuoniScacchi.menu();
            DialogBot d = new DialogBot(lunghezzaCasella, timerBianco, timerNero);
            if (d.isConfermato()) {
                int difficolta = d.getDifficolta();
                Color colore = d.getColoreBot();
                boolean avanzata = d.isRicercaAvanzata();
                bot = new Bot(scacchiera, colore, difficolta, avanzata);
                if (colore.equals(Color.white)) {
                    if (nomeBianco.getText().equals("Giocatore 1") || nomeBianco.getText().startsWith("Bot")) nomeBianco.setText("Bot " + DialogBot.getStringaDifficolta());
                    if (nomeNero.getText().startsWith("Bot")) nomeNero.setText("Giocatore 2");
                }
                else {
                    if (nomeNero.getText().equals("Giocatore 2") || nomeNero.getText().startsWith("Bot")) nomeNero.setText("Bot " + DialogBot.getStringaDifficolta());
                    if (nomeBianco.getText().startsWith("Bot")) nomeBianco.setText("Giocatore 1");
                }
                rotazioneScacchiera = false;
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Auto rotazione<br>Off</div></html>");
                gioca();
            }
        });
    }

    /**
     * Costruttore privato con immagini delle pedine predefinite ricavate da {@link IconaPedina}.
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param sfondo              colore di sfondo del pannello laterale; {@code null} per trasparente
     * @param caselleChiare       colore delle caselle chiare
     * @param caselleScure        colore delle caselle scure
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo, Color caselleChiare, Color caselleScure) {
        this(scacchiera, lunghezzaScacchiera, new ImageIcon[]{
                IconaPedina.PEDONE_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.ALFIERE_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.CAVALLO_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.TORRE_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.REGINA_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.RE_WHITE.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.PEDONE_BLACK.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.ALFIERE_BLACK.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.CAVALLO_BLACK.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.TORRE_BLACK.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.REGINA_BLACK.getImageIcon(lunghezzaScacchiera / 8),
                IconaPedina.RE_BLACK.getImageIcon(lunghezzaScacchiera / 8)
        }, sfondo, caselleChiare, caselleScure);
    }

    /**
     * Costruttore privato con immagini personalizzate e sfondo {@code null}.
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param immagini            array di 12 icone delle pedine
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini) {
        this(scacchiera, lunghezzaScacchiera, immagini, null, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    /**
     * Costruttore privato con colori delle caselle predefiniti (bianco chiaro e grigio scuro)
     * e sfondo {@code null}.
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera) {
        this(scacchiera, lunghezzaScacchiera, null, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    /**
     * Costruttore privato con immagini personalizzate e colori delle caselle predefiniti.
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param immagini            array di 12 icone delle pedine
     * @param sfondo              colore di sfondo del pannello laterale; {@code null} per trasparente
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo) {
        this(scacchiera, lunghezzaScacchiera, immagini, sfondo, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    /**
     * Costruttore privato con immagini predefinite, colori delle caselle predefiniti e sfondo specificato.
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param sfondo              colore di sfondo del pannello laterale; {@code null} per trasparente
     */
    private GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo) {
        this(scacchiera, lunghezzaScacchiera, sfondo, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    /**
     * Restituisce l'istanza unica del gestore grafico, creandola se non esiste ancora.
     * <p>
     * Se l'istanza è già stata creata, i parametri vengono ignorati.
     * </p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param immagini            array di 12 icone delle pedine
     * @param sfondo              colore di sfondo del pannello laterale
     * @param caselleChiare       colore delle caselle chiare
     * @param caselleScure        colore delle caselle scure
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo, Color caselleChiare, Color caselleScure) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera, immagini, sfondo, caselleChiare, caselleScure);
        return instance;
    }

    /**
     * Restituisce l'istanza unica con immagini predefinite.
     * <p>Se l'istanza è già stata creata, i parametri vengono ignorati.</p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param sfondo              colore di sfondo del pannello laterale
     * @param caselleChiare       colore delle caselle chiare
     * @param caselleScure        colore delle caselle scure
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo, Color caselleChiare, Color caselleScure) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera, sfondo, caselleChiare, caselleScure);
        return instance;
    }

    /**
     * Restituisce l'istanza unica con immagini personalizzate e sfondo/colori predefiniti.
     * <p>Se l'istanza è già stata creata, i parametri vengono ignorati.</p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param immagini            array di 12 icone delle pedine
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera, immagini);
        return instance;
    }

    /**
     * Restituisce l'istanza unica con tutti i parametri predefiniti.
     * <p>Se l'istanza è già stata creata, i parametri vengono ignorati.</p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera);
        return instance;
    }

    /**
     * Restituisce l'istanza unica con immagini personalizzate e sfondo specificato.
     * <p>Se l'istanza è già stata creata, i parametri vengono ignorati.</p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param immagini            array di 12 icone delle pedine
     * @param sfondo              colore di sfondo del pannello laterale
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera, immagini, sfondo);
        return instance;
    }

    /**
     * Restituisce l'istanza unica con immagini predefinite e sfondo specificato.
     * <p>Se l'istanza è già stata creata, i parametri vengono ignorati.</p>
     *
     * @param scacchiera          il modello logico della scacchiera
     * @param lunghezzaScacchiera larghezza totale della scacchiera in pixel
     * @param sfondo              colore di sfondo del pannello laterale
     * @return l'istanza unica di {@code GestoreGrafico}
     */
    public static GestoreGrafico getInstance(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo) {
        if (instance == null) instance = new GestoreGrafico(scacchiera, lunghezzaScacchiera, sfondo);
        return instance;
    }

    /**
     * Inizializza le caselle della scacchiera e le caselle di promozione.
     * <p>
     * Valida i colori delle caselle, crea i pannelli {@link Casella}, li posiziona
     * e registra i listener. Le operazioni avvengono con {@link #gestisciGrafica}
     * impostato a {@code true} per sbloccare temporaneamente i metodi Swing.
     * </p>
     *
     * @param caselleChiare colore delle caselle chiare; può essere {@code null} solo
     *                      se anche {@code caselleScure} è {@code null}
     * @param caselleScure  colore delle caselle scure; può essere {@code null} solo
     *                      se anche {@code caselleChiare} è {@code null}
     * @throws IllegalArgumentException se uno solo dei due colori è {@code null}
     * @throws IllegalStateException    se i due colori sono uguali
     */
    private void inizializzaCaselle(Color caselleChiare, Color caselleScure) {
        if (!(caselleChiare == null && caselleScure == null)) {
            if (caselleChiare == null || caselleScure == null) throw new IllegalArgumentException("I colori delle caselle devono essere entrambi assegnati o entrambi null");
            else if (caselleChiare.equals(caselleScure)) throw new IllegalStateException("Il colore di casellePari non può essere lo stesso di caselleDispari");
            else {
                this.caselleChiare = caselleChiare;
                this.caselleScure = caselleScure;
            }
        }

        gestisciGrafica = true;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                boolean pari = ((j + i) % 2 == 0);
                casellePanel[i][j] = new Casella(pari, lunghezzaCasella, numeroToLettera.get(j + 1) + (DIMENSIONE - i));
                casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                setListener(i, j);
            }
        }
        for (int i = 0; i < 4; i++) {
            casellePromozione[i] = new Casella(null, lunghezzaCasella, "PROMOZIONE");
            int offset = lunghezzaCasella + lunghezzaCasella / 2 + i * (lunghezzaCasella + lunghezzaCasella / 3);
            casellePromozione[i].setBounds(lunghezzaScacchiera + lunghezzaCasella / 8, offset, lunghezzaCasella, lunghezzaCasella);
            casellePromozione[i].setOpaque(false);
            setListenerPromozione(i);
        }
        gestisciGrafica = false;
    }

    /**
     * Valida e imposta l'array delle icone delle pedine.
     * <p>
     * Verifica che l'array contenga esattamente 12 elementi non {@code null} e tutti distinti.
     * Le immagini non corrispondenti alla dimensione di una casella vengono riscalate.
     * </p>
     *
     * @param immagini array di 12 {@link ImageIcon} (6 bianche + 6 nere)
     * @throws IllegalArgumentException se {@code immagini} è {@code null}, non contiene
     *                                  esattamente 12 elementi, contiene {@code null},
     *                                  o contiene icone duplicate
     */
    private void setImmagini(ImageIcon[] immagini) {
        if (immagini == null) throw new IllegalArgumentException("Immagini non può essere un parametro null");
        if (immagini.length != 12) throw new IllegalArgumentException("Le immagini devono essere obbligatoriamente 12");
        for (int i = 0; i < immagini.length - 1; i++) {
            if (immagini[i] == null || i == immagini.length - 2 && immagini[i + 1] == null) throw new IllegalArgumentException("Le immagini non possono essere null");
            for (int j = i + 1; j < immagini.length; j++) if (immagini[i].equals(immagini[j])) throw new IllegalArgumentException("Due tipi di pedina diversi non possono avere la stessa immagine");
        }
        for (int i = 0; i < immagini.length; i++) if (immagini[i].getIconWidth() != lunghezzaCasella || immagini[i].getIconHeight() != lunghezzaCasella) immagini[i] = new ImageIcon(immagini[i].getImage().getScaledInstance(lunghezzaCasella, lunghezzaCasella, Image.SCALE_SMOOTH));
        this.immagini = immagini;
    }

    /**
     * Avvia una nuova partita, resettando scacchiera, timer, nomi e stato grafico.
     * <p>
     * Gestisce la differenziazione tra partita umano-umano e umano-bot,
     * incluso il corretto orientamento della scacchiera e la chiamata
     * alla prima mossa del bot se necessario.
     * </p>
     */
    private void gioca() {
        scacchiera.reset();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());

        if (bot != null) {
            if (Color.white.equals(bot.getColore())) ruotaScacchiera(Color.black, true);
            else if (Color.black.equals(bot.getColore())) ruotaScacchiera(Color.white, true);
        }
        else ruotaScacchiera(scacchiera.getTurno(), true);

        casellaPosIniziale = null;
        casellaPosFinale = null;
        disegna();

        labelVittoria.setText(null);
        btnGioca.setEnabled(false);
        partitaInCorso = true;
        mossaMostrata = 0;
        aggiornaBtnSpostamento();

        nomeBianco.setText(nomeBianco.getText().trim());
        nomeNero.setText(nomeNero.getText().trim());
        if (nomeBianco.getText().isEmpty()) {
            if (bot != null && bot.getColore().equals(Color.white)) nomeBianco.setText("Bot " + DialogBot.getStringaDifficolta());
            else nomeBianco.setText("Giocatore 1");
        }
        if (nomeNero.getText().isEmpty()) {
            if (bot != null && bot.getColore().equals(Color.black)) nomeNero.setText("Bot " + DialogBot.getStringaDifficolta());
            else nomeNero.setText("Giocatore 2");
        }
        if (nomeBianco.getText().equals(nomeNero.getText())) {
            char c = 'N';
            int caratteri_max = JTextAreaCustom.getCaratteriMax();
            if (nomeNero.getText().length() == caratteri_max) {
                if (nomeBianco.getText().charAt(caratteri_max - 1) == c) c = 'n';
                nomeNero.setText(nomeNero.getText().substring(0, caratteri_max - 1) + c);
            }
            else nomeNero.setText(nomeNero.getText() + c);
        }

        nomeBianco.setEditable(false);
        nomeNero.setEditable(false);
        btnTimer.setEnabled(false);
        timerBianco.reset();
        timerNero.reset();
        timerBianco.start();
        btnBot.setEnabled(false);
        aggiornaLabelMateriale();
        btnOpzioni[5].abilita();

        if (bot != null && Color.white.equals(bot.getColore())) mossaBot();
    }

    /**
     * Termina la partita in corso, ripristinando l'interfaccia allo stato iniziale.
     * <p>
     * Ferma i timer, riabilita i controlli di configurazione, cancella
     * l'eventuale worker del bot e deseleziona le caselle.
     * </p>
     */
    private void finePartita() {
        casellaSelezionata = null;
        resetMosseValide();
        disegna();
        btnGioca.setEnabled(true);
        nomeBianco.setEditable(true);
        nomeNero.setEditable(true);
        partitaInCorso = false;
        timerBianco.pause();
        timerNero.pause();
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        btnTimer.setEnabled(true);
        btnBot.setEnabled(true);
        btnOpzioni[5].disabilita();
        if (workerBot != null) workerBot.cancel(true);
        if (rotazioneScacchiera) btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Auto rotazione<br>On</div></html>");
    }

    /**
     * Aggiorna le icone delle caselle in base alla stringa di stato della scacchiera.
     *
     * @param s la stringa di rappresentazione della scacchiera, con righe separate da
     *          {@code \n} e celle separate da {@link #SEP}; non può essere {@code null}
     * @throws IllegalArgumentException se {@code s} è {@code null}
     */
    private void aggiornaScacchiera(String s) {
        if (s == null) throw new IllegalArgumentException("La stringa non può essere null");
        String[] righe = s.split("\n");
        for (int i = 0; i < righe.length; i++) {
            String[] pedine = righe[i].split(SEP);
            for (int j = 0; j < pedine.length; j++) {
                Casella c = casellePanel[i][j];
                switch (pedine[j]) {
                    case "PB" -> c.setImg(immagini[0]);
                    case "AB" -> c.setImg(immagini[1]);
                    case "CB" -> c.setImg(immagini[2]);
                    case "TB" -> c.setImg(immagini[3]);
                    case "QB" -> c.setImg(immagini[4]);
                    case "RB" -> c.setImg(immagini[5]);
                    case "PN" -> c.setImg(immagini[6]);
                    case "AN" -> c.setImg(immagini[7]);
                    case "CN" -> c.setImg(immagini[8]);
                    case "TN" -> c.setImg(immagini[9]);
                    case "QN" -> c.setImg(immagini[10]);
                    case "RN" -> c.setImg(immagini[11]);
                    default -> casellePanel[i][j].rimuoviImg();
                }
            }
        }
    }

    /**
     * Registra il listener di clic sulla casella in posizione {@code [y][x]}.
     * <p>
     * Il listener gestisce: il doppio clic per tornare all'ultima mossa durante
     * la navigazione storico, la selezione e deselezione delle pedine, lo spostamento,
     * il rilevamento dell'en passant e l'avvio della promozione.
     * </p>
     *
     * @param y riga della casella (0–{@link #DIMENSIONE}-1)
     * @param x colonna della casella (0–{@link #DIMENSIONE}-1)
     */
    private void setListener(int y, int x) {
        casellePanel[y][x].setListener(() -> {
            long ora = System.currentTimeMillis();
            if (ora - ultimoClic <= SOGLIA_MS && mossaMostrata != scacchiera.getMosse() && !promozione) {
                ultimoClic = ora;
                mossaMostrata = scacchiera.getMosse();
                aggiornaBtnSpostamento();
                aggiornaScacchiera(scacchiera.getStringaScacchiera());
                if (partitaInCorso && scacchiera.getTurno().equals(Color.white) && timerBianco.isPaused()) timerBianco.start();
                else if (partitaInCorso && scacchiera.getTurno().equals(Color.black) && timerNero.isPaused()) timerNero.start();
                SuoniScacchi.spostamento();
                aggiornaLabelMateriale();
                disegna();
                return;
            }
            ultimoClic = ora;

            if (bot != null && bot.getColore().equals(scacchiera.getTurno())) return;

            if (!promozione && partitaInCorso && mossaMostrata == scacchiera.getMosse()) {
                resetMosseValide();
                if (casellePanel[y][x].getId().equals(casellaSelezionata)) {
                    casellaSelezionata = null;
                    scacchiera.deSelezionaPedina();
                    disegna();
                    return;
                }

                int[] pos = new int[]{y, x};
                Pedina p = scacchiera.getPedina(pos);
                String idCasellaSelOld = casellaSelezionata;

                if (p != null && p.getColore().equals(scacchiera.getTurno())) casellaSelezionata = casellePanel[y][x].getId();
                else casellaSelezionata = null;

                if (scacchiera.getCasellaSelezionata() == null || !scacchiera.muoviPedina(pos)) {
                    List<int[]> mosseValide = scacchiera.selezionaPedina(pos);
                    idEnPassant = null;
                    if (mosseValide != null) {
                        if (scacchiera.getPedinaSelezionata() instanceof Pedone) {
                            for (int[] mossa : mosseValide) {
                                if (mossa[1] != scacchiera.getCasellaSelezionata()[1] && scacchiera.getPedina(mossa) == null) {
                                    idEnPassant = casellePanel[mossa[0]][mossa[1]].getId();
                                    break;
                                }
                            }
                        }
                        mostraMosseValide(mosseValide);
                    }
                }
                else if (scacchiera.promozioneInSospeso() != null) {
                    casellaPosFinale = casellePanel[y][x].getId();
                    casellaPosIniziale = idCasellaSelOld;
                    promozione = true;
                    posPromozione = pos;
                    setImgCasellePromozione(scacchiera.getPedina(pos).getColore());
                    aggiornaBtnSpostamento();
                }
                else {
                    casellaPosFinale = casellePanel[y][x].getId();
                    casellaPosIniziale = idCasellaSelOld;
                    suonoMossa(scacchiera.getTurno());
                    aggiornaInfoScacchiera();
                    disegna();
                    mossaBot();
                    return;
                }

                disegna();
            }
        });
    }

    /**
     * Aggiorna timer, contatori di mossa, materiale e stato della scacchiera
     * dopo ogni mossa eseguita.
     * <p>
     * Gestisce tutti gli stati di fine partita tramite {@link #finePartita()} e,
     * nel caso di {@link StatoPartita#PROMOZIONE_IN_SOSPESO}, esegue una promozione
     * automatica a regina chiamandosi ricorsivamente una volta.
     * </p>
     */
    private void aggiornaInfoScacchiera() {
        timerBianco.invertiStato();
        timerNero.invertiStato();
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        mossaMostrata = scacchiera.getMosse();
        aggiornaBtnSpostamento();
        aggiornaLabelMateriale();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());

        switch (scacchiera.getStatoPartita()) {
            case StatoPartita.IN_CORSO -> ruotaScacchiera(scacchiera.getTurno());
            case StatoPartita.VITTORIA_BIANCO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeBianco.getText() + " (bianco)</div></html>");
                finePartita();
            }
            case StatoPartita.VITTORIA_NERO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeNero.getText() + " (nero)</div></html>");
                finePartita();
            }
            case StatoPartita.STALLO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Stallo:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.MATERIALE_INSUFFICIENTE -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Materiale insufficiente:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PAREGGIO_MOSSE_NEUTRE -> {
                labelVittoria.setText("<html><div style='text-align:center;'>75 mosse neutre:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PAREGGIO_RIPETIZIONI -> {
                labelVittoria.setText("<html><div style='text-align:center;'>5 posizioni ripetute:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PROMOZIONE_IN_SOSPESO -> {
                scacchiera.promuoviPedone(scacchiera.promozioneInSospeso(), 1); //la ripetizione di questa riga comporterà il cambio di statoPartita, fermando la ricorsione
                aggiornaInfoScacchiera();
            }
        }
    }

    /**
     * Registra il listener sulla casella di promozione all'indice {@code i}.
     * <p>
     * Alla pressione, completa la promozione con la pedina scelta (indice {@code i+1}),
     * aggiorna la scacchiera e avvia eventualmente la mossa del bot.
     * </p>
     *
     * @param i indice della casella di promozione (0=regina, 1=torre, 2=alfiere, 3=cavallo)
     */
    private void setListenerPromozione(int i) {
        casellePromozione[i].setListener(() -> {
            if (promozione && mossaMostrata == scacchiera.getMosse()) {
                scacchiera.promuoviPedone(posPromozione, i + 1);
                promozione = false;
                posPromozione = null;
                for (Casella c : casellePromozione) c.rimuoviImg();
                suonoMossa(scacchiera.getTurno());
                aggiornaInfoScacchiera();
                disegna();
                mossaBot();
            }
        });
    }

    /**
     * Riproduce il suono appropriato in base all'esito dell'ultima mossa.
     * <p>
     * Non produce effetti se l'audio è disattivato. La priorità dei suoni è:
     * vittoria → fine partita → scacco → cattura → spostamento.
     * </p>
     *
     * @param coloreAvversario il colore del giocatore che ha subito la mossa
     * @throws IllegalArgumentException se {@code coloreAvversario} è {@code null}
     *                                  o diverso da {@link Color#white}/{@link Color#black}
     */
    private void suonoMossa(Color coloreAvversario) {
        if (!SuoniScacchi.isAudioOn()) return;
        if (coloreAvversario == null) throw new IllegalArgumentException("Il colore dell'avversario non può essere null");
        if (!coloreAvversario.equals(Color.white) && !coloreAvversario.equals(Color.black)) throw new IllegalArgumentException("Il colore dell'avversario può essere solo bianco o nero");

        StatoPartita sp = scacchiera.getStatoPartita();
        if (sp == StatoPartita.VITTORIA_BIANCO || sp == StatoPartita.VITTORIA_NERO) SuoniScacchi.vittoria();
        else if (sp == StatoPartita.STALLO || sp == StatoPartita.PAREGGIO_MOSSE_NEUTRE || sp == StatoPartita.PAREGGIO_RIPETIZIONI || sp == StatoPartita.MATERIALE_INSUFFICIENTE) SuoniScacchi.finePartita();
        else if (scacchiera.isScaccoRe(coloreAvversario)) SuoniScacchi.scacco();
        else if (scacchiera.getMaterialeMossa(coloreAvversario, scacchiera.getMosse() - 1) > scacchiera.getMateriale(coloreAvversario)) SuoniScacchi.mangiata();
        else SuoniScacchi.spostamento();
    }

    /**
     * Calcola e applica la mossa del bot tramite un {@link SwingWorker}.
     * <p>
     * Il calcolo avviene in background per non bloccare l'EDT. Se il bot
     * non trova mosse valide o si verifica un errore, la partita viene
     * terminata con un messaggio di interruzione.
     * </p>
     */
    private void mossaBot() {
        if (bot == null) return;

        workerBot = new SwingWorker<>() {
            @Override
            protected int[][] doInBackground() throws Exception {
                Thread.sleep(100);
                return bot.getMossa();
            }

            @Override
            protected void done() {
                if (!partitaInCorso || isCancelled()) return;

                try {
                    int[][] m = get();

                    if (m != null) {
                        scacchiera.selezionaPedina(m[0]);
                        if (!scacchiera.muoviPedina(m[1])) {
                            labelVittoria.setText("<html><div style='text-align:center;'>Partita interrotta:<br>Il bot non ha trovato mosse</div></html>");
                            SuoniScacchi.finePartita();
                            finePartita();
                            return;
                        }
                        casellaPosIniziale = numeroToLettera.get(m[0][1] + 1) + (DIMENSIONE - m[0][0]);
                        casellaPosFinale = numeroToLettera.get(m[1][1] + 1) + (DIMENSIONE - m[1][0]);
                    }
                    else {
                        labelVittoria.setText("<html><div style='text-align:center;'>Partita interrotta:<br>Il bot non ha trovato mosse</div></html>");
                        SuoniScacchi.finePartita();
                        finePartita();
                        return;
                    }
                }
                catch (Exception e) {
                    labelVittoria.setText("<html><div style='text-align:center;'>Partita interrotta:<br>Il bot non ha trovato mosse</div></html>");
                    SuoniScacchi.finePartita();
                    finePartita();
                    return;
                }

                suonoMossa(bot.getColoreAvversario());
                aggiornaInfoScacchiera();
                disegna();
            }
        };

        workerBot.execute();
    }

    /**
     * Registra un {@code PropertyChangeListener} sul testo del timer per gestire
     * l'avviso sonoro di tempo in esaurimento, il lampeggio del colore e la
     * fine partita per scadenza del tempo.
     *
     * @param t il timer su cui registrare il listener; non può essere {@code null}
     * @throws IllegalArgumentException se {@code t} è {@code null}
     */
    private void setListenerTimer(TimerGrafico t) {
        if (t == null) throw new IllegalArgumentException("Il timer non può essere null");
        t.addPropertyChangeListener("text", _ -> {
            if (!t.isOff() && partitaInCorso && t.getOre() == 0 && t.getMinuti() == 0 && !t.isPaused() && (t.getMinutiDefault() > 0 || t.getSecondi() < 10)) {
                if (SuoniScacchi.isAudioOn() && (t.getSecondi() == 59 || t.getSecondi() == 9 && t.getMinutiDefault() == 0)) SuoniScacchi.tempo();
                if (t.getSecondi() % 2 != 0) t.setForeground(Color.red);
                else t.setForeground(t.getTextColor());
            }

            if (t.isTempoScaduto()) {
                if (t == timerBianco && scacchiera.materialeInsufficiente(Color.black)) labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto, pareggio:<br>Materiale nero insufficiente</div></html>");
                else if (t == timerNero && scacchiera.materialeInsufficiente(Color.white)) labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto, pareggio:<br>Materiale bianco insufficiente</div></html>");
                else {
                    String testo = nomeBianco.getText() + " (bianco)";
                    if (t == timerBianco) testo = nomeNero.getText() + " (nero)";
                    labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto:<br>Vince " + testo + "</div></html>");
                }
                SuoniScacchi.finePartita();
                finePartita();
            }
        });
    }

    /**
     * Registra i listener sui quattro bottoni di navigazione dello storico mosse
     * (prima mossa, precedente, successiva, ultima mossa).
     */
    private void setListenerSpostamenti() {
        for (int i = 0; i < 4; i++) {
            int ind = i;
            btnOpzioni[i].addActionListener(_ -> {
                if (btnOpzioni[ind].isAbilitato()) {
                    SuoniScacchi.spostamento();
                    switch (ind) {
                        case 0 -> mossaMostrata = 0;
                        case 1 -> {
                            if (mossaMostrata > 0) mossaMostrata--;
                        }
                        case 2 -> {
                            if (mossaMostrata < scacchiera.getMosse()) mossaMostrata++;
                        }
                        case 3 -> mossaMostrata = scacchiera.getMosse();
                        default -> {}
                    }
                    aggiornaScacchiera(PartitaFileManager.leggiScacchiera(mossaMostrata));
                    aggiornaLabelMateriale();
                }
                aggiornaBtnSpostamento();
            });
        }
    }

    /**
     * Registra il listener sul bottone di rotazione manuale della scacchiera.
     * <p>
     * Rileva l'orientamento corrente dalla posizione X della casella {@code [0][0]}
     * e ruota verso il lato opposto.
     * </p>
     */
    private void setListenerRotazioneManuale() {
        btnOpzioni[4].addActionListener(_ -> {
            SuoniScacchi.ruota();
            Color c = Color.black;
            if (casellePanel[0][0].getX() > lunghezzaScacchiera / 2) c = Color.white;
            ruotaScacchiera(c, true);
        });
    }

    /**
     * Registra il listener sul bottone di abbandono della partita.
     * <p>
     * Apre un dialogo di conferma; se confermato, termina la partita
     * mostrando il nome del giocatore che ha abbandonato.
     * </p>
     */
    private void setListenerAbbandona() {
        btnOpzioni[5].addActionListener(_ -> {
            if (!btnOpzioni[5].isAbilitato()) return;
            SuoniScacchi.menu();
            DialogAbbandona dilog = new DialogAbbandona();
            if (!dilog.isConfermato()) return;
            String giocatore = nomeBianco.getText() + " (bianco)";
            if (bot == null && scacchiera.getTurno().equals(Color.black) || bot != null && bot.getColore().equals(Color.white)) giocatore = nomeNero.getText() + " (nero)";
            labelVittoria.setText("<html><div style='text-align:center;'>" + giocatore + "<br>ha abbandonato</div></html>");
            finePartita();
        });
    }

    /**
     * Registra il listener sul bottone di attivazione/disattivazione dell'audio.
     * <p>
     * Alterna l'icona tra {@link BottoneOpzioni.TipoImmagine#SOUNDON} e
     * {@link BottoneOpzioni.TipoImmagine#SOUNDOFF} e aggiorna {@link SuoniScacchi}.
     * </p>
     */
    private void setListenerAudio() {
        btnOpzioni[6].addActionListener(_ -> {
            BottoneOpzioni.TipoImmagine tipo = btnOpzioni[6].getTipo();
            if (tipo == BottoneOpzioni.TipoImmagine.SOUNDON) tipo = BottoneOpzioni.TipoImmagine.SOUNDOFF;
            else if (tipo == BottoneOpzioni.TipoImmagine.SOUNDOFF) tipo = BottoneOpzioni.TipoImmagine.SOUNDON;
            btnOpzioni[6].impostaImmagine(tipo);
            SuoniScacchi.setAudio(!SuoniScacchi.isAudioOn());
            SuoniScacchi.menu();
        });
    }

    /**
     * Aggiorna l'abilitazione dei quattro bottoni di navigazione storico mosse
     * in base alla mossa correntemente visualizzata e allo stato di promozione.
     * <p>
     * Durante una promozione in attesa tutti i bottoni vengono disabilitati.
     * </p>
     */
    private void aggiornaBtnSpostamento() {
        if (promozione) {
            for (int n = 0; n < 4; n++) btnOpzioni[n].disabilita();
            return;
        }
        for (int n = 0; n < 4; n++) btnOpzioni[n].abilita();
        if (mossaMostrata != scacchiera.getMosse()) ultimaMossa = false;
        else {
            btnOpzioni[2].disabilita();
            btnOpzioni[3].disabilita();
            ultimaMossa = true;
        }
        if (mossaMostrata == 0) {
            btnOpzioni[0].disabilita();
            btnOpzioni[1].disabilita();
        }
        disegna();
    }

    /**
     * Imposta le icone delle quattro caselle di promozione in base al colore del pedone.
     *
     * @param c il colore del pedone da promuovere ({@link Color#white} o {@link Color#black})
     * @throws IllegalArgumentException se {@code c} è {@code null} o diverso da bianco/nero
     */
    private void setImgCasellePromozione(Color c) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere null");
        if (!c.equals(Color.white) && !c.equals(Color.black)) throw new IllegalArgumentException("Il colore può essere solo bianco o nero");

        if (c.equals(Color.white)) {
            casellePromozione[0].setImg(IconaPedina.REGINA_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[1].setImg(IconaPedina.TORRE_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[2].setImg(IconaPedina.ALFIERE_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[3].setImg(IconaPedina.CAVALLO_WHITE.getImageIcon(lunghezzaCasella));
        }
        else {
            casellePromozione[0].setImg(IconaPedina.REGINA_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[1].setImg(IconaPedina.TORRE_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[2].setImg(IconaPedina.ALFIERE_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[3].setImg(IconaPedina.CAVALLO_BLACK.getImageIcon(lunghezzaCasella));
        }
    }

    /**
     * Ruota la scacchiera orientandola dal lato del colore specificato.
     *
     * @param c                    il colore dal cui lato orientare la scacchiera
     * @param rotazioneObbligatoria {@code true} per forzare la rotazione ignorando
     *                             {@link #rotazioneScacchiera}; {@code false} per
     *                             eseguirla solo se la rotazione automatica è attiva
     * @throws IllegalArgumentException se {@code c} è {@code null} o diverso da bianco/nero
     */
    private void ruotaScacchiera(Color c, boolean rotazioneObbligatoria) {
        if (!(rotazioneObbligatoria || rotazioneScacchiera)) return;
        if (c == null) throw new IllegalArgumentException("Il colore non può essere null");
        if (!c.equals(Color.white) && !c.equals(Color.black)) throw new IllegalArgumentException("Il colore può essere solo bianco o nero");

        gestisciGrafica = true;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (c.equals(Color.white)) {
                    casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                    scacchieraGirata = false;
                }
                else {
                    casellePanel[i][j].setBounds(lunghezzaCasella * (7 - j), lunghezzaCasella * (7 - i), lunghezzaCasella, lunghezzaCasella);
                    scacchieraGirata = true;
                }
            }
        }
        gestisciGrafica = false;
    }

    /**
     * Ruota la scacchiera rispettando il flag {@link #rotazioneScacchiera}.
     * Equivale a {@link #ruotaScacchiera(Color, boolean) ruotaScacchiera(c, false)}.
     *
     * @param c il colore dal cui lato orientare la scacchiera
     */
    private void ruotaScacchiera(Color c) {
        ruotaScacchiera(c, false);
    }

    /**
     * Aggiorna le etichette del materiale per entrambi i giocatori
     * alla mossa attualmente visualizzata.
     */
    private void aggiornaLabelMateriale() {
        int matBianco = scacchiera.getMaterialeMossa(Color.white, mossaMostrata);
        int matNero = scacchiera.getMaterialeMossa(Color.black, mossaMostrata);
        String diffBianco = "";
        String diffNero = "";
        if (matBianco > matNero) diffBianco = "+";
        else if (matBianco < matNero) diffNero = "+";
        diffBianco += String.valueOf(matBianco - matNero);
        diffNero += String.valueOf(matNero - matBianco);
        materialeBianco.setText("<html>Materiale: " + matBianco + "<br>Differenza: " + diffBianco + "</html>");
        materialeNero.setText("<html>Materiale: " + matNero + "<br>Differenza: " + diffNero + "</html>");
    }

    /**
     * Aggiunge tutti i componenti grafici al container specificato.
     * <p>
     * Può essere chiamato al massimo una volta; una seconda chiamata lancia
     * {@link IllegalStateException}.
     * </p>
     *
     * @param container il container Swing a cui aggiungere i componenti; non può essere {@code null}
     * @throws IllegalArgumentException se {@code container} è {@code null}
     * @throws IllegalStateException    se i componenti sono già stati aggiunti a un container
     */
    public void mettiASchermo(Container container) {
        if (container == null) throw new IllegalArgumentException("Il container non può essere null");
        if (aggiuntoASchermo) throw new IllegalStateException("Impossibile mettere a schermo più di una volta");

        for (int i = 0; i < 4; i++) container.add(casellePromozione[i]);
        for (int i = 0; i < DIMENSIONE; i++) for (int j = 0; j < DIMENSIONE; j++) container.add(casellePanel[i][j]);
        container.add(panelInfo);
        for (BottoneOpzioni b : btnOpzioni) container.add(b);
        aggiuntoASchermo = true;
    }

    /**
     * Aggiunge tutti i componenti grafici al {@link JPanel} specificato.
     * Delega a {@link #mettiASchermo(Container)}.
     *
     * @param panel il pannello a cui aggiungere i componenti
     */
    public void mettiASchermo(JPanel panel) {
        mettiASchermo((Container) panel);
    }

    /**
     * Aggiunge tutti i componenti grafici al {@link JFrame} specificato.
     * Delega a {@link #mettiASchermo(Container)}.
     *
     * @param frame il frame a cui aggiungere i componenti
     */
    public void mettiASchermo(JFrame frame) {
        mettiASchermo((Container) frame);
    }

    /**
     * Azzera il flag {@link Casella#mossaValida} su tutte le caselle della scacchiera.
     */
    private void resetMosseValide() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.mossaValida = false;
        }
    }

    /**
     * Imposta il flag {@link Casella#mossaValida} sulle caselle corrispondenti
     * alle mosse valide della pedina selezionata.
     *
     * @param mosseValide lista di posizioni {@code [riga, colonna]} raggiungibili;
     *                    non può essere {@code null} né contenere elementi {@code null}
     * @throws IllegalArgumentException se {@code mosseValide} è {@code null}
     *                                  o contiene elementi {@code null}
     */
    private void mostraMosseValide(List<int[]> mosseValide) {
        if (mosseValide == null) throw new IllegalArgumentException("Le mosse valide non possono essere null");
        for (int[] m : mosseValide) {
            if (m == null) throw new IllegalArgumentException("MosseValide non può contenere elementi null");
            casellePanel[m[0]][m[1]].mossaValida = true;
        }
    }

    /**
     * Richiama {@link Casella#repaint()} su tutte le caselle della scacchiera
     * e sulle caselle di promozione.
     */
    private void disegna() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.repaint();
        }
        for (Casella c : casellePromozione) c.repaint();
    }

    /**
     * Pannello grafico che rappresenta una singola casella della scacchiera
     * o una delle caselle di scelta per la promozione.
     * <p>
     * La maggior parte dei metodi Swing è bloccata tramite override: le modifiche
     * sono consentite solo quando {@link GestoreGrafico#gestisciGrafica} è {@code true},
     * per impedire alterazioni accidentali dall'esterno.
     * </p>
     */
    private class Casella extends JPanel {

        /** Colore base della casella. */
        private Color colore;

        /** Variante più satura del colore base, usata per evidenziare l'ultima mossa. */
        private Color variante;

        /** Label interna che contiene l'icona della pedina. */
        private final JLabel label;

        /** Dimensione del lato della casella in pixel. */
        private int lunghezzaLato;

        /**
         * Identificatore univoco della casella nel formato {@code [A-H][1-8]}
         * (es. {@code "E4"}), oppure {@code "PROMOZIONE"} per le caselle di promozione.
         */
        private String id;

        /** Listener invocato al clic del mouse sulla casella. */
        private CasellaListener listener;

        /**
         * {@code true} se questa casella è raggiungibile dalla pedina selezionata
         * e deve mostrare l'indicatore di mossa valida.
         */
        private boolean mossaValida;

        /**
         * Costruisce una casella con colore, dimensione e ID specificati.
         *
         * @param pari          {@code true} per casella chiara, {@code false} per scura,
         *                      {@code null} per casella di promozione (sfondo trasparente)
         * @param lunghezzaLato dimensione del lato in pixel; deve essere {@code > 0}
         * @param id            identificatore della casella ({@code [A-H][1-8]} o {@code "PROMOZIONE"})
         * @throws IllegalArgumentException se {@code lunghezzaLato} è ≤ 0 o {@code id} non è valido
         */
        private Casella(Boolean pari, int lunghezzaLato, String id) {
            setLunghezzaLato(lunghezzaLato);
            label = new JLabel();
            label.setPreferredSize(new Dimension(lunghezzaLato, lunghezzaLato));
            setId(id);
            if (pari == null || this.id.equals("PROMOZIONE")) setColore(new Color(0, 0, 0, 0));
            else if (pari) setColore(caselleChiare);
            else setColore(caselleScure);
            this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
            this.add(label);
            mossaValida = false;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (listener != null && SwingUtilities.isLeftMouseButton(e)) listener.casellaCliccata();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (id.equals("PROMOZIONE") && promozione) setCursor(new Cursor(Cursor.HAND_CURSOR));
                    else if (id.equals("PROMOZIONE")) setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        /**
         * Restituisce la dimensione del lato della casella.
         *
         * @return la lunghezza del lato in pixel
         */
        private int getLunghezzaLato() {
            return lunghezzaLato;
        }

        /**
         * Imposta la dimensione del lato della casella.
         *
         * @param lunghezzaLato la nuova lunghezza del lato in pixel; deve essere {@code > 0}
         * @throws IllegalArgumentException se {@code lunghezzaLato} è ≤ 0
         */
        private void setLunghezzaLato(int lunghezzaLato) {
            if (lunghezzaLato <= 0) throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
            this.lunghezzaLato = lunghezzaLato;
        }

        /**
         * Restituisce il colore base della casella.
         *
         * @return il colore base
         */
        private Color getColore() {
            return colore;
        }

        /**
         * Imposta il colore base della casella e calcola la variante per l'evidenziazione.
         *
         * @param colore il nuovo colore base; non può essere {@code null}
         * @throws IllegalArgumentException se {@code colore} è {@code null}
         */
        private void setColore(Color colore) {
            if (colore == null) throw new IllegalArgumentException("Il colore non può essere null");
            this.colore = colore;
            super.setBackground(colore);
            variante = varianteColore(colore);
        }

        /**
         * Restituisce l'identificatore della casella.
         *
         * @return l'ID nel formato {@code [A-H][1-8]} o {@code "PROMOZIONE"}
         */
        private String getId() {
            return id;
        }

        /**
         * Imposta l'identificatore della casella, validandone il formato.
         * <p>
         * Per le caselle normali l'ID deve essere nel formato {@code [A-H][1-8]}
         * e non può essere già in uso. Per le caselle di promozione l'ID è {@code "PROMOZIONE"}
         * (ammesso più volte).
         * </p>
         *
         * @param id il nuovo identificatore
         * @throws IllegalArgumentException se {@code id} è {@code null}, non rispetta
         *                                  il formato, o è già stato assegnato a un'altra casella
         */
        private void setId(String id) {
            if (id == null) throw new IllegalArgumentException("L'id non può essere null");
            id = id.trim().toUpperCase();
            if ("PROMOZIONE".equals(id)) this.id = id;
            else {
                if (idUtilizzati.contains(id)) throw new IllegalArgumentException("Id " + id + " già in uso");
                if (!id.matches("[A-H][1-8]")) throw new IllegalArgumentException("Formato id non valido (esempio corretto: A1)");
                this.id = id;
                idUtilizzati.add(this.id);
            }
        }

        /**
         * Restituisce l'icona attualmente visualizzata nella casella.
         *
         * @return l'icona corrente, o {@code null} se la casella è vuota
         */
        private Icon getImg() {
            return label.getIcon();
        }

        /**
         * Imposta l'icona della pedina nella casella, riscalandola se necessario.
         * Se {@code img} è {@code null}, rimuove l'icona.
         *
         * @param img l'icona da visualizzare, o {@code null} per svuotare la casella
         */
        private void setImg(ImageIcon img) {
            if (img == null) rimuoviImg();
            else if (img.getIconWidth() != lunghezzaLato || img.getIconHeight() != lunghezzaLato) {
                Image scaled = img.getImage().getScaledInstance(lunghezzaLato, lunghezzaLato, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
            else label.setIcon(img);
        }

        /**
         * Rimuove l'icona dalla casella, lasciandola visivamente vuota.
         */
        private void rimuoviImg() {
            label.setIcon(null);
        }

        /**
         * Imposta il listener da invocare al clic del mouse sulla casella.
         *
         * @param l il listener da registrare; {@code null} per rimuoverlo
         */
        private void setListener(CasellaListener l) {
            this.listener = l;
        }

        /**
         * Calcola una variante più satura del colore specificato, usata per
         * evidenziare la casella di partenza e di arrivo dell'ultima mossa.
         *
         * @param c il colore base; non può essere {@code null}
         * @return un colore con saturazione aumentata di 0.4 (massimo 1.0)
         * @throws IllegalArgumentException se {@code c} è {@code null}
         */
        private Color varianteColore(Color c) {
            if (c == null) throw new IllegalArgumentException("Il colore non può essere null");
            float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
            hsb[1] = Math.min(1f, hsb[1] + 0.4f);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

        /**
         * Disegna la casella con sfondo, evidenziazioni e indicatori grafici.
         * <p>
         * Gestisce i seguenti stati visivi:
         * <ul>
         *   <li>Casella di partenza/arrivo dell'ultima mossa: sfondo con {@link #variante}.</li>
         *   <li>Casella selezionata: bordo nero.</li>
         *   <li>Casella di promozione attiva: sfondo dorato con bordo nero.</li>
         *   <li>Mossa valida con pedina o en passant: cerchio vuoto semitrasparente.</li>
         *   <li>Mossa valida senza pedina: cerchio pieno semitrasparente.</li>
         * </ul>
         * Se non è visualizzata l'ultima mossa ({@link GestoreGrafico#ultimaMossa} è {@code false}),
         * vengono omessi tutti gli indicatori sopra elencati.
         * </p>
         *
         * @param g il contesto grafico fornito da Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            disegnaCoordinata(g2d);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!getBackground().equals(colore)) super.setBackground(colore);
            if (!ultimaMossa) {
                g2d.dispose();
                return;
            }

            if (this.id.equals(casellaPosIniziale) || this.id.equals(casellaPosFinale)) super.setBackground(variante);
            g2d.setStroke(new BasicStroke(((float) lunghezzaLato / 20)));
            g2d.setColor(Color.black);

            if (promozione && this.id.equals("PROMOZIONE")) {
                g2d.setColor(new Color(230, 200, 0));
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setColor(Color.black);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            else if (this.id.equals(casellaSelezionata)) g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            else if (mossaValida) {
                Composite old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                int offset;
                if (this.label.getIcon() != null || this.id.equals(idEnPassant)) {
                    offset = lunghezzaLato / 25;
                    g2d.drawOval(offset, offset, lunghezzaLato - 2 * offset - 1, lunghezzaLato - 2 * offset - 1);
                }
                else {
                    offset = lunghezzaLato / 4;
                    g2d.fillOval(offset, offset, offset * 2, offset * 2);
                }
                g2d.setComposite(old);
            }
            g2d.dispose();
        }

        /**
         * Disegna la coordinata alfanumerica nell'angolo in alto a sinistra della casella.
         * <p>
         * La coordinata viene disegnata solo sulle caselle del bordo visibile
         * (colonna A o riga 1 se non girata; colonna H o riga 8 se girata).
         * Il colore del testo è il contrario del colore della casella per massimizzare
         * il contrasto. La casella {@code "PROMOZIONE"} non mostra coordinate.
         * </p>
         *
         * @param g2d il contesto grafico 2D su cui disegnare
         */
        private void disegnaCoordinata(Graphics2D g2d) {
            if (id.equals("PROMOZIONE")) return;
            if (!scacchieraGirata && (id.charAt(0) == 'A' || id.charAt(1) == '1') || scacchieraGirata && (id.charAt(0) == 'H' || id.charAt(1) == '8')) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = new Font("Arial", Font.BOLD, lunghezzaLato / 5);
                g2d.setFont(font);
                if (colore.equals(caselleScure)) g2d.setColor(caselleChiare);
                else g2d.setColor(caselleScure);

                if (!scacchieraGirata) {
                    if (id.equals("A1")) g2d.drawString(id, lunghezzaLato / 15, lunghezzaLato / 5);
                    else if (id.charAt(0) == 'A') g2d.drawString(String.valueOf(id.charAt(1)), lunghezzaLato / 15, lunghezzaLato / 5);
                    else g2d.drawString(String.valueOf(id.charAt(0)), lunghezzaLato / 15, lunghezzaLato / 5);
                }
                else {
                    if (id.equals("H8")) g2d.drawString(id, lunghezzaLato / 15, lunghezzaLato / 5);
                    else if (id.charAt(0) == 'H') g2d.drawString(String.valueOf(id.charAt(1)), lunghezzaLato / 15, lunghezzaLato / 5);
                    else g2d.drawString(String.valueOf(id.charAt(0)), lunghezzaLato / 15, lunghezzaLato / 5);
                }
            }
        }

        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setBounds(Rectangle r) { if (gestisciGrafica) super.setBounds(r); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setBounds(int x, int y, int width, int height) { if (gestisciGrafica) super.setBounds(x, y, width, height); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setLayout(LayoutManager mgr) { if (gestisciGrafica) super.setLayout(mgr); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void removeAll() { if (gestisciGrafica) super.removeAll(); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void remove(int index) { if (gestisciGrafica) super.remove(index); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void remove(Component comp) { if (gestisciGrafica) super.remove(comp); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setSize(Dimension d) { if (gestisciGrafica) super.setSize(d); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setSize(int width, int height) { if (gestisciGrafica) super.setSize(width, height); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public Component add(Component comp) { if (gestisciGrafica) return super.add(comp); return comp; }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public Component add(Component comp, int index) { if (gestisciGrafica) return super.add(comp, index); return comp; }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void add(Component comp, Object constraints) { if (gestisciGrafica) super.add(comp, constraints); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setBackground(Color bg) { if (gestisciGrafica) super.setBackground(bg); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setForeground(Color fg) { if (gestisciGrafica) super.setForeground(fg); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setOpaque(boolean isOpaque) { if (gestisciGrafica) super.setOpaque(isOpaque); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setBorder(Border border) { if (gestisciGrafica) super.setBorder(border); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setVisible(boolean aFlag) { if (gestisciGrafica) super.setVisible(aFlag); }
        /** Bloccato: modificabile solo tramite {@link GestoreGrafico#gestisciGrafica}. */
        @Override public void setEnabled(boolean enabled) { if (gestisciGrafica) super.setEnabled(enabled); }
    }
}