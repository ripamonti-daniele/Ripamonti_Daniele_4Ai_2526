import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class FinestraDomande extends Frame implements ActionListener {

    private final int FINESTRA_LARGHEZZA = 600;
    private final int FINESTRA_ALTEZZA = 300;
    private final int FONT_DIMENSIONE = 20;
    private final int IMMAGINE_LARGHEZZA = 77;
    private final int IMMAGINE_ALTEZZA = 78;

    private Label testoLabel;
    private Button siButton;
    private Button noButton;
    private ImagePanel immagineImp;
    private Panel panelCentro;

    private Albero albero;
    private Nodo nodoCorrente;

    private void creaFinestraGrafica(){

        Font font = new Font("Arial", Font.PLAIN, FONT_DIMENSIONE);
        setTitle("Indovina l'animale");
        setSize(FINESTRA_LARGHEZZA, FINESTRA_ALTEZZA);
        setLayout(new BorderLayout());

        // Label domanda
        testoLabel = new Label("", Label.CENTER);
        testoLabel.setFont(font);
        add(testoLabel, BorderLayout.NORTH);

        // Pulsanti
        siButton = new Button("Si");
        siButton.setFont(font);
        noButton = new Button("No");
        noButton.setFont(font);

        siButton.addActionListener(this);
        noButton.addActionListener(this);

        Panel panelBottoni = new Panel();
        panelBottoni.add(siButton);
        panelBottoni.add(noButton);

        add(panelBottoni, BorderLayout.SOUTH);

        // Pannello centrale per eventuale immagine
        panelCentro = new Panel();
        immagineImp = new ImagePanel(IMMAGINE_LARGHEZZA,IMMAGINE_ALTEZZA);
        panelCentro.add(immagineImp);
        add(panelCentro, BorderLayout.CENTER);

        // Chiusura finestra
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public FinestraDomande(Albero albero) {

        //TODO radice può essere null!
        this.albero = albero;
        nodoCorrente = albero.getRadice();      //il nodo corrente punta alla radice

        //crea la finestra grafica
        creaFinestraGrafica();

        //aggiorno il contenuto della finestra grafica
        aggiornaFinestra();
    }

    private void aggiornaFinestra(){

        //domanda
        if (!nodoCorrente.isAnimale()){
            testoLabel.setText(nodoCorrente.getTesto() + " (s/n)");
        }
        //tentativo di soluzione
        else{
            //mostro l'animale nella label
            testoLabel.setText("Penso che l'animale sia: " + nodoCorrente.getTesto());

            //se è prevista una immagine, la mostro
            if (nodoCorrente.getUrlImmagine() != null) {
                try {
                    Image immagine = ImageIO.read(nodoCorrente.getUrlImmagine());
                    immagineImp.setImage(immagine);
                }catch (IOException e){
                    //ignoro l'errore, non verrà mostrata nessuna immagine
                }
            }

            //disabilito i pulsanti Si / No (così non si può intraprendere alcuna ulteriore azione)
//            siButton.setEnabled(false);
//            noButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (nodoCorrente == null) return;

        if (nodoCorrente.isAnimale()) {  // nodo foglia
            if (e.getSource() == siButton) {
                JOptionPane.showMessageDialog(this, "Indovinato!");
                siButton.setEnabled(false);
                noButton.setEnabled(false);
            } else if (e.getSource() == noButton) {
                // apprendimento
                String nuovoAnimale = JOptionPane.showInputDialog(this,
                        "Quale animale stavi pensando?");
                if (nuovoAnimale == null || nuovoAnimale.isEmpty()) return;

                String nuovaDomanda = JOptionPane.showInputDialog(this,
                        "Scrivi una domanda che distingue " + nuovoAnimale + " da " + nodoCorrente.getTesto());
                if (nuovaDomanda == null || nuovaDomanda.isEmpty()) return;

                int risposta = JOptionPane.showConfirmDialog(this,
                        "La risposta alla domanda per " + nuovoAnimale + "?",
                        "Risposta", JOptionPane.YES_NO_OPTION);

                Nodo animaleNodo = new Nodo(nuovoAnimale, null);
                Nodo vecchioAnimaleNodo = new Nodo(nodoCorrente.getTesto(), nodoCorrente.getUrlImmagine());

                // nodo corrente diventa domanda
                nodoCorrente.setTesto(nuovaDomanda);
                nodoCorrente.setUrlImmagine(null);
                if (risposta == JOptionPane.YES_OPTION) {
                    nodoCorrente.si = animaleNodo;
                    nodoCorrente.no = vecchioAnimaleNodo;
                } else {
                    nodoCorrente.si = vecchioAnimaleNodo;
                    nodoCorrente.no = animaleNodo;
                }
                Serializzatore.serializza(albero);
                aggiornaFinestra();
            }
        } else if (nodoCorrente.isDomanda()) {  // nodo domanda
            if (e.getSource() == siButton) nodoCorrente = nodoCorrente.si;
            else if (e.getSource() == noButton) nodoCorrente = nodoCorrente.no;
            aggiornaFinestra();
        }
    }

}
