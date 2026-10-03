package tdp.exercises;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceInjectMockTest {
    // The extension creates a fresh mock before each test.
    @Mock
    private EmployeeRepository repository;

    // Mockito passes the matching mock to EmployeeService's constructor.
    @InjectMocks
    private EmployeeService service;

    @Test
    void countsOnlyActiveEmployees() {
        // Arrange
        List<Employee> employees = List.of(
                new Employee(1, "Maya", true),
                new Employee(2, "Dana", false),
                new Employee(3, "Avi", true));
        when(repository.findAll()).thenReturn(employees);

        // Act
        int count = service.countActiveEmployees();

        // Assert
        assertThat(count).isEqualTo(2);
        // Verify that the service queried its dependency exactly once.
        verify(repository).findAll();
    }

    @Test
    void returnsZeroWhenRepositoryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(service.countActiveEmployees()).isZero();
        verify(repository).findAll();
    }

    @Test
    void returnsZeroWhenAllEmployeesAreInactive() {
        when(repository.findAll()).thenReturn(List.of(
                new Employee(1, "Maya", false),
                new Employee(2, "Dana", false)));

        assertThat(service.countActiveEmployees()).isZero();
        verify(repository).findAll();
    }
}
