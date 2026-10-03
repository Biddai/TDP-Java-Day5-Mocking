package tdp.exercises;

import java.util.List;

public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public int countActiveEmployees() {
        List<Employee> employees = repository.findAll();
        int count = 0;
        for (Employee employee : employees) {
            if (employee.isActive()) {
                count++;
            }
        }
        return count;
    }
}
