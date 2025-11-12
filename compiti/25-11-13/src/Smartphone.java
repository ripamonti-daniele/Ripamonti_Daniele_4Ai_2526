import java.security.InvalidParameterException;

public class Smartphone {
    private String marca;
    private String modello;
    private double prezzoDiLancio;
    private double numPollici;
    private double ram;
    private boolean touchscreen;

    public Smartphone() {
        marca = "Samsung";
        modello = "Galaxy A17 5G";
        prezzoDiLancio = 239.99;
        numPollici = 6.7;
        ram = 8;
        touchscreen = true;
    }

    public Smartphone(String marca, String modello, double prezzoDiLancio, double numPollici, double ram, boolean touchscreen) {
        setMarca(marca);
        setModello(modello);
        setPrezzoDiLancio(prezzoDiLancio);
        setNumPollici(numPollici);
        setRam(ram);
        setTouchscreen(touchscreen);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca.length() < 3 || marca.length() > 20 || !marca.matches("[A-Za-z ]+")) throw new InvalidParameterException("Nome marca non valido");
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        if (modello.length() < 3 || modello.length() > 20) throw new InvalidParameterException("Nome modello non valido");
        this.modello = modello;
    }

    public double getPrezzoDiLancio() {
        return prezzoDiLancio;
    }

    public void setPrezzoDiLancio(double prezzoDiLancio) {
        if (prezzoDiLancio <= 0) throw new InvalidParameterException("Il prezzo del telefono dev'essere superiore a 0");
        this.prezzoDiLancio = prezzoDiLancio;
    }

    public double getNumPollici() {
        return numPollici;
    }

    public void setNumPollici(double numPollici) {
        if (numPollici <= 0) throw new InvalidParameterException("I pollici del telefono devono essere superiore a 0");
        this.numPollici = numPollici;
    }

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        if (ram < 2) throw new InvalidParameterException("Il telefono deve avere almeno 2GB di RAM");
        this.ram = ram;
    }

    public boolean getTouchscreen() {
        return touchscreen;
    }

    public void setTouchscreen(boolean touchscreen) {
        this.touchscreen = touchscreen;
    }

    public String mostraFasciaDiPrezzo() {
        if (prezzoDiLancio < 200) return "fascia BASSA";
        else if (prezzoDiLancio < 500) return "fascia MEDIA";
        else return "fascia ALTA";
    }

    public String mostraTipologiaSmartphone() {
        if (numPollici < 5) return "Mini";
        else if (numPollici < 7) return "Normal";
        else return "Maxi";
    }

    @Override
    public String toString() {
        String touch;
        if (touchscreen) touch = "sì";
        else touch = "no";
        return "Marca: " + marca + " - Modello: " + modello + " - Prezzo di lancio: " + prezzoDiLancio + "€ - Numero pollici: " + numPollici + " - RAM: " + ram + "GB - Touchscreen: " + touch + " - Fascia di prezzo: " + mostraFasciaDiPrezzo() + " - Tipologia: " + mostraTipologiaSmartphone();
    }
}
