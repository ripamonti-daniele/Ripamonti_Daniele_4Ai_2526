public class Personaggio {
    private String nome;
    protected int vita;
    private int stamina;
    private int danni;
    private int livello;
    public Armi arma;

    public Personaggio(String nome) {
        this(nome, 100, 30, 10, Armi.PUNGO);
    }

    protected Personaggio(String nome, int vita, int stamina, int danni, Armi arma) {
        setNome(nome);
        setVita(vita);
        setDanni(danni);
        livello = 1;
        setStamina(stamina);
        this.arma = arma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        nome = nome.trim();
        if (nome.isEmpty() || nome.length() > 20) throw new IllegalArgumentException("Lunghezza nome non valida");
        this.nome = nome;
    }

    public int getVita() {
        return vita;
    }

    protected void setVita(int vita) {
        if (vita < 0) throw new IllegalArgumentException("La vita non può essere negativa");
        this.vita = vita;
    }

    public int getStamina() {
        return stamina;
    }

    protected void setStamina(int stamina) {
        if (stamina < 0) throw new IllegalArgumentException("La stamina non può essere negativa");
        this.stamina = stamina;
    }

    public int getDanni() {
        return danni;
    }

    protected void setDanni(int danni) {
        if (danni <= 0) throw new IllegalArgumentException("Il danno deve essere maggiore di 0");
        this.danni = danni;
    }

    public int getLivello() {
        return livello;
    }

    public void attacca (Personaggio p) {
        p.subisciDanno(danni);
    }

    protected void subisciDanno(int danno) {
        vita -= danno;
        if (vita < 0) vita = 0;
    }

    @Override
    public String toString() {
        return "Personaggio{" +
                "nome='" + nome + '\'' +
                ", vita=" + vita +
                ", stamina=" + stamina +
                ", danni=" + danni +
                ", livello=" + livello +
                ", arma=" + arma +
                '}';
    }
}
