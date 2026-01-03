private Cassa[] casse;
private Gestore gestore;
private LogChiamate log;

private int chiediIntero(String msg, int min, int max, boolean vincolaMinimo, boolean vincolaMassimo) {
    System.out.println(msg);
    int n = 0;
    boolean errore = true;

    while (errore) {
        errore = false;
        try {
            n = Integer.parseInt(IO.readln());
        }
        catch (NumberFormatException e) {
            System.out.println("Errore: devi inserire un numero intero");
            errore = true;
            continue;
        }
        if (min > max && vincolaMinimo && vincolaMassimo) break;
        else if (n < min && vincolaMinimo) {
            System.out.println("Errore: il numero non può essere minore di " + min);
            errore = true;
        }
        else if (n > max && vincolaMassimo) {
            System.out.println("Errore: il numero non può essere maggiore di " + max);
            errore = true;
        }
    }
    return n;
}

private void menu() {
    int scelta;
    do {
        System.out.println("\nMENU");
        System.out.println("1) Cliente - Biglietto normale");
        System.out.println("2) Cliente - Biglietto prioritario");
        System.out.println("3) Operatore di cassa");
        System.out.println("4) Ispettore - Clienti in attesa");
        System.out.println("5) Ispettore - Storico chiamate");
        System.out.println("0) Esci");
        scelta = chiediIntero("Scelta: ", 0, 5, true, true);

        switch (scelta) {
            case 1:
                System.out.println("Biglietto: " + gestore.emettiBigliettoNormale());
                break;

            case 2:
                System.out.println("Biglietto: " + gestore.emettiBigliettoPrioritario());
                break;

            case 3:
                int i = chiediIntero("Seleziona cassa (1 - " + (casse.length) + "): ", 1, casse.length, true, true);
                String chiamato = casse[i - 1].chiamaProssimoCliente(gestore);
                if (chiamato == null)
                    System.out.println("Nessuno in attesa");
                else
                    System.out.println("Chiamato: " + chiamato);
                break;

            case 4:
                List<String> attesa = gestore.getBigliettiInAttesa();
                System.out.println("Clienti in attesa:");
                attesa.forEach(System.out::println);
                break;

            case 5:
                System.out.println("Storico chiamate:");
                log.getChiamate().forEach(System.out::println);
                break;
        }
    } while (scelta != 0);
}

void main() {
    int n = chiediIntero("Inserisci numero casse: ", 1, 0, true, false);

    casse = new Cassa[n];
    for (int i = 0; i < n; i++) {
        casse[i] = new Cassa();
    }

    log = new LogChiamate();
    gestore = new Gestore(log);

    menu();
}
