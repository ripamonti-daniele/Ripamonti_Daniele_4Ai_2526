import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class CasellaOld extends JPanel implements MouseListener {
    private Color colore;
    private final JLabel label;
    private int lunghezzaLato;
    private String id;
    private static String casellaSelezionata = null;
    private static final List<String> idUtilizzati = new ArrayList<>();
    private casellaClickListener listener;
    public boolean mossaValida;

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

    public CasellaOld(Color colore, int lunghezzaLato, String id) {
        label = new JLabel();
        setLunghezzaLato(lunghezzaLato);
        setColore(colore);
        setId(id);
        this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
        this.add(label);
        this.addMouseListener(this);
        mossaValida = false;
    }

    public CasellaOld(Color colore, int lunghezzaLato, String id, ImageIcon img) {
        this(colore, lunghezzaLato, id);
        setImg(img);
    }

    public CasellaOld(CasellaOld originale) {
        this.colore = originale.colore;
        this.lunghezzaLato = originale.lunghezzaLato;
        this.label = originale.label;
        this.id = originale.id;
        this.listener = originale.listener;
        this.mossaValida = originale.mossaValida;
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

    private void setColore(Color colore) {
        this.colore = colore;
        this.setBackground(colore);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        id = id.trim().toUpperCase();
        if (idUtilizzati.contains(id)) throw new IllegalArgumentException("Id " + id + " già in uso");
        if (!id.matches("[A-H][1-8]")) throw new IllegalArgumentException("Formato id non valido (esempio corretto: A1)");
        this.id = id;
        idUtilizzati.add(this.id);
    }

    public static String getIdCasellaSelezionata() {
        return casellaSelezionata;
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
            Image scaled = img.getImage().getScaledInstance(lunghezzaLato, lunghezzaLato, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        }
        else label.setIcon(img);
    }

    public void rimuoviImg() {
        label.setIcon(null);
    }

    public void setListener(casellaClickListener l) {
        this.listener = l;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        casellaSelezionata = id;
        if (listener != null) listener.casellaCliccata();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        if (this.id.equals(casellaSelezionata) && this.label.getIcon() != null) g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        else if (mossaValida) {
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if (this.label.getIcon() != null) g2d.drawOval(lunghezzaLato / 25, lunghezzaLato / 25, lunghezzaLato - 2 * lunghezzaLato / 25 - 1, lunghezzaLato - 2 * lunghezzaLato / 25 - 1);
            else g2d.fillOval(lunghezzaLato / 4, lunghezzaLato / 4, lunghezzaLato / 2, lunghezzaLato / 2);
            g2d.setComposite(old);
        }
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
