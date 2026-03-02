import java.io.Serial;
import java.time.LocalDate;

public class Bambino extends Persona {
    private LocalDate dataDiNascita;
    @Serial
    private static final long serialVersionUID = 2L;

    public Bambino(String nome, String cognome, int eta, LocalDate dataDiNascita) {
        super(nome, cognome, eta);
        setDataDiNascita(dataDiNascita);
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    private void setDataDiNascita(LocalDate data) {
        if (data.isAfter(LocalDate.now())) throw new IllegalArgumentException("Il bambino non può nascere in una data futura");
        dataDiNascita = data;
    }

    @Override
    public String toString() {
        return "Bambino{" + super.toString() +
                "dataDiNascita=" + dataDiNascita +
                '}';
    }
}
