import java.security.InvalidParameterException;

public class TriangoloRettangolo {
    private double cateto1;
    private double cateto2;

    public TriangoloRettangolo() {
        cateto1 = 3;
        cateto2 = 4;
    }

    public TriangoloRettangolo(double cateto1, double cateto2) {
        setCateto1(cateto1);
        setCateto2(cateto2);
    }

    public double getCateto1() {
        return cateto1;
    }

    public double getCateto2() {
        return cateto2;
    }

    public void setCateto1(double cateto1) {
        if (cateto1 <= 0) throw new InvalidParameterException("La lunghezza del cateto dev'essere maggiore di 0");
        this.cateto1 = cateto1;
    }

    public void setCateto2(double cateto2) {
        if (cateto2 <= 0) throw new InvalidParameterException("La lunghezza del cateto dev'essere maggiore di 0");
        this.cateto2 = cateto2;
    }

    public double calcolaIpotenusa() {
        return Math.round(Math.sqrt(cateto1 * cateto1 + cateto2 * cateto2) * 100.0) / 100.0;
    }

    public double calcolaArea() {
        return cateto1 * cateto2 / 2;
    }

    public double calcolaPerimetro() {
        return Math.round((cateto1 + cateto2 + calcolaIpotenusa()) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "TriangoloRettangolo {cateto1: " + cateto1 + ", cateto2: " + cateto2 + ", ipotenusa: " + calcolaIpotenusa() + ", area: " + calcolaArea() + ", perimetro: " + calcolaPerimetro() + "}";
    }
}
