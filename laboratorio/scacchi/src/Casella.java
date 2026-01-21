import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Casella extends JPanel implements MouseListener {
    private Color colore;
    private final JLabel label;
    private static Casella casellaSeleziona = null;
    private int lunghezzaLato;

//    private static final ImageIcon[] iconeValide = new ImageIcon[] {
//        IconaPedina.RE_WHITE.getImageIcon(),
//        IconaPedina.REGINA_WHITE.getImageIcon(),
//        IconaPedina.TORRE_WHITE.getImageIcon(),
//        IconaPedina.ALFIERE_WHITE.getImageIcon(),
//        IconaPedina.CAVALLO_WHITE.getImageIcon(),
//        IconaPedina.PEDONE_WHITE.getImageIcon(),
//        IconaPedina.RE_BLACK.getImageIcon(),
//        IconaPedina.REGINA_BLACK.getImageIcon(),
//        IconaPedina.TORRE_BLACK.getImageIcon(),
//        IconaPedina.ALFIERE_BLACK.getImageIcon(),
//        IconaPedina.CAVALLO_BLACK.getImageIcon(),
//        IconaPedina.PEDONE_BLACK.getImageIcon(),
//    };

    public Casella(Color colore, int lunghezzaLato, String id) {
        label = new JLabel();
        setLunghezzaLato(lunghezzaLato);
        this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
        this.add(label);
        setColore(colore);
        this.addMouseListener(this);
    }

    public Casella(Color colore, int lunghezzaLato, String id, ImageIcon img) {
        this(colore, lunghezzaLato, id);
        setImg(img);
    }

    public int getLunghezzaLato() {
        return lunghezzaLato;
    }

    private void setLunghezzaLato(int lunghezzaLato) {
        if (lunghezzaLato <= 0) throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
        this.lunghezzaLato = lunghezzaLato;
    }

    public Color getColore() {
        return colore;
    }

    public void setColore(Color colore) {
        this.colore = colore;
        this.setBackground(colore);
    }

    public Icon getImg() {
        return label.getIcon();
    }

    public void setImg(ImageIcon img) {
//        boolean trovato = false;
//        for (ImageIcon i : iconeValide) {
//            if (i.getDescription().equals(img.getDescription())) {
//                trovato = true;
//                break;
//            }
//        }
        //risolvi il problema della perdita di dati nel passaggio da ImageIcon a Image
//        if (!trovato) throw new IllegalArgumentException("Immagine non valida");

        if (img.getIconWidth() != lunghezzaLato || img.getIconHeight() != lunghezzaLato) {
            Image scaled = img.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
            System.out.println("borno");
        }
        else label.setIcon(img);
    }

    public void rimuoviImg() {
        label.setIcon(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (casellaSeleziona != null && casellaSeleziona != this) {
            setImg((ImageIcon) casellaSeleziona.getImg());
            casellaSeleziona.rimuoviImg();
            casellaSeleziona = null;
        }
        else if (getImg() != null) {
            casellaSeleziona = this;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

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
