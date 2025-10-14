import java.security.InvalidParameterException;

public class Furgone {
    private int capienza;
    private int spazio_occupato;
    public final int PICCOLO;
    public final int MEDIO;
    public final int GRANDE;
    public final float SPESA_PER_UNITA;

    /**
     * Costruttore della classe Furgone.
     * Inizializza un nuovo furgone con la capienza specificata.
     *
     * @param capienza la capienza massima del furgone (tra 5 e 15 unità)
     * @throws InvalidParameterException se la capienza è al di fuori dell'intervallo valido
     */
    public Furgone(int capienza) throws InvalidParameterException {
        if (capienza < 5 || capienza > 15) throw new InvalidParameterException("valore capienza non valido (min 5 max 15)");
        this.capienza = capienza;
        spazio_occupato = 0;
        PICCOLO = 1;
        MEDIO = 2;
        GRANDE = 3;
        SPESA_PER_UNITA = 10.0f;
    }

    /**
     * Carica uno scatolone nel furgone, incrementando lo spazio occupato.
     *
     * @param unita_scatolone il numero di unità occupate dallo scatolone
     * @throws RuntimeException se non c'è spazio sufficiente per caricare lo scatolone
     */
    private void caricaScatolone(int unita_scatolone) throws RuntimeException {
        if (capienza - spazio_occupato < unita_scatolone) throw new RuntimeException("non c'è spazio per uno scatolone da " + unita_scatolone + " unità");
        spazio_occupato += unita_scatolone;
    }

    /**
     * Aggiunge uno scatolone di tipo PICCOLO al furgone.
     */
    public void addPiccolo() {
        caricaScatolone(PICCOLO);
    }

    /**
     * Aggiunge uno scatolone di tipo MEDIO al furgone.
     */
    public void addMedio() {
        caricaScatolone(MEDIO);
    }

    /**
     * Aggiunge uno scatolone di tipo GRANDE al furgone.
     */
    public void addGrande() {
        caricaScatolone(GRANDE);
    }

    /**
     * Svuota completamente il furgone, azzerando lo spazio occupato.
     */
    public void empty() {
        spazio_occupato = 0;
    }

    /**
     * Restituisce lo spazio rimasto disponibile nel furgone.
     *
     * @return il numero di unità ancora disponibili
     */
    // metodo calcolato (usa il get ma non restituisce un attributo)
    public int getSpazioRimasto() {
        return capienza - spazio_occupato;
    }

    /**
     * Calcola il costo totale della spedizione in base allo spazio occupato.
     *
     * @return il costo della spedizione
     */
    public float getCostoSpedizione() {
        return spazio_occupato * SPESA_PER_UNITA;
    }
}
