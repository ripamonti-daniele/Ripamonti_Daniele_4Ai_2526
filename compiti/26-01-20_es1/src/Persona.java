public class Persona {
private String nome;
    private String cognome;
    private String cf;

    public Persona(String nome, String cognome, String codiceFiscale) {
        setNome(nome);
        setCognome(cognome);
        setCf(codiceFiscale);
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    public void setNome(String nome) {
        nome = nome.toLowerCase().trim();
        if (nome.length() < 2 || nome.length() > 15) throw new IllegalArgumentException("Lunghezza nome non valida (min 2 max 15");
        if (!nome.matches("[a-z]+")) throw new IllegalArgumentException("Non puoi inserire caratteri diversi da lettere nel nome");
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        cognome = cognome.toLowerCase().trim();
        if (cognome.length() < 2 || cognome.length() > 20) throw new IllegalArgumentException("Lunghezza cognome non valida (min 2 max 20");
        if (!cognome.matches("[a-z]+")) throw new IllegalArgumentException("Non puoi inserire caratteri diversi da lettere nel cognome");
        this.cognome = cognome;
    }

    public void setCf(String cf) {
        cf = cf.trim().toUpperCase();
        if (cf.length() != 16) throw new IllegalArgumentException("La lunghezza del codice fiscale non può essere diversa da 16 caratteri");
        this.cf = cf;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", cognome: " + cognome + ", codice fiscale: " + cf;
    }
}
