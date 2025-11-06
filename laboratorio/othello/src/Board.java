import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int turno;
    private final int[][] griglia;
    private List<List<Integer>> caselleLegali;
    private boolean passo;
    private boolean doppioPasso;
    public final int NORTH = 1;
    public final int EAST = 2;
    public final int SOUTH = 3;
    public final int WEST = 4;
    public final int NORTH_EAST = 5;
    public final int SOUTH_EAST = 6;
    public final int SOUTH_WEST = 7;
    public final int NORTH_WEST = 8;

    public Board() {
        griglia = new int[8][8];
        reset();
    }

    private void inizializzaGriglia() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                griglia[i][j] = -1;
            }
        }
        griglia[3][3] = 1;
        griglia[3][4] = 0;
        griglia[4][3] = 0;
        griglia[4][4] = 1;
    }

    private void cambiaTurno() {
        if (turno == 0) turno = 1;
        else turno = 0;
    }

    public int getTurno() {
        return turno;
    }

    public int[][] getGriglia() {
        int[][] copiaGriglia = griglia.clone();
        for (int i = 0; i < griglia.length; i++) {
            copiaGriglia[i] = griglia[i].clone();
        }
        return copiaGriglia;
    }

    public List<List<Integer>> getCaselleLegali() {
        List<List<Integer>> copiaCaselleLegali = new ArrayList<>();
        for (List<Integer> sublist : caselleLegali) {
            copiaCaselleLegali.add(new ArrayList<>(sublist));
        }
        return copiaCaselleLegali;
    }

    public int[] getPunteggi() {
        int[] punteggi = new int[2];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int val = griglia[y][x];
                if (val == 0) punteggi[0]++;
                else if (val == 1) punteggi[1]++;
            }
        }
        return punteggi;
    }

    public int esitoPartita() {
        int[] punteggi = getPunteggi();
        if (passo && !doppioPasso) return -2;
        else if (punteggi[0] + punteggi[1] < 64 && !doppioPasso) return -1;
        else if (punteggi[0] == punteggi[1]) return 2;
        else if (punteggi[0] > punteggi[1]) return 0;
        else return 1;
    }

    public void reset() {
        turno = 0;
        passo = false;
        doppioPasso = false;
        inizializzaGriglia();
        aggiornaCaselleLegali();
    }

    private boolean isAdiacente(int x, int y) {
        boolean adiacente = false;
        for (int verticale = -1; verticale <= 1; verticale ++) {
            for (int orizzontale = -1; orizzontale <= 1; orizzontale ++) {
                if (orizzontale == 0 && verticale == 0) continue;
                if (x + orizzontale < 0 || x + orizzontale > 7 || y + verticale < 0 || y + verticale > 7) continue;
                if (griglia[y + verticale][x + orizzontale] != turno && griglia[y + verticale][x + orizzontale] != -1) {
                    adiacente = true;
                    break;
                }
            }
        }
        return adiacente;
    }

    private void aggiornaCaselleLegali() {
        caselleLegali = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (griglia[y][x] != -1) continue;
                if (isAdiacente(x, y)) {
                    caselleLegali.add(new ArrayList<Integer>());
                    caselleLegali.getLast().add(x);
                    caselleLegali.getLast().add(y);
                    caselleLegali.getLast().add(turno);
                    for (int i = 1; i <= 8; i++) {
                        int val = aggiornaDischi(x, y, i, false);
                        if (val != 0) {
                            caselleLegali.getLast().add(val);
                        }
                    }
                    if (caselleLegali.getLast().size() == 3) caselleLegali.remove(caselleLegali.getLast());
                }
            }
        }
    }

    public void addDisco(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) throw new InvalidParameterException("Errore: questa casella non esiste");
        if (griglia[y][x] != -1) throw new InvalidParameterException("Errore: casella già occupata");

        boolean legale = false;
        for (List<Integer> mossa : caselleLegali) {
            if (mossa.getFirst() == x && mossa.get(1) == y && mossa.get(2) == turno) {
                legale = true;
                for (int i = 3; i < mossa.size(); i++) aggiornaDischi(mossa.getFirst(), mossa.get(1), mossa.get(i), true);
                break;
            }
        }
        if (!legale) throw new InvalidParameterException("Errore: non puoi inserire un disco in questa casella");

        griglia[y][x] = turno;
        cambiaTurno();

        aggiornaCaselleLegali();
        if (caselleLegali.isEmpty()) {
            passo = true;
            cambiaTurno();
            aggiornaCaselleLegali();
            if (caselleLegali.isEmpty()) {
                doppioPasso = true;
                cambiaTurno();
            }
        }
        else passo = false;
    }

    private int aggiornaDischi(int x, int y, int direzione, boolean modifica) {
        boolean catturato = false;

        //NORTH
        if (direzione == NORTH && y > 1) {
            y--;
            for (; y > -1; y--) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    }
                    else break;
                }
            }
        }

        //EAST
        else if (direzione == EAST && x < 6) {
            x++;
            for (; x < 8; x++) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    }
                    else break;
                }
            }
        }

        //SOUTH
        else if (direzione == SOUTH && y < 6) {
            y++;
            for (; y < 8; y++) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    }
                    else break;
                }
            }
        }

        //WEST
        else if (direzione == WEST && x > 1) {
            x--;
            for (; x > -1; x--) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    }
                    else break;
                }
            }
        }

        //NORTH EAST
        else if (direzione == NORTH_EAST && x < 6 && y > 1) {
            x++;
            y--;

            while (x < 8 && y > -1) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    } else break;
                }
                x++;
                y--;
            }
        }

        //SOUTH EAST
        else if (direzione == SOUTH_EAST && x < 6 && y < 6) {
            x++;
            y++;

            while (x < 8 && y < 8) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    } else break;
                }
                x++;
                y++;
            }
        }

        //SOUTH WEST
        else if (direzione == SOUTH_WEST && x > 1 && y < 6) {
            x--;
            y++;

            while (x > -1 && y < 8) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    } else break;
                }
                x--;
                y++;
            }
        }

        //NORTH WEST
        else if (direzione == NORTH_WEST && x > 1 && y > 1) {
            x--;
            y--;

            while (x > -1  && y > -1) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        return direzione;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                    } else break;
                }
                x--;
                y--;
            }
        }
        return 0;
    }
}
