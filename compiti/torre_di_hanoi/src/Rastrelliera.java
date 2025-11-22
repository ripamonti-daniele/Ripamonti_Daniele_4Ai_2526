import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Rastrelliera {
    private final Stack<Integer> dischi;
    private int maxDischi;
    public final boolean rastrelliera_iniziale;
    public final boolean rastrelliera_vittoria;

    public Rastrelliera(int maxDischi, boolean rastrelliera_iniziale, boolean rastrelliera_vittoria) {
        dischi = new Stack<>();
        setMaxDischi(maxDischi);
        if (rastrelliera_iniziale && rastrelliera_vittoria) throw new InvalidParameterException("La rastrelliera della vittoria non può essere anche quella iniziale");
        this.rastrelliera_iniziale = rastrelliera_iniziale;
        this.rastrelliera_vittoria = rastrelliera_vittoria;
        if (rastrelliera_iniziale) inizializza();
    }

    private void inizializza() {
        for (int i = maxDischi; i >= 1; i--) dischi.push(i);
    }

    public int getMaxDischi() {
        return maxDischi;
    }

    private void setMaxDischi(int maxDischi) {
        if (maxDischi < 2 || maxDischi > 20) throw new InvalidParameterException("Numero dischi non valido (min 2 max 20)");
        this.maxDischi = maxDischi;
    }

    public Stack<Integer> getDischi() {
        Stack<Integer> copia = new Stack<>();
        copia.addAll(dischi);
        return copia;
    }

    public boolean aggiungiDisco (int d) {
        if (dischi.size() == maxDischi) throw new IllegalStateException("Impossibile aggiungere un disco a una torre piena");
        if (!dischi.isEmpty() && d >= dischi.peek()) throw new InvalidParameterException("Impossibile aggiungere un disco più grande dell'ultimo inserito");
        if (d <= 0) throw new InvalidParameterException("Il disco deve avere valore maggiore o uguale a 0");
        dischi.push(d);

        return (rastrelliera_vittoria && dischi.size() == maxDischi);
    }

    public int rimuoviDisco() {
        if (dischi.isEmpty()) throw new NoSuchElementException("Impossibile prendere un disco da una rastrelliera vuota");
        return dischi.pop();
    }

    public void reset(int maxDischi) {
        setMaxDischi(maxDischi);
        dischi.clear();
        if (rastrelliera_iniziale) inizializza();
    }
}
