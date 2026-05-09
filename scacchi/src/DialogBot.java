import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class DialogBot extends JDialog {
    private int difficoltaScelta;
    private Color coloreScelta;
    private boolean ricercaAvanzata;
    private boolean confermato;

    public DialogBot(int lunghezzaCasella) {
        super();
        difficoltaScelta = -1;
        coloreScelta = null;
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

        JButton btnConferma = new JButton("Gioca");
        btnConferma.setFont(grassetto);
        btnConferma.setFocusPainted(false);
        btnConferma.setOpaque(true);
        btnConferma.setBorderPainted(false);
        btnConferma.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConferma.setEnabled(false);
        btnConferma.setBackground(new Color(150, 180, 230));
        btnConferma.setForeground(Color.white);
        btnConferma.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btnConferma.isEnabled()) btnConferma.setBackground(new Color(90, 160, 255));
            }
            public void mouseExited (MouseEvent e) {
                if (btnConferma.isEnabled()) btnConferma.setBackground(new Color(66, 133, 244));
            }
        });
        btnConferma.addActionListener(_ -> {
            confermato = true;
            dispose();
        });

        JLabel lblDiff = new JLabel("Difficoltà");
        lblDiff.setFont(titolo);
        lblDiff.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblDiff);
        centro.add(Box.createVerticalStrut(8));

        String[] nomiDiff = { "Facile", "Media", "Difficile" };
        JButton[] btnDiff = new JButton[3];
        JPanel panelDiff = new JPanel(new GridLayout(1, 3, 8, 0));
        panelDiff.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelDiff.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color[] DIFF_NORMALE = { new Color(34, 139, 60), new Color(210, 160, 10), new Color(180, 40, 40) };
        Color[] DIFF_HOVER = { new Color(50, 170, 80), new Color(240, 190, 40), new Color(210, 55,55) };
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            btnDiff[i] = creaToggleBtn(nomiDiff[i], grassetto, DIFF_NORMALE[i], DIFF_HOVER[i], Color.white);
            btnDiff[i].addActionListener(_ -> {
                difficoltaScelta = idx + 2;
                selezionaBtn(btnDiff, idx, DIFF_NORMALE, null);
                aggiornaConferma(btnConferma);
            });
            panelDiff.add(btnDiff[i]);
        }
        centro.add(panelDiff);
        centro.add(Box.createVerticalStrut(18));

        JLabel lblColore = new JLabel("Colore Bot");
        lblColore.setFont(titolo);
        lblColore.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(lblColore);
        centro.add(Box.createVerticalStrut(8));

        String[] nomiColore = { "Bianco", "Nero", "Random" };
        Color[] colNorm = { new Color(200, 200, 210), new Color(50, 50, 55), new Color(110, 50, 190) };
        Color[] colHov = { new Color(225, 225,235), new Color(75, 75, 85), new Color(140, 80, 220) };
        Color[] colTesto = { new Color(30, 30, 40), Color.white, Color.white };
        JButton[] btnColore = new JButton[3];

        JPanel panelColore = new JPanel(new GridLayout(1, 3, 8, 0));
        panelColore.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelColore.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < 3; i++) {
            final int idx = i;
            btnColore[i] = creaToggleBtn(nomiColore[i], grassetto, colNorm[i], colHov[i], colTesto[i]);
            btnColore[i].addActionListener(_ -> {
                switch(idx) {
                    case 0 -> coloreScelta = Color.white;
                    case 1 -> coloreScelta = Color.black;
                    case 2 -> {
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

        JLabel lblDescRicerca = new JLabel("<html>Abilita algoritmi di ricerca più profondi per mosse più precise.</html>");
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
            ricercaAvanzata = toggleRicerca.isSelected();
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
        centro.add(Box.createVerticalStrut(18));

        JLabel lblNota = new JLabel("<html><i>Maggiore è la difficoltà, maggiore sarà il tempo richiesto dal bot per eseguire ogni mossa.</i></html>");
        lblNota.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNota.setForeground(new Color(140, 90, 0));
        lblNota.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblNota.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 160, 10), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        centro.add(lblNota);

        add(centro, BorderLayout.CENTER);

        JButton btnAnnulla = creaToggleBtn("Annulla", grassetto, new Color(180, 40, 40), new Color(210, 55, 55), Color.white);
        btnAnnulla.setEnabled(true);
        btnAnnulla.addActionListener(_ -> dispose());

        JPanel panelBottoni = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBottoni.setBorder(BorderFactory.createEmptyBorder(10, 15, 12, 15));
        panelBottoni.setPreferredSize(new Dimension(0, 60));
        panelBottoni.add(btnAnnulla);
        panelBottoni.add(btnConferma);
        add(panelBottoni, BorderLayout.SOUTH);

        setSize(lunghezzaCasella * 11 / 2, lunghezzaCasella * 9 / 2);
        setMinimumSize(new Dimension(500, 450));
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(hov); }
            public void mouseExited (MouseEvent e) { btn.setBackground(norm); }
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
}