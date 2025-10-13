import java.security.InvalidParameterException;

public class Furgone {
    private int capienza;
    private int spazio_occupato;
    private final int piccolo;
    private final int medio;
    private final int grande;

    public Furgone(int capienza) throws InvalidParameterException {
        if (capienza < 5 || capienza > 15) throw new InvalidParameterException("valore capienza non valido (min 5 max 15)");
        this.capienza = capienza;
        spazio_occupato = 0;
        piccolo = 1;
        medio = 2;
        grande = 3;
    }

    public void addPiccolo() throws Exception {
        if (capienza - spazio_occupato < 1) throw new Exception("non c'è spazio per uno scatolone piccolo");
        spazio_occupato += piccolo;
    }

    public void addMedio() throws Exception {
        if (capienza - spazio_occupato < 2) throw new Exception("non c'è spazio per uno scatolone medio");
        spazio_occupato += medio;
    }

    public void addGrande() throws Exception {
        if (capienza - spazio_occupato < 3) throw new Exception("non c'è spazio per uno scatolone grande");
        spazio_occupato += grande;
    }

    public void empty() {
        spazio_occupato = 0;
    }

    public int getSpazioRimasto() {
        return capienza - spazio_occupato;
    }

    public int costoSpedizione() {
        return spazio_occupato * 10;
    }
}
