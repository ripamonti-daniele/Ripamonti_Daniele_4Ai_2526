import javax.sound.sampled.*;
import java.io.*;

public class SuoniScacchi {

    public enum Suono {
        //file da convertire in .waw
        SPOSTAMENTO("move.mp3"),
        MANGIATA("capture.mp3"),
        VITTORIA("checkmate.mp3"),
//        PAREGGIO("pareggio.wav");
        SCACCO("check.mp3"),
        SELEZIONA("select.mp3");

        private final String file;

        Suono(String file) {
            this.file = file;
        }
    }

    private static void riproduci(Suono suono) {
        new Thread(() -> {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(new File("suoni/" + suono.file));
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP) clip.close();
                });
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
//    public static void pareggio() {
//        riproduci(Suono.PAREGGIO);
//    }
    public static void scacco() {
        riproduci(Suono.SCACCO);
    }
    public static void seleziona() {
        riproduci(Suono.SELEZIONA);
    }
}