import java.io.*;
import java.util.*;

public class ATM {
    private static final String FILE_PATH = "data/accounts.txt";
    private Map<String, Account> accounts;
    private Account currentAccount;
    private Scanner scanner;
    private List<String> transactionHistory;

    public ATM() {
        accounts = new HashMap<>();
        scanner = new Scanner(System.in);
        transactionHistory = new ArrayList<>();
        loadAccounts();
    }

    private void loadAccounts() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                createDefaultAccounts();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Account account = new Account(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]));
                    accounts.put(parts[0], account);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private void saveAccounts() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            for (Account account : accounts.values()) {
                writer.write(account.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private void createDefaultAccounts() {
        accounts.put("1001", new Account("1001", "John Doe", "1234", 5000.0));
        accounts.put("1002", new Account("1002", "Jane Smith", "5678", 10000.0));
        accounts.put("1003", new Account("1003", "Bob Johnson", "9012", 7500.0));
        saveAccounts();
    }

    public boolean login() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        Account account = accounts.get(accountNumber);
        if (account != null && account.getPin().equals(pin)) {
            currentAccount = account;
            System.out.println("Login successful. Welcome, " + account.getAccountHolderName());
            return true;
        } else {
            System.out.println("Invalid account number or PIN.");
            return false;
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Mini Statement");
            System.out.println("5. Change PIN");
            System.out.println("6. Logout");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    showMiniStatement();
                    break;
                case 5:
                    changePin();
                    break;
                case 6:
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void checkBalance() {
        currentAccount.checkBalance();
        transactionHistory.add("Balance Inquiry - Balance: " + currentAccount.getBalance());
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = getDoubleInput();

        if (amount > 0) {
            currentAccount.deposit(amount);
            saveAccounts();
            transactionHistory.add("Deposit - Amount: " + amount + " | New Balance: " + currentAccount.getBalance());
            System.out.println("New Balance: " + currentAccount.getBalance());
        } else {
            System.out.println("Invalid amount.");
        }
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = getDoubleInput();

        if (currentAccount.withdraw(amount)) {
            saveAccounts();
            transactionHistory.add("Withdrawal - Amount: " + amount + " | New Balance: " + currentAccount.getBalance());
            System.out.println("New Balance: " + currentAccount.getBalance());
        }
    }

    private void showMiniStatement() {
        System.out.println("Mini Statement");
        System.out.println("Account: " + currentAccount.getAccountNumber());
        System.out.println("Holder: " + currentAccount.getAccountHolderName());

        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions in this session.");
        } else {
            int count = 1;
            int start = Math.max(0, transactionHistory.size() - 5);
            for (int i = start; i < transactionHistory.size(); i++) {
                System.out.println(count++ + ". " + transactionHistory.get(i));
            }
        }

        System.out.println("Current Balance: " + currentAccount.getBalance());
    }

    private void changePin() {
        System.out.print("Enter current PIN: ");
        String oldPin = scanner.nextLine();

        if (!oldPin.equals(currentAccount.getPin())) {
            System.out.println("Incorrect PIN.");
            return;
        }

        System.out.print("Enter new PIN (4 digits): ");
        String newPin = scanner.nextLine();

        if (newPin.length() != 4 || !newPin.matches("\\d+")) {
            System.out.println("PIN must be 4 digits.");
            return;
        }

        System.out.print("Confirm new PIN: ");
        String confirmPin = scanner.nextLine();

        if (newPin.equals(confirmPin)) {
            currentAccount.setPin(newPin);
            saveAccounts();
            System.out.println("PIN changed successfully.");
            transactionHistory.add("PIN Changed");
        } else {
            System.out.println("PINs do not match.");
        }
    }

    private void logout() {
        System.out.println("Logging out. Thank you for using the ATM.");
        System.out.println("Account: " + currentAccount.getAccountNumber());
        System.out.println("Final Balance: " + currentAccount.getBalance());
        currentAccount = null;
        transactionHistory.clear();
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid amount: ");
            }
        }
    }
}
