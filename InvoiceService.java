import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {

    private final UserService userService = new UserService();

    // HIGH-CONFIDENCE ISSUE: SQL injection via string concatenation.
    public List<String> getInvoicesForCustomer(String customerId) {
        List<String> invoices = new ArrayList<>();
        try {
            Connection conn = userService.connect();
            Statement stmt = conn.createStatement();
            String query = "SELECT invoice_no FROM invoices WHERE customer_id = '" + customerId + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                invoices.add(rs.getString("invoice_no"));
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        return invoices;
    }

    // LOW-CONFIDENCE BAIT: magic numbers and single-letter names.
    public double applyTax(double p, int z) {
        double v = p;
        if (z == 1) {
            v = p * 1.125;
        } else if (z == 2) {
            v = p * 1.075;
        } else if (z == 3) {
            v = p * 1.0;
        }
        if (v > 50000.0) {
            v = 50000.0;
        }
        return v;
    }

    // LOW-CONFIDENCE BAIT: redundant null check and pointless temp variable.
    public String normalizeInvoiceNumber(String invoiceNo) {
        if (invoiceNo == null) {
            return "";
        }
        if (invoiceNo != null && invoiceNo.length() > 20) {
            invoiceNo = invoiceNo.substring(0, 20);
        }
        String cleaned = invoiceNo.trim().toUpperCase();
        return cleaned;
    }

    // LOW-CONFIDENCE BAIT: console logging and unnecessary temp variable.
    public int countInvoices(String customerId) {
        List<String> items = getInvoicesForCustomer(customerId);
        int total = items.size();
        System.out.println("invoice count = " + total);
        return total;
    }
}
