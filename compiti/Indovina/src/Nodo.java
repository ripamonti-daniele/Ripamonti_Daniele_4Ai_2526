import java.io.Serial;
import java.io.Serializable;
import java.net.URL;

public class Nodo implements Serializable {
    private String testo;      // null se foglia
    private URL urlImmagine;
    public Nodo si;
    public Nodo no;

    @Serial
    private final static long serialVersionUID = 2L;

    // Nodo domanda
    public Nodo(String domanda) {
        //TODO - Mancano i set
        this.testo = domanda;
    }

    // Nodo animale
    public Nodo(String animale, URL urlImmagine) {
        //TODO - Mancano i set
        this.testo = animale;
        this.urlImmagine = urlImmagine;
    }

    public boolean isDomanda() {
        return si!= null && no != null;
    }

    public boolean isAnimale() {
        return si== null && no == null;
    }

    public String getTesto(){
        return testo;
    }

    public URL getUrlImmagine(){
        return urlImmagine;
    }

    public void setTesto(String testo) { this.testo = testo; }

    public void setUrlImmagine(URL url) { this.urlImmagine = url; }

}
