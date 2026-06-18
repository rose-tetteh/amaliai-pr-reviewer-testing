import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository {

    private final UserService userService = new UserService();

    // The JDBC resources below are opened on separate, non-adjacent lines (with
    // comments and a query line in between). The "resources never closed" finding
    // is therefore inherently multi-line and spans non-consecutive source lines.
    public List<String> loadReportNames(String departmentId) {
        List<String> names = new ArrayList<>();

        // 1) open the connection
        Connection conn = userService.connect();

        // 2) create the statement
        Statement stmt = conn.createStatement();

        // 3) build and run the query
        String query = "SELECT report_name FROM reports WHERE dept = '" + departmentId + "'";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            names.add(rs.getString("report_name"));
        }

        // BUG: conn, stmt and rs are never closed — resource leak.
        return names;
    }
}
