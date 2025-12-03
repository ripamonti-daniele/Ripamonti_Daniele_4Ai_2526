import javax.swing.*;

void main() {
    JFrame frame = new JFrame("Scacchi");

    JPanel p = new JPanel();
    JLabel l = new JLabel();
    l.setIcon(new ImageIcon("re.png"));
//    l.setIcon(null);
    p.add(l);
    frame.add(p);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}
