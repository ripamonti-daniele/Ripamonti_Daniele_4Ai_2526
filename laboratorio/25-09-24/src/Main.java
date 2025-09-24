import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static int inserisci(String[] a, int s) throws IOException {
        if (s == a.length) {
            System.out.println("impossibile inserire: archivio pieno");
        }
        else {
            System.out.println("nome iscritto:");
            String nome = br.readLine();
            a[s] = nome;
            s++;
        }
        return s;
    }

    public static void stampa(String[] a, int s) {
        System.out.println("Archiovio iscritti: " + s + " iscritti");
        if (s == 0) {
            System.out.println("al momento non ci sono iscritti");
        }
        else {
            for (int i = 0; i < s; i++) {
                System.out.println((i + 1) + ") " + a[i]);
            }
        }
    }

    public static int elimina(String[] a, int s) throws IOException {
        if (s == 0) System.out.println("non ci sono iscritti da eliminare");
        else {
            int s1 = s;
            System.out.println("quale iscritto vuoi eliminare?");
            String nome = br.readLine();
            for (int i = 0; i < s; i++) {
                if (nome.equals(a[i])) {
                    s--;
                    if (i == a.length - 1) a[i] = null;
                    else {
                        for (int j = i; j < a.length - 1; j++) {
                            a[j] = a[j + 1];
                        }
                    }
                }
            }
            if (s == s1) System.out.println("nome non trovato in archivio");
            else System.out.println("alunno eliminato con successo");
        }
        return s;
    }

    public static void modifica(String[] a, int s) throws IOException {
        if (s == 0) System.out.println("non ci sono iscritti da modificare");
        else {
            boolean modificato = false;
            System.out.println("quale iscritto vuoi modificare?");
            String nome = br.readLine();
            for (int i = 0; i < s; i++) {
                if (nome.equals(a[i])) {
                    modificato = true;
                    System.out.println("scrivi il nome del nuovo iscritto");
                    a[i] = br.readLine();
                }
            }
            if (modificato) System.out.println("nome modificato con successo");
            else System.out.println("nome non trovato");
        }
    }

    public static void nominativo(String[] a, int s) throws IOException {
        if (s == 0) System.out.println("non ci sono iscritti da cercare");
        else {
            boolean trovato = false;
            System.out.println("scrivi il nome del nominativo da cercare");
            String nome = br.readLine();
            for (int i = 0; i < s;i++) {
                if (a[i].equals(nome)) {
                    System.out.println(nome + " è presente nella lista degli iscritti");
                    trovato = true;
                }
            }
            if (!trovato) System.out.println(nome + " non è presente nella lista degli iscritti");
        }
    }

    public static void main(String[] args) throws IOException {
        String[] iscritti = new String[10];
        int count = 0;

        String scelta = "";

        do {
            System.out.println("MENU'");
            System.out.println("1) aggiungi iscritto");
            System.out.println("2) visualizza tutto");
            System.out.println("3) cancella iscrizione");
            System.out.println("4) modifica nominativo");
            System.out.println("5) cerca un nominativo");
            System.out.println("q) esci");
            System.out.println("cosa vuoi fare?");
            scelta = br.readLine();
            switch (scelta) {
                case "1":
                    count = inserisci(iscritti, count);
                    break;
                case "2":
                    stampa(iscritti, count);
                    break;
                case "3":
                    count = elimina(iscritti, count);
                    break;
                case "4":
                    modifica(iscritti, count);
                    break;
                case "5":
                    nominativo(iscritti, count);
                    break;
                case "q":
                    System.out.println("bella scech");
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
            System.out.println();

        } while (!scelta.equals("q"));
    }
}
