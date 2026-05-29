import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 * Etichetta Swing personalizzata con sfondo a gradiente o tinta unita,
 * angoli arrotondati e ridimensionamento automatico del font per testi HTML.
 * <p>
 * Il font assegnato alla costruzione è protetto da modifiche esterne:
 * {@link #setFont(Font)} è intenzionalmente disabilitato; il font viene
 * gestito internamente tramite {@code fontDefault}.
 * </p>
 * <p>
 * Se il testo contiene una parola più larga dell'area disponibile, il font
 * viene ridotto automaticamente fino a un minimo di 6pt tramite {@link #adattaHTML()}.
 * </p>
 */
public class JLabelCustom extends JLabel {

    /** Colore iniziale (in alto) del gradiente di sfondo, o colore unico se {@code backgroundColorEnd} è {@code null}. */
    private final Color backgroundColorStart;

    /** Colore finale (in basso) del gradiente di sfondo; se {@code null} viene usato solo {@code backgroundColorStart}. */
    private final Color backgroundColorEnd;

    /** Font predefinito protetto da modifiche esterne, usato come base per il ridimensionamento automatico. */
    private final Font fontDefault;

    /**
     * Costruisce una {@code JLabelCustom} con testo, sfondo a gradiente e font specificati.
     *
     * @param text                 il testo da visualizzare sull'etichetta
     * @param backgroundColorStart colore iniziale (in alto) del gradiente di sfondo
     * @param backgroundColorEnd   colore finale (in basso) del gradiente di sfondo;
     *                             se {@code null} lo sfondo sarà a tinta unita
     * @param font                 il font da applicare all'etichetta; non può essere {@code null}
     * @throws IllegalArgumentException se {@code font} è {@code null}
     */
    public JLabelCustom(String text, Color backgroundColorStart, Color backgroundColorEnd, Font font) {
        super(text);
        if (font == null) throw new IllegalArgumentException("Il font non può essere null");
        fontDefault = font;
        setFont(font);
        this.backgroundColorStart = backgroundColorStart;
        this.backgroundColorEnd = backgroundColorEnd;
        setOpaque(false);
        setForeground(Color.WHITE);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    /**
     * Costruisce una {@code JLabelCustom} con testo, sfondo a tinta unita e font specificati.
     * <p>
     * Equivale a chiamare {@link #JLabelCustom(String, Color, Color, Font)} con
     * {@code backgroundColorEnd} pari a {@code null}.
     * </p>
     *
     * @param text                 il testo da visualizzare sull'etichetta
     * @param backgroundColorStart colore uniforme di sfondo
     * @param font                 il font da applicare all'etichetta; non può essere {@code null}
     * @throws IllegalArgumentException se {@code font} è {@code null}
     */
    public JLabelCustom(String text, Color backgroundColorStart, Font font) {
        this(text, backgroundColorStart, null, font);
    }

    /**
     * Verifica se almeno una parola del testo HTML supera la larghezza massima consentita.
     * <p>
     * I tag HTML vengono rimossi prima della misurazione, così da operare
     * solo sul testo visibile.
     * </p>
     *
     * @param html     il testo HTML di cui verificare le parole
     * @param label    l'etichetta da cui ricavare le metriche del font corrente
     * @param maxWidth la larghezza massima in pixel consentita per una singola parola
     * @return {@code true} se almeno una parola supera {@code maxWidth}, {@code false} altrimenti
     */
    private boolean parolaTroppoLungaHTML(String html, JLabel label, int maxWidth) {
        String testoPulito = html.replaceAll("<[^>]*>", "");
        FontMetrics fm = label.getFontMetrics(label.getFont());

        for (String parola : testoPulito.split("\\s+")) {
            if (fm.stringWidth(parola) > maxWidth) {
                return true;
            }
        }
        return false;
    }

    /**
     * Riduce progressivamente la dimensione del font finché il testo HTML
     * rientra nella larghezza disponibile del componente.
     * <p>
     * Il metodo non esegue nulla se il testo è vuoto, se la larghezza del
     * componente non è ancora disponibile ({@code ≤ 0}), o se nessuna parola
     * supera la larghezza disponibile. La dimensione minima del font è 6pt.
     * </p>
     */
    private void adattaHTML() {
        String html = getText();
        if (html == null || html.isEmpty()) return;

        int width = getWidth() - getInsets().left - getInsets().right;
        if (width <= 0) return;

        if (!parolaTroppoLungaHTML(html, this, width)) return;

        Font base = fontDefault;
        float size = base.getSize2D();
        Font fontCorrente;

        while (size > 6f) {
            fontCorrente = base.deriveFont(size);
            super.setFont(fontCorrente);

            View view = (View) getClientProperty(BasicHTML.propertyKey);
            if (view != null) {
                int w = (int) view.getPreferredSpan(View.X_AXIS);
                if (w <= width) break;
            }
            size -= 1f;
        }
    }

    /**
     * Metodo intenzionalmente disabilitato per proteggere {@code fontDefault}
     * da modifiche esterne.
     * <p>
     * Il font viene gestito esclusivamente all'interno della classe tramite
     * {@code super.setFont()}. Qualsiasi chiamata esterna a questo metodo
     * non produce alcun effetto.
     * </p>
     *
     * @param font ignorato
     */
    @Override
    public void setFont(Font font) {}

    /**
     * Imposta il testo dell'etichetta, ripristina il font predefinito e
     * avvia l'adattamento automatico del font se necessario.
     * <p>
     * Il controllo su {@code fontDefault != null} gestisce il caso in cui
     * il metodo venga invocato da {@code super(text)} nel costruttore di
     * {@link JLabel}, prima che {@code fontDefault} sia stato inizializzato.
     * </p>
     *
     * @param text il nuovo testo da visualizzare; può contenere HTML
     */
    @Override
    public void setText(String text) {
        super.setText(text);
        if (fontDefault != null) super.setFont(fontDefault);
        adattaHTML();
        repaint();
    }

    /**
     * Disegna lo sfondo dell'etichetta con angoli arrotondati.
     * <p>
     * Se {@code backgroundColorEnd} è {@code null} viene usata una tinta unita
     * con {@code backgroundColorStart}; altrimenti viene applicato un gradiente
     * verticale da {@code backgroundColorStart} a {@code backgroundColorEnd}.
     * Se il testo è vuoto o {@code null} il componente non viene disegnato.
     * </p>
     *
     * @param g il contesto grafico fornito da Swing; viene utilizzato come
     *          {@link Graphics2D} tramite {@link Graphics#create()} per
     *          non alterare il contesto originale
     */
    @Override
    protected void paintComponent(Graphics g) {
        String testo = getText();
        if (testo == null || testo.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundColorEnd == null) {
            g2d.setColor(backgroundColorStart);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }

        else {
            GradientPaint gp = new GradientPaint(0, 0, backgroundColorStart, 0, getHeight(), backgroundColorEnd);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }

        g2d.dispose();
        super.paintComponent(g);
    }
}