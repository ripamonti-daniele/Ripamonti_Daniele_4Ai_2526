public class Griglia {

    private final char[][] griglia;
    public final int N_CASELLE = 3;
    private final char CASELLA_VUOTA = ' ';
    public final char SIMBOLO_O = 'O';
    public final char SIMBOLO_X = 'X';

    public Griglia() {
        griglia = new char[N_CASELLE][N_CASELLE];
        resetGriglia();
    }

    public void resetGriglia() {
        for (int i = 0; i < N_CASELLE; i++) {
            for (int j = 0; j < N_CASELLE; j++) {
                griglia[i][j] = CASELLA_VUOTA;
            }
        }
    }

    public char[][] getGriglia() {
        char[][] copia = new char[N_CASELLE][N_CASELLE];
        for (int i = 0; i < N_CASELLE; i++) {
            copia[i] = griglia[i].clone();
        }
        return copia;
    }

    public void inserisciSimbolo(char simbolo, int r, int c) {
        r--;
        c--;

        if (simbolo != SIMBOLO_X && simbolo != SIMBOLO_O) throw new IllegalArgumentException("Simbolo non valido");
        if (r < 0 || r > N_CASELLE-1) throw new IllegalArgumentException("Riga fuori dal range");
        if (c < 0 || c > N_CASELLE-1) throw new IllegalArgumentException("Colonna fuori dal range");
        if (griglia[r][c] != CASELLA_VUOTA) throw new IllegalArgumentException("Casella gia occupata");
        griglia[r][c] = simbolo;
    }

    public char controllaVincita() {
        if (griglia[0][0] != CASELLA_VUOTA && griglia[0][0] == griglia[1][1] && griglia[1][1] == griglia[2][2]) return griglia[0][0];
        if (griglia[0][2] != CASELLA_VUOTA && griglia[0][2] == griglia[1][1] && griglia[1][1] == griglia[2][0]) return griglia[0][2];

        boolean mosse_terminate = true;

        for (int i = 0; i < N_CASELLE; i++) {
            if (griglia[i][0] != CASELLA_VUOTA && griglia[i][0] == griglia[i][1] && griglia[i][1] == griglia[i][2]) return griglia[i][0];
            if (griglia[0][i] != CASELLA_VUOTA && griglia[0][i] == griglia[1][i] && griglia[1][i] == griglia[2][i]) return griglia[0][i];
            for (int j = 0; j < N_CASELLE; j++) {
                if (griglia[i][j] == CASELLA_VUOTA) {
                    mosse_terminate = false;
                    break;
                }
            }
        }
        if (mosse_terminate) return 'p';
        else return 'n';
    }
}
