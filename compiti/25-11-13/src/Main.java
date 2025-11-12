import java.io.IO;

void nuovaCasa() {
    Casa casa = new Casa();
    casa.setNome("Villa");
    casa.setFibraOttica(true);
    casa.setNumStanze(12);
    casa.setMetriQuadri(250);
    String nome = casa.getNome();
    boolean fibraOttica = casa.getFibraOttica();
    int numStanze = casa.getNumStanze();
    double metriQuadri = casa.getMetriQuadri();
    System.out.println(nome + " " + fibraOttica + " " + numStanze + " " + metriQuadri);
}

void nuovaPenna() {
    Penna penna = new Penna();
    penna.setMarca("Sharpie");
    penna.setCancellabile(true);
    penna.setQuantitaInchiostro(1.2);
    penna.setColore("Blu");
    String marca = penna.getMarca();
    boolean cancellabile = penna.getCancellabile();
    double quantitaInchiostro = penna.getQuantitaInchiostro();
    String colore = penna.getColore();
    System.out.println(marca + " " + cancellabile + " " + quantitaInchiostro + " " + colore);
}

void creaCasa() {
    Casa casa1 = new Casa();
    System.out.println("Inserisci il nome della casa: ");
    casa1.setNome(IO.readln());
    System.out.println("Inserisci 1 se la casa ha al fibra ottica, inserisci 0 se non ha la fibra ottica: ");
    casa1.setFibraOttica(IO.readln().trim().equals("1"));
    System.out.println("Inserisci il numero di stanze della casa: ");
    casa1.setNumStanze(Integer.parseInt(IO.readln()));
    System.out.println("Insersci i metri quadri della casa: ");
    casa1.setMetriQuadri(Double.parseDouble(IO.readln()));
}

void creaPenna() {
    Penna penna1 = new Penna();
    System.out.println("Inserisci la marca della penna: ");
    penna1.setMarca(IO.readln());
    System.out.println("Inserisci 1 se la penna è cancellabile, inserisci 0 se non è cancellabile: ");
    penna1.setCancellabile(IO.readln().trim().equals("1"));
    System.out.println("Inserisci la quantità di inchiostro della penna in ml: ");
    penna1.setQuantitaInchiostro(Double.parseDouble(IO.readln()));
    System.out.println("Inserisci il colore della penna: ");
    penna1.setColore(IO.readln());
}

void creaGiocatore() {
    Giocatore totti = new Giocatore();
    totti.setNome("Totti");
    totti.setCapitano(true);
    totti.setGoal(300);
    totti.setGoalNazionale(70);

    Giocatore zanetti = new Giocatore("Zanetti", false, 230, 45);

    Giocatore delPiero = new Giocatore();
    delPiero.setNome("Del Piero");
    System.out.println("Inserisci 1 se Del Piero è capitano, inserisci 0 se non è capitano: ");
    delPiero.setCapitano(IO.readln().trim().equals("1"));
    System.out.println("Inserisci i goal fatti da Del Piero");
    delPiero.setGoal(Integer.parseInt(IO.readln()));
    System.out.println("Inserisci i goal fatti in nazionale da Del Piero");
    delPiero.setGoalNazionale(Integer.parseInt(IO.readln()));

    System.out.println(totti);
    System.out.println(zanetti);
    System.out.println(delPiero);
}

void creaSmartphone() {
    Smartphone smartphone1 = new Smartphone();
    boolean datiValidi = false;
    while (!datiValidi) {
        try {
            System.out.print("Inserisci marca: ");
            smartphone1.setMarca(IO.readln());
            System.out.print("Inserisci modello: ");
            smartphone1.setModello(IO.readln());
            System.out.print("Inserisci prezzo di lancio (€): ");
            smartphone1.setPrezzoDiLancio(Double.parseDouble(IO.readln()));
            System.out.print("Inserisci numero pollici: ");
            smartphone1.setNumPollici(Double.parseDouble(IO.readln()));
            System.out.print("Inserisci RAM (GB): ");
            smartphone1.setRam(Double.parseDouble(IO.readln()));
            System.out.print("Inserisci 1 se il telefono è touchscreen, inserisci 0 se non è touchscreen: ");
            smartphone1.setTouchscreen(IO.readln().trim().equals("1"));
            datiValidi = true;
        }
        catch (InvalidParameterException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        catch (NumberFormatException e) {
            System.out.println("Errore: inserisci un numero valido");
        }
    }

    Smartphone smartphone2 = null;
    datiValidi = false;
    while (!datiValidi) {
        try {
            System.out.print("Inserisci marca: ");
            String marca = IO.readln();
            System.out.print("Inserisci modello: ");
            String modello = IO.readln();
            System.out.print("Inserisci prezzo di lancio (€): ");
            double prezzo = Double.parseDouble(IO.readln());
            System.out.print("Inserisci numero pollici: ");
            double pollici = Double.parseDouble(IO.readln());
            System.out.print("Inserisci RAM (GB): ");
            double ram = Double.parseDouble(IO.readln());
            System.out.print("Inserisci 1 se il telefono è touchscreen, inserisci 0 se non è touchscreen: ");
            boolean touchscreen = IO.readln().trim().equals("1");

            smartphone2 = new Smartphone(marca, modello, prezzo, pollici, ram, touchscreen);
            datiValidi = true;
        }
        catch (InvalidParameterException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        catch (NumberFormatException e) {
            System.out.println("Errore: inserisci un numero valido");
        }
    }

    System.out.println("Smartphone 1 → " + smartphone1);
    System.out.println("Smartphone 2 → " + smartphone2);
}

void main() {
    nuovaCasa();
    nuovaPenna();
    creaCasa();
    creaPenna();
    creaGiocatore();
    creaSmartphone();
}
