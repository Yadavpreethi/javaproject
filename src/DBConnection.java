import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smartconnect", // your DB URL
                "root",                                     // your MySQL username
                "Yuvareddy@55"                        // your MySQL password
            );
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }
        return con;
    }
}
