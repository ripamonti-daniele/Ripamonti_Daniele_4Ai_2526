public class Main {
    public static void main(String[] args) {
        Mostro m1 = new Mostro();
        Mostro m2 = new Mostro();
        m1.setNome("gurgugnao");
        m2.setNome("jiggy");
//        System.out.println("nome: " + m1.getNome() + ", vita: " + m1.getVita() + ", colore: " + m1.getColore());
//        System.out.println("nome: " + m2.getNome() + ", vita: " + m2.getVita() + ", colore: " + m2.getColore());

        Player p1 = new Player();
        Player p2 = new Player();
        try {
            p1.setNome("balotelli");
            p1.setId("borno01");
            p2.setNome("gurgugnao");
            p2.setId("borno02");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(p1.toString());
        System.out.println(p2.toString());
    }
}
