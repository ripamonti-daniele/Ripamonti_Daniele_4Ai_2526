import scacchiera_pedine.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class GestoreGrafico {
    private final Casella[][] casellePanel;
    private final Casella[] casellePromozione;
    private final Scacchiera scacchiera;
    private boolean partitaInCorso;
    private int mossaMostrata;
    private boolean rotazioneScacchiera;
    private boolean promozione;
    private int[] posPromozione;
    public final int lunghezzaScacchiera;
    public final int lunghezzaCasella;
    public static final int DIMENSIONE = Scacchiera.DIMENSIONE;
    private ImageIcon[] immagini;
    private static final String SEP = Scacchiera.getSEP();
    private static final Map<Integer, String> numeroToLettera = Map.of(1, "A", 2, "B", 3, "C", 4, "D", 5, "E", 6, "F", 7, "G", 8, "H");;
    private long ultimoClic;
    private static final long SOGLIA_MS = 250;
    private boolean aggiuntoASchermo;
    private Bot bot;

    private final JPanel panelInfo;
    private final JButton btnGioca;
    private final JButton btnRotazioneScacchiera;
    private final JLabel labelVittoria;
    private final JTextArea nomeBianco;
    private final JTextArea nomeNero;
    private final JButton btnBot;
    private final JButton btnTimer;
    private final TimerGrafico timerBianco;
    private final TimerGrafico timerNero;
    private final JLabel materialeBianco;
    private final JLabel materialeNero;
    private final BottoneOpzioni[] btnOpzioni;

    //attributi per la gestione di Casella
    private Color caselleChiare;
    private Color caselleScure;
    private final List<String> idUtilizzati;
    private String casellaSelezionata;
    private String casellaPosIniziale;
    private String casellaPosFinale;
    private boolean scacchieraGirata;
    private boolean gestisciGrafica;
    private boolean infoMossa;
    private String idEnPassant;

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo, Color caselleChiare, Color caselleScure) {
        if (scacchiera == null) throw new IllegalArgumentException("La scacchiera non può essere null");
        this.scacchiera = scacchiera;
        this.lunghezzaScacchiera = lunghezzaScacchiera;
        lunghezzaCasella = lunghezzaScacchiera / 8;
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        casellePromozione = new Casella[4];
        partitaInCorso = false;
        mossaMostrata = 0;
        rotazioneScacchiera = true;
        promozione = false;
        posPromozione = null;
        ultimoClic = 0;
        aggiuntoASchermo = false;
        bot = null;
        idUtilizzati = new ArrayList<>();
        casellaSelezionata = null;
        casellaPosIniziale = null;
        casellaPosFinale = null;
        scacchieraGirata = false;
        gestisciGrafica = false;
        infoMossa = true;
        idEnPassant = null;
        setImmagini(immagini);
        inizializzaCaselle(caselleChiare, caselleScure);
        aggiornaScacchiera(scacchiera.getStringaScacchiera());

        //inizializzazione parte grafica gestione utente
        panelInfo = new JPanel();
        labelVittoria = new JLabelCustom(null, new Color(180,130,20), new Color(140,95,10));
        nomeBianco = new JTextAreaCustom("Giocatore 1", 0, lunghezzaScacchiera - lunghezzaCasella / 2,  lunghezzaCasella * 2 + lunghezzaCasella / 2 + lunghezzaCasella / 15, lunghezzaCasella / 2);
        nomeNero = new JTextAreaCustom("Giocatore 2", 0, 0,  lunghezzaCasella * 2 + lunghezzaCasella / 2 + lunghezzaCasella / 15, lunghezzaScacchiera / 16);
        timerBianco = new TimerGrafico(0, 10, 0, 0, Color.white, Color.black);
        timerNero = new TimerGrafico(0, 10, 0, 0, Color.black, Color.white);
        materialeBianco = new JLabelCustom(null, Color.white);
        materialeNero = new JLabelCustom(null, Color.black);
        btnTimer = new JButtonCustom("<html><div style='text-align:center;'>Imposta timer</div></html>", 0, lunghezzaCasella * 2 - lunghezzaCasella / 15 - lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(190, 30, 30), new Color(150, 15, 15), new Color(218, 55, 55), new Color(170, 25, 25), new Color(108, 8, 8), Color.white);
        btnGioca = new JButtonCustom("<html><div style='text-align:center;'>Gioca in persona</div></html>", 0, lunghezzaCasella * 3 - lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(30, 100, 210), new Color(15, 60, 160), new Color(50, 130, 240), new Color(25, 90, 190), new Color(10, 40, 120), Color.white);
        btnBot = new JButtonCustom("<html><div style='text-align:center;'>Gioca contro<br>un bot</div></html>", 0, lunghezzaCasella * 4 + lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(20, 110, 45), new Color(10, 75, 28), new Color(35, 140, 65), new Color(15, 95, 38), new Color(5, 50, 15), Color.white);
        btnRotazioneScacchiera = new JButtonCustom("<html><div style='text-align:center;'>Ruota<br>On</div></html>", 0, lunghezzaCasella * 5 + lunghezzaCasella / 15 + lunghezzaCasella / 30, lunghezzaCasella * 2, lunghezzaCasella, new Color(60, 60, 70), new Color(35, 35, 42), new Color(80, 80, 95), new Color(55, 55, 68), new Color(20, 20, 26), Color.white);
        btnOpzioni = new BottoneOpzioni[6];
        for (int i = 0; i < btnOpzioni.length; i++) {
            if (i < 4) btnOpzioni[i] = new BottoneOpzioni(i + 1, lunghezzaScacchiera - lunghezzaCasella / 2 * (4 - i) - lunghezzaCasella / 15 * (4 - i), lunghezzaScacchiera + lunghezzaCasella / 8, lunghezzaCasella / 2);
            else btnOpzioni[i] = new BottoneOpzioni(i + 1, lunghezzaScacchiera - lunghezzaCasella * 2 - lunghezzaCasella / 15 * i - lunghezzaCasella / 2 * (i - 3), lunghezzaScacchiera + lunghezzaCasella / 8, lunghezzaCasella / 2);
        }

        //setBounds
        panelInfo.setBounds(lunghezzaScacchiera + lunghezzaCasella * 5 / 4, 0, lunghezzaCasella * 6, lunghezzaScacchiera);
        labelVittoria.setBounds(lunghezzaCasella * 2 + lunghezzaCasella / 15, lunghezzaCasella * 4 - lunghezzaCasella / 4 * 3, lunghezzaCasella * 5 / 2, lunghezzaCasella * 3 / 2);
        timerBianco.setBounds(0, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        timerNero.setBounds(0, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        materialeBianco.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);
        materialeNero.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);

        //modifiche grafiche
        panelInfo.setLayout(null);
        if (sfondo != null) {
            panelInfo.setOpaque(true);
            panelInfo.setBackground(sfondo);
        }

        Font font = new Font("Segoe UI", Font.BOLD, lunghezzaCasella / 4);
        labelVittoria.setOpaque(false);
        labelVittoria.setForeground(Color.white);
        labelVittoria.setFont(font);

        timerBianco.setFont(font);
        timerNero.setFont(font);
        setListenerTimer(timerBianco);
        setListenerTimer(timerNero);

        Font fontPiccolo = new Font("Segoe UI", Font.BOLD, lunghezzaScacchiera / 60);
        materialeBianco.setForeground(Color.black);
        materialeBianco.setFont(fontPiccolo);
        materialeNero.setForeground(Color.white);
        materialeNero.setFont(fontPiccolo);
        aggiornaLabelMateriale();

        setListenerSpostamenti();
        setListenerRotazioneManuale();
        setListenerAudio();
        aggiornaBtnSpostamento();

        panelInfo.add(btnGioca);
        panelInfo.add(btnRotazioneScacchiera);
        panelInfo.add(nomeBianco);
        panelInfo.add(nomeNero);
        panelInfo.add(btnBot);
        panelInfo.add(labelVittoria);
        panelInfo.add(timerBianco);
        panelInfo.add(timerNero);
        panelInfo.add(materialeBianco);
        panelInfo.add(materialeNero);
        panelInfo.add(btnTimer);

        //listener gestione utente
        btnGioca.addActionListener(_ -> {
            SuoniScacchi.menu();
            if (bot != null) {
                rotazioneScacchiera = true;
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>On</div></html>");
            }
            bot = null;
            gioca();
        });

        btnRotazioneScacchiera.addActionListener(_ -> {
            SuoniScacchi.menu();
            rotazioneScacchiera = !rotazioneScacchiera;
            if (rotazioneScacchiera) {
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>On</div></html>");
                if (scacchiera.getStatoPartita() == StatoPartita.IN_CORSO) ruotaScacchiera(scacchiera.getTurno(), true);
            }
            else {
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>Off</div></html>");
                if (bot != null) {
                    if (bot.getColore().equals(Color.white)) ruotaScacchiera(Color.black, true);
                    else if (scacchiera.getStatoPartita() == StatoPartita.IN_CORSO) ruotaScacchiera(Color.white, true);
                }
            }
        });

        btnTimer.addActionListener(_ -> {
            SuoniScacchi.menu();
            new DialogTimer(timerBianco, timerNero, lunghezzaCasella);
        });

        btnBot.addActionListener(_ -> {
            SuoniScacchi.menu();
            DialogBot d = new DialogBot(lunghezzaCasella);
            if (d.isConfermato()) {
                int difficolta = d.getDifficolta();
                Color colore = d.getColoreBot();
                boolean avanzata = d.isRicercaAvanzata();
                bot = new Bot(scacchiera, colore, difficolta, avanzata);
                if (colore.equals(Color.white)) {
                    if (nomeBianco.getText().equals("Giocatore 1") || nomeBianco.getText().startsWith("Bot")) nomeBianco.setText("Bot " + DialogBot.getStringaDifficolta());
                    if (nomeNero.getText().startsWith("Bot")) nomeNero.setText("Giocatore 2");
                }
                else {
                    if (nomeNero.getText().equals("Giocatore 2") || nomeNero.getText().startsWith("Bot")) nomeNero.setText("Bot " + DialogBot.getStringaDifficolta());
                    if (nomeBianco.getText().startsWith("Bot")) nomeBianco.setText("Giocatore 1");
                }
                rotazioneScacchiera = false;
                btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>Off</div></html>");
                gioca();
            }
        });
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo, Color caselleChiare, Color caselleScure) {
        int lunghezzaCasella = lunghezzaScacchiera / 8;
        this(scacchiera, lunghezzaScacchiera, new ImageIcon[]{
                IconaPedina.PEDONE_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.ALFIERE_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.CAVALLO_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.TORRE_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.REGINA_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.RE_WHITE.getImageIcon(lunghezzaCasella),
                IconaPedina.PEDONE_BLACK.getImageIcon(lunghezzaCasella),
                IconaPedina.ALFIERE_BLACK.getImageIcon(lunghezzaCasella),
                IconaPedina.CAVALLO_BLACK.getImageIcon(lunghezzaCasella),
                IconaPedina.TORRE_BLACK.getImageIcon(lunghezzaCasella),
                IconaPedina.REGINA_BLACK.getImageIcon(lunghezzaCasella),
                IconaPedina.RE_BLACK.getImageIcon(lunghezzaCasella)
        }, sfondo, caselleChiare, caselleScure);
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini) {
        this(scacchiera, lunghezzaScacchiera, immagini, null, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera) {
        this(scacchiera, lunghezzaScacchiera, null, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon[] immagini, Color sfondo) {
        this(scacchiera, lunghezzaScacchiera, immagini, sfondo, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo) {
        this(scacchiera, lunghezzaScacchiera, sfondo, new Color(245, 245, 245), new Color(70, 70, 70));
    }

    private void inizializzaCaselle(Color caselleChiare, Color caselleScure) {
        if (!(caselleChiare == null && caselleScure == null)) {
            if (caselleChiare == null || caselleScure == null) throw new IllegalArgumentException("I colori delle caselle devono essere entrambi assegnati o entrambi null");
            else if (caselleChiare.equals(caselleScure)) throw new IllegalStateException("Il colore di casellePari non può essere lo stesso di caselleDispari");
            else {
                this.caselleChiare = caselleChiare;
                this.caselleScure = caselleScure;
            }
        }

        gestisciGrafica = true;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                boolean pari =  ((j + i) % 2 == 0);
                casellePanel[i][j] = new Casella(pari, lunghezzaCasella, numeroToLettera.get(j + 1) + (DIMENSIONE - i));
                casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                setListener(i, j);
            }
        }
        for (int i = 0; i < 4; i++) {
            casellePromozione[i] = new Casella(null, lunghezzaCasella, "PROMOZIONE");
            int offset = lunghezzaCasella + lunghezzaCasella / 2 + i * (lunghezzaCasella + lunghezzaCasella / 3);
            casellePromozione[i].setBounds(lunghezzaScacchiera + lunghezzaCasella / 8, offset, lunghezzaCasella, lunghezzaCasella);
            casellePromozione[i].setOpaque(false);
            setListenerPromozione(i);
        }
        gestisciGrafica = false;
    }

    private void setImmagini(ImageIcon[] immagini) {
        if (immagini == null) throw new IllegalArgumentException("Immagini non può essere un parametro null");
        if (immagini.length != 12) throw new IllegalArgumentException("Le immagini devono essere obbligatoriamente 12");
        for (int i = 0; i < immagini.length - 1; i++) {
            if (immagini[i] == null || i == immagini.length - 2 && immagini[i + 1] == null) throw new IllegalArgumentException("Le immagini non possono essere null");
            for (int j = i + 1; j < immagini.length; j++) if (immagini[i].equals(immagini[j])) throw new IllegalArgumentException("Due tipi di pedina diversi non possono avere la stessa immagine");
        }
        for (int i = 0; i < immagini.length; i++) if (immagini[i].getIconWidth() != lunghezzaCasella || immagini[i].getIconHeight() != lunghezzaCasella) immagini[i] = new ImageIcon(immagini[i].getImage().getScaledInstance(lunghezzaCasella, lunghezzaCasella, Image.SCALE_SMOOTH));
        this.immagini = immagini;
    }

    private void gioca() {
        scacchiera.reset();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());
        if (bot != null) {
            if (Color.white.equals(bot.getColore())) ruotaScacchiera(Color.black, true);
            else if (Color.black.equals(bot.getColore())) ruotaScacchiera(Color.white, true);
        }
        else ruotaScacchiera(scacchiera.getTurno(), true);
        casellaPosIniziale = null;
        casellaPosFinale = null;
        disegna();
        labelVittoria.setText(null);
        btnGioca.setEnabled(false);
        partitaInCorso = true;
        mossaMostrata = 0;
        aggiornaBtnSpostamento();
        nomeBianco.setText(nomeBianco.getText().trim());
        nomeNero.setText(nomeNero.getText().trim());
        if (nomeBianco.getText().isEmpty()) {
            if (bot != null && bot.getColore().equals(Color.white)) nomeBianco.setText("Bot " + DialogBot.getStringaDifficolta());
            else nomeBianco.setText("Giocatore 1");
        }
        if (nomeNero.getText().isEmpty()) {
            if (bot != null && bot.getColore().equals(Color.black)) nomeNero.setText("Bot " + DialogBot.getStringaDifficolta());
            nomeNero.setText("Giocatore 2");
        }
        if (nomeBianco.getText().equals(nomeNero.getText())) {
            char c = 'N';
            if (nomeNero.getText().length() == 15) {
                if (nomeBianco.getText().charAt(14) == c) c = 'n';
                nomeNero.setText(nomeNero.getText().substring(0, 14) + c);
            }
            else nomeNero.setText(nomeNero.getText() + c);
        }
        nomeBianco.setEditable(false);
        nomeNero.setEditable(false);
        btnTimer.setEnabled(false);
        timerBianco.reset();
        timerNero.reset();
        timerBianco.start();
        btnBot.setEnabled(false);
        aggiornaLabelMateriale();

        if (bot != null && Color.white.equals(bot.getColore())) mossaBot();
    }

    private void finePartita() {
        casellaSelezionata = null;
        resetMosseValide();
        disegna();
        btnGioca.setEnabled(true);
        nomeBianco.setEditable(true);
        nomeNero.setEditable(true);
        partitaInCorso = false;
        timerBianco.pause(true);
        timerNero.pause(true);
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        btnTimer.setEnabled(true);
        btnBot.setEnabled(true);
        if (rotazioneScacchiera) btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>On</div></html>");
    }

    private void aggiornaScacchiera(String s) {
        String[] righe = s.split("\n");
        for (int i = 0; i < righe.length; i++) {
            String[] pedine = righe[i].split(SEP);
            for (int j = 0; j < pedine.length; j++) {
                Casella c = casellePanel[i][j];
                switch (pedine[j]) {
                    case "PB" -> c.setImg(immagini[0]);
                    case "AB" -> c.setImg(immagini[1]);
                    case "CB" -> c.setImg(immagini[2]);
                    case "TB" -> c.setImg(immagini[3]);
                    case "QB" -> c.setImg(immagini[4]);
                    case "RB" -> c.setImg(immagini[5]);
                    case "PN" -> c.setImg(immagini[6]);
                    case "AN" -> c.setImg(immagini[7]);
                    case "CN" -> c.setImg(immagini[8]);
                    case "TN" -> c.setImg(immagini[9]);
                    case "QN" -> c.setImg(immagini[10]);
                    case "RN" -> c.setImg(immagini[11]);
                    default -> casellePanel[i][j].rimuoviImg();
                }
            }
        }
    }

    private void setListener(int y, int x) {
        casellePanel[y][x].setListener(() -> {
            //se sto guardano una vecchia mossa e clicco due volte la scacchiera torno alla mossa corrente
            long ora = System.currentTimeMillis();
            if (ora - ultimoClic <= SOGLIA_MS && mossaMostrata != scacchiera.getMosse() && !promozione) {
                ultimoClic = ora;
                mossaMostrata = scacchiera.getMosse();
                aggiornaBtnSpostamento();
                aggiornaScacchiera(scacchiera.getStringaScacchiera());
                if (partitaInCorso && scacchiera.getTurno().equals(Color.white) && timerBianco.isPaused()) timerBianco.start();
                else if (partitaInCorso && scacchiera.getTurno().equals(Color.black) && timerNero.isPaused()) timerNero.start();
                aggiornaLabelMateriale();
                disegna();
                return;
            }
            ultimoClic = ora;

            if (bot != null && bot.getColore().equals(scacchiera.getTurno())) return;

            if (!promozione && partitaInCorso && mossaMostrata == scacchiera.getMosse()) {
                //se clicco su una casella già selezionata la deseleziono
                resetMosseValide();
                if (casellePanel[y][x].getId().equals(casellaSelezionata)) {
                    casellaSelezionata = null;
                    scacchiera.deSelezionaPedina();
                    disegna();
                    return;
                }

                int[] pos = new int[]{y, x};
                Pedina p = scacchiera.getPedina(pos);
                String idCasellaSelOld = casellaSelezionata;

                if (p != null && p.getColore().equals(scacchiera.getTurno())) casellaSelezionata = casellePanel[y][x].getId();
                else casellaSelezionata = null;

                //se la casella selezionata non è null allora seleziona la pedina; se è null prova a spostarla e se non riesce seleziona la pedina dove si intendeva spostare quella selezionata precedentemente
                if (scacchiera.getCasellaSelezionata() == null || !scacchiera.muoviPedina(pos)) {
                    List<int[]> mosseValide = scacchiera.selezionaPedina(pos);
                    idEnPassant = null;
                    if (mosseValide != null) {
                        //se c'è un en passant tre le mosse valide viene indicato che il pedone avversario viene mangiato
                        if (scacchiera.getPedinaSelezionata() instanceof Pedone) {
                            for (int[] mossa : mosseValide) {
                                if (mossa[1] != scacchiera.getCasellaSelezionata()[1] && scacchiera.getPedina(mossa) == null) {
                                    idEnPassant = casellePanel[mossa[0]][mossa[1]].getId();
                                    break;
                                }
                            }
                        }
                        mostraMosseValide(mosseValide);
                    }
                }

                //promozione pedone
                else if (scacchiera.promozioneInSospeso() != null) {
                    casellaPosFinale = casellePanel[y][x].getId();
                    casellaPosIniziale = idCasellaSelOld;
                    promozione = true;
                    posPromozione = pos;
                    setImgCasellePromozione(scacchiera.getPedina(pos).getColore());
                    aggiornaBtnSpostamento();
                }

                //mossa normale
                else {
                    casellaPosFinale = casellePanel[y][x].getId();
                    casellaPosIniziale = idCasellaSelOld;
                    suonoMossa(scacchiera.getTurno());
                    aggiornaInfoScacchiera();
                    disegna();
                    mossaBot();
                    return;
                }

                disegna();
            }
        });
    }

    private void aggiornaInfoScacchiera() {
        timerBianco.invertiStato();
        timerNero.invertiStato();
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        mossaMostrata = scacchiera.getMosse();
        aggiornaBtnSpostamento();
        aggiornaLabelMateriale();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());
        switch (scacchiera.getStatoPartita()) {
            case StatoPartita.IN_CORSO -> ruotaScacchiera(scacchiera.getTurno());
            case StatoPartita.VITTORIA_BIANCO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeBianco.getText() + " (bianco)</div></html>");
                finePartita();
            }
            case StatoPartita.VITTORIA_NERO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeNero.getText() + " (nero)</div></html>");
                finePartita();
            }
            case StatoPartita.STALLO -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Stallo:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.MATERIALE_INSUFFICIENTE -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Materiale insufficiente:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PAREGGIO_MOSSE_NEUTRE -> {
                labelVittoria.setText("<html><div style='text-align:center;'>75 mosse neutre:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PAREGGIO_RIPETIZIONI -> {
                labelVittoria.setText("<html><div style='text-align:center;'>5 posizioni ripetute:<br>Pareggio</div></html>");
                finePartita();
            }
            case StatoPartita.PROMOZIONE_IN_SOSPESO -> {
                scacchiera.promuoviPedone(scacchiera.promozioneInSospeso(), 1); //la ripetizione di questa riga comporterà il cambio di statoPartita, fermando la ricorsione
                aggiornaInfoScacchiera();
            }
        }
    }

    private void setListenerPromozione(int i) {
        casellePromozione[i].setListener(() -> {
            if (promozione && mossaMostrata == scacchiera.getMosse()) {
                scacchiera.promuoviPedone(posPromozione, i + 1);
                promozione = false;
                posPromozione = null;
                for (Casella c : casellePromozione) c.rimuoviImg();
                suonoMossa(scacchiera.getTurno());
                aggiornaInfoScacchiera();
                disegna();
                mossaBot();
            }
        });
    }

    private void suonoMossa(Color coloreAvversario) {
        if (!SuoniScacchi.audio) return;
        StatoPartita sp = scacchiera.getStatoPartita();
        if (sp == StatoPartita.VITTORIA_BIANCO || sp == StatoPartita.VITTORIA_NERO) SuoniScacchi.vittoria();
        else if (sp == StatoPartita.STALLO || sp == StatoPartita.PAREGGIO_MOSSE_NEUTRE || sp == StatoPartita.PAREGGIO_RIPETIZIONI || sp == StatoPartita.MATERIALE_INSUFFICIENTE) SuoniScacchi.finePartita();
        else if (scacchiera.isScaccoRe(coloreAvversario)) SuoniScacchi.scacco();
        else if (scacchiera.getMaterialeMossa(coloreAvversario, scacchiera.getMosse() - 1) > scacchiera.getMateriale(coloreAvversario)) SuoniScacchi.mangiata();
        else SuoniScacchi.spostamento();
    }

    private void mossaBot() {
        if (bot == null) return;

        new SwingWorker<int[][], Void>() {
            @Override
            protected int[][] doInBackground() throws Exception {
                Thread.sleep(100);
                return bot.muovi();
            }

            @Override
            protected void done() {
                if (bot.getColore().equals(Color.white) && timerBianco.isTempoScaduto() || bot.getColore().equals(Color.black) && timerNero.isTempoScaduto()) return;
                try {
                    int[][] m = get();
                    if (m != null) {
                        casellaPosIniziale = numeroToLettera.get(m[0][1] + 1) + (DIMENSIONE - m[0][0]);
                        casellaPosFinale = numeroToLettera.get(m[1][1] + 1) + (DIMENSIONE - m[1][0]);
                    }
                    else {
                        labelVittoria.setText("<html><div style='text-align:center;'>Partita interrotta:<br>Il bot non ha trovato mosse</div></html>");
                        SuoniScacchi.finePartita();
                        finePartita();
                    }
                }
                catch (Exception e) {
                    labelVittoria.setText("<html><div style='text-align:center;'>Partita interrotta:<br>Il bot non ha trovato mosse</div></html>");
                    SuoniScacchi.finePartita();
                    finePartita();
                }
                suonoMossa(bot.getColoreAvversario());
                SuoniScacchi.spostamento();
                aggiornaInfoScacchiera();
                disegna();
            }
        }.execute();
    }

    private void setListenerTimer(TimerGrafico t) {
        t.addPropertyChangeListener("text", _ -> {
            if (!t.isOff() && partitaInCorso && t.getOre() == 0 && t.getMinuti() == 0 && !t.isPaused() && (t.getMinutiDefault() > 0 || t.getSecondi() < 10)) {
                if (SuoniScacchi.audio && (t.getSecondi() == 59 || t.getSecondi() == 9 && t.getMinutiDefault() == 0)) SuoniScacchi.tempo();
                if (t.getSecondi() % 2 != 0) t.setForeground(Color.red);
                else t.setForeground(t.getTextColor());
            }

            if (t.isTempoScaduto()) {
                if (t == timerBianco && scacchiera.materialeInsufficiente(Color.black)) labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto, pareggio:<br>Materiale nero insufficiente</div></html>");
                else if (t == timerNero && scacchiera.materialeInsufficiente(Color.white)) labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto, pareggio:<br>Materiale bianco insufficiente</div></html>");
                else {
                    String testo = nomeBianco.getText() + " (bianco)";
                    if (t == timerBianco) testo = nomeNero.getText() + " (nero)";
                    labelVittoria.setText("<html><div style='text-align:center;'>Tempo scaduto:<br>Vince " + testo + "</div></html>");
                }
                SuoniScacchi.finePartita();
                finePartita();
            }
        });
    }

    private void setListenerSpostamenti() {
        for (int i = 0; i < 4; i++) {
            int ind = i;
            btnOpzioni[i].addActionListener(_ -> {
                if (btnOpzioni[ind].isAbilitato()) {
                    SuoniScacchi.spostamento();
                    switch (ind) {
                        case 0 -> mossaMostrata = 0;
                        case 1 -> {
                            if (mossaMostrata > 0) mossaMostrata--;
                        }
                        case 2 -> {
                            if (mossaMostrata < scacchiera.getMosse()) mossaMostrata++;
                        }
                        case 3 -> mossaMostrata = scacchiera.getMosse();
                        default -> {}
                    }
                    aggiornaScacchiera(PartitaFileManager.leggiScacchiera(mossaMostrata));
                    if (mossaMostrata != scacchiera.getMosse()) {
                        timerBianco.pause();
                        timerBianco.setForeground(timerBianco.getTextColor());
                        timerNero.pause();
                        timerNero.setForeground(timerNero.getTextColor());
                    }
                    else if (partitaInCorso && scacchiera.getTurno().equals(Color.white) && timerBianco.isPaused()) timerBianco.start();
                    else if (partitaInCorso && scacchiera.getTurno().equals(Color.black) && timerNero.isPaused()) timerNero.start();
                    aggiornaLabelMateriale();
                }
                aggiornaBtnSpostamento();
            });
        }
    }

    private void setListenerRotazioneManuale() {
        btnOpzioni[4].addActionListener(_ -> {
            SuoniScacchi.ruota();
            Color c = Color.black;
            if (casellePanel[0][0].getX() > lunghezzaScacchiera / 2) c = Color.white;
            ruotaScacchiera(c, true);
        });
    }

    private void setListenerAudio() {
        btnOpzioni[5].addActionListener(_ -> {
            int tipo = btnOpzioni[5].getTipo();
            if (tipo == 6) tipo++;
            else if (tipo == 7) tipo--;
            btnOpzioni[5].impostaImmagine(tipo);
            SuoniScacchi.audio = !SuoniScacchi.audio;
            SuoniScacchi.menu();
        });
    }

    private void aggiornaBtnSpostamento() {
        if (promozione) {
            for (int n = 0; n < 4; n++) btnOpzioni[n].disabilita();
            return;
        }
        for (int n = 0; n < 4; n++) btnOpzioni[n].abilita();
        if (mossaMostrata != scacchiera.getMosse()) infoMossa = false;
        else {
            btnOpzioni[2].disabilita();
            btnOpzioni[3].disabilita();
            infoMossa = true;
        }
        if (mossaMostrata == 0) {
            btnOpzioni[0].disabilita();
            btnOpzioni[1].disabilita();
        }
        disegna();
    }

    private void setImgCasellePromozione(Color c) {
        if (c.equals(Color.white)) {
            casellePromozione[0].setImg(IconaPedina.REGINA_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[1].setImg(IconaPedina.TORRE_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[2].setImg(IconaPedina.ALFIERE_WHITE.getImageIcon(lunghezzaCasella));
            casellePromozione[3].setImg(IconaPedina.CAVALLO_WHITE.getImageIcon(lunghezzaCasella));
        }
        else {
            casellePromozione[0].setImg(IconaPedina.REGINA_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[1].setImg(IconaPedina.TORRE_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[2].setImg(IconaPedina.ALFIERE_BLACK.getImageIcon(lunghezzaCasella));
            casellePromozione[3].setImg(IconaPedina.CAVALLO_BLACK.getImageIcon(lunghezzaCasella));
        }
    }

    private void ruotaScacchiera(Color c, boolean rotazioneObbligatoria) {
        if (c == null) throw new IllegalArgumentException("Il colore non può essere null");
        if (!c.equals(Color.white) && !c.equals(Color.black)) throw new IllegalArgumentException("Il colore può essere solo bianco o nero");
        if (!(rotazioneObbligatoria || rotazioneScacchiera)) return;
        gestisciGrafica = true;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (c.equals(Color.white)) {
                    casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                    scacchieraGirata = false;
                }
                else {
                    casellePanel[i][j].setBounds(lunghezzaCasella * (7 - j),  lunghezzaCasella * (7 - i), lunghezzaCasella, lunghezzaCasella);
                    scacchieraGirata = true;
                }
            }
        }
        gestisciGrafica = false;
    }

    private void ruotaScacchiera(Color c) {
        ruotaScacchiera(c, false);
    }

    private void aggiornaLabelMateriale() {
        int matBianco = scacchiera.getMaterialeMossa(Color.white, mossaMostrata);
        int matNero = scacchiera.getMaterialeMossa(Color.black, mossaMostrata);
        String diffBianco = "";
        String diffNero = "";
        if (matBianco > matNero) diffBianco = "+";
        else if (matBianco < matNero) diffNero = "+";
        diffBianco += String.valueOf(matBianco - matNero);
        diffNero += String.valueOf(matNero - matBianco);
        materialeBianco.setText("<html>Materiale: " + matBianco + "<br>Differenza: " + diffBianco + "</html>");
        materialeNero.setText("<html>Materiale: " + matNero + "<br>Differenza: " + diffNero + "</html>");
    }

    private void mettiASchermo(Container container) {
        if (aggiuntoASchermo) throw new IllegalStateException("Impossibile mettere a schermo più di una volta");
        for (int i = 0; i < 4; i++) container.add(casellePromozione[i]);
        for (int i = 0; i < DIMENSIONE; i++) for (int j = 0; j < DIMENSIONE; j++) container.add(casellePanel[i][j]);
        container.add(panelInfo);
        for (BottoneOpzioni b : btnOpzioni) container.add(b);
        aggiuntoASchermo = true;
    }

    public void mettiASchermo(JPanel panel) {
        mettiASchermo((Container) panel);
    }

    public void mettiASchermo(JFrame frame) {
        mettiASchermo((Container) frame);
    }

    private void resetMosseValide() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.mossaValida = false;
        }
    }

    private void mostraMosseValide(List<int[]> mosseValide) {
        if (mosseValide == null) throw new IllegalArgumentException("Le mosse valide non possono essere null");
        for (int[] m : mosseValide) {
            casellePanel[m[0]][m[1]].mossaValida = true;
        }
    }

    private void disegna() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.repaint();
        }
        for (Casella c : casellePromozione) c.repaint();
    }

    private class Casella extends JPanel {
        private Color colore;
        private Color variante;
        private final JLabel label;
        private int lunghezzaLato;
        private String id;
        private casellaClickListener listener;
        private boolean mossaValida;

        Casella(Boolean pari, int lunghezzaLato, String id) {
            setLunghezzaLato(lunghezzaLato);
            label = new JLabel();
            label.setPreferredSize(new Dimension(lunghezzaLato,lunghezzaLato));
            setId(id);
            if (pari == null || this.id.equals("PROMOZIONE")) setColore(new Color(0, 0, 0, 0));
            else if (pari) setColore(caselleChiare);
            else setColore(caselleScure);
            this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
            this.add(label);
            mossaValida = false;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (listener != null && SwingUtilities.isLeftMouseButton(e)) listener.casellaCliccata();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (id.equals("PROMOZIONE")) setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            });
        }

        int getLunghezzaLato() {
            return lunghezzaLato;
        }

        private void setLunghezzaLato(int lunghezzaLato) {
            if (lunghezzaLato <= 0) throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
            this.lunghezzaLato = lunghezzaLato;
        }

        Color getColore() {
            return colore;
        }

        private void setColore(Color colore) {
            this.colore = colore;
            super.setBackground(colore);
            variante = varianteColore(colore);
        }

        String getId() {
            return id;
        }

        private void setId(String id) {
            id = id.trim().toUpperCase();
            if ("PROMOZIONE".equals(id)) this.id = id;
            else {
                if (idUtilizzati.contains(id)) throw new IllegalArgumentException("Id " + id + " già in uso");
                if (!id.matches("[A-H][1-8]")) throw new IllegalArgumentException("Formato id non valido (esempio corretto: A1)");
                this.id = id;
                idUtilizzati.add(this.id);
            }
        }

        Icon getImg() {
            return label.getIcon();
        }

        void setImg(ImageIcon img) {
            if (img.getIconWidth() != lunghezzaLato || img.getIconHeight() != lunghezzaLato) {
                Image scaled = img.getImage().getScaledInstance(lunghezzaLato, lunghezzaLato, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
            else label.setIcon(img);
        }

        void rimuoviImg() {
            label.setIcon(null);
        }

        void setListener(casellaClickListener l) {
            this.listener = l;
        }

        private Color varianteColore(Color c) {
            float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
            hsb[1] = Math.min(1f, hsb[1] + 0.4f);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            disegnaCoordinata(g2d);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!getBackground().equals(colore)) super.setBackground(colore);
            if (!infoMossa) {
                g2d.dispose();
                return;
            }

            if (this.id.equals(casellaPosIniziale) || this.id.equals(casellaPosFinale)) super.setBackground(variante);
            g2d.setStroke(new BasicStroke(((float) lunghezzaLato / 20)));
            g2d.setColor(Color.black);

            if (promozione && this.id.equals("PROMOZIONE")) {
                g2d.setColor(new Color(230, 200, 0));
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setColor(Color.black);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            else if (this.id.equals(casellaSelezionata)) g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            else if (mossaValida) {
                Composite old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                int offset;
                if (this.label.getIcon() != null || this.id.equals(idEnPassant)) {
                    offset = lunghezzaLato / 25;
                    g2d.drawOval(offset, offset, lunghezzaLato - 2 * offset - 1, lunghezzaLato - 2 * offset - 1);
                }
                else {
                    offset = lunghezzaLato / 4;
                    g2d.fillOval(offset, offset, offset * 2, offset * 2);
                }
                g2d.setComposite(old);
            }
            g2d.dispose();
        }

        private void disegnaCoordinata(Graphics2D g2d) {
            if (id.equals("PROMOZIONE")) return;
            if (!scacchieraGirata && (id.charAt(0) == 'A' || id.charAt(1) == '1') || scacchieraGirata && (id.charAt(0) == 'H' || id.charAt(1) == '8')) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = new Font("Arial", Font.BOLD, lunghezzaLato / 5);
                g2d.setFont(font);
                if (colore.equals(caselleScure)) g2d.setColor(caselleChiare);
                else g2d.setColor(caselleScure);

                if (!scacchieraGirata) {
                    if (id.equals("A1")) g2d.drawString(id, lunghezzaLato / 15, lunghezzaLato / 5);
                    else if (id.charAt(0) == 'A') g2d.drawString(String.valueOf(id.charAt(1)), lunghezzaLato / 15, lunghezzaLato / 5);
                    else g2d.drawString(String.valueOf(id.charAt(0)), lunghezzaLato / 15, lunghezzaLato / 5);
                }
                else {
                    if (id.equals("H8")) g2d.drawString(id, lunghezzaLato / 15, lunghezzaLato / 5);
                    else if (id.charAt(0) == 'H') g2d.drawString(String.valueOf(id.charAt(1)), lunghezzaLato / 15, lunghezzaLato / 5);
                    else g2d.drawString(String.valueOf(id.charAt(0)), lunghezzaLato / 15, lunghezzaLato / 5);
                }
            }
        }

        //rende meno modificabili possibili le caselle da classi esterne
        @Override
        public void setBounds(Rectangle r) {
            if (gestisciGrafica) super.setBounds(r);
        }

        @Override
        public void setBounds(int x, int y, int width, int height) {
            if (gestisciGrafica) super.setBounds(x, y, width, height);
        }

        @Override
        public void setLayout(LayoutManager mgr) {
            if (gestisciGrafica) super.setLayout(mgr);
        }

        @Override
        public void removeAll() {
            if (gestisciGrafica) super.removeAll();
        }

        @Override
        public void remove(int index) {
            if (gestisciGrafica) super.remove(index);
        }

        @Override
        public void remove(Component comp) {
            if (gestisciGrafica) super.remove(comp);
        }

        @Override
        public void setSize(Dimension d) {
            if (gestisciGrafica) super.setSize(d);
        }

        @Override
        public void setSize(int width, int height) {
            if (gestisciGrafica) super.setSize(width, height);
        }

        @Override
        public Component add(Component comp) {
            if (gestisciGrafica) return super.add(comp);
            return comp;
        }

        @Override
        public Component add(Component comp, int index) {
            if (gestisciGrafica) return super.add(comp, index);
            return comp;
        }

        @Override
        public void add(Component comp, Object constraints) {
            if (gestisciGrafica) super.add(comp, constraints);
        }

        @Override
        public void setBackground(Color bg) {
            if (gestisciGrafica) super.setBackground(bg);
        }

        @Override
        public void setForeground(Color fg) {
            if (gestisciGrafica) super.setForeground(fg);
        }

        @Override
        public void setOpaque(boolean isOpaque) {
            if (gestisciGrafica) super.setOpaque(isOpaque);
        }

        @Override
        public void setBorder(Border border) {
            if (gestisciGrafica) super.setBorder(border);
        }

        @Override
        public void setVisible(boolean aFlag) {
            if (gestisciGrafica) super.setVisible(aFlag);
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (gestisciGrafica) super.setEnabled(enabled);
        }
    }
}
