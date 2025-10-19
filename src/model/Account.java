package model;

public class Account {
    private final String id;
    private double balance;

    public Account(String id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive.");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance)
            throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
    }

    @Override
    public String toString() {
        return String.format("Account %s | Balance: $%.2f", id, balance);
    }
}
