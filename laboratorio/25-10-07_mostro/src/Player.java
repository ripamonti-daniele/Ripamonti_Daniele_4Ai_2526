import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nome;
    private String nickname;
    private String id;
    private static List<String> raccoltaId; //crea un attributo/metodo comune a tutti gli oggetti della classe

    public Player() {
        nome = "SuperMario";
        raccoltaId = new ArrayList<String>();
    }

    public void setNome(String nome) throws Exception {
        if (nome.length() < 4) throw new Exception("Nome non valido");
        if (!(nome.matches("[a-zA-Z]+"))) throw new Exception("caratteri non ammessi");
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setId(String id) throws Exception {
        if (raccoltaId.contains(id)) throw new Exception("id già utilizzato");
        this.id = id;
        raccoltaId.add(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Player(" + id + "): " + nickname + " [" + nome + "]";
    }
}
