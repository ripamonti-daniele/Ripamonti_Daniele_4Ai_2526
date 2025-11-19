import com.sun.security.jgss.GSSUtil;

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

double chiediNumero() {
    double n = 0;
    System.out.println("Inserisci la lunghezza del cateto");
    try {
        n = Integer.parseInt(IO.readln());
    }
    catch (Exception e) {
        System.out.println("Numero non valido");
    }
    return n;
}

void creaTriangoloRettangolo() {
    TriangoloRettangolo tr1 = new TriangoloRettangolo();
    TriangoloRettangolo tr2 = new TriangoloRettangolo(12.6, 14.9);

    String scelta = "";

    while (!scelta.equals("0")) {
        System.out.println("Inserisci 1 per visualizzare i dati del primo triangolo rettangolo");
        System.out.println("Inserisci 2 per visualizzare i dati del primo secondo rettangolo");
        System.out.println("Inserisci 3 per modificare il primo cateto del primo triangolo rettangolo");
        System.out.println("Inserisci 4 per modificare il secondo cateto del primo triangolo rettangolo");
        System.out.println("Inserisci 5 per modificare il primo cateto del secondo triangolo rettangolo");
        System.out.println("Inserisci 6 per modificare il secondo cateto del secondo triangolo rettangolo");
        scelta = IO.readln();

        switch(scelta) {
            case "1":
                System.out.println(tr1);
                break;
            case "2":
                System.out.println(tr2);
                break;
            case "3":
                try {
                    tr1.setCateto1(chiediNumero());
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "4":
                try {
                    tr1.setCateto2(chiediNumero());
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "5":
                try {
                    tr2.setCateto1(chiediNumero());
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "6":
                try {
                    tr2.setCateto2(chiediNumero());
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Scelta non valida");
                break;
        }
    }

}

void main() {
    creaGiocatore();
    creaSmartphone();
    creaTriangoloRettangolo();
}