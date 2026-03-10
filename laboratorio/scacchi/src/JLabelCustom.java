import javax.swing.*;
import java.awt.*;

public class JLabelCustom extends JLabel {

    private final Color backgroundColor;

    public JLabelCustom(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        setForeground(Color.WHITE);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
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

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.dispose();

        super.paintComponent(g);
    }
}