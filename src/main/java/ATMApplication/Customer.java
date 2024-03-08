package ATMApplication;

import java.util.Random;

public class Customer {
    private String name;
    private int accountNumber;
    private double balance;
    private int pin;

    public Customer(String name, double balance, int pin) {
        this.name = name;
        this.accountNumber = generateAccountNumber();
        this.balance = balance;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }

    private int generateAccountNumber() {
        Random random = new Random();
        return 100000000 + random.nextInt(900000000);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
