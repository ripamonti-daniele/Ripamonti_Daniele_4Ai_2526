import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PanelGioco extends JPanel implements MouseListener {
    private final int latoPanel;
    private int spaziatura;
    private boolean discoInserito;
    private Color coloreDisco;
    private boolean pari;

    public PanelGioco(int x, int y, int altezzaPanelPadre) {
        latoPanel = Integer.parseInt(String.valueOf(altezzaPanelPadre / 8));
        spaziatura = Integer.parseInt(String.valueOf(latoPanel / 10));
        this.setBounds(x * latoPanel, y * latoPanel, latoPanel, latoPanel);
        pari = ((x + y) % 2 == 0);
        discoInserito = false;
        this.addMouseListener(this);
    }

    public void aggiungiDisco(Color colore) {
        coloreDisco = colore;
        discoInserito = true;
        repaint();
    }

    public void paint(Graphics g) {
        Graphics2D grafiche = (Graphics2D) g;
        if (pari) grafiche.setColor(new Color(240, 217, 181));
        else grafiche.setColor(new Color(181, 136, 99));
        grafiche.drawRect(0, 0, latoPanel, latoPanel);
        grafiche.fillRect(0, 0, latoPanel, latoPanel);
        if (discoInserito) {
            grafiche.setColor(coloreDisco);
            grafiche.fillOval(spaziatura, spaziatura, latoPanel - 2 * spaziatura, latoPanel - 2 * spaziatura);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!discoInserito) aggiungiDisco(Color.black);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
