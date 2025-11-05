import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class Brano {
    private String titolo;
    private String artista;
    private int anno;
    private int copie_vendute;
    private static final String[] esempiTitoli = {"abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx"};
    private static final String[] esempiArtisti = {"jiggy", "gian", "borno", "giuse360", "maionese", "schiavo", "ruzza"};
    private static final Map<String, List<String>> brani_per_artista = new HashMap<>();

    public Brano(String titolo, String artista, int anno, int copie_vendute) {
        setTitolo(titolo);
        setArtista(artista);
        setAnno(anno);
        setCopieVendute(copie_vendute);
        if (!brani_per_artista.containsKey(artista)) brani_per_artista.put(artista, new ArrayList<>());
        else if (brani_per_artista.get(artista).contains(titolo)) throw new InvalidParameterException("Questo brano è già stato creato");
        brani_per_artista.get(artista).add(titolo);
    }

    public static Brano creaBrano() {
        Random r = new Random();
        String titolo = esempiTitoli[r.nextInt(esempiTitoli.length)];
        String artista = esempiArtisti[r.nextInt(esempiArtisti.length)];

        //fare controlli

        return new Brano(titolo, artista, LocalDate.now().getYear(), 0);
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        if (titolo.isEmpty()) throw new InvalidParameterException("Il titolo non può essere vuoto");
        this.titolo = titolo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        if (artista.length() < 2) throw new InvalidParameterException("Il nome dell'artista deve contenere almeno due caratteri");
        this.artista = artista;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        if (anno > LocalDate.now().getYear()) throw new InvalidParameterException("Non si accettano brani nel futuro");
        this.anno = anno;
    }

    public int getCopie_vendute() {
        return copie_vendute;
    }

    public void setCopieVendute(int copie_vendute) {
        if (copie_vendute < 0) throw new InvalidParameterException("Il numero di copie vendute non può essere negativo");
        this.copie_vendute = copie_vendute;
    }

    public Classificazione getRiconoscimento() {
        Classificazione riconoscimento = Classificazione.NESSUNA;
        if (copie_vendute <= 1000) riconoscimento = Classificazione.LEGNO;
        else if (copie_vendute >= 100000 && copie_vendute < 250000) riconoscimento = Classificazione.ARGENTO;
        else if (copie_vendute >= 250000 && copie_vendute < 500000) riconoscimento = Classificazione.ORO;
        else if (copie_vendute >= 500000) riconoscimento = Classificazione.PLATINO;

        return riconoscimento;
    }

    public int getEta() {
        return LocalDate.now().getYear() - anno;
    }

    @Override
    public String toString() {
        return titolo + " - " + artista + " (" + anno + " - " + copie_vendute + " copie vendute)";
    }
}
