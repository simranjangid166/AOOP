import java.time.Instant;

public class LedgerEntry {

    private final String transactionId;
    private final String transactionType;
    private final Money amount;
    private final Instant timestamp;

    // Constructor
    public LedgerEntry(String transactionId,
                       String transactionType,
                       Money amount) {

        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Invalid transaction ID");
        }

        if (transactionType == null || transactionType.isBlank()) {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = Instant.now();
    }

    // Getter methods
    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Money getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    // Display transaction details
    @Override
    public String toString() {

        return "Transaction ID: " + transactionId +
               "\nType: " + transactionType +
               "\nAmount: " + amount +
               "\nTimestamp: " + timestamp;
    }
}