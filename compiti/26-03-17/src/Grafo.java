import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grafo {
    private final List<Nodo> nodi;

    public Grafo() {
        nodi = new ArrayList<>();
    }

    public Grafo(List<Nodo> nodi) {
        if (nodi == null) throw new IllegalArgumentException("I nodi non possono essere null");
        this.nodi = nodi;
    }

    public Grafo(Nodo nodo) {
        this();
        if (nodo == null) throw new IllegalArgumentException("Il nodo non può essere null");
        nodi.add(nodo);
    }

    public void inserisciNodo(Nodo n, Map<String, Integer> adiacenze) {
        if (nodi.isEmpty()) nodi.add(n);
        else {
            boolean aggiunto = false;
            for (Nodo nodo : nodi) {
                if (adiacenze.containsKey(nodo.getNome())) {
                    nodo.aggiungiNodo(n, adiacenze.get(nodo.getNome()));
                    n.aggiungiNodo(nodo, adiacenze.get(nodo.getNome()));
                    aggiunto = true;
                }
            }
            if (aggiunto) nodi.add(n);
        }
    }

    public List<String> getNomeNodi() {
        List<String> nomi = new ArrayList<>();
        for (Nodo n : nodi) nomi.add(n.getNome());
        return nomi;
    }

    @Override
    public String toString() {
        String strNodi = "";
        for (Nodo n : nodi) strNodi += n.toString() + "\n";
        return strNodi;
    }
}
