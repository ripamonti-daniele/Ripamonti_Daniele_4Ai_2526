import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogChiamate {
    private final List<Chiamata> chiamate;

    public LogChiamate() {
        chiamate = new ArrayList<>();
    }

    public void aggiungiChiamata(String c, String i, Timestamp o) {
        chiamate.add(new Chiamata(c, i, o));
    }

    public List<Chiamata> getChiamate() {
        return new ArrayList<>(chiamate);
    }
}
