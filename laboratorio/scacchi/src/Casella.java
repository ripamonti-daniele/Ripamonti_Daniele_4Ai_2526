import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;

public class Casella extends JPanel {
    private Color colore;
    private JLabel label;
    private ImageIcon img;

    public Casella(Color colore, int lunghezzaLato) {
        this.setSize(lunghezzaLato, lunghezzaLato);
        this.add(label);
        setColore(colore);
    }

    public Casella(Color colore, int lunghezzaLato, ImageIcon img) {
        this(colore, lunghezzaLato);
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
        if (img == ScacchieraLabel.RE) label.setIcon(ScacchieraLabel.RE);
        else if (img == ScacchieraLabel.REGINA) label.setIcon(ScacchieraLabel.REGINA);
        else if (img == ScacchieraLabel.TORRE) label.setIcon(ScacchieraLabel.TORRE);
        else if (img == ScacchieraLabel.ALFIERE) label.setIcon(ScacchieraLabel.ALFIERE);
        else if (img == ScacchieraLabel.CAVALLO) label.setIcon(ScacchieraLabel.CAVALLO);
        else if (img == ScacchieraLabel.PEDONE) label.setIcon(ScacchieraLabel.PEDONE);
        else throw new IllegalArgumentException("Immagine non valida");
    }

    public void rimuoviImg() {
        label.setIcon(null);
    }

    @Override
    public void setSize(Dimension dimensione) {
        if (dimensione.width != dimensione.height) throw new IllegalArgumentException("La casella deve essere un quadrato");
        this.resize(dimensione);
    }

    @Override
    public void setSize(int width, int height) {
        if (width != height) throw new IllegalArgumentException("La casella deve essere un quadrato");
        this.resize(width, height);
    }

    //fai setbounds
}
