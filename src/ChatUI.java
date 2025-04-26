import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ChatUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private String username;

    public ChatUI(String username) {
        this.username = username;
        setTitle("Chat with Faculty – " + username);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        loadMessages();

        setVisible(true);
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/smartconnect", "root", "Yuvareddy@55");
                 PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO chat (sender, message) VALUES (?, ?)")) {
                ps.setString(1, username);
                ps.setString(2, msg);
                ps.executeUpdate();
                chatArea.append(username + ": " + msg + "\n");
                inputField.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadMessages() {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartconnect", "root", "Yuvareddy@55");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT sender, message FROM chat ORDER BY id")) {

            while (rs.next()) {
                chatArea.append(rs.getString("sender") + ": " + rs.getString("message") + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
