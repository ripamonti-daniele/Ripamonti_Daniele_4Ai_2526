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
    int n = 0;
    System.out.println(msg);
    boolean errore = true;
    while (errore) {
        try {
            n = Integer.parseInt(IO.readln());
            errore = false;
        } catch (Exception e) {
            System.out.println("Non hai inserito un numero intero");
        }
    }
    return n;
}

void aggiungi(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    String targa = chiediStringa("Inserisci la targa: ");
    String modello = chiediStringa("Inserisci il nome del modello: ");
    int km = chediNumero("Inserisci il kilometraggio");
    int annoAcquisto = chediNumero("Inserisci l'anno di acquisto: ");
    int meseAcquisto = chediNumero("Inserisci il numero del mese di acquisto: ");
    int giornoAcquisto = chediNumero("Inserisci il giorno di acquisto: ");
    LocalDate dataAcquisto = null;
    int indiceProprietario = chediNumero("Inserisci il numero assegnato a uno dei proprietari(1, 2, 3...)");
    try {
        dataAcquisto = LocalDate.of(annoAcquisto, meseAcquisto, giornoAcquisto);
        Scooter s = new Scooter(targa, km, modello, dataAcquisto, anagrafica.get(indiceProprietario - 1));
        scooter.put(s.getTarga(), s);
        System.out.println("Scooter inserito");
    }
    catch (Exception e) {
        System.out.println("Impossibile aggiungere questo scooter: " + e.getMessage());
    }
}

void visualizza(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    if (scooter.isEmpty()) System.out.println("Non ci sono scooter");
    else {
        int i = 0;
        for (Scooter s : scooter.values()) {
            i++;
            System.out.println(i + ") " + s);
            System.out.println("Proprietario (" + (anagrafica.indexOf(s.getProprietario()) + 1) + "): " + s.getProprietario());
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
                System.out.println(scooter.get(t));
                trovato = true;
            }
        }
        if (!trovato) System.out.println("Nessuno scooter trovato");
    }
}

void aggiungiProprietario(List<Proprietario> anagrafica) {
    String CF = chiediStringa("Inserisci il codice fiscale: ");
    String nome = chiediStringa("Inserisci il nome: ");
    String cognome = chiediStringa("Inserisci il cognome: ");
    String residenza = chiediStringa("Inserisci l'indirizzo di residenza");

    try {
        anagrafica.add(new Proprietario(CF, nome, cognome, residenza));
        System.out.println("Proprietario inserito");
    }
    catch (Exception e) {
        System.out.println("Impossibile aggiungere questo proprietario: " + e.getMessage());
    }
}

void visualizzaProprietari(List<Proprietario> anagrafica) {
    if (anagrafica.isEmpty()) {
        System.out.println("Non ci sono proprietari registrati");
        return;
    }
    int ind = 0;
    for (Proprietario p: anagrafica) {
        ind++;
        System.out.println(ind + ") " + p);
    }
}

void eliminaProprietario(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    if (anagrafica.isEmpty()) {
        System.out.println("Non ci sono proprietari registrati");
        return;
    }
    int indiceProprietario = chediNumero("Inserisci il numero assegnato a uno dei proprietari(1, 2, 3...)");
    Proprietario p;
    try {
        p = anagrafica.get(indiceProprietario - 1);
    }
    catch (Exception e) {
        System.out.println("Numero non valido");
        return;
    }
    boolean rimuovibile = true;
    for (Scooter s : scooter.values()) {
        if (s.getProprietario() == p) {
            System.out.println("Impossibile rimuovere un proprietario a cui appartiene uno scooter");
            rimuovibile = false;
            break;
        }
    }
    if (rimuovibile) {
        anagrafica.remove(indiceProprietario - 1);
        System.out.println("Proprietario rimosso");
    }
}

void visualizzaScooterPerProprietario(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    if (anagrafica.isEmpty()) {
        System.out.println("Non ci sono proprietari registrati");
        return;
    }
    int ind = 0;
    boolean noScooterAssegnati;
    for (Proprietario p : anagrafica) {
        noScooterAssegnati = true;
        ind++;
        System.out.println(ind + ") " + p + "\nProprietario di: ");
        for (Scooter s : scooter.values()) {
            if (s.getProprietario() == p) {
                System.out.println(s);
                noScooterAssegnati = false;
            }
        }
        if (noScooterAssegnati) System.out.println("Nessuno scooter");
    }
}

boolean menu(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    System.out.println("\nInserisci 1 per aggiungere uno scooter");
    System.out.println("Inserisci 2 per visualizzare gli scooter");
    System.out.println("Inserisci 3 per eliminare uno scooter");
    System.out.println("Inserisci 4 per modificare il kilometraggio uno scooter");
    System.out.println("Inserisci 5 per cercare uno scooter in base alla targa completa");
    System.out.println("Inserisci 6 per cercare gli scooter in base a una targa parziale");
    System.out.println("Inserisci 7 per aggiungere un proprietario");
    System.out.println("Inserisci 8 per visualizzare i proprietari");
    System.out.println("Inserisci 9 per eliminare un proprietario");
    System.out.println("Inserisci 10 per visualizzare gli scooter di ogni proprietario");
    System.out.println("Inserisci 0 per uscire");

    int scelta = -1;
    while (scelta < 0 || scelta > 10) {
        scelta = chediNumero("Inserisci un'opzione: ");
    }
    switch (scelta) {
        case 1:
            aggiungi(scooter, anagrafica);
            break;
        case 2:
            visualizza(scooter, anagrafica);
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
        case 7:
            aggiungiProprietario(anagrafica);
            break;
        case 8:
            visualizzaProprietari(anagrafica);
            break;
        case 9:
            eliminaProprietario(scooter, anagrafica);
            break;
        case 10:
            visualizzaScooterPerProprietario(scooter, anagrafica);
            break;
        case 0:
            return false;
    }
    return true;
}

void inizializza(HashMap<String, Scooter> scooter, List<Proprietario> anagrafica) {
    anagrafica.add(new Proprietario("RSSMRA85C10H501Z", "Marco", "Nania", "Via Roma 4 - Boltiere"));
    anagrafica.add(new Proprietario("BNCLGU72A45F205X", "Luca", "Rossi", "Via Europa 23 - Dalmine"));
    anagrafica.add(new Proprietario("VRDLNZ93T22L219Y", "Cristian", "Locatelli", "Via Verdi 14 - Dalmine"));
    anagrafica.add(new Proprietario("CNTFNC60B18D612W", "Andrea", "Pesenti", "Via delle Betulle 9 - Brembate"));

    Scooter s1 = new Scooter("XAC992", 3000, "F10", LocalDate.of(2023, 4, 17), anagrafica.get(0));
    Scooter s2 = new Scooter("XH97AC", 23000, "Aprilia", LocalDate.of(2018, 1, 29), anagrafica.get(1));
    Scooter s3 = new Scooter("XEE334", 12500, "Booster", LocalDate.of(1993, 11, 2), anagrafica.get(2));
    Scooter s4 = new Scooter("XXA91E", 1500, "Peugeot", LocalDate.of(2025, 5, 17), anagrafica.get(1));
    scooter.put(s1.getTarga(), s1);
    scooter.put(s2.getTarga(), s2);
    scooter.put(s3.getTarga(), s3);
    scooter.put(s4.getTarga(), s4);
}

void main() {
    HashMap<String, Scooter> scooter = new HashMap<>();
    List<Proprietario> anagrafica = new ArrayList<>();
    inizializza(scooter, anagrafica);

    boolean ciclo = true;
    while (ciclo) ciclo = menu(scooter, anagrafica);
}
