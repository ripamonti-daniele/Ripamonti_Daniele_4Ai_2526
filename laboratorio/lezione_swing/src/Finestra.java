import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Finestra implements ActionListener {
    private final JFrame frame;
    private final JPanel panel;
    private final JTextField usernameTF;
    private final JTextField passwordTF;
    private final JButton loginBTN;
    private final JButton clearBTN;

    public Finestra(){
        frame = new JFrame();
        panel = new JPanel();

        usernameTF = new JTextField();
        passwordTF = new JTextField();
        usernameTF.setPreferredSize(new Dimension(300, 50));
        usernameTF.setMaximumSize(new Dimension(300, 50));
        passwordTF.setPreferredSize(new Dimension(300, 50));
        passwordTF.setMaximumSize(new Dimension(300, 50));

        loginBTN = new JButton("Login");
        loginBTN.addActionListener(this);
        clearBTN = new JButton("Clear");
        clearBTN.addActionListener(this);

        panel.add(usernameTF);
        panel.add(passwordTF);
        panel.add(loginBTN);
        panel.add(clearBTN);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBTN){
            String username = usernameTF.getText();
            String password = passwordTF.getText();
            if (username.equals("admin") && password.equals("admin")){
                JOptionPane.showMessageDialog(null, "Login effettuato con successo");
            }
            else {
                JOptionPane.showMessageDialog(null, "Credenziali non valide");
            }
        }
        else if (e.getSource() == clearBTN){
            usernameTF.setText("");
            passwordTF.setText("");
        }
    }
}
