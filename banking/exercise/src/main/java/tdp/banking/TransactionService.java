package tdp.banking;

public class TransactionService {
    private final PaymentGateway paymentGateway;

    public TransactionService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public TransactionResult processTransaction(String accountNumber, double amount) throws PaymentException {
        if (amount < 0) {
            return TransactionResult.BAD_NEGATIVE_AMOUNT;
        }

        boolean success = paymentGateway.processPayment(accountNumber, amount);
        return success ? TransactionResult.SUCCESS : TransactionResult.FAILURE;
    }
}
