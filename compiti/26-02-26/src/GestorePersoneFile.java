import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorePersoneFile {
    public final static String SEPARATORE = ";";

    public static void salvaPersone(List<Persona> personeDaSalvare, String nomeFile) throws IOException {

        //creo il buffer writer
        BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(nomeFile));
        String personaTesto;

        //scorro le persone da salvare
        for (Persona p : personeDaSalvare) {
            //creo una stringa da salvare su file per ogni persona
            personaTesto = p.getClass().getSimpleName() + SEPARATORE + p.getNome() + SEPARATORE + p.getCognome() + SEPARATORE + p.getEta();

            if (p instanceof Bambino) personaTesto += SEPARATORE + ((Bambino) p).getDataDiNascita().toString();

            //scrivo su file
            bufferWriter.write(personaTesto);

            //metto un a capo su file
            bufferWriter.newLine();
        }

        bufferWriter.close();

    }

    public static List<Persona> caricaPersone(String nomeFile) throws IOException {
        //creo il BufferReader
        BufferedReader br = new BufferedReader(new FileReader(nomeFile));
        //creo la lista delle persone
        List<Persona> personeDaCaricare = new ArrayList<>();
        //creo la variabile per salvare la stringa completa
        String stringaCompleta;
        //finche la riga ha qualcosa dentro
        int riga = 0;
        while ((stringaCompleta = br.readLine()) != null) {
            riga++;
            //splitta la stringa
            String[] dati = stringaCompleta.split(SEPARATORE);
            if (dati.length != 5 && dati.length != 4) throw new IOException("Errore riga " +  riga + ": numero dati non valido");
            //salvo il nome, cognome, eta
            String nome = dati[1];
            String cognome = dati[2];
            String strEta = dati[3];
            int eta;
            if (!strEta.matches("[0-9]{1,3}")) throw new IOException("Errore riga " + riga + ": formato età non valido");
            else eta = Integer.parseInt(strEta);

            //carico tutto sulla lista che devo ritornare
            if (dati.length == 5 && dati[0].equals("Bambino")) {
                LocalDate dataDiNascita;
                try {
                    dataDiNascita = LocalDate.parse(dati[4]);
                }
                catch (Exception e) {
                    throw new IOException("Errore riga " + riga + ": formato data di nascita non valido");
                }
                personeDaCaricare.add(new Bambino(nome, cognome, eta, dataDiNascita));
            }
            else personeDaCaricare.add(new Persona(nome, cognome, eta));
        }
        //chiudo il BufferReader e faccio la return
        br.close();
        return personeDaCaricare;
    }

    @SuppressWarnings("unchecked")
    public static List<Persona> deSerializza(String nomeFile) throws IOException {
        List<Persona> p;
        try {
            FileInputStream file = new FileInputStream(nomeFile);
            ObjectInputStream input = new ObjectInputStream(file);
            p = (List<Persona>) input.readObject();
            file.close();
            return p;
        }
        catch (IOException e) {
            throw new IOException("Errore nell'apertura del file " + nomeFile);
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static void serializza(List<Persona> persone, String nomeFile) throws IOException {

        try {
            FileOutputStream file = new FileOutputStream(nomeFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(persone);
            file.close();
        } catch (IOException e) {
            throw new IOException("Errore nella scrittura del file " + nomeFile);
        }
    }
}