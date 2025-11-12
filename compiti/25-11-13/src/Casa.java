import java.security.InvalidParameterException;

public class Casa {
    private String nome;
    private boolean fibraOttica;
    private int numStanze;
    private double metriQuadri;

    public Casa() {
        nome = "Appartamento";
        fibraOttica = false;
        numStanze = 4;
        metriQuadri = 50;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome.length() < 4 || nome.length() > 20 || !nome.matches("[A-Za-z ]+")) throw new InvalidParameterException("Nome non valido");
        this.nome = nome;
    }

    public boolean getFibraOttica() {
        return fibraOttica;
    }

    public void setFibraOttica(boolean fibraOttica) {
        this.fibraOttica = fibraOttica;
    }

    public int getNumStanze() {
        return numStanze;
    }

    public void setNumStanze(int numStanze) {
        if (numStanze < 1) throw new InvalidParameterException("Numero stanze non valido");
        this.numStanze = numStanze;
    }

    public double getMetriQuadri() {
        return metriQuadri;
    }

    public void setMetriQuadri(double metriQuadri) {
        if (metriQuadri < 5) throw new InvalidParameterException("Impossibile avere una casa così piccola");
        this.metriQuadri = metriQuadri;
    }
}
