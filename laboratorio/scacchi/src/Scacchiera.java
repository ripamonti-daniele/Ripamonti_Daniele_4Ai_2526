import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scacchiera {
    public final int DIMENSIONE = 8;
    private final Pedina[][] caselle;
    private Color turno;
    private int mosseNeutre;
    private int[] casella_selezionata;
    private int mosse;
    private List<int[]> mosseValide;
    private final String SEP;

    public Scacchiera() {
        caselle = new Pedina[DIMENSIONE][DIMENSIONE];
        mosseNeutre = 0;
        mosse = 0;
        turno = Color.white;
        casella_selezionata = null;
        mosseValide = new ArrayList<>();
        inizializza();
        SEP = ";";
        scriviScacchiera();
    }

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
        mosseNeutre = 0;
        mosse = 0;
        turno = Color.white;
        casella_selezionata = null;
        mosseValide.clear();
        inizializza();
        scriviScacchiera();
    }

    private Pedina copiaPedina(Pedina p) {
        if (p == null) return null;
        return switch (p) {
            case Pedone _ -> new Pedone((Pedone) p);
            case Alfiere _ -> new Alfiere((Alfiere) p);
            case Cavallo _ -> new Cavallo((Cavallo) p);
            case Torre _ -> new Torre((Torre) p);
            case Regina _ -> new Regina((Regina) p);
            case Re _ -> new Re((Re) p);
            default -> throw new IllegalArgumentException("Tipo di pedina non valido");
        };
    }

    public Pedina[][] getScacchiera() {
        Pedina[][] copia = new Pedina[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < caselle.length; i++) {
            for (int j = 0; j < caselle[i].length; j++) {
                if (caselle[i][j] == null) copia[i][j] = null;
                else copia[i][j] = copiaPedina(caselle[i][j]);
            }
        }
        return copia;
    }

    public Pedina getPedina(int[] pos) {
        if (caselle[pos[0]][pos[1]] == null) return null;
        return caselle[pos[0]][pos[1]].copy();
    }

    public String[][] getTipoPedine() {
        String[][] tipoPedine = new String[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < tipoPedine.length; i++) {
            for (int j = 0; j < tipoPedine[i].length; j++) {
                if (caselle[i][j] == null) tipoPedine[i][j] = null;
                else tipoPedine[i][j] = caselle[i][j].getClass().getSimpleName();
            }
        }
        return tipoPedine;
    }

    public int getMosseNeutre() {
        return mosseNeutre;
    }

    public int getMosse() {
        return mosse;
    }

    private List<int[]> filtraMossePedone(int[] pos, List<int[]> mosseValide) {
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("Puoi fare questi controlli solo sui pedoni");
        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) {
            if (mossa[1] != pos[1] && caselle[mossa[0]][mossa[1]] != null) mosseFiltrate.add(mossa);
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore() == Color.black && pos[0] == DIMENSIONE - 4) {
                if (caselle[mossa[0] - 1][mossa[1]] != null && caselle[mossa[0] - 1][mossa[1]] instanceof Pedone && ((Pedone) caselle[mossa[0] - 1][mossa[1]]).getEnpassant()) mosseFiltrate.add(mossa);
            }
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].getColore() == Color.white && pos[0] == 3) {
                if (caselle[mossa[0] + 1][mossa[1]] != null && caselle[mossa[0] + 1][mossa[1]] instanceof Pedone && ((Pedone) caselle[mossa[0] + 1][mossa[1]]).getEnpassant()) mosseFiltrate.add(mossa);
            }
            else if (mossa[1] == pos[1] && caselle[mossa[0]][mossa[1]] == null) {
                if (Math.abs(mossa[0] - pos[0]) == 1) mosseFiltrate.add(mossa);
                else if (caselle[pos[0]][pos[1]].getColore() == Color.white && caselle[mossa[0] + 1][mossa[1]] == null) mosseFiltrate.add(mossa);
                else if (caselle[pos[0]][pos[1]].getColore() == Color.black && caselle[mossa[0] - 1][mossa[1]] == null) mosseFiltrate.add(mossa);
            }
        }

        return mosseFiltrate;
    }

    private List<int[]> filtraMosseAlfiere(int[] pos, List<int[]> mosseValide) {
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Alfiere || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sugli alfieri o sulle regine");
        List<int[]> mosseFiltrate = new ArrayList<>();

        int[][] vincoli = new int[4][2];
        for (int i = 0; i < 4; i++) vincoli[i] = null;

        int i = pos[0] + 1;
        int j = pos[1] + 1;
        while (i < DIMENSIONE && j < DIMENSIONE) {
            if (caselle[i][j] != null) {
                vincoli[0] = new int[]{i, j};
                break;
            }
            i++;
            j++;
        }
        if (vincoli[0] == null) vincoli[0] = new int[]{--i, --j};

        i = pos[0] + 1;
        j = pos[1] - 1;
        while (i < DIMENSIONE && j >= 0) {
            if (caselle[i][j] != null) {
                vincoli[1] = new int[]{i, j};
                break;
            }
            i++;
            j--;
        }
        if (vincoli[1] == null) vincoli[1] = new int[]{--i, ++j};

        i = pos[0] - 1;
        j = pos[1] + 1;
        while (i >= 0 && j < DIMENSIONE) {
            if (caselle[i][j] != null) {
                vincoli[2] = new int[]{i, j};
                break;
            }
            i--;
            j++;
        }
        if (vincoli[2] == null) vincoli[2] = new int[]{++i, --j};

        i = pos[0] - 1;
        j = pos[1] - 1;
        while (i >= 0 && j >= 0) {
            if (caselle[i][j] != null) {
                vincoli[3] = new int[]{i, j};
                break;
            }
            i--;
            j--;
        }
        if (vincoli[3] == null) vincoli[3] = new int[]{++i, ++j};

        for (int[] mossa : mosseValide) {
            if (mossa[0] > pos[0] && mossa[1] > pos[1] && mossa[0] <= vincoli[0][0] && mossa[1] <= vincoli[0][1]) mosseFiltrate.add(mossa);
            else if (mossa[0] > pos[0] && mossa[1] < pos[1] && mossa[0] <= vincoli[1][0] && mossa[1] >= vincoli[1][1]) mosseFiltrate.add(mossa);
            else if (mossa[0] < pos[0] && mossa[1] > pos[1] && mossa[0] >= vincoli[2][0] && mossa[1] <= vincoli[2][1]) mosseFiltrate.add(mossa);
            else if (mossa[0] < pos[0] && mossa[1] < pos[1] && mossa[0] >= vincoli[3][0] && mossa[1] >= vincoli[3][1]) mosseFiltrate.add(mossa);
            if (caselle[pos[0]][pos[1]] instanceof Regina && mossa[0] == pos[0] || mossa[1] == pos[1]) mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    private List<int[]> filtraMosseTorre(int[] pos, List<int[]> mosseValide) {
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Torre || caselle[pos[0]][pos[1]] instanceof Regina)) throw new IllegalArgumentException("Puoi fare questi controlli solo sulle torri o sulle regine");
        List<int[]> mosseFiltrate = new ArrayList<>();

        int YAlto = 0;
        int YBasso = DIMENSIONE - 1;
        int XSinistra = 0;
        int XDestra = DIMENSIONE - 1;

        for (int i = 0; i < DIMENSIONE; i++) {
            if (i < pos[0] && i > YAlto && caselle[i][pos[1]] != null) YAlto = i;
            else if (i > pos[0] && i < YBasso && caselle[i][pos[1]] != null) YBasso = i;
            if (i < pos[1] && i > XSinistra && caselle[pos[0]][i] != null) XSinistra = i;
            else if (i > pos[1] && i < XDestra && caselle[pos[0]][i] != null) XDestra = i;
        }

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
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Re)) throw new IllegalArgumentException("Puoi fare questi controlli solo sul re");
        List<int[]> mosseFiltrate = new ArrayList<>();

        boolean annullaArroccoSx = controllaScacco && controllaScaccoRe(pos);
        boolean annullaArroccoDx = annullaArroccoSx;

        for (int[] mossa : mosseValide) {
            if (controllaScacco && controllaScaccoRe(mossa, caselle[pos[0]][pos[1]].getColore())) {
                if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] + 1) annullaArroccoDx = true;
                else if ((mossa[0] == 0 || mossa[0] == DIMENSIONE - 1) && mossa[1] == pos[1] - 1) annullaArroccoSx = true;
                continue;
            }

            if (Math.abs(pos[1] - mossa[1]) == 2 && ((Re) caselle[pos[0]][pos[1]]).getArrocco()) {
                if (pos[1] - mossa[1] == 2 && caselle[pos[0]][pos[1] - 1] == null && caselle[pos[0]][pos[1] - 2] == null && caselle[pos[0]][pos[1] - 3] == null && (caselle[pos[0]][0] instanceof Torre) && ((Torre) caselle[pos[0]][0]).getArrocco()) mosseFiltrate.add(mossa);
                if (pos[1] - mossa[1] == - 2 && caselle[pos[0]][pos[1] + 1] == null && caselle[pos[0]][pos[1] + 2] == null && (caselle[pos[0]][DIMENSIONE - 1] instanceof Torre) && ((Torre) caselle[pos[0]][DIMENSIONE - 1]).getArrocco()) mosseFiltrate.add(mossa);
            }
            else mosseFiltrate.add(mossa);
        }

        if (annullaArroccoDx || annullaArroccoSx) {
            int[][] eliminaArrocco = new int[][]{null, null};
            for (int[] mossa : mosseFiltrate) {
                if (mossa[1] - pos[1] == 2 && annullaArroccoDx) eliminaArrocco[0] = mossa;
                else if (mossa[1] - pos[1] == - 2 && annullaArroccoSx) eliminaArrocco[1] = mossa;
            }
            mosseFiltrate.remove(eliminaArrocco[0]);
            mosseFiltrate.remove(eliminaArrocco[1]);
        }

        return mosseFiltrate;
    }

    private List<int[]> rimuoviMosseStessoColore(List<int[]> mosseValide, Color colorePedina) {
        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) if (caselle[mossa[0]][mossa[1]] == null || !caselle[mossa[0]][mossa[1]].getColore().equals(colorePedina)) mosseFiltrate.add(mossa);
        return mosseFiltrate;
    }

    private List<int[]> ottieniMosseFiltrate(int[] pos, boolean controllaScacco) {
        Pedina p = caselle[pos[0]][pos[1]];
        if (p == null) throw new IllegalArgumentException("Non puoi inserire una posizione che corrisponde a null nella scacchiera");
        List<int[]> mosseValide = p.getMosseValide();
        mosseValide = rimuoviMosseStessoColore(mosseValide, caselle[pos[0]][pos[1]].getColore());
        switch (p) {
            case Pedone _ -> mosseValide = filtraMossePedone(pos, mosseValide);
            case Alfiere _ -> mosseValide = filtraMosseAlfiere(pos, mosseValide);
            case Torre _ -> mosseValide = filtraMosseTorre(pos, mosseValide);
            case Regina _ -> mosseValide = filtraMosseTorre(pos, filtraMosseAlfiere(pos, mosseValide));
            case Re _ -> mosseValide = filtraMosseRe(pos, mosseValide, controllaScacco);
            case Cavallo _ -> mosseValide = rimuoviMosseStessoColore(mosseValide, p.getColore());
            default -> throw new IllegalStateException("Tipo pedina non valido: " + p.getClass().getSimpleName());
        }
        return mosseValide;
    }

    private List<int[]> ottieniMosseFiltrate(int[] pos) {
        return ottieniMosseFiltrate(pos, true);
    }

    private int[] trovaPosRe(Color c) {
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p instanceof Re && p.getColore() == c) return p.getPosizione();
            }
        }
        return null;
    }

    private boolean controllaScaccoRe(int[] posRe, Color coloreRe) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può avere valore null");
        if (coloreRe == null) {
            if (caselle[posRe[0]][posRe[1]] == null) throw new IllegalArgumentException("Non è stato fornito il colore del Re");
            coloreRe = caselle[posRe[0]][posRe[1]].getColore();
        }
        if (!coloreRe.equals(Color.white) && !coloreRe.equals(Color.black)) throw new IllegalArgumentException("Il colore del re può essere solo bianco o nero");

        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (caselle[i][j] == null || caselle[i][j].getColore() == coloreRe) continue;
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
        return controllaScaccoRe(posRe, caselle[posRe[0]][posRe[1]].getColore());
    }

    private List<int[]> filtraMosseScacco(int[] pos, List<int[]> mosseValide) {
        if (pos == null || caselle[pos[0]][pos[1]] == null) throw new IllegalArgumentException("La posizione della pedina da controllare non può avere valore null e non può essere null nella scacchiera");
        List<int[]> mosseFiltrate = new ArrayList<>();
        Color c = caselle[pos[0]][pos[1]].getColore();

        for (int[] mossa : mosseValide) {
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

    public List<int[]> selezionaPedina(int[] pos, Color turno) {
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");
        if (caselle[pos[0]][pos[1]] == null) return null;

        Pedina p = caselle[pos[0]][pos[1]];
        if (!p.getColore().equals(turno)) return null;

        for (Pedina ped : caselle[0]) if (ped instanceof Pedone) promuoviPedone(ped.getPosizione(), 1);
        for (Pedina ped : caselle[DIMENSIONE - 1]) if (ped instanceof Pedone) promuoviPedone(ped.getPosizione(), 1);

        this.mosseValide = filtraMosseScacco(pos, ottieniMosseFiltrate(pos));
        this.casella_selezionata = pos;
        return mosseValide;
    }

    public boolean muoviPedina(int[] pos) {
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");
        if (casella_selezionata == null || caselle[casella_selezionata[0]][casella_selezionata[1]] == null) return false;

        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == pos[0] && mossa[1] == pos[1]) {
                valido = true;
                break;
            }
        }

        if (valido) {
            Pedina p = caselle[casella_selezionata[0]][casella_selezionata[1]];

            if (!(mosseNeutre == 0 && turno == Color.black)) mosseNeutre++;
            if (caselle[pos[0]][pos[1]] != null || p instanceof Pedone) mosseNeutre = 0;

            if (p instanceof Pedone && pos[1] != casella_selezionata[1] && caselle[pos[0]][pos[1]] == null) {
                if (p.getColore() == Color.white) caselle[pos[0] + 1][pos[1]] = null;
                if (p.getColore() == Color.black) caselle[pos[0] - 1][pos[1]] = null;
            }

            for (Pedina[] riga : caselle) for (Pedina ped : riga) if (ped instanceof Pedone && ped.getColore() != turno) ((Pedone) ped).rimuoviEnpassant();

            //arrocco
            if (p instanceof Re && casella_selezionata[1] - pos[1] == 2) {
                caselle[pos[0]][0].muovi(new int[]{pos[0], pos[1] + 1});
                caselle[pos[0]][pos[1] + 1] = caselle[pos[0]][0];
                caselle[pos[0]][0] = null;
            }
            else if (p instanceof Re && casella_selezionata[1] - pos[1] == - 2) {
                caselle[pos[0]][DIMENSIONE - 1].muovi(new int[]{pos[0], pos[1] - 1});
                caselle[pos[0]][pos[1] - 1] = caselle[pos[0]][DIMENSIONE - 1];
                caselle[pos[0]][DIMENSIONE - 1] = null;
            }

            p.muovi(pos);
            caselle[pos[0]][pos[1]] = p;
            caselle[casella_selezionata[0]][casella_selezionata[1]] = null;

            mosseValide.clear();
            if (!(p instanceof Pedone && (pos[0] == DIMENSIONE -1 || pos[0] == 0))) {
                mosse++;
                scriviScacchiera();
            }
        }
        casella_selezionata = null;
        return valido;
    }

    public void promuoviPedone(int[] pos, int numeroPedina) {
        if (!((pos[0] == 0 || pos[0] == 7) && caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("La pedina che hai scelto non è un pedone in fondo alla scacchiera");
        Color c = caselle[pos[0]][pos[1]].getColore();

        switch (numeroPedina) {
            case 2 -> caselle[pos[0]][pos[1]] = new Torre(c, pos);
            case 3 -> caselle[pos[0]][pos[1]] = new Alfiere(c, pos);
            case 4 -> caselle[pos[0]][pos[1]] = new Cavallo(c, pos);
            default -> caselle[pos[0]][pos[1]] = new Regina(c, pos);
        }
        mosse++;
        scriviScacchiera();
    }

    // -1 partita non finita; 0 vittoria bianco; 1 vittoria nero; 2 stallo; 3 materiale insufficiente; 4 pareggio ripetizioni; 5 pareggio mosse neutre
    public int getStatoPartita() {
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
        if (noMosse) {
            if (controllaScaccoRe(turno)) {
                if (turno.equals(Color.white)) return 1;
                else return 0;
            }
            return 2;
        }
        if (materialeInsufficiente(Color.black) && materialeInsufficiente(Color.white)) return 3;
        if (mosseNeutre >= 150) return 4;
        if (pareggioRipetizioni()) return 5;
        return -1;
    }

    public int[] getCasella_selezionata() {
        if (casella_selezionata == null) return null;
        return casella_selezionata.clone();
    }

    public Color getTurno() {
        return turno;
    }

    public void cambiaTurno() {
        if (turno == Color.white) turno = Color.black;
        else turno = Color.white;
    }

    public int getMateriale(Color c) {
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        int materiale = 0;
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) if (p != null && p.getColore().equals(c)) materiale += p.getMateriale();
        }
        return materiale;
    }

    public boolean materialeInsufficiente(Color c) {
        if (!c.equals(Color.black) && !c.equals(Color.white)) throw new IllegalArgumentException("Il colore del giocatore scelto può essere solo bianco o nero");
        int materiale = getMateriale(c);
        if (materiale == 0) return true;
        if (materiale == 3) {
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) if (p != null && p.getColore().equals(c) && p instanceof Cavallo || p instanceof Alfiere) return true;
            }
        }
        return false;
    }

    public boolean pareggioRipetizioni() {
        if (mosse < 17) return false;
        String mossaCorrente = getStringaScacchieraMossa(mosse,true);
        String mossaAvversario = getStringaScacchieraMossa(mosse - 1, true);
        int mosseRipetute = 0;

        for (int i = mosse - 4; i > 0; i -= 2) {
            if (getStringaScacchieraMossa(i, true).equals(mossaCorrente) && getStringaScacchieraMossa(i - 1, true).equals(mossaAvversario)) mosseRipetute++;
        }
        return mosseRipetute >= 4;
    }

    private void scriviScacchiera() {
        BufferedWriter writer;
        if (mosse == 0) {
            try {
                writer = new BufferedWriter(new FileWriter("partita.txt"));
                writer.write("");
                writer.close();
            }
            catch (IOException _) {
                return;
            }
        }

        try {
            writer = new BufferedWriter(new FileWriter("partita.txt", true));
            writer.write(mosse + "\n" + getStringaScacchiera(true) + "\n");
            writer.close();
        }
        catch (IOException _) {}
    }

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
        if (info) {
            StringBuilder infoRipetizioni = new StringBuilder();
            for (Pedina[] riga : caselle) {
                for (Pedina p : riga) {
                    if (p == null) continue;
                    int[] pos = p.getPosizione();
                    if (p instanceof Pedone && p.getColore() != turno) {
                        for (int[] mossa : filtraMosseScacco(pos, ottieniMosseFiltrate(pos))) {
                            if (mossa[1] == pos[1] - 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("sx");
                            if (mossa[1] == pos[1] + 1 && caselle[mossa[0]][mossa[1]] == null) infoRipetizioni.append("ep").append(pos[1] + 1).append("dx");
                        }
                    }
                    else if (p instanceof Re) {
                        String col = "B";
                        if (!p.getColore().equals(Color.white)) col = "N";
                        for (int[] mossa : filtraMosseScacco(pos, ottieniMosseFiltrate(pos))) {
                            if (mossa[1] == pos[1] - 2) infoRipetizioni.append("asx").append(col);
                            if (mossa[1] == pos[1] + 2) infoRipetizioni.append("adx").append(col);
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

    public String getStringaScacchieraMossa(int mossa, boolean info) {
        if (mossa < 0 || mossa > mosse) return null;

        StringBuilder scacchiera = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("partita.txt"));
            for (int i = 0; i < mossa * 10 + 1; i++) reader.readLine();
            int iterazioni = 8;
            if (info) iterazioni++;
            for (int i = 0; i < iterazioni; i++) {
                scacchiera.append(reader.readLine());
                if (i < iterazioni - 1) scacchiera.append("\n");
            }
        }
        catch (IOException e) {
            return null;
        }

        return scacchiera.toString();
    }

    public String getStringaScacchieraMossa(int mossa) {
        return getStringaScacchieraMossa(mossa, false);
    }

    @Override
    public String toString() {
        return getStringaScacchiera();
    }
}
