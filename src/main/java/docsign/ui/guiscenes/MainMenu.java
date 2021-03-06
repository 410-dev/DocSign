package docsign.ui.guiscenes;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;

import docsign.signutil.UserIdentity;
import docsign.ui.CommandLineUI;
import docsign.ui.GraphicUI;

import java.awt.event.MouseEvent;

public class MainMenu extends JPanel implements Scene {

    private JButton selectFile = new JButton("Select File...");
    private JTextField filePath = new JTextField();

    private JLabel emailLabel = new JLabel("Email to Verify");
    private JTextField emailField = new JTextField();

    private JLabel daysValidLabel = new JLabel("Days Valid");
    private JTextField daysValidField = new JTextField();

    private JLabel miniNoteLabel = new JLabel("Mini Note");
    private JTextField miniNoteField = new JTextField();

    private JButton signButton = new JButton("Sign");
    private JButton verifyButton = new JButton("Verify");
    private JButton readSignButton = new JButton("Read Sign");

    private JScrollPane scrollPane = new JScrollPane();
    private JTextArea output = new JTextArea();

    private static String password = "";

    public static void setPassword(String pw) {
        password = pw;
    }

    public MainMenu() {
        super();
        setLayout(null);

        selectFile.setBounds(10, 10, 150, 25);
        add(selectFile);

        filePath.setBounds(180, 10, 300, 25);
        add(filePath);

        emailLabel.setBounds(10, 50, 150, 25);
        add(emailLabel);

        emailField.setBounds(180, 50, 300, 25);
        add(emailField);

        daysValidLabel.setBounds(10, 90, 180, 25);
        add(daysValidLabel);

        daysValidField.setBounds(180, 90, 300, 25);
        daysValidField.setText("30");
        add(daysValidField);

        miniNoteLabel.setBounds(10, 130, 180, 25);
        add(miniNoteLabel);

        miniNoteField.setBounds(180, 130, 300, 25);
        add(miniNoteField);

        signButton.setBounds(10, 170, 150, 25);
        add(signButton);

        verifyButton.setBounds(160, 170, 150, 25);
        add(verifyButton);

        readSignButton.setBounds(310, 170, 150, 25);
        add(readSignButton);

        output.setEditable(false);
        output.setLineWrap(true);
        scrollPane = new JScrollPane(output);
        scrollPane.setBounds(10, 210, 465, 550);
        add(scrollPane);

        signButton.setEnabled(false);
        verifyButton.setEnabled(false);
        readSignButton.setEnabled(false);

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (emailField.getText().length() > 0) {
                        verifyButton.setEnabled(true);
                        readSignButton.setEnabled(true);
                    }else{
                        verifyButton.setEnabled(false);
                        readSignButton.setEnabled(false);
                    }
                }
            }
        };   
        
        t.start();

        selectFile.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performSelectFile();
                }
            }
        );

        signButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performSign();
                }
            }
        );

        verifyButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performVerify();
                }
            }
        );

        readSignButton.addMouseListener(
            new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    performReadSign();
                }
            }
        );

        GraphicUI.frame.setSize(GraphicUI.frame.getWidth(), 800);
    }

    private void performSelectFile() {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            filePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }

        fileChooser.setVisible(false);
        signButton.setEnabled(true);
    }

    private void performSign() {
        CommandLineUI cli = new CommandLineUI();
        cli.setActionFlag(cli.ACTION_WRITESIGN);
        cli.setMininote(miniNoteField.getText());
        actionPerform(cli);
    }

    private void performVerify() {
        CommandLineUI cli = new CommandLineUI();
        cli.setActionFlag(cli.ACTION_VERIFY);
        actionPerform(cli);
    }

    private void performReadSign() {
        CommandLineUI cli = new CommandLineUI();
        cli.setActionFlag(cli.ACTION_READSIGN);
        actionPerform(cli);
    }

    private void actionPerform(CommandLineUI cli) {
        cli.setDocumentPath(filePath.getText());
        cli.setMailToVerify(emailField.getText());
        cli.setDaysValidInt(Integer.parseInt(daysValidField.getText()));
        cli.setDaysValid(daysValidField.getText());
        cli.setPassword(password);
        cli.setMailAddress(UserIdentity.getCurrentIdentity().getEmail());

        int result = cli.action();
        
        
        if (cli.output.contains("Signature is not valid.") || cli.output.contains(" is null") || cli.output.contains("Invalid") || cli.output.contains("Signature not found.")) {
            output.setForeground(java.awt.Color.RED);
        } else if (cli.output.contains("Signature is valid.")) {
            output.setForeground(new java.awt.Color(0, 100, 0, 255));
        } else {
            output.setForeground(java.awt.Color.BLACK);
        }

        switch(result) {
            case 0:
                break;

            case 1:
                cli.output = "Signature not found.";
                break;

            case 2:
                // cli.output = "Signature is not valid.";
                break;

            case 255:
                cli.output = "An error occurred wile processing. Perhaps the author is not a valid user?";
                break;

            default:
                cli.output = "Unknown action: " + result;
                break;
        }

        // output.setText("<html>" + cli.output.replace("\n", "<br>") + "</html>");
        output.setText(cli.output);
    }

    @Override
    public JPanel getScene() {
        return this;
    }
    
}
