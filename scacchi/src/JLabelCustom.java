import javax.swing.*;
import java.awt.*;

public class JLabelCustom extends JLabel {

    private final Color backgroundColorStart;
    private final Color backgroundColorEnd;

    public JLabelCustom(String text, Color backgroundColorStart, Color backgroundColorEnd) {
        super(text);
        this.backgroundColorStart = backgroundColorStart;
        this.backgroundColorEnd = backgroundColorEnd;
        setOpaque(false);
        setForeground(Color.WHITE);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public JLabelCustom(String text, Color backgroundColorStart) {
        this(text, backgroundColorStart, null);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
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