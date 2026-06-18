import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    // CRITICAL: hardcoded credential committed to source.
    private static final String ADMIN_TOKEN = "super-secret-admin-token-123";

    private final UserService userService = new UserService();

    // CRITICAL: SQL injection — userId concatenated straight into the query.
    // HIGH: Connection/Statement/ResultSet are never closed (resource leak).
    public List<String> getAccountsForUser(String userId) {
        List<String> accounts = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT account_no FROM accounts WHERE user_id = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                accounts.add(rs.getString("account_no"));
            }
        } catch (Exception e) {
            // HIGH: exception swallowed and written to stdout.
            System.out.println("account error: " + e.getMessage());
        }
        return accounts;
    }

    // LOW-VALUE NITPICKS below — these should NOT clutter the inline review.

    // magic numbers + single-letter parameter names
    public double fee(double a, int t) {
        double r = a;
        if (t == 1) {
            r = a * 0.02;
        } else if (t == 2) {
            r = a * 0.015;
        }
        if (r > 250.0) {
            r = 250.0;
        }
        return r;
    }

    // redundant null check + pointless temp variable
    public String normalize(String name) {
        if (name == null) {
            return "";
        }
        if (name != null && name.length() > 30) {
            name = name.substring(0, 30);
        }
        String cleaned = name.trim();
        return cleaned;
    }

    // console logging + unnecessary temp variable
    public int countAccounts(String userId) {
        List<String> data = getAccountsForUser(userId);
        int n = data.size();
        System.out.println("account count = " + n);
        return n;
    }
}
