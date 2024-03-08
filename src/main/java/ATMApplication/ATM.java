package ATMApplication;

public class ATM {
    private Customer customer;

    public ATM(Customer customer) {
        this.customer = customer;
    }

    public void deposit(double amount) {
        customer.setBalance(customer.getBalance() + amount);
        System.out.println("Deposit successful. New balance: " + customer.getBalance());
    }

    public void withdraw(double amount, int pin) {
        if (pin == customer.getPin()) {
            if (customer.getBalance() >= amount) {
                customer.setBalance(customer.getBalance() - amount);
                System.out.println("Withdrawal successful. New balance: " + customer.getBalance());
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }

    public void checkBalance(int pin) {
        if (pin == customer.getPin()) {
            System.out.println("Current balance: " + customer.getBalance());
        } else {
            System.out.println("Incorrect PIN.");
        }
    }
}
