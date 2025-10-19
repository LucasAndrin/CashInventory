import java.util.Scanner;

import model.CashInventory;
import model.Denomination;
import model.strategy.PreserveLowerNotesStrategy;
import service.ATMService;
import service.AccountService;
import service.ConsoleNotificationService;
import service.FileNotificationService;
import service.NotificationService;
import service.WithdrawalService;

public class ATMConsoleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // === Setup ===
        AccountService accountService = new AccountService();
        accountService.createAccount("A123", 500.0);
        accountService.createAccount("B456", 150.0);

        CashInventory inventory = new CashInventory();
        inventory.add(new Denomination(2.0), 10);
        inventory.add(new Denomination(5.0), 10);
        inventory.add(new Denomination(10.0), 5);
        inventory.add(new Denomination(50.0), 2);

        NotificationService notificationService = new NotificationService();
        notificationService.register(new ConsoleNotificationService());
        notificationService.register(new FileNotificationService(".log"));

        WithdrawalService withdrawalService = new WithdrawalService(inventory);
        withdrawalService.setStrategy(new PreserveLowerNotesStrategy());
        withdrawalService.setNotificationService(notificationService);

        ATMService atm = new ATMService(accountService, withdrawalService, notificationService, inventory);

        System.out.println("====================================");
        System.out.println("      Welcome to the ATM System     ");
        System.out.println("====================================");

        while (true) {
            System.out.print("\nEnter your account ID (or 'exit' to quit): ");
            String accountId = scanner.nextLine().trim();

            if (accountId.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using our ATM!");
                break;
            }

            try {
                // Authenticate user
                var account = accountService.authenticate(accountId);
                System.out.printf("Welcome, %s! Your current balance is $%.2f%n", account.getId(), account.getBalance());

                // Menu loop
                boolean loggedIn = true;
                while (loggedIn) {
                    System.out.println("\n==== ATM MENU ====");
                    System.out.println("1. Check balance");
                    System.out.println("2. Withdraw cash");
                    System.out.println("3. View cash inventory (admin/debug)");
                    System.out.println("4. Logout");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            System.out.printf("Your current balance is: $%.2f%n", account.getBalance());
                            break;
                        case "2":
                            System.out.print("Enter amount to withdraw: ");
                            try {
                                double amount = Double.parseDouble(scanner.nextLine());
                                atm.withdraw(accountId, amount);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid amount format.");
                            }
                            break;
                        case "3":
                            inventory.printInventory();
                            break;
                        case "4":
                            System.out.println("Logging out...");
                            loggedIn = false;
                            break;
                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
