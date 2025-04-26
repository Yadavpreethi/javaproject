import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StudentDashboard extends JFrame {
    private String username;
    
    // Components for “Home” tab
    private JButton takeQuizButton, chatButton, logoutButton;
    
    // Components for “Materials” tab
    private DefaultListModel<String> matModel;
    private JList<String> matList;
    private List<String> matPaths;
    private JButton openMaterialButton;

    public StudentDashboard(String username) {
        this.username = username;
        
        setTitle("Student Dashboard – " + username);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Home", createHomePanel());
        tabs.addTab("Materials", createMaterialsPanel());
        
        add(tabs);
        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel p = new JPanel();
        p.setLayout(null);

        JLabel welcome = new JLabel("Welcome, " + username + "!");
        welcome.setFont(new Font("Arial", Font.BOLD, 18));
        welcome.setBounds(20, 20, 300, 30);
        p.add(welcome);

        takeQuizButton = new JButton("Take Quiz");
        takeQuizButton.setBounds(20, 70, 140, 40);
        p.add(takeQuizButton);

        chatButton = new JButton("Chat with Faculty");
        chatButton.setBounds(20, 130, 140, 40);
        p.add(chatButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(20, 190, 140, 40);
        p.add(logoutButton);

        // Actions
        takeQuizButton.addActionListener(e -> {
            dispose();
            new QuizUI(username);
        });
        chatButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Open chat window...");
        });
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        return p;
    }

    private JPanel createMaterialsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        
        matModel = new DefaultListModel<>();
        matList  = new JList<>(matModel);
        matPaths = new ArrayList<>();
        
        // load from DB
        try (Connection con = DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/smartconnect","root","Yuvareddy@55");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title, filepath FROM materials")) {
            
            while (rs.next()) {
                matModel.addElement(rs.getString("title"));
                matPaths.add(rs.getString("filepath"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        p.add(new JScrollPane(matList), BorderLayout.CENTER);

        openMaterialButton = new JButton("Open Selected");
        openMaterialButton.addActionListener(e -> {
            int idx = matList.getSelectedIndex();
            if (idx >= 0) {
                try {
                    Desktop.getDesktop().open(new File(matPaths.get(idx)));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Cannot open: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a material first");
            }
        });
        p.add(openMaterialButton, BorderLayout.SOUTH);

        return p;
    }

    public static void main(String[] args) {
        new StudentDashboard("student1");
    }
}