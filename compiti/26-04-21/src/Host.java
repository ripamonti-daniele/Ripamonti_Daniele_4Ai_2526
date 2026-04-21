import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Host {
    private String nome;
    private int[] indirizzoIP;
    private static final List<int[]> indirizziUtilizzati = new ArrayList<>();
    private final List<Host> adiacenze;

    public Host(int[] indirizzoIP, String nome) {
        setNome(nome);
        setIndirizzo(indirizzoIP);
        adiacenze = new ArrayList<>();
    }

    public Host(Host h) {
        nome = h.nome;
        indirizzoIP = h.indirizzoIP.clone();
        adiacenze = new ArrayList<>(h.adiacenze);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        nome = nome.trim().toLowerCase();
        if (nome.length() < 3 || nome.length() > 20) throw new IllegalArgumentException("Lunghezza nome non valida (min 3 max 20)");
        this.nome = nome;
    }

    public int[] getIndirizzoIP() {
        return indirizzoIP.clone();
    }

    private void setIndirizzo(int[] indirizzoIP) {
        if (indirizzoIP == null) throw new IllegalArgumentException("L'indirizzo non può essere null");
        if (indirizzoIP.length != 4) throw new IllegalArgumentException("L'indirizzo deve essere composto da 4 ottetti");
        for (int ottetto : indirizzoIP) if (ottetto < 0 || ottetto > 255) throw new IllegalArgumentException("Valore " + ottetto + " non valido: min 0 max 255");
        if (indirizzoIP[3] == 0 || indirizzoIP[3] == 255) throw new IllegalArgumentException("L'indirizzo non può essere di rete o di broadcast");
        for (int[] indirizzo : indirizziUtilizzati) {
            boolean uguale = true;
            for (int i = 0; i < 4; i++) {
                if (indirizzo[i] != indirizzoIP[i]) {
                    uguale = false;
                    break;
                }
            }
            if (uguale) throw new IllegalArgumentException("Indirizzo ip già in uso");
        }
        this.indirizzoIP = indirizzoIP.clone();
        indirizziUtilizzati.add(this.indirizzoIP);
    }

    public void cambiaIndirizzo(int[] indirizzoIP) {
        int[] vecchio = this.indirizzoIP;
        setIndirizzo(indirizzoIP);
        indirizziUtilizzati.remove(vecchio);
    }

    public void aggiungiAdiacenza(Host h) {
        if (h == null) throw new IllegalArgumentException("Le adiacenze non possono essere null");
        adiacenze.add(h);
    }

    public void aggiungiAdiacenze(Set<Host> host) {
        if (host == null) throw new IllegalArgumentException("Host non può essere null");
        for (Host h : host) aggiungiAdiacenza(h);
    }

    public List<Host> getAdiacenze() {
        return new ArrayList<>(adiacenze);
    }

    public static List<int[]> getIndirizziUtilizzati() {
        List<int[]> copia = new ArrayList<>();
        for (int[] ip : indirizziUtilizzati) copia.add(ip.clone());
        return copia;
    }

    @Override
    public String toString() {
        String s = nome + " - indirizzo: ";
        for (int ottetto : indirizzoIP) s += ottetto + ".";
        s = s.substring(0, s.length() - 1);
        if (!adiacenze.isEmpty()) {
            s += "\nAdiacenze: { ";
            for (Host adiacenza : adiacenze) {
                for (int ottetto : adiacenza.indirizzoIP) s += ottetto + ".";
                s = s.substring(0, s.length() - 1);
                s += " - ";
            }
            s = s.substring(0, s.length() - 2);
            s += "}\n\n";
        }
        else s += "\nNessuna adiacenza\n\n";
        for (Host adiacenza : adiacenze) s += adiacenza.toString();
        return s;
    }
}
