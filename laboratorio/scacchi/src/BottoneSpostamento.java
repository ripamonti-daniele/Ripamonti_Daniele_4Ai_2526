import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottoneSpostamento extends JButton {
    private final Color borderColor;
    private final Color hoverColor;
    private final Color pressedColor;

    private boolean hover   = false;
    private boolean pressed = false;

    private final int arc;

    public BottoneSpostamento(int tipo, int x, int y, int dimensione, Color borderColor, Color hoverColor, Color pressedColor, Color textColor) {
        super();
        switch (tipo) {
            case 1 -> setText("⏮");
            case 2 -> setText("◀");
            case 3 -> setText("▶");
            case 4 -> setText("⏭");
            default -> {}
        }
        setBounds(x, y, dimensione, dimensione);

        this.borderColor = borderColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        this.arc = dimensione / 3;

        setForeground(textColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI Emoji", Font.PLAIN, dimensione / 4));

        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) { hover = true;  repaint(); }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) { hover = false; pressed = false; repaint(); }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) { pressed = true;  repaint(); }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) { pressed = false; repaint(); }
            }
        });

//        addPropertyChangeListener("enabled", e -> { hover = false; pressed = false; repaint(); });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (pressed) {
            g2.setColor(pressedColor);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }
        else if (hover) {
            g2.setColor(hoverColor);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        int spessore = 2;
        g2.setStroke(new BasicStroke(spessore));
        Color bc = borderColor;
        if (!isEnabled()) bc = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 80);
        g2.setColor(bc);
        g2.drawRoundRect(spessore / 2, spessore / 2, w - spessore, h - spessore, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }
}