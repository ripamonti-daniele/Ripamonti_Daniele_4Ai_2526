import java.util.HashMap;
import java.util.Map;

public class Nodo {
    private String nome;
    private final Map<Nodo, Integer> adiacenze;

    public Nodo(String nome) {
        setNome(nome);
        adiacenze = new HashMap<>();
    }

    public void aggiungiNodo(Nodo n, int peso) {
        if (n == null) throw new IllegalArgumentException("Il nodo da aggiungere non può essere null");
        adiacenze.put(n, peso);
    }

    public Nodo getNodoAdiacente(String nome) {
        if (adiacenze.isEmpty()) return null;
        for (Nodo n : adiacenze.keySet()) {
            if (n.getNome().equals(nome)) return n;
        }
        return null;
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        if (nome == null || nome.isEmpty()) throw new IllegalArgumentException("Il nome non può essere vuoto");
        this.nome = nome;
    }

    @Override
    public String toString() {
        String str = "Nodo " + nome + ", collegamenti: [ ";
        for (Nodo n : adiacenze.keySet()) str += n.getNome() + " -> " + adiacenze.get(n)+ "; ";
        if (!adiacenze.isEmpty()) str = str.substring(0, str.length() - 2);
        str += " ]";
        return str;
    }
}
