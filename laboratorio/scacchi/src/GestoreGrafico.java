import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GestoreGrafico {
    private final Casella[][] casellePanel;
    private final Casella[] casellePromozione;
    private final Scacchiera scacchiera;
    private boolean gioca;
    private boolean rotazioneScacchiera;
    private boolean promozione;
    private int[] posPromozione;
    public final int lunghezzaScacchiera;
    public final int DIMENSIONE = 8;
    private ImageIcon pedoneW; //da mettere in un array e rivedere controlli
    private ImageIcon alfiereW;
    private ImageIcon cavalloW;
    private ImageIcon torreW;
    private ImageIcon reginaW;
    private ImageIcon reW;
    private ImageIcon pedoneB;
    private ImageIcon alfiereB;
    private ImageIcon cavalloB;
    private ImageIcon torreB;
    private ImageIcon reginaB;
    private ImageIcon reB;
    private final Map<Integer, String> numeroToLettera = new HashMap<>();

    private final JPanel panelInfo;
    private final JButtonCustom btnGioca;
    private final JButtonCustom btnRotazioneScacchiera;
    private final JLabel labelVittoria;
    private final JTextAreaCustom nomeBianco;
    private final JTextAreaCustom nomeNero;
    private final JButtonCustom btnBotBianco;
    private final JButtonCustom btnBotNero;

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera, ImageIcon pedoneW, ImageIcon alfiereW, ImageIcon cavalloW, ImageIcon torreW, ImageIcon reginaW, ImageIcon reW, ImageIcon pedoneB, ImageIcon alfiereB, ImageIcon cavalloB, ImageIcon torreB, ImageIcon reginaB, ImageIcon reB) {
        numeroToLettera.put(1, "A");
        numeroToLettera.put(2, "B");
        numeroToLettera.put(3, "C");
        numeroToLettera.put(4, "D");
        numeroToLettera.put(5, "E");
        numeroToLettera.put(6, "F");
        numeroToLettera.put(7, "G");
        numeroToLettera.put(8, "H");

        setPedoneW(pedoneW);
        setAlfiereW(alfiereW);
        setCavalloW(cavalloW);
        setTorreW(torreW);
        setReginaW(reginaW);
        setReW(reW);
        setPedoneB(pedoneB);
        setAlfiereB(alfiereB);
        setCavalloB(cavalloB);
        setTorreB(torreB);
        setReginaB(reginaB);
        setReB(reB);

        this.scacchiera = scacchiera;
        this.lunghezzaScacchiera = lunghezzaScacchiera;
        casellePanel = new Casella[DIMENSIONE][DIMENSIONE];
        casellePromozione = new Casella[4];
        promozione = false;
        posPromozione = new int[2];
        gioca = false;
        rotazioneScacchiera = true;
        inizializza();
        aggiornaScacchiera(scacchiera.getScacchiera());

        panelInfo = new JPanel();
        btnGioca = new JButtonCustom("<html>Gioca</html>", 0, lunghezzaScacchiera / 2 - lunghezzaScacchiera / 16, lunghezzaScacchiera / 4, lunghezzaScacchiera / 8, new Color(66, 133, 244), new Color(52, 103, 206), new Color(90, 160, 255), new Color(66, 133, 244), new Color(30, 70, 180), Color.white);
        btnRotazioneScacchiera = new JButtonCustom("<html><div style='text-align:center;'>Ruota<br>On</div><html>", lunghezzaScacchiera / 4 + lunghezzaScacchiera / 100, lunghezzaScacchiera / 2 - lunghezzaScacchiera / 16, lunghezzaScacchiera / 4, lunghezzaScacchiera / 8, new Color(66, 133, 244), new Color(52, 103, 206), new Color(90, 160, 255), new Color(66, 133, 244), new Color(30, 70, 180), Color.white);
        labelVittoria = new JLabel();
        nomeBianco = new JTextAreaCustom("Giocatore 1", lunghezzaScacchiera / 16 + lunghezzaScacchiera / 100, lunghezzaScacchiera - lunghezzaScacchiera / 16,  lunghezzaScacchiera / 4, lunghezzaScacchiera / 16);
        nomeNero = new JTextAreaCustom("Giocatore 2", lunghezzaScacchiera / 16 + lunghezzaScacchiera / 100, 0,  lunghezzaScacchiera / 4, lunghezzaScacchiera / 16);
        btnBotBianco = new JButtonCustom("<html><div style='text-align:center;'>Bot<br>Off</div><html>", 0, lunghezzaScacchiera - lunghezzaScacchiera / 16,  lunghezzaScacchiera / 16, lunghezzaScacchiera / 16, new Color(51, 51, 51), new Color(0, 0, 0), new Color(85, 85, 85), new Color(0, 0, 0), new Color(0, 0, 0), Color.WHITE);
        btnBotNero = new JButtonCustom("<html><div style='text-align:center;'>Bot<br>Off</div><html>", 0, 0,  lunghezzaScacchiera / 16, lunghezzaScacchiera / 16, new Color(51, 51, 51), new Color(0, 0, 0), new Color(85, 85, 85), new Color(0, 0, 0), new Color(0, 0, 0), Color.WHITE);

        panelInfo.setBounds(lunghezzaScacchiera + lunghezzaScacchiera / 6, 0, lunghezzaScacchiera * 3 / 4, lunghezzaScacchiera);
        panelInfo.setLayout(null);
//        panelInfo.setBackground(Color.red);
        panelInfo.setOpaque(true);

        btnGioca.addActionListener(e -> {
            scacchiera.reset();
            if (rotazioneScacchiera) ruotaScacchiera(scacchiera.getTurno());
            Casella.casellaSelezionata = null;
            aggiornaScacchiera(scacchiera.getScacchiera());
            disegna();
            btnGioca.setEnabled(false);
            btnGioca.setText("<html>Gioca ancora</html>");
            labelVittoria.setText(null);
            gioca = true;
            nomeBianco.setText(nomeBianco.getText().trim());
            nomeNero.setText(nomeNero.getText().trim());
            if (nomeBianco.getText().isEmpty()) nomeBianco.setText("Giocatore 1");
            if (nomeNero.getText().isEmpty()) nomeNero.setText("Giocatore 2");
            if (nomeBianco.getText().equals(nomeNero.getText())) nomeNero.setText(nomeNero.getText() + " 1");
            nomeBianco.setEditable(false);
            nomeNero.setEditable(false);
            btnBotBianco.setEnabled(false);
            btnBotNero.setEnabled(false);
            btnRotazioneScacchiera.setEnabled(false);
        });

        btnRotazioneScacchiera.addActionListener(e -> {
            rotazioneScacchiera = !rotazioneScacchiera;
            if (rotazioneScacchiera) btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>On</div><html>");
            else btnRotazioneScacchiera.setText("<html><div style='text-align:center;'>Ruota<br>Off</div><html>");
        });

        panelInfo.add(btnGioca);
        panelInfo.add(btnRotazioneScacchiera);
        panelInfo.add(nomeBianco);
        panelInfo.add(nomeNero);
        panelInfo.add(btnBotBianco);
        panelInfo.add(btnBotNero);

        labelVittoria.setBounds(0, lunghezzaScacchiera / 3, lunghezzaScacchiera / 2 + lunghezzaScacchiera / 100, lunghezzaScacchiera / 10);
        labelVittoria.setOpaque(true);
        labelVittoria.setHorizontalAlignment(SwingConstants.CENTER);
        labelVittoria.setForeground(Color.black);
        labelVittoria.setFont(new Font("Segoe UI", Font.BOLD, Math.max(12, lunghezzaScacchiera / 32)));

        panelInfo.add(labelVittoria);
    }

    public GestoreGrafico(Scacchiera scacchiera, int lunghezzaScacchiera) {
        int lunghezzaCasella = lunghezzaScacchiera / 8;
        this(scacchiera, lunghezzaScacchiera,IconaPedina.PEDONE_WHITE.getImageIcon(lunghezzaCasella),
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
                         IconaPedina.RE_BLACK.getImageIcon(lunghezzaCasella));
    }

    private void inizializza() {
        Casella.gestisciGrafica = true;
        int lunghezzaCasella = lunghezzaScacchiera / 8;
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                Color c;
                if ((j + i) % 2 == 0) c = new Color(240, 217, 181);
                else c = new Color(161, 116, 79);

                casellePanel[i][j] = new Casella(c, lunghezzaCasella, numeroToLettera.get(j + 1) + (DIMENSIONE - i));
                casellePanel[i][j].setBounds(lunghezzaCasella * j, lunghezzaCasella * i, lunghezzaCasella, lunghezzaCasella);
                setListener(i, j);
            }
        }
        for (int i = 0; i < 4; i++) {
            casellePromozione[i] = new Casella(new Color(0, 0, 0, 0), lunghezzaCasella, "PROMOZIONE");
            int offset = lunghezzaCasella + lunghezzaCasella / 2 + i * (lunghezzaCasella + lunghezzaCasella / 3);
            casellePromozione[i].setBounds(lunghezzaScacchiera, offset, lunghezzaCasella, lunghezzaCasella);
            casellePromozione[i].setOpaque(false);
            setListenerPromozione(i);
        }
        Casella.gestisciGrafica = false;
    }

    private void reset() {
        btnGioca.setEnabled(true);
        nomeBianco.setEditable(true);
        nomeNero.setEditable(true);
        btnBotBianco.setEnabled(true);
        btnBotNero.setEnabled(true);
        gioca = false;
        ruotaScacchiera(Color.white);
        btnRotazioneScacchiera.setEnabled(true);
    }

    private void setImgCasella(Color col, Casella c, ImageIcon imgW, ImageIcon imgB) {
        if (col.equals(Color.white)) c.setImg(imgW);
        else c.setImg(imgB);
    }

    private void aggiornaScacchiera(Pedina[][] scacchiera) {
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                Pedina p = scacchiera[i][j];
                Casella c = casellePanel[i][j];
                if (p == null) c.rimuoviImg();
                else {
                    Color col = p.getColore();
                    switch (p) {
                        case Pedone _ -> setImgCasella(col, c, pedoneW, pedoneB);
                        case Alfiere _ -> setImgCasella(col, c, alfiereW, alfiereB);
                        case Torre _ -> setImgCasella(col, c, torreW, torreB);
                        case Regina _ -> setImgCasella(col, c, reginaW, reginaB);
                        case Re _ -> setImgCasella(col, c, reW, reB);
                        case Cavallo _ -> setImgCasella(col, c, cavalloW, cavalloB);
                        default -> throw new IllegalStateException("Tipo pedina non valido: " + p.getClass().getSimpleName());
                    }
                }
            }
        }
    }

    public JPanel[][] getCasellePanel() {
        Casella[][] copia = new Casella[DIMENSIONE][DIMENSIONE];
        for (int i = 0; i < DIMENSIONE; i++) {
            for (int j = 0; j < DIMENSIONE; j++) {
                copia[i][j] = new Casella(casellePanel[i][j]);
            }
        }
        return copia;
    }

    private void setListener(int y, int x) {
        casellePanel[y][x].setListener(() -> {
            if (!promozione && gioca) {
                Pedina p = scacchiera.getPedina(new int[]{y, x});
                if (p != null && p.getColore().equals(scacchiera.getTurno())) Casella.casellaSelezionata = casellePanel[y][x].id;
                resetMosseValide();

                if (scacchiera.getCasella_selezionata() == null || !scacchiera.muoviPedina(new int[]{y, x})) { //se la casella selezionata non è null allora seleziona la pedina; se è null prova a spostarla e se non riesce seleziona la pedina dove si intendeva spostare quella selezionata precedentemente
                    List<int[]> mosseValide = scacchiera.selezionaPedina(new int[]{y, x}, scacchiera.getTurno());
                    if (mosseValide != null) mostraMosseValide(mosseValide);
                }
                else if ((y == 0 || y == 7) && scacchiera.getPedina(new int[]{y, x}) instanceof Pedone) {
                    promozione = true;
                    Casella.sceltaPromozione = true;
                    posPromozione = new int[]{y, x};
                    setImgCasellePromozione(scacchiera.getPedina(posPromozione).getColore());
                }
                else {
                    Casella.casellaSelezionata = casellePanel[y][x].id;
                    scacchiera.cambiaTurno();
                    switch (scacchiera.getStatoPartita()) {
                        case 0 -> {
                            labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeBianco.getText() + " (bianco)</div><html>");
                            reset();
                        }
                        case 1 -> {
                            labelVittoria.setText("<html><div style='text-align:center;'>Scacco matto:<br>Vince " + nomeNero.getText() + " (nero)</div><html>");
                            reset();
                        }
                        case 2 -> {
                            labelVittoria.setText("<html><div style='text-align:center;'>Stallo:<br>Pareggio</div><html>");
                            reset();
                        }
                        default -> {
                            if (rotazioneScacchiera) ruotaScacchiera(scacchiera.getTurno());
                        }
                    }
//                    if (scacchiera.getStatoPartita() != -1) btnGioca.setEnabled(true);
//                    else ruotaScacchiera(scacchiera.getTurno());
                }

                aggiornaScacchiera(scacchiera.getScacchiera());
                disegna();
            }
        });
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

    private void setListenerPromozione(int i) {
        casellePromozione[i].setListener(() -> {
            if (promozione) {
                scacchiera.promuoviPedone(posPromozione, i + 1);
                promozione = false;
                Casella.sceltaPromozione = false;
                for (Casella c : casellePromozione) c.rimuoviImg();
                scacchiera.cambiaTurno();
                if (rotazioneScacchiera) ruotaScacchiera(scacchiera.getTurno());
                aggiornaScacchiera(scacchiera.getScacchiera());
                disegna();
            }
        });
    }

    public void mettiASchermo(JPanel panel) {
        for (int i = 0; i < 4; i++) panel.add(casellePromozione[i]);
        for (int i = 0; i < DIMENSIONE; i++) for (int j = 0; j < DIMENSIONE; j++) panel.add(casellePanel[i][j]);
        panel.add(panelInfo);
    }

    public void mettiASchermo(JFrame frame) {
        for (int i = 0; i < 4; i++) frame.add(casellePromozione[i]);
        for (int i = 0; i < DIMENSIONE; i++) for (int j = 0; j < DIMENSIONE; j++) frame.add(casellePanel[i][j]);
        frame.add(panelInfo);
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

    private void disegna() {
        for (Casella[] riga : casellePanel) {
            for (Casella c : riga) c.repaint();
        }
        for (Casella c : casellePromozione) c.repaint();
    }

    private void controllaImmagini(ImageIcon daControllare, ImageIcon img1, ImageIcon img2, ImageIcon img3, ImageIcon img4, ImageIcon img5, ImageIcon img6, ImageIcon img7, ImageIcon img8, ImageIcon img9, ImageIcon img10, ImageIcon img11) {
        if (daControllare == null) throw new IllegalArgumentException("Le immagini delle pedine non possono essere null");
        if (daControllare == img1 || daControllare == img2 || daControllare == img3 || daControllare == img4 || daControllare == img5 || daControllare == img6 || daControllare == img7 || daControllare == img8 || daControllare == img9 || daControllare == img10 || daControllare == img11) throw new IllegalArgumentException("Le immagini delle pedine devono essere diverse fra loro");
    }

    public ImageIcon getPedoneW() {
        return new ImageIcon(pedoneW.getImage());
    }

    private void setPedoneW(ImageIcon pedoneW) {
        controllaImmagini(pedoneW, alfiereW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneW = pedoneW;
    }

    public ImageIcon getAlfiereW() {
        return new ImageIcon(alfiereW.getImage());
    }

    private void setAlfiereW(ImageIcon alfiereW) {
        controllaImmagini(alfiereW, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.alfiereW = alfiereW;
    }

    public ImageIcon getCavalloW() {
        return new ImageIcon(cavalloW.getImage());
    }

    private void setCavalloW(ImageIcon cavalloW) {
        controllaImmagini(cavalloW, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.cavalloW = cavalloW;
    }

    public ImageIcon getTorreW() {
        return new ImageIcon(torreW.getImage());
    }

    private void setTorreW(ImageIcon torreW) {
        controllaImmagini(torreW, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.torreW = torreW;
    }

    public ImageIcon getReginaW() {
        return new ImageIcon(reginaW.getImage());
    }

    private void setReginaW(ImageIcon reginaW) {
        controllaImmagini(reginaW, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reginaW = reginaW;
    }

    public ImageIcon getReW() {
        return new ImageIcon(reW.getImage());
    }

    private void setReW(ImageIcon reW) {
        controllaImmagini(reW, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reB);
        this.reW = reW;
    }

    public ImageIcon getPedoneB() {
        return new ImageIcon(pedoneB.getImage());
    }

    private void setPedoneB(ImageIcon pedoneB) {
        controllaImmagini(pedoneB, alfiereW, cavalloW, torreW, reginaW, reW, pedoneW, alfiereB, cavalloB, torreB, reginaB, reB);
        this.pedoneB = pedoneB;
    }

    public ImageIcon getAlfiereB() {
        return new ImageIcon(alfiereB.getImage());
    }

    private void setAlfiereB(ImageIcon alfiereB) {
        controllaImmagini(alfiereB, pedoneW, cavalloW, torreW, reginaW, reW, pedoneB, alfiereW, cavalloB, torreB, reginaB, reB);
        this.alfiereB = alfiereB;
    }

    public ImageIcon getCavalloB() {
        return new ImageIcon(cavalloB.getImage());
    }

    private void setCavalloB(ImageIcon cavalloB) {
        controllaImmagini(cavalloB, alfiereW, pedoneW, torreW, reginaW, reW, pedoneB, alfiereB, cavalloW, torreB, reginaB, reB);
        this.cavalloB = cavalloB;
    }

    public ImageIcon getTorreB() {
        return new ImageIcon(torreB.getImage());
    }

    private void setTorreB(ImageIcon torreB) {
        controllaImmagini(torreB, alfiereW, cavalloW, pedoneW, reginaW, reW, pedoneB, alfiereB, cavalloB, torreW, reginaB, reB);
        this.torreB = torreB;
    }

    public ImageIcon getReginaB() {
        return new ImageIcon(reginaB.getImage());
    }

    private void setReginaB(ImageIcon reginaB) {
        controllaImmagini(reginaB, alfiereW, cavalloW, torreW, pedoneW, reW, pedoneB, alfiereB, cavalloB, torreB, reginaW, reB);
        this.reginaB = reginaB;
    }

    public ImageIcon getReB() {
        return new ImageIcon(reB.getImage());
    }

    private void setReB(ImageIcon reB) {
        controllaImmagini(reB, alfiereW, cavalloW, torreW, reginaW, pedoneW, pedoneB, alfiereB, cavalloB, torreB, reginaB, reW);
        this.reB = reB;
    }

    private static class Casella extends JPanel implements MouseListener {
        private Color colore;
        private final JLabel label;
        private int lunghezzaLato;
        private String id;
        private static String casellaSelezionata = null;
        private static final List<String> idUtilizzati = new ArrayList<>();
        private casellaClickListener listener;
        public boolean mossaValida;
        private static boolean sceltaPromozione = false;
        private static boolean scacchieraGirata = false;
        private static boolean gestisciGrafica = false;

        public Casella(Color colore, int lunghezzaLato, String id) {
            label = new JLabel();
            setLunghezzaLato(lunghezzaLato);
            setColore(colore);
            setId(id);
            this.setSize(new Dimension(lunghezzaLato, lunghezzaLato));
            this.add(label);
            this.addMouseListener(this);
            mossaValida = false;
        }

        public Casella(Color colore, int lunghezzaLato, String id, ImageIcon img) {
            this(colore, lunghezzaLato, id);
            setImg(img);
        }

        public Casella(Casella originale) {
            this.colore = originale.colore;
            this.lunghezzaLato = originale.lunghezzaLato;
            this.label = originale.label;
            this.id = originale.id;
            this.listener = originale.listener;
            this.mossaValida = originale.mossaValida;
        }

        public int getLunghezzaLato() {
            return lunghezzaLato;
        }

        private void setLunghezzaLato(int lunghezzaLato) {
            if (lunghezzaLato <= 0)
                throw new IllegalArgumentException("La lunghezza del lato deve essere maggiore di 0");
            this.lunghezzaLato = lunghezzaLato;
        }

        public Color getColore() {
            return colore;
        }

        private void setColore(Color colore) {
            this.colore = colore;
            this.setBackground(colore);
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

        public static String getIdCasellaSelezionata() {
            return casellaSelezionata;
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

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (listener != null) listener.casellaCliccata();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(5));
            disegnaCoordinata(g2d);
            g2d.setColor(Color.black);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (sceltaPromozione && this.id.equals("PROMOZIONE")) {
                g2d.setColor(new Color(0, 128, 200, 100));
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setColor(Color.black);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            else if (this.id.equals(casellaSelezionata)) g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            else if (mossaValida) {
                Composite old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                int offset;
                if (this.label.getIcon() != null) {
                    offset = lunghezzaLato / 25;
                    g2d.drawOval(offset, offset, lunghezzaLato - 2 * offset - 1, lunghezzaLato - 2 * offset - 1);
                }
                else {
                    offset = lunghezzaLato / 4;
                    g2d.fillOval(offset, offset, offset * 2, offset * 2);
                }
                g2d.setComposite(old);
            }
        }

        private void disegnaCoordinata(Graphics2D g2d) {
            if (!scacchieraGirata && (id.charAt(0) == 'A' || id.charAt(1) == '1') || scacchieraGirata && (id.charAt(0) == 'H' || id.charAt(1) == '8')) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = new Font("Arial", Font.BOLD, 20);
                g2d.setFont(font);
                Color a = new Color(240, 217, 181);
                Color b = new Color(161, 116, 79);
                if (colore.equals(a)) g2d.setColor(b);
                else g2d.setColor(a);

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
