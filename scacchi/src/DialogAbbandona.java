import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Dialog modale di conferma per l'abbandono della partita in corso.
 * <p>
 * Mostra un messaggio di conferma e due pulsanti: "Abbandona" e "Annulla".
 * Al termine dell'interazione, il risultato della scelta dell'utente è
 * consultabile tramite {@link #isConfermato()}.
 * </p>
 * <p>
 * Il dialog è modale: la chiamata al costruttore blocca il thread chiamante
 * fino alla chiusura della finestra.
 * </p>
 */
public class DialogAbbandona extends JDialog {

    /**
     * {@code true} se l'utente ha confermato l'abbandono, {@code false}
     * se ha annullato o chiuso il dialog senza confermare.
     */
    private boolean confermato = false;

    /**
     * Costruisce e visualizza il dialog di conferma abbandono.
     * <p>
     * Il costruttore inizializza tutti i componenti grafici, registra i listener
     * sui pulsanti e rende il dialog visibile. Poiché il dialog è modale,
     * il costruttore ritorna solo dopo che l'utente ha chiuso la finestra.
     * </p>
     */
    public DialogAbbandona() {
        super();
        setIconImage(new ImageIcon(new ImageIcon(Objects.requireNonNull(IconaPedina.class.getResource("/img/chess.png"))).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
        setTitle("Abbandona");
        setModal(true);
        setResizable(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font titolo = new Font("Segoe UI", Font.BOLD, 17);
        Font grassetto = new Font("Segoe UI", Font.BOLD, 13);

        JPanel centro = new JPanel();
        centro.setLayout(new BorderLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lbl = new JLabel("<html><div style='text-align:center;'>Sei sicuro di voler abbandonare la partita?</div></html>");
        lbl.setFont(titolo);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        centro.add(lbl, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JButton btnAbbandona = creaBtn("Abbandona", grassetto, new Color(180, 40, 40), new Color(210, 55, 55));
        JButton btnAnnulla = creaBtn("Annulla", grassetto, new Color(60, 60, 60), new Color(90, 90, 90));

        btnAbbandona.addActionListener(_ -> {
            SuoniScacchi.finePartita();
            confermato = true;
            dispose();
        });

        btnAnnulla.addActionListener(_ -> {
            SuoniScacchi.opzioniDialog();
            confermato = false;
            dispose();
        });

        JPanel bottoni = new JPanel(new GridLayout(1, 2, 10, 0));
        bottoni.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        bottoni.add(btnAnnulla);
        bottoni.add(btnAbbandona);
        add(bottoni, BorderLayout.SOUTH);

        setSize(400, 180);
        setMinimumSize(new Dimension(400, 180));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crea un pulsante stilizzato con effetto hover.
     * <p>
     * Il pulsante non mostra bordo né indicatore di focus, ha il cursore
     * a forma di mano e cambia colore di sfondo al passaggio del mouse.
     * </p>
     *
     * @param testo il testo da visualizzare sul pulsante
     * @param f     il font da applicare al testo
     * @param norm  il colore di sfondo nella stato normale
     * @param hover il colore di sfondo quando il cursore è sopra il pulsante
     * @return il pulsante configurato
     */
    private JButton creaBtn(String testo, Font f, Color norm, Color hover) {
        JButton btn = new JButton(testo);
        btn.setFont(f);
        btn.setBackground(norm);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(norm);
            }
        });
        return btn;
    }

    /**
     * Indica se l'utente ha confermato l'abbandono della partita.
     * <p>
     * Il valore è significativo solo dopo la chiusura del dialog.
     * </p>
     *
     * @return {@code true} se l'utente ha premuto "Abbandona",
     *         {@code false} se ha premuto "Annulla" o chiuso il dialog
     */
    public boolean isConfermato() {
        return confermato;
    }
}