import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogAbbandona extends JDialog {

    private boolean confermato = false;

    public DialogAbbandona() {
        super();
        setIconImage(new ImageIcon(new ImageIcon("img/chess.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
        setTitle("Abbandona");
        setModal(true);
        setResizable(false);
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

        JButton btnSi = creaBtn("Sì", grassetto, new Color(180, 40, 40), new Color(210, 55, 55), Color.white);
        JButton btnAnnulla = creaBtn("Annulla", grassetto, new Color(60, 60, 60), new Color(90, 90, 90), Color.white);

        btnSi.addActionListener(_ -> {
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
        bottoni.add(btnSi);

        add(bottoni, BorderLayout.SOUTH);

        setSize(400, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton creaBtn(String testo, Font f, Color norm, Color hover, Color fg) {
        JButton btn = new JButton(testo);
        btn.setFont(f);
        btn.setBackground(norm);
        btn.setForeground(fg);
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

    public boolean isConfermato() {
        return confermato;
    }
}