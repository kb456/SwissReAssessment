# SwissReAssessment

A small Java 8 console application that:

1. Reads employee data from a CSV file  
2. Finds managers earning less than they should  
3. Finds managers earning more than they should  
4. Finds employees with long reporting lines

---

## Prerequisites

- Java 8  
- Eclipse (or any Java IDE)  
- Optional: JUnit 4 if you want to run the test cases

---

## Project Setup in Eclipse

1. Import the project  
   File -> Import -> Existing Projects into Workspace  
   Select the project folder and finish.  

2. Set Java version  
   Right-click the project -> Properties -> Java Compiler  
   Enable project-specific settings -> set Compiler compliance level to 1.8 (Java 8).  

3. Add JUnit (for tests)  
   Right-click the project -> Build Path -> Add Libraries -> JUnit -> select JUnit 4  
   Or manually add junit-4.13.2.jar and hamcrest-core-1.3.jar to the classpath.  

---

## Input

The application expects a CSV file with the following columns:

