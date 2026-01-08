import java.util.ArrayList;
import java.util.List;

public class Tavolo {
    public final int POSTIMASSIMI = 4;
    private int persone;
    private static int numeroTavolo = 1;
    public final String id;
    private List<Pizza> ordini;
    private boolean stato;

    public Tavolo(int persone, List<Pizza> ordini) {
        stato = true;
        id = "Tavolo" + numeroTavolo;
        numeroTavolo++;
        setPersoneSedute(persone);
        this.ordini = ordini;
    }

    public String getId() {
        return id;
    }

    private void setPersoneSedute(int persone) {
        if (persone > POSTIMASSIMI) throw new IllegalArgumentException("Il numero massimo di posti è " + POSTIMASSIMI);
        this.persone = persone;
    }

    public void chiudi() {
        stato = false;
    }

    public boolean getStato() {
        return stato;
    }

    public List<Pizza> getOrdini() {
        List<Pizza> copia = new ArrayList<>();

        for (Pizza p : ordini) {
            copia.add(new Pizza(p));
        }
        return copia;
    }

}
