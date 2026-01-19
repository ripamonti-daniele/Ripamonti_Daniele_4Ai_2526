public class DataNumerica extends DataFormattata {
    public DataNumerica(int giorno, int mese, int anno){
        super(giorno, mese, anno);
    }

    private String adattaStringa(int n, boolean anno_corto) {
        if (anno_corto && n < 100) return String.valueOf(n + 2000);
        else if (!anno_corto && n < 10) return "0" + n;
        return String.valueOf(n);
    }

    @Override
    public String stringaFormattata() {
        return adattaStringa(giorno, false) + "/" + adattaStringa(mese, false) + "/" + adattaStringa(anno, true);
    }
}
