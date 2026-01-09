import java.util.ArrayList;
import java.util.List;

public class Tavolo {
    private static final int POSTIMASSIMI = 4;
    private int persone;
    private static int numeroTavolo = 1;
    public final String id;
    private final List<Pizza> ordini;
    private boolean stato;

    public Tavolo(int persone, List<Pizza> ordini) {
        stato = true;
        id = "Tavolo" + numeroTavolo;
        numeroTavolo++;
        setPersoneSedute(persone);
        this.ordini = ordini;
    }

    public static int getPostiMassimi() {
        return POSTIMASSIMI;
    }

    public String getId() {
        return id;
    }

    public int getPersoneSedute() {
        return persone;
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

    @Override
    public String toString() {
        String statoStringa = "aperto";
        if (!stato) statoStringa = "chiuso";

        String ordiniStringa = "";
        for (Pizza p : ordini) {
            ordiniStringa += p.getNome() + ", ";
        }
        ordiniStringa = ordiniStringa.substring(0, ordiniStringa.length() - 2);

        return id + " - " + statoStringa + ", " + persone + " persone, ordine: " + ordiniStringa;
    }
}
