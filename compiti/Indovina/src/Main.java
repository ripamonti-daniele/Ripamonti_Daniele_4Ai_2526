void serializza(Albero a) {
    try {
        FileOutputStream file = new FileOutputStream("fileAlbero.ser");
        ObjectOutputStream output = new ObjectOutputStream(file);

        output.writeObject(a);

        output.close();
        file.close();
    }
    catch (Exception e) {
        System.out.println("Errore nella scrittura del file fileAlbero.ser");
    }
}

Albero deSerializza() {
    Albero a = null;
    String nomeFile = "fileAlbero.ser";
    for (int i = 0; i < 2; i++) {
        if (i == 1) nomeFile = "fileAlberoOriginale.ser";
        try {
            FileInputStream file = new FileInputStream(nomeFile);
            ObjectInputStream input = new ObjectInputStream(file);

            a = (Albero) input.readObject();

            input.close();
            file.close();

            if (i == 0) break;
            else System.out.println("Lettura di " + nomeFile + " riuscita");
        }
        catch (Exception e) {
            if (i == 0) System.out.println("Errore nella lettura del file " + nomeFile + "\nTentativo di lettura da fileAlberoOriginale.ser...");
            else System.out.println("Lettura di " + nomeFile + " non riuscita");
        }
    }
    return a;
}

void main() {
    Albero alberoBase = deSerializza();
    FinestraDomande finestra = new FinestraDomande(alberoBase.getRadice());

  }
