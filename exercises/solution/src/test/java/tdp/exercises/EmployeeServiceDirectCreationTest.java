package tdp.exercises;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceDirectCreationTest {
    @Test
    void countsOnlyActiveEmployees() {
        // Arrange
        EmployeeRepository repository = mock(EmployeeRepository.class);

        List<Employee> employees = List.of(
                new Employee(1, "Maya", true),
                new Employee(2, "Dana", false),
                new Employee(3, "Avi", true));
        when(repository.findAll()).thenReturn(employees);
        EmployeeService service = new EmployeeService(repository);

        // Act
        int count = service.countActiveEmployees();

        // Assert
        assertThat(count).isEqualTo(2);
        // Verify that the service queried its dependency exactly once.
        verify(repository).findAll();
    }

    @Test
    void returnsZeroWhenRepositoryIsEmpty() {
        EmployeeRepository repository = mock(EmployeeRepository.class);
        when(repository.findAll()).thenReturn(List.of());
        EmployeeService service = new EmployeeService(repository);

        assertThat(service.countActiveEmployees()).isZero();
        verify(repository).findAll();
    }

    @Test
    void returnsZeroWhenAllEmployeesAreInactive() {
        EmployeeRepository repository = mock(EmployeeRepository.class);
        when(repository.findAll()).thenReturn(List.of(
                new Employee(1, "Maya", false),
                new Employee(2, "Dana", false)));
        EmployeeService service = new EmployeeService(repository);

        assertThat(service.countActiveEmployees()).isZero();
        verify(repository).findAll();
    }
}
