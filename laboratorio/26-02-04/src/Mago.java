import java.util.Random;

public class Mago extends Personaggio {
    private int aura;
    private Potere tipo;

    private enum Potere {
        GHIACCIO, ELETTRICO, FOUCO
    }

    protected Mago(String nome) {
        super(nome, 150, 50, 20, Armi.BACCHETTA_MAGICA);
        aura = 1;
        tipoRandom();
    }

    private void tipoRandom() {
        Random r = new Random();
        switch (r.nextInt(1, 4)) {
            case 1:
                tipo = Potere.FOUCO;
                break;
            case 2:
                tipo = Potere.ELETTRICO;
                break;
            case 3:
                tipo = Potere.GHIACCIO;
                break;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nAura: " + aura + ", tipo: " + tipo;
    }
}
