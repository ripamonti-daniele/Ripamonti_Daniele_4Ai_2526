import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Scooter {
    private String targa;
    private int km;
    private String modello;
    private LocalDate dataAcquisto;
    private static final List<String> targhe = new ArrayList<>();
    private Proprietario proprietario;

    public Scooter(String targa, int km, String modello, LocalDate dataAcquisto, Proprietario proprietario) {
        setTarga(targa);
        setKm(km);
        setModello(modello);
        setDataAcquisto(dataAcquisto);
        setProprietario(proprietario);
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        targa = targa.toUpperCase();
        if (!targa.matches("[A-Z][0-9A-Z]{5}")) throw new InvalidParameterException("Formato targa non valido");
        if (targhe.contains(targa)) throw new InvalidParameterException("Esiste già uno scooter con questa targa");
        this.targa = targa;
        targhe.add(targa);
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        if (km < 0 || km > 50000) throw new InvalidParameterException("Kilometraggio non valido");
        this.km = km;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        if (modello.length() < 2) throw new InvalidParameterException("Nome modello troppo corto");
        this.modello = modello;
    }

    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(LocalDate dataAcquisto) {
        if (dataAcquisto.getYear() > LocalDate.now().getYear() || dataAcquisto.getYear() == LocalDate.now().getYear() && dataAcquisto.getDayOfYear() > LocalDate.now().getDayOfYear()) throw new InvalidParameterException("Non puoi inserire una data futura");
        if (dataAcquisto.getYear() < 1919) throw new InvalidParameterException("Gli scooter non esistevano in quest'anno");
        this.dataAcquisto = dataAcquisto;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        if (proprietario == null) throw new InvalidParameterException("Proprietario non valido");
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return "Targa: " + targa + " - Km: " + km + " - Modello: " + modello + " - Data acquisto: " + dataAcquisto;
    }
}
