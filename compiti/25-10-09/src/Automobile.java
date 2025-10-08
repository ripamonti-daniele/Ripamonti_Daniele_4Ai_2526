import java.time.LocalDate;

public class Automobile {
    private String marca;
    private String modello;
    private int anno;
    private float chilometraggio;

    public Automobile(String marca, String modello, int anno, float chilometraggio) {
        this.marca = marca;
        this.modello = modello;
        this.anno = anno;
        this.chilometraggio = chilometraggio;
    }

    public Automobile() {
        this("Ford", "Fiesta", LocalDate.now().getYear(), 0);
    }

    public Automobile(String marca, String modello, int anno) {
        this(marca, modello, anno, 0);
    }

    public void stampaDettagli() {
        System.out.println("Marca: " + marca + ", modello: " + modello + ", anno: " + anno + ", chilometraggio: " + chilometraggio + "km");
    }

    public void setChilometraggio(float chilometraggio) throws Exception {
        if (chilometraggio < this.chilometraggio) throw new Exception("impossibile avere un chilometraggio minore di " + this.chilometraggio + "km");
        this.chilometraggio = chilometraggio;
    }

    public int calcolaEta() {
        return LocalDate.now().getYear() - anno;
    }
}
