import java.util.List;

public class Nodo {
    public final int valore;
    private Nodo sx;
    private Nodo dx;

    public Nodo(int valore) {
        this.valore = valore;
        sx = null;
        dx = null;
    }

//    public void setNodoSx(Nodo sx) {
//        if (sx.valore > valore) throw new IllegalArgumentException("Il nodo sinistro non può avere valore maggiore della sua root");
//        if (this.sx != null) sx.setNodoSx(this.sx);
//        this.sx = sx;
//    }
//
//    public void setNodoDx(Nodo dx) {
//        if (dx.valore <= valore) throw new IllegalArgumentException("Il nodo destro non può avere valore minore o uguale della sua root");
//        if (this.dx != null) dx.setNodoDx(this.dx);
//        this.dx = dx;
//    }

    public void aggiungiNodo(int valore) {
        if (valore == this.valore) return;
        if (valore > this.valore && dx != null) dx.aggiungiNodo(valore);
        else if (valore > this.valore) dx = new Nodo(valore);
        else if (sx != null) sx.aggiungiNodo(valore);
        else sx = new Nodo(valore);
    }

    public Nodo getNodoSx() {
        return sx;
    }

    public Nodo getNodoDx() {
        return dx;
    }

    public void ottieniValori(List<Integer> valori) {
        valori.add(valore);
        if (sx != null) sx.ottieniValori(valori);
        if (dx != null) dx.ottieniValori(valori);
    }
}
