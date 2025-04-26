import java.awt.*;
import javax.swing.*;

public class FacultyDashboard extends JFrame {
    private JButton createQuizButton;
    private JButton uploadMaterialsButton;
    private JButton viewMessagesButton;
    private JButton logoutButton;

    public FacultyDashboard(String username) {
        setTitle("Faculty Dashboard – " + username);
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, Prof. " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBounds(50, 20, 400, 30);
        add(welcomeLabel);

        createQuizButton = new JButton("Create Quiz");
        createQuizButton.setBounds(50, 80, 150, 40);
        add(createQuizButton);

        uploadMaterialsButton = new JButton("Upload Materials");
        uploadMaterialsButton.setBounds(50, 140, 150, 40);
        add(uploadMaterialsButton);

        viewMessagesButton = new JButton("Reply to Doubts");
        viewMessagesButton.setBounds(50, 200, 150, 40);
        add(viewMessagesButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(50, 260, 150, 40);
        add(logoutButton);

        // placeholder actions
        createQuizButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open quiz-creation screen...");
        });
        uploadMaterialsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open material-upload screen...");
        });
        viewMessagesButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open reply-to-students screen...");
        });
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        setVisible(true);
    }

    // Optional standalone test
    public static void main(String[] args) {
        new FacultyDashboard("faculty1");
    }
}