void main() {
    Finestra f1 = Finestra.getInstance();
    Finestra f2 = Finestra.getInstance();
    Finestra f3 = Finestra.getInstance();
    Finestra f4 = Finestra.getInstance();

    System.out.println(f1.equals(f2));
}
