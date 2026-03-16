void main() {
    Grafo g = new Grafo(new Nodo("a"));
    Map<String, Integer> collegamenti = new HashMap<>(Map.of("a", 6));
    g.inserisciNodo(new Nodo("b"), collegamenti);
    collegamenti = new HashMap<>(Map.of("a", 2, "b", 7));
    g.inserisciNodo(new Nodo("c"), collegamenti);
    collegamenti = new HashMap<>(Map.of("b", 1));
    g.inserisciNodo(new Nodo("d"), collegamenti);
    System.out.println(g);
}