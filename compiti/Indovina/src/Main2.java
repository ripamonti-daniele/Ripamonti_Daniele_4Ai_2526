//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    Albero albero = new Albero();
    Nodo nodoCorrente = albero.getRadice();
    Scanner sc = new Scanner(System.in);
    while (!nodoCorrente.isAnimale()) {
        System.out.println(nodoCorrente.getTesto() + " (s/n)");
        String risposta = sc.nextLine();
        if (risposta.equalsIgnoreCase("s") || risposta.equalsIgnoreCase("si")) {
            nodoCorrente = nodoCorrente.si;
        } else {
            nodoCorrente = nodoCorrente.no;
        }
    }
    System.out.println("Penso che l'animale sia: " + nodoCorrente.getTesto());

}
