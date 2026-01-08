public class Pizza {
    private final String nome;
    private float costo;

    public Pizza(String nome, float costo) {
        this.nome = nome;
    }

    public Pizza(Pizza p) {
        this.nome = p.nome;
        this.costo = p.costo;
    }

    public String getNome() {
        return nome;
    }

    public float getCosto() {
        return costo;
    }
}
