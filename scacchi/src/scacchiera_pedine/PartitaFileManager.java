package scacchiera_pedine;

import java.io.*;

public class PartitaFileManager {
    static final String percorsoFile = "partita.txt"; //package-private

    //fa in modo che la classe non possa essere istanziata
    private PartitaFileManager() {}

    //package-private
    static synchronized void scriviScacchiera(String percorso, int mosse, String scacchiera) {
        if (mosse < 0) return;
        if (mosse == 0) svuotaFile(percorso);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorso, true))) {
            writer.write(mosse + "\n" + scacchiera + "\n");
        }
        catch (IOException _) {}
    }

    //package-private
    static synchronized void scriviScacchiera(int mosse, String scacchiera) {
        scriviScacchiera(percorsoFile, mosse, scacchiera);
    }

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

    public static synchronized String leggiScacchiera(int mossa, boolean info) {
        return leggiScacchiera(percorsoFile, mossa, info);
    }

    public static synchronized String leggiScacchiera(String percorso, int mossa) {
        return leggiScacchiera(percorso, mossa, false);
    }

    public static synchronized String leggiScacchiera(int mossa) {
        return leggiScacchiera(percorsoFile, mossa, false);
    }

    public static synchronized void svuotaFile(String percorso) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorso))) {
            writer.write("");
        }
        catch (IOException _) {}
    }

    public static synchronized void svuotaFile() {
        svuotaFile(percorsoFile);
    }
}
