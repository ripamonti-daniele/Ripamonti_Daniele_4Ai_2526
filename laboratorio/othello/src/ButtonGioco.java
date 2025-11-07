import javax.swing.*;
import java.awt.*;

public class ButtonGioco extends JButton {
    private final String testo;
    private final int PROPORZIONI;
    private boolean hover = false;

    public ButtonGioco(String testo, int proporzioni) {
        this.testo = testo;
        this.PROPORZIONI = proporzioni;
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setBorder(null);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafiche = (Graphics2D) g.create();

        //migliora la grafica
        grafiche.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //migliora la qualità del testo
        grafiche.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;
        int offset = 5;
        grafiche.setStroke(new BasicStroke(offset * 2));

        if (!this.isEnabled()) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            grafiche.setColor(new Color(60, 70, 60));
            grafiche.fillRoundRect(0, 0, w, h, arc, arc);
            grafiche.setColor(new Color(80, 100, 80));
            grafiche.fillRoundRect(offset, offset, w - offset * 2, h - offset * 2, arc, arc);
        }

        else if (hover && this.isEnabled()) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            grafiche.setColor(new Color(0, 130, 0));
            grafiche.fillRoundRect(0, 0, w, h, arc, arc);
            grafiche.setColor(new Color(50, 170, 50));
            grafiche.fillRoundRect(offset, offset, w - offset * 2, h - offset * 2, arc, arc);
        }

        else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            grafiche.setColor(new Color(0, 100, 0));
            grafiche.fillRoundRect(0, 0, w, h, arc, arc);
            grafiche.setColor(new Color(34, 139, 34));
            grafiche.fillRoundRect(offset, offset, w - offset * 2, h - offset * 2, arc, arc);
        }

        if (!this.isEnabled()) grafiche.setColor(new Color(200, 200, 200));
        else grafiche.setColor(Color.WHITE);
        grafiche.setFont(new Font("", Font.BOLD, 20 * PROPORZIONI));
        FontMetrics fm = grafiche.getFontMetrics();
        int x = (w - fm.stringWidth(testo)) / 2;
        int y = (h + fm.getAscent()) / 2 - 4;
        grafiche.drawString(testo, x, y);

        grafiche.dispose();
    }
}
