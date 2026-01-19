import java.util.HashMap;
import java.util.Map;

public class DataEstesa extends DataFormattata {
    private final Map<Integer, String> converti_mese = new HashMap<>();

    public DataEstesa(int giorno, int mese, int anno){
        super(giorno, mese, anno);
        converti_mese.put(1, "Gennaio");
        converti_mese.put(2, "Febbraio");
        converti_mese.put(3, "Marzo");
        converti_mese.put(4, "Aprile");
        converti_mese.put(5, "Maggio");
        converti_mese.put(6, "Giugno");
        converti_mese.put(7, "Luglio");
        converti_mese.put(8, "Agosto");
        converti_mese.put(9, "Settembre");
        converti_mese.put(10, "Ottobre");
        converti_mese.put(11, "Novembre");
        converti_mese.put(12, "Dicembre");
    }

    @Override
    public String stringaFormattata() {
        String a;
        String g;
        if (anno < 100) a = String.valueOf(anno + 2000);
        else a = String.valueOf(anno);
        if (giorno < 10) g = "0" + giorno;
        else g = String.valueOf(giorno);

        return g + " " + converti_mese.get(mese) + " " + a;
    }
}
