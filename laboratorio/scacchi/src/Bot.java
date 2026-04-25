import scacchiera_pedine.*;
import java.awt.*;
import java.util.List;

public class Bot {
    private final Scacchiera scacchiera;
    private final Color colore;
    private int PROFONDITA;
    boolean vantaggioSemplice;

    public Bot(Scacchiera scacchiera, Color colore) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
        PROFONDITA = 3;
        vantaggioSemplice = false;
    }

    public Bot(Scacchiera scacchiera, Color colore, int profondita) {
        this(scacchiera, colore);
        PROFONDITA = profondita;
        vantaggioSemplice = true;
    }

    public Color getColore() {
        return colore;
    }

    public int[][] muovi() {
        if (!scacchiera.getTurno().equals(colore)) throw new IllegalStateException("Il bot non può muovere se non è il suo turno");
        int[][] mossaMigliore = trovaMossaMigliore(scacchiera.getCaselle());
        if (mossaMigliore != null) {
            scacchiera.selezionaPedina(mossaMigliore[0]);
            scacchiera.muoviPedina(mossaMigliore[1]);
        }
        return mossaMigliore;
    }

    private int[][] trovaMossaMigliore(Pedina[][] caselle) {
        int[][] mossa = null;
        int valoreMigliore = Integer.MIN_VALUE;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null || !caselle[i][j].getColore().equals(colore)) continue;

                int[] pos = new int[]{i, j};

                List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, pos, colore);
                if (mosse == null) continue;

                for (int[] m : mosse) {
                    Pedina[][] copia = Scacchiera.getCopiaCaselle(caselle);

                    if (!Scacchiera.muoviPedinaCaselle(copia, mosse, pos, m)) continue;

                    if (Scacchiera.promozioneInSospesoCaselle(copia) != null) Scacchiera.promozionePedoneCaselle(copia, m, 1);
                    int val = miniMax(copia, PROFONDITA - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (val > valoreMigliore) {
                        valoreMigliore = val;
                        mossa = new int[][]{pos, m};
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

        if (profondita == 0 || sp != StatoPartita.IN_CORSO) {
            if (vantaggioSemplice) return trovaVantaggioSemplice(caselle, sp);
            return trovaVantaggio(caselle, sp);
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
                        Pedina[][] copia = Scacchiera.getCopiaCaselle(caselle);

//                        if (copia[m[0]][m[1]] instanceof Re) System.out.println(Scacchiera.getStringaScacchieraCaselle(copia, false));

                        if (!Scacchiera.muoviPedinaCaselle(copia, mosse, pos, m)) continue;
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
                        Pedina[][] copia = Scacchiera.getCopiaCaselle(caselle);
                        if (!Scacchiera.muoviPedinaCaselle(copia, mosse, pos, m)) continue;
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

    private int trovaVantaggio(Pedina[][] caselle, StatoPartita statoPartita) {
        if (statoPartita == StatoPartita.VITTORIA_BIANCO) {
            if (colore.equals(Color.white)) return Integer.MAX_VALUE - 10;
            else return Integer.MIN_VALUE + 10;
        }
        if (statoPartita == StatoPartita.VITTORIA_NERO) {
            if (colore.equals(Color.black)) return Integer.MAX_VALUE - 10;
            else return Integer.MIN_VALUE + 10;
        }
        if (statoPartita != StatoPartita.IN_CORSO) return 0;

        int diffMateriale = Scacchiera.getMaterialeCaselle(caselle, Color.white) - Scacchiera.getMaterialeCaselle(caselle, Color.black);
        if (colore.equals(Color.black)) diffMateriale *= -1;

        Pedina[][] copiaBot = Scacchiera.getCopiaCaselle(caselle);
        Pedina[][] copiaAvversario = Scacchiera.getCopiaCaselle(caselle);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (copiaBot[y][x] != null && !(copiaBot[y][x] instanceof Re) && copiaBot[y][x].getColore().equals(colore)) copiaBot[y][x] = new Alfiere(coloreAvversario(), new int[]{y, x});
                if (copiaAvversario[y][x] != null && !(copiaAvversario[y][x] instanceof Re) && copiaAvversario[y][x].getColore().equals(coloreAvversario())) copiaAvversario[y][x] = new Alfiere(colore, new int[]{y, x});
            }
        }

        int[] registroPedoni = new int[16];
        int pedoniDoppiBot = 0, pedoniDoppiAvversario = 0;

        for (int col = 0; col < 8; col++) {
            int pedoniBot = 0;
            int pedoniAvversario = 0;

            for (int row = 0; row < 8; row++) {
                if (!(caselle[row][col] instanceof Pedone)) continue;
                if (caselle[row][col].getColore().equals(colore)) pedoniBot++;
                else pedoniAvversario++;
            }

            if (pedoniBot > 1) {
                pedoniDoppiBot += pedoniBot - 1;
                registroPedoni[col] = pedoniBot;
            }

            if (pedoniAvversario > 1) {
                pedoniDoppiAvversario += pedoniAvversario - 1;
                registroPedoni[8 + col] = pedoniAvversario;
            }
        }

        int pedoniIsolatiBot = 0;
        int pedoniIsolatiAvversario = 0;

        for (int col = 0; col < 8; col++) {
            if (registroPedoni[col] > 0) {
                boolean isolato = (col == 0 || registroPedoni[col - 1] == 0) && (col == 7 || registroPedoni[col + 1] == 0);
                if (isolato) pedoniIsolatiBot += registroPedoni[col];
            }
            if (registroPedoni[8 + col] > 0) {
                boolean isolato = (col == 0 || registroPedoni[8 + col - 1] == 0) && (col == 7 || registroPedoni[8 + col + 1] == 0);
                if (isolato) pedoniIsolatiAvversario += registroPedoni[8 + col];
            }
        }

        int scaccoBot = 0;
        int scaccoAvversario = 0;
        if (Scacchiera.isScaccoReCaselle(caselle, colore)) scaccoBot = 1;
        if (Scacchiera.isScaccoReCaselle(caselle, coloreAvversario())) scaccoAvversario = 1;

        int vantaggioPosizioneBot = 0;
        int vantaggioPosizioneAvversario = 0;
        int pedineProtetteBot = 0;
        int pedineProtetteAvversario = 0;
        int mosseDisponibiliBot = 0;
        int mosseDisponibiliAvversario = 0;
        int pedineMinacciateBot = 0;
        int pedineMinacciateAvversario = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;

                int[] pos = new int[]{i, j};
                boolean isBot = caselle[i][j].getColore().equals(colore);

                int vantaggioPosizione = 0;
                int ri = i + 1;
                int rj = j + 1;
                if (ri > 4) ri = 9 - ri;
                if (rj > 4) rj = 9 - rj;
                vantaggioPosizione += ri * rj;

                List<int[]> mosseReali = Scacchiera.selezionaPedinaCaselle(caselle, pos, caselle[i][j].getColore());
                int mosseDisponibili = 0;
                if (mosseReali != null) mosseDisponibili = mosseReali.size();

                Pedina[][] copiaGiusta = copiaAvversario;
                if (isBot) copiaGiusta = copiaBot;

                List<int[]> mosseProtezione = Scacchiera.selezionaPedinaCaselle(copiaGiusta, pos, caselle[i][j].getColore());
                int pedineProtette = 0;
                int pedineMinacciate = 0;
                if (mosseProtezione != null) {
                    for (int[] m : mosseProtezione) {
                        Pedina occupante = copiaGiusta[m[0]][m[1]];
                        if (occupante == null) continue;
                        if (occupante.getColore().equals(caselle[i][j].getColore())) pedineProtette += occupante.getMateriale();
                        else pedineMinacciate += occupante.getMateriale();
                    }
                }

                if (isBot) {
                    vantaggioPosizioneBot += vantaggioPosizione;
                    mosseDisponibiliBot += mosseDisponibili;
                    pedineProtetteBot += pedineProtette;
                    pedineMinacciateBot += pedineMinacciate;
                } else {
                    vantaggioPosizioneAvversario += vantaggioPosizione;
                    mosseDisponibiliAvversario += mosseDisponibili;
                    pedineProtetteAvversario += pedineProtette;
                    pedineMinacciateAvversario += pedineMinacciate;
                }
            }
        }

        return diffMateriale * 155
                + (vantaggioPosizioneBot - vantaggioPosizioneAvversario) * 25
                + (mosseDisponibiliBot - mosseDisponibiliAvversario) * 10
                + (pedineProtetteBot - pedineProtetteAvversario) * 10
                + (pedineMinacciateBot - pedineMinacciateAvversario) * 10
                - (pedoniIsolatiBot - pedoniIsolatiAvversario) * 12
                - (pedoniDoppiBot - pedoniDoppiAvversario) * 8
                - scaccoBot * 30
                + scaccoAvversario * 30;
    }

    private int trovaVantaggioSemplice(Pedina[][] caselle, StatoPartita statoPartita) {
        if (statoPartita == StatoPartita.VITTORIA_BIANCO) return colore.equals(Color.white) ? Integer.MAX_VALUE - 10 : Integer.MIN_VALUE + 10;
        if (statoPartita == StatoPartita.VITTORIA_NERO) return colore.equals(Color.black) ? Integer.MAX_VALUE - 10 : Integer.MIN_VALUE + 10;
        if (statoPartita != StatoPartita.IN_CORSO) return 0;

        int diffMateriale = Scacchiera.getMaterialeCaselle(caselle, Color.white) - Scacchiera.getMaterialeCaselle(caselle, Color.black);
        if (colore.equals(Color.black)) diffMateriale *= -1;

        int vantaggioPosizioneBot = 0, vantaggioPosizioneAvversario = 0;
        int pedoniDoppiBot = 0, pedoniDoppiAvversario = 0;
        int pedoniIsolatiBot = 0, pedoniIsolatiAvversario = 0;
        int[] registroPedoni = new int[16];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (caselle[i][j] == null) continue;

                boolean isBot = caselle[i][j].getColore().equals(colore);

                int ri = (i < 4) ? i + 1 : 8 - i;
                int rj = (j < 4) ? j + 1 : 8 - j;
                int vantaggioPosizione = ri * rj;

                if (isBot) vantaggioPosizioneBot += vantaggioPosizione;
                else vantaggioPosizioneAvversario += vantaggioPosizione;

                if (caselle[i][j] instanceof Pedone) {
                    if (isBot) registroPedoni[j]++;
                    else registroPedoni[8 + j]++;
                }
            }
        }

        for (int col = 0; col < 8; col++) {
            if (registroPedoni[col] > 1) pedoniDoppiBot += registroPedoni[col] - 1;
            if (registroPedoni[8 + col] > 1) pedoniDoppiAvversario += registroPedoni[8 + col] - 1;

            if (registroPedoni[col] > 0) {
                boolean isolato = (col == 0 || registroPedoni[col - 1] == 0) && (col == 7 || registroPedoni[col + 1] == 0);
                if (isolato) pedoniIsolatiBot += registroPedoni[col];
            }
            if (registroPedoni[8 + col] > 0) {
                boolean isolato = (col == 0 || registroPedoni[8 + col - 1] == 0) && (col == 7 || registroPedoni[8 + col + 1] == 0);
                if (isolato) pedoniIsolatiAvversario += registroPedoni[8 + col];
            }
        }

        return diffMateriale  * 100
                + (vantaggioPosizioneBot - vantaggioPosizioneAvversario) * 25
                - (pedoniIsolatiBot - pedoniIsolatiAvversario) * 12
                - (pedoniDoppiBot - pedoniDoppiAvversario) * 8;
    }
}
