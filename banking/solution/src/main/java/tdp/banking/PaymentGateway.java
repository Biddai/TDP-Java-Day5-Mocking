package tdp.banking;

public interface PaymentGateway {
    boolean processPayment(String accountNumber, double amount) throws PaymentException;
}
