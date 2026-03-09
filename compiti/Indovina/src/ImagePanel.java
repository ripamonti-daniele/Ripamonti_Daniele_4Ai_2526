import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends Panel {

    private BufferedImage immagine;

    public ImagePanel(int w, int h) {
        setPreferredSize(new Dimension(w, h));
    }

    public void setImage(BufferedImage img) {
        immagine = img;
        repaint(); // aggiorna automaticamente
    }

    public void setImage(Image img) {

        //da Image a BufferedImage
        immagine = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = immagine.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        repaint(); // aggiorna automaticamente
    }

    public BufferedImage getImage() {
        return immagine;
    }

    @Override
    public void paint(Graphics g) {
        if (immagine != null) {
            g.drawImage(immagine, 0, 0, this);
        }
    }

    //serve per evitare lo sfarfallio (in AWT puro)
    @Override
    public void update(Graphics g) {
        paint(g);
    }
}