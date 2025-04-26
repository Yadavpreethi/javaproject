import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class QuizUI extends JFrame {
    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JButton nextButton;

    // simple holder for one question
    static class Question {
        String text, a, b, c, d, correct;
        Question(String t, String a, String b, String c, String d, String corr) {
            text=t; this.a=a; this.b=b; this.c=c; this.d=d; correct=corr;
        }
    }

    public QuizUI(String username) {
        setTitle("Quiz Time – " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // load questions from DB
        loadQuestions();

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4,1));
        optionButtons = new JRadioButton[4];
        group = new ButtonGroup();
        for(int i=0; i<4; i++){
            optionButtons[i] = new JRadioButton();
            group.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> checkAndNext());

        displayQuestion();
        setVisible(true);
    }

    private void loadQuestions() {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartconnect","root","Yuvareddy@55");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions WHERE quiz_id=1")) {
            while(rs.next()){
                questions.add(new Question(
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayQuestion() {
        if(currentIndex < questions.size()){
            Question q = questions.get(currentIndex);
            questionLabel.setText("Q"+(currentIndex+1)+": "+q.text);
            optionButtons[0].setText(q.a);
            optionButtons[1].setText(q.b);
            optionButtons[2].setText(q.c);
            optionButtons[3].setText(q.d);
            group.clearSelection();
        } else {
            // quiz finished
            JOptionPane.showMessageDialog(this, "Quiz over! Your score: " + score + "/" + questions.size());
            dispose();
            new StudentDashboard(JOptionPane.showInputDialog("Enter your username to return:"));
        }
    }

    private void checkAndNext() {
        Question q = questions.get(currentIndex);
        String selected = null;
        for(int i=0;i<4;i++){
            if(optionButtons[i].isSelected()){
                selected = switch(i){
                    case 0 -> "A";
                    case 1 -> "B";
                    case 2 -> "C";
                    default -> "D";
                };
            }
        }
        if(selected!=null && selected.equalsIgnoreCase(q.correct)){
            score++;
        }
        currentIndex++;
        displayQuestion();
    }

    public static void main(String[] args) {
        new QuizUI("student1");
    }
}
