import java.io.IO;

Brano creaBrano() {
    System.out.println("Inserisci il titolo");
    String titolo = IO.readln();
    System.out.println("Inserisci l'artista");
    String artista = IO.readln();
    System.out.println("Inserisci l'anno di pubblicazione");
    int anno = Integer.parseInt(IO.readln());
    System.out.println("Inserisci i dischi venduti");
    int vendite = Integer.parseInt(IO.readln());
    return new Brano(titolo, artista, anno, vendite);
}

void modificaBrano(List<Brano> brani) {
    visualizzaBrani(brani);
    System.out.println("Scegli l'indice del brano che vuoi modificare");
}

void visualizzaBrani(List<Brano> brani) {
    int ind = 1;
    for (Brano b : brani) {
        System.out.println(ind + ") " + b);
        ind++;
    }
}

void main(String[] args) {
    System.out.println("1: Crea brano\n2: Modifica brano\n3: Elimina brano\n 4: Visualizza i brani");
    int scelta = Integer.parseInt(IO.readln());
    List<Brano> brani = new ArrayList<>();

    switch (scelta) {
        case 1:
           brani.add(creaBrano());
           break;
        case 4:
            visualizzaBrani(brani);
            break;
    }
}