package service;

import java.util.Map;

import exceptions.InsufficientFundsException;
import model.CashInventory;
import model.Denomination;
import model.strategy.WithdrawalStrategyContract;

public class WithdrawalService {
    private final CashInventory inventory;
    private WithdrawalStrategyContract strategy;
    private NotificationService notificationService;

    public WithdrawalService(CashInventory inventory) {
        this.inventory = inventory;
    }

    public WithdrawalStrategyContract getStrategy() {
        return strategy;
    }

    public void setStrategy(WithdrawalStrategyContract strategy) {
        this.strategy = strategy;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    public void withdraw(double amount, Map<Denomination, Integer> composition) throws InsufficientFundsException {
        if (amount > inventory.getTotalValue()) {
            throw new InsufficientFundsException("ATM has insufficient cash for this operation.");
        }

        /** Remove used notes from the inventory */
        for (Map.Entry<Denomination, Integer> e : composition.entrySet()) {
            inventory.remove(e.getKey(), e.getValue());
        }

        String successMsg = String.format(
            "Withdrawal of $%.2f successful using %s.",
            amount, strategy.getClass().getSimpleName()
        );

        notificationService.notifyAll(successMsg);

        composition.forEach((denomination, qtd) ->
            notificationService.notifyAll(String.format("%dx %s", qtd, denomination.getLabel()))
        );
    }

    /**
     * Find the closed available amount to withdraw
     * @param requestedAmount
     * @return closed available amount
     */
    public double findClosestAvailableAmount(double requestedAmount) {
        double total = inventory.getTotalValue();
        Map<Denomination, Integer> invetorySnapshot = inventory.getInventorySnapshot();

        // Try decreasing until a possible combination is found
        for (double alt = requestedAmount - 1; alt >= 1; alt--) {
            try {
                strategy.compose(alt, invetorySnapshot);
                return alt;
            } catch (InsufficientFundsException e) {
                // Keep searching
            }
        }

        // No valid smaller composition found, check if any higher possible within total
        for (double alt = requestedAmount + 1; alt <= total; alt++) {
            try {
                strategy.compose(alt, invetorySnapshot);
                return alt;
            } catch (InsufficientFundsException e) {
                // Keep searching
            }
        }

        return 0; // No valid suggestion
    }
}
