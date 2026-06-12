import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    private final UserService userService = new UserService();

    // HIGH-CONFIDENCE ISSUE: SQL built by string concatenation (injection).
    public List<String> getReportsForUser(String userId) {
        List<String> reports = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT title FROM reports WHERE owner_id = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reports.add(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return reports;
    }

    // LOW-CONFIDENCE BAIT: magic numbers, vague naming, redundant checks.
    public double calc(double a, int t) {
        double r = a;
        if (t == 1) {
            r = a * 0.85;
        } else if (t == 2) {
            r = a * 0.7;
        } else if (t == 3) {
            r = a * 0.55;
        }
        if (r > 9999.99) {
            r = 9999.99;
        }
        return r;
    }

    // LOW-CONFIDENCE BAIT: redundant null check and slightly awkward flow.
    public String formatReportTitle(String title) {
        if (title == null) {
            return "";
        }
        if (title != null && title.length() > 50) {
            title = title.substring(0, 50);
        }
        String result = title.trim();
        return result;
    }

    // LOW-CONFIDENCE BAIT: console logging and a temp variable that adds nothing.
    public int countReports(String userId) {
        List<String> data = getReportsForUser(userId);
        int n = data.size();
        System.out.println("count = " + n);
        return n;
    }
}
