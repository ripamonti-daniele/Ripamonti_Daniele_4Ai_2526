import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Pulsante grafico personalizzato per il pannello delle opzioni di gioco.
 * <p>
 * Estende {@link JButton} con un'icona scalabile caricata da file, effetti
 * visivi di hover e pressione (l'icona si ingrandisce al passaggio del mouse
 * e si rimpicciolisce alla pressione), e la possibilità di essere abilitato
 * o disabilitato mantenendo comunque la propria rappresentazione grafica.
 * </p>
 * <p>
 * Il tipo del pulsante, espresso tramite {@link TipoImmagine}, determina
 * l'icona visualizzata e il percorso del file immagine associato.
 * </p>
 */
public class BottoneOpzioni extends JButton {

    /**
     * Enumerato che definisce i tipi di icona disponibili per {@link BottoneOpzioni}.
     * <p>
     * Ogni costante associa un significato semantico al percorso relativo
     * del file immagine corrispondente.
     * </p>
     */
    public enum TipoImmagine {
        /** Freccia verso l'inizio: riporta alla prima mossa della partita. */
        FRECCIASTART("img/opzioni/frecciaStart.png"),

        /** Freccia sinistra: torna alla mossa precedente. */
        FRECCIASX("img/opzioni/frecciaSx.png"),

        /** Freccia destra: avanza alla mossa successiva. */
        FRECCIADX("img/opzioni/frecciaDx.png"),

        /** Freccia verso la fine: porta all'ultima mossa della partita. */
        FRECCIAEND("img/opzioni/frecciaEnd.png"),

        /** Frecce di rotazione: ruota la scacchiera di 180°. */
        FRECCEROTAZIONE("img/opzioni/frecceRotazione.png"),

        /** Bandiera: segnala la resa del giocatore. */
        BANDIERA("img/opzioni/bandiera.png"),

        /** Icona audio attivo: il suono è abilitato. */
        SOUNDON("img/opzioni/sound_on.png"),

        /** Icona audio disattivato: il suono è disabilitato. */
        SOUNDOFF("img/opzioni/sound_off.png");

        /** Il percorso relativo del file immagine associato a questo tipo. */
        private final String path;

        /**
         * Costruisce una costante {@code TipoImmagine} con il percorso immagine specificato.
         *
         * @param path il percorso relativo del file immagine
         */
        TipoImmagine(String path) {
            this.path = path;
        }
    }

    /**
     * Il tipo di icona correntemente associato al pulsante.
     *
     * @see #setTipo(TipoImmagine)
     * @see TipoImmagine
     */
    private TipoImmagine tipo;

    /**
     * Indica se il pulsante è attualmente abilitato all'interazione.
     * Se {@code false}, gli effetti di hover e il cambio cursore sono soppressi.
     */
    private boolean abilitato;

    /**
     * Indica se il cursore del mouse si trova attualmente sopra il pulsante.
     * Usato per ripristinare correttamente l'icona al rilascio del mouse.
     */
    private boolean hover;

    /**
     * La dimensione di riferimento (in pixel) del pulsante, usata come base
     * per il calcolo delle dimensioni dell'icona nei vari stati (normale,
     * hover, pressione).
     */
    private int dimensione;

    /**
     * Costruisce un nuovo {@code BottoneOpzioni} con posizione, dimensione e tipo specificati.
     * <p>
     * Il pulsante viene inizializzato senza bordo, senza sfondo e senza indicatore
     * di focus, con l'icona centrata. Vengono registrati i listener del mouse per
     * gestire gli effetti visivi di hover e pressione:
     * <ul>
     *   <li>al passaggio del mouse l'icona si ingrandisce alla dimensione piena;</li>
     *   <li>alla pressione l'icona si rimpicciolisce di un quinto;</li>
     *   <li>al rilascio l'icona torna alla dimensione di hover se il cursore
     *       è ancora sopra il pulsante, altrimenti alla dimensione normale.</li>
     * </ul>
     * Gli effetti visivi e il cambio del cursore sono soppressi se il pulsante
     * è disabilitato tramite {@link #disabilita()}.
     * </p>
     *
     * @param tipo       il tipo di icona da visualizzare; non può essere {@code null}
     * @param x          la coordinata X del pulsante nel pannello contenitore
     * @param y          la coordinata Y del pulsante nel pannello contenitore
     * @param dimensione la dimensione (larghezza e altezza) in pixel del pulsante;
     *                   deve essere maggiore di 0
     * @throws IllegalArgumentException se {@code tipo} è {@code null}
     *                                  o se {@code dimensione} è minore o uguale a 0
     */
    public BottoneOpzioni(TipoImmagine tipo, int x, int y, int dimensione) {
        super();
        setDimensione(dimensione);
        setTipo(tipo);
        abilitato = true;
        setBounds(x, y, dimensione, dimensione);
        impostaImmagine(dimensione - dimensione / 10, tipo);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                if (abilitato) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    impostaImmagine(dimensione, getTipo());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                impostaImmagine(dimensione - dimensione / 10, getTipo());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (abilitato && SwingUtilities.isLeftMouseButton(e)) impostaImmagine(dimensione - dimensione / 5, getTipo());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover && abilitato) impostaImmagine(dimensione, getTipo());
                else {
                    impostaImmagine(dimensione - dimensione / 10, getTipo());
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    /**
     * Imposta l'icona del pulsante scalandola alla dimensione indicata,
     * usando il percorso immagine associato al tipo specificato.
     *
     * @param dimensione la dimensione in pixel a cui scalare l'icona
     * @param tipo       il tipo di icona da visualizzare
     */
    private void impostaImmagine(int dimensione, TipoImmagine tipo) {
        setIcon(creaIconaScalata(tipo.path, dimensione));
    }

    /**
     * Aggiorna il tipo del pulsante e ne sostituisce l'icona visualizzata,
     * scalata alla dimensione corrente del pulsante.
     * <p>
     * Utile per cambiare l'aspetto del pulsante a runtime, ad esempio per
     * alternare tra {@link TipoImmagine#SOUNDON} e {@link TipoImmagine#SOUNDOFF}.
     * </p>
     *
     * @param tipo il nuovo tipo di icona da visualizzare; non può essere {@code null}
     * @throws IllegalArgumentException se {@code tipo} è {@code null}
     */
    public void impostaImmagine(TipoImmagine tipo) {
        setTipo(tipo);
        impostaImmagine(dimensione, tipo);
    }

    /**
     * Carica un'immagine dal percorso specificato e la scala alla dimensione indicata.
     *
     * @param percorso   il percorso relativo del file immagine
     * @param dimensione la dimensione in pixel (larghezza e altezza) dell'icona scalata
     * @return un {@link ImageIcon} contenente l'immagine scalata con interpolazione bilineare
     */
    private ImageIcon creaIconaScalata(String percorso, int dimensione) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(IconaPedina.class.getResource("/" + percorso)));
        Image img = icon.getImage().getScaledInstance(dimensione, dimensione, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Abilita il pulsante all'interazione, ripristinando gli effetti visivi
     * di hover e il cambio del cursore.
     */
    public void abilita() {
        abilitato = true;
    }

    /**
     * Disabilita il pulsante, sopprimendo gli effetti visivi di hover
     * e il cambio del cursore al passaggio del mouse.
     * <p>
     * Il pulsante rimane visibile ma non reagisce visivamente all'interazione.
     * </p>
     */
    public void disabilita() {
        abilitato = false;
    }

    /**
     * Indica se il pulsante è attualmente abilitato all'interazione.
     *
     * @return {@code true} se il pulsante è abilitato, {@code false} altrimenti
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * Restituisce la dimensione di riferimento del pulsante in pixel.
     *
     * @return la dimensione corrente del pulsante
     */
    public int getDimensione() {
        return dimensione;
    }

    /**
     * Imposta la dimensione di riferimento del pulsante in pixel.
     * <p>
     * La dimensione viene usata come base per il calcolo delle dimensioni
     * dell'icona nei vari stati (normale, hover, pressione). Non ridimensiona
     * automaticamente il bounds del componente.
     * </p>
     *
     * @param dimensione la nuova dimensione in pixel; deve essere maggiore di 0
     * @throws IllegalArgumentException se {@code dimensione} è minore o uguale a 0
     */
    public void setDimensione(int dimensione) {
        if (dimensione <= 0) throw new IllegalArgumentException("La dimensione deve essere maggiore di 0");
        this.dimensione = dimensione;
    }

    /**
     * Restituisce il tipo di icona correntemente associato al pulsante.
     *
     * @return il tipo corrente come costante {@link TipoImmagine}
     */
    public TipoImmagine getTipo() {
        return tipo;
    }

    /**
     * Imposta il tipo di icona del pulsante senza aggiornarne la visualizzazione.
     * <p>
     * Per aggiornare anche l'icona visualizzata usare {@link #impostaImmagine(TipoImmagine)}.
     * </p>
     *
     * @param tipo il nuovo tipo di icona; non può essere {@code null}
     * @throws IllegalArgumentException se {@code tipo} è {@code null}
     */
    public void setTipo(TipoImmagine tipo) {
        if (tipo == null) throw new IllegalArgumentException("Il tipo di immagine non può essere null");
        this.tipo = tipo;
    }
}