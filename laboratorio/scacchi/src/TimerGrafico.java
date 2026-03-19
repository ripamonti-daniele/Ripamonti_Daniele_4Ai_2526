import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimerGrafico extends JLabel {
    private final Timer timer;
    private long millesimi;
    private long ultimoTick;
    private int ore;
    private int minuti;
    private int secondi;
    private int guadagno;
    private int oreDefault;
    private int minutiDefault;
    private int secondiDefault;
    private boolean tempoScaduto;
    private boolean off;
    private boolean modificaTesto;
    private final Color sfondo;

    public TimerGrafico(int ore, int minuti, int secondi, int guadagno, Color sfondo, Color textColor) {
        inizializzaTimer(ore, minuti, secondi, guadagno);
        displayTimer();
        tempoScaduto = false;
        modificaTesto = false;
        this.sfondo = sfondo;
        setOpaque(false);
        setForeground(textColor);
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);

        timer = new Timer(1, (ActionEvent e) -> {
            millesimi += System.currentTimeMillis() - ultimoTick;
            if (millesimi >= 1000) {
                aggiorna();
                millesimi -= 1000;
            }
            ultimoTick = System.currentTimeMillis();
        });
    }

    private void inizializzaTimer(int ore, int minuti, int secondi, int guadagno) {
        oreDefault = ore;
        minutiDefault = minuti;
        secondiDefault = secondi;
        this.guadagno = guadagno;

        if (ore > 23) oreDefault = 23;
        if (minuti > 59) minutiDefault = 59;
        if (secondi > 59) secondiDefault = 59;
        if (guadagno > 60) this.guadagno = 60;

        if (ore < 0) oreDefault = 0;
        if (minuti < 0) minutiDefault = 0;
        if (secondi < 0) secondiDefault = 0;
        if (guadagno < 0) this.guadagno = 0;

        this.ore = oreDefault;
        this.minuti = minutiDefault;
        this.secondi = secondiDefault;

        off = (oreDefault == 0 && minutiDefault == 0 && secondiDefault == 0);
    }

    private void setTimer(int ore, int minuti, int secondi, int guadagno) {
        reset();
        inizializzaTimer(ore, minuti, secondi, guadagno);
    }

    private void aggiorna() {
        secondi--;
        if (ore == 0 && minuti == 0 && secondi == 0) {
            timer.stop();
            tempoScaduto = true;
        }

        if (secondi == -1) {
            secondi = 59;
            minuti--;
        }
        if (minuti == -1) {
            minuti = 59;
            ore--;
        }
        displayTimer();
    }

    public void start() {
        if (!off && !timer.isRunning()) {
            ultimoTick = System.currentTimeMillis();
            timer.start();
        }
    }

    public void pause() {
        if (timer.isRunning()) {
            timer.stop();
            sommaGuadagno();
        }
    }

    public void reset() {
        pause();
        millesimi = 0;
        ore = oreDefault;
        minuti = minutiDefault;
        secondi = secondiDefault;
        if (!off) {
            tempoScaduto = false;
            displayTimer();
        }
    }

    public void invertiStato() {
        if (!off) {
            if (timer.isRunning()) {
                timer.stop();
                sommaGuadagno();
            }
            else {
                ultimoTick = System.currentTimeMillis();
                timer.start();
            }
        }
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    public boolean isTempoScaduto() {
        return tempoScaduto;
    }

    public boolean isOff() {
        return off;
    }

    public void sommaGuadagno() {
        if (tempoScaduto || guadagno == 0) return;
        if (guadagno == 60) minuti++;
        else {
            secondi += guadagno;
            if (secondi > 59) {
                minuti++;
                secondi -= 59;
            }
        }
        if (minuti > 59) {
            ore++;
            minuti -= 59;
        }
        if (ore > 23) {
            ore = 23;
            minuti = 59;
            secondi = 59;
        }
        displayTimer();
    }

    private void displayTimer() {
        String s = "";
        if (ore >= 10) s = ore + ":";
        else if (ore > 0) s = "0" + ore + ":";
        if (minuti < 10) s += "0";
        s += minuti + ":";
        if (secondi < 10) s += "0";
        s += String.valueOf(secondi);
        modificaTesto = true;
        setText(s);
        modificaTesto = false;
    }

    @Override
    public void setText(String s) {
        if (modificaTesto) super.setText(s);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(sfondo);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2d.dispose();
        super.paintComponent(g);
    }
}
