import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private int turno;
    private final int[][] griglia;
    private List<List<Integer>> caselleLegali;
    private final int NORTH = 1;
    private final int EAST = 2;
    private final int SOUTH = 3;
    private final int WEST = 4;
    private final int NORTH_EAST = 5;
    private final int SOUTH_EAST = 6;
    private final int SOUTH_WEST = 7;
    private final int NORTH_WEST = 8;

    public Board() {
        griglia = new int[8][8];
        turno = 0;
        inizializzaGriglia();
    }

    public void inizializzaGriglia() {
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

    public void cambiaTurno() {
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

        if (punteggi[0] + punteggi[1] < 64) return -1;
        else if (punteggi[0] == punteggi[1]) return 2;
        else if (punteggi[0] > punteggi[1]) return 0;
        else return 1;
    }

    private boolean isAdiacente(int x, int y, int turno) {
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

    private void aggiornaCaselleLegali(int turno) {
        caselleLegali = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (griglia[y][x] != -1) continue;
                if (isAdiacente(x, y, 0)) {
                    caselleLegali.add(new ArrayList<Integer>());
                    caselleLegali.getLast().add(x);
                    caselleLegali.getLast().add(y);
                    caselleLegali.getLast().add(turno);
                    for (int i = 1; i <= 8; i++) {
                        int val = aggiornaDischi(x, y, i, false);
                        if (val != 0) {
                            caselleLegali.getLast().add(val);
                            System.out.println("coordinate:" + x + " " + y + " - turno: " + turno);
                        }
                    }
                    if (caselleLegali.getLast().size() == 3) caselleLegali.remove(caselleLegali.getLast());
                }
            }
        }
    }

    public void addDisco(int x, int y, int turno) {
        if (x < 0 || x > 7 || y < 0 || y > 7) throw new InvalidParameterException("Errore: questa casella non esiste");
        if (griglia[y][x] != -1) throw new InvalidParameterException("Errore: casella già occupata");

        aggiornaCaselleLegali(turno);
        boolean legale = false;

        for (List<Integer> mossa : caselleLegali) {
            System.out.println(mossa);
            if (mossa.getFirst() == x && mossa.get(1) == y && mossa.get(2) == turno) {
                legale = true;
                for (int i = 3; i < mossa.size(); i++) aggiornaDischi(mossa.getFirst(), mossa.get(1), mossa.get(i), true);
            }
        }

        if (!legale) throw new InvalidParameterException("Errore: non puoi inserire un disco in questa casella");
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
        if (direzione == EAST && x < 6) {
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
        if (direzione == SOUTH && y < 6) {
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
        if (direzione == WEST && x > 1) {
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
        if (direzione == NORTH_EAST && x < 6 && y > 1) {
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
        if (direzione == SOUTH_EAST && x < 6 && y < 6) {
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
        if (direzione == SOUTH_WEST && x > 1 && y < 6) {
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
        if (direzione == NORTH_WEST && x > 1 && y > 1) {
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
