import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleBox;

    public LoginUI() {
        setTitle("Smart Connect Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 30, 100, 30);
        add(roleLabel);

        String[] roles = { "student", "faculty" };
        roleBox = new JComboBox<>(roles);
        roleBox.setBounds(150, 30, 180, 30);
        add(roleBox);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 80, 100, 30);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 30);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 130, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 180, 30);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 180, 100, 30);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        setVisible(true);
    }

    private void handleLogin() {
        String role = ((String) roleBox.getSelectedItem()).trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        System.out.println("DEBUG → role=" + role + " user=" + username + " pass=" + password);

        String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartconnect",
                "root",
                "Yuvareddy@55"
            );
            PreparedStatement pst = conn.prepareStatement(sql);
        ) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    if (role.equals("student")) {
                        new StudentDashboard(username);
                        dispose();
                    } else {
                        new FacultyDashboard(username);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}