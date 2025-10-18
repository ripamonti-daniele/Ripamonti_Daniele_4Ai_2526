import java.io.IO;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class Tris {
    private final char[][] griglia;
    public final int LUNGHEZZA_GRIGLIA;
    public final int ALTEZZA_GRIGLIA;
    public final char giocatoreX;
    public final char giocatoreO;
    public final char casellaVuota;
    public final HashMap<Character, Integer> diozionario;

    /**
     * Costruttore: inizializza la griglia e i caratteri dei giocatori.
     */
    public Tris() {
        LUNGHEZZA_GRIGLIA = 3;
        ALTEZZA_GRIGLIA = 3;
        giocatoreX = 'X';
        giocatoreO = 'O';
        casellaVuota = ' ';
        griglia = new char[LUNGHEZZA_GRIGLIA][ALTEZZA_GRIGLIA];
        inizializzaGriglia();
        diozionario = new HashMap<>();
        diozionario.put('A', 0);
        diozionario.put('B', 1);
        diozionario.put('C', 2);
    }

    /**
     * Inizializza la griglia riempiendola con caselle vuote.
     */
    private void inizializzaGriglia() {
        for(int x = 0; x < ALTEZZA_GRIGLIA; x++) {
            for(int y = 0; y < LUNGHEZZA_GRIGLIA; y++) {
                griglia[x][y] = casellaVuota;
            }
        }
    }

    /**
     * Restituisce il segno dell'altro giocatore.
     *
     * @param segno Segno corrente ('X' o 'O')
     * @return Il segno opposto
     * @throws InvalidParameterException Se il segno non è valido
     */
    public char cambiaSegno(char segno) throws InvalidParameterException {
        if (segno != giocatoreO && segno != giocatoreX) throw new InvalidParameterException("Segno non valido");
        if (segno == giocatoreO) return giocatoreX;
        else return giocatoreO;
    }

    /**
     * Inserisce un segno nella griglia in base alle coordinate.
     *
     * @param segno Segno del giocatore ('X' o 'O')
     * @param x Lettera della colonna ('A', 'B', 'C')
     * @param y Numero della riga (1-3)
     * @throws InvalidParameterException Se il segno o le coordinate non sono valide
     * @throws Exception Se la casella è già occupata
     */
    public void addSegno(char segno, char x, int y) throws InvalidParameterException, Exception {
        y--;
        if (segno != giocatoreO && segno != giocatoreX) throw new InvalidParameterException("Segno non valido");
        if (y < 0 || y >= ALTEZZA_GRIGLIA || (!diozionario.containsKey(x))) throw new InvalidParameterException("Casella inesistente");
        if (griglia[y][diozionario.get(x)] != casellaVuota) throw new Exception("Casella già occupata");
        griglia[y][diozionario.get(x)] = segno;
    }

    /**
     * Controlla l'esito della partita.
     *
     * @param segno Segno del giocatore da controllare
     * @return 1 se il giocatore ha vinto, 2 se la griglia è piena (pareggio), 0 se la partita continua
     */
    public int controlli(char segno) {
        //controllo orizzontale
        for(char[] y : griglia) {
            boolean tris = true;
            for(char x : y) {
               if (x != segno) {
                   tris = false;
                   break;
               }
            }
            if (tris) return 1;
        }

        //controllo verticale
        for (int y = 0; y < ALTEZZA_GRIGLIA; y++) {
            boolean tris = true;
            for (int x = 0; x < LUNGHEZZA_GRIGLIA; x++) {
                if (griglia[x][y] != segno) {
                    tris = false;
                    break;
                }
            }
            if (tris) return 1;
        }

        //controllo diagonale
        int x = 0;
        int y = 0;
        boolean tris = true;
        while (x < LUNGHEZZA_GRIGLIA && y < ALTEZZA_GRIGLIA) {
            if (griglia[y][x] != segno) {
                tris = false;
                break;
            }
            x++;
            y++;
        }
        if (tris) return 1;

        x = LUNGHEZZA_GRIGLIA - 1;
        y = 0;
        tris = true;
        while (x >= 0 && y < ALTEZZA_GRIGLIA) {
            if (griglia[y][x] != segno) {
                tris = false;
                break;
            }
            x--;
            y++;
        }
        if (tris) return 1;

        //controlla se è piena
        boolean piena = true;
        for (char[] verticale : griglia) {
            for (char orizzontale : verticale) {
                if (orizzontale == casellaVuota) {
                    piena = false;
                    break;
                }
            }
            if (!piena) break;
        }
        if (piena) return 2;

        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale della griglia di gioco.
     *
     * @return Stringa che rappresenta la griglia del Tris
     */
    public String getStringaGriglia() {
        StringBuilder s = new StringBuilder(" A   B   C \n");
        for (int y = 0; y < ALTEZZA_GRIGLIA; y++) {
            for (int x = 0; x < LUNGHEZZA_GRIGLIA; x++) {
                s.append(" ");
                s.append(griglia[y][x]);
                s.append(" ");

                if (x == LUNGHEZZA_GRIGLIA - 1) {
                    s.append(" ");
                    s.append(y + 1);
                    s.append("\n");
                }
                else s.append("|");
            }
            if (y < ALTEZZA_GRIGLIA - 1) s.append("---|---|---\n");
        }
        s.append("\n");
        return s.toString();
    }

    /**
     * Gestisce l'inserimento della mossa di un giocatore tramite input console.
     *
     * @param segno Segno del giocatore ('X' o 'O')
     */
    private void occupaCasellaConsole(char segno) {
        System.out.println("Giocatore " + segno + " inserisci la casella che vuoi occupare (esempio: A1)");
        boolean errore = true;

        while (errore) {
            String casella = IO.readln().trim().toUpperCase();

            if (casella.length() != 2) System.out.println("Formato casella non valido");
            else {
                try {
                    addSegno(segno, casella.charAt(0), Character.getNumericValue(casella.charAt(1)));
                    System.out.println();
                    errore = false;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Avvia una partita di Tris giocata tramite console.
     * Il gioco continua fino a quando un giocatore vince o si verifica un pareggio.
     * Alla fine chiede se l'utente vuole giocare di nuovo.
     */
    public void giocaConsole() {
        char segno = giocatoreO;
        int esito = 0;

        while (esito == 0) {
            segno = cambiaSegno(segno);
            System.out.print(getStringaGriglia());
            occupaCasellaConsole(segno);
            esito = controlli(segno);
        }

        System.out.print(getStringaGriglia());
        if (esito == 1) System.out.println("Vince il giocatore " + segno + "\n");
        else System.out.println("Pareggio\n");

        System.out.println("Vuoi giocare ancora? (si/no)");
        String scelta = IO.readln().trim().toLowerCase();
        if (scelta.equals("si")) {
            System.out.println();
            inizializzaGriglia();
            giocaConsole();
        }
    }
}
