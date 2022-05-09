package docsign.ui.guiscenes;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import docsign.ui.CommandLineUI;
import docsign.ui.GraphicUI;

import java.awt.event.MouseEvent;

public class Signup extends JPanel implements Scene {

    private JLabel nameLabel = new JLabel("Name");
    private JTextField nameField = new JTextField();

    private JLabel mailLabel = new JLabel("Email");
    private JTextField mailField = new JTextField();

    private JLabel passwordLabel = new JLabel("Password");
    private JPasswordField passwordField = new JPasswordField();

    private JButton signupButton = new JButton("Create Identity");

    public Signup() {
        super();
        setLayout(null);

        nameLabel.setBounds(10, 10, 80, 25);
        add(nameLabel);

        nameField.setBounds(100, 10, 300, 25);
        add(nameField);

        mailLabel.setBounds(10, 40, 80, 25);
        add(mailLabel);

        mailField.setBounds(100, 40, 300, 25);
        add(mailField);

        passwordLabel.setBounds(10, 70, 80, 25);
        add(passwordLabel);

        passwordField.setBounds(100, 70, 300, 25);
        add(passwordField);

        signupButton.setBounds(10, 100, 300, 25);
        add(signupButton);

        signupButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performSignup();
                }
            }
        );
    }

    private void performSignup() {
        String name = nameField.getText();
        String mail = mailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || mail.isEmpty() || password.isEmpty()) {
            return;
        }

        CommandLineUI cli = new CommandLineUI();
        cli.setDocumentPath(null);
        cli.setMailToVerify(null);
        cli.setDaysValidInt(0);
        cli.setDaysValid("0");
        cli.setPassword(password);
        cli.setMailAddress(mail);
        cli.setName(name);
        cli.setActionFlag(cli.ACTION_REGISTER);

        int result = cli.action();

        if (result == 0) {
            GraphicUI.loadScene(new MainMenu());
        }else{
            JOptionPane.showMessageDialog(null, "Signup failed: " + result);
        }
    }

    @Override
    public JPanel getScene() {
        return this;
    }
    
}
