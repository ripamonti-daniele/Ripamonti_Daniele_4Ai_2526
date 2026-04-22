import scacchiera_pedine.*;
import java.awt.*;
import java.util.List;

public class Bot {
    private final Scacchiera scacchiera;
    private final Color colore;
    private static final int PROFONDITA = 3;

    public Bot(Scacchiera scacchiera, Color colore, Color turno) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
    }

    public Color getColore() {
        return colore;
    }

    public int[][] muovi() {
        if (!scacchiera.getTurno().equals(colore))
            throw new IllegalStateException("Il bot non può muovere se non è il suo turno");
        int[][] mossaMigliore = trovaMossaMigliore(scacchiera.getCaselle());
        scacchiera.selezionaPedina(mossaMigliore[0]);
        scacchiera.muoviPedina(mossaMigliore[1]);
        return mossaMigliore;
    }

    private int[][] trovaMossaMigliore(Pedina[][] caselle) {
        int[][] mossa = null;
        int valoreMigliore = Integer.MIN_VALUE;;

        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p == null || !p.getColore().equals(colore)) continue;
                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, p.getPosizione(), p.getColore());
                if (mosse == null) continue;
                for (int[] m : mosse) {
                    Pedina[][] copia = copiaCaselle(caselle);
                    Scacchiera.muoviPedinaCaselle(copia, mosse, p.getPosizione(), m);
                    if (Scacchiera.promozioneInSospesoCaselle(copia) != null) Scacchiera.promozionePedoneCaselle(copia, m, 1);
                    int val = miniMax(copia, PROFONDITA - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (val > valoreMigliore) {
                        valoreMigliore = val;
                        mossa = new int[][]{p.getPosizione(), m};
                    }
                }
            }
        }
        return mossa;
    }

    private int miniMax(Pedina[][] caselle, int profondita, boolean massimizza, int alpha, int beta) {
        Color coloreTurno = colore;
        if (!massimizza) coloreTurno = coloreAvversario();

        if (profondita == 0 || Scacchiera.statoPartitaCaselle(caselle, coloreTurno) != StatoPartita.IN_CORSO) {
            return trovaVantaggio(caselle);
        }

        if (massimizza) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(coloreTurno)) continue;

                    int[] pos = new int[]{i, j};
                    List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, coloreTurno);
                    if (mosse == null) continue;

                    for (int[] m : mosse) {
                        Pedina[][] copia = copiaCaselle(caselle);
                        Scacchiera.muoviPedinaCaselle(copia, mosse, pos, m);
                        if (Scacchiera.promozioneInSospesoCaselle(copia) != null) Scacchiera.promozionePedoneCaselle(copia, m, 1);

                        int val = miniMax(copia, profondita - 1, false, alpha, beta);
                        if (val > max) max = val;
                        if (max > alpha) alpha = max;
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
                        Pedina[][] copia = copiaCaselle(caselle);
                        Scacchiera.muoviPedinaCaselle(copia, mosse, pos, m);
                        if (Scacchiera.promozioneInSospesoCaselle(copia) != null) Scacchiera.promozionePedoneCaselle(copia, m, 1);

                        int val = miniMax(copia, profondita - 1, true, alpha, beta);
                        if (val < min) min = val;
                        if (min < beta) beta = min;
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

    private int trovaVantaggio(Pedina[][] caselle) {
        int diffMateriale = Scacchiera.getMaterialeCaselle(caselle, Color.white) - Scacchiera.getMaterialeCaselle(caselle, Color.black);
        if (colore.equals(Color.black)) diffMateriale *= - 1;

        int vantaggioPosizioneBot = 0;
        int vantaggioPosizioneAvversario = 0;

        int pedineProtetteBot = 0;
        int pedineProtetteAvversario = 0;

        int mosseDisponibiliBot = 0;
        int mosseDisponibiliAvversario = 0;

        int pedineMinacciateBot = 0;
        int pedineMinacciateAvversario = 0;

        int pedoniIsolatiBot = 0;
        int pedoniIsolatiAvversario = 0;

        int pedoniDoppiBot = 0;
        int pedoniDoppiAvversario = 0;

       int[] registroPedoni = new int[16];

        for (int i = 0; i < 8; i++) {
            int pedoniBot = 0;
            int pedoniAvversario = 0;

            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;

                int mosseDisponibili = 0;
                int pedineMinacciate = 0;
                int pedineProtette = 0;

                List<int[]> m = Scacchiera.selezionaPedinaCaselle(caselle, new int[]{i, j}, caselle[i][j].getColore());
                if (m != null) mosseDisponibili = m.size();

                Pedina[][] copia = copiaCaselle(caselle);
                Color c = Color.white;
                if (caselle[i][j].getColore().equals(c)) c = Color.black;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (copia[y][x] != null && copia[y][x].getColore().equals(caselle[i][j].getColore())) copia[y][x] = new Alfiere(c, new int[]{y, x});
                    }
                }
                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(copia, new int[]{i, j}, caselle[i][j].getColore());
                if (mosse != null) {
                    for (int[] pos : mosse) {
                        if (caselle[pos[0]][pos[1]] != null) pedineMinacciate++;
                        else if (copia[pos[0]][pos[1]] != null) pedineProtette++;
                    }
                }

                int vantaggioPosizione = 0;
                int ri = i + 1;
                int rj = j + 1;
                if (ri > 4) ri = 9 - ri;
                if (rj > 4) rj = 9 - rj;
                vantaggioPosizione += ri * rj;

                if (caselle[i][j].getColore().equals(colore)) {
                    vantaggioPosizioneBot += vantaggioPosizione;
                    mosseDisponibiliBot += mosseDisponibili;
                    pedineMinacciateBot += pedineMinacciate;
                    pedineProtetteBot += pedineProtette;
                    if (caselle[j][i] instanceof Pedone) pedoniBot++;
                }
                else {
                    vantaggioPosizioneAvversario += vantaggioPosizione;
                    mosseDisponibiliAvversario += mosseDisponibili;
                    pedineMinacciateAvversario += pedineMinacciate;
                    pedineProtetteAvversario += pedineProtette;
                    if (caselle[j][i] instanceof Pedone) pedoniAvversario++;
                }
            }
            if (pedoniBot > 1) {
                pedoniDoppiBot += pedoniBot - 1;
                registroPedoni[i] = pedoniBot;
            }
            else registroPedoni[i] = 0;
            if (pedoniAvversario > 1) {
                pedoniDoppiAvversario += pedoniAvversario - 1;
                registroPedoni[8 + i] = pedoniAvversario;
            }
            else registroPedoni[8 + i] = 0;
        }

        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 8) {
                if (registroPedoni[i] > 0 && registroPedoni[i + 1] == 0) {
                    if (i == 0) pedoniIsolatiBot += registroPedoni[i];
                    else pedoniIsolatiAvversario += registroPedoni[i];
                }
            }
            else if (i == 7 || i == 15) {
                if (registroPedoni[i] > 0 && registroPedoni[i - 1] == 0) {
                    if (i == 7) pedoniIsolatiBot += registroPedoni[i];
                    else pedoniIsolatiAvversario += registroPedoni[i];
                }
            }
            else {
                if (registroPedoni[i] > 0 && registroPedoni[i - 1] == 0 && registroPedoni[i + 1] == 0) {
                    if (i <= 7) pedoniIsolatiBot += registroPedoni[i];
                    else pedoniIsolatiAvversario += registroPedoni[i];
                }
            }
        }

        return diffMateriale * 100 +
                (vantaggioPosizioneBot - vantaggioPosizioneAvversario) * 25 +
                (pedineProtetteBot - pedineProtetteAvversario) * 10 +
                (mosseDisponibiliBot - mosseDisponibiliAvversario) * 10 +
                (pedineMinacciateBot - pedineMinacciateAvversario) * 15 -
                (pedoniIsolatiBot - pedoniIsolatiAvversario) * 12 -
                (pedoniDoppiBot - pedoniDoppiAvversario) * 8;
    }

    private Pedina[][] copiaCaselle(Pedina[][] caselle) {
        Pedina[][] copia = new Pedina[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = caselle[i][j].copy();
            }
        }
        return copia;
    }
}
