import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorePersoneFile {

    public final static String NOME_FILE = "persone.txt";
    public final static String SEPARATORE = ";";

    public static void salvaPersone(List<Persona> personeDaSalvare) throws IOException {

        //creo il buffer writer
        BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(NOME_FILE));
        String personaTesto = "";

        //scorro le persone da salvare
        for (Persona p : personeDaSalvare) {
            //creo una stringa da salvare su file per ogni persona
            personaTesto = p.getNome() + SEPARATORE + p.getCognome() + SEPARATORE + p.getEta();

            //scrivo su file
            bufferWriter.write(personaTesto);

            //metto un a capo su file
            bufferWriter.newLine();
        }

        bufferWriter.close();

    }

    public static List<Persona> caricaPersone() throws IOException {
        //creo il BufferReader
        BufferedReader br = new BufferedReader(new FileReader(NOME_FILE));
        //creo la lista delle persone
        List<Persona> personeDaCaricare = new ArrayList<>();
        //creo la variabile per salvare la stringa completa
        String stringaCompleta = "";
        //finche la riga ha qualcosa dentro
        int riga = 0;
        while ((stringaCompleta = br.readLine()) != null) {
            riga++;
            //splitta la stringa
            String[] dati = stringaCompleta.split(SEPARATORE);
            if (dati.length != 3) throw new IOException("Errore riga " +  riga + ": non sono stati trovati 3 dati nella lettura di persona");
            //salvo il nome, cognome, eta
            String nome = dati[0];
            String cognome = dati[1];
            String strEta = dati[2];
            int eta;
            if (!strEta.matches("[0-9]{1,3}")) throw new IOException("Errore riga " + riga + ": formato età non valido");
            else eta = Integer.parseInt(strEta);
            //carico tutto sulla lista che devo ritornare
            personeDaCaricare.add(new Persona(nome, cognome, eta));
        }
        //chiudo il BufferReader e faccio la return
        br.close();
        return personeDaCaricare;
    }

/*
    public static List<Persona> caricaPersone() throws IOException{
        List<Persona> lista= new ArrayList<>();
        //creo un buffer reader
        BufferedReader br = new BufferedReader(new FileReader(NOME_FILE));
        List<String> righe;
        righe = br.readAllLines();
        for (String riga:righe){
            //leggo il file fino all'ultima riga

            //per ogni riga eseguo la split sul carattere "|"
            String nome =riga.split(SEPARATORE)[0] ;
            String cognome = riga.split(SEPARATORE)[1];
            int eta = Integer.parseInt(riga.split(SEPARATORE)[2]);
            //prendo le 3 informazioni, e creo una Persona
            Persona p = new Persona(nome,cognome,eta);
            lista.add(p);

        }
        br.close();
        return lista;
        //chiudo il buffer reader
    }
 */

}