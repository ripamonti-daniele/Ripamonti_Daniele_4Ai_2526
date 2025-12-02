String chiediStringa(String msg) {
    System.out.println(msg);
    return IO.readln();
}

float chiediFloat(String msg) {
    System.out.println(msg);
    boolean errore = true;
    float n = 0f;

    while (errore) {
        errore = false;
        try {
            n = Float.parseFloat(IO.readln());
        }
        catch (NumberFormatException e) {
            System.out.println("Formato numero non valido: riprova ");
            errore = true;
        }
    }
    return n;
}

int chiediNumero(String msg) {
    System.out.println(msg);
    boolean errore = true;
    int n = 0;

    while (errore) {
        errore = false;
        try {
            n = Integer.parseInt(IO.readln());
        }
        catch (NumberFormatException e) {
            System.out.println("Formato numero non valido: riprova ");
            errore = true;
        }
    }
    return n;
}

String impostaDescrizione() {
    String descrizione = "";
    boolean errore = true;
    while (errore) {
        errore = false;
        descrizione = chiediStringa("Inserisci la descrizione dell'articolo ");
        try {
            Articolo.controllaDescrizione(descrizione);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            errore = true;
        }
    }
    return descrizione;
}

String impostaTipo() {
    String tipo = "";
    boolean errore = true;
    while (errore) {
        errore = false;
        System.out.print("Tipi di articolo disponibili: ");
        for (String t : Articolo.getTipiArticolo()) {
            System.out.print(t + " ");
        }
        tipo = chiediStringa("\nInserisci il tipo di articolo ");
        try {
            Articolo.controllaTipo(tipo);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            errore = true;
        }
    }
    return tipo;
}

LocalDate impostaData() {
    int anno;
    int mese;
    int giorno;
    LocalDate data = null;
    boolean errore = true;
    while (errore) {
        errore = false;
        giorno = chiediNumero("Inserisci il giorno di aggiunta al catalogo");
        mese = chiediNumero("Inserisci il mese di aggiunta al catalogo");
        anno = chiediNumero("Inserisci l'anno di aggiunta al catalogo");
        try {
            data = LocalDate.of(anno, mese, giorno);
            Articolo.controllaData(data);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            errore = true;
        }
        catch (DateTimeException e) {
            System.out.println("Formato data non valido");
            errore = true;
        }
    }
    return data;
}

float impostaPrezzo() {
    float prezzo = 0;
    boolean errore = true;
    while (errore) {
        errore = false;
        prezzo = chiediFloat("Inserisci il prezzo dell'articolo ");
        try {
            Articolo.controllaPrezzo(prezzo);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            errore = true;
        }
    }
    return prezzo;
}

void inserisci(List<Articolo> articoli) {
    boolean errore = true;
    String id = "";
    String descrizione;
    String tipo;
    LocalDate data;
    float prezzo;

    System.out.println("Scrivi esc per annullare ");
    while (errore) {
        errore = false;
        id = chiediStringa("Inserisci l'id dell'articolo (formato AAA000) ");
        if (id.trim().equalsIgnoreCase("esc")) return;
        try {
            Articolo.controllaId(id);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            errore = true;
        }
    }

    descrizione = impostaDescrizione();
    tipo = impostaTipo();
    prezzo = impostaPrezzo();
    data = impostaData();

    articoli.add(new Articolo(id, descrizione, tipo, prezzo, data));
    System.out.println("Articolo aggiunto");
}

void visualizza(List<Articolo> articoli) {
    if (articoli.isEmpty()) System.out.println("Non ci sono articoli presenti in catalogo");
    else {
        for (Articolo a : articoli) {
            System.out.print(a);
            if (a == articoli.getLast()) System.out.println();
            else System.out.print(", ");
            System.out.println();
        }
    }
}

Articolo cercaPerId(List<Articolo> articoli, String id) {
    for (Articolo a : articoli) {
        if (a.getId().equals(id)) return a;
    }
    return null;
}

void cancella(List<Articolo> articoli) {
    if (articoli.isEmpty()) System.out.println("non ci sono articoli da rimuovere");
    else {
        Articolo a = cercaPerId(articoli, chiediStringa("Inserisci l'id dell'articolo che vuoi rimuovere ").toUpperCase());
        if (a != null) {
            articoli.remove(a);
            System.out.println("Articolo rimosso");
        } else System.out.println("Articolo non trovato");
    }
}

void modifica(List<Articolo> articoli) {
    if (articoli.isEmpty()) System.out.println("non ci sono articoli da modificare");
    else {
        Articolo a = cercaPerId(articoli, chiediStringa("Inserisci l'id dell'articolo che vuoi modificare ").toUpperCase());
        if (a == null) System.out.println("Articolo non trovato");
        else {
            String scelta = "";
            while (!scelta.equals("0")) {
                System.out.println("Scegli cosa modificare: 1 - descrizione; 2 - tipo; 3 - data; 4 - prezzo; 0 - esci ");
                scelta = IO.readln().trim();
                switch (scelta) {
                    case "1":
                        a.setDescrizione(impostaDescrizione());
                        System.out.println("Modifica effettuata");
                        break;
                    case "2":
                        a.setTipo(impostaTipo());
                        System.out.println("Modifica effettuata");
                        break;
                    case "3":
                        a.setData(impostaData());
                        System.out.println("Modifica effettuata");
                        break;
                    case "4":
                        a.setPrezzo(impostaPrezzo());
                        System.out.println("Modifica effettuata");
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Scelta non valida");
                        break;
                }
            }
        }
    }
}

void visualizzaDati(List<Articolo> articoli) {
    int totale = articoli.size();
    int scontabili = 0;
    float somma_prezzi = 0;
    float scontomassimo = 0;
    for (Articolo a : articoli) {
        if (a.scontoApplicabile() != 0) scontabili++;
        somma_prezzi += a.getPrezzo();
        if (a.scontoApplicabile() > scontomassimo) scontomassimo = a.scontoApplicabile();
    }

    System.out.println("articoli totali: " + totale);
    System.out.println("Articoli scontabili: " + scontabili);
    System.out.println("Prezzo medio: " + Math.round(somma_prezzi / totale * 100f) / 100f);
    System.out.println("Sconto massimo: " + scontomassimo);
}

void inizializza(List<Articolo> articoli) {
    articoli.add(new Articolo("AGB889", "descrizione", Articolo.getTipiArticolo()[0], 45, LocalDate.of(2025, 10, 3)));
    articoli.add(new Articolo("ARB789", "descrizione", Articolo.getTipiArticolo()[2], 25, LocalDate.of(2024, 9, 3)));
    articoli.add(new Articolo("ANN114", "descrizione", Articolo.getTipiArticolo()[3], 3, LocalDate.of(2022, 10, 29)));
}

void main() {
    boolean run = true;
    List<Articolo> articoli = new ArrayList<>();
    inizializza(articoli);
    while (run) {
        System.out.println("Inserisci 1 per inserire un articolo");
        System.out.println("Inserisci 2 per visualizzare gli articoli");
        System.out.println("Inserisci 3 per cancellare un articolo");
        System.out.println("Inserisci 4 per modificare un articolo");
        System.out.println("Inserisci 5 per visualizzare altre info");
        System.out.println("Inserisci 0 per uscire");
        switch (IO.readln().trim()) {
            case "1":
                inserisci(articoli);
                break;
            case "2":
                visualizza(articoli);
                break;
            case "3":
                cancella(articoli);
                break;
            case "4":
                modifica(articoli);
                break;
            case "5":
                visualizzaDati(articoli);
                break;
            case "0":
                run = false;
                break;
            default:
                System.out.println("Scelta non valida");
                break;
        }
        System.out.println();
    }
}
