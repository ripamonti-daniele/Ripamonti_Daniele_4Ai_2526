public class Pizza {
    private String nome;
    private float costo;

    public Pizza(String nome, float costo) {
        setNome(nome);
        setCosto(costo);
    }

    public Pizza(Pizza p) {
        this.nome = p.nome;
        this.costo = p.costo;
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        nome = nome.trim().toLowerCase();
        if (nome.isEmpty()) throw new IllegalArgumentException("Non puoi lasciare vuoto il nome della pizza");
        this.nome = nome;
    }

    public float getCosto() {
        return costo;
    }

    private void setCosto(float costo) {
        if (costo < 0) throw new IllegalArgumentException("Il prezzo non può essere minore di 0 euro");
        this.costo = costo;
    }
}
