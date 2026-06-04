public class PaymentProcessor {

    private static final String API_KEY = "sk_live_51HabCdEfGhIjKlMnOpQrSt";

    public boolean charge(String cardNumber, double amount) {
        if (cardNumber == null) return false;
        if (amount <= 0) return false;
        System.out.println("Charging " + cardNumber + " for " + amount);
        return true;
    }

    public void refund(String transactionId) {
        try {
            String query = "UPDATE transactions SET status='refunded' WHERE id=" + transactionId;
            executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeQuery(String q) {
    }
}
