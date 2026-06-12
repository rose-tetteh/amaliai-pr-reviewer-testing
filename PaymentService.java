import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    private final Map<String, BigDecimal> balances = new HashMap<>();

    /**
     * Returns the current balance for the given account, or zero when the
     * account has no recorded transactions. (Harmless doc change — commit B.)
     */
    public BigDecimal getBalance(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        return balances.getOrDefault(accountId, BigDecimal.ZERO);
    }

    public void deposit(String accountId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balances.merge(accountId, amount, BigDecimal::add);
    }

    public boolean withdraw(String accountId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        BigDecimal balance = getBalance(accountId);
        if (balance.compareTo(amount) < 0) {
            return false;
        }
        balances.put(accountId, balance.subtract(amount));
        return true;
    }
}
