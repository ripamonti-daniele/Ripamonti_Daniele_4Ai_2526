import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pulsante grafico personalizzato per il pannello delle opzioni di gioco.
 * <p>
 * Estende {@link JButton} con un'icona scalabile caricata da file, effetti
 * visivi di hover e pressione (l'icona si ingrandisce al passaggio del mouse
 * e si rimpicciolisce alla pressione), e la possibilità di essere abilitato
 * o disabilitato mantenendo comunque la propria rappresentazione grafica.
 * </p>
 * <p>
 * Il tipo del pulsante determina l'icona visualizzata secondo la seguente
 * mappatura:
 * <ol>
 *   <li>Freccia Start (vai all'inizio)</li>
 *   <li>Freccia Sinistra (mossa precedente)</li>
 *   <li>Freccia Destra (mossa successiva)</li>
 *   <li>Freccia End (vai alla fine)</li>
 *   <li>Frecce Rotazione (ruota la scacchiera)</li>
 *   <li>Bandiera (resa / abbandono)</li>
 *   <li>Audio attivo</li>
 *   <li>Audio disattivato</li>
 * </ol>
 * </p>
 */
public class BottoneOpzioni extends JButton {

    /**
     * Il tipo di icona del pulsante, compreso tra 1 e 8 (inclusi).
     *
     * @see #setTipo(int)
     */
    private int tipo;

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
     * gestire gli effetti visivi di hover e pressione.
     * </p>
     *
     * @param tipo       il tipo di icona da visualizzare; deve essere compreso tra 1 e 8
     * @param x          la coordinata X del pulsante nel pannello contenitore
     * @param y          la coordinata Y del pulsante nel pannello contenitore
     * @param dimensione la dimensione (larghezza e altezza) in pixel del pulsante;
     *                   deve essere maggiore di 0
     * @throws IllegalArgumentException se {@code tipo} non è nel range [1, 8]
     *                                  o se {@code dimensione} è minore o uguale a 0
     */
    public BottoneOpzioni(int tipo, int x, int y, int dimensione) {
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
     * Imposta l'icona del pulsante in base al tipo specificato, scalandola
     * alla dimensione indicata.
     *
     * @param dimensione la dimensione in pixel a cui scalare l'icona
     * @param tipo       il tipo di icona da visualizzare; valori non mappati
     *                   non producono alcun cambiamento
     */
    private void impostaImmagine(int dimensione, int tipo) {
        switch (tipo) {
            case 1 -> setIcon(creaIconaScalata("img/opzioni/frecciaStart.png", dimensione));
            case 2 -> setIcon(creaIconaScalata("img/opzioni/frecciaSx.png", dimensione));
            case 3 -> setIcon(creaIconaScalata("img/opzioni/frecciaDx.png", dimensione));
            case 4 -> setIcon(creaIconaScalata("img/opzioni/frecciaEnd.png", dimensione));
            case 5 -> setIcon(creaIconaScalata("img/opzioni/frecceRotazione.png", dimensione));
            case 6 -> setIcon(creaIconaScalata("img/opzioni/bandiera.png", dimensione));
            case 7 -> setIcon(creaIconaScalata("img/opzioni/sound_on.png", dimensione));
            case 8 -> setIcon(creaIconaScalata("img/opzioni/sound_off.png", dimensione));
            default -> {}
        }
    }

    /**
     * Aggiorna il tipo del pulsante e ne sostituisce l'icona visualizzata,
     * scalata alla dimensione corrente del pulsante.
     * <p>
     * Utile per cambiare l'aspetto del pulsante a runtime, ad esempio per
     * alternare tra l'icona audio attivo (tipo 7) e audio disattivato (tipo 8).
     * </p>
     *
     * @param tipo il nuovo tipo di icona da visualizzare; deve essere compreso tra 1 e 8
     * @throws IllegalArgumentException se {@code tipo} non è nel range [1, 8]
     */
    public void impostaImmagine(int tipo) {
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
        ImageIcon icon = new ImageIcon(percorso);
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
     * Il pulsante rimane visibile ma non reagisce all'interazione dell'utente.
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
     * @return il tipo corrente, compreso tra 1 e 8
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Imposta il tipo di icona del pulsante senza aggiornarne la visualizzazione.
     * <p>
     * Per aggiornare anche l'icona visualizzata usare {@link #impostaImmagine(int)}.
     * </p>
     *
     * @param tipo il nuovo tipo; deve essere compreso tra 1 e 8 (inclusi)
     * @throws IllegalArgumentException se {@code tipo} non è nel range [1, 8]
     */
    public void setTipo(int tipo) {
        if (tipo < 1 || tipo > 8) throw new IllegalArgumentException("Tipo di immagine non valido (min 1 max 8)");
        this.tipo = tipo;
    }
}