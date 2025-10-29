import java.security.InvalidParameterException;

public class Board {
    private final int[][] griglia;
    private int turno;

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

    public void addDisco(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) throw new InvalidParameterException("Errore: questa casella non esiste");
        if (griglia[y][x] != -1) throw new InvalidParameterException("Errore: casella già occupata");

        boolean adiacente = isAdiacente(x, y);
        if (!adiacente) throw new InvalidParameterException("Errore: casella non adiacente ai dischi dell'avversario");

//        int aggiornato = 0;

        griglia[y][x] = turno;
        aggiornaDischi(x, y, true, true, false, false);
        aggiornaDischi(x, y, true, false, true, false);
        aggiornaDischi(x, y, true, true, true, false);
        aggiornaDischi(x, y, false, true, false, false);
        aggiornaDischi(x, y, false, false, true, false);
        aggiornaDischi(x, y, false, true, true, false);
        aggiornaDischiDiagonaliRimaste(x, y, true, false);
        aggiornaDischiDiagonaliRimaste(x, y, false, false);

//        if (aggiornato == 0) {
//            griglia[y][x] = -1;
//            throw new InvalidParameterException("Errore: in questa casella non si possono rubare dischi all'avversario");
//        }
    }

    public int finePartita() {
        int nero = 0;
        int bianco = 1;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                switch (griglia[y][x]) {
                    case -1:
                        return -1;
                    case 0:
                        nero++;
                        break;
                    case 1:
                        bianco++;
                        break;
                }
            }
        }

        if (nero == bianco) return 2;
        else if (nero > bianco) return 0;
        else return 1;
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

    private void aggiornaDischi(int x, int y, boolean aumenta, boolean cambiaX, boolean cambiaY, boolean modifica) {
        if (!cambiaX && !cambiaY) throw new InvalidParameterException("Errore, almeno uno tra cambiaX e cambiaY dev'essere true");
        int startX = x;
        int startY = y;
        int incrementoX;
        int incrementoY;
        int endX;
        int endY;
        int aggiornato = 0;
        boolean catturato = false;

        if (aumenta) {
            if (cambiaX) {
                endX = 8;
                incrementoX = 1;
            }
            else {
                endX = startX;
                incrementoX = 0;
            }
            if (cambiaY) {
                endY = 8;
                incrementoY = 1;
            }
            else {
                endY = startY;
                incrementoY = 0;
            }

            while (x < endX || y < endY) {
                x += incrementoX;
                y += incrementoY;
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, aumenta, cambiaX, cambiaY, true);
                        break;
                    }
                    else break;
                }
                else {
                    aggiornato = 1;
                    if (griglia[y][x] != turno) griglia[y][x] = turno;
                    else break;
                }
            }
        }

        else {
            if (cambiaX) {
                endX = 0;
                incrementoX = -1;
            }
            else {
                endX = startX;
                incrementoX = 0;
            }
            if (cambiaY) {
                endY = 0;
                incrementoY = -1;
            }
            else {
                endY = startY;
                incrementoY = 0;
            }

            while (x > endX || y > endY) {
                x += incrementoX;
                y += incrementoY;
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, aumenta, cambiaX, cambiaY, true);
                        break;
                    }
                    else break;
                }
                else {
                    aggiornato = 1;
                    if (griglia[y][x] != turno) griglia[y][x] = turno;
                    else break;
                }
            }
        }
    }

    private void aggiornaDischiDiagonaliRimaste(int x, int y, boolean altoDestra, boolean modifica) {
        int startX = x;
        int startY = y;
        boolean catturato = false;

        if (altoDestra) {
            int endX = 8;
            int endY = 0;

            while (x < endX || y > endY) {
                x++;
                y--;
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischiDiagonaliRimaste(startX, startY, altoDestra, true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) griglia[y][x] = turno;
                    else break;
                }
            }
        }

        else {
            int endX = 0;
            int endY = 8;

            while (x > endX || y < endY) {
                x--;
                y++;
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischiDiagonaliRimaste(startX, startY, altoDestra, true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) griglia[y][x] = turno;
                    else break;
                }
            }
        }
    }
}
