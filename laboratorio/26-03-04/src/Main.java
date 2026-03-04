void main() {
    Albero a = new Albero(7);
    a.inserisci(5);
    a.inserisci(9);
    a.inserisci(18);
    a.inserisci(41);
    a.inserisci(2);
    a.inserisci(20);
    a.inserisci(-4);
    a.inserisci(-28);
    a.inserisci(0);
    a.inserisci(5);
    a.inserisci(1);
    a.inserisci(50);
    a.inserisci(67);
    a.inserisci(69);
    a.inserisci(420);
    a.inserisci(373);
    a.inserisci(3);
    a.inserisci(-3);
    a.inserisci(9);
    a.inserisci(11);

    System.out.println(a);
    System.out.println(a.getNumeroNodi());
}
