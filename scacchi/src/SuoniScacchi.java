import javax.sound.sampled.*;
import java.io.*;

public class SuoniScacchi {
    public static boolean audio = true;

    public enum Suono {
        SPOSTAMENTO("move.wav"),
        MANGIATA("capture.wav"),
        VITTORIA("checkmate.wav"),
        FINEPARTITA("game_end.wav"),
        SCACCO("check.wav"),
        TEMPO("low_time.wav"),
        RUOTA("rotate.wav"),
        MENU("click1.wav"),
        CONFERMA("click2.wav"),
        CAMBIOTEMPO("click3.wav"),
        OPZIONIDIALOG("click4.wav"),
        AUDIOON("audio_on.wav");

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

    public static void finePartita() {
        riproduci(Suono.FINEPARTITA);
    }

    public static void scacco() {
        riproduci(Suono.SCACCO);
    }

    public static void tempo() {
        riproduci(Suono.TEMPO);
    }

    public static void ruota() {
        riproduci(Suono.RUOTA);
    }

    public static void menu() {
        riproduci(Suono.MENU);
    }

    public static void conferma() {
        riproduci(Suono.CONFERMA);
    }

    public static void cambioTempo() {
        riproduci(Suono.CAMBIOTEMPO);
    }

    public static void opzioniDialog() {
        riproduci(Suono.OPZIONIDIALOG);
    }

    public static void audioOn() {
        riproduci(Suono.AUDIOON);
    }
}