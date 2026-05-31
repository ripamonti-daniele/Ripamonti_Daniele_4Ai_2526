import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Area di testo Swing personalizzata con sfondo bianco ad angoli arrotondati
 * e limite massimo di caratteri inseribili.
 * <p>
 * Il numero massimo di caratteri consentiti è definito dalla costante
 * {@link #CARATTERI_MAX} e consultabile tramite {@link #getCaratteriMax()}.
 * Il testo viene troncato automaticamente se supera tale limite.
 * </p>
 * <p>
 * Lo sfondo con bordo arrotondato è gestito tramite
 * {@link #paintComponent(Graphics)}; il componente è impostato come
 * non opaco ({@code setOpaque(false)}) per permettere il rendering personalizzato.
 * </p>
 */
public class JTextAreaCustom extends JTextArea {

    /** Numero massimo di caratteri inseribili nell'area di testo. */
    private static final int CARATTERI_MAX = 15;

    /**
     * Costruisce una {@code JTextAreaCustom} vuota, posizionata e dimensionata
     * secondo le coordinate e le misure fornite.
     * <p>
     * Il margine verticale viene calcolato in modo da centrare il testo
     * rispetto all'altezza del componente. Un {@link DocumentListener} interno
     * tronca automaticamente il testo a {@link #CARATTERI_MAX} caratteri
     * ad ogni inserimento.
     * </p>
     *
     * @param x      coordinata X del componente nel pannello padre
     * @param y      coordinata Y del componente nel pannello padre
     * @param width  larghezza del componente in pixel
     * @param height altezza del componente in pixel; influenza anche
     *               la dimensione del font ({@code height/3})
     */
    public JTextAreaCustom(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
        setFont(new Font("Segoe UI", Font.PLAIN, height / 3));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setCaretColor(Color.BLACK);
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(false);
        int altezzaCarattere = getFontMetrics(getFont()).getHeight() * getLineCount();
        setMargin(new Insets((getHeight() - altezzaCarattere) / 2, getWidth() / 20, (getHeight() + altezzaCarattere) / 2, getWidth() / 20));

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String testo = JTextAreaCustom.this.getText();
                    if (testo.length() > CARATTERI_MAX) {
                        JTextAreaCustom.this.setText(testo.substring(0, 15));
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    /**
     * Costruisce una {@code JTextAreaCustom} con testo iniziale, posizionata
     * e dimensionata secondo le coordinate e le misure fornite.
     * <p>
     * Delega al costruttore principale e imposta il testo tramite {@link #setText(String)}.
     * Se {@code text} supera {@link #CARATTERI_MAX} caratteri, verrà troncato
     * automaticamente dal {@link DocumentListener} interno.
     * </p>
     *
     * @param text   il testo iniziale da visualizzare nell'area di testo
     * @param x      coordinata X del componente nel pannello padre
     * @param y      coordinata Y del componente nel pannello padre
     * @param width  larghezza del componente in pixel
     * @param height altezza del componente in pixel
     */
    public JTextAreaCustom(String text, int x, int y, int width, int height) {
        this(x, y, width, height);
        setText(text);
    }

    /**
     * Restituisce il numero massimo di caratteri inseribili nell'area di testo.
     *
     * @return il valore di {@link #CARATTERI_MAX}
     */
    public static int getCaratteriMax() {
        return CARATTERI_MAX;
    }

    /**
     * Imposta la modalità di editabilità del componente e aggiorna
     * la visibilità del cursore di conseguenza.
     * <p>
     * Quando il componente è editabile il cursore è nero; quando non è
     * editabile il cursore diventa trasparente così da non essere visibile.
     * </p>
     *
     * @param b {@code true} per rendere il componente editabile,
     *          {@code false} per renderlo in sola lettura
     */
    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
        if (b) setCaretColor(Color.black);
        else setCaretColor(new Color(0, 0, 0, 0));
    }

    /**
     * Disegna lo sfondo dell'area di testo con angoli arrotondati e bordo nero.
     * <p>
     * Il raggio degli angoli è calcolato come {@code max(8, height/4)}.
     * Il contesto grafico viene duplicato tramite {@link Graphics#create()}
     * per non alterare lo stato del contesto originale.
     * </p>
     *
     * @param g il contesto grafico fornito da Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int arc = Math.max(8, getHeight() / 4);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        g2d.dispose();
        super.paintComponent(g);
    }
}