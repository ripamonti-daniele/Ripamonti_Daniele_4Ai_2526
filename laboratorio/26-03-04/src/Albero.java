//ottieni profondità

import java.util.ArrayList;
import java.util.List;

public class Albero {
    private final Nodo root;
    private final List<Integer> valori;

    public Albero(int valRoot) {
        root = new Nodo(valRoot);
        valori = new ArrayList<>();
    }

    public void inserisci(int valore) {
        root.aggiungiNodo(valore);
    }

    public int getRoot() {
        return root.valore;
    }

    public List<Integer> getValori() {
        valori.clear();
        root.ottieniValori(valori);
        return new ArrayList<>(valori);
    }

    public int getNumeroNodi() {
        valori.clear();
        root.ottieniValori(valori);
        return valori.size();
    }

    @Override
    public String toString() {
        valori.clear();
        root.ottieniValori(valori);
        String s = "";
        for (int v : valori) s += v + " ";
        return s;
    }
}
