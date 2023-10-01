
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// This is the controller for both login screen and register
public class LoginController {
    public void onLogin(ActionEvent e) {
        // if (e.getSource() == this.getBtnLogin()) {
        String username = Application.getInstance().getLoginScreen().getTxtUserName().getText().trim();
        String password = Application.getInstance().getLoginScreen().getTxtPassword().getText().trim();

        System.out.println("Login with username = " + username + " and password = " + password);
        User user = Application.getInstance().getDataAdapter().loadUser(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(null, "This user does not exist!");
        } else {
            Application.getInstance().setCurrentUser(user);
            Application.getInstance().getLoginScreen().setVisible(false);
            Application.getInstance().getMainScreen().setVisible(true);
        }
    }

    public void onReg(ActionEvent e) {
        // JOptionPane.showMessageDialog(null,"button clicked");
        // return;

        String username = Application.getInstance().getRegScreen().getTxtUserName().getText().trim();
        String password = Application.getInstance().getRegScreen().getTxtPassword().getText().trim();
        String name = Application.getInstance().getRegScreen().getTxtFullName().getText().trim();
        String email = Application.getInstance().getRegScreen().getTxtEmail().getText().trim();

        System.out.println(
                "Register with username = " + username + " and password = " + password + " " + name + " " + email);
        User user = new User();
        user.setUsername(username);
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(password);
        // return;
        if (Application.getInstance().getDataAdapter().AddUser(user)) {
            // If register is successful, log the user in
            Application.getInstance().getRegScreen().setVisible(false);
            Application.getInstance().getLoginScreen().setVisible(false);
            Application.getInstance().setCurrentUser(user);
            Application.getInstance().getMainScreen().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "username already exists!");
            return;
        }

        if (user == null) {
            JOptionPane.showMessageDialog(null, "This user does not exist!");
        } else {
            Application.getInstance().setCurrentUser(user);
            Application.getInstance().getLoginScreen().setVisible(false);
            Application.getInstance().getMainScreen().setVisible(true);
        }
    }

}
