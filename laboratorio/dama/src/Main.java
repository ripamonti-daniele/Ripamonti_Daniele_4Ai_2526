import java.awt.Color;

void main(String[] args) {
    Scacchiera s = new Scacchiera();
    Pedina[][] board = s.getPedine();

    stampaScacchiera(board);
}

void stampaScacchiera(Pedina[][] board) {
    for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
            Pedina p = board[r][c];
            if (p == null) {
                System.out.print(". ");
            } else if (p.getColore() == Color.white) {
                System.out.print("b ");
            } else {
                System.out.print("n ");
            }
        }
        System.out.println();
    }
}
