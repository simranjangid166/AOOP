public class FundTransfer {

    private final String transferId;
    private final BankAccount source;
    private final BankAccount destination;
    private final Money amount;

    private String status;

    // Constructor
    public FundTransfer(BankAccount source,
                        BankAccount destination,
                        Money amount) {

        if (source == null || destination == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }

        if (source == destination) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        this.transferId = java.util.UUID.randomUUID().toString();
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.status = "PENDING";
    }

    // Transfer money
    public synchronized void transfer() {

        if (!status.equals("PENDING")) {
            throw new IllegalStateException("Transfer already processed");
        }

        synchronized (FundTransfer.class) {

            if (source.getStatus() != AccountStatus.ACTIVE ||
                destination.getStatus() != AccountStatus.ACTIVE) {

                throw new IllegalStateException("Both accounts must be active");
            }

            if (!source.getBalance().getCurrency().equals(amount.getCurrency()) ||
                !destination.getBalance().getCurrency().equals(amount.getCurrency())) {

                throw new IllegalArgumentException("Currency mismatch");
            }

            // Check source overdraft limit
            Money newSourceBalance = source.getBalance().subtract(amount);

            long minimumBalance =
                -source.getOverdraftLimit().getAmountInPaise();

            if (newSourceBalance.getAmountInPaise() < minimumBalance) {
                throw new IllegalStateException("Insufficient balance");
            }

            // Check destination balance overflow
            destination.getBalance().add(amount);

            boolean moneyDebited = false;

            try {

                // Deduct money from source
                source.withdraw(amount);
                moneyDebited = true;

                // Add money to destination
                destination.deposit(amount);

                status = "COMPLETED";

                System.out.println("Transfer successful");

            } catch (RuntimeException e) {

                // Attempt to refund source if destination credit fails
                if (moneyDebited) {
                    try {
                        source.deposit(amount);
                    } catch (RuntimeException refundError) {
                        e.addSuppressed(refundError);
                    }
                }

                status = "FAILED";
                throw e;
            }
        }
    }

    // Getter methods
    public String getTransferId() {
        return transferId;
    }

    public BankAccount getSource() {
        return source;
    }

    public BankAccount getDestination() {
        return destination;
    }

    public Money getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    // Display transfer details
    @Override
    public String toString() {

        return "Transfer ID: " + transferId +
               "\nSource Account: " + source.getAccountNumber() +
               "\nDestination Account: " + destination.getAccountNumber() +
               "\nAmount: " + amount +
               "\nStatus: " + status;
    }
}