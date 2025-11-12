import java.security.InvalidParameterException;

public class Penna {
    private String marca;
    private boolean cancellabile;
    private double quantitaInchiostro;
    private String colore;

    public Penna() {
        marca = "Bic";
        cancellabile = false;
        quantitaInchiostro = 1.5;
        colore = "Nero";
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca.length() < 3 || marca.length() > 15 || !marca.matches("[A-Za-z ]+")) throw new InvalidParameterException("Nome marca non valido");
        this.marca = marca;
    }

    public boolean getCancellabile() {
        return cancellabile;
    }

    public void setCancellabile(boolean cancellabile) {
        this.cancellabile = cancellabile;
    }

    public double getQuantitaInchiostro() {
        return quantitaInchiostro;
    }

    public void setQuantitaInchiostro(double quantitaInchiostro) {
        if (quantitaInchiostro < 0 || quantitaInchiostro > 10) throw new InvalidParameterException("Quantità inchiostro non valida");
        this.quantitaInchiostro = quantitaInchiostro;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        if (colore.length() < 3 || colore.length() > 15) throw new InvalidParameterException("Lunghezza nome colore non valida");
        this.colore = colore;
    }
}
