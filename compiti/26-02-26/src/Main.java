void main() throws IOException {
    List<Persona> persone = new ArrayList<>();

    persone.add(new Persona("Alessio", "Amato", 42));
    persone.add(new Bambino("Enis", "Gjnini", 3, LocalDate.of(2023, 2, 11)));
    persone.add(new Persona("Cicco", "Pasticcio", 55));
    persone.add(new Bambino("Ignazio", "Silvestri", 0, LocalDate.now()));
    persone.add(new Bambino("Giovanni", "Ferrai", 1, LocalDate.of(2024, 7, 18)));
    persone.add(new Persona("Cicco", "Pasticcio", 55));

//    //salvare la lista delle persone su file
//    try {
//        GestorePersoneFile.salvaPersone(persone, "persone.txt");
//    } catch (IOException e){
//        System.out.println("Errore di scrittura dei dati su file.");
//    }
//
//    persone.clear();
//
//    //ricaricaricare la lista dal file
//    try {
//        persone = GestorePersoneFile.caricaPersone("persone.txt");
//    } catch (IOException e){
//        System.out.println(e.getMessage());
//    }
//
//    // stampa della lista
//    for (Persona p: persone) {
//        System.out.println(p);
//    }

    GestorePersoneFile.serializza(persone, "personeSerializzato.ser");
    List<Persona> prova = GestorePersoneFile.deSerializza("personeSerializzato.ser");
    for (Persona p : prova) System.out.println(p);
}
