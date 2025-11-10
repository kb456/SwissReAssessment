

import java.util.Map;

/*
 * organization Validator Service 
 */
public interface OrganizationValidatorService {

    public Map<Employee, Double> salaryAboveAverageFunction();

    public Map<Employee, Double> salaryBelowAverageFunction();

    public Map<Employee, Integer> longReportingLineFunction();

}