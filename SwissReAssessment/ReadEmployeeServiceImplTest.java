import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/*
 * Read Employee Service Implementation Test
 */
public class ReadEmployeeServiceImplTest {

	// creating Temp CSV file for test case purposes
    private File createTempCsv(String content) throws IOException {
        File tempFile = File.createTempFile("employees", ".csv");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    // Passing Valid Data through CSV
    @Test
    public void testReadEmployeeCSV_validData_parsesEmployees() throws IOException {
        String csv =
                "id,firstName,lastName,salary,managerId\n" +
                "123,Joe,Doe,60000,\n" +
                "124,Martin,Chekov,45000,123\n" +
                "125,Bob,Ronstad,47000,123\n";

        File file = createTempCsv(csv);

        ReadEmployeeServiceImpl service = new ReadEmployeeServiceImpl();
        List<Employee> employees = service.readEmployeeCSV(file.getAbsolutePath());

        assertNotNull(employees);
        assertEquals(3, employees.size());

        Employee e1 = employees.get(0);
        assertEquals(123L, e1.getId());
        assertEquals("Joe", e1.getFirstName());
        assertEquals("Doe", e1.getLastName());
        assertEquals(60000.0, e1.getSalary(), 0.0001);
        assertNull(e1.getManagerId()); // empty managerId -> null

        Employee e2 = employees.get(1);
        assertEquals(124L, e2.getId());
        assertEquals(Long.valueOf(123L), e2.getManagerId());
    }

    // passing empty information/ lines - Validation checks
    @Test
    public void testReadEmployeeCSV_skipsHeaderEmptyLinesAndShortLines() throws IOException {
        String csv =
                "id,firstName,lastName,salary,managerId\n" +  // header
                "\n" +                                         // empty line
                "200,Ann,Smith,50000,100\n" +                  // valid
                "201,Only,FourCols,40000\n" +                  // <5 cols -> skipped
                "   \n" +                                      // whitespace only -> skipped
                "202,Mark,Twain,55000,\n";                     // valid, empty managerId

        File file = createTempCsv(csv);

        ReadEmployeeServiceImpl service = new ReadEmployeeServiceImpl();
        List<Employee> employees = service.readEmployeeCSV(file.getAbsolutePath());

        assertNotNull(employees);
        assertEquals(2, employees.size());

        // First valid
        Employee e1 = employees.get(0);
        assertEquals(200L, e1.getId());
        assertEquals("Ann", e1.getFirstName());
        assertEquals("Smith", e1.getLastName());
        assertEquals(50000.0, e1.getSalary(), 0.0001);
        assertEquals(Long.valueOf(100L), e1.getManagerId());

        // Last valid, short line & blanks skipped
        Employee e2 = employees.get(1);
        assertEquals(202L, e2.getId());
        assertEquals("Mark", e2.getFirstName());
        assertEquals("Twain", e2.getLastName());
        assertNull(e2.getManagerId());
    }


    // Trim Functions - skipping white spaces
    @Test
    public void testReadEmployeeCSV_trimsWhitespaceAndParsesManagerId() throws IOException {
        String csv =
                "id,firstName,lastName,salary,managerId\n" +
                "300,  Alice  , Hasacat ,50000, 124  \n" +
                "305,Brett ,Hardleaf,34000,   \n";

        File file = createTempCsv(csv);

        ReadEmployeeServiceImpl service = new ReadEmployeeServiceImpl();
        List<Employee> employees = service.readEmployeeCSV(file.getAbsolutePath());

        assertEquals(2, employees.size());

        Employee alice = employees.get(0);
        assertEquals(300L, alice.getId());
        assertEquals("Alice", alice.getFirstName());
        assertEquals("Hasacat", alice.getLastName());
        assertEquals(50000.0, alice.getSalary(), 0.0001);
        assertEquals(Long.valueOf(124L), alice.getManagerId());

        Employee brett = employees.get(1);
        assertEquals(305L, brett.getId());
        assertNull(brett.getManagerId()); // spaces -> treated as empty -> null
    }

    //Illegal Argument Exception test case when input passed as null
    @Test(expected = IllegalArgumentException.class)
    public void testReadEmployeeCSV_nullPath_throwsException() throws IOException {
        ReadEmployeeServiceImpl service = new ReadEmployeeServiceImpl();
        service.readEmployeeCSV(null);
    }
}
