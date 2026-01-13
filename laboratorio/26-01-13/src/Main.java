void main() {
    Quadrato quadrato = new Quadrato(50);
    FiguraGeometrica triangolo = new FiguraGeometrica(3, 10);
    System.out.println("perimetro quadrato: " + quadrato.perimetro());
    System.out.println("perimetro triangolo: " + triangolo.perimetro());
}
