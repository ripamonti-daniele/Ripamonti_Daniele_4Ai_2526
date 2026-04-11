import scacchiera_pedine.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private class Nodo {
        private final int numeroMossa;
        private static int mosse = 0;
        private Pedina[][] caselle;
        private List<Nodo> sottoNodi;
        private int vantaggio;
        private StatoPartita statoPartita;
        private final int moltiplicatore;
        private int[] casellaSelezionata;
        private int[] mossa;

        public Nodo(List<Nodo> sottoNodi, Pedina[][] scacchiera, int[] casellaSelezionata, int[] mossa, int moltiplicatore) {
            if (sottoNodi == null) this.sottoNodi = new ArrayList<>();
            else this.sottoNodi = sottoNodi;
            if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
            this.caselle = scacchiera;
            mosse++;
            numeroMossa = mosse;
            trovaVantaggio();
            this.moltiplicatore = moltiplicatore;
        }

        public Nodo(Pedina[][] scacchiera, int[] casellaSelezionata, int[] mossa, int moltiplicatore) {
            this(null, scacchiera, casellaSelezionata, mossa, moltiplicatore);
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
                aggiungiNodo(new Nodo(pedinePromosse(2), mossa, casellaSelezionata, moltiplicatore), numeroMossa - 1);
                aggiungiNodo(new Nodo(pedinePromosse(3), mossa, casellaSelezionata, moltiplicatore), numeroMossa - 1);
                aggiungiNodo(new Nodo(pedinePromosse(4), mossa, casellaSelezionata, moltiplicatore), numeroMossa - 1);
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

        public void aggiungiNodo(Nodo n, int mossaPrecedente) {
            if (mossaPrecedente != numeroMossa) {
                for (Nodo nodo : sottoNodi) nodo.aggiungiNodo(n, mossaPrecedente);
            }
            else sottoNodi.add(n);
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

        public void creaLayer() {
            if (statoPartita != StatoPartita.IN_CORSO) return;
            if (numeroMossa - scacchiera.getMosse() > 5) return;

            List<Nodo> nuovi = new ArrayList<>();

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Pedina[][] copia = copiaCaselle();
                    if (copia[i][j] == null || !copia[i][j].getColore().equals(colore)) continue;
                    List<int[]> mosse = scacchiera.simulaSelezionePedina(caselle, new int[]{i, j}, colore);
                    if (mosse == null) continue;
                    for (int[] mossa : mosse) {
                        if (scacchiera.simulaSpostamento(copia, mossa)) {
                            nuovi.add(new Nodo(copia, new int[]{i, j}, mossa, moltiplicatore * (-1)));
                        }
                    }
                }
            }
            sottoNodi.addAll(nuovi);
        }

        public Pedina[][] getCaselle() {
            return caselle;
        }
    }

    private final Scacchiera scacchiera;
    private Nodo root;
    private final Color colore;

    public Bot(Scacchiera scacchiera, Color colore) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.colore = colore;
        Nodo.mosse = scacchiera.getMosse() - 1;
        root = new Nodo(scacchiera.getCaselle(), null, null, 1);
        root.creaLayer();
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
        scacchiera.selezionaPedina(scelta.casellaSelezionata);
        scacchiera.muoviPedina(scelta.mossa);
        root = scelta;
        root.creaLayer();
    }

    public void mossaAvversario(Pedina[][] caselle) {
        Nodo scelta = null;
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
                break;
            }
        }
    }
}
