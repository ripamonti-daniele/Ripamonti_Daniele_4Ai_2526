        import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Pizzaiolo {
    private Queue<List<Pizza>> pizze;

    public Pizzaiolo() {
        pizze = new LinkedList<>();
    }

    public void aggiungiPizze(List<Pizza> p) {
        pizze.offer(p);
        preparaPizze(); //non è utile ma simula realtà

    }

    public void preparaPizze() {
        pizze.remove();
    }
}