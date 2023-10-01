import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField txtUserName = new JTextField(10);
    private JTextField txtPassword = new JTextField(10);
    private JButton btnReg = new JButton("New Register");
    private JButton btnLogin = new JButton("Login");

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public LoginScreen() {

        this.setSize(300, 180);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.getContentPane().add(new JLabel("Store Management System"));

        JPanel main = new JPanel(new SpringLayout());
        main.add(new JLabel("Username:"));
        main.add(txtUserName);
        main.add(new JLabel("Password:"));
        main.add(txtPassword);

        SpringUtilities.makeCompactGrid(main,
                2, 2, // rows, cols
                6, 6, // initX, initY
                6, 6); // xPad, yPad

        JPanel buttons = new JPanel();
        buttons.add(btnReg);
        buttons.add(btnLogin);

        this.getContentPane().add(main);
        this.getContentPane().add(buttons);
        // this.getContentPane().add(btnLogin);
        btnReg.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Application.getInstance().getRegScreen().setVisible(true);
                    }
                });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().getLoginScreenCtrl().onLogin(e);
            }
        }

        );
    }

    // Below is the controller component of the
    private void onLogin(ActionEvent e) {
        if (e.getSource() == this.getBtnLogin()) {
            String username = this.getTxtUserName().getText().trim();
            String password = this.getTxtPassword().getText().trim();

            System.out.println("Login with username = " + username + " and password = " + password);
            User user = Application.getInstance().getDataAdapter().loadUser(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(null, "This user does not exist!");
            } else {
                Application.getInstance().setCurrentUser(user);
                this.setVisible(false);
                Application.getInstance().getMainScreen().setVisible(true);
            }
            // Add the username section to the mainscreen
            // JLabel userLabel = new JLabel("User: " + username);
            // userLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));
            // JPanel panelUser = new JPanel();
            // panelUser.add(userLabel);
            // Application.getInstance().getMainScreen().getContentPane().add(panelUser);
        }
    }
}
