import java.util.List;

public class Cameriere {
    private final Pizzaiolo pizzaiolo;
    private final Cassa cassa;

    public Cameriere(Pizzaiolo pizzaiolo, Cassa cassa) {
        this.pizzaiolo = pizzaiolo;
        this.cassa = cassa;
    }

    public void prendiOrdine(Tavolo t) {
        if (!t.getStato()) throw new IllegalArgumentException("Impossibile prendere l'ordine: il tavolo è chiuso");
        List<Pizza> ordini = t.getOrdini();
        pizzaiolo.aggiungiPizze(ordini);
        cassa.creaConto(t);
        t.chiudi();
    }
}
