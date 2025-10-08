import java.util.ArrayList;
import java.util.List;

public class Studente {
    String nome;
    String cognome;
    String matricola;
    int anno_iscrizione;
    List<Float> voti;
    float media;

    public Studente(String nome, String cognome, String matricola, int anno_iscrizione) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.anno_iscrizione = anno_iscrizione;
        voti = new ArrayList<Float>();
    }

    public void aggiungiVoto(float voto) throws Exception {
        if (voto < 0 || voto > 10) throw new Exception("il voto deve essere compreso tra 1 e 10");
        voti.add(Math.round(voto * 100f) / 100f); //arrotonda a due decimali
    }

    public float calcolaMedia() throws Exception {
        if (voti.isEmpty()) throw new Exception("lo studente non ha ancora ricevuto voti");
        float totale = 0;
        for (float i : voti) totale += i;
        media = Math.round(totale / voti.size() * 10f) / 10f;
        return media;
    }

    public void stampaDettagi() {
        if (media == 0) System.out.println("Nome: " + nome + ", cognome: " + cognome + ", matricola: " + matricola + ", anno di iscrizione: " + anno_iscrizione);
        else System.out.println("Nome: " + nome + ", cognome: " + cognome + ", matricola: " + matricola + ", anno di iscrizione: " + anno_iscrizione + ", media: " + media);
    }
}
