import scacchiera_pedine.*;
import javax.swing.*;
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
    private boolean mossaNonCorrente;
    private boolean rotazioneScacchiera;
    private boolean promozione;
    private int[] posPromozione;
    public final int lunghezzaScacchiera;
    public final int lunghezzaCasella;
    public final int DIMENSIONE = 8;
    private ImageIcon[] immagini;
    private final String SEP;
    private final Map<Integer, String> numeroToLettera;
    private long ultimoClic;
    private static final long SOGLIA_MS = 250;

    private final JPanel panelInfo;
    private final JButton btnGioca;
    private final JButton btnRotazioneScacchiera;
    private final JLabel labelVittoria;
    private final JTextArea nomeBianco;
    private final JTextArea nomeNero;
    private final JButton btnBotBianco;
    private final JButton btnBotNero;
    private final JButton btnTimer;
    private final TimerGrafico timerBianco;
    private final TimerGrafico timerNero;
    private final JLabel materialeBianco;
    private final JLabel materialeNero;
    private final BottoneSpostamento[] btnSpostamenti;

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo, ImageIcon[] immagini) {
        this.scacchiera = scacchiera;
        this.lunghezzaScacchiera = lunghezzaScacchiera;
        lunghezzaCasella = lunghezzaScacchiera / 8;
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        casellePromozione = new Casella[4];
        partitaInCorso = false;
        mossaMostrata = 0;
        mossaNonCorrente = false;
        rotazioneScacchiera = true;
        promozione = false;
        posPromozione = new int[2];
        SEP = ";";
        numeroToLettera = Map.of(1, "A", 2, "B", 3, "C", 4, "D", 5, "E", 6, "F", 7, "G", 8, "H");
        ultimoClic = 0;
        setImmagini(immagini);
        inizializza();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());

        //inizializzazione parte grafica gestione utente
        panelInfo = new JPanel();
        labelVittoria = new JLabelCustom(null, new Color(180,130,20), new Color(140,95,10));
        nomeBianco = new JTextAreaCustom("Giocatore 1", 0, lunghezzaScacchiera - lunghezzaCasella / 2,  lunghezzaCasella * 2, lunghezzaCasella / 2);
        nomeNero = new JTextAreaCustom("Giocatore 2", 0, 0,  lunghezzaScacchiera / 4, lunghezzaScacchiera / 16);
        timerBianco = new TimerGrafico(0, 10, 0, 0, Color.white, Color.black);
        timerNero = new TimerGrafico(0, 10, 0, 0, Color.black, Color.white);
        materialeBianco = new JLabelCustom(null, Color.white);
        materialeNero = new JLabelCustom(null, Color.black);
        btnGioca = new JButtonCustom("<html>Gioca</html>", 0, lunghezzaCasella * 4 - lunghezzaCasella / 2, lunghezzaCasella * 2, lunghezzaCasella, new Color(30, 100, 210), new Color(15, 60, 160), new Color(50, 130, 240), new Color(25, 90, 190), new Color(10, 40, 120), Color.white);
        btnRotazioneScacchiera = new JButtonCustom("<html><div style='text-align:center;'>Ruota On</div></html>", 0, lunghezzaCasella * 4 + lunghezzaCasella / 2 + lunghezzaCasella / 15, lunghezzaCasella * 2, lunghezzaCasella, new Color(60, 60, 70), new Color(35, 35, 42), new Color(80, 80, 95), new Color(55, 55, 68), new Color(20, 20, 26), Color.white);
        btnTimer = new JButtonCustom("<html><div style='text-align:center;'>Imposta timer</div></html>", 0, lunghezzaCasella * 4 - lunghezzaCasella * 3 / 2 - lunghezzaCasella / 15, lunghezzaCasella * 2, lunghezzaCasella, new Color(240, 120, 20), new Color(200, 75, 10), new Color(255, 150, 50), new Color(225, 100, 30), new Color(160, 50, 5), Color.white);
        btnBotBianco = new JButtonCustom("<html><div style='text-align:center;'>Bot<br>Off</div></html>", lunghezzaCasella * 2 + lunghezzaCasella / 15, lunghezzaScacchiera - lunghezzaCasella / 2,  lunghezzaCasella / 2, lunghezzaCasella / 2, new Color(51, 51, 51), new Color(0, 0, 0), new Color(85, 85, 85), new Color(0, 0, 0), new Color(0, 0, 0), Color.white);
        btnBotNero = new JButtonCustom("<html><div style='text-align:center;'>Bot<br>Off</div></html>", lunghezzaCasella * 2 + lunghezzaCasella / 15, 0,  lunghezzaCasella / 2, lunghezzaCasella / 2, new Color(51, 51, 51), new Color(0, 0, 0), new Color(85, 85, 85), new Color(0, 0, 0), new Color(0, 0, 0), Color.white);
        btnSpostamenti = new BottoneSpostamento[4];
        for (int i = 0; i < 4; i++) btnSpostamenti[i] = new BottoneSpostamento(i + 1, lunghezzaScacchiera - lunghezzaCasella / 2 * (4 - i) - lunghezzaCasella / 15 * (4 - i), lunghezzaScacchiera + lunghezzaCasella / 8, lunghezzaCasella / 2);

        //setBounds
        panelInfo.setBounds(lunghezzaScacchiera + lunghezzaCasella * 4 / 3, 0, lunghezzaCasella * 6, lunghezzaScacchiera);
        labelVittoria.setBounds(lunghezzaCasella * 2 + lunghezzaCasella / 15, lunghezzaCasella * 4 - lunghezzaCasella / 4 * 3, lunghezzaCasella * 5 / 2, lunghezzaCasella * 3 / 2);
        timerBianco.setBounds(0, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        timerNero.setBounds(0, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaCasella * 4 / 3, lunghezzaCasella / 2);
        materialeBianco.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaScacchiera - lunghezzaCasella - lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);
        materialeNero.setBounds(lunghezzaCasella * 4 / 3 + lunghezzaCasella / 15, lunghezzaCasella / 2 + lunghezzaCasella / 15,  lunghezzaScacchiera / 7, lunghezzaCasella / 2);

        //modifiche grafiche
        panelInfo.setLayout(null);
        panelInfo.setOpaque(true);
        panelInfo.setBackground(sfondo);

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
        aggiornaBtnSpostamento();

        panelInfo.add(btnGioca);
        panelInfo.add(btnRotazioneScacchiera);
        panelInfo.add(nomeBianco);
        panelInfo.add(nomeNero);
        panelInfo.add(btnBotBianco);
        panelInfo.add(btnBotNero);
        panelInfo.add(labelVittoria);
        panelInfo.add(timerBianco);
        panelInfo.add(timerNero);
        panelInfo.add(materialeBianco);
        panelInfo.add(materialeNero);
        panelInfo.add(btnTimer);

        //listener gestione utente
        btnGioca.addActionListener(_ -> gioca());

        btnRotazioneScacchiera.addActionListener(_ -> {
            rotazioneScacchiera = !rotazioneScacchiera;
            if (rotazioneScacchiera) btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>On</div></html>");
            else btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>Off</div></html>");
        });

        btnTimer.addActionListener(_ -> creaDialogTimer());
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, Color sfondo) {
        int lunghezzaCasella = lunghezzaScacchiera / 8;
        this(scacchiera, lunghezzaScacchiera, sfondo, new ImageIcon[]{
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
        });
    }

    private void inizializza() {
        Casella.casellePari = new Color(240, 217, 181);
        Casella.caselleDispari = new Color(161, 116, 79);
        Casella.gestisciGrafica = true;
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
            casellePromozione[i].setBounds(lunghezzaScacchiera, offset, lunghezzaCasella, lunghezzaCasella);
            casellePromozione[i].setOpaque(false);
            setListenerPromozione(i);
        }
        Casella.gestisciGrafica = false;
    }

    private void setImmagini(ImageIcon[] immagini) {
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
        ruotaScacchiera(scacchiera.getTurno());
        Casella.casellaPosIniziale = null;
        Casella.casellaPosFinale = null;
        disegna();
        labelVittoria.setText(null);
        btnGioca.setEnabled(false);
        btnGioca.setText("<html>Gioca ancora</html>");
        partitaInCorso = true;
        mossaMostrata = 0;
        aggiornaBtnSpostamento();
        mossaNonCorrente = false;
        nomeBianco.setText(nomeBianco.getText().trim());
        nomeNero.setText(nomeNero.getText().trim());
        if (nomeBianco.getText().isEmpty()) nomeBianco.setText("Giocatore 1");
        if (nomeNero.getText().isEmpty()) nomeNero.setText("Giocatore 2");
        if (nomeBianco.getText().equals(nomeNero.getText())) {
            if (nomeNero.getText().length() == 15) nomeNero.setText(nomeNero.getText().substring(0, 14) + "N");
            else nomeNero.setText(nomeNero.getText() + "N");
        }
        nomeBianco.setEditable(false);
        nomeNero.setEditable(false);
        btnBotBianco.setEnabled(false);
        btnBotNero.setEnabled(false);
        btnRotazioneScacchiera.setEnabled(false);
        btnTimer.setEnabled(false);
        timerBianco.reset();
        timerNero.reset();
        timerBianco.start();
        aggiornaLabelMateriale();
    }

    private void finePartita() {
        Casella.casellaSelezionata = null;
        resetMosseValide();
        disegna();
        btnGioca.setEnabled(true);
        nomeBianco.setEditable(true);
        nomeNero.setEditable(true);
        btnBotBianco.setEnabled(true);
        btnBotNero.setEnabled(true);
        partitaInCorso = false;
        btnRotazioneScacchiera.setEnabled(true);
        timerBianco.pause(true);
        timerNero.pause(true);
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        btnTimer.setEnabled(true);
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
            long ora = System.currentTimeMillis();
            if (ora - ultimoClic <= SOGLIA_MS && mossaNonCorrente && !promozione) {
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

            if (!promozione && partitaInCorso && !mossaNonCorrente) {
                Pedina p = scacchiera.getPedina(new int[]{y, x});
                String idCasellaSelOld = Casella.casellaSelezionata;
                resetMosseValide();
                if (casellePanel[y][x].getId().equals(Casella.casellaSelezionata)) {
                    Casella.casellaSelezionata = null;
                    scacchiera.deSelezionaPedina();
                    disegna();
                    return;
                }
                if (p != null && p.getColore().equals(scacchiera.getTurno())) Casella.casellaSelezionata = casellePanel[y][x].getId();
                else Casella.casellaSelezionata = null;

                if (scacchiera.getCasellaSelezionata() == null || !scacchiera.muoviPedina(new int[]{y, x})) { //se la casella selezionata non è null allora seleziona la pedina; se è null prova a spostarla e se non riesce seleziona la pedina dove si intendeva spostare quella selezionata precedentemente
                    List<int[]> mosseValide = scacchiera.selezionaPedina(new int[]{y, x}, scacchiera.getTurno());
                    Casella.idEnPassant = null;
                    if (scacchiera.getPedinaSelezionata() instanceof Pedone) {
                        for (int[] mossa : mosseValide) {
                            if (mossa[1] != scacchiera.getCasellaSelezionata()[1] && scacchiera.getPedina(mossa) == null) {
                                Casella.idEnPassant = casellePanel[mossa[0]][mossa[1]].getId();
                                break;
                            }
                        }
                    }
                    if (mosseValide != null) mostraMosseValide(mosseValide);
                }

                else if ((y == 0 || y == DIMENSIONE - 1) && scacchiera.getPedina(new int[]{y, x}) instanceof Pedone) {
                    Casella.casellaPosFinale = casellePanel[y][x].getId();
                    Casella.casellaPosIniziale = idCasellaSelOld;
                    promozione = true;
                    Casella.sceltaPromozione = true;
                    posPromozione = new int[]{y, x};
                    setImgCasellePromozione(scacchiera.getPedina(posPromozione).getColore());
                    aggiornaBtnSpostamento();
                }

                else {
                    Casella.casellaPosFinale = casellePanel[y][x].getId();
                    Casella.casellaPosIniziale = idCasellaSelOld;
                    aggiornaInfoScacchiera();
                }

                disegna();
            }
        });
    }

    private void aggiornaInfoScacchiera() {
        scacchiera.cambiaTurno();
        timerBianco.invertiStato();
        timerNero.invertiStato();
        timerBianco.setForeground(timerBianco.getTextColor());
        timerNero.setForeground(timerNero.getTextColor());
        mossaMostrata = scacchiera.getMosse();
        aggiornaBtnSpostamento();
        aggiornaLabelMateriale();
        aggiornaScacchiera(scacchiera.getStringaScacchiera());
        switch (scacchiera.getStatoPartita()) {
            case 0 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeBianco.getText() + " (bianco)</div></html>");
                finePartita();
            }
            case 1 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeNero.getText() + " (nero)</div></html>");
                finePartita();
            }
            case 2 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Stallo:<br>Pareggio</div></html>");
                finePartita();
            }
            case 3 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>Materiale insufficiente:<br>Pareggio</div></html>");
                finePartita();
            }
            case 4 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>75 mosse neutre:<br>Pareggio</div></html>");
                finePartita();
            }
            case 5 -> {
                labelVittoria.setText("<html><div style='text-align:center;'>5 posizioni ripetute:<br>Pareggio</div></html>");
                finePartita();
            }
            default -> {
                if (rotazioneScacchiera) ruotaScacchiera(scacchiera.getTurno());
            }
        }
    }

    private void setListenerPromozione(int i) {
        casellePromozione[i].setListener(() -> {
            if (promozione && !mossaNonCorrente) {
                scacchiera.promuoviPedone(posPromozione, i + 1);
                promozione = false;
                Casella.sceltaPromozione = false;
                for (Casella c : casellePromozione) c.rimuoviImg();
                aggiornaInfoScacchiera();
                aggiornaScacchiera(scacchiera.getStringaScacchiera());
                mossaMostrata = scacchiera.getMosse();
                aggiornaBtnSpostamento();
                disegna();
            }
        });
    }

    private void setListenerTimer(TimerGrafico t) {
        t.addPropertyChangeListener("text", e -> {
            if (t.getMinuti() == 0 && (t.getMinutiDefault() > 0 || t.getSecondi() < 10)) {
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
                finePartita();
            }
        });
    }

    private void setListenerSpostamenti() {
        for (int i = 0; i < btnSpostamenti.length; i++) {
            int ind = i;
            btnSpostamenti[i].addActionListener(_ -> {
                if (btnSpostamenti[ind].isAbilitato()) {
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
                    aggiornaScacchiera(scacchiera.getStringaScacchieraMossa(mossaMostrata));
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

    private void aggiornaBtnSpostamento() {
        if (promozione) {
            for (int n = 0; n < 4; n++) btnSpostamenti[n].disabilita();
            return;
        }
        for (int n = 0; n < 4; n++) btnSpostamenti[n].abilita();
        if (mossaMostrata != scacchiera.getMosse()) {
            Casella.infoMossa = false;
            mossaNonCorrente = true;
        }
        else {
            btnSpostamenti[2].disabilita();
            btnSpostamenti[3].disabilita();
            Casella.infoMossa = true;
            mossaNonCorrente = false;
        }
        if (mossaMostrata == 0) {
            btnSpostamenti[0].disabilita();
            btnSpostamenti[1].disabilita();
        }
        disegna();
    }

    private void setImgCasellePromozione(Color c) {
        int lunghezzaCasella = lunghezzaScacchiera / 8;
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

    private void ruotaScacchiera(Color c) {
        Casella.gestisciGrafica = true;
        int lunghezzaCasella = lunghezzaScacchiera / 8;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                if (c.equals(Color.white)) {
                    casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                    Casella.scacchieraGirata = false;
                }
                else {
                    casellePanel[i][j].setBounds(lunghezzaCasella * (7 - j),  lunghezzaCasella * (7 - i), lunghezzaCasella, lunghezzaCasella);
                    Casella.scacchieraGirata = true;
                }
            }
        }
        Casella.gestisciGrafica = false;
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

    private void creaDialogTimer() {
        JSpinner s1 = new JSpinner(new SpinnerNumberModel(timerBianco.getOreDefault(), 0, 23, 1));
        JSpinner s2 = new JSpinner(new SpinnerNumberModel(timerBianco.getMinutiDefault(), 0, 59, 5));
        JSpinner s3 = new JSpinner(new SpinnerNumberModel(timerBianco.getSecondiDefault(), 0, 59, 5));
        JSpinner s4 = new JSpinner(new SpinnerNumberModel(timerBianco.getGuadagno(), 0, 60, 5));

        JDialog dialog = new JDialog();
        dialog.setIconImage(new ImageIcon(new ImageIcon("img/chess.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)).getImage());
        dialog.setTitle("Imposta il timer");
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setLayout(new BorderLayout());

        Font f = new Font("Segoe UI", Font.BOLD, 14);
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel l1 = new JLabel("Ore:"); l1.setFont(f); panel.add(l1);
        s1.setFont(f); panel.add(s1);
        JLabel l2 = new JLabel("Minuti:"); l2.setFont(f); panel.add(l2);
        s2.setFont(f); panel.add(s2);
        JLabel l3 = new JLabel("Secondi:"); l3.setFont(f); panel.add(l3);
        s3.setFont(f); panel.add(s3);
        JLabel l4 = new JLabel("Guadagno (sec):"); l4.setFont(f); panel.add(l4);
        s4.setFont(f); panel.add(s4);

        JButton btnConferma = new JButton("Conferma");
        Insets margin = new Insets(4, 8, 4, 8);
        btnConferma.setFont(f);
        btnConferma.setMargin(margin);
        btnConferma.setBackground(new Color(66, 133, 244));
        btnConferma.setForeground(Color.white);
        btnConferma.setFocusPainted(false);
        btnConferma.setOpaque(true);
        btnConferma.setBorderPainted(false);
        btnConferma.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConferma.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnConferma.setBackground(new Color(90, 160, 255)); }
            public void mouseExited(MouseEvent e) { btnConferma.setBackground(new Color(66, 133, 244)); }
        });
        btnConferma.addActionListener(_ -> {
            timerBianco.setTimer((int) s1.getValue(), (int) s2.getValue(), (int) s3.getValue(), (int) s4.getValue());
            timerNero.setTimer((int) s1.getValue(), (int) s2.getValue(), (int) s3.getValue(), (int) s4.getValue());
            dialog.dispose();
        });

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.setFont(f);
        btnAnnulla.setMargin(margin);
        btnAnnulla.setBackground(new Color(60, 60, 60));
        btnAnnulla.setForeground(Color.white);
        btnAnnulla.setFocusPainted(false);
        btnAnnulla.setOpaque(true);
        btnAnnulla.setBorderPainted(false);
        btnAnnulla.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAnnulla.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnAnnulla.setBackground(new Color(80, 80, 80)); }
            public void mouseExited(MouseEvent e) { btnAnnulla.setBackground(new Color(60, 60, 60)); }
        });
        btnAnnulla.addActionListener(_ -> dialog.dispose());

        JButton btnDisattiva = new JButton("Disattiva");
        btnDisattiva.setFont(f);
        btnDisattiva.setMargin(margin);
        btnDisattiva.setBackground(new Color(180, 40, 40));
        btnDisattiva.setForeground(Color.white);
        btnDisattiva.setFocusPainted(false);
        btnDisattiva.setOpaque(true);
        btnDisattiva.setBorderPainted(false);
        btnDisattiva.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDisattiva.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDisattiva.setBackground(new Color(210, 55, 55)); }
            public void mouseExited(MouseEvent e) { btnDisattiva.setBackground(new Color(180, 40, 40)); }
        });
        btnDisattiva.addActionListener(_ -> {
            timerBianco.disattiva();
            timerNero.disattiva();
            dialog.dispose();
        });

        JPanel panelBottoni = new JPanel(new GridLayout(1, 3, 10, 0));
        panelBottoni.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelBottoni.add(btnAnnulla);
        panelBottoni.add(btnDisattiva);
        panelBottoni.add(btnConferma);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBottoni, BorderLayout.SOUTH);
        dialog.setSize(lunghezzaCasella * 9 / 2, lunghezzaCasella * 7 / 2);
        dialog.setMinimumSize(new Dimension(350, 300));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void mettiASchermo(Container container) {
        for (int i = 0; i < 4; i++) container.add(casellePromozione[i]);
        for (int i = 0; i < DIMENSIONE; i++) for (int j = 0; j < DIMENSIONE; j++) container.add(casellePanel[i][j]);
        container.add(panelInfo);
        for (BottoneSpostamento b : btnSpostamenti) container.add(b);
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

    private static class Casella extends JPanel {
        private static Color caselleDispari = Color.white;
        private static Color casellePari = Color.black;
        private Color colore;
        private Color variante;
        private final JLabel label;
        private int lunghezzaLato;
        private String id;
        private static String casellaSelezionata = null;
        private static String casellaPosIniziale = null;
        private static String casellaPosFinale = null;
        private static final List<String> idUtilizzati = new ArrayList<>();
        private casellaClickListener listener;
        public boolean mossaValida;
        private static boolean sceltaPromozione = false;
        private static boolean scacchieraGirata = false;
        private static boolean gestisciGrafica = false;
        private static boolean infoMossa = true;
        private static String idEnPassant = null;

        public Casella(Boolean pari, int lunghezzaLato, String id) {
            setLunghezzaLato(lunghezzaLato);
            label = new JLabel();
            label.setPreferredSize(new Dimension(lunghezzaLato,lunghezzaLato));
            setId(id);
            if (pari == null || this.id.equals("PROMOZIONE")) setColore(new Color(0, 0, 0, 0));
            else if (pari) setColore(casellePari);
            else setColore(caselleDispari);
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

        public int getLunghezzaLato() {
            return lunghezzaLato;
        }

        private void setLunghezzaLato(int lunghezzaLato) {
            if (lunghezzaLato <= 0) throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
            this.lunghezzaLato = lunghezzaLato;
        }

        public Color getColore() {
            return colore;
        }

        private void setColore(Color colore) {
            this.colore = colore;
            this.setBackground(colore);
            variante = varianteColore(colore);
        }

        public String getId() {
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

        public Icon getImg() {
            return label.getIcon();
        }

        public void setImg(ImageIcon img) {
            if (img.getIconWidth() != lunghezzaLato || img.getIconHeight() != lunghezzaLato) {
                Image scaled = img.getImage().getScaledInstance(lunghezzaLato, lunghezzaLato, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
            else label.setIcon(img);
        }

        public void rimuoviImg() {
            label.setIcon(null);
        }

        public void setListener(casellaClickListener l) {
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

            if (!getBackground().equals(colore)) setBackground(colore);
            if (!infoMossa) {
                g2d.dispose();
                return;
            }

            if (this.id.equals(casellaPosIniziale) || this.id.equals(casellaPosFinale)) setBackground(variante);

            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.black);

            if (sceltaPromozione && this.id.equals("PROMOZIONE")) {
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
            if (!scacchieraGirata && (id.charAt(0) == 'A' || id.charAt(1) == '1') || scacchieraGirata && (id.charAt(0) == 'H' || id.charAt(1) == '8')) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = new Font("Arial", Font.BOLD, lunghezzaLato / 5);
                g2d.setFont(font);
                if (colore.equals(caselleDispari)) g2d.setColor(casellePari);
                else g2d.setColor(caselleDispari);

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

        //da rivedere

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

//        @Override
//        public Component[] getComponents() {
//            if (gestisciGrafica) return super.getComponents();
//            return null;
//        }

//        @Override
//        public Component getComponent(int n) {
//            if (gestisciGrafica) return super.getComponent(n);
//            return null;
//        }

        @Override
        public void setSize(Dimension d) {
            if (gestisciGrafica) super.setSize(d);
        }

        @Override
        public void setSize(int width, int height) {
            if (gestisciGrafica) super.setSize(width, height);
        }
    }
}
