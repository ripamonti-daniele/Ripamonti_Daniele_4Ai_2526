import java.io.StringReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Proprietario {
    private String CF;
    private String nome;
    private String cognome;
    private String residenza;
    private static List<String> codici_registrati = new ArrayList<>();

    public Proprietario(String CF, String nome, String cognome, String residenza) {
        setCF(CF);
        setNome(nome);
        setCognome(cognome);
        setResidenza(residenza);
    }

    public String getCF() {
        return CF;
    }

    public String getCognome() {
        return cognome;
    }

    public String getResidenza() {
        return residenza;
    }

    public String getNome() {
        return nome;
    }

    public void setCF(String CF) {
        if (CF.length() != 16) throw new InvalidParameterException("Codice fiscale non valido");
        if (codici_registrati.contains(CF)) throw new InvalidParameterException("Codice già registrato");
        this.CF = CF.toUpperCase();
        codici_registrati.add(this.CF);
    }

    public void setResidenza(String residenza) {
        if (residenza.length() < 5 || residenza.length() > 40) throw new InvalidParameterException("Lunghezza indirizzo non valida");
        this.residenza = residenza;
    }

    public void setCognome(String cognome) {
        if (cognome.length() < 2 || cognome.length() > 20) throw new InvalidParameterException("Lunghezza cognome non valida");
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        if (nome.length() < 2 || nome.length() > 20) throw new InvalidParameterException("Lunghezza nome non valida");
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Codice Fiscale: " + CF + " - Nome: " + nome + " - Cognome: " + cognome + " - Residenza: " +residenza;
    }
}
