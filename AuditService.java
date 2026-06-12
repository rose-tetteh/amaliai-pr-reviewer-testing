import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuditService {

    private final UserService userService = new UserService();

    // CRITICAL ISSUE (commit A): SQL injection — should be caught by an
    // incremental review, but the current bot only reviews the newest commit.
    public List<String> getAuditTrail(String accountId) {
        List<String> entries = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT action FROM audit_log WHERE account_id = '" + accountId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                entries.add(rs.getString("action"));
            }
        } catch (Exception e) {
            System.out.println("audit error: " + e.getMessage());
        }
        return entries;
    }

    public void purgeAuditTrail(String accountId) {
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM audit_log WHERE account_id = '" + accountId + "'";
            stmt.executeUpdate(query);
        } catch (Exception e) {
        }
    }
}
