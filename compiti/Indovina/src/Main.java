void main() {
    Albero alberoBase = Serializzatore.deSerializza();
    FinestraDomande finestra = new FinestraDomande(alberoBase);
}
