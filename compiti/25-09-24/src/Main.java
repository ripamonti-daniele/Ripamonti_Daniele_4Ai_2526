import java.io.IO;

public class Main {
    public static String[] zaino = new String[10];

    public static void aggiungi(String[] oggetti, int index) {
        if (oggetti[9] != null && index < 0) {
            System.out.println("lo zaino è pieno");
        }
        else {
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

                for (int i = 0; i < oggetti.length; i++) {
                    if (oggetti[i] == null) {
                        break;
                    } else if (oggetti[i].equals(oggetto)) {
                        errore = true;
                        System.out.println("quest'oggetto è già presente nello zaino");
                        break;
                    }
                }
            }

            if (index < 0) {
                for (int i = 0; i < oggetti.length; i++) {
                    if (oggetti[i] == null) {
                        oggetti[i] = oggetto;
                        break;
                    }
                }
            }
            else {
                oggetti[index] = oggetto;
            }
        }
    }

    public static void modifica(String[] oggetti) {
        if (oggetti[0] == null) {
            System.out.println("lo zaino è vuoto");
        }
        else {
            boolean errore = true;

            while (errore) {
                String oggetto_vecchio = IO.readln("scrivi l'oggetto che vuoi sostituire: ");
                oggetto_vecchio = oggetto_vecchio.toLowerCase().trim();
                errore = false;

                for (int i = 0; i < oggetti.length; i++) {
                    if (oggetti[i] == null) {
                        System.out.println("quest'oggetto non è nello zaino");
                        errore = true;
                        break;
                    } else if (oggetti[i].equals(oggetto_vecchio)) {
                        aggiungi(oggetti, i);
                        break;
                    }
                }
            }
        }
    }

    public static void elimina(String[] oggetti) {
        if  (oggetti[0] == null) {
            System.out.println("lo zaino è vuoto");
        }
        else {
            boolean errore = true;

            while (errore) {
                String oggetto_vecchio = IO.readln("scrivi l'oggetto che vuoi rimuovere: ");
                oggetto_vecchio = oggetto_vecchio.toLowerCase().trim();

                for (int i = 0; i < oggetti.length; i++) {
                    if (oggetti[i] == null) {
                        System.out.println("quest'oggetto non è nello zaino");
                        break;
                    } else if (oggetti[i].equals(oggetto_vecchio)) {
                        for (int j = i; j < (oggetti.length - 1); j++) {
                            if (oggetti[j] != null) {
                                oggetti[j] = oggetti[j + 1];
                            }
                        }
                        for (int j = 0; j < oggetti.length; j++) {
                            if (oggetti[j] == null || oggetti[j + 1] == null) {
                                break;
                            }
                            if (oggetti[j].equals(oggetti[j + 1])) {
                                oggetti[j + 1] = null;
                            }
                        }
                        errore = false;
                        break;
                    }
                }
            }
        }
    }

    public static void visualizza(String[] oggetti) {
        if (oggetti[0] == null) {
            System.out.println("lo zaino è vuoto");
        }
        else {
            System.out.println("oggetti nello zaino:");
            for (int i = 0; i < oggetti.length; i++) {
                if (oggetti[i] == null) {
                    break;
                }
                else {
                    System.out.println((i + 1) + ") " + oggetti[i]);
                }
            }
        }
    }

    public static int menu() {
        int[] opzioni = {0, 1, 2, 3, 4};
        while (true) {
            System.out.println("inserisci 1 per aggiungere un oggetto allo zaino");
            System.out.println("inserisci 2 per sostituire un oggetto nello zaino");
            System.out.println("inserisci 3 per togliere un oggetto dallo zaino");
            System.out.println("inserisci 4 per visualizzare gli oggetti nello zaino");
            System.out.println("inserisci 0 per uscire");

            try {
                int scelta = Integer.parseInt(IO.readln());
                for (int i = 0; i < opzioni.length; i++) {
                    if (scelta == opzioni[i]) {
                        return scelta;
                    }
                }
                System.out.println("scelta non disponibile");
            }
            catch (NumberFormatException e) {
                System.out.println("scelta non valida");
            }
        }
    }

    public static void main(String[] args) {
        int scelta = -1;
        while (scelta != 0) {
            scelta = menu();
            if (scelta == 1) {
                aggiungi(zaino, -1);
            }
            else if (scelta == 2) {
                modifica(zaino);
            }
            else if (scelta == 3) {
                elimina(zaino);
            }
            else if (scelta == 4) {
                visualizza(zaino);
            }
            System.out.println();
        }
    }
}