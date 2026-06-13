import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<String> queue = new ArrayList<>();

    public void enqueue(String recipient, String message) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("recipient must not be blank");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        queue.add(recipient + ":" + message);
    }

    /**
     * Returns the number of notifications waiting to be delivered.
     * (Harmless doc change — commit B, the newest commit in the push.)
     */
    public int pendingCount() {
        return queue.size();
    }

    public List<String> drain() {
        List<String> pending = new ArrayList<>(queue);
        queue.clear();
        return pending;
    }
}
