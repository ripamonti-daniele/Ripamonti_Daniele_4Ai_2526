import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;

public class Gestore {
    private Queue<String> codaNormali;
    private Queue<String> codaPrioritari;
    private int progressivoNormali;
    private int progressivoPrioritari;
    private int CCN;
    private LogChiamate log;

    public Gestore(LogChiamate log) {
        this.log = log;
        codaNormali = new LinkedList<>();
        codaPrioritari = new LinkedList<>();
        progressivoNormali = 0;
        progressivoPrioritari = 0;
        CCN = 0;
    }

    public String emettiBigliettoNormale() {
        String codice = "N" + progressivoNormali++;
        codaNormali.add(codice);
        return codice;
    }

    public String emettiBigliettoPrioritario() {
        String codice = "P" + progressivoPrioritari++;
        codaPrioritari.add(codice);
        return codice;
    }

    public String chiamaBiglietto(String idCassa) {
        String codice = null;

        if (!codaPrioritari.isEmpty() && CCN >= 2) {
            codice = codaPrioritari.poll();
            CCN = 0;
        }
        else if (!codaNormali.isEmpty()) {
            codice = codaNormali.poll();
            CCN++;
        }
        else if (!codaPrioritari.isEmpty()) {
            codice = codaPrioritari.poll();
            CCN = 0;
        }

        if (codice != null) {
            log.aggiungiChiamata(codice, idCassa, new Timestamp(System.currentTimeMillis()));
        }

        return codice;
    }

    public List<String> getBigliettiInAttesa() {
        List<String> lista = new ArrayList<>();
        lista.addAll(codaNormali);
        lista.addAll(codaPrioritari);
        return lista;
    }
}
