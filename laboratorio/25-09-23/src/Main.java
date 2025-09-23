//campionato di ping pong
//chiedi nome e cognome

import java.io.IO;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;

public class Main {
    public static String[] iscritti = new String[10];
    public static int conta = 0;

    static public void stampa(String[] a, int size) {
        System.out.println("Archivio icritti al campionato: " + size + " iscritti");

        if (size == 0) {
            System.out.println("Non ci sono iscritti");
        }
        else {
            for (int i = 0; i < size; i++) {
                System.out.println((i + 1) + ") " + a[i]);
            }
        }
    }

    public static void main(String[] args) {
        iscritti[0] = "gorgo";
        iscritti[1] = "borno";
        iscritti[2] = "nania";
        conta = 3;

        stampa(iscritti, conta);

//        System.out.println("inserisci il nome del nuovo iscritto: ");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String nome = br.readLine();
//        non funziona

        String nuovo = IO.readln("Scrivi il nome del nuovo candidato: ");
        System.out.println("Benvenuto " + nuovo);
    }
}