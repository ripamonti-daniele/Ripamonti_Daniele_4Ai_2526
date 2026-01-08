import java.util.List;

public class Tavolo {
    public final int POSTIMASSIMI = 4;
    private static int numeroTavolo = 1;
    public final String id;
    private List<Pizza> ordini;
    private boolean stato;

    public Tavolo(List<Pizza> ordini) {
        stato = true;
        id = "Tavolo" + numeroTavolo;
        numeroTavolo++;
    }

    public void chiudi() {
        stato = false;
    }

}
