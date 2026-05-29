import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pulsante Swing personalizzato con sfondo a gradiente, angoli arrotondati
 * ed effetti visivi per gli stati hover e pressed.
 * <p>
 * Il rendering del sfondo è gestito tramite {@link #paintComponent(Graphics)},
 * che applica un gradiente verticale differente a seconda dello stato corrente
 * del pulsante (normale, hover, premuto, disabilitato).
 * </p>
 */
public class JButtonCustom extends JButton {

    /** Colore iniziale (in alto) del gradiente nello stato normale. */
    private final Color colorStart;

    /** Colore finale (in basso) del gradiente nello stato normale. */
    private final Color colorEnd;

    /** Colore iniziale (in alto) del gradiente nello stato hover. */
    private final Color hoverStart;

    /** Colore finale (in basso) del gradiente nello stato hover. */
    private final Color hoverEnd;

    /** Colore uniforme applicato quando il pulsante è premuto. */
    private final Color pressedColor;

    /** {@code true} se il cursore si trova sopra il pulsante. */
    private boolean hover;

    /** {@code true} se il pulsante è attualmente premuto. */
    private boolean pressed;

    /** Raggio degli angoli arrotondati in pixel, calcolato come {@code max(10, height/4)}. */
    private final int arc;

    /**
     * Costruisce un {@code JButtonCustom} senza testo, posizionato e dimensionato
     * secondo le coordinate e le misure fornite.
     *
     * @param x            coordinata X del pulsante nel pannello padre
     * @param y            coordinata Y del pulsante nel pannello padre
     * @param width        larghezza del pulsante in pixel
     * @param height       altezza del pulsante in pixel; influenza anche la dimensione
     *                     del font ({@code height/4}) e il raggio degli angoli
     * @param colorStart   colore iniziale del gradiente nello stato normale
     * @param colorEnd     colore finale del gradiente nello stato normale
     * @param hoverStart   colore iniziale del gradiente nello stato hover
     * @param hoverEnd     colore finale del gradiente nello stato hover
     * @param pressedColor colore uniforme nello stato premuto
     * @param textColor    colore del testo del pulsante
     */
    public JButtonCustom(int x, int y, int width, int height, Color colorStart, Color colorEnd, Color hoverStart, Color hoverEnd, Color pressedColor, Color textColor) {
        super();
        setBounds(x, y, width, height);
        setForeground(textColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, height / 4));
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.hoverStart = hoverStart;
        this.hoverEnd = hoverEnd;
        this.pressedColor = pressedColor;
        hover = false;
        pressed = false;
        arc = Math.max(10, height / 4);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    hover = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    hover = false;
                    pressed = false;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    pressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    pressed = false;
                    repaint();
                }
            }
        });

        addPropertyChangeListener("enabled", e -> {
            hover = false;
            pressed = false;
            repaint();
        });
    }

    /**
     * Costruisce un {@code JButtonCustom} con testo, posizionato e dimensionato
     * secondo le coordinate e le misure fornite.
     * <p>
     * Delega al costruttore principale e imposta il testo tramite {@link #setText(String)}.
     * </p>
     *
     * @param text         il testo da visualizzare sul pulsante
     * @param x            coordinata X del pulsante nel pannello padre
     * @param y            coordinata Y del pulsante nel pannello padre
     * @param width        larghezza del pulsante in pixel
     * @param height       altezza del pulsante in pixel
     * @param colorStart   colore iniziale del gradiente nello stato normale
     * @param colorEnd     colore finale del gradiente nello stato normale
     * @param hoverStart   colore iniziale del gradiente nello stato hover
     * @param hoverEnd     colore finale del gradiente nello stato hover
     * @param pressedColor colore uniforme nello stato premuto
     * @param textColor    colore del testo del pulsante
     */
    public JButtonCustom(String text, int x, int y, int width, int height, Color colorStart, Color colorEnd, Color hoverStart, Color hoverEnd, Color pressedColor, Color textColor) {
        this(x, y, width, height, colorStart, colorEnd, hoverStart, hoverEnd, pressedColor, textColor);
        setText(text);
    }

    /**
     * Disegna lo sfondo del pulsante con angoli arrotondati e gradiente verticale,
     * in base allo stato corrente.
     * <p>
     * Gli stati hanno la seguente priorità:
     * <ol>
     *   <li><b>Premuto</b>: sfondo uniforme con {@code pressedColor}.</li>
     *   <li><b>Hover</b>: gradiente verticale da {@code hoverStart} a {@code hoverEnd}.</li>
     *   <li><b>Normale</b>: gradiente verticale da {@code colorStart} a {@code colorEnd}.</li>
     * </ol>
     * Il rendering usa l'antialiasing per ammorbidire gli angoli arrotondati.
     * </p>
     *
     * @param g il contesto grafico fornito da Swing; viene utilizzato come
     *          {@link Graphics2D} tramite {@link Graphics#create()} per
     *          non alterare il contesto originale
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (pressed) {
            g2d.setColor(pressedColor);
        }
        else if (hover) {
            GradientPaint gp = new GradientPaint(0, 0, hoverStart, 0, h, hoverEnd);
            g2d.setPaint(gp);
        }
        else {
            GradientPaint gp = new GradientPaint(0, 0, colorStart, 0, h, colorEnd);
            g2d.setPaint(gp);
        }
        g2d.fillRoundRect(0, 0, w, h, arc, arc);

        g2d.dispose();
        super.paintComponent(g);
    }
}