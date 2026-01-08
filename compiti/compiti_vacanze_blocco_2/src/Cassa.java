import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cassa {
    private final Map<String, Float> conti;

    public Cassa() {
        conti = new HashMap<>();
    }

    public void creaConto(Tavolo t) {
        float prezzo = 0;
        List<Pizza> ordini = t.getOrdini();
        for (Pizza p : ordini) prezzo += p.getCosto();
        conti.put(t.getId(), prezzo);
    }

    public float getConto(String idTavolo) {
        if (!conti.containsKey(idTavolo)) throw new IllegalArgumentException("Tavolo non valido");
        float conto = conti.get(idTavolo);
        conti.remove(idTavolo);
        return conto;
    }
}

