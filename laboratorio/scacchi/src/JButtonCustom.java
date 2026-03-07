import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonCustom extends JButton {
    private final Color colorStart;
    private final Color colorEnd;
    private final Color hoverStart;
    private final Color hoverEnd;
    private final Color pressedColor;

    private boolean hover;
    private boolean pressed;
    private final int arc; // raggio bordi

    public JButtonCustom(int x, int y, int width, int height, Color colorStart, Color colorEnd, Color hoverStart, Color hoverEnd, Color pressedColor, Color textColor) {
        super();
        setBounds(x, y, width, height);
        setForeground(textColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, Math.max(12, height / 4)));

        // Colori
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.hoverStart = hoverStart;
        this.hoverEnd = hoverEnd;
        this.pressedColor = pressedColor;

        hover = false;
        pressed = false;

        arc = Math.max(10, height / 4); // arc circa 1/4 altezza

        // Mouse listener
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
                if (isEnabled()) {
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

        // Ridisegna quando cambia lo stato enabled
        addPropertyChangeListener("enabled", evt -> {
            hover = false;
            pressed = false;
            repaint();
        });
    }

    public JButtonCustom(String text, int x, int y, int width, int height, Color colorStart, Color colorEnd, Color hoverStart, Color hoverEnd, Color pressedColor, Color textColor) {
        this(x, y, width, height, colorStart, colorEnd, hoverStart, hoverEnd, pressedColor, textColor);
        setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (pressed) {
            g2d.setColor(pressedColor);
            g2d.fillRoundRect(0, 0, w, h, arc, arc);
            setForeground(Color.WHITE);
        } else if (hover) {
            GradientPaint gp = new GradientPaint(0, 0, hoverStart, 0, h, hoverEnd);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, arc, arc);
            setForeground(Color.WHITE);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, colorStart, 0, h, colorEnd);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, w, h, arc, arc);
            setForeground(Color.WHITE);
        }

        g2d.dispose();
        super.paintComponent(g);
    }
}