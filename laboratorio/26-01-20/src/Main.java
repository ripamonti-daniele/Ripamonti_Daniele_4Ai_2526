//classe per creare automobili
//crud
//crea 5 macchine scegliendo il colore a caso
//filtra per tempo
import javax.swing.*;
import java.awt.*;
import java.util.List;

int chiediNumero(String msg) {
    System.out.println(msg);
    int n = 0;
    boolean errore = true;
    while (errore) {
        errore = false;
        try {
            n = Integer.parseInt(IO.readln().trim());
        }
        catch (NumberFormatException e) {
            errore = true;
            System.out.println("Non hai inserito un numero intero");
        }
    }
    return n;
}

boolean create(List<Automobile> magazzino, String marca, String modello, String targa, int km, Color colore, LocalDate data, JLabel mostraErrore) {
//    <html>Prima riga<br>Seconda riga<br>Terza riga</html>
    String label = "<html>";
    boolean valida = true;

    try {
        Automobile.controllaTarga(targa);
    }
    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    try {
        Automobile.controllaMarca(marca);
    }
    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    try {
        Automobile.controllaModello(modello);
    }
    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    try {
        Automobile.controllaColore(colore);
    }

    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    try {
        Automobile.controllaKm(km);
    }
    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    try {
        Automobile.controllaDataDiFabbrica(data);
    }
    catch (IllegalArgumentException e) {
        valida = false;
        label += e.getMessage() + "<br>";
    }

    if (valida) {
        magazzino.add(new Automobile(marca, modello, targa, colore, km, data));
    }

    return valida;
}

void inizializza(List<Automobile> magazzino) {
    magazzino.add(new Automobile("toyota", "corolla", "AA000AA", Color.gray, 20000, LocalDate.now()));
    magazzino.add(new Automobile("ford", "focus", "GH092AJ", Color.blue, 350000, LocalDate.of(2008, 12, 3)));
    magazzino.add(new Automobile("fiat", "punto", "FB237JK", Color.white, 120000, LocalDate.of(2016, 3, 14)));
}

void main() {
    List<Automobile> magazzino = new ArrayList<>();
    inizializza(magazzino);

//    Map<Integer, Color> ottieniColore = new HashMap<>();
//    ottieniColore.put(1, Color.red);
//    ottieniColore.put(2, Color.blue);
//    ottieniColore.put(3, Color.green);
//    ottieniColore.put(4, Color.magenta);
//    ottieniColore.put(5, Color.yellow);

    JFrame frame = new JFrame("Automobile");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200);
    frame.setLayout(null);
    frame.setSize(new Dimension(1500, 900));
    frame.setLocationRelativeTo(null);

    JLabel labelTarga = new JLabel("Inserisci la targa");
    labelTarga.setBounds(0, 0, 250, 50);
    JTextArea scriviTarga = new JTextArea();
    scriviTarga.setBounds(0, 50, 250, 50);

    JLabel labelMarca = new JLabel("Inserisci la marca");
    labelMarca.setBounds(275, 0, 250, 50);
    JTextArea scriviMarca = new JTextArea();
    scriviMarca.setBounds(275, 50, 250, 50);

    JLabel labelModello = new JLabel("Inserisci il modello");
    labelModello.setBounds(550, 0, 250, 50);
    JTextArea scriviModello = new JTextArea();
    scriviModello.setBounds(550, 50, 250, 50);

    JLabel labelKm = new JLabel("Inserisci il kilometraggio (non obbligatorio)");
    labelKm.setBounds(825, 0, 250, 50);
    SpinnerNumberModel modelKm = new SpinnerNumberModel(0, 0, null, 1000);
    JSpinner spinnerKm = new JSpinner(modelKm);
    spinnerKm.setBounds(825, 50, 250, 50);

    JLabel mostraErrore = new JLabel();
    mostraErrore.setVerticalAlignment(SwingConstants.TOP);
    mostraErrore.setBounds(0, 430, 1000, 200);

    frame.add(labelTarga);
    frame.add(labelMarca);
    frame.add(labelModello);
    frame.add(labelKm);
    frame.add(scriviTarga);
    frame.add(scriviMarca);
    frame.add(scriviModello);
    frame.add(spinnerKm);
    frame.add(mostraErrore);

    JLabel labelColore = new JLabel("Scegli il colore");
    labelColore.setBounds(275, 110, 150, 50);
    JRadioButton r1 = new JRadioButton("Rosso");
    JRadioButton r2 = new JRadioButton("Blu");
    JRadioButton r3 = new JRadioButton("Verde");
    JRadioButton r4 = new JRadioButton("Viola");
    JRadioButton r5 = new JRadioButton("Giallo");

    r1.setBounds(275, 160, 100, 20);
    r2.setBounds(275, 190, 100, 20);
    r3.setBounds(275, 220, 100, 20);
    r4.setBounds(275, 250, 100, 20);
    r5.setBounds(275, 280, 100, 20);
    r1.putClientProperty("color", Color.red);
    r2.putClientProperty("color", Color.blue);
    r3.putClientProperty("color", Color.green);
    r4.putClientProperty("color", Color.magenta);
    r5.putClientProperty("color", Color.yellow);

    ButtonGroup group = new ButtonGroup();
    group.add(r1);
    group.add(r2);
    group.add(r3);
    group.add(r4);
    group.add(r5);

    frame.add(labelColore);
    frame.add(r1);
    frame.add(r2);
    frame.add(r3);
    frame.add(r4);
    frame.add(r5);

    JLabel labelGiorno = new JLabel("Inserisci il giorno di acquisto");
    labelGiorno.setBounds(0, 110, 250, 50);
    SpinnerNumberModel modelGiorno = new SpinnerNumberModel(1, 1, 31, 1);
    JSpinner spinnerGiorno = new JSpinner(modelGiorno);
    spinnerGiorno.setBounds(0, 160, 250, 50);

    JLabel labelMese = new JLabel("Inserisci il mese di acquisto");
    labelMese.setBounds(0, 210, 250, 50);
    SpinnerNumberModel modelMese = new SpinnerNumberModel(1, 1, 12, 1);
    JSpinner spinnerMese = new JSpinner(modelMese);
    spinnerMese.setBounds(0, 260, 250, 50);

    JLabel labelAnno = new JLabel("Inserisci l'anno di acquisto");
    labelAnno.setBounds(0, 310, 250, 50);
    SpinnerNumberModel modelAnno = new SpinnerNumberModel(2000, 1800, LocalDate.now().getYear(), 1);
    JSpinner spinnerAnno = new JSpinner(modelAnno);
    spinnerAnno.setBounds(0, 360, 250, 50);

    frame.add(labelGiorno);
    frame.add(labelMese);
    frame.add(labelAnno);
    frame.add(spinnerGiorno);
    frame.add(spinnerMese);
    frame.add(spinnerAnno);

    JButton conferma = new JButton("Conferma");
    conferma.setBounds(275, 320, 100, 40);

    conferma.addActionListener(e -> {
        LocalDate data = null;
        Color colore = null;
        boolean errore = false;
        try {
            //da sistemare la scelta del colore
            String valore = group.getSelection().getActionCommand();
            System.out.println("x");
            System.out.println("Hai scelto: " + valore);
            data = LocalDate.of((int) spinnerAnno.getValue(), (int) spinnerMese.getValue(), (int) spinnerGiorno.getValue());
            System.out.println("a");
            JRadioButton selected = (JRadioButton) group.getSelection().getSelectedObjects()[0];
            System.out.println("b");
            colore = (Color) selected.getClientProperty("color");
            System.out.println("c");
        }
        catch (Exception exc) {
            mostraErrore.setText(exc.getMessage());
            errore = true;
        }

        if (!errore) {
            create(magazzino, scriviMarca.getText(), scriviModello.getText(), scriviTarga.getText(), (int)spinnerKm.getValue(), colore, data, mostraErrore);
        }
    });

    frame.add(conferma);

    frame.setVisible(true);
}
