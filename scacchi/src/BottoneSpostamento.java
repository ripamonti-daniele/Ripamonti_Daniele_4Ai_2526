import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottoneSpostamento extends JButton {
    private final int tipo;
    private boolean abilitato;
    private boolean hover;

    public BottoneSpostamento(int tipo, int x, int y, int dimensione) {
        super();
        this.tipo = tipo;
        abilitato = true;
        setBounds(x, y, dimensione, dimensione);
        impostaImmagine(dimensione - dimensione / 10);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                if (abilitato) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    impostaImmagine(dimensione);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                impostaImmagine(dimensione - dimensione / 10);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (abilitato && SwingUtilities.isLeftMouseButton(e)) impostaImmagine(dimensione - dimensione / 5);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover && abilitato) impostaImmagine(dimensione);
                else {
                    impostaImmagine(dimensione - dimensione / 10);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    private void impostaImmagine(int dimensione) {
        switch (tipo) {
            case 1 -> setIcon(creaIconaScalata("img/frecce/frecceRotazione.png", dimensione));
            case 2 -> setIcon(creaIconaScalata("img/frecce/frecciaStart.png", dimensione));
            case 3 -> setIcon(creaIconaScalata("img/frecce/frecciaSx.png", dimensione));
            case 4 -> setIcon(creaIconaScalata("img/frecce/frecciaDx.png", dimensione));
            case 5 -> setIcon(creaIconaScalata("img/frecce/frecciaEnd.png", dimensione));
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