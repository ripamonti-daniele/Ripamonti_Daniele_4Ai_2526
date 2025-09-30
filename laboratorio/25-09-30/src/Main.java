//registro di accesso della scuola, apre o chiude il cancello in automatico, nelle liste ci sono le targhe degli autorizzati.
//aggiungi, elimina, visualizza, apri cancello

import java.io.IO;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void aggiungi(List<String> r) {
        boolean errore = true;

        while (errore) {
            System.out.println("Scrivi la targa da aggiungere");
            String targa = IO.readln().trim().toUpperCase();

            if (r.contains(targa)) {
                System.out.println("targa già inserita");
                continue;
            }

            if (targa.matches("[A-Z]{2}[0-9]{3}[A-Z]{2}")) {
                r.add(targa);
                errore = false;
                System.out.println("targa inserita");
            } else System.out.println("formato targa non valido");
        }
    }

    public static void elimina(List<String> r) {
        if (r.isEmpty()) System.out.println("non ci sono targhe registrate");

        else {
            boolean errore = true;

            while (errore) {
                System.out.println("Scrivi la targa da rimuovere (inserisci V per visualizzare le targhe in elenco)");
                String targa = IO.readln().trim().toUpperCase();

                if (targa.equals("V")) visualizza(r);
                else {
                    if (r.contains(targa)) {
                        r.remove(targa);
                        System.out.println("targa rimossa");
                        errore = false;
                    } else System.out.println("traga non trovata");
                }
            }
        }
    }

    public static void visualizza(List<String> r) {
        if (r.isEmpty()) System.out.println("non ci sono targhe da visualizzare");

        else {
            for (int i = 0; i < r.size(); i++) {
                System.out.println((i + 1) + ") " + r.get(i));
            }
        }
    }

    public static boolean apri_cancello(List<String> r) {
        if (r.isEmpty()) {
            System.out.println("impossibile aprire il cancello: non ci sono targhe registrate");
            return false;
        }

        System.out.println("inserci la targa");
        String targa = IO.readln().trim().toUpperCase();

        if (r.contains(targa)) {
            System.out.println("targa accettata: cancello aperto");
            return true;
        }
        else {
            System.out.println("impossibile aprire il cancello: targa non valida");
            return false;
        }
    }

    public static boolean menu (List<String> r) {
        System.out.println("inserisci 1 per aggiungere una traga");
        System.out.println("inserisci 2 per rimuovere una targa");
        System.out.println("inserisci 3 per visualizzare le targhe");
        System.out.println("inserisci 4 per aprire il cancello");
        System.out.println("inserisci 0 per uscire");

        String scelta = IO.readln();

        switch (scelta) {
            case "1":
                aggiungi(r);
                return false;
            case "2":
                elimina(r);
                return false;
            case "3":
                visualizza(r);
                return false;
            case "4":
                return apri_cancello(r);
            case "0":
                return true;
            default:
                System.out.println("scelta non valida");
                return false;
        }
    }

    public static void main(String[] args) {
        List<String> registro = new ArrayList<String>();
        boolean esci = false;

        do {
            esci = menu(registro);
            System.out.println();
        } while(!esci);
    }
}