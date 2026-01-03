public class Cassa {
    private static int progressivo = 0;
    private final String identificativo;

    public Cassa() {
        progressivo++;
        this.identificativo = "CASSA" + progressivo;
    }

    public String getIdentificativo() {
        return identificativo;
    }

    public String chiamaProssimoCliente(Gestore gestore) {
        return gestore.chiamaBiglietto(identificativo);
    }
}
