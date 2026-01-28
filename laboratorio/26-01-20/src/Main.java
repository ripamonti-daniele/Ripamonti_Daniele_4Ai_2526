//classe per creare automobili
//crud
//crea 5 macchine scegliendo il colore a caso
//filtra per tempo
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
    mostraErrore.setText(label);

    return valida;
}

int read(List<Automobile> magazzino, String targa, JLabel mostraErrore) {
    for(int i = 0; i < magazzino.size(); i++) {
        if (magazzino.get(i).getTarga().equals(targa)) {
            mostraErrore.setText("Targa trovata");
            return i;
        }
    }
    mostraErrore.setText("Targa non trovata");
    return -1;
}

boolean update(List<Automobile> magazzino, String targaOld, String targaNew, ButtonGroup colori, Map<String, Color> ottieniColore, int km, JLabel mostraErrore) {
    int ind = read(magazzino, targaOld, mostraErrore);
    if (ind == -1) return false;
    boolean valido = true;
    Color c = magazzino.get(ind).getColore();

    String label = "<html>";

    try {
        c = ottieniColore.get(colori.getSelection().getActionCommand());
        Automobile.controllaColore(c);
    }
    catch (IllegalArgumentException e1) {
        label += e1.getMessage() + "<br>";
        valido = false;
    }
    catch (Exception e2) {
        label += "Seleziona un colore<br>";
    }

    if (!targaNew.equals(targaOld)) {
        try {
            Automobile.controllaTarga(targaOld);
        }
        catch (Exception e) {
            label += e.getMessage() + "<br>";
            valido = false;
        }
    }

    if (valido) {
        try {
            magazzino.get(ind).aumentaKm(km);
        }
        catch (Exception e) {
            label += e.getMessage() + "<br>";
            mostraErrore.setText(label);
            return false;
        }
        mostraErrore.setText("Macchina modificata correttamente");
        magazzino.get(ind).cambiaTarga(targaNew);
        magazzino.get(ind).setColore(c);
    }

    mostraErrore.setText(label);
    return valido;
}

boolean delete(List<Automobile> magazzino, String targa, JLabel mostraErrore) {
    for(int i = 0; i < magazzino.size(); i++) {
        if (magazzino.get(i).getTarga().equals(targa)) {
            magazzino.remove(i);
            mostraErrore.setText("Macchina rimossa correttamente");
            return true;
        }
    }
    mostraErrore.setText("Targa non trovata");
    return false;
}

void inizializza(List<Automobile> magazzino) {
    magazzino.add(new Automobile("toyota", "corolla", "AA000AA", Color.gray, 20000, LocalDate.now()));
    magazzino.add(new Automobile("ford", "focus", "GH092AJ", Color.blue, 350000, LocalDate.of(2008, 12, 3)));
    magazzino.add(new Automobile("fiat", "punto", "FB237JK", Color.white, 120000, LocalDate.of(2016, 3, 14)));
}

void main() {
    List<Automobile> magazzino = new ArrayList<>();
    inizializza(magazzino);

    Map<String, Color> ottieniColore = new HashMap<>();
    ottieniColore.put("rosso", Color.red);
    ottieniColore.put("blu", Color.blue);
    ottieniColore.put("verde", Color.green);
    ottieniColore.put("viola", Color.magenta);
    ottieniColore.put("giallo", Color.yellow);

    Map<String, ImageIcon> ottieniImg = new HashMap<>();
    ottieniImg.put("rosso", new ImageIcon("img/macchina_rossa.png"));
    ottieniImg.put("blu", new ImageIcon("img/macchina_blu.png"));
    ottieniImg.put("verde", new ImageIcon("img/macchina_verde.png"));
    ottieniImg.put("viola", new ImageIcon("img/macchina_viola.png"));
    ottieniImg.put("giallo", new ImageIcon("img/macchina_gialla.png"));

    Map<Color, ImageIcon> colorToImageIcon = new HashMap<>();
    colorToImageIcon.put(Color.red, new ImageIcon("img/macchina_rossa.png"));
    colorToImageIcon.put(Color.blue, new ImageIcon("img/macchina_blu.png"));
    colorToImageIcon.put(Color.green, new ImageIcon("img/macchina_verde.png"));
    colorToImageIcon.put(Color.magenta, new ImageIcon("img/macchina_viola.png"));
    colorToImageIcon.put(Color.yellow, new ImageIcon("img/macchina_gialla.png"));

    JFrame frame = new JFrame("Automobile");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.setSize(new Dimension(1500, 900));
    frame.setLocationRelativeTo(null);

    JLabel labelTarga = new JLabel("Inserisci la targa");
    labelTarga.setBounds(0, 0, 250, 50);
    JTextArea scriviTarga = new JTextArea();
    scriviTarga.setBounds(0, 50, 250, 50);

    JLabel labelMarca = new JLabel("Inserisci la marca");
    labelMarca.setBounds(0, 110, 250, 50);
    JTextArea scriviMarca = new JTextArea();
    scriviMarca.setBounds(0, 160, 250, 50);

    JLabel labelModello = new JLabel("Inserisci il modello");
    labelModello.setBounds(0, 210, 250, 50);
    JTextArea scriviModello = new JTextArea();
    scriviModello.setBounds(0, 260, 250, 50);

    JLabel labelKm = new JLabel("Inserisci il kilometraggio");
    labelKm.setBounds(0, 310, 250, 50);
    SpinnerNumberModel modelKm = new SpinnerNumberModel(0, 0, null, 1000);
    JSpinner spinnerKm = new JSpinner(modelKm);
    spinnerKm.setBounds(0, 360, 250, 50);

    JLabel mostraErrore = new JLabel();
    mostraErrore.setVerticalAlignment(SwingConstants.TOP);
    mostraErrore.setBounds(0, 430, 1000, 200);

    JLabel labelImg = new JLabel(new ImageIcon("img/ruzza.jpg"));
    labelImg.setBounds(500, 400, 500, 250);

    frame.add(labelTarga);
    frame.add(labelMarca);
    frame.add(labelModello);
    frame.add(labelKm);
    frame.add(scriviTarga);
    frame.add(scriviMarca);
    frame.add(scriviModello);
    frame.add(spinnerKm);
    frame.add(mostraErrore);
    frame.add(labelImg);

    JLabel labelColore = new JLabel("Scegli il colore");
    labelColore.setBounds(550, 0, 250, 50);
    JRadioButton r1 = new JRadioButton("Rosso");
    JRadioButton r2 = new JRadioButton("Blu");
    JRadioButton r3 = new JRadioButton("Verde");
    JRadioButton r4 = new JRadioButton("Viola");
    JRadioButton r5 = new JRadioButton("Giallo");

    r1.setBounds(550, 40, 150, 50);
    r2.setBounds(550, 80, 150, 50);
    r3.setBounds(550, 120, 150, 50);
    r4.setBounds(550, 160, 150, 50);
    r5.setBounds(550, 200, 150, 50);
    r1.setActionCommand("rosso");
    r2.setActionCommand("blu");
    r3.setActionCommand("verde");
    r4.setActionCommand("viola");
    r5.setActionCommand("giallo");

    ButtonGroup group = new ButtonGroup();
    group.add(r1);
    group.add(r2);
    group.add(r3);
    group.add(r4);
    group.add(r5);

    ActionListener listener = e -> {
        String valore = e.getActionCommand();
        labelImg.setIcon(ottieniImg.get(valore));
    };
    r1.addActionListener(listener);
    r2.addActionListener(listener);
    r3.addActionListener(listener);
    r4.addActionListener(listener);
    r5.addActionListener(listener);

    frame.add(labelColore);
    frame.add(r1);
    frame.add(r2);
    frame.add(r3);
    frame.add(r4);
    frame.add(r5);

    JLabel labelGiorno = new JLabel("Inserisci il giorno di acquisto");
    labelGiorno.setBounds(275, 0, 250, 50);
    SpinnerNumberModel modelGiorno = new SpinnerNumberModel(LocalDate.now().getDayOfMonth(), 1, 31, 1);
    JSpinner spinnerGiorno = new JSpinner(modelGiorno);
    spinnerGiorno.setBounds(275, 50, 250, 50);

    JLabel labelMese = new JLabel("Inserisci il mese di acquisto");
    labelMese.setBounds(275, 110, 250, 50);
    SpinnerNumberModel modelMese = new SpinnerNumberModel(LocalDate.now().getMonthValue(), 1, 12, 1);
    JSpinner spinnerMese = new JSpinner(modelMese);
    spinnerMese.setBounds(275, 160, 250, 50);

    JLabel labelAnno = new JLabel("Inserisci l'anno di acquisto");
    labelAnno.setBounds(275, 210, 250, 50);
    SpinnerNumberModel modelAnno = new SpinnerNumberModel(LocalDate.now().getYear(), 1800, LocalDate.now().getYear(), 1);
    JSpinner spinnerAnno = new JSpinner(modelAnno);
    spinnerAnno.setBounds(275, 260, 250, 50);

    frame.add(labelGiorno);
    frame.add(labelMese);
    frame.add(labelAnno);
    frame.add(spinnerGiorno);
    frame.add(spinnerMese);
    frame.add(spinnerAnno);

    JLabel labelScelta = new JLabel("Scegli cosa fare");
    labelScelta.setBounds(700, 0, 250, 50);

    JRadioButton nuova = new JRadioButton("Nuova");
    nuova.setSelected(true);
    JRadioButton visualizza = new JRadioButton("Visualizza");
    JRadioButton modifica = new JRadioButton("Modifica");
    JRadioButton elimina = new JRadioButton("Elimina");

    nuova.setBounds(700, 40, 150, 50);
    visualizza.setBounds(700, 80, 150, 50);
    modifica.setBounds(700, 120, 150, 50);
    elimina.setBounds(700, 160, 150, 50);

    ButtonGroup opzioni = new ButtonGroup();
    opzioni.add(nuova);
    opzioni.add(visualizza);
    opzioni.add(modifica);
    opzioni.add(elimina);

    nuova.setActionCommand("nuova");
    visualizza.setActionCommand("visualizza");
    modifica.setActionCommand("modifica");
    elimina.setActionCommand("elimina");

    frame.add(labelScelta);
    frame.add(nuova);
    frame.add(visualizza);
    frame.add(modifica);
    frame.add(elimina);

    nuova.addActionListener(e -> {
        scriviMarca.setEnabled(true);
        scriviModello.setEnabled(true);
        spinnerKm.setEnabled(true);
        spinnerGiorno.setEnabled(true);
        spinnerMese.setEnabled(true);
        spinnerAnno.setEnabled(true);
        r1.setEnabled(true);
        r2.setEnabled(true);
        r3.setEnabled(true);
        r4.setEnabled(true);
        r5.setEnabled(true);
        scriviMarca.setText(null);
        scriviModello.setText(null);
        spinnerKm.setValue(0);
        spinnerGiorno.setValue(LocalDate.now().getDayOfMonth());
        spinnerMese.setValue(LocalDate.now().getMonthValue());
        spinnerAnno.setValue(LocalDate.now().getYear());
        group.clearSelection();
        labelImg.setIcon(null);
    });

    visualizza.addActionListener(e -> {
        scriviMarca.setEnabled(false);
        scriviModello.setEnabled(false);
        spinnerKm.setEnabled(false);
        spinnerGiorno.setEnabled(false);
        spinnerMese.setEnabled(false);
        spinnerAnno.setEnabled(false);
        r1.setEnabled(false);
        r2.setEnabled(false);
        r3.setEnabled(false);
        r4.setEnabled(false);
        r5.setEnabled(false);
        scriviMarca.setText(null);
        scriviModello.setText(null);
        spinnerKm.setValue(0);
        spinnerGiorno.setValue(LocalDate.now().getDayOfMonth());
        spinnerMese.setValue(LocalDate.now().getMonthValue());
        spinnerAnno.setValue(LocalDate.now().getYear());
        group.clearSelection();
        labelImg.setIcon(null);
    });

    modifica.addActionListener(e -> {
        scriviMarca.setEnabled(false);
        scriviModello.setEnabled(false);
        spinnerKm.setEnabled(true);
        spinnerGiorno.setEnabled(false);
        spinnerMese.setEnabled(false);
        spinnerAnno.setEnabled(false);
        r1.setEnabled(true);
        r2.setEnabled(true);
        r3.setEnabled(true);
        r4.setEnabled(true);
        r5.setEnabled(true);
        scriviMarca.setText(null);
        scriviModello.setText(null);
        spinnerKm.setValue(0);
        spinnerGiorno.setValue(LocalDate.now().getDayOfMonth());
        spinnerMese.setValue(LocalDate.now().getMonthValue());
        spinnerAnno.setValue(LocalDate.now().getYear());
        group.clearSelection();
        labelImg.setIcon(null);
    });

    elimina.addActionListener(e -> {
        scriviMarca.setEnabled(false);
        scriviModello.setEnabled(false);
        spinnerKm.setEnabled(false);
        spinnerGiorno.setEnabled(false);
        spinnerMese.setEnabled(false);
        spinnerAnno.setEnabled(false);
        r1.setEnabled(false);
        r2.setEnabled(false);
        r3.setEnabled(false);
        r4.setEnabled(false);
        r5.setEnabled(false);
        scriviMarca.setText(null);
        scriviModello.setText(null);
        spinnerKm.setValue(0);
        spinnerGiorno.setValue(LocalDate.now().getDayOfMonth());
        spinnerMese.setValue(LocalDate.now().getMonthValue());
        spinnerAnno.setValue(LocalDate.now().getYear());
        group.clearSelection();
        labelImg.setIcon(null);
    });

    JButton conferma = new JButton("Conferma");
    conferma.setBounds(550, 260, 100, 40);

    conferma.addActionListener(e -> {
        switch (opzioni.getSelection().getActionCommand()) {
            case "nuova":
                LocalDate data = null;
                Color colore = null;
                boolean errore = false;
                try {
                    String valore = group.getSelection().getActionCommand();
                    colore = ottieniColore.get(valore);
                    data = LocalDate.of((int) spinnerAnno.getValue(), (int) spinnerMese.getValue(), (int) spinnerGiorno.getValue());
                }
                catch (Exception exc) {
                    mostraErrore.setText("Compila i campi correttamente");
                    errore = true;
                }

                if (!errore) {
                    boolean corretto = create(magazzino, scriviMarca.getText(), scriviModello.getText(), scriviTarga.getText(), (int)spinnerKm.getValue(), colore, data, mostraErrore);
                    if (corretto) {
                        scriviTarga.setText(null);
                        scriviMarca.setText(null);
                        scriviModello.setText(null);
                        spinnerKm.setValue(0);
                        spinnerGiorno.setValue(LocalDate.now().getDayOfMonth());
                        spinnerMese.setValue(LocalDate.now().getMonthValue());
                        spinnerAnno.setValue(LocalDate.now().getYear());
                        group.clearSelection();
                        labelImg.setIcon(null);
                        mostraErrore.setText("Macchina aggiunta correttamente");
                    }
                }
                break;

            case "visualizza":
                int ind = read(magazzino, scriviTarga.getText().trim().toUpperCase(), mostraErrore);
                if (ind != -1) {
                    Automobile a = magazzino.get(ind);
                    scriviMarca.setText(a.getMarca());
                    scriviModello.setText(a.getModello());
                    spinnerKm.setValue(a.getKm());
                    spinnerGiorno.setValue(a.getDataDiFabbrica().getDayOfMonth());
                    spinnerMese.setValue(a.getDataDiFabbrica().getMonthValue());
                    spinnerAnno.setValue(a.getDataDiFabbrica().getYear());
                    labelImg.setIcon(colorToImageIcon.get(a.getColore()));
                }
                break;

            case "modifica":
                break;
        }

    });

    frame.add(conferma);

    frame.setVisible(true);
}
