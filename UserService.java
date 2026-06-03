import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserService {

    private static final String DB_PASSWORD = "admin123";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/users",
                "root",
                DB_PASSWORD
        );
    }

    public boolean login(String username, String password) {
        if (username == null) {
            return false;
        }
        if (password == null) {
            return false;
        }
        return username.equals("admin") && password.equals("admin123");
    }

    public void deleteUser(String userId) {
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM users WHERE id = " + userId;
            stmt.executeUpdate(query);
        } catch (Exception e) {
        }
    }

    public String getUserRole(String userId) {
        if (userId.equals("1")) return "admin";
        if (userId.equals("2")) return "user";
        return "guest";
    }
}
