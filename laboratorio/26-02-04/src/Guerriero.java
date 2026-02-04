public class Guerriero extends Personaggio {
    private int armatura;

    public Guerriero(String nome) {
        super(nome, 100, 30, 30, Armi.SPADA);
        armatura = 20;
    }

    @Override
    protected void subisciDanno(int danno) {
        vita -= danno - (armatura / 100 * danno);
    }

    @Override
    public String toString() {
        return super.toString() + "\nPercentuale di protezione dell'armatura: " + armatura + "%";
    }
}
