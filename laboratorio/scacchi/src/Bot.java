import scacchiera_pedine.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private class Nodo {
        private Pedina[][] caselle;
        private List<Nodo> sottoNodi;
        private int vantaggio;
        private StatoPartita statoPartita;
        private final int moltiplicatore;
        private int[] casellaSelezionata;
        private int[] mossa;

        public Nodo(Pedina[][] scacchiera, int[] casellaSelezionata, int[] mossa, int moltiplicatore, int profondita) {
            if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
            this.caselle = scacchiera;
            this.sottoNodi = new ArrayList<>();
            this.moltiplicatore = moltiplicatore;
            this.mossa = mossa;
            this.casellaSelezionata = casellaSelezionata;
            trovaVantaggio();
            creaLayer(profondita);
        }

        private void trovaVantaggio() {
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) {
                    if (p == null) continue;
                    if (p.getColore() == colore) vantaggio += p.getMateriale();
                    else vantaggio -= p.getMateriale();
                }
            }
            vantaggio *= moltiplicatore;
            statoPartita = scacchiera.simulaStatoPartita(caselle, scacchiera.getTurno());
            if (statoPartita == StatoPartita.PROMOZIONE_IN_SOSPESO) {
                aggiungiNodo(new Nodo(pedinePromosse(2), mossa, casellaSelezionata, moltiplicatore, 0), this);
                aggiungiNodo(new Nodo(pedinePromosse(3), mossa, casellaSelezionata, moltiplicatore, 0), this);
                aggiungiNodo(new Nodo(pedinePromosse(4), mossa, casellaSelezionata, moltiplicatore, 0), this);
                caselle = pedinePromosse(1);
            }
        }

        private Pedina[][] pedinePromosse(int tipo) {
            Pedina[][] copia = new Pedina[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null) copia[i][j] = null;
                    else {
                        if ((i == 0 || i == 7) && caselle[i][j] instanceof Pedone) {
                            switch (tipo) {
                                case 2 -> copia[i][j] = new Torre(caselle[i][j].getColore(), caselle[i][j].getPosizione());
                                case 3 -> copia[i][j] = new Alfiere(caselle[i][j].getColore(), caselle[i][j].getPosizione());
                                case 4 -> copia[i][j] = new Cavallo(caselle[i][j].getColore(), caselle[i][j].getPosizione());
                                default -> copia[i][j] = new Regina(caselle[i][j].getColore(), caselle[i][j].getPosizione());
                            }
                        }
                        else copia[i][j] = caselle[i][j].copy();
                    }
                }
            }
            return copia;
        }

        public void aggiungiNodo(Nodo n, Nodo padre) {
            if (this == padre) sottoNodi.add(n);
            else for (Nodo nodo : sottoNodi) nodo.aggiungiNodo(n, padre);
        }

        private Pedina[][] copiaCaselle() {
            Pedina[][] copia = new Pedina[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null) copia[i][j] = null;
                    else copia[i][j] = caselle[i][j].copy();
                }
            }
            return copia;
        }

        public void creaLayer(int profondita) {
            if (statoPartita != StatoPartita.IN_CORSO) return;
            if (profondita <= 0) return;

            Color coloreTurno;
            if (moltiplicatore == 1) coloreTurno = colore;
            else if (colore.equals(Color.white)) coloreTurno = Color.black;
            else coloreTurno = Color.white;

            List<Nodo> nuovi = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(coloreTurno)) continue;
                    int[] casellaSelezionata = new int[]{i, j};
                    List<int[]> mosse = scacchiera.simulaSelezionePedina(caselle, casellaSelezionata, coloreTurno);
                    if (mosse == null) continue;
                    for (int[] mossa : mosse) {
                        Pedina[][] copia = copiaCaselle();
                        if (scacchiera.simulaSpostamento(copia, mosse, casellaSelezionata, mossa)) {
                            nuovi.add(new Nodo(copia, new int[]{i, j}, mossa, moltiplicatore * (-1), profondita - 1));
                        }
                    }
                }
            }
            sottoNodi.addAll(nuovi);
            System.out.println("aa" + sottoNodi.size());
            System.out.println("---------------------");
        }

        public Pedina[][] getCaselle() {
            return caselle;
        }
    }

    private final Scacchiera scacchiera;
    private Nodo root;
    private final Color colore;
    private static final int PROFONDITA = 3;

    public Bot(Scacchiera scacchiera, Color colore) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
        root = new Nodo(scacchiera.getCaselle(), null, null, 1, PROFONDITA);
//        root.creaLayer();
    }

    public Color getColore() {
        return colore;
    }

    public void muovi() {
        if (!scacchiera.getTurno().equals(colore)) throw new IllegalStateException("Il bot non può muovere in quanto non è il suo turno");
        Nodo scelta = null;
        for (Nodo n : root.sottoNodi) {
            if (n.statoPartita == StatoPartita.VITTORIA_BIANCO && colore == Color.white || n.statoPartita == StatoPartita.VITTORIA_NERO && colore == Color.black) {
                scelta = n;
                break;
            }
            if (scelta == null || n.vantaggio > scelta.vantaggio) scelta = n;
        }
        if (scelta == null) return;
        scacchiera.selezionaPedina(scelta.casellaSelezionata);
        scacchiera.muoviPedina(scelta.mossa);
        root = scelta;
        root.creaLayer(PROFONDITA);
    }

    public void mossaAvversario(Pedina[][] caselle) {
        for (Nodo n : root.sottoNodi) {
            boolean uguale = true;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] != n.caselle[i][j]) {
                        uguale = false;
                        break;
                    }
                }
                if (!uguale) break;
            }
            if (uguale) {
                root = n;
//                root.creaLayer();
                break;
            }
        }
    }
}
