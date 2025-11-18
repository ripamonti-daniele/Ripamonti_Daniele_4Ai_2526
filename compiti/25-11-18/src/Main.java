import java.io.IO;

public static char cambiaSegno(char segno, char giocatoreO, char giocatoreX) {
    if (segno == giocatoreO) return giocatoreX;
    else if (segno == giocatoreX) return giocatoreO;
    return segno;
}

public static String creaStringaGriglia(char[][] griglia, int n_caselle) {
    StringBuilder s = new StringBuilder(" 1   2   3 \n");
    for (int y = 0; y < n_caselle; y++) {
        for (int x = 0; x < n_caselle; x++) {
            s.append(" ");
            s.append(griglia[y][x]);
            s.append(" ");

            if (x == n_caselle - 1) {
                s.append(" ");
                s.append(y + 1);
                s.append("\n");
            }
            else s.append("|");
        }
        if (y < n_caselle - 1) s.append("---|---|---\n");
    }
    return s.toString();
}

void main() {
    Griglia griglia = new Griglia();
    char giocatoreO = griglia.SIMBOLO_O;
    char giocatoreX = griglia.SIMBOLO_X;
    char giocatoreCorrente = giocatoreX;
    char esito = 'n';
    int riga = -1;
    int col = -1;

    boolean gioca = true;
    while (gioca) {
        while (esito == 'n') {
            System.out.println(creaStringaGriglia(griglia.getGriglia(), griglia.N_CASELLE));
            boolean errore = true;

            System.out.println("Giocatore " + giocatoreCorrente + " inserisci la colonna che vuoi occupare");
            while (errore) {
                try {
                    col = Integer.parseInt(IO.readln());
                    errore = false;
                } catch (Exception e) {
                    System.out.println("Devi inseirire un numero intero");
                }
            }

            errore = true;
            System.out.println("Giocatore " + giocatoreCorrente + " inserisci la riga che vuoi occupare");
            while (errore) {
                try {
                    riga = Integer.parseInt(IO.readln());
                    errore = false;
                } catch (Exception e) {
                    System.out.println("Devi inseirire un numero intero");
                }
            }

            try {
                griglia.inserisciSimbolo(giocatoreCorrente, riga, col);
                esito = griglia.controllaVincita();
                giocatoreCorrente = cambiaSegno(giocatoreCorrente, giocatoreO, giocatoreX);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(creaStringaGriglia(griglia.getGriglia(), griglia.N_CASELLE));
        if (esito == giocatoreO) System.out.println("Vince il giocatore " + giocatoreO);
        else if (esito == giocatoreX) System.out.println("Vince il giocatore " + giocatoreX);
        else System.out.println("Pareggio");

        System.out.println("\nVuoi giocare ancora? (si/no)");
        String scelta = IO.readln().trim().toLowerCase();

        if (!scelta.equals("si")) gioca = false;
        else {
            griglia.resetGriglia();
            esito = griglia.controllaVincita();
            giocatoreCorrente = giocatoreX;
            System.out.println();
        }
    }
}
