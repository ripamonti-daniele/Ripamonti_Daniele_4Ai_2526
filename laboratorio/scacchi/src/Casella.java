import javax.swing.*;
import java.awt.*;

public class Casella extends JPanel {
    private Color colore;
    private final JLabel label;
    private ImageIcon img;
    private static final ImageIcon[] iconeValide = new ImageIcon[] {
        IconaPedina.RE_WHITE.getImageIcon(),
        IconaPedina.REGINA_WHITE.getImageIcon(),
        IconaPedina.TORRE_WHITE.getImageIcon(),
        IconaPedina.ALFIERE_WHITE.getImageIcon(),
        IconaPedina.CAVALLO_WHITE.getImageIcon(),
        IconaPedina.PEDONE_WHITE.getImageIcon(),
        IconaPedina.RE_BLACK.getImageIcon(),
        IconaPedina.REGINA_BLACK.getImageIcon(),
        IconaPedina.TORRE_BLACK.getImageIcon(),
        IconaPedina.ALFIERE_BLACK.getImageIcon(),
        IconaPedina.CAVALLO_BLACK.getImageIcon(),
        IconaPedina.PEDONE_BLACK.getImageIcon(),
    };

    public Casella(Color colore, int lunghezzaLato, String id) {
        label = new JLabel();
        this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
        this.add(label);
        setColore(colore);
    }

    public Casella(Color colore, int lunghezzaLato, String id, ImageIcon img) {
        this(colore, lunghezzaLato, id);
        setImg(img);
    }

    public Color getColore() {
        return colore;
    }

    public void setColore(Color colore) {
        this.colore = colore;
        this.setBackground(colore);
    }

    public ImageIcon getImg() {
        return img;
    }

    public void setImg(ImageIcon img) {
        boolean trovato = false;
        for (ImageIcon i : iconeValide) {
            if (i.getDescription().equals(img.getDescription())) {
                trovato = true;
                break;
            }
        }

        if (!trovato) throw new IllegalArgumentException("Immagine non valida");

        Image scaled = img.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaled));
    }

    public void rimuoviImg() {
        label.setIcon(null);
    }

//    @Override
//    public void setSize(Dimension dimensione) {
//        if (dimensione.width != dimensione.height) throw new IllegalArgumentException("La casella deve essere un quadrato");
//        this.resize(dimensione);
//    }
//
//    @Override
//    public void setSize(int width, int height) {
//        if (width != height) throw new IllegalArgumentException("La casella deve essere un quadrato");
//        this.resize(width, height);
//    }

    //fai setbounds
}
