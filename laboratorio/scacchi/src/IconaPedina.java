import javax.swing.ImageIcon;
import java.awt.*;

public enum IconaPedina {
    RE_WHITE(Color.WHITE,  "img/re_white.png"),
    REGINA_WHITE(Color.WHITE, "img/regina_white.png"),
    TORRE_WHITE(Color.WHITE,  "img/torre_white.png"),
    ALFIERE_WHITE(Color.WHITE,"img/alfiere_white.png"),
    CAVALLO_WHITE(Color.WHITE,"img/cavallo_white.png"),
    PEDONE_WHITE(Color.WHITE,  "img/pedone_white.png"),

    RE_BLACK(Color.BLACK,  "img/re_black.png"),
    REGINA_BLACK(Color.BLACK, "img/regina_black.png"),
    TORRE_BLACK(Color.BLACK,  "img/torre_black.png"),
    ALFIERE_BLACK(Color.BLACK,"img/alfiere_black.png"),
    CAVALLO_BLACK(Color.BLACK,"img/cavallo_black.png"),
    PEDONE_BLACK(Color.BLACK,  "img/pedone_black.png");

    private final Color colore;
    private final String iconPath;

    IconaPedina(Color colore, String iconPath) {
        this.colore = colore;
        this.iconPath = iconPath;
    }

    public Color getColore() {
        return colore;
    }

    public String getPath() {
        return iconPath;
    }

    public ImageIcon getImageIcon(int dimensione) {
        Image image = new ImageIcon(iconPath).getImage().getScaledInstance(dimensione, dimensione, Image.SCALE_SMOOTH);
        ImageIcon img = new ImageIcon(image);
        img.setDescription(iconPath);
        return img;
    }
}
