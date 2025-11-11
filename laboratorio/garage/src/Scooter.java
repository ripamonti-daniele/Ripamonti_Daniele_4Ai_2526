import java.security.InvalidParameterException;
import java.time.LocalDate;

public class Scooter {
    private String targa;
    private int km;
    private String modello;
    private LocalDate dataAcquisto;

//    public Scooter(String targa, int km, String modello, LocalDate anno) {
//
//    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        if (!targa.matches("[A-Z][0-9A-Z]{5}")) throw new InvalidParameterException("Formato targa non valido");
        this.targa = targa;
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

    }

    public LocalDate getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(LocalDate dataAcquisto) {

    }
}
