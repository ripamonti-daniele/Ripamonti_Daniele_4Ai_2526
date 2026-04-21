void main() {
    Rete rete = new Rete(new int[]{192, 168, 1, 1}, "router a");
    rete.aggiungiHost(new int[]{192, 168, 1, 2}, "router b", new int[]{192, 168, 1, 1});
    rete.aggiungiHost(new int[]{192, 168, 1, 3}, "router c", new int[]{192, 168, 1, 1});
    rete.aggiungiHost(new int[]{192, 168, 1, 4}, "router d", new int[]{192, 168, 1, 2});
    rete.aggiungiHost(new int[]{192, 168, 1, 5}, "router e", new int[]{192, 168, 1, 4});
    System.out.println(rete);
}