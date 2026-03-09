import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String testo = JTextAreaCustom.this.getText();
                    if (testo.length() > 15) {
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

    public JTextAreaCustom(String text, int x, int y, int width, int height) {
        this(x, y, width, height);
        setText(text);
    }

    @Override
    public void setEditable(boolean b) {
        super.setEditable(b);
        if (b) setCaretColor(Color.black);
        else setCaretColor(new Color(0,0,0,0));
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
}