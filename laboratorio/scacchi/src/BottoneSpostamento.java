import javax.swing.*;
import java.awt.*;

public class BottoneSpostamento extends JButton {
    private final int tipo;
    private Timer timer;
    private int step = 0;
    private boolean abilitato;

    public BottoneSpostamento(int tipo, int x, int y, int dimensione) {
        super();
        this.tipo = tipo;
        abilitato = true;
        setBounds(x, y, dimensione, dimensione);
        impostaImmagine(dimensione);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        timer = new Timer(60, e -> {
            step++;
            if (step == 1) impostaImmagine(dimensione - dimensione / 10);
            else if (step == 2) {
                impostaImmagine(dimensione);
                timer.stop();
                step = 0;
            }
        });

        addActionListener(e -> {
            if (abilitato) timer.start();
        });
    }

    private void impostaImmagine(int dimensione) {
        switch (tipo) {
            case 1 -> setIcon(creaIconaScalata("frecce/frecciaStart.png", dimensione));
            case 2 -> setIcon(creaIconaScalata("frecce/frecciaSx.png", dimensione));
            case 3 -> setIcon(creaIconaScalata("frecce/frecciaDx.png", dimensione));
            case 4 -> setIcon(creaIconaScalata("frecce/frecciaEnd.png", dimensione));
            default -> {}
        }
    }

    private ImageIcon creaIconaScalata(String percorso, int dimensione) {
        ImageIcon icon = new ImageIcon(percorso);
        Image img = icon.getImage().getScaledInstance(dimensione, dimensione, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void abilita() {
        abilitato = true;
    }

    public void disabilita() {
        abilitato = false;
    }

    public boolean isAbilitato() {
        return abilitato;
    }
}