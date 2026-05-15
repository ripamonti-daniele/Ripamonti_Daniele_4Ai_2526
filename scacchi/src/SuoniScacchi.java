import javax.sound.sampled.*;
import java.io.*;

public class SuoniScacchi {
    public static boolean audio = true;

    public enum Suono {
        SPOSTAMENTO("move.wav"),
        MANGIATA("capture.wav"),
        VITTORIA("checkmate.wav"),
        PAREGGIO("draw.wav"),
        SCACCO("check.wav");
//        SELEZIONA("select.wav");

        private final String file;

        Suono(String file) {
            this.file = file;
        }
    }

    private static void riproduci(Suono suono) {
        if (!audio) return;
        new Thread(() -> {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(new File("suoni/" + suono.file));
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }
            catch (Exception _) {}
        }).start();
    }

    public static void spostamento() {
        riproduci(Suono.SPOSTAMENTO);
    }
    public static void mangiata() {
        riproduci(Suono.MANGIATA);
    }
    public static void vittoria() {
        riproduci(Suono.VITTORIA);
    }
    public static void pareggio() {
        riproduci(Suono.PAREGGIO);
    }
    public static void scacco() {
        riproduci(Suono.SCACCO);
    }
//    public static void seleziona() {
//        riproduci(Suono.SELEZIONA);
//    }
}