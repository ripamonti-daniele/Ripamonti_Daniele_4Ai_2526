public class Main {
    public static void main(String[] args) {
        Mostro m1 = new Mostro();
        Mostro m2 = new Mostro();
        m1.cambiaNome("gurgugnao");
        m2.cambiaNome("jiggy");
        System.out.println("nome: " + m1.mostraNome() + ", vita: " + m1.getVita() + ", colore: " + m1.mostraColore());
        System.out.println("nome: " + m2.mostraNome() + ", vita: " + m2.getVita() + ", colore: " + m2.mostraColore());
    }
}
