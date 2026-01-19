public class Studente extends Persona {
    String matricola;
    String universita;

    public Studente(String nome, String cognome, String codiceFiscale, String matricola, String universita) {
        super(nome, cognome, codiceFiscale);
        setMatricola(matricola);
        setUniversita(universita);
    }

    public String getMatricola() {
        return matricola;
    }

    public String getUniversita() {
        return universita;
    }

    public void setMatricola(String matricola) {
        if (matricola.isEmpty()) throw new IllegalArgumentException("La matricola non può essere vuota");
        this.matricola = matricola;
    }

    public void setUniversita(String universita) {
        if (universita.isEmpty()) throw new IllegalArgumentException("Il nome dell'università non può essere vuoto");
        this.universita = universita;
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + ", cognome: " + getCognome() + ", matricola: " + matricola + ", università: " + universita + ", codice fiscale: " + getCf();
    }
}
