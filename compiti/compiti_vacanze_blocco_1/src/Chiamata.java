import java.sql.Timestamp;

public class Chiamata {
    private String codiceBiglietto;
    private String identificativoCassa;
    private Timestamp orario;

    public Chiamata(String c, String i, Timestamp o) {
        this.codiceBiglietto = c;
        this.identificativoCassa = i;
        this.orario = o;
    }

    public String getCodiceBiglietto() {
        return codiceBiglietto;
    }

    public String getIdentificativoCassa() {
        return identificativoCassa;
    }

    public Timestamp getOrario() {
        return orario;
    }

    @Override
    public String toString() {
        return "[" + orario + "] " + identificativoCassa + " → " + codiceBiglietto;
    }
}
