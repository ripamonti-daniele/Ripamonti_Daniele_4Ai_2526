import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;


public class Lampadina {
    static private int conta = 0;
    private int x, y;
    private int id;
    private int accensioni;
    private StatoLamp statoLamp;
    private final int ACCENSIONI_MAX = 10;


    static private ArrayList<Lampadina> archivio = new ArrayList<>();

    Random random = new Random();

    public Lampadina(){
        x = 0;
        y = 0;
        id = conta;
        conta ++;
        accensioni = 0;
        statoLamp = StatoLamp.SPENTA;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public int getAccensioni() {
        return accensioni;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "Lampadina{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", accensioni=" + accensioni +
                ", statoLamp=" + statoLamp +
                '}';
    }

    public void posizione(int x, int y){
        setY(y);
        setX(x);
    }

    //chiede alla lampadina di provare ad accendersi
    // se la lampadina è accesa o rotta la richiesta è ignorata
    // se supera le accensioni massime(10) la lampadina si rompe
    public void accendi(){
        if(statoLamp == StatoLamp.SPENTA) {
            statoLamp = StatoLamp.ACCESA;
            accensioni++;
            if(accensioni > ACCENSIONI_MAX){
                statoLamp = StatoLamp.ROTTA;
            }
        }
    }

    public void spegni(){
        if(statoLamp == StatoLamp.ACCESA)
            statoLamp = StatoLamp.SPENTA;
    }

    public void CreaLampadine(int n){
        for (int i = 0; i < n; i++){
            Lampadina l = new Lampadina();
            archivio.add(l);
        }
    }

    public void PosizionaLampadine() {
        for (int i = 0; i < archivio.size(); i++) {
            int numX = random.nextInt(0,Integer.MAX_VALUE);
            int numY = random.nextInt(0,Integer.MAX_VALUE);
            archivio.get(i).posizione(numX, numY);
        }
    }

    public void accendiTutte(){
        for(int i = 0; i < archivio.size(); i++){
            archivio.get(i).accendi();
        }
    }

    public void spegniTutte(){
        for(int i = 0; i < archivio.size(); i++){
            archivio.get(i).spegni();
        }
    }


    public void pulisciArchivio() {
        for (int i = archivio.size() - 1; i >= 0; i--) {
            Lampadina l = archivio.get(i);
            if (l.statoLamp == StatoLamp.ROTTA) {
                archivio.remove(i);
            }
        }
    }

    public int AccensioniBuonfine () {
        int conta = 0;
        for(int i = 0; i < archivio.size(); i++){
            conta += archivio.get(i).accensioni;
        }
        return conta;
    }









    //Creare un gruppo di lampadine(FATTO), posiziona lampadine a caso sullo schermo, funzionalita per accendere e spendere tutte le lampadine, 4 butta tutte le lampadine in archivio, stampa di numero di accensioni a buon fine
}
 