import java.security.InvalidParameterException;
import java.sql.Array;
import java.time.LocalDate;
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

    public Articolo() {
        id = "000000"; //valore non ammissibile volontario per segnalare che l'articolo non è stato inizializzato
        descrizione = "articolo non inizializzato";
        tipo = tipiArticolo[0];
        data = LocalDate.now();
        prezzo = 1f;
    }

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

    public void setId(String id) {
        id = id.toUpperCase().trim();
        if (!id.matches("[A-Z]{3}[0-9]{3}")) throw new InvalidParameterException("Formato id non valido");
        if (id_registrati.contains(id)) throw new InvalidParameterException("ID già registrato, usane un altro");
        this.id = id;
        id_registrati.add(id);
    }

    public void setPrezzo(float prezzo) {
        if (prezzo <= 0) throw new InvalidParameterException("Il prezzo deve avere valore maggiore di 0");
        this.prezzo = prezzo;
    }

    public void setTipo(String tipo) {
        boolean trovato = false;
        tipo = tipo.trim().toLowerCase();
        for (String a : tipiArticolo) {
            if (tipo.equals(a)) trovato = true;
        }
        if (!trovato) throw new InvalidParameterException("Tipo di articolo non valido");
        this.tipo = tipo;
    }

    public void setDescrizione(String descrizione) {
        descrizione = descrizione.trim();
        if (descrizione.length() < 5 || descrizione.length() > 45) throw new InvalidParameterException("Lunghezza descrizione non valida (min 5 max 45)");
        this.descrizione = descrizione;
    }

    public void setData(LocalDate data) {
        if (data.hashCode() > LocalDate.now().hashCode()) throw new InvalidParameterException("Non puoi inserire una data futura");
        if (LocalDate.now().getYear() - data.getYear() > 200) throw new InvalidParameterException("Non puoi avere un articolo che hai più di 200 anni");
        this.data = data;
    }

    public float scontoApplicabile() {
        if (tipo.equals(tipiArticolo[0]) || tipo.equals(tipiArticolo[1]) || prezzo < 15) return 0;
        return 0.8f + ((LocalDate.now().hashCode() - data.hashCode()) / 1000f) * prezzo;
    }

    @Override
    public String toString() {
        return "Articolo --> id: " + id + " - descrizione: " + descrizione + " - tipo: " + tipo + " - prezzo: " + prezzo + "€ - sconto: " + scontoApplicabile() + "€";
    }
}
