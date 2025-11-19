import java.security.InvalidParameterException;

public class Giocatore {
    private String nome;
    private boolean capitano;
    private int goal;
    private int goalNazionale;

    public Giocatore() {
        nome = "Mario Balotelli";
        capitano = true;
        goal = 250;
        goalNazionale = 50;
    }

    public Giocatore(String nome, boolean capitano, int goal, int goalNazionale) {
        setNome(nome);
        setCapitano(capitano);
        setGoal(goal);
        setGoalNazionale(goalNazionale);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome.length() < 3 || nome.length() > 20 || !nome.matches("[A-Za-z ]+")) throw new InvalidParameterException("nome non valido");
        this.nome = nome;
    }

    public boolean getCapitano() {
        return capitano;
    }

    public void setCapitano(boolean capitano) {
        this.capitano = capitano;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        if (goal < 0) throw new InvalidParameterException("Impossibile avere meno di 0 goal");
        this.goal = goal;
    }

    public int getGoalNazionale() {
        return goalNazionale;
    }

    public void setGoalNazionale(int goalNazionale) {
        if (goalNazionale < 0) throw new InvalidParameterException("Impossibile avere meno di 0 goal in nazionale");
        this.goalNazionale = goalNazionale;
    }

    public int calcolaGoalTotali() {
        return goal + goalNazionale;
    }

    @Override
    public String toString() {
        String cap;
        if (capitano) cap = "sì";
        else cap = "no";
        return "Giocatore: " + nome + " - Capitano: " + cap + " - Goal: " + goal + " - Goal in nazionale: " + goalNazionale;
    }
}
