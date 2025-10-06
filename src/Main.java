import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Mini ATM System");

        ATM atm = new ATM();

        System.out.println("Default Test Accounts:");
        System.out.println("Account: 1001 | PIN: 1234 | Balance: 5000");
        System.out.println("Account: 1002 | PIN: 5678 | Balance: 10000");
        System.out.println("Account: 1003 | PIN: 9012 | Balance: 7500");

        while (true) {
            if (atm.login()) {
                atm.showMenu();
            }

            System.out.print("Do you want to perform another transaction? (y/n): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                System.out.println("Thank you for using Mini ATM System!");
                break;
            }
        }

        scanner.close();
    }
}
