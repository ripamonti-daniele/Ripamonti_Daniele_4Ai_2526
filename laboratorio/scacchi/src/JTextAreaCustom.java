import javax.swing.*;
import java.awt.*;

public class JTextAreaCustom extends JTextArea {

    public JTextAreaCustom(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
        setFont(new Font("Segoe UI", Font.PLAIN, Math.max(12, height / 3)));
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setCaretColor(Color.BLACK);
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(false);
    }

    public JTextAreaCustom(String text, int x, int y, int width, int height) {
        this(x, y, width, height);
        setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int arc = Math.max(8, getHeight() / 4);
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
        g2d.dispose();

        int altezzaCarattere = getFontMetrics(getFont()).getHeight() * getLineCount();
        setMargin(new Insets((getHeight() - altezzaCarattere) / 2, getWidth() / 20, (getHeight() + altezzaCarattere) / 2, getWidth() / 20));

        super.paintComponent(g);
    }

    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
        if (b) setCaretColor(Color.black);
        else setCaretColor(new Color(0,0,0,0));
    }
}