import java.util.List;
import java.util.Map;

public class Cassa {
    private Map<String, Float> conti;

    public void creaConto(Tavolo t) {
        float prezzo = 0;
        List<Pizza> ordini = t.getOrdini();
        for (Pizza p : ordini) prezzo += p.getCosto();
        conti.put(t.getId(), prezzo);
    }

    public float getConto(String idTavolo) {

        return 0;
        //toglie il tavolo dal dizionario
    }
}

