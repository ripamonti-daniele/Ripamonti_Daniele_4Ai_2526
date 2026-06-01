import javax.swing.ImageIcon;
import java.awt.*;
import java.util.Objects;

/**
 * Enumerazione delle icone grafiche associate a ciascuna pedina degli scacchi.
 * <p>
 * Ogni costante rappresenta una pedina specifica (tipo e colore) e mantiene
 * il riferimento al colore del giocatore e al percorso dell'immagine su disco.
 * </p>
 */
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

    /** Colore del giocatore a cui appartiene la pedina ({@link Color#WHITE} o {@link Color#BLACK}). */
    private final Color colore;

    /** Percorso relativo del file immagine associato alla pedina. */
    private final String iconPath;

    /**
     * Costruisce una costante {@code IconaPedina} con il colore e il percorso immagine specificati.
     *
     * @param colore   il colore del giocatore ({@link Color#WHITE} o {@link Color#BLACK})
     * @param iconPath il percorso relativo del file immagine della pedina
     */
    IconaPedina(Color colore, String iconPath) {
        this.colore = colore;
        this.iconPath = iconPath;
    }

    /**
     * Restituisce il colore del giocatore a cui appartiene la pedina.
     *
     * @return {@link Color#WHITE} per le pedine bianche, {@link Color#BLACK} per le nere
     */
    public Color getColore() {
        return colore;
    }

    /**
     * Restituisce il percorso relativo del file immagine associato alla pedina.
     *
     * @return il percorso del file immagine come stringa
     */
    public String getPath() {
        return iconPath;
    }

    /**
     * Restituisce l'icona della pedina ridimensionata alla dimensione specificata.
     * <p>
     * Se {@code dimensione} è ≤ 0, oppure l'immagine ha già esattamente
     * la dimensione richiesta, viene restituita l'icona originale senza scaling.
     * </p>
     *
     * @param dimensione la larghezza e l'altezza in pixel dell'icona risultante
     * @return una {@link ImageIcon} delle dimensioni richieste, o l'icona originale
     *         se {@code dimensione} è ≤ 0 o il ridimensionamento non è necessario
     */
    public ImageIcon getImageIcon(int dimensione) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(IconaPedina.class.getResource("/" + iconPath)));
        if (dimensione <= 0) return icon;
        if (icon.getIconWidth() == dimensione && icon.getIconHeight() == dimensione) return icon;
        Image image = icon.getImage().getScaledInstance(dimensione, dimensione, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    /**
     * Restituisce l'icona della pedina nelle dimensioni originali del file immagine.
     *
     * @return una {@link ImageIcon} caricata direttamente dal percorso del file,
     *         senza alcun ridimensionamento
     */
    public ImageIcon getImageIcon() {
        return new ImageIcon(iconPath);
    }
}