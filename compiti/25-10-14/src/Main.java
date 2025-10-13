public class Main {
    public static void main(String[] args) throws Exception {
        Furgone f = new Furgone(10);
        f.addPiccolo();
        System.out.println("spazio rimasto: " + f.getSpazioRimasto());
        f.addMedio();
        System.out.println("spazio rimasto: " + f.getSpazioRimasto());
        f.addGrande();
        System.out.println("spazio rimasto: " + f.getSpazioRimasto());
        System.out.println("costo spedizione: " + f.costoSpedizione() + " euro");
        f.empty();
        System.out.println("furgone svuotato, spazio rimasto: " + f.getSpazioRimasto());

    }
}