import java.awt.*;

public class Pedone extends Pedina {
    private boolean muoviDiDueCaselle;
    private boolean enpassant;

    public Pedone(Color colore, int[] posizione) {
        super(colore, posizione, 1);
        muoviDiDueCaselle = true;
        enpassant = false;
    }

    public boolean getEnpassant() {
        return enpassant;
    }

    @Override
    public void muovi(int[] posizione) {
        int[] posizione_attuale = getPosizione();
        if (getColore() == Color.white) {
            if (posizione[0] > 7 || posizione[0] < 2 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Questa casella non esiste");
            if (muoviDiDueCaselle && posizione_attuale[0] == posizione[0] + 2 && posizione_attuale[1] == posizione[1]) enpassant = true;
            else if (posizione_attuale[0] != posizione[0] + 1) throw new IllegalArgumentException("Posizione non valida");
            else if (posizione_attuale[1] != posizione[1] && posizione[1] != posizione_attuale[1] - 1 && posizione[1] != posizione_attuale[1] + 1) throw new IllegalArgumentException("Posizione non valida");
            else enpassant = false;
        }
        else {
            if (posizione[0] < 0 || posizione[0] > DIMENSIONE_SCACCHIERA - 3 || posizione[1] < 0 || posizione[1] > DIMENSIONE_SCACCHIERA - 1) throw new IllegalArgumentException("Questa casella non esiste");
            if (muoviDiDueCaselle && posizione_attuale[0] == posizione[0] - 2 && posizione_attuale[1] == posizione[1]) enpassant = true;
            else if (posizione_attuale[0] != posizione[0] - 1) throw new IllegalArgumentException("Posizione non valida");
            else if (posizione_attuale[1] != posizione[1] && posizione[1] != posizione_attuale[1] - 1 && posizione[1] != posizione_attuale[1] + 1) throw new IllegalArgumentException("Posizione non valida");
            else enpassant = false;
        }
        muoviDiDueCaselle = false;
        setPosizione(posizione);
    }
}
