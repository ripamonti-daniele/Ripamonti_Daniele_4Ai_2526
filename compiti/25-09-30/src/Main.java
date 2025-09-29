import java.io.IO;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void aggiungi(List<String> oggetti, int index) {
        boolean errore = true;
        String oggetto = "";

        while (errore) {
            oggetto = IO.readln("scrivi l'oggetto che vuoi mettere nello zaino: ");
            oggetto = oggetto.trim().toLowerCase();
            errore = false;

            if (oggetto.length() < 3 || oggetto.length() > 20) {
                errore = true;
                System.out.println("Nome oggetto non valido");
                continue;
            }

            for (int i = 0; i < oggetti.size(); i++) {
                if (oggetti.get(i).equals(oggetto)) {
                    errore = true;
                    System.out.println("quest'oggetto è già presente nello zaino");
                    break;
                }
            }
        }
        if (index == -1) {
            oggetti.add(oggetto);
            System.out.println(oggetto + " aggiunto");
        }
        else {
            System.out.println(oggetti.get(index) + " modificato con " + oggetto);
            oggetti.set(index, oggetto);
        }
    }

    public static void modifica(List<String> oggetti) {
        boolean errore = true;

        while (errore) {
            boolean trovato = false;
            String oggetto_vecchio = IO.readln("scrivi l'oggetto che vuoi sostituire: ");
            oggetto_vecchio = oggetto_vecchio.toLowerCase().trim();

            for (int i = 0; i < oggetti.size(); i++) {
                if (oggetti.get(i).equals(oggetto_vecchio)) {
                    aggiungi(oggetti, i);
                    trovato = true;
                    errore = false;
                    break;
                }
            }
            if (!trovato) {
                System.out.println("quest'oggetto non è nello zaino");
            }
        }
    }

    public static void elimina(List<String> oggetti) {
        boolean errore = true;

        while (errore) {
            boolean trovato = false;
            String oggetto_vecchio = IO.readln("scrivi l'oggetto che vuoi rimuovere: ");
            oggetto_vecchio = oggetto_vecchio.toLowerCase().trim();

            for (int i = 0; i < oggetti.size(); i++) {
                if (oggetti.get(i).equals(oggetto_vecchio)) {
                   oggetti.remove(i);
                   trovato = true;
                   errore = false;
                   System.out.println(oggetto_vecchio + " rimosso");
                   break;
                }
            }
            if (!trovato) {
                System.out.println("quest'oggetto non è nello zaino");
            }
        }
    }

    public static void visualizza(List<String> oggetti) {
        if (oggetti.isEmpty()) {
            System.out.println("lo zaino è vuoto");
        }
        else {
            System.out.println("oggetti nello zaino:");
            for (int i = 0; i < oggetti.size(); i++) {
                System.out.println((i + 1) + ") " + oggetti.get(i));
            }
        }
    }

    public static String menu() {
        String[] opzioni = {"0", "1", "2", "3", "4"};
        while (true) {
            System.out.println("inserisci 1 per aggiungere un oggetto allo zaino");
            System.out.println("inserisci 2 per sostituire un oggetto nello zaino");
            System.out.println("inserisci 3 per togliere un oggetto dallo zaino");
            System.out.println("inserisci 4 per visualizzare gli oggetti nello zaino");
            System.out.println("inserisci 0 per uscire");

            String scelta = IO.readln().trim();
            for (int i = 0; i < opzioni.length; i++) {
                if (scelta.equals(opzioni[i])) {
                    return scelta;
                }
            }
            System.out.println("scelta non valida");
        }
    }

    public static void main(String[] args) {
        List<String> zaino = new ArrayList<String>();

        String scelta = "";
        while (!scelta.equals("0")) {
            scelta = menu();
            switch (scelta) {
                case "1":
                    aggiungi(zaino, -1);
                    break;
                case "2":
                    modifica(zaino);
                    break;
                case "3":
                    elimina(zaino);
                    break;
                case "4":
                    visualizza(zaino);
                    break;
            }
            System.out.println();
        }
    }
}