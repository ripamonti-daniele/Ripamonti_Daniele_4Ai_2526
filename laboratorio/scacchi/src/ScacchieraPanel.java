public class ScacchieraPanel {
    private final Casella[][] casellePanel;
    private final Pedina[][] scacchiera;
    private final int DIMENSIONE = 8;

    public ScacchieraPanel() {
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        scacchiera = new Pedina[DIMENSIONE][DIMENSIONE];
    }

}
