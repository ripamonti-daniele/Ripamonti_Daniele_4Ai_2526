import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class DialogBot extends JDialog {
    private int difficoltaScelta;
    private Color coloreScelta;
    private boolean ricercaAvanzata;
    private boolean random;
    private boolean confermato;

    private static int difficoltaSceltaDefault = -1;
    private static Color coloreSceltaDefault = null;
    private static boolean ricercaAvanzataDefault = false;
    private static boolean randomDefault = false;

    public DialogBot(int lunghezzaCasella, TimerGrafico timerBianco, TimerGrafico timerNero) {
        if (lunghezzaCasella <= 0) throw new IllegalArgumentException("La lunghezza delle casella deve essere maggiore di 0");
        super();
        difficoltaScelta = difficoltaSceltaDefault;
        coloreScelta = coloreSceltaDefault;
        ricercaAvanzata = ricercaAvanzataDefault;
        random = randomDefault;

        setIconImage(new ImageIcon(new ImageIcon("img/chess.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
        setTitle("Impostazioni Bot");
        setModal(true);
        setResizable(true);
        setLayout(new BorderLayout());

        Font titolo = new Font("Segoe UI", Font.BOLD, 15);
        Font normale = new Font("Segoe UI", Font.PLAIN, 13);
        Font grassetto = new Font("Segoe UI", Font.BOLD,13);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JButton btnConferma = creaToggleBtn("Gioca", grassetto, new Color(66, 133, 244), new Color(90, 160, 255), Color.white);
        btnConferma.setEnabled(false);
        btnConferma.addActionListener(_ -> {
            confermato = true;
            difficoltaSceltaDefault = difficoltaScelta;
            coloreSceltaDefault = coloreScelta;
            ricercaAvanzataDefault = ricercaAvanzata;
            randomDefault = random;
            SuoniScacchi.inizioPartita();
            dispose();
        });

        JLabel lblNota = new JLabel("");
        lblNota.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNota.setForeground(new Color(140, 90, 0));
        lblNota.setAlignmentX(Component.LEFT_ALIGNMENT);
        Border bordoNota = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 160, 10), 1, true), BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        JLabel lblDiff = new JLabel("Difficoltà");
        lblDiff.setFont(titolo);
        lblDiff.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblDiff);
        centro.add(Box.createVerticalStrut(8));

        String[] nomiDiff = { "Facile", "Media", "Difficile", "Estrema" };
        JButton[] btnDiff = new JButton[4];
        JPanel panelDiff = new JPanel(new GridLayout(1, 4, 8, 0));
        panelDiff.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelDiff.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color[] DIFF_NORMALE = { new Color(34, 139, 60), new Color(210, 160, 10), new Color(180, 40, 40), new Color(0, 30, 100) };
        Color[] DIFF_HOVER = { new Color(50, 170, 80), new Color(240, 190, 40), new Color(210, 55, 55), new Color(10, 55, 140) };
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            btnDiff[i] = creaToggleBtn(nomiDiff[i], grassetto, DIFF_NORMALE[i], DIFF_HOVER[i], Color.white);
            btnDiff[i].addActionListener(_ -> {
                SuoniScacchi.opzioniDialog();
                if (idx == 3) {
                    lblNota.setText("<html><i>In difficoltà estrema il bot può richiedere oltre 1 minuto per effettuare alcune mosse</i></html>");
                    lblNota.setBorder(bordoNota);
                }
                else {
                    lblNota.setText("");
                    lblNota.setBorder(null);
                }
                difficoltaScelta = idx + 2;
                selezionaBtn(btnDiff, idx, DIFF_NORMALE, null);
                aggiornaConferma(btnConferma);
            });
            panelDiff.add(btnDiff[i]);
        }
        centro.add(panelDiff);
        centro.add(Box.createVerticalStrut(18));
        centro.add(lblNota);

        JLabel lblColore = new JLabel("Colore");
        lblColore.setFont(titolo);
        lblColore.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblColore);
        centro.add(Box.createVerticalStrut(8));

        String[] nomiColore = { "Bianco", "Nero", "Random" };
        Color[] colNorm = { new Color(200, 200, 210), new Color(50, 50, 55), new Color(105, 105, 105) };
        Color[] colHov = { new Color(225, 225,235), new Color(75, 75, 85), new Color(130, 130, 130) };
        Color[] colTesto = { new Color(30, 30, 40), Color.white, Color.white };
        JButton[] btnColore = new JButton[3];

        JPanel panelColore = new JPanel(new GridLayout(1, 3, 8, 0));
        panelColore.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelColore.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 3; i++) {
            final int idx = i;
            btnColore[i] = creaToggleBtn(nomiColore[i], grassetto, colNorm[i], colHov[i], colTesto[i]);
            btnColore[i].addActionListener(_ -> {
                SuoniScacchi.opzioniDialog();
                switch(idx) {
                    case 0 -> {
                        random = false;
                        coloreScelta = Color.white;
                    }
                    case 1 -> {
                        random = false;
                        coloreScelta = Color.black;
                    }
                    case 2 -> {
                        random = true;
                        Random r = new Random();
                        if (r.nextBoolean()) coloreScelta = Color.white;
                        else coloreScelta = Color.black;
                    }
                }
                selezionaBtn(btnColore, idx, colNorm, colTesto);
                aggiornaConferma(btnConferma);
            });
            panelColore.add(btnColore[i]);
        }
        centro.add(panelColore);
        centro.add(Box.createVerticalStrut(18));

        JLabel lblRicerca = new JLabel("Ricerca avanzata");
        lblRicerca.setFont(titolo);
        lblRicerca.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblRicerca);
        centro.add(Box.createVerticalStrut(4));

        JLabel lblDescRicerca = new JLabel("<html>In determinate situazioni abilita algoritmi di ricerca più profondi per mosse più precise ma con tempi di attesa maggiori.</html>");
        lblDescRicerca.setFont(normale);
        lblDescRicerca.setForeground(new Color(90, 90, 90));
        lblDescRicerca.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblDescRicerca);
        centro.add(Box.createVerticalStrut(8));

        JToggleButton toggleRicerca = new JToggleButton("Off");
        toggleRicerca.setFont(grassetto);
        toggleRicerca.setFocusPainted(false);
        toggleRicerca.setBorderPainted(false);
        toggleRicerca.setContentAreaFilled(false);
        toggleRicerca.setOpaque(true);
        toggleRicerca.setBackground(new Color(60, 60, 60));
        toggleRicerca.setForeground(Color.white);
        toggleRicerca.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleRicerca.setPreferredSize(new Dimension(90, 32));
        toggleRicerca.setMaximumSize(new Dimension(90, 32));
        toggleRicerca.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleRicerca.addItemListener(_ -> {
            SuoniScacchi.opzioniDialog();
            ricercaAvanzata = !ricercaAvanzata;
            toggleRicerca.setText(ricercaAvanzata ? "On" : "Off");
        });
        toggleRicerca.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                toggleRicerca.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                toggleRicerca.setBackground(new Color(60, 60, 60));
            }
        });
        centro.add(toggleRicerca);
        centro.add(Box.createVerticalStrut(5));

        add(centro, BorderLayout.CENTER);

        JButton btnAnnulla = creaToggleBtn("Annulla", grassetto, new Color(60, 60, 60), new Color(80, 80, 80), Color.white);
        btnAnnulla.setEnabled(true);
        btnAnnulla.addActionListener(_ -> {
            SuoniScacchi.conferma();
            dispose();
        });

        JButton btnTimer = creaToggleBtn("Modifica timer", grassetto, new Color(180, 40, 40), new Color(210, 55, 55), Color.white);
        btnTimer.setEnabled(true);
        btnTimer.addActionListener(_ -> {
            SuoniScacchi.menu();
            new DialogTimer(timerBianco, timerNero, lunghezzaCasella);
        });

        JPanel panelBottoni = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBottoni.setBorder(BorderFactory.createEmptyBorder(10, 15, 12, 15));
        panelBottoni.setPreferredSize(new Dimension(0, 60));
        panelBottoni.add(btnAnnulla);
        if (timerBianco != null && timerNero != null) panelBottoni.add(btnTimer);
        panelBottoni.add(btnConferma);
        add(panelBottoni, BorderLayout.SOUTH);

        if (coloreScelta != null || random) {
            int idx = 0;
            if (coloreScelta != null && coloreScelta.equals(Color.black)) idx = 1;
            if (random) {
                Random r = new Random();
                if (r.nextBoolean()) coloreScelta = Color.white;
                else coloreScelta = Color.black;
                idx = 2;
            }
            selezionaBtn(btnColore, idx, colNorm, colTesto);
        }
        if (difficoltaScelta != -1) selezionaBtn(btnDiff, difficoltaScelta - 2, DIFF_NORMALE, null);
        if (ricercaAvanzata) toggleRicerca.setText("On");
        aggiornaConferma(btnConferma);

        setSize(lunghezzaCasella * 5, lunghezzaCasella * 4);
        setMinimumSize(new Dimension(515, 415));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton creaToggleBtn(String testo, Font f, Color norm, Color hov, Color fg) {
        JButton btn = new JButton(testo);
        btn.setFont(f);
        btn.setBackground(norm);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 3, false));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(hov);
            }
            public void mouseExited (MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(norm);
            }
        });
        return btn;
    }

    private void selezionaBtn(JButton[] btns, int idx, Color[] normali, Color[] testi) {
        for (int j = 0; j < btns.length; j++) {
            boolean sel = (j == idx);
            btns[j].putClientProperty("selected", sel);
            btns[j].setBackground(normali[j]);
            if (testi != null) btns[j].setForeground(testi[j]);
            if (sel) btns[j].setBorder(BorderFactory.createLineBorder(Color.black, 3, false));
            else btns[j].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 3, false));
        }
    }

    private void aggiornaConferma(JButton btnConferma) {
        boolean pronto = difficoltaScelta != -1 && coloreScelta != null;
        btnConferma.setEnabled(pronto);
        btnConferma.setBackground(pronto ? new Color(66, 133, 244) : new Color(150, 180, 230));
    }

    public boolean isConfermato(){
        return confermato;
    }

    public boolean isRicercaAvanzata() {
        return ricercaAvanzata;
    }

    public int getDifficolta() {
        return difficoltaScelta;
    }

    public Color getColoreBot() {
        return coloreScelta;
    }

    public boolean isRandom() {
        return random;
    }

    public static String getStringaDifficolta() {
        switch (difficoltaSceltaDefault) {
            case 2 -> {
                return "facile";
            }
            case 3 -> {
                return "medio";
            }
            case 4 -> {
                return "difficile";
            }
            case 5 -> {
                return "estremo";
            }
            default -> {
                return "";
            }
        }
    }
}