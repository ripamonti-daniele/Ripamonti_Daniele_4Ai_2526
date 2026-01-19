public class Docente extends Persona {
    private String materia;
    private float salario;

    public Docente (String nome, String cognome, String codiceFiscale, String materia, float salario) {
        super(nome, cognome, codiceFiscale);
        setMateria(materia);
        setSalario(salario);
    }

    public String getMateria() {
        return materia;
    }

    public float getSalario() {
        return salario;
    }

    public void setMateria(String materia) {
        if (materia.isEmpty()) throw new IllegalArgumentException("La materia non può essere vuota");
        this.materia = materia;
    }

    public void setSalario(float salario) {
        if (salario <= 0) throw new IllegalArgumentException("Il salario deve essere maggiore di 0");
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Nome: " + getNome() + ", cognome: " + getCognome() + ", materia: " + materia + ", salario: " + salario + "€, codice fiscale: " + getCf();
    }
}
