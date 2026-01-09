List<Pizza> ordineCasuale(int numeroPersone, Pizza[] menu) {
    Random random = new Random();

    List<Pizza> ordine = new ArrayList<>();
    for (int i = 0; i < numeroPersone; i++) {
        ordine.add(menu[random.nextInt(menu.length)]);
    }

    return ordine;
}

void aspetta(int secondi)  {
    try {
        Thread.sleep(secondi * 1000L);
    } catch (InterruptedException e) {
        System.out.println("C'è stato un errore nella chiusura del programma");
    }
}

void main() {
    Random random = new Random();

    Pizza[] menu = {new Pizza("Margherita", 6), new Pizza("Diavola", 7), new Pizza("Kebab", 7.5f)};

    Tavolo[] tavoli = new Tavolo[10];
    for (int i = 0; i < tavoli.length; i++) {
        int numeroPersone = random.nextInt(Tavolo.getPostiMassimi()) + 1;
        tavoli[i] = new Tavolo(numeroPersone, ordineCasuale(numeroPersone, menu));
    }

    Pizzaiolo pizzaiolo = new Pizzaiolo();
    Cassa cassa = new Cassa();
    Cameriere[] camerieri = {new Cameriere(pizzaiolo, cassa), new Cameriere(pizzaiolo, cassa)};

    for (int i = 0; i < tavoli.length; i++) {
        String idTavolo = tavoli[i].getId();
        int indiceCameriere = 0;
        String indicativoCameriere = "primo";
        if (i % 2 != 0) {
            indiceCameriere = 1;
            indicativoCameriere = "secondo";
        }

        System.out.println("Il " + indicativoCameriere + " cameriere ha preso l'ordine al " + idTavolo);
        System.out.println(tavoli[i]);
        System.out.println("In attesa...");
        aspetta(3);
        camerieri[indiceCameriere].prendiOrdine(tavoli[i]);
        System.out.println("Ordine consegnato");
        System.out.println("Il conto per il " + idTavolo + " è di €" + cassa.getConto(idTavolo) + "\n");
        aspetta(1);
    }
}
