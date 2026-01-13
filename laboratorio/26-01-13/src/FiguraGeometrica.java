public class FiguraGeometrica {
    private final int nLati;
    private final float lunghezzaLato;

    public FiguraGeometrica(int nLati, float lunghezzaLato) {
        this.nLati = nLati;
        this.lunghezzaLato = lunghezzaLato;
    }

    public float perimetro() {
        return lunghezzaLato * nLati;
    }
}
