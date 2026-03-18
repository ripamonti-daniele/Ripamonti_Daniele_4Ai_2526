import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContoCorrente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String intestatario;
    private float saldo;
    private final String codiceConto;
    private String pin;
    private final LocalDate apertura;
    private final List<Transazione> movimenti;
    private static int idConto = 0;
    private static List<String> codici = new ArrayList<>();

    private ContoCorrente(String intestatario) {
        movimenti = new ArrayList<>();
        if (intestatario == null) throw new IllegalArgumentException("L'intestatario non può essere null");
        this.intestatario = intestatario.trim().toLowerCase();
        if (this.intestatario.length() < 3) throw new IllegalArgumentException("Nome intestatario non valido");
        codiceConto = intestatario.substring(0, 4) + idConto;
        codici.add(codiceConto);
        idConto++;
        apertura = LocalDate.now();
        setPin();
    }

    public ContoCorrente(String intestatario, float importo) {
        this(intestatario);
        versamento(importo);
    }

    public String getCodiceConto() {
        return codiceConto;
    }

    public String getPin() {
        return pin;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public float getSaldo() {
        return saldo;
    }

    public LocalDate getApertura() {
        return apertura;
    }

    public void versamento(float importo) {
        if (importo <= 0) throw new IllegalArgumentException("L'importo deve essere maggiore di 0");
        saldo += importo;
    }

    private void setPin() {
        Random r = new Random();
        String p = String.valueOf(r.nextInt() * 100000);
        String zeri = "";
        for (int i = 0; i < 5 - p.length(); i++) zeri += "0";
        pin = zeri + p;
    }

    public void bonifico(String codiceDestinatario, float importo, String causale) {
        if (saldo < importo) throw new IllegalStateException("Errore: il saldo è minore dell'importo");
        if (!codici.contains(codiceDestinatario)) throw new IllegalArgumentException("Errore: codice destinatario non valido");
        try {
            movimenti.add(new Transazione(intestatario, codiceDestinatario, importo, causale));
            saldo -= importo;
        }
        catch (IllegalArgumentException e) {
            throw new IllegalStateException("bonifico fallito: " + e.getMessage());
        }
    }
}
