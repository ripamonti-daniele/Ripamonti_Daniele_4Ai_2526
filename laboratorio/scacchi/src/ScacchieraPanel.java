import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ScacchieraPanel extends JPanel {
    private final Casella[][] casellePanel;
    private final Scacchiera scacchiera;
    public final int DIMENSIONE = 8;
    private ImageIcon pedoneW;
    private ImageIcon alfiereW;
    private ImageIcon cavalloW;
    private ImageIcon torreW;
    private ImageIcon reginaW;
    private ImageIcon reW;
    private ImageIcon pedoneB;
    private ImageIcon alfiereB;
    private ImageIcon cavalloB;
    private ImageIcon torreB;
    private ImageIcon reginaB;
    private ImageIcon reB;

    private final Map<Integer, String> numeroToLettera = new HashMap<>();

    public ScacchieraPanel(Scacchiera scacchiera, ImageIcon pedoneW, ImageIcon alfiereW, ImageIcon cavalloW, ImageIcon torreW, ImageIcon reginaW, ImageIcon reW, ImageIcon pedoneB, ImageIcon alfiereB, ImageIcon cavalloB, ImageIcon torreB, ImageIcon reginaB, ImageIcon reB) {
        numeroToLettera.put(1, "A");
        numeroToLettera.put(2, "B");
        numeroToLettera.put(3, "C");
        numeroToLettera.put(4, "D");
        numeroToLettera.put(5, "E");
        numeroToLettera.put(6, "F");
        numeroToLettera.put(7, "G");
        numeroToLettera.put(8, "H");

        setPedoneW(pedoneW);
        setAlfiereW(alfiereW);
        setCavalloW(cavalloW);
        setTorreW(torreW);
        setReginaW(reginaW);
        setReW(reW);
        setPedoneB(pedoneB);
        setAlfiereB(alfiereB);
        setCavalloB(cavalloB);
        setTorreB(torreB);
        setReginaB(reginaB);
        setReB(reB);

        this.scacchiera = scacchiera;
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        inizializza();
        aggiornaScacchiera(scacchiera.getScacchiera());
    }

    public ScacchieraPanel(Scacchiera scacchiera) {
        this(scacchiera, IconaPedina.PEDONE_WHITE.getImageIcon(100),
                         IconaPedina.ALFIERE_WHITE.getImageIcon(100),
                         IconaPedina.CAVALLO_WHITE.getImageIcon(100),
                         IconaPedina.TORRE_WHITE.getImageIcon(100),
                         IconaPedina.REGINA_WHITE.getImageIcon(100),
                         IconaPedina.RE_WHITE.getImageIcon(100),
                         IconaPedina.PEDONE_BLACK.getImageIcon(100),
                         IconaPedina.ALFIERE_BLACK.getImageIcon(100),
                         IconaPedina.CAVALLO_BLACK.getImageIcon(100),
                         IconaPedina.TORRE_BLACK.getImageIcon(100),
                         IconaPedina.REGINA_BLACK.getImageIcon(100),
                         IconaPedina.RE_BLACK.getImageIcon(100));
    }

    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                Color c;
                if ((j + i) % 2 == 0) c = new Color(240, 217, 181);
                else c = new Color(161, 116, 79);

                casellePanel[i][j] = new Casella(c, 100, numeroToLettera.get(j + 1) + (DIMENSIONE - i));
                casellePanel[i][j].setBounds(100 * j, 100 * i,100, 100);
                setListener(i, j);
            }
        }
    }

    private void setImgCasella(Color col, Casella c, ImageIcon imgW, ImageIcon imgB) {
        if (col.equals(Color.white)) c.setImg(imgW);
        else c.setImg(imgB);
    }

    public void aggiornaScacchiera(Pedina[][] scacchiera) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                Pedina p = scacchiera[i][j];
                Casella c = casellePanel[i][j];
                if (p == null) c.rimuoviImg();
                else {
                    Color col = p.getColore();
                    switch (p) {
                        case Pedone _ -> setImgCasella(col, c, pedoneW, pedoneB);
                        case Alfiere _ -> setImgCasella(col, c, alfiereW, alfiereB);
                        case Torre _ -> setImgCasella(col, c, torreW, torreB);
                        case Regina _ -> setImgCasella(col, c, reginaW, reginaB);
                        case Re _ -> setImgCasella(col, c, reW, reB);
                        case Cavallo _ -> setImgCasella(col, c, cavalloW, cavalloB);
                        default -> throw new IllegalStateException("Tipo pedina non valido: " + p.getClass().getSimpleName());
                    }
                }
            }
        }
    }

    public JPanel[][] getCasellePanel() {
        Casella[][] copia = new Casella[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                copia[i][j] = new Casella(casellePanel[i][j]);
            }
        }
        return copia;
    }

    private void setListener(int y, int x) {
        casellePanel[y][x].setListener(() -> {
            resetMosseValide();

            if (scacchiera.getCasella_selezionata() == null || !scacchiera.muoviPedina(new int[]{y, x})) { //se la casella selezionata non è null allora seleziona la pedina; se è null prova a spostarla e se non riesce seleziona la pedina dove si intendeva spostare quella selezionata precedentemente
                List<int[]> mosseValide = scacchiera.selezionaPedina(new int[]{y, x}, scacchiera.getTurno());
                if (mosseValide != null) mostraMosseValide(mosseValide);
            }
            else scacchiera.cambiaTurno();

            aggiornaScacchiera(scacchiera.getScacchiera());
            disegna();
        });
    }

    public void mettiASchermo(JPanel panel) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                panel.add(casellePanel[i][j]);
            }
        }
    }

    private void resetMosseValide() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.mossaValida = false;
        }
    }

    private void mostraMosseValide(List<int[]> mosseValide) {
        if (mosseValide == null) throw new IllegalArgumentException("Le mosse valide non possono essere null");
        for (int[] m : mosseValide) {
            casellePanel[m[0]][m[1]].mossaValida = true;
        }
    }

    private void disegna() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.repaint();
        }
    }

    private void controllaImmagini(ImageIcon daControllare, ImageIcon img1, ImageIcon img2, ImageIcon img3, ImageIcon img4, ImageIcon img5, ImageIcon img6, ImageIcon img7, ImageIcon img8, ImageIcon img9, ImageIcon img10, ImageIcon img11) {
        if (daControllare == null) throw new IllegalArgumentException("Le immagini delle pedine non possono essere null");
        if (daControllare == img1 || daControllare == img2 || daControllare == img3 || daControllare == img4 || daControllare == img5 || daControllare == img6 || daControllare == img7 || daControllare == img8 || daControllare == img9 || daControllare == img10 || daControllare == img11) throw new IllegalArgumentException("Le immagini delle pedine devono essere diverse fra loro");
    }

    public ImageIcon getPedoneW() {
        return new ImageIcon(pedoneW.getImage());
    }

    private void setPedoneW(ImageIcon pedoneW) {
        controllaImmagini(pedoneW, alfiereW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneW = pedoneW;
    }

    public ImageIcon getAlfiereW() {
        return new ImageIcon(alfiereW.getImage());
    }

    private void setAlfiereW(ImageIcon alfiereW) {
        controllaImmagini(alfiereW, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.alfiereW = alfiereW;
    }

    public ImageIcon getCavalloW() {
        return new ImageIcon(cavalloW.getImage());
    }

    private void setCavalloW(ImageIcon cavalloW) {
        controllaImmagini(cavalloW, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.cavalloW = cavalloW;
    }

    public ImageIcon getTorreW() {
        return new ImageIcon(torreW.getImage());
    }

    private void setTorreW(ImageIcon torreW) {
        controllaImmagini(torreW, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.torreW = torreW;
    }

    public ImageIcon getReginaW() {
        return new ImageIcon(reginaW.getImage());
    }

    private void setReginaW(ImageIcon reginaW) {
        controllaImmagini(reginaW, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reginaW = reginaW;
    }

    public ImageIcon getReW() {
        return new ImageIcon(reW.getImage());
    }

    private void setReW(ImageIcon reW) {
        controllaImmagini(reW, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reW = reW;
    }

    public ImageIcon getPedoneB() {
        return new ImageIcon(pedoneB.getImage());
    }

    private void setPedoneB(ImageIcon pedoneB) {
        controllaImmagini(pedoneB, alfiereW, cavalloW, torreW, reginaW, reW, pedoneW, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneB = pedoneB;
    }

    public ImageIcon getAlfiereB() {
        return new ImageIcon(alfiereB.getImage());
    }

    private void setAlfiereB(ImageIcon alfiereB) {
        controllaImmagini(alfiereB, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereW, cavalloB, torreB, reginaB, reB);
        this.alfiereB = alfiereB;
    }

    public ImageIcon getCavalloB() {
        return new ImageIcon(cavalloB.getImage());
    }

    private void setCavalloB(ImageIcon cavalloB) {
        controllaImmagini(cavalloB, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloW, torreB, reginaB, reB);
        this.cavalloB = cavalloB;
    }

    public ImageIcon getTorreB() {
        return new ImageIcon(torreB.getImage());
    }

    private void setTorreB(ImageIcon torreB) {
        controllaImmagini(torreB, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreW, reginaB, reB);
        this.torreB = torreB;
    }

    public ImageIcon getReginaB() {
        return new ImageIcon(reginaB.getImage());
    }

    private void setReginaB(ImageIcon reginaB) {
        controllaImmagini(reginaB, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaW, reB);
        this.reginaB = reginaB;
    }

    public ImageIcon getReB() {
        return new ImageIcon(reB.getImage());
    }

    private void setReB(ImageIcon reB) {
        controllaImmagini(reB, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reW);
        this.reB = reB;
    }

    private class Casella extends JPanel implements MouseListener {
        private Color colore;
        private final JLabel label;
        private int lunghezzaLato;
        private String id;
        private static String casellaSelezionata = null;
        private static final List<String> idUtilizzati = new ArrayList<>();
        private casellaClickListener listener;
        public boolean mossaValida;

        public Casella(Color colore, int lunghezzaLato, String id) {
            label = new JLabel();
            setLunghezzaLato(lunghezzaLato);
            setColore(colore);
            setId(id);
            this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
            this.add(label);
            this.addMouseListener(this);
            mossaValida = false;
        }

        public Casella(Color colore, int lunghezzaLato, String id, ImageIcon img) {
            this(colore, lunghezzaLato, id);
            setImg(img);
        }

        public Casella(Casella originale) {
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
            if (lunghezzaLato <= 0)
                throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
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
            if (!id.matches("[A-H][1-8]"))
                throw new IllegalArgumentException("Formato id non valido (esempio corretto: A1)");
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
            if (img.getIconWidth() != lunghezzaLato || img.getIconHeight() != lunghezzaLato) {
                Image scaled = img.getImage().getScaledInstance(lunghezzaLato, lunghezzaLato, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            } else label.setIcon(img);
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
            if (this.id.equals(casellaSelezionata) && this.label.getIcon() != null)
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            else if (mossaValida) {
                Composite old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                if (this.label.getIcon() != null)
                    g2d.drawOval(lunghezzaLato / 25, lunghezzaLato / 25, lunghezzaLato - 2 * lunghezzaLato / 25 - 1, lunghezzaLato - 2 * lunghezzaLato / 25 - 1);
                else g2d.fillOval(lunghezzaLato / 4, lunghezzaLato / 4, lunghezzaLato / 2, lunghezzaLato / 2);
                g2d.setComposite(old);
            }
        }
    }
}
