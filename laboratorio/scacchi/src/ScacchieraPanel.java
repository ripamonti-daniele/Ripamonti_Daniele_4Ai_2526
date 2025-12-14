import javax.swing.*;

public class ScacchieraPanel extends JPanel {
    private final Casella[][] casellePanel;
    private final Pedina[][] scacchiera;
    public final int DIMENSIONE = 8;

    public ScacchieraPanel(Pedina[][] scacchiera) {
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        this.scacchiera = scacchiera;
    }

//    private void
}
