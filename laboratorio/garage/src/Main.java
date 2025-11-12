String chiediStringa(String msg) {
    String elem;
    System.out.println(msg);
    elem = IO.readln();
    return elem;
}

int chediNumero(String msg) {
    int n = -1;
    System.out.println(msg);
    try {
        n = Integer.parseInt(IO.readln());
    }
    catch (Exception e) {
        System.out.println("Non hai inserito un numero intero");
    }
    return n;
}

void aggiungi() {
    String targa = chiediStringa("Inserisci la targa: ");
    String modello = chiediStringa("Inserisci il nome del modello: ");
    System.out.println("Insersci l'anno di fabbriaczione: ");
    int anno = chediNumero("Inserisci l'anno di fabbricazione: ");
    int annoAcquisto = chediNumero("Inserisci l'anno di acquisto: ");
    int meseAcquisto = chediNumero("Inserisci il numero del mese di acquisto: ");
    int giornoAcquisto = chediNumero("Inserisci il giorno di acquisto: ");
    LocalDate dataAcquisto;
    try {
        dataAcquisto = LocalDate.of(annoAcquisto, meseAcquisto, giornoAcquisto);
    }
    catch (Exception e) {
        System.out.println("formato data non valido");
    }
    try {
        Scooter s = new Scooter(targa, modello, anno, dataAcquisto);
    }
    catch (Exception e) {
        System.out.println(e.getMessage());
    }

}

void main() {
    HashMap<String, Scooter> scooter = new HashMap<>();
}