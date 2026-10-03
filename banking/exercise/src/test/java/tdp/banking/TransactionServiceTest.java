package tdp.banking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    // Write separate tests for TransactionResult.SUCCESS, TransactionResult.FAILURE,
    // and propagation of the original PaymentException using assertThrows/assertSame.
    // Negative amounts return BAD_NEGATIVE_AMOUNT without calling the gateway:
    // check this with verifyNoInteractions. Zero amounts should still reach the gateway.
    // Use @ParameterizedTest/@ValueSource to test both -100.0 and -0.01.
    // Try both direct mock creation and @Mock/@InjectMocks, with descriptive test names.
}
