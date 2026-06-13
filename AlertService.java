import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlertService {

    private final UserService userService = new UserService();

    // CRITICAL ISSUE (commit A): SQL injection. With the last-reviewed-commit
    // fix, the incremental review must compare against the marker and catch
    // this, even though a later commit (B) is the newest one in the push.
    public List<String> getAlertsForUser(String userId) {
        List<String> alerts = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT message FROM alerts WHERE user_id = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                alerts.add(rs.getString("message"));
            }
        } catch (Exception e) {
            System.out.println("alert error: " + e.getMessage());
        }
        return alerts;
    }
}
