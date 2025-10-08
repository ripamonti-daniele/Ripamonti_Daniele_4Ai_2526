public class Main {
    public static void main(String[] args) {
        Automobile auto1 = new Automobile();
        Automobile auto2 = new Automobile("Toyota", "Corolla", 2024);
        Automobile auto3 = new Automobile("BMW", "x1", 2020, 10000);
        auto1.stampaDettagli();
        auto2.stampaDettagli();
        auto3.stampaDettagli();
        try {
            auto1.setChilometraggio(50000);
            auto2.setChilometraggio(1000);
            auto3.setChilometraggio(30000);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("età auto1: " + auto1.calcolaEta() + " anni");
        System.out.println("età auto2: " + auto2.calcolaEta() + " anni");
        System.out.println("età auto3: " + auto3.calcolaEta() + " anni");
        System.out.println("-----------------------------------------------------------------------------");
        auto1.stampaDettagli();
        auto2.stampaDettagli();
        auto3.stampaDettagli();
        System.out.println("-----------------------------------------------------------------------------");
        Studente s1 = new Studente("Gjini", "Enis", "4", 2022);
        Studente s2 = new Studente("Mario", "Rossi", "1", 2025);
        try {
            s1.aggiungiVoto(8.5f);
            s2.aggiungiVoto(5.15f);
            s1.aggiungiVoto(4f);
            s2.aggiungiVoto(6.5f);
            s1.aggiungiVoto(6.85f);
            s2.aggiungiVoto(9);
            float media1 = s1.calcolaMedia();
            float media2 = s2.calcolaMedia();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        s1.stampaDettagi();
        s2.stampaDettagi();
    }
}
