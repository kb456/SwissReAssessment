


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Read Employee Service Implementation
 */
public class ReadEmployeeServiceImpl implements ReadEmployeeService {

    @Override
    public List<Employee> readEmployeeCSV(String path) throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(" Path cannot be null");
        }

        List<Employee> employees = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isHeader = true;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty())
                    continue;

                if (isHeader) {
                    // skipping first line
                    isHeader = false;
                    continue;
                }

                String[] empInfo = line.split(",", -1);

                // Assuming all the fields are mandatory and are present.
                // Ignoring if the employee data is incomplete. Also can always add checks to
                // ignore any personal fields like name
                if (empInfo.length < 5)
                    continue;

                final long id = Long.parseLong(empInfo[0].trim());
                final String firstName = empInfo[1].trim();
                final String lastName = empInfo[2].trim();
                final double salary = Double.parseDouble(empInfo[3].trim());

                final String managerIdString = empInfo[4].trim();
                final Long managerId = Optional.ofNullable(managerIdString)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .orElse(null);

                Employee employee = new Employee(id, firstName, lastName, salary, managerId);

                employees.add(employee);
            }
        }
        return employees;
    }

}
