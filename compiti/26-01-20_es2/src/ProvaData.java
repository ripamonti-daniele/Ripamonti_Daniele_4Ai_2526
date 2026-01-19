public class ProvaData {
    private static int chiediNumero(String msg) {
        int n = -1;

        boolean errore = true;
        while (errore) {
            errore = false;
            try {
                System.out.println(msg);
                n = Integer.parseInt(IO.readln().trim());
            }
            catch (NumberFormatException e) {
                System.out.println("Non hai inserito un numero intero");
                errore = true;
                continue;
            }
            if (n <= 0) {
                System.out.println("Il numero di date deve essere maggiore di 0");
                errore = true;
            }
        }

        return n;
    }

    public static void main() {
        int n_date = chiediNumero("Scegli il numero di date che vuoi inserire");

        DataFormattata[] date = new DataFormattata[n_date];

        for (int i = 0; i < n_date; i++) {
            int giorno = chiediNumero("\nInserisci il giorno della data numero " + (i + 1));
            int mese = chiediNumero("Inserisci il mese della data numero " + (i + 1));
            int anno = chiediNumero("Inserisci l' anno della data numero " + (i + 1));
            System.out.println();
            String scelta = "";
            System.out.println("Inserisci 1 per salvare la data nel formato gg/mm/aaaa");
            System.out.println("Inserisci 2 per salvare la data nel formato gg mese aaaa");
            while (!scelta.equals("1") && !scelta.equals("2")) {
                scelta = IO.readln().trim();
                if (!scelta.equals("1") && !scelta.equals("2")) System.out.println("scelta non valida");
            }
            if (scelta.equals("1")) date[i] = new DataNumerica(giorno, mese, anno);
            else date[i] = new DataEstesa(giorno, mese, anno);
        }

        System.out.println();
        for (DataFormattata d : date) {
            System.out.println(d.stringaFormattata());
        }
    }
}
