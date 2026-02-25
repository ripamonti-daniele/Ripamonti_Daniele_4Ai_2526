void main() {
    List<Persona> persone = new ArrayList<Persona>();

//    persone.add(new Persona("Alessio", "Amato", 42));
//    persone.add(new Persona("Carlo", "Bacuzzi", 18));
//    persone.add(new Persona("Cicco", "Pasticcio", 55));
//
//    //salvare la lista delle persone su file
//    try {
//        GestorePersoneFile.salvaPersone(persone);
//    } catch (IOException e){
//        System.out.println("Errore di scrittura dei dati su file.");
//    }
//
//    persone.clear();

    //ricaricaricare la lista dal file
    try {
        persone = GestorePersoneFile.caricaPersone();
    } catch (IOException e){
        System.out.println(e.getMessage());
    }

    // stampa della lista
    for (Persona p: persone) {
        System.out.println(p);
    }
}
