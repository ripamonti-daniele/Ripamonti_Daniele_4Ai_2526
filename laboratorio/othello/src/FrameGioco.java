import javax.swing.*;
import java.awt.*;

public class FrameGioco extends JFrame {
    private final PanelGioco[][] griglia;
    private final int LUNGHEZZAFRAME;
    private final int ALTEZZAFRAME;
    private JPanel pInfo;
    private JPanel pGioco;
    private final JLabel label;
    private final Board board;

    public FrameGioco(Board board) {
        this.board = board;
        griglia = new PanelGioco[8][8];
        label = new JLabel();
        label.setFont(new Font("", Font.BOLD, 50));
        label.setForeground(Color.lightGray);
        setLabelText("Turno: nero");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        LUNGHEZZAFRAME = this.getWidth();
        ALTEZZAFRAME = this.getHeight() - (Integer.parseInt(String.valueOf(this.getHeight() / 20)));
        inizializzaPanel();
        inizializzaGriglia();
        aggiornaPanelInfo();
        setIcona();
        this.setTitle("Othello");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(28, 27, 27));
        this.add(pInfo);
        this.add(pGioco);
    }

    private void setIcona() {
        try {
            Image icona = new ImageIcon("othello_icona.png").getImage();
            this.setIconImage(icona);
        }
        catch (Exception e) {
            System.out.println("Icona non trovata");
        }
    }

    private void inizializzaGriglia() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                griglia[i][j] = new PanelGioco(j, i, pGioco.getWidth(), pGioco.getHeight(), board, this);
                pGioco.add(griglia[i][j]);
            }
        }
    }

    private void inizializzaPanel() {
        pInfo = new JPanel();
        pInfo.setBounds(0, 0, LUNGHEZZAFRAME, Integer.parseInt(String.valueOf(ALTEZZAFRAME / 5)));
        pInfo.setLayout(new FlowLayout());
        pInfo.setBackground(new Color(28, 27, 27));
        pInfo.add(label);
        pGioco = new JPanel();
        pGioco.setBounds(0, pInfo.getHeight(), LUNGHEZZAFRAME, ALTEZZAFRAME - pInfo.getHeight());
        pGioco.setLayout(null);
        pGioco.setBackground(new Color(28, 27, 27));
    }

    public void aggiornaPanelInfo() {
        int turno = board.getTurno();
        int[] punteggi = board.getPunteggi();
        int esito;
        String testoTurno;

        esito = board.esitoPartita();
        if (esito != -1 && esito != -2) {
            griglia[0][0].disabilitaClick();
            finePartita(esito, punteggi);
            drawDischi(board.getGriglia());
        }
        else {
            if (turno == 0) {
                testoTurno = "nero";
                if (esito == -2) testoTurno += " (il bianco salta il turno)";
            }
            else {
                testoTurno = "bianco";
                if (esito == -2) testoTurno += " (il nero salta il turno)";
            }
            setLabelText("<html><div style='text-align:center;'>Nero: " + punteggi[0] + " - Bianco: " + punteggi[1] + "<br>Turno: " + testoTurno + "</div><html>");
            drawDischi(board.getGriglia());
        }
    }

    private void drawDischi(int[][] griglia) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (griglia[x][y] == 0) this.griglia[x][y].disegnaDisco(Color.black);
                else if (griglia[x][y] == 1) this.griglia[x][y].disegnaDisco(Color.white);
            }
        }
    }

    private void finePartita(int esito, int[] punteggi) {
        String testo = "<html><div style='text-align:center;'>Nero: " + punteggi[0] + " - Bianco: " + punteggi[1] + "<br>";
        if (esito == 0) testo += "Vince il nero!";
        else if (esito == 1) testo += "Vince il bianco!";
        else if (esito == 2) testo += "Pareggio!";
        if (punteggi[0] + punteggi[1] < 64) testo += " (mosse finite)";
        testo += "</div><html>";
        label.setText(testo);
    }

    private void setLabelText(String s) {
        label.setText(s);
    }
}
