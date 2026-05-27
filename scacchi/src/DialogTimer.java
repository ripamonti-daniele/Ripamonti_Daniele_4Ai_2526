import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogTimer extends JDialog {
    private JSpinner s1;
    private JSpinner s2;
    private JSpinner s3;
    private JSpinner s4;
    private JButton btnConferma;
    private JButton btnAnnulla;
    private JButton btnDisattiva;

    public DialogTimer(TimerGrafico timerBianco, TimerGrafico timerNero, int lunghezzaCasella) {
        if (lunghezzaCasella <= 0) throw new IllegalArgumentException("La lunghezza delle casella deve essere maggiore di 0");
        if (timerBianco == null) throw new IllegalArgumentException("Il timer bianco non può essere null");
        if (timerNero == null) throw new IllegalArgumentException("Il timer nero non può essere null");
        super();
        setIconImage(new ImageIcon(new ImageIcon("img/chess.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
        setTitle("Impostazioni timer");
        setModal(true);
        setResizable(true);
        setLayout(new BorderLayout());

        Font f = new Font("Segoe UI", Font.BOLD, 14);
        creaSpinner(timerBianco, f);
        creaBottoni(timerBianco, timerNero, f);

        setSize(lunghezzaCasella * 9 / 2, lunghezzaCasella * 7 / 2);
        setMinimumSize(new Dimension(350, 300));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void creaSpinner(TimerGrafico tg, Font f) {
        s1 = new JSpinner(new SpinnerNumberModel(tg.getOreDefault(), 0, 23, 1));
        s2 = new JSpinner(new SpinnerNumberModel(tg.getMinutiDefault(), 0, 59, 1));
        s3 = new JSpinner(new SpinnerNumberModel(tg.getSecondiDefault(), 0, 59, 5));
        s4 = new JSpinner(new SpinnerNumberModel(tg.getGuadagno(), 0, 60, 5));

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel l1 = new JLabel("Ore:"); l1.setFont(f); panel.add(l1);
        s1.setFont(f); panel.add(s1);
        JLabel l2 = new JLabel("Minuti:"); l2.setFont(f); panel.add(l2);
        s2.setFont(f); panel.add(s2);
        JLabel l3 = new JLabel("Secondi:"); l3.setFont(f); panel.add(l3);
        s3.setFont(f); panel.add(s3);
        JLabel l4 = new JLabel("Guadagno (sec):"); l4.setFont(f); panel.add(l4);
        s4.setFont(f); panel.add(s4);

        s1.addChangeListener(_ -> SuoniScacchi.cambioTempo());
        s2.addChangeListener(_ -> SuoniScacchi.cambioTempo());
        s3.addChangeListener(_ -> SuoniScacchi.cambioTempo());
        s4.addChangeListener(_ -> SuoniScacchi.cambioTempo());

        add(panel, BorderLayout.CENTER);
    }

    private void creaBottoni(TimerGrafico timerBianco, TimerGrafico timerNero, Font f) {
        Insets margin = new Insets(4, 8, 4, 8);
        btnConferma = new JButton("Conferma");
        btnConferma.setFont(f);
        btnConferma.setMargin(margin);
        btnConferma.setBackground(new Color(66, 133, 244));
        btnConferma.setForeground(Color.white);
        btnConferma.setFocusPainted(false);
        btnConferma.setOpaque(true);
        btnConferma.setBorderPainted(false);
        btnConferma.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConferma.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnConferma.setBackground(new Color(90, 160, 255)); }
            public void mouseExited(MouseEvent e) { btnConferma.setBackground(new Color(66, 133, 244)); }
        });
        btnConferma.addActionListener(_ -> {
            timerBianco.setTimer((int) s1.getValue(), (int) s2.getValue(), (int) s3.getValue(), (int) s4.getValue());
            timerNero.setTimer((int) s1.getValue(), (int) s2.getValue(), (int) s3.getValue(), (int) s4.getValue());
            SuoniScacchi.conferma();
            dispose();
        });

        btnAnnulla = new JButton("Annulla");
        btnAnnulla.setFont(f);
        btnAnnulla.setMargin(margin);
        btnAnnulla.setBackground(new Color(60, 60, 60));
        btnAnnulla.setForeground(Color.white);
        btnAnnulla.setFocusPainted(false);
        btnAnnulla.setOpaque(true);
        btnAnnulla.setBorderPainted(false);
        btnAnnulla.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAnnulla.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAnnulla.setBackground(new Color(80, 80, 80)); }
            public void mouseExited(MouseEvent e) { btnAnnulla.setBackground(new Color(60, 60, 60)); }
        });
        btnAnnulla.addActionListener(_ -> {
            SuoniScacchi.conferma();
            dispose();
        });

        btnDisattiva = new JButton("Disattiva");
        btnDisattiva.setFont(f);
        btnDisattiva.setMargin(margin);
        btnDisattiva.setBackground(new Color(180, 40, 40));
        btnDisattiva.setForeground(Color.white);
        btnDisattiva.setFocusPainted(false);
        btnDisattiva.setOpaque(true);
        btnDisattiva.setBorderPainted(false);
        btnDisattiva.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDisattiva.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDisattiva.setBackground(new Color(210, 55, 55)); }
            public void mouseExited(MouseEvent e) { btnDisattiva.setBackground(new Color(180, 40, 40)); }
        });
        btnDisattiva.addActionListener(_ -> {
            timerBianco.disattiva();
            timerNero.disattiva();
            SuoniScacchi.conferma();
            dispose();
        });

        JPanel panelBottoni = new JPanel(new GridLayout(1, 3, 10, 0));
        panelBottoni.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelBottoni.add(btnAnnulla);
        panelBottoni.add(btnDisattiva);
        panelBottoni.add(btnConferma);

        add(panelBottoni, BorderLayout.SOUTH);
    }
}
