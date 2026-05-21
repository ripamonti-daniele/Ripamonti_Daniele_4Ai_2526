import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottoneOpzioni extends JButton {
    private int tipo;
    private boolean abilitato;
    private boolean hover;
    private int dimensione;

    public BottoneOpzioni(int tipo, int x, int y, int dimensione) {
        super();
        setDimensione(dimensione);
        setTipo(tipo);
        abilitato = true;
        setBounds(x, y, dimensione, dimensione);
        impostaImmagine(dimensione - dimensione / 10, tipo);
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
                    impostaImmagine(dimensione, getTipo());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                impostaImmagine(dimensione - dimensione / 10, getTipo());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (abilitato && SwingUtilities.isLeftMouseButton(e)) impostaImmagine(dimensione - dimensione / 5, getTipo());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover && abilitato) impostaImmagine(dimensione, getTipo());
                else {
                    impostaImmagine(dimensione - dimensione / 10, getTipo());
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    private void impostaImmagine(int dimensione, int tipo) {
        switch (tipo) {
            case 1 -> setIcon(creaIconaScalata("img/opzioni/frecciaStart.png", dimensione));
            case 2 -> setIcon(creaIconaScalata("img/opzioni/frecciaSx.png", dimensione));
            case 3 -> setIcon(creaIconaScalata("img/opzioni/frecciaDx.png", dimensione));
            case 4 -> setIcon(creaIconaScalata("img/opzioni/frecciaEnd.png", dimensione));
            case 5 -> setIcon(creaIconaScalata("img/opzioni/frecceRotazione.png", dimensione));
            case 6 -> setIcon(creaIconaScalata("img/opzioni/bandiera.png", dimensione));
            case 7 -> setIcon(creaIconaScalata("img/opzioni/sound_on.png", dimensione));
            case 8 -> setIcon(creaIconaScalata("img/opzioni/sound_off.png", dimensione));
            default -> {}
        }
    }

    public void impostaImmagine(int tipo) {
        setTipo(tipo);
        impostaImmagine(dimensione, tipo);
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

    public int getDimensione() {
        return dimensione;
    }

    public void setDimensione(int dimensione) {
        if (dimensione <= 0) throw new IllegalArgumentException("La dimensione deve essere maggiore di 0");
        this.dimensione = dimensione;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        if (tipo < 1 || tipo > 8) throw new IllegalArgumentException("Tipo di immagine non valido (min 1 max 8)");
        this.tipo = tipo;
    }
}