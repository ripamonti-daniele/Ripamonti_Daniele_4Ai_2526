public class Televisione {
    // ATTRIBUTI/VARIABILI

    // Attributi funzionali
    public float dimensione;
    public String marca;
    public boolean colore;

    // Attributi di stato (interni alla classe)
    public int volume;
    private int canale;
    public boolean accesa;

    // COSTRUTTORE: è un metodo della classe
    // richiamabile solo una volta alla creazione/istanzazione
    // dell'oggetto, senza tipo di ritorno,
    // e con lo stesso nome della classe

    //principio di overloading
    //all'interno di una classe possono esistere più metodi con lo stesso nome a patto che abbiano parametri diversi

    //costruttore di default
    public Televisione(){
        this("Samsung", 1, 10, false);
    }

    private Televisione(String marca, int canale, int volume, boolean accesa) {
        this.marca = marca;
        dimensione = 27.5f;
        this.volume = volume;
        colore = true;
        this.accesa = accesa;
        this.canale = canale;
    }

    public Televisione(String marca){
        this(marca, 1, 10, false);
    }

    public Televisione(int canale) {
        marca = "Samsung";
        dimensione = 27.5f;
        volume = 10;
        colore = true;
        accesa = false;
        this.canale = canale;
    }

    public Televisione(String marca, int canale) {
        this.marca = marca;
        dimensione = 27.5f;
        volume = 10;
        colore = true;
        accesa = false;
        this.canale = canale;
    }

    // METODI/FUNZIONI
    public void accendi(){
        accesa = true;
    }
    public void spegni(){
        accesa = false;
    }
    public void canalePiu(){
        // canale = canale + 1;
        // canale += 1;
        // ++canale; -> incrementa prima
        canale++; // incrementa dopo
    }
    public void canaleMeno(){
        // canale = canale - 1;
        // canale -= 1;
        // --canale; -> decrementa prima
        canale--; // decrementa dopo
    }
    public void volumePiu(){
        volume++;
    }
    public void volumeMeno(){
        volume--;
    }
    public void impostaCanale(int canale){
        if (canale <= 0) {
            System.out.println("il canale non può essere negativo");
        }
        else this.canale = canale;
    }
    public int mostraCanale(){
        return canale;
    }
}
