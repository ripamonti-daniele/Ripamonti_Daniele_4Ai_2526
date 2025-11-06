import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FrameGioco extends JFrame implements ActionListener {
    private final PanelGioco[][] griglia;
    private final int LUNGHEZZAFRAME;
    private final int ALTEZZAFRAME;
    private JPanel pInfo;
    private JPanel pButton;
    private JPanel pGioco;
    private final JLabel label;
    private final JButton btn;
    private final Board board;
    private List<List<Integer>> mossePossibili = new ArrayList<>();

    public FrameGioco(Board board) {
        this.board = board;
        griglia = new PanelGioco[8][8];
        label = new JLabel();
        label.setForeground(Color.lightGray);
        setLabelText("Turno: nero");
        btn = new JButton("Gioca ancora");
        btn.setEnabled(false);
        btn.setFocusable(false);
        btn.addActionListener(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        LUNGHEZZAFRAME = this.getWidth();
        ALTEZZAFRAME = this.getHeight() - (Integer.parseInt(String.valueOf(this.getHeight() / 20)));
        label.setFont(new Font("", Font.BOLD, 50 * (Integer.parseInt(String.valueOf(ALTEZZAFRAME / 996)))));
        inizializzaPanel();
        inizializzaGriglia();
        aggiornaPanel();
        setIcona();
        this.setTitle("Othello");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(28, 27, 27));
        this.add(pInfo);
        this.add(pButton);
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
        pInfo.setBounds(0, 0, LUNGHEZZAFRAME, Integer.parseInt(String.valueOf(ALTEZZAFRAME / 50 * 7)));
        pInfo.setLayout(new FlowLayout());
        pInfo.setBackground(new Color(28, 27, 27));
        pInfo.add(label);

        pButton = new JPanel();
        pButton.setBounds(0, pInfo.getHeight(), LUNGHEZZAFRAME, Integer.parseInt(String.valueOf(ALTEZZAFRAME / 50 * 3)));
        pButton.setLayout(new FlowLayout());
        pButton.setBackground(new Color(28, 27, 27));
        btn.setPreferredSize(new Dimension(Integer.parseInt(String.valueOf(pButton.getWidth() / 12)), Integer.parseInt(String.valueOf(pButton.getHeight() / 10 * 8))));
        btn.setFont(new Font("", Font.BOLD, 20 * (Integer.parseInt(String.valueOf(ALTEZZAFRAME / 996)))));
        pButton.add(btn);

        pGioco = new JPanel();
        pGioco.setBounds(0, pInfo.getHeight() + pButton.getHeight(), LUNGHEZZAFRAME, ALTEZZAFRAME - pInfo.getHeight() - pButton.getHeight());
        pGioco.setLayout(null);
        pGioco.setBackground(new Color(28, 27, 27));
    }

    public void aggiornaPanel() {
        int turno = board.getTurno();
        int[] punteggi = board.getPunteggi();
        int esito;
        String testoTurno;

        esito = board.esitoPartita();
        cancellaMossePossibili();
        if (esito != -1 && esito != -2) {
            griglia[0][0].disabilitaClick();
            finePartita(esito, punteggi);
            drawDischi(board.getGriglia());
            btn.setEnabled(true);
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
            drawMossePossibili();
        }
    }

    private void drawDischi(int[][] griglia) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (griglia[x][y] == 0) this.griglia[x][y].disegnaDisco(Color.black, false, false);
                else if (griglia[x][y] == 1) this.griglia[x][y].disegnaDisco(Color.white, false, false);
            }
        }
    }

    private void drawMossePossibili() {
        mossePossibili = board.getCaselleLegali();
        for (List<Integer> mp : mossePossibili) {
            Color c;
            if (mp.get(2) == 0) c = Color.black;
            else c = Color.white;
            griglia[mp.get(1)][mp.getFirst()].disegnaDisco(c, true, false);
        }
    }

    private void cancellaMossePossibili() {
        for (List<Integer> mp : mossePossibili) griglia[mp.get(1)][mp.getFirst()].disegnaDisco(null, false, true);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        board.reset();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) griglia[y][x].reset(x, y);
        }
        aggiornaPanel();
        btn.setEnabled(false);
    }
}
