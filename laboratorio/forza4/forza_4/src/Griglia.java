import java.io.IO;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;

/**
 * Rappresenta la griglia di un gioco "Forza Quattro".
 *
 * <p>La griglia è implementata come un array bidimensionale {@code int[][]} in cui
 * il valore {@code -1} indica una cella vuota, mentre {@code 0} e {@code 1}
 * rappresentano i due giocatori. La classe fornisce metodi per aggiungere un disco,
 * controllare lo stato di vittoria/pareggio, cambiare turno e giocare in console.</p>
 */
public class Griglia {
    public final int LUNGHEZZA;
    public final int ALTEZZA;
    private final int[][] contenitore;
    private final String[] colore;
    private int turno;
    private final Map<Integer, String> dizionario;

    /**
     * Costruttore: inizializza dimensioni, contenitore, colori, dizionario e turno.
     * Inizializza inoltre tutte le celle del contenitore a {@code -1}.
     */
    public Griglia() {
        LUNGHEZZA = 7;
        ALTEZZA = 6;
        contenitore = new int[LUNGHEZZA][ALTEZZA];
        colore = new String[]{"\u001B[91m", "\u001B[93m", "\u001B[31m", "\u001B[33m", "\u001B[0m", "\u001B[92m", "\u001B[94m"};
        dizionario = Map.of(0, "rosso", 1, "giallo");
        turno = 1;
        inizializzaContenitore();
    }

    /**
     * Inizializza tutte le celle del {@code contenitore} impostandole a {@code -1}.
     *
     * <p>Metodo privato utilizzato dal costruttore e per resettare la griglia in caso
     * di una nuova partita.</p>
     */
    private void inizializzaContenitore() {
        for (int x = 0; x < LUNGHEZZA; x++) {
            for (int y = 0; y < ALTEZZA; y++) contenitore[x][y] = -1;
        }
    }

    /**
     * Restituisce il turno corrente.
     *
     * @return {@code 0} se è il turno del giocatore indicato come 0, {@code 1} altrimenti.
     */
    public int getTurno() {
        return turno;
    }

    /**
     * Restituisce il codice colore corrispondente all'indice fornito.
     *
     * <p>ATTENZIONE: l'argomento {@code index} è 1-based (1..n). Il metodo ritorna
     * la stringa di escape colore memorizzata nell'array {@code colore}.</p>
     *
     * @param index indice 1-based del colore desiderato
     * @return stringa del colore corrispondente (es. codice ANSI)
     * @throws ArrayIndexOutOfBoundsException se {@code index-1} non è valido
     */
    public String selezionaColore(int index) {
        return colore[index - 1];
    }

    /**
     * Restituisce una copia profonda del contenitore.
     *
     * <p>Viene creata e restituita una nuova matrice {@code int[][]} le cui righe
     * sono copie delle righe originali. Modificare l'array restituito non influisce
     * sulla griglia interna.</p>
     *
     * @return copia profonda dell'array {@code contenitore}
     */
    public int[][] getContenitore() {
        int[][] copiaContenitore = new int[contenitore.length][];
        for (int i = 0; i < contenitore.length; i++) copiaContenitore[i] = Arrays.copyOf(contenitore[i], contenitore[i].length);
        return copiaContenitore;
    }

    /**
     * Cambia il turno tra i due giocatori.
     *
     * <p>Se {@code turno} è 0 lo imposta a 1, se è 1 lo imposta a 0. Non gestisce
     * altri valori (assume che {@code turno} sia sempre 0 o 1).</p>
     */
    public void cambiaTurno() {
        if (turno == 0) turno = 1;
        else if (turno == 1) turno = 0;
    }

    /**
     * Inserisce un disco nella colonna specificata della griglia di gioco.
     *
     * <p>Il disco "cade" fino alla prima cella libera a partire dal basso nella colonna scelta.
     * Se la colonna è piena o il numero inserito non è valido, viene lanciata un'eccezione.</p>
     *
     * @param x la colonna (1-based) in cui inserire il disco
     * @throws InvalidParameterException se:
     *         <ul>
     *           <li>la colonna indicata è fuori dall'intervallo valido (x &lt; 1 o x &gt; LUNGHEZZA);</li>
     *           <li>la colonna è piena e non è possibile aggiungere altri dischi.</li>
     *         </ul>
     *
     * @implNote L’indice fornito è 1-based, ma internamente viene decrementato di 1
     *           per adattarsi all’indicizzazione 0-based dell’array {@code contenitore}.
     */
    public void addDisco(int x) throws InvalidParameterException {
        x--;
        if (x < 0 || x >= LUNGHEZZA) throw new InvalidParameterException("Numero colonna non valido");
        if (contenitore[x][0] != -1) throw new InvalidParameterException("Colonna piena");
        for (int y = ALTEZZA - 1; y >= 0; y--) {
            if (contenitore[x][y] == -1) {
                contenitore[x][y] = turno;
                break;
            }
        }
    }

    /**
     * Esegue i controlli sullo stato della partita.
     *
     * <p>Controlla in ordine:
     * <ol>
     *   <li>vittoria orizzontale (quattro in fila)</li>
     *   <li>vittoria verticale</li>
     *   <li>vittoria diagonale (entrambe le direzioni)</li>
     *   <li>pareggio (nessuna cella libera)</li>
     * </ol>
     * </p>
     *
     * @return {@code -1} se la partita continua (nessun vincitore ancora e ci sono mosse),
     *         {@code 0} se vince il giocatore 0 (rosso),
     *         {@code 1} se vince il giocatore 1 (giallo),
     *         {@code 2} se è pareggio (griglia piena senza vincitori).
     */
    public int controlli() {
        //controllo orizzontale
        for (int y = 0; y < ALTEZZA; y++) {
            boolean quattro = false;
            int val = -1;
            for (int x = 0; x < 4; x++) {
                val = contenitore[x][y];
                if (val == -1) continue;
                quattro = true;
                for (int i = 1; i < 4; i++) {
                    if (contenitore[x + i][y] != val) {
                        quattro = false;
                        break;
                    }
                }
                if (quattro) {
                    return val;
                }
            }
        }

        //controllo verticale
        for (int x = 0; x < LUNGHEZZA; x++) {
            boolean quattro = false;
            int val = -1;
            for (int y = 0; y < 3; y++) {
                val = contenitore[x][y];
                if (val == -1) continue;
                quattro = true;
                for (int i = 1; i < 4; i++) {
                    if (contenitore[x][y + i] != val) {
                        quattro = false;
                        break;
                    }
                }
                if (quattro) {
                    return val;
                }
            }
        }

        //controllo diagonale
        for (int y = 0; y < 3; y++) {
            boolean quattroDx = false;
            boolean quattroSx = false;
            int val = -1;
            for (int x = 0; x < LUNGHEZZA; x++) {
                val = contenitore[x][y];
                if (val == -1) continue;
                quattroDx = true;
                quattroSx = true;
                for (int i = 1; i < 4; i++) {
                    if (x <= 3) {
                        if (contenitore[x + i][y + i] != val) {
                            quattroDx = false;
                            if (x != 3) quattroSx = false;
                        }
                    }
                    if (x >= 3) {
                        if (contenitore[x - i][y + i] != val) {
                            quattroSx = false;
                            if (x != 3) quattroDx = false;
                        }
                    }
                }
                if (quattroDx || quattroSx) {
                    return val;
                }
            }
        }

        //controllo caselle libere
        boolean vuota = false;
        for (int x = 0; x < LUNGHEZZA; x++) {
            for (int y = 0; y < ALTEZZA; y++) {
                if (contenitore[x][y] == -1) {
                    vuota = true;
                    break;
                }
            }
            if (vuota) break;
        }
        if (!vuota) return 2;

        return -1;
    }

    /**
     * Avvia una partita di "Forza Quattro" in modalità console.
     * <p>
     * Il metodo gestisce il ciclo di gioco completo:
     * stampa la griglia, chiede al giocatore di scegliere una colonna,
     * inserisce il disco e verifica se qualcuno ha vinto o se c’è un pareggio.
     * Alla fine chiede se si vuole iniziare una nuova partita.
     * </p>
     *
     * <p>Durante l’inserimento della colonna:
     * <ul>
     *   <li>lancia {@link NumberFormatException} se l’input non è un numero;</li>
     *   <li>lancia {@link java.security.InvalidParameterException} se la colonna è piena o non valida.</li>
     * </ul>
     * </p>
     *
     * @implNote Il metodo richiama se stesso per riavviare una nuova partita
     *           quando l’utente sceglie “si”.
     */
    public void giocaConsole() {
        boolean ciclo = true;
        String msg = "";
        while (ciclo) {
            //controlli
            int c = controlli();
            switch (c) {
                case -1:
                    cambiaTurno();
                    break;
                case 0:
                    msg = selezionaColore(3) + "Vince il giocatore rosso" + selezionaColore(5);
                    ciclo = false;
                    break;
                case 1:
                    msg = selezionaColore(4) + "Vince il giocatore giallo" + selezionaColore(5);
                    ciclo = false;
                    break;
                case 2:
                    msg = selezionaColore(7) + "Pareggio" + selezionaColore(5);
                    ciclo = false;
                    break;
            }

            //messa a schermo griglia
            System.out.println(selezionaColore(7) + "\n        FORZA QUATTRO        " + selezionaColore(5));
            System.out.println("  1   2   3   4   5   6   7");
            for (int y = 0; y < ALTEZZA; y++) {
                System.out.println("-----------------------------");
                System.out.print("| ");
                for (int x = 0; x < LUNGHEZZA; x++) {
                    int val = contenitore[x][y];
                    String s = "O";
                    if (val == -1) System.out.print(selezionaColore(5) + "  | ");
                    else System.out.print(selezionaColore(val + 1) + s + selezionaColore(5) + " | ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------");

            //IO
            if (ciclo) {
                System.out.println(selezionaColore(6) + "Giocatore " + selezionaColore(turno + 1) + dizionario.get(turno) + selezionaColore(6) + " scegli una colonna" + selezionaColore(5));
                int colonna = 0;
                boolean errore = true;
                while (errore) {
                    try {
                        colonna = Integer.parseInt(IO.readln());
                        addDisco(colonna);
                        errore = false;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Errore: devi inserire un numero intero");
                    }
                    catch (InvalidParameterException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            else System.out.println(msg);
        }
        System.out.println("\nVuoi giocare ancora? (si/no)");
        String scelta = IO.readln().trim().toLowerCase();
        if (scelta.equals("si")) {
            turno = 1;
            inizializzaContenitore();
            giocaConsole();
        }
    }
}
