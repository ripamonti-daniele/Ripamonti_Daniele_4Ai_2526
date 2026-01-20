import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Automobile {
    private String marca;
    private String modello;
    private int km;
    private LocalDate dataDiFabbrica;
    private Color colore;
    private String targa;
    private static final List<String> targheRegistrate = new ArrayList<>();

    public Automobile(String marca, String modello, String targa, Color colore, LocalDate dataDiFabbrica) {
        setMarca(marca);
        setModello(modello);
        setTarga(targa);
        setColore(colore);
        km = 0;
        setDataDiFabbrica(dataDiFabbrica);
    }

    public Automobile(String marca, String modello, String targa, Color colore) {
        setMarca(marca);
        setModello(modello);
        setTarga(targa);
        setColore(colore);
        km = 0;
        dataDiFabbrica = LocalDate.now();
    }

    public Automobile(String marca, String modello, String targa, Color colore, int km, LocalDate dataDiFabbrica) {
        setMarca(marca);
        setModello(modello);
        setTarga(targa);
        setColore(colore);
        setKm(km);
        setDataDiFabbrica(dataDiFabbrica);
    }

    public String getMarca() {
        return marca;
    }

    public static void controllaMarca(String marca) {
        marca = marca.trim().toLowerCase();
        if (marca.isEmpty()) throw new IllegalArgumentException("La marca non può essere vuota");
    }

    private void setMarca(String marca) {
        marca = marca.trim().toLowerCase();
        controllaMarca(marca);
        this.marca = marca;
    }

    public String getTarga() {
        return targa;
    }

    public static void controllaTarga(String targa) {
        targa = targa.trim().toUpperCase();
        if (targa.matches("[A-Z]{2}[0-9]{3}[A-Z]{2}")) throw new IllegalArgumentException("Formato targa non valido");
        if (targheRegistrate.contains(targa)) throw new IllegalArgumentException("Targa già in uso");
    }

    private void setTarga(String targa) {
        targa = targa.trim().toUpperCase();
        controllaTarga(targa);
        this.targa = targa;
        targheRegistrate.add(targa);
    }

    public void cambiaTarga(String targa) {
        controllaTarga(targa);
        targheRegistrate.remove(this.targa);
        targheRegistrate.add(targa);
        this.targa = targa;
    }

    public Color getColore() {
        return colore;
    }

    public static void controllaColore(Color colore) {
        if (colore == null) throw new IllegalArgumentException("Il colore non può essere null");
    }

    public void setColore(Color colore) {
        controllaColore(colore);
        this.colore = colore;
    }

    public int getKm() {
        return km;
    }

    public static void controllaKm(int km) {
        if (km < 0) throw new IllegalArgumentException("Il kilometraggio non può essere minore di 0");
    }

    private void setKm(int km) {
        controllaKm(km);
        this.km = km;
    }

    public void aumentaKm(int incremento) {
        if (incremento < 0) throw new IllegalArgumentException("Il kilometraggio non può diminuire");
        km += incremento;
    }

    public LocalDate getDataDiFabbrica() {
        return dataDiFabbrica;
    }

    public static void controllaDataDiFabbrica(LocalDate dataDiFabbrica) {
        if (LocalDate.now().isBefore(dataDiFabbrica)) throw new IllegalArgumentException("La data non può essere futura");
    }

    private void setDataDiFabbrica(LocalDate dataDiFabbrica) {
        controllaDataDiFabbrica(dataDiFabbrica);
        this.dataDiFabbrica = dataDiFabbrica;
    }

    public String getModello() {
        return modello;
    }

    public static void controllaModello(String modello) {
        modello = modello.trim().toLowerCase();
        if (modello.isEmpty())  throw new IllegalArgumentException("Il modello non può essere vuoto");
    }

    private void setModello(String modello) {
        modello = modello.trim().toLowerCase();
        controllaModello(modello);
        this.modello = modello;
    }
}
