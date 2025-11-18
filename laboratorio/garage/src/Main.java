//TODO
//km min e km max ordinato
//ricerca in base al modello
//range tra due date

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

void aggiungi(HashMap<String, Scooter> scooter) {
    String targa = chiediStringa("Inserisci la targa: ");
    String modello = chiediStringa("Inserisci il nome del modello: ");
    int km = chediNumero("Inserisci il kilometraggio");
    int annoAcquisto = chediNumero("Inserisci l'anno di acquisto: ");
    int meseAcquisto = chediNumero("Inserisci il numero del mese di acquisto: ");
    int giornoAcquisto = chediNumero("Inserisci il giorno di acquisto: ");
    LocalDate dataAcquisto = null;
    try {
        dataAcquisto = LocalDate.of(annoAcquisto, meseAcquisto, giornoAcquisto);
        Scooter s = new Scooter(targa, km, modello, dataAcquisto);
        scooter.put(s.getTarga(), s);
        System.out.println("Scooter inserito");
    }
    catch (Exception e) {
        System.out.println("Impossibile aggiungere questo scooter: " + e.getMessage());
    }
}

void visualizza(HashMap<String, Scooter> scooter) {
    if (scooter.isEmpty()) System.out.println("Non ci sono scooter");
    else {
        int i = 0;
        for (Scooter s : scooter.values()) {
            i++;
            System.out.println(i + ") Targa: " + s.getTarga() + " - Km: " + s.getKm() + " - Modello: " + s.getModello() + " - Data acquisto: " + s.getDataAcquisto());
        }
    }
}

void elimina(HashMap<String, Scooter> scooter) {
    String targa = chiediStringa("Inserisci la targa dello scooter che vuoi eliminare: ").toUpperCase();
    int n_elementi = scooter.size();
    scooter.remove(targa);
    if (n_elementi == scooter.size() + 1) System.out.println("Scooter rimosso");
    else System.out.println("Targa non trovata");
}

void modifica(HashMap<String, Scooter> scooter) {
    String targa = chiediStringa("Inserisci la targa dello scooter che vuoi modificare: ").toUpperCase();
    if (!scooter.containsKey(targa)) System.out.println("Targa non trovata");
    else {
        int km = chediNumero("Inserisci il nuovo kilometraggio");
        if (scooter.get(targa).getKm() > km) System.out.println("Non puoi inserire un kilometraggio minore di quello precedente");
        else {
            try {
                scooter.get(targa).setKm(km);
                System.out.println("Kilometraggio cambiato");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

void ricerca_targa(boolean parziale, HashMap<String, Scooter> scooter) {
    if (!parziale) {
        String targa = chiediStringa("Inserisci la targa: ").toUpperCase();
        if (!scooter.containsKey(targa)) System.out.println("Nessuno scooter trovato");
        else System.out.println("Km: " + scooter.get(targa).getKm() + " - Modello: " + scooter.get(targa).getModello() + " - Data acquisto: " + scooter.get(targa).getDataAcquisto());
    }
    else {
        boolean trovato = false;
        String targa = chiediStringa("Inserisci la targa parziale").toUpperCase();
        for (String t : scooter.keySet()) {
            if (t.contains(targa)) {
                System.out.println("Targa: " + scooter.get(t).getTarga() + " - Km: " + scooter.get(t).getKm() + " - Modello: " + scooter.get(t).getModello() + " - Data acquisto: " + scooter.get(t).getDataAcquisto());
                trovato = true;
            }
        }
        if (!trovato) System.out.println("Nessuno scooter trovato");
    }
}

boolean menu(HashMap<String, Scooter> scooter) {
    System.out.println("\nInserisci 1 per aggiungere uno scooter");
    System.out.println("Inserisci 2 per visualizzare gli scooter");
    System.out.println("Inserisci 3 per eliminare uno scooter");
    System.out.println("Inserisci 4 per modificare il kilometraggio uno scooter");
    System.out.println("Inserisci 5 per cercare uno scooter in base alla targa completa");
    System.out.println("Inserisci 6 per cercare gli scooter in base a una targa parziale");
    System.out.println("Inserisci 0 per uscire");

    int scelta = -1;
    while (scelta < 0 || scelta > 6) {
        scelta = chediNumero("Inserisci un'opzione: ");
    }
    switch (scelta) {
        case 1:
            aggiungi(scooter);
            break;
        case 2:
            visualizza(scooter);
            break;
        case 3:
            elimina(scooter);
            break;
        case 4:
            modifica(scooter);
            break;
        case 5:
            ricerca_targa(false, scooter);
            break;
        case 6:
            ricerca_targa(true, scooter);
            break;
        case 0:
            return false;
    }
    return true;
}

void inizializza(HashMap<String, Scooter> scooter) {
    Scooter s1 = new Scooter("XAC992", 3000, "F10", LocalDate.of(2023, 4, 17));
    Scooter s2 = new Scooter("XH97AC", 23000, "Aprilia", LocalDate.of(2018, 1, 29));
    Scooter s3 = new Scooter("XEE334", 12500, "50 special", LocalDate.of(1993, 11, 2));
    scooter.put(s1.getTarga(), s1);
    scooter.put(s2.getTarga(), s2);
    scooter.put(s3.getTarga(), s3);
}

void main() {
    HashMap<String, Scooter> scooter = new HashMap<>();
    inizializza(scooter);

    boolean ciclo = true;
    while (ciclo) ciclo = menu(scooter);
}