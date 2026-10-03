package tdp.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TransactionServiceDirectCreationTest {

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, -0.01})
    void negativeAmountDoesNotCallGateway(double amount) throws PaymentException {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        TransactionService transactionService = new TransactionService(paymentGateway);

        TransactionResult result = transactionService.processTransaction("12345", amount);

        assertEquals(TransactionResult.BAD_NEGATIVE_AMOUNT, result);
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void zeroAmountIsPassedToGateway() throws PaymentException {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        TransactionService transactionService = new TransactionService(paymentGateway);
        when(paymentGateway.processPayment("12345", 0.0)).thenReturn(true);

        TransactionResult result = transactionService.processTransaction("12345", 0.0);

        assertEquals(TransactionResult.SUCCESS, result);
        verify(paymentGateway).processPayment("12345", 0.0);
    }

    @Test
    void acceptedPaymentReturnsSuccess() throws PaymentException {
        // Arrange
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        TransactionService transactionService = new TransactionService(paymentGateway);
        when(paymentGateway.processPayment("12345", 100.0)).thenReturn(true);

        // Act
        TransactionResult result = transactionService.processTransaction("12345", 100.0);

        // Assert
        assertEquals(TransactionResult.SUCCESS, result);
        verify(paymentGateway).processPayment("12345", 100.0);
    }

    @Test
    void rejectedPaymentReturnsFailure() throws PaymentException {
        // Arrange
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        TransactionService transactionService = new TransactionService(paymentGateway);
        when(paymentGateway.processPayment("12345", 100.0)).thenReturn(false);

        // Act
        TransactionResult result = transactionService.processTransaction("12345", 100.0);

        // Assert
        assertEquals(TransactionResult.FAILURE, result);
        verify(paymentGateway).processPayment("12345", 100.0);
    }

    @Test
    void gatewayExceptionPropagates() throws PaymentException {
        // Arrange
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        TransactionService transactionService = new TransactionService(paymentGateway);
        PaymentException failure = new PaymentException("Insufficient Funds");
        when(paymentGateway.processPayment("12345", 100.0)).thenThrow(failure);

        // Act
        PaymentException thrown = assertThrows(PaymentException.class,
                () -> transactionService.processTransaction("12345", 100.0));

        // Assert
        assertSame(failure, thrown);
        verify(paymentGateway).processPayment("12345", 100.0);
    }
}
