import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Televisione t1 = new Televisione("Samsung", 10);

        int canale = -1;
        try {
            System.out.println("inserisci il canale da impostare");
            Scanner s = new Scanner(System.in);
            canale = s.nextInt();
            t1.impostaCanale(canale);
            if (canale == t1.mostraCanale()) System.out.println("il nuovo canale è " + t1.mostraCanale());
        }
        catch (Exception e) {
            System.out.println("formato del canale non corretto");
        }
    }
}
