import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Articolo {
    private String id;
    private String descrizione;
    private String tipo;
    private LocalDate data;
    private float prezzo;
    private static final String[] tipiArticolo = {"elettronico", "decorativo", "ecofriendly", "ironico"};
    private static final List<String> id_registrati = new ArrayList<>();

    public Articolo(String id, String descrizione, String tipo, float prezzo, LocalDate data) {
        setId(id);
        setDescrizione(descrizione);
        setTipo(tipo);
        setPrezzo(prezzo);
        setData(data);
    }

    public String getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTipo() {
        return tipo;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public LocalDate getData() {
        return data;
    }

    public static String[] getTipiArticolo() {
        return tipiArticolo.clone();
    }

    public String[] getId_registrati() {
        return (String[]) id_registrati.toArray().clone();
    }

    public static void controllaId(String id) {
        id = id.toUpperCase().trim();
        if (!id.matches("[A-Z]{3}[0-9]{3}")) throw new IllegalArgumentException("Formato id non valido");
        if (id_registrati.contains(id)) throw new IllegalArgumentException("ID già registrato, usane un altro");
    }

    private void setId(String id) {
        id = id.toUpperCase().trim();
        controllaId(id);
        this.id = id;
        id_registrati.add(id);
    }

    public static void controllaPrezzo(float prezzo) {
        if (prezzo <= 0) throw new IllegalArgumentException("Il prezzo deve avere valore maggiore di 0");
    }

    public void setPrezzo(float prezzo) {
        controllaPrezzo(prezzo);
        this.prezzo = prezzo;
    }

    public static void controllaTipo(String tipo) {
        boolean trovato = false;
        tipo = tipo.trim().toLowerCase();
        for (String a : tipiArticolo) {
            if (tipo.equals(a)) {
                trovato = true;
                break;
            }
        }
        if (!trovato) throw new IllegalArgumentException("Tipo di articolo non valido");
    }

    public void setTipo(String tipo) {
        tipo = tipo.trim().toLowerCase();
        controllaTipo(tipo);
        this.tipo = tipo;
    }

    public static void controllaDescrizione(String descrizione) {
        descrizione = descrizione.trim();
        if (descrizione.length() < 5 || descrizione.length() > 45) throw new IllegalArgumentException("Lunghezza descrizione non valida (min 5 max 45)");
    }

    public void setDescrizione(String descrizione) {
        descrizione = descrizione.trim();
        controllaDescrizione(descrizione);
        this.descrizione = descrizione;
    }

    public static void controllaData(LocalDate data) {
        if (ChronoUnit.DAYS.between(data, LocalDate.now()) < 0) throw new IllegalArgumentException("Non puoi inserire una data futura");
    }

    public void setData(LocalDate data) {
        controllaData(data);
        this.data = data;
    }

    public float scontoApplicabile() {
        if (tipo.equals(tipiArticolo[0]) || tipo.equals(tipiArticolo[1]) || prezzo < 15) return 0;
        return Math.round((0.8f + (float) ChronoUnit.DAYS.between(data, LocalDate.now()) / 1000 * prezzo) * 100f) / 100f;
    }

    @Override
    public String toString() {
        return "Articolo --> id: " + id + " - descrizione: " + descrizione + " - tipo: " + tipo + " - prezzo: " + prezzo + "€ - sconto: " + scontoApplicabile() + "€";
    }
}
