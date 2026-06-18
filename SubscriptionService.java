import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {

    private final UserService userService = new UserService();

    // Clear, high-confidence issues so the bot is guaranteed to produce a review.
    // The point of this PR is to confirm the unified API round-trips and that the
    // OpenAI-format response is parsed correctly (the Claude-via-gateway path).
    public List<String> getSubscriptions(String customerId) {
        List<String> subs = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT plan FROM subscriptions WHERE customer_id = '" + customerId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                subs.add(rs.getString("plan"));
            }
        } catch (Exception e) {
            System.out.println("subscription error: " + e.getMessage());
        }
        return subs;
    }

    public boolean isActive(String customerId) {
        return getSubscriptions(customerId).size() > 0;
    }
}
