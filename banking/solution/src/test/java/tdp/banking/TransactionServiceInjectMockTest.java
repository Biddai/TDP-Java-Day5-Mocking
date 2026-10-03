package tdp.banking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceInjectMockTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private TransactionService transactionService;

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, -0.01})
    void negativeAmountDoesNotCallGateway(double amount) throws PaymentException {
        TransactionResult result = transactionService.processTransaction("12345", amount);

        assertEquals(TransactionResult.BAD_NEGATIVE_AMOUNT, result);
        verifyNoInteractions(paymentGateway);
    }

    @Test
    void zeroAmountIsPassedToGateway() throws PaymentException {
        when(paymentGateway.processPayment("12345", 0.0)).thenReturn(true);

        TransactionResult result = transactionService.processTransaction("12345", 0.0);

        assertEquals(TransactionResult.SUCCESS, result);
        verify(paymentGateway).processPayment("12345", 0.0);
    }

    @Test
    void acceptedPaymentReturnsSuccess() throws PaymentException {
        // Arrange
        when(paymentGateway.processPayment("12345", 100.0)).thenReturn(true);

        // Act
        TransactionResult result = transactionService.processTransaction("12345", 100.0);

        // Assert
        assertEquals(TransactionResult.SUCCESS, result);
        verify(paymentGateway, times(1)).processPayment("12345", 100.0);
    }

    @Test
    void rejectedPaymentReturnsFailure() throws PaymentException {
        // Arrange
        when(paymentGateway.processPayment("12345", 100.0)).thenReturn(false);

        // Act
        TransactionResult result = transactionService.processTransaction("12345", 100.0);

        // Assert
        assertEquals(TransactionResult.FAILURE, result);
        verify(paymentGateway, times(1)).processPayment("12345", 100.0);
    }

    @Test
    void gatewayExceptionPropagates() throws PaymentException {
        // Arrange
        PaymentException failure = new PaymentException("Insufficient Funds");
        when(paymentGateway.processPayment("12345", 100.0)).thenThrow(failure);

        // Act
        PaymentException thrown = assertThrows(PaymentException.class,
                () -> transactionService.processTransaction("12345", 100.0));

        // Assert
        assertSame(failure, thrown);
        verify(paymentGateway, times(1)).processPayment("12345", 100.0);
    }
}
