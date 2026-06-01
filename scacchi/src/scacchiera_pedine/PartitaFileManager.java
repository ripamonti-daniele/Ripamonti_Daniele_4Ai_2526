package scacchiera_pedine;
import java.io.*;

/**
 * Gestore della persistenza su file degli stati della partita di scacchi.
 * <p>
 * Fornisce metodi statici sincronizzati per scrivere e leggere la
 * rappresentazione testuale della scacchiera a ogni mossa, consentendo
 * il rilevamento delle ripetizioni di posizione e la ricostruzione della
 * storia della partita. Tutti gli accessi al file sono thread-safe.
 * </p>
 * <p>
 * La classe non può essere istanziata.
 * </p>
 * <p>
 * Il file ha il seguente formato: ogni stato è composto da un indice di mossa
 * su una riga, seguito da 8 righe con la griglia della scacchiera e,
 * opzionalmente, da una riga con le informazioni su arrocco ed en passant
 * (vedere {@link Scacchiera#getStringaScacchiera(boolean)}).
 * Ogni blocco occupa quindi 9 o 10 righe; gli stati sono scritti in sequenza
 * a partire dalla mossa {@code 0}.
 * </p>
 *
 * @see Scacchiera
 */
public class PartitaFileManager {

    /**
     * Percorso predefinito del file in cui vengono salvati gli stati della
     * partita. Visibilità package-private per consentirne l'uso diretto da
     * parte delle classi del package.
     */
    static final String percorsoFile = System.getProperty("user.home") + File.separator + "PartitaScacchi.txt";

    /**
     * Costruttore privato che impedisce l'istanziazione della classe.
     */
    private PartitaFileManager() {}

    // -------------------------------------------------------------------------
    // Scrittura
    // -------------------------------------------------------------------------

    /**
     * Scrive lo stato della scacchiera alla mossa indicata nel file specificato,
     * in modalità append.
     * <p>
     * Se {@code mosse} è {@code 0}, il file viene azzerato prima della
     * scrittura. Se {@code mosse} è negativo, il metodo non esegue nulla.
     * Gli errori di I/O vengono silenziosamente ignorati.
     * </p>
     * <p>
     * Visibilità package-private: questo metodo è destinato esclusivamente
     * all'uso interno del package.
     * </p>
     *
     * @param percorso  percorso del file su cui scrivere
     * @param mosse     indice della mossa corrente; deve essere ≥ {@code 0}
     * @param scacchiera rappresentazione testuale della scacchiera da salvare
     */
    static synchronized void scriviScacchiera(String percorso, int mosse, String scacchiera) {
        if (mosse < 0) return;
        if (mosse == 0) svuotaFile(percorso);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorso, true))) {
            writer.write(mosse + "\n" + scacchiera + "\n");
        }
        catch (IOException _) {}
    }

    /**
     * Scrive lo stato della scacchiera alla mossa indicata nel file predefinito
     * {@link #percorsoFile}, in modalità append.
     * <p>
     * Visibilità package-private: questo metodo è destinato esclusivamente
     * all'uso interno del package.
     * </p>
     *
     * @param mosse      indice della mossa corrente; deve essere ≥ {@code 0}
     * @param scacchiera rappresentazione testuale della scacchiera da salvare
     * @see #scriviScacchiera(String, int, String)
     */
    static synchronized void scriviScacchiera(int mosse, String scacchiera) {
        scriviScacchiera(percorsoFile, mosse, scacchiera);
    }

    // -------------------------------------------------------------------------
    // Lettura
    // -------------------------------------------------------------------------

    /**
     * Legge e restituisce la rappresentazione testuale della scacchiera salvata
     * alla mossa indicata nel file specificato.
     * <p>
     * Ogni stato occupa un blocco di 10 righe nel file (1 riga di indice +
     * 8 righe di griglia + 1 riga di informazioni); il metodo si posiziona
     * direttamente al blocco corrispondente a {@code mossa} e ne legge le
     * 8 righe della griglia più, se {@code info} è {@code true}, la riga
     * aggiuntiva con le informazioni su arrocco ed en passant.
     * </p>
     *
     * @param percorso percorso del file da leggere
     * @param mossa    indice della mossa da recuperare; deve essere ≥ {@code 0}
     * @param info     {@code true} per includere la riga con le informazioni su
     *                 arrocco ed en passant
     * @return stringa con la rappresentazione della scacchiera alla mossa
     *         indicata, oppure {@code null} se {@code mossa} è negativa, se
     *         il file non contiene dati sufficienti o in caso di errore di I/O
     */
    public static synchronized String leggiScacchiera(String percorso, int mossa, boolean info) {
        if (mossa < 0) return null;
        StringBuilder scacchiera = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(percorso))) {
            for (int i = 0; i < mossa * 10 + 1; i++) reader.readLine();
            int iterazioni = 8;
            if (info) iterazioni++;
            for (int i = 0; i < iterazioni; i++) {
                String s = reader.readLine();
                if (s == null) return null;
                scacchiera.append(s);
                if (i < iterazioni - 1) scacchiera.append("\n");
            }
        }
        catch (IOException _) {
            return null;
        }

        return scacchiera.toString();
    }

    /**
     * Legge e restituisce la rappresentazione testuale della scacchiera salvata
     * alla mossa indicata nel file predefinito {@link #percorsoFile}.
     *
     * @param mossa indice della mossa da recuperare; deve essere ≥ {@code 0}
     * @param info  {@code true} per includere le informazioni su arrocco ed
     *              en passant
     * @return stringa con la rappresentazione della scacchiera, oppure
     *         {@code null} in caso di errore o dati insufficienti
     * @see #leggiScacchiera(String, int, boolean)
     */
    public static synchronized String leggiScacchiera(int mossa, boolean info) {
        return leggiScacchiera(percorsoFile, mossa, info);
    }

    /**
     * Legge e restituisce la rappresentazione testuale della scacchiera salvata
     * alla mossa indicata nel file specificato, senza le informazioni aggiuntive
     * su arrocco ed en passant.
     * Equivale a {@code leggiScacchiera(percorso, mossa, false)}.
     *
     * @param percorso percorso del file da leggere
     * @param mossa    indice della mossa da recuperare; deve essere ≥ {@code 0}
     * @return stringa con la rappresentazione della scacchiera, oppure
     *         {@code null} in caso di errore o dati insufficienti
     * @see #leggiScacchiera(String, int, boolean)
     */
    public static synchronized String leggiScacchiera(String percorso, int mossa) {
        return leggiScacchiera(percorso, mossa, false);
    }

    /**
     * Legge e restituisce la rappresentazione testuale della scacchiera salvata
     * alla mossa indicata nel file predefinito {@link #percorsoFile}, senza le
     * informazioni aggiuntive su arrocco ed en passant.
     * Equivale a {@code leggiScacchiera(percorsoFile, mossa, false)}.
     *
     * @param mossa indice della mossa da recuperare; deve essere ≥ {@code 0}
     * @return stringa con la rappresentazione della scacchiera, oppure
     *         {@code null} in caso di errore o dati insufficienti
     * @see #leggiScacchiera(String, int, boolean)
     */
    public static synchronized String leggiScacchiera(int mossa) {
        return leggiScacchiera(percorsoFile, mossa, false);
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Svuota il file nel percorso indicato sovrascrivendolo con una stringa
     * vuota. Gli errori di I/O vengono silenziosamente ignorati.
     *
     * @param percorso percorso del file da azzerare
     */
    public static synchronized void svuotaFile(String percorso) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorso))) {
            writer.write("");
        }
        catch (IOException _) {}
    }

    /**
     * Svuota il file predefinito {@link #percorsoFile} sovrascrivendolo con
     * una stringa vuota.
     *
     * @see #svuotaFile(String)
     */
    public static synchronized void svuotaFile() {
        svuotaFile(percorsoFile);
    }

    /**
     * Elimina il file al percorso specificato.
     * <p>
     * Se l'eliminazione fallisce, svuota il contenuto del file tramite
     * {@link #svuotaFile()} come operazione di fallback.
     * </p>
     *
     * @param percorso il percorso del file da eliminare
     */
    public static synchronized void eliminaFile(String percorso) {
        boolean deleted = new File(percorso).delete();
        if (!deleted) svuotaFile();
    }

    /**
     * Elimina il file di partita predefinito ({@link #percorsoFile}).
     * <p>
     * Delega a {@link #eliminaFile(String)}.
     * </p>
     */
    public static synchronized void eliminaFile() {
        eliminaFile(percorsoFile);
    }
}
