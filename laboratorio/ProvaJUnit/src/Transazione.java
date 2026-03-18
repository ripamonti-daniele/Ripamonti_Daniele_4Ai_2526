import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Transazione implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private final String mittente, destinatario;
    private final LocalDate data;
    private final float importo;
    private final String causale; // motivazione della transazione

    public Transazione(String mittente, String destinatario, float importo, String causale) {
        this.mittente = controllaNome(mittente);
        if (this.mittente == null) throw new IllegalArgumentException("nome mittente non valido");
        this.destinatario = controllaNome(destinatario);
        if (this.destinatario == null) throw new IllegalArgumentException("nome destinatario non valido");
        data = LocalDate.now();
        if (importo <= 0) throw new IllegalArgumentException("L'importo deve essere maggiore di 0");
        this.importo = importo;
        if (causale == null || causale.isEmpty()) throw new IllegalArgumentException("La causale non può essere vuota");
        this.causale = causale;
    }

    private String controllaNome(String nome) {
        if (nome == null) return null;
        nome = nome.trim().toLowerCase();
        if (nome.length()< 3) return null;
        return nome;
    }

    public String getMittente() {
        return mittente;
    }

    public String getCausale() {
        return causale;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public float getImporto() {
        return importo;
    }
}
