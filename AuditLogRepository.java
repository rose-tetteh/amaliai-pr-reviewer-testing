import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuditLogRepository {

    private final UserService userService = new UserService();

    // Same shape as the repro: the JDBC resources are opened on non-adjacent
    // lines (comments and the query line sit between them), so the
    // "resources never closed" finding is inherently multi-line and spans
    // non-consecutive source lines. With the snippet-matching fix it should now
    // survive validation and post as its own inline comment.
    public List<String> loadAuditEntries(String accountId) {
        List<String> entries = new ArrayList<>();

        // 1) open the connection
        Connection conn = userService.connect();

        // 2) create the statement
        Statement stmt = conn.createStatement();

        // 3) build and run the query
        String query = "SELECT action FROM audit_log WHERE account_id = '" + accountId + "'";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            entries.add(rs.getString("action"));
        }

        // BUG: conn, stmt and rs are never closed — resource leak.
        return entries;
    }

    // CRITICAL: command injection. The user-supplied fileName is concatenated into
    // a shell command and executed. This is a 3-line block (build, exec, wait) and
    // the fix must replace all three lines together — e.g. a ProcessBuilder with a
    // fixed argument list and no shell string concatenation.
    public void exportAuditLog(String fileName) throws Exception {
        String command = "backup.sh " + fileName;
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }
}
