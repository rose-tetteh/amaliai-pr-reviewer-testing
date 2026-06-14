import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final UserService userService = new UserService();

    // HIGH-CONFIDENCE ISSUE: SQL injection. This line never changes across the
    // PR's commits, so it should be commented exactly once. A re-review caused
    // by an unrelated edit elsewhere in the file must NOT repeat this comment.
    public List<String> getOrdersForCustomer(String customerId) {
        List<String> orders = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT order_no FROM orders WHERE customer_id = '" + customerId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                orders.add(rs.getString("order_no"));
            }
        } catch (Exception e) {
            System.out.println("order error: " + e.getMessage());
        }
        return orders;
    }

    // UNRELATED ADDITION (commit B): a brand-new method appended at the bottom.
    // It does not touch the SQL injection line above. A correct reviewer should
    // comment on this new code only — NOT repeat the existing SQL injection comment.
    public int countOrders(String customerId) {
        return getOrdersForCustomer(customerId).size();
    }
}
