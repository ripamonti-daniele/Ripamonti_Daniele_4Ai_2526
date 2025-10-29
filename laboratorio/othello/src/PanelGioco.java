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
    private final boolean PARI;

    public PanelGioco(int x, int y, int altezzaPanelPadre, Board board) {
        LATOPANEL = Integer.parseInt(String.valueOf(altezzaPanelPadre / 8));
        SPAZIATURA = Integer.parseInt(String.valueOf(LATOPANEL / 10));
        discoDisegnato = false;
        PARI = ((x + y) % 2 == 0);
        this.setBounds(x * LATOPANEL, y * LATOPANEL, LATOPANEL, LATOPANEL);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!discoDisegnato) {
                    try {
                        board.addDisco(x, y);
                    }
                    catch (InvalidParameterException exc) {
                        System.out.println(exc.getMessage());
                        return;
                    }
                    int turno = board.getTurno();
                    Color c;
                    if (turno == 0) c = Color.black;
                    else c = Color.white;
                    disegnaDisco(c);
                    board.cambiaTurno();
                }
            }
        });
        disegnaDiscoIniziale(x, y);
    }

    private void disegnaDiscoIniziale(int x, int y) {
        if (x == 3 && y == 3) disegnaDisco(Color.white);
        else if (x == 4 && y == 3) disegnaDisco(Color.black);
        else if (x == 3 && y == 4) disegnaDisco(Color.black);
        else if (x == 4 && y == 4) disegnaDisco(Color.white);
    }

    public void disegnaDisco(Color colore) {
        if (!discoDisegnato || coloreDisco != colore) {
            coloreDisco = colore;
            discoDisegnato = true;
            repaint();
        }
    }

    public void paint(Graphics g) {
        Graphics2D grafiche = (Graphics2D) g;
        if (PARI) grafiche.setColor(new Color(240, 217, 181));
        else grafiche.setColor(new Color(181, 136, 99));
        grafiche.drawRect(0, 0, LATOPANEL, LATOPANEL);
        grafiche.fillRect(0, 0, LATOPANEL, LATOPANEL);
        if (discoDisegnato) {
            grafiche.setColor(coloreDisco);
            grafiche.fillOval(SPAZIATURA, SPAZIATURA, LATOPANEL - 2 * SPAZIATURA, LATOPANEL - 2 * SPAZIATURA);
        }
    }
}
