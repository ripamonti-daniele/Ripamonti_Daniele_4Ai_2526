import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scacchiera {
    public final int DIMENSIONE = 8;
    private final Pedina[][] caselle;
    private int mosseNeutre;
    private Color turno;
    private int[] casella_selezionata;
    private List<int[]> mosseValide;

    public Scacchiera() {
        caselle = new Pedina[DIMENSIONE][DIMENSIONE];
        mosseNeutre = 0;
        turno = Color.white;
        casella_selezionata = null;
        mosseValide = new ArrayList<>();
        inizializza();
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
        inizializza();
    }

    private Pedina copiaPedina(Pedina p) {
        if (p == null) return null;
        return switch (p.getClass().getSimpleName()) {
            case "Pedone" -> new Pedone((Pedone) p);
            case "Alfiere" -> new Alfiere((Alfiere) p);
            case "Cavallo" -> new Cavallo((Cavallo) p);
            case "Torre" -> new Torre((Torre) p);
            case "Regina" -> new Regina((Regina) p);
            case "Re" -> new Re((Re) p);
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

    private List<int[]> filtraMossePedone(int[] pos, List<int[]> mosseValide) {
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Pedone)) throw new IllegalArgumentException("Puoi fare questi controlli solo sui pedoni");
        List<int[]> mosseFiltrate = new ArrayList<>();
        for (int[] mossa : mosseValide) {
            if (mossa[1] != pos[1] && caselle[mossa[0]][mossa[1]] != null) mosseFiltrate.add(mossa);
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].colore == Color.black && pos[0] == DIMENSIONE - 4) {
                if (caselle[mossa[0] - 1][mossa[1]] != null && caselle[mossa[0] - 1][mossa[1]] instanceof Pedone && ((Pedone) caselle[mossa[0] - 1][mossa[1]]).getEnpassant()) mosseFiltrate.add(mossa);
            }
            else if (mossa[1] != pos[1] && caselle[pos[0]][pos[1]].colore == Color.white && pos[0] == 3) {
                if (caselle[mossa[0] + 1][mossa[1]] != null && caselle[mossa[0] + 1][mossa[1]] instanceof Pedone && ((Pedone) caselle[mossa[0] + 1][mossa[1]]).getEnpassant()) mosseFiltrate.add(mossa);
            }
            else if (mossa[1] == pos[1] && caselle[mossa[0]][mossa[1]] == null) {
                if (Math.abs(mossa[0] - pos[0]) == 1) mosseFiltrate.add(mossa);
                else if (caselle[pos[0]][pos[1]].colore == Color.white && caselle[mossa[0] + 1][mossa[1]] == null) mosseFiltrate.add(mossa);
                else if (caselle[pos[0]][pos[1]].colore == Color.black && caselle[mossa[0] - 1][mossa[1]] == null) mosseFiltrate.add(mossa);
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

    private List<int[]> filtraMosseRe(int[] pos, List<int[]> mosseValide) {
        if (caselle[pos[0]][pos[1]] == null || !(caselle[pos[0]][pos[1]] instanceof Re)) throw new IllegalArgumentException("Puoi fare questi controlli solo sul re");
        List<int[]> mosseFiltrate = new ArrayList<>();

        for (int[] mossa : mosseValide) {
            if (controllaScaccoRe(mossa, caselle[pos[0]][pos[1]].getColore())) continue;

            if (Math.abs(pos[1] - mossa[1]) == 2) {
                if (pos[1] - mossa[1] == 2 && caselle[pos[0]][pos[1] - 1] == null && caselle[pos[0]][pos[1] - 2] == null && ((Re) caselle[pos[0]][pos[1]]).getArrocco() && (caselle[pos[0]][0] instanceof Torre) && ((Torre) caselle[pos[0]][0]).getArrocco()) mosseFiltrate.add(mossa);
                if (pos[1] - mossa[1] == - 2 && caselle[pos[0]][pos[1] + 1] == null && caselle[pos[0]][pos[1] + 2] == null && ((Re) caselle[pos[0]][pos[1]]).getArrocco() && (caselle[pos[0]][DIMENSIONE - 1] instanceof Torre) && ((Torre) caselle[pos[0]][DIMENSIONE - 1]).getArrocco()) mosseFiltrate.add(mossa);
            }
            else mosseFiltrate.add(mossa);
        }

        return mosseFiltrate;
    }

    private int[] trovaPosRe(Color c) {
        for (Pedina[] riga : caselle) {
            for (Pedina p : riga) {
                if (p instanceof Re && p.getColore() == c) return p.getPosizione();
            }
        }
        return null;
    }

    private boolean controllaScaccoRe(int[] posRe, Color coloreRe, int[] posPedinaOld, int[] posPedinaNew) {
        if (posRe == null) throw new IllegalArgumentException("La posizione del re non può essere null");
        if (caselle[posRe[0]][posRe[1]] instanceof Re) coloreRe = caselle[posRe[0]][posRe[1]].getColore();
        if (!coloreRe.equals(Color.white) && !coloreRe.equals(Color.black)) throw new IllegalArgumentException("Il colore del re può essere solo bianco o nero");
        if (posPedinaOld == null && posPedinaNew != null || posPedinaOld != null && posPedinaNew == null) throw new IllegalArgumentException("Non puoi dichiarare solo una delle due posizioni della pedina da spostare");
        boolean controllaPosPedinaNew = false;
        if (posPedinaOld != null) {
            if (caselle[posPedinaOld[0]][posPedinaOld[1]] == null) throw new IllegalArgumentException("Deve esserci una pedina nella posizione old");
            if (caselle[posPedinaOld[0]][posPedinaOld[1]].getColore() != coloreRe) throw new IllegalArgumentException("Il colore della pedina da spostare deve essere lo stesso di quello del re");
            controllaPosPedinaNew = true;
        }


        for (int i = - 2; i <= 2; i += 4) {
            Pedina cavallo;
            if (posRe[0] + i >= 0 && posRe[0] + i < DIMENSIONE && posRe[1] + 1 < DIMENSIONE) {
                cavallo = caselle[posRe[0] + i][posRe[1] + 1];
                if (cavallo instanceof Cavallo && cavallo.getColore() != coloreRe) return true;
            }
            if (posRe[0] + i >= 0 && posRe[0] + i < DIMENSIONE && posRe[1] - 1 >= 0) {
                cavallo = caselle[posRe[0] + i][posRe[1] - 1];
                if (cavallo instanceof Cavallo && cavallo.getColore() != coloreRe) return true;
            }
            if (posRe[1] + i >= 0 && posRe[1] + i < DIMENSIONE && posRe[0] + 1 < DIMENSIONE) {
                cavallo = caselle[posRe[0] + 1][posRe[1] + i];
                if (cavallo instanceof Cavallo && cavallo.getColore() != coloreRe) return true;
            }
            if (posRe[1] + i >= 0 && posRe[1] + i < DIMENSIONE && posRe[0] - 1 >= 0) {
                cavallo = caselle[posRe[0] - 1][posRe[1] + i];
                if (cavallo instanceof Cavallo && cavallo.getColore() != coloreRe) return true;
            }
        }

        //DA RIFARE
        boolean nord = true;
        boolean sud = true;
        boolean est = true;
        boolean ovest = true;
        boolean nordest = true;
        boolean nordovest = true;
        boolean sudest = true;
        boolean sudovest = true;

        for (int i = 1; i < DIMENSIONE; i++) {
            if (nord && posRe[0] - i >= 0 && caselle[posRe[0] - i][posRe[1]] != null && (posPedinaOld == null || !(posRe[0] - i == posPedinaOld[0] && posRe[1] == posPedinaOld[1]))) {
                if (caselle[posRe[0] - i][posRe[1]].getColore() == coloreRe && !(caselle[posRe[0] - i][posRe[1]] instanceof Re) || controllaPosPedinaNew && posRe[0] - i == posPedinaNew[0] && posRe[1] == posPedinaNew[1]) nord = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] - i) + " " + posRe[1]);
                    return true;
                }
            }
            if (sud && posRe[0] + i < DIMENSIONE && caselle[posRe[0] + i][posRe[1]] != null && (posPedinaOld == null || !(posRe[0] + i == posPedinaOld[0] && posRe[1] == posPedinaOld[1]))) {
                if (caselle[posRe[0] + i][posRe[1]].getColore() == coloreRe && !(caselle[posRe[0] + i][posRe[1]] instanceof Re) || controllaPosPedinaNew && posRe[0] + i == posPedinaNew[0] && posRe[1] == posPedinaNew[1]) sud = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] + i) + " " + posRe[1]);
                    return true;
                }
            }
            if (est && posRe[1] + i < DIMENSIONE && caselle[posRe[0]][posRe[1] + i] != null && (posPedinaOld == null || !(posRe[0] == posPedinaOld[0] && posRe[1] + i == posPedinaOld[1]))) {
                if (caselle[posRe[0]][posRe[1] + i].getColore() == coloreRe && !(caselle[posRe[0]][posRe[1] + i] instanceof Re) || controllaPosPedinaNew && posRe[0] == posPedinaNew[0] && posRe[1] + i == posPedinaNew[1]) est = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + posRe[0] + " " + (posRe[1] + i));
                    return true;
                }
            }
            if (ovest && posRe[1] - i >= 0 && caselle[posRe[0]][posRe[1] - i] != null && (posPedinaOld == null || !(posRe[0] == posPedinaOld[0] && posRe[1] - i == posPedinaOld[1]))) {
                if (caselle[posRe[0]][posRe[1] - i].getColore() == coloreRe && !(caselle[posRe[0]][posRe[1] - i] instanceof Re) || controllaPosPedinaNew && posRe[0] == posPedinaNew[0] && posRe[1] - i == posPedinaNew[1]) ovest = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + posRe[0] + " " + (posRe[1] - i));
                    return true;
                }
            }
            if (nordest && posRe[0] - i >= 0 && posRe[1] + i < DIMENSIONE && caselle[posRe[0] - i][posRe[1] + i] != null && (posPedinaOld == null || !(posRe[0] - i == posPedinaOld[0] && posRe[1] + i == posPedinaOld[1]))) {
                if (caselle[posRe[0] - i][posRe[1] + i].getColore() == coloreRe && !(caselle[posRe[0] - i][posRe[1] + i] instanceof Re) || controllaPosPedinaNew && posRe[0] - i == posPedinaNew[0] && posRe[1] + i == posPedinaNew[1]) nordest = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] - i) + " " + (posRe[1] + i));
                    return true;
                }
            }
            if (nordovest && posRe[0] - i >= 0 && posRe[1] - i >= 0 && caselle[posRe[0] - i][posRe[1] - i] != null && (posPedinaOld == null || !(posRe[0] - i == posPedinaOld[0] && posRe[1] - i == posPedinaOld[1]))) {
                if (caselle[posRe[0] - i][posRe[1] - i].getColore() == coloreRe && !(caselle[posRe[0] - i][posRe[1] - i] instanceof Re) || controllaPosPedinaNew && posRe[0] - i == posPedinaNew[0] && posRe[1] - i == posPedinaNew[1]) nordovest = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] - i) + " " + (posRe[1] - i));
                    return true;
                }
            }
            if (sudest && posRe[0] + i < DIMENSIONE && posRe[1] + i < DIMENSIONE && caselle[posRe[0] + i][posRe[1] + i] != null && (posPedinaOld == null || !(posRe[0] + i == posPedinaOld[0] && posRe[1] + i == posPedinaOld[1]))) {
                if (caselle[posRe[0] + i][posRe[1] + i].getColore() == coloreRe && !(caselle[posRe[0] + i][posRe[1] + i] instanceof Re) || controllaPosPedinaNew && posRe[0] + i == posPedinaNew[0] && posRe[1] + i == posPedinaNew[1]) sudest = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] + i) + " " + (posRe[1] + i));
                    return true;
                }
            }
            if (sudovest && posRe[0] + i < DIMENSIONE && posRe[1] - i >= 0 && caselle[posRe[0] + i][posRe[1] - i] != null && (posPedinaOld == null || !(posRe[0] + i == posPedinaOld[0] && posRe[1] - i == posPedinaOld[1]))) {
                if (caselle[posRe[0] + i][posRe[1] - i].getColore() == coloreRe && !(caselle[posRe[0] + i][posRe[1] - i] instanceof Re) || controllaPosPedinaNew && posRe[0] + i == posPedinaNew[0] && posRe[1] - i == posPedinaNew[1]) sudovest = false;
                else {
                    System.out.println("pos re: " + posRe[0] + " " + posRe[1]);
                    System.out.println("pos offest: " + (posRe[0] + i) + " " + (posRe[1] - i));
                    return true;
                }
            }
        }
        return false;
    }

    boolean controllaScaccoRe(int[] posRe, Color coloreRe) {
        return controllaScaccoRe(posRe, coloreRe, null, null);
    }

    public List<int[]> selezionaPedina(int[] pos, Color turno) {
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");
        if (!turno.equals(Color.white) && !turno.equals(Color.black)) throw new IllegalArgumentException("Il colore del turno può essere solo bianco o nero");
        if (caselle[pos[0]][pos[1]] == null) return null;

        Pedina p = caselle[pos[0]][pos[1]];
        if (!p.getColore().equals(turno)) return null;

        List<int[]> mosseValide = p.getMosseValide();

        int x, y;
        for (int i = mosseValide.size() - 1; i >= 0; i--) {
            y = mosseValide.get(i)[0];
            x = mosseValide.get(i)[1];
            if (caselle[y][x] != null && caselle[y][x].getColore() == p.getColore()) mosseValide.remove(i);
        }

        switch (p) {
            case Pedone _ -> mosseValide = filtraMossePedone(pos, mosseValide);
            case Alfiere _ -> mosseValide = filtraMosseAlfiere(pos, mosseValide);
            case Torre _ -> mosseValide = filtraMosseTorre(pos, mosseValide);
            case Regina _ -> mosseValide = filtraMosseTorre(pos, filtraMosseAlfiere(pos, mosseValide));
            case Re _ -> mosseValide = filtraMosseRe(pos, mosseValide);
            case Cavallo _ -> {}
            default -> throw new IllegalStateException("Tipo pedina non valido: " + p);
        }

        if (!(p instanceof Re)) {
            List<int[]> mosseSenzaScacco = new ArrayList<>();

            for (int[] mossa : mosseValide) {
                if (!controllaScaccoRe(trovaPosRe(turno), turno, pos, mossa)) mosseSenzaScacco.add(mossa);
            }

            mosseValide = mosseSenzaScacco;
        }

        this.casella_selezionata = pos;
        this.mosseValide = mosseValide;
        return mosseValide;
    }

    public boolean muoviPedina(int[] pos) {
        if (pos[0] < 0 || pos[0] > DIMENSIONE - 1 || pos[1] < 0 || pos[1] > DIMENSIONE - 1) throw new IllegalArgumentException("Posizione non valida");
        if (casella_selezionata == null) return false;

        boolean valido = false;
        for (int[] mossa : mosseValide) {
            if (mossa[0] == pos[0] && mossa[1] == pos[1]) {
                if (caselle[casella_selezionata[0]][casella_selezionata[1]] != null && caselle[casella_selezionata[0]][casella_selezionata[1]] instanceof Pedone && mossa[1] != casella_selezionata[1] && caselle[mossa[0]][mossa[1]] == null) {
                    if (caselle[casella_selezionata[0]][casella_selezionata[1]].colore == Color.white) caselle[mossa[0] + 1][mossa[1]] = null;
                    if (caselle[casella_selezionata[0]][casella_selezionata[1]].colore == Color.black) caselle[mossa[0] - 1][mossa[1]] = null;
                }
                valido = true;
                break;
            }
        }

        if (valido) {
            for (Pedina[] riga : caselle) for (Pedina p : riga) if (p instanceof Pedone && p.colore != caselle[casella_selezionata[0]][casella_selezionata[1]].colore) ((Pedone) p).rimuoviEnpassant();

            if (caselle[casella_selezionata[0]][casella_selezionata[1]] instanceof Re && casella_selezionata[1] - pos[1] == 2) {
                caselle[pos[0]][0].muovi(new int[]{pos[0], pos[1] + 1});
                caselle[pos[0]][pos[1] + 1] = caselle[pos[0]][0];
                caselle[pos[0]][0] = null;
            }
            else if (caselle[casella_selezionata[0]][casella_selezionata[1]] instanceof Re && casella_selezionata[1] - pos[1] == - 2) {
                caselle[pos[0]][DIMENSIONE - 1].muovi(new int[]{pos[0], pos[1] - 1});
                caselle[pos[0]][pos[1] - 1] = caselle[pos[0]][DIMENSIONE - 1];
                caselle[pos[0]][DIMENSIONE - 1] = null;
            }

            caselle[casella_selezionata[0]][casella_selezionata[1]].muovi(pos);
            caselle[pos[0]][pos[1]] = caselle[casella_selezionata[0]][casella_selezionata[1]];
            caselle[casella_selezionata[0]][casella_selezionata[1]] = null;
        }
        casella_selezionata = null;
        return valido;
    }

    public int[] getCasella_selezionata() {
        if (casella_selezionata == null) return null;
        return casella_selezionata.clone();
    }

    private void promuoviPedone(Pedina pedina) {

    }

    public Color getTurno() {
        return turno;
    }

    public void cambiaTurno() {
        if (turno == Color.white) turno = Color.black;
        else turno = Color.white;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pedina p = caselle[i][j];
                switch (p) {
                    case null -> sb.append(". "); // casella vuota
                    case Pedone pedone -> sb.append("P ");
                    case Torre torre -> sb.append("T ");
                    case Cavallo cavallo -> sb.append("C ");
                    case Alfiere alfiere -> sb.append("A ");
                    case Regina regina -> sb.append("Q ");
                    case Re re -> sb.append("K ");
                    default -> {}
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
