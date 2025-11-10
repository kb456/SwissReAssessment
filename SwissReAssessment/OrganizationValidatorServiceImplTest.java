import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
 * organization Validator Service Implementation Test
 */
public class OrganizationValidatorServiceImplTest {

    @Test
    public void testSalaryBelowAverageFunction_detectsUnderpaidManagers() {
        List<Employee> employees = Arrays.asList(
                new Employee(123L, "Joe", "Doe", 60000.0, null),
                new Employee(124L, "Martin", "Chekov", 45000.0, 123L),
                new Employee(125L, "Bob", "Ronstad", 47000.0, 123L),
                new Employee(300L, "Alice", "Hasacat", 50000.0, 124L),
                new Employee(305L, "Brett", "Hardleaf", 34000.0, 300L)
        );

        OrganizationValidatorServiceImpl service = new OrganizationValidatorServiceImpl(employees);
        Map<Employee, Double> result = service.salaryBelowAverageFunction();

        assertEquals(1, result.size());
        Employee martin = employees.get(1);
        assertTrue(result.containsKey(martin));
        assertEquals(15000.0, result.get(martin), 0.0001);
    }
    
    
    // test case to check when there are no subordinates and all work independently
    @Test
    public void testSalaryBelowAverageFunction_noSubordinates_returnsEmptyMap() {
        // Scenario: 3 employees, none of them manage anyone
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Alice", "Solo", 50000.0, null),
                new Employee(2L, "Bob", "Lone", 40000.0, null),
                new Employee(3L, "Charlie", "Independent", 45000.0, null)
        );

        OrganizationValidatorServiceImpl service = new OrganizationValidatorServiceImpl(employees);

        // When: calling salaryBelowAverageFunction
        Map<Employee, Double> result = service.salaryBelowAverageFunction();

        // Then: no one should be reported because no one has subordinates
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    public void testSalaryAboveAverageFunction_detectsOverpaidManagers() {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Big", "Boss", 100000.0, null),
                new Employee(2L, "Rich", "Manager", 60000.0, 1L),
                new Employee(3L, "Worker", "One", 20000.0, 2L),
                new Employee(4L, "Worker", "Two", 20000.0, 2L)
        );

        OrganizationValidatorServiceImpl service = new OrganizationValidatorServiceImpl(employees);
        Map<Employee, Double> result = service.salaryAboveAverageFunction();

        assertEquals(2, result.size());
        Employee rich = employees.get(1);
        assertTrue(result.containsKey(rich));
        assertEquals(30000.0, result.get(rich), 0.0001);
    }

    @Test
    public void testLongReportingLineFunction_detectsEmployeesWithTooManyManagers() {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Ceo", "Root", 100000.0, null),
                new Employee(2L, "M1", "L1", 90000.0, 1L),
                new Employee(3L, "M2", "L2", 80000.0, 2L),
                new Employee(4L, "M3", "L3", 70000.0, 3L),
                new Employee(5L, "M4", "L4", 60000.0, 4L),
                new Employee(6L, "M5", "L5", 50000.0, 5L),
                new Employee(7L, "Deep", "Employee", 40000.0, 6L)
        );

        OrganizationValidatorServiceImpl service = new OrganizationValidatorServiceImpl(employees);
        Map<Employee, Integer> result = service.longReportingLineFunction();

        // Employees 6 and 7 exceed allowedMaxLevels=4
        assertEquals(2, result.size());
        Employee m5 = employees.get(5);
        Employee deep = employees.get(6);
        assertTrue(result.containsKey(m5));
        assertTrue(result.containsKey(deep));
        assertEquals(5, (int) result.get(m5));
        assertEquals(6, (int) result.get(deep));
    }
    
    // Test case when there are exactly 4 levels which will not be flagged
    @Test
    public void testLongReportingLineFunction_exactlyAtLimit_notFlagged() {

        List<Employee> employees = Arrays.asList(
                new Employee(1L, "Ceo", "Root", 100000.0, null),
                new Employee(2L, "M1", "L1", 90000.0, 1L),
                new Employee(3L, "M2", "L2", 80000.0, 2L),
                new Employee(4L, "M3", "L3", 70000.0, 3L),
                new Employee(5L, "M4", "L4", 60000.0, 4L)
        );

        OrganizationValidatorServiceImpl service = new OrganizationValidatorServiceImpl(employees);

        Map<Employee, Integer> result = service.longReportingLineFunction();

        // No one exceeds 4 levels
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
