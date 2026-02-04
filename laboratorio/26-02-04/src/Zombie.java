import java.util.ArrayList;
import java.util.List;

public class Zombie extends Personaggio {
    List<Personaggio> schiavi = new ArrayList<>();

    public Zombie(String nome) {
        super(nome, 50, 100, 20, Armi.PUNGO);
    }

    public void cattura(Personaggio p) {
        schiavi.add(p);
    }

    @Override
    public void attacca(Personaggio p) {
        super.attacca(p);
        cattura(p);
    }

    @Override
    public String toString() {
        String catturati = "Catturati: ";
        for (Personaggio p : schiavi) catturati += p.getNome() + ", ";
        catturati = catturati.substring(0, catturati.length() - 2);
        return super.toString() + "\n" + catturati;
    }
}
 