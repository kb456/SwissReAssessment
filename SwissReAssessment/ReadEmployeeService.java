


import java.io.IOException;
import java.util.List;

public interface ReadEmployeeService {

    List<Employee> readEmployeeCSV(String path) throws IOException;

}