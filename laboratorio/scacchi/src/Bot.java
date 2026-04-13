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
        private final Color turno;
        private final int profondita;
        private int[] casellaSelezionata;
        private int[] mossa;

        public Nodo(Pedina[][] scacchiera, int[] casellaSelezionata, int[] mossa, Color turno, int profondita) {
            if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
            this.caselle = scacchiera;
            this.sottoNodi = new ArrayList<>();
            this.turno = turno;
            this.mossa = mossa;
            this.casellaSelezionata = casellaSelezionata;
            this.profondita = profondita;
            trovaVantaggio();
        }

        private void trovaVantaggio() {
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) {
                    if (p == null) continue;
                    if (p.getColore() == colore) vantaggio += p.getMateriale();
                    else vantaggio -= p.getMateriale();
                }
            }
            if (!colore.equals(turno)) vantaggio *= -1;
            statoPartita = scacchiera.simulaStatoPartita(caselle, turno);
            if (statoPartita == StatoPartita.PROMOZIONE_IN_SOSPESO) {
                aggiungiNodo(new Nodo(pedinePromosse(2), mossa, casellaSelezionata, turno, 0), this);
                aggiungiNodo(new Nodo(pedinePromosse(3), mossa, casellaSelezionata, turno, 0), this);
                aggiungiNodo(new Nodo(pedinePromosse(4), mossa, casellaSelezionata, turno, 0), this);
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

            List<Nodo> nuovi = new ArrayList<>();
            Color prossimoTurno = Color.white;
            if (prossimoTurno.equals(turno)) prossimoTurno = Color.black;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (caselle[i][j] == null || !caselle[i][j].getColore().equals(turno)) continue;
                    int[] casellaSelezionata = new int[]{i, j};
                    List<int[]> mosse = scacchiera.simulaSelezionePedina(caselle, casellaSelezionata, turno);
                    if (mosse == null) continue;
                    for (int[] mossa : mosse) {
                        Pedina[][] copia = copiaCaselle();
                        if (scacchiera.simulaSpostamento(copia, mosse, casellaSelezionata, mossa)) {
                            nuovi.add(new Nodo(copia, new int[]{i, j}, mossa, prossimoTurno, profondita - 1));
                            nuovi.getLast().creaLayer(profondita - 1);
                        }
                    }
                }
            }
            sottoNodi.addAll(nuovi);
//            System.out.println("aa" + sottoNodi.size());
//            System.out.println("---------------------");
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
        root = new Nodo(scacchiera.getCaselle(), null, null, colore, PROFONDITA);
        root.creaLayer(PROFONDITA);
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

//    public void mossaAvversario(Pedina[][] caselle) {
//        System.out.println("sottoNodi disponibili: " + root.sottoNodi.size());
//        for (Nodo n : root.sottoNodi) {
//            boolean uguale = true;
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    Pedina a = caselle[i][j];
//                    Pedina b = n.caselle[i][j];
//                    if (a == null && b == null) continue;
//                    if (a == null || b == null || !a.getClass().equals(b.getClass()) || !a.getColore().equals(b.getColore())) {
//                        uguale = false;
//                        break;
//                    }
//                }
//                if (!uguale) break;
//            }
//            if (uguale) {
//                System.out.println("tartora");
//                root = n;
//                root.creaLayer(PROFONDITA);
//                break;
//            }
//            else System.out.println("doc");
//        }
//    }

    public void mossaAvversario(int[] casellaSelezionata, int[] mossa) {
        for (Nodo n : root.sottoNodi) {
            System.out.println("confronto: n.casellaSelezionata=" + java.util.Arrays.toString(n.casellaSelezionata) + " n.mossa=" + java.util.Arrays.toString(n.mossa));
            if (n.casellaSelezionata[0] == casellaSelezionata[0]
                    && n.casellaSelezionata[1] == casellaSelezionata[1]
                    && n.mossa[0] == mossa[0]
                    && n.mossa[1] == mossa[1]) {
                System.out.println("nodo trovato");
                root = n;
                root.creaLayer(PROFONDITA);
                return;
            }
        }
        System.out.println("nodo NON trovato, ricalcolo");
        System.out.println("cercavo: casellaSelezionata=" + java.util.Arrays.toString(casellaSelezionata) + " mossa=" + java.util.Arrays.toString(mossa));
        root = new Nodo(scacchiera.getCaselle(), null, null, colore, PROFONDITA);
        root.creaLayer(PROFONDITA);
    }
}
