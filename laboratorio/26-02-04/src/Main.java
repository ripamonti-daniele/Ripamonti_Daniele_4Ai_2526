void main() {
    Mago m = new Mago("Gjini");
    System.out.println(m);

    Guerriero g = new Guerriero("Jiggy");
    System.out.println(g);

    Zombie z = new Zombie("Doc");
    z.cattura(g);
    z.cattura(m);
    System.out.println(z);
}
