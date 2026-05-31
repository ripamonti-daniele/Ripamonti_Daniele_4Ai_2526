import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Timer grafico per il gioco degli scacchi, implementato come {@link JLabel}
 * con sfondo ad angoli arrotondati.
 * <p>
 * Il timer effettua un conto alla rovescia con granularità di un secondo,
 * aggiornandosi ogni 100ms tramite un {@link Timer} Swing interno.
 * Supporta pausa, ripresa, reset e guadagno di tempo dopo ogni mossa.
 * </p>
 * <p>
 * Il testo visualizzato è gestito esclusivamente dalla classe tramite
 * {@link #displayTimer()}; {@link #setText(String)} è intenzionalmente
 * disabilitato per le chiamate esterne.
 * </p>
 */
public class TimerGrafico extends JLabel {

    /** Timer Swing che scatta ogni 100ms per aggiornare il conto alla rovescia. */
    private final Timer timer;

    /** Millisecondi accumulati dall'ultimo tick intero di un secondo. */
    private long millesimi;

    /** Timestamp in millisecondi dell'ultimo tick del timer. */
    private long ultimoTick;

    /** Ore rimanenti nel conto alla rovescia. */
    private int ore;

    /** Minuti rimanenti nel conto alla rovescia. */
    private int minuti;

    /** Secondi rimanenti nel conto alla rovescia. */
    private int secondi;

    /** Secondi di guadagno da aggiungere dopo ogni mossa. */
    private int guadagno;

    /** Valore predefinito delle ore, usato per il reset. */
    private int oreDefault;

    /** Valore predefinito dei minuti, usato per il reset. */
    private int minutiDefault;

    /** Valore predefinito dei secondi, usato per il reset. */
    private int secondiDefault;

    /** {@code true} se il tempo è scaduto. */
    private boolean tempoScaduto;

    /**
     * {@code true} se il timer è disattivato (tutti i valori di default a zero).
     * In questo stato il timer non parte e viene visualizzato "Off".
     */
    private boolean off;

    /**
     * Flag interno usato per consentire la modifica del testo solo tramite
     * {@link #displayTimer()}, bloccando le chiamate esterne a {@link #setText(String)}.
     */
    private boolean modificaTesto;

    /** Colore di sfondo del componente. */
    private Color sfondo;

    /** Colore del testo visualizzato. */
    private Color textColor;

    /**
     * Costruisce un {@code TimerGrafico} con i valori temporali e i colori specificati.
     * <p>
     * I valori di ore, minuti, secondi e guadagno vengono automaticamente
     * ricondotti ai range validi (ore: 0–23, minuti/secondi: 0–59, guadagno: 0–60).
     * Se tutti i valori temporali sono zero, il timer viene creato in stato disattivato.
     * </p>
     *
     * @param ore       ore iniziali del conto alla rovescia (0–23)
     * @param minuti    minuti iniziali del conto alla rovescia (0–59)
     * @param secondi   secondi iniziali del conto alla rovescia (0–59)
     * @param guadagno  secondi da aggiungere dopo ogni mossa (0–60)
     * @param sfondo    colore di sfondo del componente; non può essere {@code null}
     * @param textColor colore del testo; non può essere {@code null}
     */
    public TimerGrafico(int ore, int minuti, int secondi, int guadagno, Color sfondo, Color textColor) {
        inizializzaTimer(ore, minuti, secondi, guadagno);
        displayTimer();
        tempoScaduto = false;
        modificaTesto = false;
        setSfondo(sfondo);
        setTextColor(textColor);
        setOpaque(false);
        setForeground(textColor);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        timer = new Timer(100, (ActionEvent _) -> {
            millesimi += System.currentTimeMillis() - ultimoTick;
            if (millesimi >= 1000) {
                aggiorna();
                millesimi -= 1000;
            }
            ultimoTick = System.currentTimeMillis();
        });
    }

    /**
     * Inizializza i valori del timer applicando il clamping ai range validi.
     * <p>
     * Imposta anche il flag {@link #off} se tutti i valori temporali sono zero.
     * </p>
     *
     * @param ore      ore iniziali
     * @param minuti   minuti iniziali
     * @param secondi  secondi iniziali
     * @param guadagno secondi di guadagno per mossa
     */
    private void inizializzaTimer(int ore, int minuti, int secondi, int guadagno) {
        oreDefault = ore;
        minutiDefault = minuti;
        secondiDefault = secondi;
        this.guadagno = guadagno;

        if (ore > 23) oreDefault = 23;
        if (minuti > 59) minutiDefault = 59;
        if (secondi > 59) secondiDefault = 59;
        if (guadagno > 60) this.guadagno = 60;

        if (ore < 0) oreDefault = 0;
        if (minuti < 0) minutiDefault = 0;
        if (secondi < 0) secondiDefault = 0;
        if (guadagno < 0) this.guadagno = 0;

        this.ore = oreDefault;
        this.minuti = minutiDefault;
        this.secondi = secondiDefault;

        off = (oreDefault == 0 && minutiDefault == 0 && secondiDefault == 0);
    }

    /**
     * Reimposta il timer con nuovi valori e aggiorna il display.
     * <p>
     * Equivale a un reset seguito da una nuova inizializzazione.
     * </p>
     *
     * @param ore      nuove ore iniziali (0–23)
     * @param minuti   nuovi minuti iniziali (0–59)
     * @param secondi  nuovi secondi iniziali (0–59)
     * @param guadagno nuovi secondi di guadagno per mossa (0–60)
     */
    public void setTimer(int ore, int minuti, int secondi, int guadagno) {
        reset();
        inizializzaTimer(ore, minuti, secondi, guadagno);
        displayTimer();
    }

    /**
     * Disattiva il timer impostando tutti i valori a zero e il flag {@link #off} a {@code true}.
     * Il display mostrerà "Off".
     */
    public void disattiva() {
        setTimer(0, 0, 0, 0);
        off = true;
    }

    /**
     * Decrementa il tempo di un secondo e aggiorna il display.
     * <p>
     * Se il tempo raggiunge zero il timer viene fermato e
     * {@link #tempoScaduto} viene impostato a {@code true}.
     * Gestisce il riporto da secondi a minuti e da minuti a ore.
     * </p>
     */
    private void aggiorna() {
        secondi--;
        if (ore == 0 && minuti == 0 && secondi <= 0) {
            secondi = 0;
            timer.stop();
            tempoScaduto = true;
        }

        if (secondi == -1) {
            secondi = 59;
            minuti--;
        }
        if (minuti == -1) {
            minuti = 59;
            ore--;
        }
        displayTimer();
    }

    /**
     * Avvia il conto alla rovescia se il timer non è disattivato e non è già in esecuzione.
     */
    public void start() {
        if (!off && !timer.isRunning()) {
            ultimoTick = System.currentTimeMillis();
            timer.start();
        }
    }

    /**
     * Mette in pausa il timer, con la possibilità di applicare il guadagno di tempo.
     *
     * @param guadagno {@code true} per aggiungere il guadagno di tempo alla pausa
     */
    public void pause(boolean guadagno) {
        if (timer.isRunning() && !off) {
            timer.stop();
            if (guadagno) sommaGuadagno();
        }
    }

    /**
     * Mette in pausa il timer senza applicare il guadagno di tempo.
     * Equivale a {@link #pause(boolean) pause(false)}.
     */
    public void pause() {
        pause(false);
    }

    /**
     * Reimposta il timer ai valori predefiniti e lo mette in pausa.
     * <p>
     * Se il timer non è disattivato, azzera anche {@link #tempoScaduto}
     * e aggiorna il display.
     * </p>
     */
    public void reset() {
        pause();
        millesimi = 0;
        ore = oreDefault;
        minuti = minutiDefault;
        secondi = secondiDefault;
        if (!off) {
            tempoScaduto = false;
            displayTimer();
        }
    }

    /**
     * Alterna lo stato del timer tra in esecuzione e in pausa.
     * <p>
     * Se il timer è in esecuzione lo ferma e applica il guadagno di tempo;
     * se è in pausa lo riavvia. Non produce effetti se il timer è disattivato.
     * </p>
     */
    public void invertiStato() {
        if (!off) {
            if (timer.isRunning()) {
                timer.stop();
                sommaGuadagno();
            }
            else {
                ultimoTick = System.currentTimeMillis();
                timer.start();
            }
        }
    }

    /**
     * Aggiunge il guadagno di tempo al tempo rimanente.
     * <p>
     * Non produce effetti se il tempo è scaduto, il guadagno è zero,
     * o il timer è disattivato. Il tempo massimo consentito è 23:59:59.
     * Gestisce i riporti da secondi a minuti e da minuti a ore.
     * </p>
     */
    public void sommaGuadagno() {
        if (tempoScaduto || guadagno == 0 || off) return;
        if (guadagno == 60) minuti++;
        else {
            secondi += guadagno;
            if (secondi > 59) {
                minuti++;
                secondi -= 60;
            }
        }
        if (minuti > 59) {
            ore++;
            minuti -= 59;
        }
        if (ore > 23) {
            ore = 23;
            minuti = 59;
            secondi = 59;
        }
        displayTimer();
    }

    /**
     * Aggiorna il testo del componente con il tempo rimanente nel formato
     * {@code H:MM:SS} (le ore sono omesse se zero) oppure "Off" se disattivato.
     */
    private void displayTimer() {
        String s = "";
        if (off) s = "Off";
        else {
            if (ore > 0) s = ore + ":";
            if (minuti < 10) s += "0";
            s += minuti + ":";
            if (secondi < 10) s += "0";
            s += String.valueOf(secondi);
        }
        modificaTesto = true;
        setText(s);
        modificaTesto = false;
    }

    /**
     * Restituisce {@code true} se il timer è in pausa o non ancora avviato.
     *
     * @return {@code true} se il timer non è in esecuzione
     */
    public boolean isPaused() {
        return !timer.isRunning();
    }

    /**
     * Restituisce {@code true} se il tempo è scaduto.
     *
     * @return {@code true} se il conto alla rovescia ha raggiunto zero
     */
    public boolean isTempoScaduto() {
        return tempoScaduto;
    }

    /**
     * Restituisce {@code true} se il timer è disattivato.
     *
     * @return {@code true} se tutti i valori temporali di default sono zero
     */
    public boolean isOff() {
        return off;
    }

    /**
     * Restituisce il valore predefinito delle ore, usato per il reset.
     *
     * @return ore predefinite (0–23)
     */
    public int getOreDefault() {
        return oreDefault;
    }

    /**
     * Restituisce il valore predefinito dei minuti, usato per il reset.
     *
     * @return minuti predefiniti (0–59)
     */
    public int getMinutiDefault() {
        return minutiDefault;
    }

    /**
     * Restituisce il valore predefinito dei secondi, usato per il reset.
     *
     * @return secondi predefiniti (0–59)
     */
    public int getSecondiDefault() {
        return secondiDefault;
    }

    /**
     * Restituisce i secondi di guadagno applicati dopo ogni mossa.
     *
     * @return guadagno in secondi (0–60)
     */
    public int getGuadagno() {
        return guadagno;
    }

    /**
     * Restituisce i secondi rimanenti nel conto alla rovescia.
     *
     * @return secondi correnti (0–59)
     */
    public int getSecondi() {
        return secondi;
    }

    /**
     * Restituisce i minuti rimanenti nel conto alla rovescia.
     *
     * @return minuti correnti (0–59)
     */
    public int getMinuti() {
        return minuti;
    }

    /**
     * Restituisce le ore rimanenti nel conto alla rovescia.
     *
     * @return ore correnti (0–23)
     */
    public int getOre() {
        return ore;
    }

    /**
     * Restituisce il colore del testo del componente.
     *
     * @return colore del testo
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Restituisce il colore di sfondo del componente.
     *
     * @return colore di sfondo
     */
    public Color getSfondo() {
        return sfondo;
    }

    /**
     * Imposta il colore del testo del componente.
     * La chiamata viene ignorata se {@code textColor} è {@code null}.
     *
     * @param textColor il nuovo colore del testo
     */
    public void setTextColor(Color textColor) {
        if (textColor != null) this.textColor = textColor;
    }

    /**
     * Imposta il colore di sfondo del componente.
     * La chiamata viene ignorata se {@code sfondo} è {@code null}.
     *
     * @param sfondo il nuovo colore di sfondo
     */
    public void setSfondo(Color sfondo) {
        if (sfondo != null) this.sfondo = sfondo;
    }

    /**
     * Metodo intenzionalmente limitato per proteggere il testo da modifiche esterne.
     * <p>
     * Il testo è aggiornabile solo tramite {@link #displayTimer()}, che imposta
     * il flag {@link #modificaTesto} prima della chiamata. Qualsiasi chiamata
     * esterna a questo metodo non produce effetti.
     * </p>
     *
     * @param s ignorato se la chiamata non proviene da {@link #displayTimer()}
     */
    @Override
    public void setText(String s) {
        if (modificaTesto) super.setText(s);
    }

    /**
     * Disegna lo sfondo del componente con angoli arrotondati.
     *
     * @param g il contesto grafico fornito da Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(sfondo);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2d.dispose();
        super.paintComponent(g);
    }
}