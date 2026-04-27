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

    static {
        vantaggioCasella = new HashMap<>();

        vantaggioCasella.put("P", new int[][] {
                {  0,   0,   0,   0,   0,   0,   0,   0},
                { 78,  83,  86,  73, 102,  82,  85,  90},
                {  7,  29,  21,  44,  40,  31,  44,   7},
                {-17,  16,  18,  30,  28,   0,  15, -13},
                {-26,   3,  10,  22,  21,   1,   0, -23},
                {-22,   9,   5, -11, -10,  -2,   3, -19},
                {-31,   8,  -7, -37, -36,   7,   3, -31},
                {  0,   0,   0,   0,   0,   0,   0,   0}
        });

        vantaggioCasella.put("C", new int[][] {
                {-66, -53, -75, -75, -10, -55, -58, -70},
                { -3,  -6, 100, -36,   4,  62,  -4, -14},
                { 10,  67,   1,  74,  73,  27,  62,  -2},
                { 24,  24,  45,  37,  33,  41,  25,  17},
                { -1,   5,  31,  21,  22,  35,   2,   0},
                {-18,  10,  13,  22,  18,  15,  11, -14},
                {-23, -15,   2,   0,   2,   0, -23, -20},
                {-74, -23, -26, -24, -19, -35, -22, -69}
        });

        vantaggioCasella.put("A", new int[][] {
                {-59, -78, -82, -76, -23,-107, -37, -50},
                {-11,  20,  35, -42, -39,  31,   2, -22},
                { -9,  39, -32,  41,  52, -10,  28, -14},
                { 25,  17,  20,  34,  26,  25,  15,  10},
                { 13,  10,  17,  23,  17,  16,   0,   7},
                { 14,  25,  24,  15,   8,  25,  20,  15},
                { 19,  20,  11,   6,   7,   6,  20,  16},
                { -7,   2, -15, -12, -14, -15, -10, -10}
        });

        vantaggioCasella.put("T", new int[][] {
                { 35,  29,  33,   4,  37,  33,  56,  50},
                { 55,  29,  56,  67,  55,  62,  34,  60},
                { 19,  35,  28,  33,  45,  27,  25,  15},
                {  0,   5,  16,  13,  18,  -4,  -9,  -6},
                {-28, -35, -16,  -1, -12, -26, -22, -31},
                {-33, -28, -22,  -6,  -1, -20, -31, -41},
                {-36, -26, -12,  -1,   9,  -7,   6, -23},
                {-24, -11,   7,  26,  24,  20,   1,  -7}
        });

        vantaggioCasella.put("Q", new int[][] {
                {  6,   1,  -8,-104,  69,  24,  88,  26},
                { 14,  32,  60, -10,  20,  76,  57,  24},
                { -2,  43,  32,  60,  72,  63,  43,   2},
                {  1, -16,  22,  17,  25,  20, -13,  -6},
                {-14, -15,  -4,   5,   6,  -8, -15, -18},
                {-20,  -6,   0,  -5,  -6,  -6, -11, -21},
                {-16, -21, -18, -21, -21, -21, -25, -15},
                {-36, -18,   0, -19, -15, -15, -21, -38}
        });

        vantaggioCasella.put("R", new int[][] {
                {-73, -57, -72, -46, -44, -22, -65, -75},
                {-62, -47, -47, -48, -43, -26, -44, -67},
                {-62, -49, -50, -47, -51, -37, -47, -63},
                {-56, -49, -52, -55, -55, -52, -49, -56},
                {-36, -32, -42, -47, -46, -40, -31, -35},
                {-13, -12, -25, -31, -30, -24, -12, -12},
                { 27,  13,  -8, -10, -10,  -8,  14,  24},
                { 29,  47,  26,  10,  17,  19,  47,  29}
        });
    }

    public Bot(Scacchiera scacchiera, Color colore, int profondita) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
        if (profondita < 1 || profondita > 7) throw new IllegalArgumentException("Profondità non valida: max 7 min 1");
        PROFONDITA = profondita;
    }

    public Bot(Scacchiera scacchiera, Color colore) {
        this(scacchiera, colore, 4);
    }

    public Color getColore() {
        return colore;
    }

    public int[][] muovi() {
        if (!scacchiera.getTurno().equals(colore)) throw new IllegalStateException("Il bot non può muovere se non è il suo turno");
        int[][] mossaMigliore = trovaMossaMigliore(scacchiera.getCaselle());
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

    private int[][] trovaMossaMigliore(Pedina[][] caselle) {
        int[][] mossa = null;
        int valoreMigliore = Integer.MIN_VALUE;

        int profondita = PROFONDITA - 1;
        int tot = mosseTotali(caselle);
        if (tot <= 20) profondita++;
        if (tot <= 5) profondita++;

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
                    int val = miniMax(caselle, profondita, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

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
                    }
                }
            }
        }
        return mossa;
    }

    private int miniMax(Pedina[][] caselle, int profondita, boolean massimizza, int alpha, int beta) {
        Color coloreTurno = colore;
        if (!massimizza) coloreTurno = coloreAvversario();

        StatoPartita sp = Scacchiera.statoPartitaCaselle(caselle, coloreTurno);

        if (profondita == 0 || sp != StatoPartita.IN_CORSO) return evaluation(caselle, sp);

        if (massimizza) {
            int max = Integer.MIN_VALUE;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(coloreTurno)) continue;

                    int[] pos = new int[]{i, j};
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, coloreTurno);
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

                        int val = miniMax(caselle, profondita - 1, false, alpha, beta);
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
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(coloreTurno)) continue;

                    int[] pos = new int[]{i, j};
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, coloreTurno);
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

                        int val = miniMax(caselle, profondita - 1, true, alpha, beta);
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

    private int evaluation(Pedina[][] caselle, StatoPartita statoPartita) {
        if (statoPartita == StatoPartita.VITTORIA_BIANCO) return colore.equals(Color.white) ? Integer.MAX_VALUE - 10 : Integer.MIN_VALUE + 10;
        if (statoPartita == StatoPartita.VITTORIA_NERO) return colore.equals(Color.black) ? Integer.MAX_VALUE - 10 : Integer.MIN_VALUE + 10;
        if (statoPartita != StatoPartita.IN_CORSO) return 0;

        int evalTotale = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;
                int evalPedina = evaluationCasella(caselle, i, j);
                if (caselle[i][j].getColore().equals(colore)) evalTotale += evalPedina;
                else evalTotale -= evalPedina;
            }
        }

        return evalTotale;
    }

    private int evaluationCasella (Pedina[][]caselle, int i, int j){
        if (caselle[i][j] == null) return 0;
        Pedina p = caselle[i][j];
        int materiale = p.getMateriale();
        int vantaggioPosizione;
        String nomePedina = p.getClass().getSimpleName().substring(0, 1);
        if (p instanceof Regina) nomePedina = "Q";
        if (p.getColore().equals(Color.white)) vantaggioPosizione = vantaggioCasella.get(nomePedina)[i][j];
        else vantaggioPosizione = vantaggioCasella.get(nomePedina)[7 - i][7 - j];
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
