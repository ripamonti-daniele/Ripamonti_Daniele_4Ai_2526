public class Mostro {
    private int vita;
    private String nome;
    private String colore;

    public Mostro() {
        vita = 1000;
        nome = "Bowser";
        colore = "rosso";
    }

    public void cambiaNome(String nome) {
        this.nome = nome;
    }

    public String mostraNome() {
        return nome;
    }

    public int getVita() {
        return vita;
    }

    public String mostraColore() {
        return colore;
    }
}
