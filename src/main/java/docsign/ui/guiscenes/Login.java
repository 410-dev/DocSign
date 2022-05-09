package docsign.ui.guiscenes;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import docsign.signutil.UserIdentity;
import docsign.ui.GraphicUI;

import java.awt.event.MouseEvent;


public class Login extends JPanel implements Scene {

    private JTextField emailField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    private JLabel emailLabel = new JLabel("Email");
    private JLabel passwordLabel = new JLabel("Password");

    private JButton loginButton = new JButton("Login");
    private JButton signupButton = new JButton("New ID");

    private JLabel errorLabel = new JLabel("");

    public Login() {
        super();
        setLayout(null);

        emailLabel.setBounds(10, 10, 80, 25);
        add(emailLabel);

        emailField.setBounds(100, 10, 300, 25);
        add(emailField);

        passwordLabel.setBounds(10, 40, 80, 25);
        add(passwordLabel);

        passwordField.addKeyListener(
            new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        performLogin();
                    }
                }
            }
        );

        passwordField.setBounds(100, 40, 300, 25);
        add(passwordField);

        loginButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performLogin();
                }
            }
        );

        loginButton.setBounds(10, 80, 80, 25);
        add(loginButton);

        signupButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    GraphicUI.loadScene(new Signup());
                }
            }
        );

        signupButton.setBounds(100, 80, 80, 25);
        add(signupButton);

        errorLabel.setBounds(10, 110, 280, 25);
        add(errorLabel);

        setVisible(true);
    }

    public void performLogin() {
        UserIdentity.login(emailField.getText(), new String(passwordField.getPassword()));

        if (!UserIdentity.isLoggedIn()) {
            errorLabel.setForeground(java.awt.Color.RED);
            errorLabel.setText("Login failed");
        } else {
            errorLabel.setText("");
            GraphicUI.loadScene(new MainMenu());
        }
    }

    @Override
    public JPanel getScene() {
        return this;
    }
    
}
