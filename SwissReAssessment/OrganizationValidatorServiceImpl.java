
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * organization Validator Service Implementation
 */
public class OrganizationValidatorServiceImpl implements OrganizationValidatorService {

    private final List<Employee> employees;
    private final Set<Long> managerIds;
    Map<Long, Employee> employeeMap;
    Map<Long, List<Employee>> subsMap;

    /**
     * Creates an {@code OrganizationValidatorServiceImpl} instance and prepares the
     * required data structures for validation.
     *
     * @param employees       list of employees in the organization
     */
    public OrganizationValidatorServiceImpl(List<Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException("employees list cannot be null");
        }

        this.employees = new ArrayList<>(employees);
        managerIds = employees.stream().map(Employee::getManagerId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        employeeMap = buildEmployeeMap(employees);
        subsMap = buildSubordinatesMap(employees);
    }

    private static Map<Long, Employee> buildEmployeeMap(List<Employee> employees) {
        Map<Long, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.getId(), e);
        }
        return map;
    }

    private static Map<Long, List<Employee>> buildSubordinatesMap(List<Employee> employees) {
        Map<Long, List<Employee>> subs = new HashMap<>();
        for (Employee e : employees) {
            Long mId = e.getManagerId();
            if (mId != null) {
                subs.computeIfAbsent(mId, k -> new ArrayList<>()).add(e);
            }
        }
        return subs;
    }

    private static double averageSalary(List<Employee> subs) {
        if (subs == null || subs.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (Employee e : subs) {
            sum += e.getSalary();
        }
        return sum / subs.size();
    }

    @Override
    public Map<Employee, Double> salaryBelowAverageFunction() {

        Map<Employee, Double> outputList = new HashMap<>();
        for (Long managerId : managerIds) {
            Employee manager = employeeMap.get(managerId);
            if (manager == null)
                continue;

            List<Employee> subordinates = subsMap.get(managerId);
            if (subordinates == null || subordinates.isEmpty())
                continue;

            double avgSubSalary = averageSalary(subordinates);
            double minManagerSalary = avgSubSalary * 1.2;

            if (manager.getSalary() < minManagerSalary) {
                double diffSalary = minManagerSalary - manager.getSalary();
                System.out.printf(
                        "Manager %s %s (salary=%.2f) should earn at least %.2f => SHORT by %.2f%n",
                        manager.getFirstName(),
                        manager.getLastName(),
                        manager.getSalary(),
                        minManagerSalary,
                        diffSalary);

                outputList.put(manager, (Double) diffSalary);

            }
        }
        return outputList;
    }

    @Override
    public Map<Employee, Double> salaryAboveAverageFunction() {

        Map<Employee, Double> outputList = new HashMap<>();
        for (Long managerId : managerIds) {
            Employee manager = employeeMap.get(managerId);
            if (manager == null)
                continue;

            List<Employee> subordinates = subsMap.get(managerId);
            if (subordinates == null || subordinates.isEmpty())
                continue;

            double avgSubSalary = averageSalary(subordinates);
            double maxManagerSalary = avgSubSalary * 1.5;

            if (manager.getSalary() > maxManagerSalary) {
                double diffSalary = manager.getSalary() - maxManagerSalary;
                System.out.printf(
                        "Manager %s %s (salary=%.5f) should only earn at most %.5f => MORE by %.2f%n",
                        manager.getFirstName(),
                        manager.getLastName(),
                        manager.getSalary(),
                        maxManagerSalary,
                        diffSalary);

                outputList.put(manager, (Double) diffSalary);

            }
        }
        return outputList;
    }

    @Override
    public Map<Employee, Integer> longReportingLineFunction() {
        int levels = 0;
        int allowedMaxLevels = 4;
        Map<Employee, Integer> empLevelChart = new HashMap<>();

        for (Employee emp : employees) {
            Long managerId = emp.getManagerId();

            while (managerId != null) {
                levels++;
                Employee recursiveManager = employeeMap.get(managerId);
                managerId = recursiveManager.getManagerId();
            }

            if (levels > allowedMaxLevels) {
                empLevelChart.put(emp, levels);
                System.out.println(
                        "Employee " + emp.getFirstName() + " " + emp.getLastName() + " EmployeeId " + emp.getId()
                                + " have " + levels + " managers  them and the CEO");
            }
            levels = 0;
        }
        return empLevelChart;
    }

}
