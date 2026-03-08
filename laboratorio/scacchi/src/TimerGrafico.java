import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimerGrafico extends JLabel {
    private final Timer timer;
    private int ore;
    private int minuti;
    private int secondi;
    private int guadagno;
    private boolean tempoScaduto;
    private boolean off;
    private boolean modificaTesto;
    private final Color sfondo;

    public TimerGrafico(int ore, int minuti, int secondi, int guadagno, Color sfondo, Color textColor) {
        setTimer(ore, minuti, secondi, guadagno);
        displayTimer();
        tempoScaduto = false;
        modificaTesto = false;
        this.sfondo = sfondo;
        setOpaque(false);
        setForeground(textColor);

        timer = new Timer(1000, (ActionEvent e) -> {
            TimerGrafico.this.secondi--;
            aggiorna();
            displayTimer();
        });
    }

    public void start() {
        if (!off && !timer.isRunning()) timer.start();
    }

    public void pause() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    public void resume() {
        start();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    public void setTimer(int ore, int minuti, int secondi, int guadagno) {
        this.ore = ore;
        this.minuti = minuti;
        this.secondi = secondi;
        this.guadagno = guadagno;

        if (ore > 23) this.ore = 23;
        if (minuti > 59) this.minuti = 59;
        if (secondi > 59) this.secondi = 59;
        if (guadagno > 60) this.guadagno = 60;

        if (ore < 0) this.ore = 0;
        if (minuti < 0) this.minuti = 0;
        if (secondi < 0) this.secondi = 0;
        if (guadagno < 0) this.guadagno = 0;

        off = (ore == 0 && minuti == 0 && secondi == 0);
    }

    private void aggiorna() {
        if (ore == 0 && minuti == 0 && secondi == -1) {
            secondi = 0;
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

        int arc = 20;
        g2d.setColor(sfondo);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2d.dispose();
        super.paintComponent(g);
    }
}
