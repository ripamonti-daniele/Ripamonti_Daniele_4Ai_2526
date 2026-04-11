package scacchiera_pedine;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scacchiera {
    public static final int DIMENSIONE = Pedina.DIMENSIONE_SCACCHIERA;
    private Pedina[][] caselle;
    private Color turno;
    private int mosse;
    private int mosseNeutre;
    private List<int[]> mosseValide;
    private int[] casellaSelezionata;
    private int[] casellaSelezionataSimulazione;
    private static final String SEP = ";";
    private boolean autoCambioTurno;

    public Scacchiera(boolean autoCambioTurno) {
        caselle = new Pedina[DIMENSIONE][DIMENSIONE];
        mosseValide = new ArrayList<>();
        setAutoCambioTurno(autoCambioTurno);
        reset();
    }

    public Scacchiera() {
        this(true);
    }

    // --- inizializzazione e reset ---

    private void inizializza() {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (i == 1) caselle[i][j] = new Pedone(Color.black, new int[]{i, j});
                else if (i == DIMENSIONE - 2) caselle[i][j] = new Pedone(Color.white, new int[]{i, j});
                else if (i == 0) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.black, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.black, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.black, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.black, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.black, new int[]{i, j});
                }
                else if (i == DIMENSIONE - 1) {
                    if (j == 0 || j == DIMENSIONE - 1) caselle[i][j] = new Torre(Color.white, new int[]{i, j});
                    else if (j == 1 || j == DIMENSIONE - 2) caselle[i][j] = new Cavallo(Color.white, new int[]{i, j});
                    else if (j == 2 || j == DIMENSIONE - 3) caselle[i][j] = new Alfiere(Color.white, new int[]{i, j});
                    else if (j == 4) caselle[i][j] = new Re(Color.white, new int[]{i, j});
                    else caselle[i][j] = new Regina(Color.white, new int[]{i, j});
                }
                else caselle[i][j] = null;
            }
        }
    }

    public void reset() {
        turno = Color.white;
        mosse = 0;
        mosseNeutre = 0;
        mosseValide.clear();
        casellaSelezionata = null;
        inizializza();
        PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
    }

    // --- getters e setters ---

    public int getMosse() {
        return mosse;
    }

    public int getMosseNeutre() {
        return mosseNeutre;
    }

    public Pedina[][] getCaselle() {
        Pedina[][] copia = new Pedina[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < caselle.length; i++) {
            for (int j = 0; j < caselle[i].length; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = caselle[i][j].copy();
            }
        }
        return copia;
    }

    public Pedina getPedina(int[] pos) {
        if (pos == null || caselle[pos[0]][pos[1]] == null) return null;
        return caselle[pos[0]][pos[1]].copy();
    }

    public Pedina getPedinaSelezionata() {
        if (casellaSelezionata == null) return null;
        return caselle[casellaSelezionata[0]][casellaSelezionata[1]].copy();
    }

    public int[] getCasellaSelezionata() {
        if (casellaSelezionata == null) return null;
        return casellaSelezionata.clone();
    }

    public Color getTurno() {
        return turno;
    }

    public void setTurno(Color turno) {
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere null");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il turno può essere solo bianco o nero");
        this.turno = turno;
    }

    public static String getSEP() {
        return SEP;
    }

    public boolean getAutoCambioTurno() {
        return autoCambioTurno;
    }

    public void setAutoCambioTurno(boolean autoCambioTurno) {
        this.autoCambioTurno = autoCambioTurno;
    }

    // --- calcolo mosse valide ---

    private List<int[]> filtraMossePedone(int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("Puoi fare questi controlli solo sui pedoni");

        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) {
            //cattura diagonale
            if (mossa[1] != pos[1] && caselle[mossa[0]][mossa[1]] != null) mosseFiltrate.add(mossa);
            //en passant
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore().equals(Color.black) && pos[0] == DIMENSIONE - 4 && caselle[mossa[0] - 1][mossa[1]] instanceof Pedone pedone && pedone.getEnPassant()) mosseFiltrate.add(mossa);
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore().equals(Color.white) && pos[0] == 3 && caselle[mossa[0] + 1][mossa[1]] instanceof Pedone pedone && pedone.getEnPassant()) mosseFiltrate.add(mossa);
            //avanzamento frontale
            else if (mossa[1] == pos[1] && caselle[mossa[0]][mossa[1]] == null) {
                if (Math.abs(mossa[0] - pos[0]) == 1) mosseFiltrate.add(mossa);
                //movimento di due caselle
                else if (caselle[pos[0]][pos[1]].getColore().equals(Color.white) && caselle[mossa[0] + 1][mossa[1]] == null || caselle[pos[0]][pos[1]].getColore().equals(Color.black) && caselle[mossa[0] - 1][mossa[1]] == null) mosseFiltrate.add(mossa);
            }
        }

        return mosseFiltrate;
    }

    private List<int[]> filtraMosseAlfiere(int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Alfiere || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sugli alfieri o sulle regine");

        List<int[]> mosseFiltrate = new ArrayList<>();
        int[][] vincoli = new int[4][2];
        for (int i = 0; i < 4; i++) vincoli[i] = null;
        int y = pos[0];
        int x = pos[1];
        //per ogni direzione imposta il vincolo se trova una casella occupata o se è arrivato in fondo alla scacchiera
        for (int i = 1; i < DIMENSIONE; i++) {
            if (vincoli[0] == null && y + i < DIMENSIONE && x + i < DIMENSIONE && (caselle[y + i][x + i] != null || y + i + 1 >= DIMENSIONE || x + i + 1 >= DIMENSIONE)) vincoli[0] = new int[]{y + i, x + i};
            if (vincoli[1] == null && y + i < DIMENSIONE && x - i >= 0 && (caselle[y + i][x - i] != null || y + i + 1 >= DIMENSIONE || x - i - 1 < 0)) vincoli[1] = new int[]{y + i, x - i};
            if (vincoli[2] == null && y - i >= 0 && x + i < DIMENSIONE && (caselle[y - i][x + i] != null || y - i - 1 < 0 || x + i + 1 >= DIMENSIONE)) vincoli[2] = new int[]{y - i, x + i};
            if (vincoli[3] == null && y - i >= 0 && x - i >= 0 && (caselle[y - i][x - i] != null || y - i - 1 < 0 || x - i - 1 < 0)) vincoli[3] = new int[]{y - i, x - i};
            boolean esci = true;
            for (int[] v : vincoli)
                if (v == null) {
                    esci = false;
                    break;
                }
            if (esci) break;
        }

        //accetta solo le mosse valide che rientrano nei vincoli
        for (int[] mossa : mosseValide) {
            if (vincoli[0] != null && mossa[0] > y && mossa[1] > x && mossa[0] <= vincoli[0][0] && mossa[1] <= vincoli[0][1]) mosseFiltrate.add(mossa);
            else if (vincoli[1] != null && mossa[0] > y && mossa[1] < x && mossa[0] <= vincoli[1][0] && mossa[1] >= vincoli[1][1]) mosseFiltrate.add(mossa);
            else if (vincoli[2] != null && mossa[0] < y && mossa[1] > x && mossa[0] >= vincoli[2][0] && mossa[1] <= vincoli[2][1]) mosseFiltrate.add(mossa);
            else if (vincoli[3] != null && mossa[0] < y && mossa[1] < x && mossa[0] >= vincoli[3][0] && mossa[1] >= vincoli[3][1]) mosseFiltrate.add(mossa);
            else if (caselle[pos[0]][pos[1]] instanceof Regina && (mossa[0] == y || mossa[1] == x)) mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    private List<int[]> filtraMosseTorre(int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Torre || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sulle torri o sulle regine");

        List<int[]> mosseFiltrate = new ArrayList<>();
        int YAlto = -1;
        int YBasso = DIMENSIONE - 1;
        int XSinistra = -1;
        int XDestra = DIMENSIONE - 1;

        //per ogni direzione imposta il vincolo se trova una casella occupata
        for (int i = 0; i < DIMENSIONE; i++) {
            if (i < pos[0] && i > YAlto && caselle[i][pos[1]] != null) YAlto = i;
            else if (i > pos[0] && i < YBasso && caselle[i][pos[1]] != null) YBasso = i;
            if (i < pos[1] && i > XSinistra && caselle[pos[0]][i] != null) XSinistra = i;
            else if (i > pos[1] && i < XDestra && caselle[pos[0]][i] != null) XDestra = i;
        }

        //accetta solo le mosse valide che rientrano nei vincoli
        for (int[] mossa : mosseValide) {
            if (mossa[1] == pos[1] && mossa[0] < pos[0] && mossa[0] >= YAlto) mosseFiltrate.add(mossa);
            if (mossa[1] == pos[1] && mossa[0] > pos[0] && mossa[0] <= YBasso) mosseFiltrate.add(mossa);
            if (mossa[0] == pos[0] && mossa[1] < pos[1] && mossa[1] >= XSinistra) mosseFiltrate.add(mossa);
            if (mossa[0] == pos[0] && mossa[1] > pos[1] && mossa[1] <= XDestra) mosseFiltrate.add(mossa);
            if (caselle[pos[0]][pos[1]] instanceof Regina && mossa[0] != pos[0] && mossa[1] != pos[1]) mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    private List<int[]> filtraMosseRe(int[] pos, List<int[]> mosseValide, boolean controllaScacco) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Re)) throw new IllegalArgumentException("Puoi fare questi controlli solo sul re");

        List<int[]> mosseFiltrate = new ArrayList<>();
        boolean annullaArroccoSx = controllaScacco && controllaScaccoRe(pos);
        boolean annullaArroccoDx = annullaArroccoSx;

        for (int[] mossa : mosseValide) {
            //se il re sarebbe sotto scacco rimuove la mossa
            if (controllaScacco && controllaScaccoRe(mossa, caselle[pos[0]][pos[1]].getColore())) {
                //rimuove l'arrocco se la mossa intermedia comporterebbe uno scacco
                if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] + 1) annullaArroccoDx = true;
                else if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] - 1) annullaArroccoSx = true;
                continue;
            }

            //arrocco
            if (Math.abs(pos[1] - mossa[1]) == 2 && ((Re) caselle[pos[0]][pos[1]]).getArrocco()) {
                if (pos[1] - mossa[1] == 2 && caselle[pos[0]][pos[1] - 1] == null && caselle[pos[0]][pos[1] - 2] == null && caselle[pos[0]][pos[1] - 3] == null && caselle[pos[0]][0] instanceof Torre torre && torre.getArrocco()) mosseFiltrate.add(mossa);
                if (pos[1] - mossa[1] == -2 && caselle[pos[0]][pos[1] + 1] == null && caselle[pos[0]][pos[1] + 2] == null && caselle[pos[0]][DIMENSIONE - 1] instanceof Torre torre && torre.getArrocco()) mosseFiltrate.add(mossa);
            }
            //mossa normale
            else mosseFiltrate.add(mossa);
        }

        if (annullaArroccoDx) mosseFiltrate.removeIf(m -> m[1] - pos[1] == 2);
        if (annullaArroccoSx) mosseFiltrate.removeIf(m -> m[1] - pos[1] == -2);

        return mosseFiltrate;
    }

    private List<int[]> rimuoviMosseStessoColore(List<int[]> mosseValide, Color colorePedina) {
        if (colorePedina == null || mosseValide == null) throw new IllegalArgumentException("Il colore e le mosse valide non possono essere parametri null");
        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) if (caselle[mossa[0]][mossa[1]] == null || !caselle[mossa[0]][mossa[1]].getColore().equals(colorePedina)) mosseFiltrate.add(mossa);
        return mosseFiltrate;
    }

    private List<int[]> ottieniMosseFiltrate(int[] pos, boolean controllaScacco) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        Pedina p = caselle[pos[0]][pos[1]];
        if (p == null) throw new IllegalArgumentException("Non puoi inserire una posizione che corrisponde a null nella scacchiera");
        List<int[]> mosseValide = rimuoviMosseStessoColore(p.getMosseValide(), p.getColore());
        switch (p) {
            case Pedone _ -> mosseValide = filtraMossePedone(pos, mosseValide);
            case Alfiere _ -> mosseValide = filtraMosseAlfiere(pos, mosseValide);
            case Torre _ -> mosseValide = filtraMosseTorre(pos, mosseValide);
            case Regina _ -> mosseValide = filtraMosseTorre(pos, filtraMosseAlfiere(pos, mosseValide));
            case Re _ -> mosseValide = filtraMosseRe(pos, mosseValide, controllaScacco);
            case Cavallo _ -> {}
            default -> throw new IllegalStateException("Tipo pedina non valido: " + p.getClass().getSimpleName());
        }
        return mosseValide;
    }

    private List<int[]> ottieniMosseFiltrate(int[] pos) {
        return ottieniMosseFiltrate(pos, true);
    }

    private int[] trovaPosRe(Color c) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p instanceof Re && p.getColore().equals(c)) return p.getPosizione();
            }
        }
        return null;
    }

    private boolean controllaScaccoRe(int[] posRe, Color coloreRe) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può essere un parametro null");
        if (coloreRe == null) {
            //se non viene fornito il colore del re e la casella nella posizione indicata non èun re lancia eccezione, altrimenti prende il colore della casella indicata
            if (caselle[posRe[0]][posRe[1]] == null || !(caselle[posRe[0]][posRe[1]] instanceof Re)) throw new IllegalArgumentException("Non è stato fornito il colore del re");
            coloreRe = caselle[posRe[0]][posRe[1]].getColore();
        }
        if (!coloreRe.equals(Color.white) && !coloreRe.equals(Color.black)) throw new IllegalArgumentException("Il colore del re può essere solo bianco o nero");

        //simula ogni mossa possibile dell'avversario per vedere se il re sarebbe in scacco
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (caselle[i][j] == null || caselle[i][j].getColore().equals(coloreRe)) continue;
                for (int[] mossa : ottieniMosseFiltrate(new int[]{i, j}, false)) {
                    if (mossa[0] == posRe[0] && mossa[1] == posRe[1] && !(caselle[i][j] instanceof Pedone && mossa[1] == j)) return true;
                }
            }
        }

        return false;
    }

    private boolean controllaScaccoRe(Color coloreRe) {
        return controllaScaccoRe(trovaPosRe(coloreRe), coloreRe);
    }

    private boolean controllaScaccoRe(int[] posRe) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può essere un parametro null");
        return controllaScaccoRe(posRe, caselle[posRe[0]][posRe[1]].getColore());
    }

    private List<int[]> filtraMosseScacco(int[] pos, List<int[]> mosseValide) {
        if (pos == null || mosseValide == null) throw new IllegalArgumentException("La posizione e le mosse valide non possono essere parametri null");
        if (caselle[pos[0]][pos[1]] == null) throw new IllegalArgumentException("La posizione della pedina da controllare non può essere null nella scacchiera");

        List<int[]> mosseFiltrate = new ArrayList<>();
        Color c = caselle[pos[0]][pos[1]].getColore();

        for (int[] mossa : mosseValide) {
            //simula lo spostamento della pedina in ogni mossa disponibile e filtra le mosse che lascerebbero il re sotto scacco
            Pedina temp1 = caselle[pos[0]][pos[1]];
            Pedina temp2 = caselle[mossa[0]][mossa[1]];

            caselle[mossa[0]][mossa[1]] = temp1;
            caselle[pos[0]][pos[1]] = null;

            if (!(temp1 instanceof Re) && !controllaScaccoRe(c) || temp1 instanceof Re && !controllaScaccoRe(mossa, c)) mosseFiltrate.add(mossa);

            caselle[pos[0]][pos[1]] = temp1;
            caselle[mossa[0]][mossa[1]] = temp2;
        }

        return mosseFiltrate;
    }

    // --- gestione gioco utente ---

    public List<int[]> selezionaPedina(Pedina p, Color turno) {
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere un parametro null");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");
        if (p == null || !p.getColore().equals(turno)) return null;
        if (promozioneInSospeso() != null) throw new IllegalStateException("Impossibile selezionare una pedina: ci sono dei pedoni in fondo alla scacchiera non promossi");
        int[] pos = p.getPosizione();

        this.mosseValide = filtraMosseScacco(pos, ottieniMosseFiltrate(pos));
        this.casellaSelezionata = pos;
        return mosseValide;
    }

    public List<int[]> selezionaPedina(int[] pos, Color turno) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (pos[0] < 0 || pos[0] >= DIMENSIONE || pos[1] < 0 || pos[1] >= DIMENSIONE) throw new IllegalArgumentException("Posizione non valida");
        if (caselle[pos[0]][pos[1]] == null) return null;
        return selezionaPedina(caselle[pos[0]][pos[1]], turno);
    }

    public List<int[]> selezionaPedina(Pedina p) {
        return selezionaPedina(p, turno);
    }

    public List<int[]> selezionaPedina(int[] pos) {
        return selezionaPedina(pos, turno);
    }

    public void deSelezionaPedina() {
        casellaSelezionata = null;
        mosseValide.clear();
    }

    public boolean muoviPedina(int[] pos) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (pos[0] < 0 || pos[0] >= DIMENSIONE || pos[1] < 0 || pos[1] >= DIMENSIONE) throw new IllegalArgumentException("Posizione non valida");
        if (casellaSelezionata == null || caselle[casellaSelezionata[0]][casellaSelezionata[1]] == null) return false;

        //si assicura che la mossa scelta sia valida
        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == pos[0] && mossa[1] == pos[1]) {
                valido = true;
                break;
            }
        }

        if (valido) {
            Pedina p = caselle[casellaSelezionata[0]][casellaSelezionata[1]];
            Color c = p.getColore();

            if (!(mosseNeutre == 0 && c.equals(Color.black))) mosseNeutre++;
            if (caselle[pos[0]][pos[1]] != null || p instanceof Pedone) mosseNeutre = 0;

            //en passant
            if (p instanceof Pedone && pos[1] != casellaSelezionata[1] && caselle[pos[0]][pos[1]] == null) {
                if (p.getColore().equals(Color.white)) caselle[pos[0] + 1][pos[1]] = null;
                else if (p.getColore().equals(Color.black)) caselle[pos[0] - 1][pos[1]] = null;
            }

            for (Pedina[] riga : caselle) for (Pedina ped : riga) if (ped instanceof Pedone && !ped.getColore().equals(c)) ((Pedone) ped).rimuoviEnPassant();

            //arrocco
            if (p instanceof Re && casellaSelezionata[1] - pos[1] == 2) {
                //sinistra
                caselle[pos[0]][0].muovi(new int[]{pos[0], pos[1] + 1});
                caselle[pos[0]][pos[1] + 1] = caselle[pos[0]][0];
                caselle[pos[0]][0] = null;
            }
            else if (p instanceof Re && casellaSelezionata[1] - pos[1] == -2) {
                //destra
                caselle[pos[0]][DIMENSIONE - 1].muovi(new int[]{pos[0], pos[1] - 1});
                caselle[pos[0]][pos[1] - 1] = caselle[pos[0]][DIMENSIONE - 1];
                caselle[pos[0]][DIMENSIONE - 1] = null;
            }

            p.muovi(pos);
            caselle[pos[0]][pos[1]] = p;
            caselle[casellaSelezionata[0]][casellaSelezionata[1]] = null;

            mosseValide.clear();

            //se la mossa inizia una promozione la scacchiera verrà aggiornata solo quando verrà chiamato il metodo promuoviPedone
            if (!(p instanceof Pedone && (pos[0] == DIMENSIONE - 1 || pos[0] == 0))) {
                mosse++;
                PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
                cambiaTurno();
            }
        }
        casellaSelezionata = null;
        return valido;
    }

    public Pedina promozioneInSospeso() {
        for (Pedina ped : caselle[0]) if (ped instanceof Pedone) return ped.copy();
        for (Pedina ped : caselle[DIMENSIONE - 1]) if (ped instanceof Pedone) return ped.copy();
        return null;
    }

    public void promuoviPedone(int[] pos, int numeroPedina) {
        if (pos == null) throw new IllegalArgumentException("La posizione non può essere un parametro null");
        if (!((pos[0] == 0 || pos[0] == DIMENSIONE - 1) && caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("La pedina che hai scelto non è un pedone in fondo alla scacchiera");
        Color c = caselle[pos[0]][pos[1]].getColore();

        //1 -> regina; 2 -> torre; 3 -> alfiere; 4 -> cavallo; default -> regina
        switch (numeroPedina) {
            case 2 -> caselle[pos[0]][pos[1]] = new Torre(c, pos);
            case 3 -> caselle[pos[0]][pos[1]] = new Alfiere(c, pos);
            case 4 -> caselle[pos[0]][pos[1]] = new Cavallo(c, pos);
            default -> caselle[pos[0]][pos[1]] = new Regina(c, pos);
        }
        //aggiorna la scacchiera
        mosse++;
        PartitaFileManager.scriviScacchiera(mosse, getStringaScacchiera(true));
        cambiaTurno();
    }

    public void promuoviPedone(Pedina p, int numeroPedina) {
        if (p == null) throw new IllegalArgumentException("La pedina non può essere null");
        promuoviPedone(p.getPosizione(), numeroPedina);
    }

    private void cambiaTurno() {
        if (!autoCambioTurno) return;
        if (turno == Color.white) turno = Color.black;
        else turno = Color.white;
    }

    public List<int[]> simulaSelezionePedina(Pedina[][] caselle, int[] pos, Color turno) {
        Pedina[][] temp1 = this.caselle;
        int[] temp2 = casellaSelezionata;
        this.caselle = caselle;
        List<int[]> mosse = selezionaPedina(caselle[pos[0]][pos[1]], turno);
        casellaSelezionataSimulazione = casellaSelezionata;
        this.caselle = temp1;
        casellaSelezionata = temp2;
        return mosse;
    }

    public boolean simulaSpostamento(Pedina[][] caselle, int[] pos) {
        Pedina[][] temp1 = this.caselle;
        int[] temp2 = casellaSelezionata;
        this.caselle = caselle;
        casellaSelezionata = casellaSelezionataSimulazione;
        boolean statoMossa = muoviPedina(pos);
        this.caselle = temp1;
        casellaSelezionata = temp2;
        return statoMossa;
    }

    // --- condizioni di vittoria / pareggio ---
    
    public StatoPartita getStatoPartita(Color turno) {
        if (turno == null) throw new IllegalArgumentException("Il turno non può essere un parametro null");
        if (!turno.equals(Color.black) && !turno.equals(Color.white)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");

        if (promozioneInSospeso() != null) return StatoPartita.PROMOZIONE_IN_SOSPESO;

        //controlla se il giocatore del colore del turno scelto non ha più mosse disponibili
        boolean noMosse = true;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p == null || !p.getColore().equals(turno)) continue;
                if (!filtraMosseScacco(p.getPosizione(), ottieniMosseFiltrate(p.getPosizione())).isEmpty()) {
                    noMosse = false;
                    break;
                }
            }
            if (!noMosse) break;
        }

        //se non ci sono mosse disponibili e il re indica il vincitore, altrimenti è stallo
        if (noMosse) {
            if (controllaScaccoRe(turno)) {
                if (turno.equals(Color.white)) return StatoPartita.VITTORIA_NERO;
                else return StatoPartita.VITTORIA_BIANCO;
            }
            return StatoPartita.STALLO;
        }
        if (materialeInsufficiente(Color.black) && materialeInsufficiente(Color.white)) return StatoPartita.MATERIALE_INSUFFICIENTE;
        if (mosseNeutre >= 150) return StatoPartita.PAREGGIO_MOSSE_NEUTRE;
        if (pareggioRipetizioni()) return StatoPartita.PAREGGIO_RIPETIZIONI;
        return StatoPartita.IN_CORSO;
    }

    public StatoPartita getStatoPartita() {
        return getStatoPartita(turno);
    }

    public StatoPartita simulaStatoPartita(Pedina[][] caselle, Color turno) {
        Pedina[][] temp = this.caselle;
        this.caselle = caselle;
        StatoPartita statoPartita = getStatoPartita(turno);
        this.caselle = temp;
        return statoPartita;
    }

    public int getMateriale(Color c) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        int materiale = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) if (p != null && p.getColore().equals(c)) materiale += p.getMateriale();
        }
        return materiale;
    }

    public int getMaterialeMossa(Color c, int mossa) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (mossa < 0 || mossa > mosse) throw new IllegalArgumentException("Il valore della mossa non può essere maggiore di quella corrente o minore di 0");
        if (mossa == mosse) return getMateriale(c);
        int materiale = 0;
        String s = PartitaFileManager.leggiScacchiera(mossa);
        if (s == null) return -1;
        //prende la stringa della scacchiera alla mossa indicata come parametro e restituisce il materiale del giocatore del colore passato come parametro
        for (String riga : s.split("\n")) {
            for (String pedina : riga.split(SEP)) {
                if (pedina.length() < 2) continue;
                if (!(pedina.charAt(1) == 'B' && c.equals(Color.white) || pedina.charAt(1) == 'N' && c.equals(Color.black))) continue;
                switch (pedina.charAt(0)) {
                    case 'Q' -> materiale += Regina.MATERIALE;
                    case 'T' -> materiale += Torre.MATERIALE;
                    case 'A' -> materiale += Alfiere.MATERIALE;
                    case 'C' -> materiale += Cavallo.MATERIALE;
                    case 'P' -> materiale += Pedone.MATERIALE;
                    default -> {}
                }
            }
        }
        return materiale;
    }

    //se un giocatore oltre al re non ha pedine o ha solo un cavallo o solo un alfiere allora ha materiale insufficiente per vincere
    public boolean materialeInsufficienteMossa(Color c, int mossa) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere un parametro null");
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        if (mossa < 0 || mossa > mosse) throw new IllegalArgumentException("Il valore della mossa non può essere maggiore di quella corrente o minore di 0");
        int materiale = getMaterialeMossa(c, mossa);
        if (materiale == 0) return true;
        if (materiale == 3) {
            String s = PartitaFileManager.leggiScacchiera(mossa);
            if (s == null) return false;
            for (String riga : s.split("\n")) {
                for (String pedina : riga.split(SEP)) {
                    if (pedina.length() < 2) continue;
                    if (!(pedina.charAt(1) == 'B' && c.equals(Color.white) || pedina.charAt(1) == 'N' && c.equals(Color.black))) continue;
                    if (pedina.charAt(0) == 'C' || pedina.charAt(0) == 'A') return true;
                }
            }
        }
        return false;
    }

    public boolean materialeInsufficiente(Color c) {
        return materialeInsufficienteMossa(c, mosse);
    }

    //se una mossa del bianco seguita da una mossa del nero è identica per 5 volte allora è automaticamente pareggio
    public boolean pareggioRipetizioni() {
        if (mosse < 17) return false;
        String mossaCorrente = PartitaFileManager.leggiScacchiera(mosse, true);
        String mossaAvversario = PartitaFileManager.leggiScacchiera(mosse - 1, true);
        if (mossaCorrente == null || mossaAvversario == null) return false;
        int mosseRipetute = 0;

        for (int i = mosse - 4; i > 0; i -= 2) {
            String m1 = PartitaFileManager.leggiScacchiera(i, true);
            String m2 = PartitaFileManager.leggiScacchiera(i - 1, true);
            if (m1 != null && m2 != null && m1.equals(mossaCorrente) && m2.equals(mossaAvversario)) mosseRipetute++;
        }
        return mosseRipetute >= 4;
    }

    // --- scrittura e lettera scacchiera su file ---

    public String getStringaScacchiera(boolean info) {
        StringBuilder scacchiera;
        scacchiera = new StringBuilder();
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p == null) scacchiera.append("--");
                else {
                    if (p instanceof Regina) scacchiera.append("Q");
                    else scacchiera.append(p.getClass().getSimpleName().charAt(0));
                    if (p.getColore().equals(Color.white)) scacchiera.append("B");
                    else scacchiera.append("N");
                }
                scacchiera.append(SEP);
            }
            scacchiera.deleteCharAt(scacchiera.length() - 1);
            scacchiera.append("\n");
        }
        //per avere un pareggio per ripetizione le condizioni di arrocco e di en passant devono essere sempre uguali, queste vengono scritte se info == true
        if (info) {
            StringBuilder infoRipetizioni = new StringBuilder();
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) {
                    if (p == null) continue;
                    int[] pos = p.getPosizione();
                    String col = "B";
                    if (!p.getColore().equals(Color.white)) col = "N";
                    if (p instanceof Pedone) {
                        for (int[] mossa : filtraMosseScacco(pos, ottieniMosseFiltrate(pos))) {
                            if (mossa[1] == pos[1] - 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("sx").append(col). append(" ");
                            if (mossa[1] == pos[1] + 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("dx").append(col).append(" ");
                        }
                    }
                    else if (p instanceof Re) {
                        for (int[] mossa : filtraMosseScacco(pos, ottieniMosseFiltrate(pos))) {
                            if (mossa[1] == pos[1] - 2) infoRipetizioni.append("asx").append(col).append(" ");
                            if (mossa[1] == pos[1] + 2) infoRipetizioni.append("adx").append(col).append(" ");
                        }
                    }
                }
            }
            scacchiera.append(infoRipetizioni);
        }
        return scacchiera.toString();
    }

    public String getStringaScacchiera() {
        return getStringaScacchiera(false);
    }

    @Override
    public String toString() {
        return getStringaScacchiera();
    }
}
