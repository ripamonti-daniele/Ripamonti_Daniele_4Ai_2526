import java.util.Arrays;

public class Rete {
    private final Host root;

    public Rete(int[] indirizzoIPRoot, String nomeRoot) {
        root = new Host(indirizzoIPRoot, nomeRoot);
    }

    private void aggiungiHost(Host root, int[] indirizzoIP, String nome, int[] indirizzoIPAdiacenza) {
        if (Arrays.equals(indirizzoIPAdiacenza, root.getIndirizzoIP())) {
            root.aggiungiAdiacenza(new Host(indirizzoIP, nome));
        }
        else {
            for (Host adiacenza : root.getAdiacenze()) aggiungiHost(adiacenza, indirizzoIP, nome, indirizzoIPAdiacenza);
        }
    }

    public void aggiungiHost(int[] indirizzoIP, String nome, int[] indirizzoIPAdiacenza) {
        aggiungiHost(root, indirizzoIP, nome, indirizzoIPAdiacenza);
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
