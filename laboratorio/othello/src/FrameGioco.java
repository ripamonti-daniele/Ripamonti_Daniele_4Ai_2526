import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FrameGioco extends JFrame implements ComponentListener {
    private final PanelGioco[][] griglia;
    private int lunghezzaFrame;
    private int altezzaFrame;
    private int proporzioniFrame;
    private final int BARRASUPERIORE;
    private JPanel pInfo;
    private JPanel pGioco;
    private final Board board;

    public FrameGioco(Board board) {
        this.board = board;
        lunghezzaFrame = 1024;
        altezzaFrame = 768;
        BARRASUPERIORE = 31;
        proporzioniFrame = Integer.parseInt(String.valueOf(lunghezzaFrame / (altezzaFrame - BARRASUPERIORE)));
        griglia = new PanelGioco[8][8];
        inizializzaPanel();
        inizializzaGriglia();
        this.setTitle("Othello");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(lunghezzaFrame, altezzaFrame + BARRASUPERIORE);
        this.addComponentListener(this);
        this.add(pInfo);
        this.add(pGioco);

        this.setVisible(true);
    }

    private void inizializzaGriglia() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                griglia[i][j] = new PanelGioco(j, i, pGioco.getHeight(), board);
                pGioco.add(griglia[i][j]);
            }
        }
    }

    private void inizializzaPanel() {
        pInfo = new JPanel();
        pInfo.setBounds(0, 0, lunghezzaFrame, Integer.parseInt(String.valueOf(altezzaFrame / 5)));
        pInfo.setBackground(Color.red);
        pGioco = new JPanel();
        pGioco.setLayout(null);
        pGioco.setBounds(0, pInfo.getHeight(), lunghezzaFrame, Integer.parseInt(String.valueOf(altezzaFrame / 5 * 4)));
    }

    public void drawDischi(int[][] griglia) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (griglia[x][y] == 0) this.griglia[x][y].disegnaDisco(Color.black);
                else if (griglia[x][y] == 1) this.griglia[x][y].disegnaDisco(Color.white);
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        lunghezzaFrame = this.getWidth();
        altezzaFrame = this.getHeight();
        proporzioniFrame = Integer.parseInt(String.valueOf(lunghezzaFrame / (altezzaFrame - BARRASUPERIORE)));
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
