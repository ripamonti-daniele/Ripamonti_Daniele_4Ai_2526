import java.security.InvalidParameterException;

public class Board {
    private final int[][] griglia;
    private int turno;
    private boolean aggiornato;

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

    public String esitoPartita() {
        int nero = 0;
        int bianco = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                switch (griglia[y][x]) {
                    case -1:
                        return "";
                    case 0:
                        nero++;
                        break;
                    case 1:
                        bianco++;
                        break;
                }
            }
        }

        if (nero == bianco) return "Nero: " + nero + " Bianco: " + bianco + "\nPareggio!";
        else if (nero > bianco) return "Nero: " + nero + " Bianco: " + bianco + "\nVince il nero!";
        else return "Nero: " + nero + " Bianco: " + bianco + "\nVince il bianco!";
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

    public void addDisco(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) throw new InvalidParameterException("Errore: questa casella non esiste");
        if (griglia[y][x] != -1) throw new InvalidParameterException("Errore: casella già occupata");

        boolean adiacente = isAdiacente(x, y);
        if (!adiacente) throw new InvalidParameterException("Errore: casella non adiacente ai dischi dell'avversario");

        aggiornato = false;

        aggiornaDischi(x, y, true, false, true, false, false);
        aggiornaDischi(x, y, true, false, false, false, false);
        aggiornaDischi(x, y, false, true, false, true, false);
        aggiornaDischi(x, y, false, true, false, false, false);
        aggiornaDischi(x, y, true, true, true, true, false);
        aggiornaDischi(x, y, true, true, false, false, false);
        aggiornaDischi(x, y, true, true, true, false, false);
        aggiornaDischi(x, y, true, true, false, true, false);

        if (aggiornato) griglia[y][x] = turno;
        else throw new InvalidParameterException("Errore: questa casella non permette di rubare dischi all'avversario");
    }

    private void aggiornaDischi(int x, int y, boolean cambiaX, boolean cambiaY, boolean aumentaX, boolean aumentaY, boolean modifica) {
        if (!cambiaX && !cambiaY) {
            throw new InvalidParameterException("Errore, almeno uno tra cambiaX e cambiaY dev'essere true");
        }
        int startX = x;
        int startY = y;
        boolean catturato = false;

        //destra
        if (aumentaX && cambiaX && x < 6 && !cambiaY) {
            x++;
            for (; x < 8; x++) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    }
                    else break;
                }
            }
        }

        //sinistra
        if (!aumentaX && cambiaX && x > 1 && !cambiaY) {
            x--;
            for (; x > -1; x--) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    }
                    else break;
                }
            }
        }

        //basso
        if (aumentaY && cambiaY && y < 6 && !cambiaX) {
            y++;
            for (; y < 8; y++) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    }
                    else break;
                }
            }
        }

        //alto
        if (!aumentaY && cambiaY && y > 1 && !cambiaX) {
            y--;
            for (; y > -1; y--) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    }
                    else break;
                }
                else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    }
                    else break;
                }
            }
        }

        //basso destra
        if (aumentaX && cambiaX && x < 6 && aumentaY && cambiaY && y < 6) {
            x++;
            y++;

            while (x < 8 && y < 8) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    } else break;
                }
                x++;
                y++;
            }
        }

        //alto sinistra
        if (!aumentaX && cambiaX && x > 1 && !aumentaY && cambiaY && y > 1) {
            x--;
            y--;

            while (x > -1  && y > -1) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    } else break;
                }
                x--;
                y--;
            }
        }

        //basso sinistra
        if (!aumentaX && cambiaX && x > 1 && aumentaY && cambiaY && y < 6) {
            x--;
            y++;

            while (x > -1 && y < 8) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    } else break;
                }
                x--;
                y++;
            }
        }

        //alto destra
        if (aumentaX && cambiaX && x < 6 && !aumentaY && cambiaY && y > 1) {
            x++;
            y--;

            while (x < 8 && y > -1) {
                if (!modifica) {
                    if (griglia[y][x] == -1) break;
                    if (griglia[y][x] != turno) catturato = true;
                    else if (griglia[y][x] == turno && catturato) {
                        aggiornaDischi(startX, startY, cambiaX, cambiaY, aumentaX, aumentaY,true);
                        break;
                    } else break;
                } else {
                    if (griglia[y][x] != turno) {
                        griglia[y][x] = turno;
                        aggiornato = true;
                    } else break;
                }
                x++;
                y--;
            }
        }
    }
}
