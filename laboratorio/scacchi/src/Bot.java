import scacchiera_pedine.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bot {
    private class Nodo {
        private final int mossa;
        private static int mosse = 0;
        private Pedina[][] caselle;
        private List<Nodo> sottoNodi;
        private int vantaggio;
        private StatoPartita statoPartita;
        private final int moltiplicatore;

        public Nodo(List<Nodo> sottoNodi, Pedina[][] scacchiera, int moltiplicatore) {
            if (sottoNodi == null) this.sottoNodi = new ArrayList<>();
            else this.sottoNodi = sottoNodi;
            if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
            this.caselle = scacchiera;
            mosse++;
            mossa = mosse;
            trovaVantaggio();
            this.moltiplicatore = moltiplicatore;
            creaLayer();
        }

        public Nodo(Pedina[][] scacchiera, int moltiplicatore) {
            this(null, scacchiera, moltiplicatore);
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
            statoPartita = scacchiera.simulaStatoPartita(caselle, turno);
            if (statoPartita == StatoPartita.PROMOZIONE_IN_SOSPESO) {
                aggiungiNodo(new Nodo(pedinePromosse(2), moltiplicatore), mossa - 1);
                aggiungiNodo(new Nodo(pedinePromosse(3), moltiplicatore), mossa - 1);
                aggiungiNodo(new Nodo(pedinePromosse(4), moltiplicatore), mossa - 1);
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
            if (mossaPrecedente != mossa) {
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
            if (mossa - scacchiera.getMosse() > 5) return;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Pedina[][] copia = copiaCaselle();
                    for (int[] mossa : scacchiera.simulaSelezionePedina(caselle, new int[]{i, j}, turno)) {
                        if (scacchiera.simulaSpostamento(copia, mossa)) sottoNodi.add(new Nodo(copia, moltiplicatore * (-1)));
                    }
                }
            }
        }

        public Pedina[][] getCaselle() {
            return caselle;
        }
    }

    private final Scacchiera scacchiera;
    private final Nodo root;
    private final Color colore;
    private Color turno;

    public Bot(Scacchiera scacchiera, Color colore) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        Nodo.mosse = scacchiera.getMosse() - 1;
        root = new Nodo(scacchiera.getCaselle(), 1);
        this.colore = colore;
        turno = scacchiera.getTurno();
    }

    public void muovi() {

    }


}
