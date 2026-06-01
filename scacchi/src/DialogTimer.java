import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Finestra di dialogo modale per la configurazione dei timer di gioco degli scacchi.
 * <p>
 * Permette all'utente di impostare ore, minuti, secondi e il guadagno di tempo
 * per entrambi i timer (bianco e nero). Le modifiche vengono applicate a entrambi
 * i timer simultaneamente alla conferma.
 * </p>
 * <p>
 * La finestra offre tre azioni:
 * <ul>
 *   <li><b>Conferma</b>: applica le impostazioni inserite a entrambi i timer.</li>
 *   <li><b>Annulla</b>: chiude il dialogo senza modificare i timer.</li>
 *   <li><b>Disattiva</b>: disattiva entrambi i timer.</li>
 * </ul>
 * </p>
 */
public class DialogTimer extends JDialog {

    /** Spinner per la selezione delle ore (0–23). */
    private JSpinner s1;

    /** Spinner per la selezione dei minuti (0–59). */
    private JSpinner s2;

    /** Spinner per la selezione dei secondi (0–59, passo 5). */
    private JSpinner s3;

    /**
     * Spinner per la selezione del guadagno di tempo in secondi
     * dopo ogni mossa (0–60, passo 5).
     */
    private JSpinner s4;

    /** Pulsante per confermare e applicare le impostazioni del timer. */
    private JButton btnConferma;

    /** Pulsante per annullare senza salvare le modifiche. */
    private JButton btnAnnulla;

    /** Pulsante per disattivare entrambi i timer. */
    private JButton btnDisattiva;

    /**
     * Costruisce e visualizza il dialogo di configurazione del timer.
     * <p>
     * I valori iniziali degli spinner vengono letti dallo stato corrente
     * di {@code timerBianco}. Le dimensioni della finestra sono calcolate
     * proporzionalmente a {@code lunghezzaCasella}.
     * </p>
     *
     * @param timerBianco     il timer del giocatore bianco; non può essere {@code null}
     * @param timerNero       il timer del giocatore nero; non può essere {@code null}
     * @param lunghezzaCasella la dimensione in pixel di una casella della scacchiera,
     *                        usata per calcolare le dimensioni della finestra;
     *                        deve essere maggiore di 0
     * @throws IllegalArgumentException se {@code lunghezzaCasella} è ≤ 0,
     *                                  o se uno dei timer è {@code null}
     */
    public DialogTimer(TimerGrafico timerBianco, TimerGrafico timerNero, int lunghezzaCasella) {
        super();
        if (lunghezzaCasella <= 0) throw new IllegalArgumentException("La lunghezza delle casella deve essere maggiore di 0");
        if (timerBianco == null) throw new IllegalArgumentException("Il timer bianco non può essere null");
        if (timerNero == null) throw new IllegalArgumentException("Il timer nero non può essere null");
        setIconImage(new ImageIcon(new ImageIcon(Objects.requireNonNull(IconaPedina.class.getResource("/img/chess.png"))).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
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

    /**
     * Crea e aggiunge al dialogo il pannello centrale con i quattro spinner
     * per la configurazione del tempo.
     * <p>
     * Ogni modifica agli spinner riproduce un suono tramite
     * {@link SuoniScacchi#cambioTempo()}.
     * </p>
     *
     * @param tg il timer da cui leggere i valori predefiniti per gli spinner;
     *           tipicamente il timer del giocatore bianco
     * @param f  il font da applicare alle etichette e agli spinner
     */
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

    /**
     * Crea e aggiunge al dialogo il pannello inferiore con i pulsanti
     * di azione: Annulla, Disattiva e Conferma.
     * <p>
     * Ogni pulsante include un effetto hover sul colore di sfondo e
     * riproduce un suono di conferma tramite {@link SuoniScacchi#conferma()}
     * alla pressione.
     * </p>
     *
     * @param timerBianco il timer del giocatore bianco su cui applicare
     *                    le modifiche o la disattivazione
     * @param timerNero   il timer del giocatore nero su cui applicare
     *                    le modifiche o la disattivazione
     * @param f           il font da applicare ai pulsanti
     */
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
