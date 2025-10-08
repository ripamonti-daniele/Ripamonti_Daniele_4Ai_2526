public class Mostro {
    private int vita;
    private String nome;
    private String colore;

    public Mostro() {
        vita = 1000;
        nome = "Bowser";
        colore = "rosso";
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getVita() {
        return vita;
    }

    public String getColore() {
        return colore;
    }
}
