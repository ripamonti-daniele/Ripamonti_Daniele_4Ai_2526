void tettoDisco(StringBuilder s, boolean disegna, int dim, int val) {
    if (disegna) s.append(" ".repeat(dim - val)).append("-".repeat(val * 2 + 1)).append(" ".repeat(dim - val + 1));
    else s.append(" ".repeat(dim)).append("|").append(" ".repeat(dim + 1));
}

void corpoDisco(StringBuilder s, boolean disegna, int dim, int val) {
    int offset = 0;
    if (val >= 10) offset = 1;
    if (disegna) s.append(" ".repeat(dim - val)).append("|").append(" ".repeat(val - 1)).append(val).append(" ".repeat(val - 1 - offset)).append("|").append(" ".repeat(dim - val + 1));
    else s.append(" ".repeat(dim)).append("|").append(" ".repeat(dim + 1));
}

String creaStringaRastrelliere(Stack<Integer> r1, Stack<Integer> r2, Stack<Integer> r3, int dim) {
    boolean disegnaV1;
    boolean disegnaV2;
    boolean disegnaV3;
    int vr1 = 0;
    int vr2 = 0;
    int vr3 = 0;

    StringBuilder s = new StringBuilder();
    s.append(" ".repeat(dim)).append("A").append(" ".repeat(dim * 2 + 1)).append("B").append(" ".repeat(dim * 2 + 1)).append("C").append("\n\n");
    s.append(" ".repeat(dim)).append("|").append(" ".repeat(dim * 2 + 1)).append("|").append(" ".repeat(dim * 2 + 1)).append("|\n");

    for (int i = 1; i <= dim; i++) {
        disegnaV1 = r1.size() - 1 >= dim - i;
        disegnaV2 = r2.size() - 1 >= dim - i;
        disegnaV3 = r3.size() - 1 >= dim - i;

        if (disegnaV1) vr1 = r1.pop();
        if (disegnaV2) vr2 = r2.pop();
        if (disegnaV3) vr3 = r3.pop();

        tettoDisco(s, disegnaV1, dim, vr1);
        tettoDisco(s, disegnaV2, dim, vr2);
        tettoDisco(s, disegnaV3, dim, vr3);
        s.append("\n");

        corpoDisco(s, disegnaV1, dim, vr1);
        corpoDisco(s, disegnaV2, dim, vr2);
        corpoDisco(s, disegnaV3, dim, vr3);
        s.append("\n");
    }
    s.append("-".repeat(dim * 2 + 1)).append(" ").append("-".repeat(dim * 2 + 1)).append(" ").append("-".repeat(dim * 2 + 1)).append("\n");

    return s.toString();
}

void main() {
    Rastrelliera r1 = null;
    Rastrelliera r2 = null;
    Rastrelliera r3 = null;
    int n_dischi = 0;
    int mosse = 0;

    System.out.println("Scegli il numero di dischi con cui giocare: ");
    boolean errore = true;
    while (errore) {
        try {
            n_dischi = Integer.parseInt(IO.readln());
            r1 = new Rastrelliera(n_dischi, true, false);
            r2 = new Rastrelliera(n_dischi, false, false);
            r3 = new Rastrelliera(n_dischi, false, true);
            errore = false;
        }
        catch (NumberFormatException e) {
            System.out.println("Non hai inserito un numero intero: riprova ");
        }
        catch (InvalidParameterException e) {
            System.out.println(e.getMessage() + ": riprova ");
        }
    }

    boolean gioca_ancora = true;

    while (gioca_ancora) {

        boolean gioca = true;
        String scelta1 = "";
        String scelta2;

        while (gioca) {
            System.out.println(creaStringaRastrelliere(r1.getDischi(), r2.getDischi(), r3.getDischi(), n_dischi));

            System.out.println("Scegli la rastrelliera da cui prendere il disco: ");
            errore = true;
            int disco = -1;

            while (errore) {
                errore = false;
                scelta1 = IO.readln().trim().toUpperCase();
                switch (scelta1) {
                    case "A":
                        try {
                            disco = r1.rimuoviDisco();
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    case "B":
                        try {
                            disco = r2.rimuoviDisco();
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    case "C":
                        try {
                            disco = r3.rimuoviDisco();
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    default:
                        System.out.println("Opzione non valida: riprova ");
                        errore = true;
                        break;
                }
            }


            System.out.println("Rastrelliera selezionata\n\nScegli la rastrelliera a cui aggiungere il disco (inserisci q per annullare): ");
            errore = true;

            while (errore) {
                scelta2 = IO.readln().trim().toUpperCase();

                if (scelta2.equals(scelta1)) {
                    System.out.println("Non puoi scegliere la rastrelliera da cui hai tolto il disco: riprova ");
                    continue;
                }

                errore = false;

                switch (scelta2) {
                    case "A":
                        try {
                            gioca = !r1.aggiungiDisco(disco);
                            System.out.println("Disco spostato\n");
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    case "B":
                        try {
                            gioca = !r2.aggiungiDisco(disco);
                            System.out.println("Disco spostato\n");
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    case "C":
                        try {
                            gioca = !r3.aggiungiDisco(disco);
                            System.out.println("Disco spostato\n");
                        } catch (Exception e) {
                            errore = true;
                            System.out.print(e.getMessage() + ": riprova ");
                        }
                        break;

                    case "Q":
                        if (scelta1.equals("A")) r1.aggiungiDisco(disco);
                        else if (scelta1.equals("B")) r2.aggiungiDisco(disco);
                        else if (scelta1.equals("C")) r3.aggiungiDisco(disco);
                        mosse--;
                        break;

                    default:
                        System.out.println("Opzione non valida: riprova ");
                        errore = true;
                        break;
                }
            }
            mosse++;
        }

        System.out.println(creaStringaRastrelliere(r1.getDischi(), r2.getDischi(), r3.getDischi(), n_dischi));
        System.out.println("Hai vinto con " + mosse + " mosse");
        System.out.println("Vuoi giocare ancora? (si/no)");
        if (IO.readln().trim().equalsIgnoreCase("si")) {
            System.out.println("Scegli il numero di dischi con cui giocare: ");
            errore = true;
            while (errore) {
                try {
                    n_dischi = Integer.parseInt(IO.readln());
                    r1.reset(n_dischi);
                    r2.reset(n_dischi);
                    r3.reset(n_dischi);
                    errore = false;
                } catch (NumberFormatException e) {
                    System.out.println("Non hai inserito un numero intero: riprova ");
                } catch (InvalidParameterException e) {
                    System.out.println(e.getMessage() + ": riprova ");
                }
            }
        }
        else gioca_ancora = false;
    }
}
