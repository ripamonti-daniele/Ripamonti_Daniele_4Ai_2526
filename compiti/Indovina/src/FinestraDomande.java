import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
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

    public FinestraDomande(Nodo radice) {

        //TODO radice può essere null!
        nodoCorrente = radice;      //il nodo corrente punta alla radice

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
            siButton.setEnabled(false);
            noButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //se l'utente preme si, mi sposto sul ramo dell'albero "si"
        if (e.getSource() == siButton) {
            nodoCorrente = nodoCorrente.si;
            //aggiorno la finestra grafica
            aggiornaFinestra();
        }

        //se l'utente preme no, mi sposto sul ramo dell'albero "no"
        if (e.getSource() == noButton) {
            nodoCorrente = nodoCorrente.no;
            //aggiorno la finestra grafica
            aggiornaFinestra();
        }

    }

}
