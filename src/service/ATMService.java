package service;

import java.util.Map;

import exceptions.InsufficientFundsException;
import model.Account;
import model.CashInventory;
import model.Denomination;
import model.strategy.WithdrawalStrategyContract;

public class ATMService {
    private final AccountService accountService;
    private final WithdrawalService withdrawalService;
    private final NotificationService notificationService;
    private final CashInventory inventory;

    public ATMService(AccountService accountService, WithdrawalService withdrawalService, NotificationService notificationService, CashInventory inventory) {
        this.accountService = accountService;
        this.withdrawalService = withdrawalService;
        this.notificationService = notificationService;
        this.inventory = inventory;
    }

    public void withdraw(String accountId, double amount) {
        try {
            Account account = accountService.authenticate(accountId);

            if (account.getBalance() < amount) {
                amount = account.getBalance();
                throw new InsufficientFundsException("Insufficient balance");
            }

            // Check if it's possible to compose the amount with the invetory
            Map<Denomination, Integer> composition = withdrawalService.getStrategy().compose(amount, inventory.getInventorySnapshot());

            // Deduct from account and inventory
            account.withdraw(amount);
            withdrawalService.withdraw(amount, composition);

            notificationService.notifyAll(String.format(
                    "Account %s withdrew $%.2f successfully.", accountId, amount));
            notificationService.notifyAll(account.toString());
        } catch (InsufficientFundsException e) {
            double suggestion = withdrawalService.findClosestAvailableAmount(amount);
            String message = String.format("Withdrawal failed: %s.", e.getMessage());
            if (suggestion > 0)
                message = String.format("%s You can withdraw $%.2f instead.", message, suggestion);

            notificationService.notifyAll(message);
        } catch (Exception e) {
            notificationService.notifyAll("Withdrawal failed: " + e.getMessage());
        }
    }

    public void showAccounts() {
        accountService.showAccounts();
    }

    public void loadCash(Denomination d, int qty) {
        inventory.add(d, qty);
    }

    public WithdrawalStrategyContract getStrategy() {
        return withdrawalService.getStrategy();
    }
}
