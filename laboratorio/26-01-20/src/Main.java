//classe per creare automobili
//crud
//crea 5 macchine scegliendo il colore a caso
//filtra per tempo

import java.awt.*;
import java.util.List;

int chiediNumero(String msg) {
    System.out.println(msg);
    int n = 0;
    boolean errore = true;
    while (errore) {
        errore = false;
        try {
            n = Integer.parseInt(IO.readln().trim());
        }
        catch (NumberFormatException e) {
            errore = true;
            System.out.println("Non hai inserito un numero intero");
        }
    }
    return n;
}

void create(List<Automobile> magazzino, Map<Integer, Color> ottieniColore) {
    String marca = "";
    String modello = "";
    String targa = "";
    Color colore = null;
    int km = 0;
    LocalDate data = null;
    boolean esito;
//
//    do {
//        System.out.println("Inserisci la marca: ");
//        marca = IO.readln().trim();
//        esito = Automobile.controllaMarca(marca);
//       if (!esito)
//
//    } while (!esito);

}

void inizializza(List<Automobile> magazzino) {
    magazzino.add(new Automobile("toyota", "corolla", "AA000AA", Color.gray, 20000, LocalDate.now()));
    magazzino.add(new Automobile("ford", "focus", "GH092AJ", Color.blue, 350000, LocalDate.of(2008, 12, 3)));
    magazzino.add(new Automobile("fiat", "punto", "FB237JK", Color.white, 120000, LocalDate.of(2016, 3, 14)));
}

void main() {
    List<Automobile> magazzino = new ArrayList<>();
    inizializza(magazzino);

    Map<Integer, Color> ottieniColore = new HashMap<>();
    ottieniColore.put(1, Color.red);
    ottieniColore.put(2, Color.blue);
    ottieniColore.put(3, Color.green);
    ottieniColore.put(4, Color.magenta);
    ottieniColore.put(5, Color.yellow);

    //fai crud con swing
}
