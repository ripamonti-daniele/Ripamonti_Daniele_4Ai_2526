import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.InvalidParameterException;

public class PanelGioco extends JPanel {
    private final int LATOPANEL;
    private final int SPAZIATURA;
    private boolean discoDisegnato;
    private Color coloreDisco;
    private boolean mossaPossibile = false;
    private static boolean clickAbilitato = true;

    public PanelGioco(int x, int y, int lunghezzaPanelPadre, int altezzaPanelPadre, Board board, FrameGioco frame) {
        discoDisegnato = false;
        LATOPANEL  = altezzaPanelPadre / 8;
        SPAZIATURA = LATOPANEL / 10;
        int offset = (lunghezzaPanelPadre - LATOPANEL * 8) / 2;
        this.setBounds(x * LATOPANEL + offset, y * LATOPANEL, LATOPANEL, LATOPANEL);
        disegnaDiscoIniziale(x, y);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!discoDisegnato && clickAbilitato) {
                    try {
                        board.addDisco(x, y);
                    }
                    catch (InvalidParameterException exc) {
                        return;
                    }
                    frame.aggiornaPanel();
                }
            }
        });
    }

    private void disegnaDiscoIniziale(int x, int y) {
        if (x == 3 && y == 3) disegnaDisco(Color.white, false, false);
        else if (x == 4 && y == 3) disegnaDisco(Color.black, false, false);
        else if (x == 3 && y == 4) disegnaDisco(Color.black, false, false);
        else if (x == 4 && y == 4) disegnaDisco(Color.white, false, false);
    }

    public void reset(int x, int y) {
        discoDisegnato = false;
        coloreDisco = null;
        mossaPossibile = false;
        disegnaDisco(null, false, true);
        disegnaDiscoIniziale(x, y);
    }

    public void setClickAbilitato(boolean abilitato) {
        clickAbilitato = abilitato;
    }

    public void disegnaDisco(Color colore, boolean disegnaMossaPossibile, boolean cancella) {
        if (disegnaMossaPossibile && cancella) throw new InvalidParameterException("Non puoi disegnare e cancellare contemporaneamente le mosse possibili");
        mossaPossibile = disegnaMossaPossibile;

        if (cancella) repaint();
        else if (mossaPossibile && !discoDisegnato) {
            coloreDisco = colore;
            repaint();
        }
        else if (!discoDisegnato || coloreDisco != colore) {
            coloreDisco = colore;
            discoDisegnato = true;
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D grafiche = (Graphics2D) g.create();

        //migliora la grafica
        grafiche.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        grafiche.setStroke(new BasicStroke(5));
        grafiche.setColor(new Color(34, 139, 34));
        grafiche.fillRect(0, 0, LATOPANEL, LATOPANEL);
        grafiche.setColor(Color.black);
        grafiche.drawRect(0, 0, LATOPANEL, LATOPANEL);

        if (mossaPossibile) {
            grafiche.setColor(coloreDisco);
            grafiche.drawOval(SPAZIATURA, SPAZIATURA, LATOPANEL - 2 * SPAZIATURA, LATOPANEL - 2 * SPAZIATURA);
        }

        else if (discoDisegnato) {
            grafiche.setColor(coloreDisco);
            grafiche.fillOval(SPAZIATURA, SPAZIATURA, LATOPANEL - 2 * SPAZIATURA, LATOPANEL - 2 * SPAZIATURA);
        }

        grafiche.dispose();
    }
}
