
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * organization Main Service 
 */
public class OrganizationMainService {

    public static void main(String args[]) throws IOException {
    	
    	// Dynamically passing different csv file paths.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the full path to the employee CSV file: ");
        String filePath = scanner.nextLine();
        scanner.close();

        final ReadEmployeeService readEmployeeService = new ReadEmployeeServiceImpl();
        
        System.out.println("Reading file from: " + filePath);
        List<Employee> companyEmployees = readEmployeeService.readEmployeeCSV(filePath);
        for (Employee emp : companyEmployees) {
        	 System.out.println(emp.toString());
        	  }
        
        System.out.println("");
        System.out.println("");

//        System.out.println("Function to read the CSV file");
//        List<Employee> companyEmployees =
//                readEmployeeService.readEmployeeCSV("D:\\WorkSpace\\Java Codes\\SwissReAssessment\\employees.csv");
//        for (Employee emp : companyEmployees) {
//            System.out.println(emp.toString());
//        }

        final OrganizationValidatorService organizationValidatorService = new OrganizationValidatorServiceImpl(
                companyEmployees);

        // Returning output to the Main Controller class. Results can be maipulated later
        System.out.println("Function to find the List of Managers earning less ");
        Map<Employee, Double> managersWithlessSalary = organizationValidatorService.salaryBelowAverageFunction();
        System.out.println("");
        System.out.println("");
        
        System.out.println("Function to find List of Managers earning more");
        Map<Employee, Double> managersWithMoreSalary = organizationValidatorService.salaryAboveAverageFunction();
        
        System.out.println("");
        System.out.println("");
        System.out.println("Function to find employees who have long reporting line");
        Map<Employee, Integer> EmployeesWithManyReporters = organizationValidatorService.longReportingLineFunction();

    }
}
