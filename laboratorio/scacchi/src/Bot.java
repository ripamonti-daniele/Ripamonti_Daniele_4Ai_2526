import scacchiera_pedine.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot {
    private final Scacchiera scacchiera;
    private final Color colore;
    private final int PROFONDITA;
    public static final Map<String, int[][]> vantaggioCasella;
    private final String[] mossePrecedenti;
    private boolean ricercaAvanzata;

    static {
        vantaggioCasella = new HashMap<>();

        vantaggioCasella.put("P", new int[][] {
                {99, 99, 99, 99, 99, 99, 99, 99},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                { 5,  5, 10, 25, 25, 10,  5,  5},
                { 0,  0,  0, 20, 20,  0,  0,  0},
                { 5, -5,-10,  0,  0,-10, -5,  5},
                { 5, 10, 10,-20,-20, 10, 10,  5},
                { 0,  0,  0,  0,  0,  0,  0,  0}
        });

        vantaggioCasella.put("C", new int[][] {
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-30,-30,-30,-30,-40,-50}
        });

        vantaggioCasella.put("A", new int[][] {
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-20,-10,-10,-10,-10,-10,-10,-20}
        });

        vantaggioCasella.put("T", new int[][] {
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 5, 10, 10, 10, 10, 10, 10,  5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                { 0,  0,  0,  5,  5,  0,  0,  0}
        });

        vantaggioCasella.put("Q", new int[][] {
                {-20,-10,-10, -5, -5,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5,  5,  5,  5,  0,-10},
                { -5,  0,  5,  5,  5,  5,  0, -5},
                {  0,  0,  5,  5,  5,  5,  0, -5},
                {-10,  5,  5,  5,  5,  5,  0,-10},
                {-10,  0,  5,  0,  0,  0,  0,-10},
                {-20,-10,-10, -5, -5,-10,-10,-20}
        });

        vantaggioCasella.put("R", new int[][] {
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-30,-40,-40,-50,-50,-40,-40,-30},
                {-20,-30,-30,-40,-40,-30,-30,-20},
                {-10,-20,-20,-20,-20,-20,-20,-10},
                { 20, 20,  0,  0,  0,  0, 20, 20},
                { 20, 30, 10,  0,  0, 10, 30, 20}
        });

        vantaggioCasella.put("Rend", new int[][] {
                {-50,-40,-30,-20,-20,-30,-40,-50},
                {-30,-20,-10,  0,  0,-10,-20,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-30,  0,  0,  0,  0,-30,-30},
                {-50,-30,-30,-30,-30,-30,-30,-50}
        });
    }

    public Bot(Scacchiera scacchiera, Color colore, int profondita, boolean ricercaAvanzata) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
        if (profondita < 1 || profondita > 7) throw new IllegalArgumentException("Profondità non valida: max 7 min 1");
        PROFONDITA = profondita;
        mossePrecedenti = new String[4];
        this.ricercaAvanzata = ricercaAvanzata;
    }

    public Color getColore() {
        return colore;
    }

    public boolean isRicercaAvanzata() {
        return ricercaAvanzata;
    }

    public void setRicercaAvanzata(boolean ricercaAvanzata) {
        this.ricercaAvanzata = ricercaAvanzata;
    }

    public int[][] muovi() {
        if (!scacchiera.getTurno().equals(colore)) throw new IllegalStateException("Il bot non può muovere se non è il suo turno");
        int[][] mossaMigliore = getMossa(scacchiera.getCaselle());
        if (mossaMigliore != null) {
            scacchiera.selezionaPedina(mossaMigliore[0]);
            if (!scacchiera.muoviPedina(mossaMigliore[1])) throw new IllegalStateException("Mossa trovata non valida");
        }
        return mossaMigliore;
    }

    private int mosseTotali(Pedina[][] caselle) {
        int tot = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p != null && p.getColore().equals(colore)) {
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, p.getPosizione(), colore);
                    if (mosse == null) continue;
                    tot += mosse.size();
                }
            }
        }
        return tot;
    }

    private void aggiungiMossa(String mossa) {
        for (int i = mossePrecedenti.length - 1; i > 0; i--) {
            mossePrecedenti[i] = mossePrecedenti[i - 1];
        }
        mossePrecedenti[0] = mossa;
    }

    public int[][] getMossa(Pedina[][] caselle) {
        int[][] mossa = null;
        int valoreMigliore = Integer.MIN_VALUE;

        int profondita = PROFONDITA - 1;
        if (ricercaAvanzata) {
            int tot = mosseTotali(caselle);
            if (tot <= 20 && !Scacchiera.isScaccoReCaselle(caselle, colore)) profondita++;
            if (tot <= 5) profondita++;
        }

        String stringaMossaMigliore = "";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null || !caselle[i][j].getColore().equals(colore)) continue;

                int[] pos = new int[]{i, j};

                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, colore);
                if (mosse == null) continue;

                for (int[] m : mosse) {
                    Pedina pezzoMosso = caselle[pos[0]][pos[1]].copy();
                    Pedina pezzoMangiato = null;
                    if (caselle[m[0]][m[1]] != null) pezzoMangiato = caselle[m[0]][m[1]].copy();
                    Pedina torreSalvata = null;
                    if (pezzoMosso instanceof Re && Math.abs(pos[1] - m[1]) == 2) {
                        if (pos[1] - m[1] == 2) torreSalvata = caselle[pos[0]][0].copy();
                        else torreSalvata = caselle[pos[0]][7].copy();
                    }
                    int enPassant = 0;
                    if (pezzoMosso instanceof Pedone && pezzoMangiato == null && m[1] != pos[1]) {
                        if (m[1] == pos[1] - 1) enPassant = -1;
                        else enPassant = 1;
                    }

                    if (!Scacchiera.muoviPedinaCaselle(caselle, mosse, pos, m)) continue;

                    if (Scacchiera.promozioneInSospesoCaselle(caselle) != null) Scacchiera.promozionePedoneCaselle(caselle, m, 1);

                    String stringaScacchiera = Scacchiera.getStringaScacchieraCaselle(caselle, true);
                    int val = 0;
                    if (!(stringaScacchiera.equals(mossePrecedenti[1]) && stringaScacchiera.equals(mossePrecedenti[3]) && mossePrecedenti[0].equals(mossePrecedenti[2]))) {
                        val = miniMax(caselle, profondita, false, Integer.MIN_VALUE, Integer.MAX_VALUE, coloreAvversario());
                    }

                    caselle[pos[0]][pos[1]] = pezzoMosso;
                    caselle[m[0]][m[1]] = pezzoMangiato;
                    if (torreSalvata != null) {
                        if (pos[1] - m[1] > 0) caselle[pos[0]][pos[1] - 1] = null;
                        else caselle[pos[0]][pos[1] + 1] = null;
                        caselle[torreSalvata.getPosizione()[0]][torreSalvata.getPosizione()[1]] = torreSalvata;
                    }
                    if (enPassant != 0) {
                        Color c = Color.white;
                        if (pezzoMosso.getColore().equals(c)) c = Color.black;
                        caselle[pos[0]][pos[1] + enPassant] = new Pedone(c, new int[]{pos[0], pos[1] + enPassant});
                    }

                    if (val > valoreMigliore) {
                        valoreMigliore = val;
                        mossa = new int[][]{{pos[0], pos[1]}, {m[0], m[1]}};
                        stringaMossaMigliore = stringaScacchiera;
                    }
                }
            }
        }
        aggiungiMossa(stringaMossaMigliore);
        return mossa;
    }

    private int miniMax(Pedina[][] caselle, int profondita, boolean massimizza, int alpha, int beta, Color turno) {
        Color prossimoTurno = Color.white;
        if (turno.equals(Color.white)) prossimoTurno = Color.black;

        StatoPartita sp = Scacchiera.statoPartitaCaselle(caselle, turno);

        if (profondita == 0 || sp != StatoPartita.IN_CORSO) return evaluation(caselle, sp, profondita);

        if (massimizza) {
            int max = Integer.MIN_VALUE;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(turno)) continue;

                    int[] pos = new int[]{i, j};
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, turno);
                    if (mosse == null) continue;

                    for (int[] m : mosse) {
                        Pedina pezzoMosso = caselle[pos[0]][pos[1]].copy();
                        Pedina pezzoMangiato = null;
                        if (caselle[m[0]][m[1]] != null) pezzoMangiato = caselle[m[0]][m[1]].copy();
                        Pedina torreSalvata = null;
                        if (pezzoMosso instanceof Re && Math.abs(pos[1] - m[1]) == 2) {
                            if (pos[1] - m[1] == 2) torreSalvata = caselle[pos[0]][0].copy();
                            else torreSalvata = caselle[pos[0]][7].copy();
                        }
                        int enPassant = 0;
                        if (pezzoMosso instanceof Pedone && pezzoMangiato == null && m[1] != pos[1]) {
                            if (m[1] == pos[1] - 1) enPassant = -1;
                            else enPassant = 1;
                        }

                        if (!Scacchiera.muoviPedinaCaselle(caselle, mosse, pos, m)) continue;
                        if (Scacchiera.promozioneInSospesoCaselle(caselle) != null) Scacchiera.promozionePedoneCaselle(caselle, m, 1);

                        int val = miniMax(caselle, profondita - 1, false, alpha, beta, prossimoTurno);
                        if (val > max) max = val;
                        if (max > alpha) alpha = max;

                        caselle[pos[0]][pos[1]] = pezzoMosso;
                        caselle[m[0]][m[1]] = pezzoMangiato;
                        if (torreSalvata != null) {
                            if (pos[1] - m[1] > 0) caselle[pos[0]][pos[1] - 1] = null;
                            else caselle[pos[0]][pos[1] + 1] = null;
                            caselle[torreSalvata.getPosizione()[0]][torreSalvata.getPosizione()[1]] = torreSalvata;
                        }
                        if (enPassant != 0) {
                            Color c = Color.white;
                            if (pezzoMosso.getColore().equals(c)) c = Color.black;
                            caselle[pos[0]][pos[1] + enPassant] = new Pedone(c, new int[]{pos[0], pos[1] + enPassant});
                        }

                        if (beta <= alpha) return max;
                    }
                }
            }
            return max;

        }
        else {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(turno)) continue;

                    int[] pos = new int[]{i, j};
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, turno);
                    if (mosse == null) continue;

                    for (int[] m : mosse) {
                        Pedina pezzoMosso = caselle[pos[0]][pos[1]].copy();
                        Pedina pezzoMangiato = null;
                        if (caselle[m[0]][m[1]] != null) pezzoMangiato = caselle[m[0]][m[1]].copy();
                        Pedina torreSalvata = null;
                        if (pezzoMosso instanceof Re && Math.abs(pos[1] - m[1]) == 2) {
                            if (pos[1] - m[1] == 2) torreSalvata = caselle[pos[0]][0].copy();
                            else torreSalvata = caselle[pos[0]][7].copy();
                        }
                        int enPassant = 0;
                        if (pezzoMosso instanceof Pedone && pezzoMangiato == null && m[1] != pos[1]) {
                            if (m[1] == pos[1] - 1) enPassant = -1;
                            else enPassant = 1;
                        }

                        if (!Scacchiera.muoviPedinaCaselle(caselle, mosse, pos, m)) continue;
                        if (Scacchiera.promozioneInSospesoCaselle(caselle) != null) Scacchiera.promozionePedoneCaselle(caselle, m, 1);

                        int val = miniMax(caselle, profondita - 1, true, alpha, beta, prossimoTurno);
                        if (val < min) min = val;
                        if (min < beta) beta = min;

                        caselle[pos[0]][pos[1]] = pezzoMosso;
                        caselle[m[0]][m[1]] = pezzoMangiato;
                        if (torreSalvata != null) {
                            if (pos[1] - m[1] > 0) caselle[pos[0]][pos[1] - 1] = null;
                            else caselle[pos[0]][pos[1] + 1] = null;
                            caselle[torreSalvata.getPosizione()[0]][torreSalvata.getPosizione()[1]] = torreSalvata;
                        }
                        if (enPassant != 0) {
                            Color c = Color.white;
                            if (pezzoMosso.getColore().equals(c)) c = Color.black;
                            caselle[pos[0]][pos[1] + enPassant] = new Pedone(c, new int[]{pos[0], pos[1] + enPassant});
                        }

                        if (beta <= alpha) return min;
                    }
                }
            }
            return min;
        }
    }

    private Color coloreAvversario() {
        if (colore.equals(Color.white)) return Color.black;
        else return Color.white;
    }

    private int evaluation(Pedina[][] caselle, StatoPartita statoPartita, int profondita) {
        if (statoPartita == StatoPartita.VITTORIA_BIANCO) {
            if (colore.equals(Color.white)) return Integer.MAX_VALUE - 10000 + 10 * profondita;
            else return Integer.MIN_VALUE + 10000 - 10 * profondita;
        }
        if (statoPartita == StatoPartita.VITTORIA_NERO) {
            if (colore.equals(Color.black)) return Integer.MAX_VALUE - 10000 + 10 * profondita;
            else return Integer.MIN_VALUE + 10000 - 10 * profondita;
        }
        if (statoPartita != StatoPartita.IN_CORSO) return 0;

        int evalTotale = 0;
        boolean endgame = Scacchiera.getMaterialeCaselle(caselle, colore) <= 13 && Scacchiera.getMaterialeCaselle(caselle, coloreAvversario()) <= 13;
        if (endgame && Scacchiera.isScaccoReCaselle(caselle, coloreAvversario())) evalTotale += 30;
        if (endgame && Scacchiera.isScaccoReCaselle(caselle, colore)) evalTotale -= 30;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;
                int evalPedina = evaluationCasella(caselle, i, j, endgame);
                if (caselle[i][j].getColore().equals(colore)) evalTotale += evalPedina;
                else evalTotale -= evalPedina;
            }
        }

        return evalTotale;
    }

    private int evaluationCasella (Pedina[][]caselle, int i, int j, boolean endgame){
        if (caselle[i][j] == null) return 0;
        Pedina p = caselle[i][j];
        int materiale = p.getMateriale();
        int vantaggioPosizione;
        String nomePedina = p.getClass().getSimpleName().substring(0, 1);
        if (p instanceof Regina) nomePedina = "Q";
        else if (p instanceof Re && endgame) nomePedina += "end";
        if (p.getColore().equals(Color.white)) vantaggioPosizione = vantaggioCasella.get(nomePedina)[i][j];
        else vantaggioPosizione = vantaggioCasella.get(nomePedina)[7 - i][j];
        vantaggioPosizione *= materiale;

        int pedoneDoppio = 0;
        int pedoneIsolato = 0;

        if (p instanceof Pedone) {
            vantaggioPosizione *= 2;
            boolean pedoneSx = false;
            boolean pedoneDx = false;
            for (int vert = 0; vert < 8; vert++) {
                if (!pedoneSx && j > 0 && caselle[vert][j - 1] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneSx = true;
                if (!pedoneDx && j < 7 && caselle[vert][j + 1] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneDx = true;
                if (vert != i && caselle[vert][j] instanceof Pedone ped && ped.getColore().equals(p.getColore())) pedoneIsolato += 20 * materiale;
            }
            if (!pedoneSx && !pedoneDx) pedoneIsolato = 25 * materiale;
        }

        return materiale * 150 + vantaggioPosizione - pedoneDoppio - pedoneIsolato;
    }
}
