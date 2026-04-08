import javax.swing.ImageIcon;
import java.awt.*;

public enum IconaPedina {
    RE_WHITE(Color.WHITE,  "img/pedine/re_white.png"),
    REGINA_WHITE(Color.WHITE, "img/pedine/regina_white.png"),
    TORRE_WHITE(Color.WHITE,  "img/pedine/torre_white.png"),
    ALFIERE_WHITE(Color.WHITE,"img/pedine/alfiere_white.png"),
    CAVALLO_WHITE(Color.WHITE,"img/pedine/cavallo_white.png"),
    PEDONE_WHITE(Color.WHITE,  "img/pedine/pedone_white.png"),

    RE_BLACK(Color.BLACK,  "img/pedine/re_black.png"),
    REGINA_BLACK(Color.BLACK, "img/pedine/regina_black.png"),
    TORRE_BLACK(Color.BLACK,  "img/pedine/torre_black.png"),
    ALFIERE_BLACK(Color.BLACK,"img/pedine/alfiere_black.png"),
    CAVALLO_BLACK(Color.BLACK,"img/pedine/cavallo_black.png"),
    PEDONE_BLACK(Color.BLACK,  "img/pedine/pedone_black.png");

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
        ImageIcon icon = new ImageIcon(iconPath);
        if (dimensione <= 0) return icon;
        if (icon.getIconWidth() == dimensione && icon.getIconHeight() == dimensione) return icon;
        Image image = icon.getImage().getScaledInstance(dimensione, dimensione, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public ImageIcon getImageIcon() {
        return new ImageIcon(iconPath);
    }
}
