import scacchiera_pedine.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private class Nodo {
        private final Pedina[][] caselle;
        private final List<Nodo> sottoNodi;
        private int vantaggio;
        private StatoPartita statoPartita;
        private final Color turno;
        private final int[] casellaSelezionata;
        private final int[] mossa;

        public Nodo(Pedina[][] scacchiera, int[] casellaSelezionata, int[] mossa, Color turno) {
            if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
            this.caselle = scacchiera;
            this.sottoNodi = new ArrayList<>();
            this.turno = turno;
            this.mossa = mossa;
            this.casellaSelezionata = casellaSelezionata;
            trovaVantaggio();
        }

        private void trovaVantaggio() {
            vantaggio = Scacchiera.getMaterialeCaselle(caselle, colore);
            if (colore.equals(Color.white)) vantaggio -= Scacchiera.getMaterialeCaselle(caselle, Color.black);
            else vantaggio -= Scacchiera.getMaterialeCaselle(caselle, Color.white);
            statoPartita = Scacchiera.statoPartitaCaselle(caselle, turno);
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

        private Pedina[][] copiaCaselle(Pedina[][] caselle) {
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
            if (!sottoNodi.isEmpty()) {
                for (Nodo n : sottoNodi) n.creaLayer(profondita - 1);
            }
            else {
                Color prossimoTurno = Color.white;
                if (prossimoTurno.equals(turno)) prossimoTurno = Color.black;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (caselle[i][j] == null || !caselle[i][j].getColore().equals(turno)) continue;
                        int[] casellaSelezionata = new int[]{i, j};
                        List<int[]> mosse = Scacchiera.selezionaPedinaCaselle(caselle, casellaSelezionata, turno);
                        if (mosse == null || mosse.isEmpty()) continue;
                        for (int[] mossa : mosse) {
                            Pedina[][] copia = copiaCaselle(caselle);
                            if (Scacchiera.muoviPedinaCaselle(copia, mosse, casellaSelezionata, mossa)) {
                                Nodo n = new Nodo(copia, casellaSelezionata, mossa, prossimoTurno);
                                if (n.statoPartita == StatoPartita.PROMOZIONE_IN_SOSPESO) {
                                    for (int x = 2; x < 5; x++){
                                        Pedina[][] copiaPromozione = copiaCaselle(copia);
                                        Scacchiera.promozionePedoneCaselle(copiaPromozione, mossa, x);
                                        sottoNodi.add(new Nodo(copiaPromozione, casellaSelezionata, mossa, prossimoTurno));
                                    }
                                    Scacchiera.promozionePedoneCaselle(copia, mossa, 1);
                                }
                                sottoNodi.add(n);
//                                if (profondita == 3) {
//                                    for (Pedina[] riga : n.caselle) {
//                                        for (Pedina p : riga) {
//                                            if (p == null) System.out.print("n");
//                                            else System.out.print(p.getClass().getSimpleName().charAt(0));
//                                            System.out.print("|");
//                                        }
//                                        System.out.println();
//                                    }
//                                    System.out.println("-----------------------------------");
//                                }
                                n.creaLayer(profondita - 1);
                            }
                        }
                    }
                }
            }
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
        root = new Nodo(scacchiera.getCaselle(), null, null, colore);
        root.creaLayer(PROFONDITA);
    }

    public Color getColore() {
        return colore;
    }

    public void muovi() {
        if (!scacchiera.getTurno().equals(colore)) throw new IllegalStateException("Il bot non può muovere se non è il suo turno");
        Nodo scelta = null;
        for (Nodo n : root.sottoNodi) {
            if (n.statoPartita == StatoPartita.VITTORIA_BIANCO && colore == Color.white || n.statoPartita == StatoPartita.VITTORIA_NERO && colore == Color.black) {
                scelta = n;
                break;
            }
            if (scelta == null || n.vantaggio > scelta.vantaggio) scelta = n;
        }
        if (scelta == null) {
            System.out.println("nodo mossa non trovato");
            return;
        }
        scacchiera.selezionaPedina(scelta.casellaSelezionata);
        scacchiera.muoviPedina(scelta.mossa);
//        for (Pedina[] riga : scelta.caselle) {
//            for (Pedina p : riga) {
//                if (p == null) System.out.print("n");
//                else System.out.print(p.getClass().getSimpleName().charAt(0));
//                System.out.print("|");
//            }
//            System.out.println();
//        }
        System.out.println("nodo mossa trovato");
        root = scelta;
        root.creaLayer(PROFONDITA);
    }

    public void mossaAvversario(int[] casellaSelezionata, int[] mossa) {
        for (Nodo n : root.sottoNodi) {
            if (n.casellaSelezionata[0] == casellaSelezionata[0] && n.casellaSelezionata[1] == casellaSelezionata[1] && n.mossa[0] == mossa[0] && n.mossa[1] == mossa[1]) {
                Scacchiera.muoviPedinaCaselle(n.caselle, Scacchiera.selezionaPedinaCaselle(n.caselle, casellaSelezionata, n.turno), casellaSelezionata, mossa);
//                for (Pedina[] riga : n.caselle) {
//                    for (Pedina p : riga) {
//                        if (p == null) System.out.print("n");
//                        else System.out.print(p.getClass().getSimpleName().charAt(0));
//                        System.out.print("|");
//                    }
//                    System.out.println();
//                }
                System.out.println("nodo avversario trovato");
                root = n;
                root.creaLayer(PROFONDITA);
                return;
            }
        }
        System.out.println("nodo avversario non trovato");
    }
}
