package jvm.jirc.client.ui.registerlogin;

import jvm.jirc.client.net.Connection;
import jvm.jirc.client.res.Res;
import jvm.jirc.client.ui.loading.LoadingWindow;
import jvm.jirc.client.ui.util.LabelComponent;
import jvm.jirc.client.util.Utils;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterLoginWindow extends JFrame implements ActionListener{

    private static RegisterLoginWindow instance;

    private static final String USER_EXCEPTIONS = "_";
    private static final String PASS_EXCEPTIONS = "_.!@#$%^&*-+";

    private static final int MIN_USER_LENGTH = 1;
    private static final int MAX_USER_LENGTH = 20;

    private static final int MIN_PASS_LENGTH = 5;
    private static final int MAX_PASS_LENGTH = 50;

    private static final int INVALID_USER = 1;
    private static final int INVALID_PASS = 2;
    private static final int USER_TAKEN = 3;

    private static final int GOOD = 4;
    private static final int ERROR = 5;

    private final JTextField userBox;
    private final JPasswordField passBox;

    private final JLabel statusLabel;

    private final JButton registerButton;
    private final JButton loginButton;

    public RegisterLoginWindow(){
        super("JIRC - Register/Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        userBox = new JTextField(15);

        passBox = new JPasswordField(15);

        final JPanel fields = new JPanel(new GridLayout(2, 1, 0, 2));
        fields.add(LabelComponent.create("Username:", 70, userBox));
        fields.add(LabelComponent.create("Password:", 70, passBox));

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setPreferredSize(new Dimension(320, statusLabel.getPreferredSize().height));

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        final JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(statusLabel);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(registerButton);
        buttons.add(Box.createHorizontalStrut(5));
        buttons.add(loginButton);

        add(new JLabel(Res.DOG_256), BorderLayout.NORTH);
        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
    }

    private void enableInput(final boolean enable){
        SwingUtilities.invokeLater(() -> {
            userBox.setEditable(enable);
            userBox.repaint();
            passBox.setEditable(enable);
            passBox.repaint();
            registerButton.setEnabled(enable);
            registerButton.repaint();
            loginButton.setEnabled(enable);
            loginButton.repaint();
        });
    }

    public void status(final String status, final Color color){
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
            statusLabel.setToolTipText(status);
            statusLabel.setForeground(color);
            statusLabel.revalidate();
            statusLabel.repaint();
        });
    }

    private void resetStatus(){
        status(null, null);
    }

    public void handleLoginResponse(final byte response){
        switch(response){
            case INVALID_USER:
                status("Username does not exist: " + userBox.getText(), Color.RED);
                break;
            case INVALID_PASS:
                status("Incorrect password for: " + userBox.getText(), Color.RED);
                break;
            case GOOD:
                resetStatus();
                Utils.hide(this);
                LoadingWindow.showNow();
                break;
            case ERROR:
                status("Error processing login request", Color.RED);
                break;
            default:
                status("Unexpected server response code. ID: " + response, Color.RED);
        }
    }

    public void handleRegisterResponse(final byte response){
        switch(response){
            case INVALID_USER:
                status("Invalid Username", Color.RED);
                break;
            case INVALID_PASS:
                status("Invalid Password", Color.RED);
                break;
            case USER_TAKEN:
                status("Username taken: " + userBox.getText(), Color.RED);
                break;
            case GOOD:
                status("Successfully registered: " + userBox.getText(), Color.GREEN);
                break;
            case ERROR:
                status("Error processing register request: " + userBox.getText(), Color.RED);
                break;
            default:
                status("Unexpected server response code. Try again.", Color.RED);
        }
    }

    public void actionPerformed(final ActionEvent e){
        resetStatus();
        final Object source = e.getSource();
        final String user = userBox.getText().trim();
        if(user.isEmpty()){
            status("Username cannot be empty!", Color.RED);
            return;
        }
        final String pass = new String(passBox.getPassword()).trim();
        if(pass.isEmpty()){
            status("Password cannot be empty!", Color.RED);
            return;
        }
        if(!Utils.isValidLength(user.length(), MIN_USER_LENGTH, MAX_USER_LENGTH)){
            status(String.format("Username length must be between %d and %d", MIN_USER_LENGTH, MAX_USER_LENGTH), Color.RED);
            return;
        }
        if(!Utils.isValidLength(pass.length(), MIN_PASS_LENGTH, MAX_PASS_LENGTH)){
            status(String.format("Password length must be between %d and %d", MIN_PASS_LENGTH, MAX_PASS_LENGTH), Color.RED);
            return;
        }
        if(!Utils.isValidChars(user, USER_EXCEPTIONS)){
            status("Username cannot contain symbols except for: " + USER_EXCEPTIONS, Color.RED);
            return;
        }
        if(!Utils.isValidChars(pass, PASS_EXCEPTIONS)){
            status("Password cannot contain symbols except for: " + PASS_EXCEPTIONS, Color.RED);
            return;
        }
        if(source.equals(registerButton)){
            new Thread(() -> {
                enableInput(false);
                if(Connection.register(user, pass)){
                    status("Awaiting response...", null);
                }else{
                    status("Error sending register request", Color.RED);
                }
                enableInput(true);
            }).start();
        }else if(source.equals(loginButton)){
            new Thread(() -> {
                enableInput(false);
                if(Connection.login(user, pass)){
                    status("Awaiting response...", null);
                }else{
                    status("Error sending login request", Color.RED);
                }
                enableInput(true);
            }).start();

        }
    }

    public static RegisterLoginWindow getInstance(){
        if(instance == null)
            instance = new RegisterLoginWindow();
        return instance;
    }

    public static void showNow(){
        Utils.show(getInstance());
    }
}
