import javax.sound.sampled.*;
import java.io.*;

/**
 * Classe utility per la riproduzione degli effetti sonori dell'applicazione scacchistica.
 * <p>
 * Tutti i metodi sono statici; la classe non è istanziabile.
 * La riproduzione avviene su un thread dedicato per non bloccare l'EDT.
 * Se l'audio è disattivato tramite {@link #setAudio(boolean)}, le chiamate
 * ai metodi di riproduzione non producono alcun effetto.
 * </p>
 */
public class SuoniScacchi {

    /** Indica se la riproduzione audio è attualmente abilitata. */
    private static boolean audio = true;

    /** Classe utility: costruttore privato per impedire l'istanziazione. */
    private SuoniScacchi() {}

    /**
     * Enumerazione dei suoni disponibili nell'applicazione.
     * Ogni costante è associata al nome del file audio nella cartella {@code suoni/}.
     */
    public enum Suono {
        /** Suono riprodotto quando una pedina viene spostata. */
        SPOSTAMENTO("move.wav"),
        /** Suono riprodotto quando una pedina viene mangiata. */
        MANGIATA("capture.wav"),
        /** Suono riprodotto alla vittoria di un giocatore. */
        VITTORIA("checkmate.wav"),
        /** Suono riprodotto all'inizio di una nuova partita. */
        INIZIOPARTITA("game_start.wav"),
        /** Suono riprodotto alla fine della partita. */
        FINEPARTITA("game_end.wav"),
        /** Suono riprodotto quando viene dato scacco al re. */
        SCACCO("check.wav"),
        /** Suono riprodotto quando il tempo di un giocatore è in esaurimento. */
        TEMPO("low_time.wav"),
        /** Suono riprodotto quando la scacchiera viene ruotata. */
        RUOTA("rotate.wav"),
        /** Suono riprodotto per le interazioni con il menu. */
        MENU("click1.wav"),
        /** Suono riprodotto alla conferma di un'azione. */
        CONFERMA("click2.wav"),
        /** Suono riprodotto alla modifica delle impostazioni del timer. */
        CAMBIOTEMPO("click3.wav"),
        /** Suono riprodotto all'apertura di un dialog di opzioni. */
        OPZIONIDIALOG("click4.wav");

        /** Nome del file audio associato alla costante, relativo alla cartella {@code suoni/}. */
        private final String file;

        /**
         * Costruisce una costante {@code Suono} con il nome del file audio specificato.
         *
         * @param file il nome del file audio nella cartella {@code suoni/}
         */
        Suono(String file) {
            this.file = file;
        }
    }

    /**
     * Riproduce il suono specificato su un thread dedicato.
     * <p>
     * Se l'audio è disattivato il metodo termina immediatamente senza
     * riprodurre alcun suono. Eventuali eccezioni durante il caricamento
     * o la riproduzione vengono silenziate.
     * </p>
     *
     * @param suono il suono da riprodurre
     */
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

    /**
     * Restituisce lo stato corrente dell'audio.
     *
     * @return {@code true} se l'audio è attivo, {@code false} altrimenti
     */
    public static boolean isAudioOn() {
        return audio;
    }

    /**
     * Abilita o disabilita la riproduzione audio.
     *
     * @param audio {@code true} per abilitare l'audio, {@code false} per disabilitarlo
     */
    public static void setAudio(boolean audio) {
        SuoniScacchi.audio = audio;
    }

    /**
     * Riproduce il suono di spostamento di una pedina.
     * @see Suono#SPOSTAMENTO
     */
    public static void spostamento() {
        riproduci(Suono.SPOSTAMENTO);
    }

    /**
     * Riproduce il suono di cattura di una pedina.
     * @see Suono#MANGIATA
     */
    public static void mangiata() {
        riproduci(Suono.MANGIATA);
    }

    /**
     * Riproduce il suono di vittoria.
     * @see Suono#VITTORIA
     */
    public static void vittoria() {
        riproduci(Suono.VITTORIA);
    }

    /**
     * Riproduce il suono di inizio partita.
     * @see Suono#INIZIOPARTITA
     */
    public static void inizioPartita() {
        riproduci(Suono.INIZIOPARTITA);
    }

    /**
     * Riproduce il suono di fine partita.
     * @see Suono#FINEPARTITA
     */
    public static void finePartita() {
        riproduci(Suono.FINEPARTITA);
    }

    /**
     * Riproduce il suono di scacco al re.
     * @see Suono#SCACCO
     */
    public static void scacco() {
        riproduci(Suono.SCACCO);
    }

    /**
     * Riproduce il suono di avvertimento per il tempo in esaurimento.
     * @see Suono#TEMPO
     */
    public static void tempo() {
        riproduci(Suono.TEMPO);
    }

    /**
     * Riproduce il suono di rotazione della scacchiera.
     * @see Suono#RUOTA
     */
    public static void ruota() {
        riproduci(Suono.RUOTA);
    }

    /**
     * Riproduce il suono di interazione con il menu.
     * @see Suono#MENU
     */
    public static void menu() {
        riproduci(Suono.MENU);
    }

    /**
     * Riproduce il suono di conferma di un'azione.
     * @see Suono#CONFERMA
     */
    public static void conferma() {
        riproduci(Suono.CONFERMA);
    }

    /**
     * Riproduce il suono di modifica delle impostazioni del timer.
     * @see Suono#CAMBIOTEMPO
     */
    public static void cambioTempo() {
        riproduci(Suono.CAMBIOTEMPO);
    }

    /**
     * Riproduce il suono di selezione in un dialog di opzioni.
     * @see Suono#OPZIONIDIALOG
     */
    public static void opzioniDialog() {
        riproduci(Suono.OPZIONIDIALOG);
    }
}