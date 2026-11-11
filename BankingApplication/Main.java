public class Main {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("       BANKING APPLICATION");
        System.out.println("==================================");

        // Create Money objects
        Money openingBalanceA = new Money(1000000, "INR");
        Money openingBalanceB = new Money(500000, "INR");
        Money overdraftLimit = new Money(200000, "INR");

        // Create bank accounts
        BankAccount accountA = new BankAccount(
            "1001", openingBalanceA, overdraftLimit
        );

        BankAccount accountB = new BankAccount(
            "1002", openingBalanceB, overdraftLimit
        );

        System.out.println("\n--- Initial Account Details ---");
        System.out.println(accountA);
        System.out.println();
        System.out.println(accountB);

        // Deposit money
        System.out.println("\n--- Deposit Operation ---");
        accountA.deposit(new Money(500000, "INR"));

        // Withdraw money
        System.out.println("\n--- Withdrawal Operation ---");
        accountA.withdraw(new Money(200000, "INR"));

        // Transfer money
        System.out.println("\n--- Fund Transfer Operation ---");

        FundTransfer transfer = new FundTransfer(
            accountA,
            accountB,
            new Money(300000, "INR")
        );

        transfer.transfer();

        System.out.println(transfer);

        // Display updated balances
        System.out.println("\n--- Updated Account Balances ---");
        System.out.println("Account A: " + accountA.getBalance());
        System.out.println("Account B: " + accountB.getBalance());

        // Freeze account
        System.out.println("\n--- Freeze Account ---");
        accountA.freeze();

        try {
            accountA.withdraw(new Money(10000, "INR"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Unfreeze account
        System.out.println("\n--- Unfreeze Account ---");
        accountA.unfreeze();

        // Test overdraft limit
        System.out.println("\n--- Overdraft Test ---");

        try {
            accountA.withdraw(new Money(2000000, "INR"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Display transaction history
        System.out.println("\n--- Account A Transaction History ---");

        for (LedgerEntry entry : accountA.getLedger()) {
            System.out.println(entry);
            System.out.println("-----------------------------");
        }

        // Test zero amount
        System.out.println("\n--- Zero Amount Test ---");

        try {
            accountA.deposit(new Money(0, "INR"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test negative amount
        System.out.println("\n--- Negative Amount Test ---");

        try {
            accountA.withdraw(new Money(-50000, "INR"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test closed account
        System.out.println("\n--- Closed Account Test ---");

        BankAccount accountC = new BankAccount(
            "1003",
            new Money(0, "INR"),
            overdraftLimit
        );

        accountC.close();

        try {
            accountC.deposit(new Money(10000, "INR"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Display final account details
        System.out.println("\n--- Final Account Details ---");
        System.out.println(accountA);
        System.out.println();
        System.out.println(accountB);
        System.out.println();
        System.out.println(accountC);

        System.out.println("\n==================================");
        System.out.println("       APPLICATION COMPLETED");
        System.out.println("==================================");
    }
}