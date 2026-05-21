import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

public class JLabelCustom extends JLabel {
    private final Color backgroundColorStart;
    private final Color backgroundColorEnd;
    private final Font fontDefault;

    public JLabelCustom(String text, Color backgroundColorStart, Color backgroundColorEnd, Font font) {
        if (font == null) throw new IllegalArgumentException("Il font non può essere null");
        fontDefault = font;
        super(text);
        setFont(font);
        this.backgroundColorStart = backgroundColorStart;
        this.backgroundColorEnd = backgroundColorEnd;
        setOpaque(false);
        setForeground(Color.WHITE);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    public JLabelCustom(String text, Color backgroundColorStart, Font font) {
        this(text, backgroundColorStart, null, font);
    }

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

    @Override
    public void setFont(Font font) {}

    @Override
    public void setText(String text) {
        super.setText(text);
        super.setFont(fontDefault);
        adattaHTML();
        repaint();
    }

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