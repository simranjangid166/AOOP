import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankAccount {

    private final String accountNumber;
    private Money balance;
    private final Money overdraftLimit;
    private AccountStatus status;

    private final List<LedgerEntry> ledger = new ArrayList<>();

    // Constructor
    public BankAccount(String accountNumber,
                       Money openingBalance,
                       Money overdraftLimit) {

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid account number");
        }

        if (openingBalance == null || overdraftLimit == null) {
            throw new IllegalArgumentException("Money values cannot be null");
        }

        if (overdraftLimit.getAmountInPaise() < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }

        if (!openingBalance.getCurrency().equals(overdraftLimit.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }

        Money minimumBalance = new Money(
            -overdraftLimit.getAmountInPaise(),
            overdraftLimit.getCurrency()
        );

        if (openingBalance.getAmountInPaise() <
            minimumBalance.getAmountInPaise()) {
            throw new IllegalArgumentException("Opening balance exceeds overdraft limit");
        }

        this.accountNumber = accountNumber;
        this.balance = openingBalance;
        this.overdraftLimit = overdraftLimit;
        this.status = AccountStatus.ACTIVE;
    }

    // Check whether account is active
    private void checkActive() {

        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
    }

    // Validate transaction amount
    private void validateAmount(Money amount) {

        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (!balance.getCurrency().equals(amount.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }

    // Deposit money
    public synchronized void deposit(Money amount) {

        checkActive();
        validateAmount(amount);

        Money newBalance = balance.add(amount);

        LedgerEntry entry = new LedgerEntry(
            UUID.randomUUID().toString(),
            "DEPOSIT",
            amount
        );

        balance = newBalance;
        ledger.add(entry);

        System.out.println("Deposit successful");
    }

    // Withdraw money
    public synchronized void withdraw(Money amount) {

        checkActive();
        validateAmount(amount);

        Money newBalance = balance.subtract(amount);

        long minimumBalance = -overdraftLimit.getAmountInPaise();

        if (newBalance.getAmountInPaise() < minimumBalance) {
            throw new IllegalStateException("Overdraft limit exceeded");
        }

        LedgerEntry entry = new LedgerEntry(
            UUID.randomUUID().toString(),
            "WITHDRAWAL",
            amount
        );

        balance = newBalance;
        ledger.add(entry);

        System.out.println("Withdrawal successful");
    }

    // Freeze account
    public synchronized void freeze() {

        checkActive();
        status = AccountStatus.FROZEN;

        System.out.println("Account frozen");
    }

    // Unfreeze account
    public synchronized void unfreeze() {

        if (status != AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is not frozen");
        }

        status = AccountStatus.ACTIVE;

        System.out.println("Account unfrozen");
    }

    // Close account
    public synchronized void close() {

        checkActive();

        if (balance.getAmountInPaise() != 0) {
            throw new IllegalStateException("Balance must be zero to close account");
        }

        status = AccountStatus.CLOSED;

        System.out.println("Account closed");
    }

    // Getter methods
    public synchronized String getAccountNumber() {
        return accountNumber;
    }

    public synchronized Money getBalance() {
        return balance;
    }

    public synchronized Money getOverdraftLimit() {
        return overdraftLimit;
    }

    public synchronized AccountStatus getStatus() {
        return status;
    }

    public synchronized List<LedgerEntry> getLedger() {
        return List.copyOf(ledger);
    }

    // Display account details
    @Override
    public synchronized String toString() {

        return "Account Number: " + accountNumber +
               "\nBalance: " + balance +
               "\nOverdraft Limit: " + overdraftLimit +
               "\nStatus: " + status;
    }
}