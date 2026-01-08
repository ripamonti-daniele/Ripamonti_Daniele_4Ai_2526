import java.util.List;
import java.util.Queue;

public class Pizzaiolo {
    private Queue<List<Pizza>> pizze;

    public void aggiungiPizze(List<Pizza> p) {
        pizze.offer(p);
    }

    public void preparaPizze() {
        //while dentro a un thread
    }
}
