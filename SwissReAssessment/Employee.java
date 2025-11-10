

/*
 * Employee Class 
 */
public class Employee {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final Long managerId; // can be null

    // constructor to create an employee object
    Employee(long id, String firstName, String lastName, Double salary, Long managerId) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {

        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + managerId +
                '}';
    }
}
