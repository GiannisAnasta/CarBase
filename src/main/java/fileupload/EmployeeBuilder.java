package fileupload;

import fileupload.IndefiniteData.Row;
import java.util.ArrayList;
import java.util.List;
import model.Employee;

public class EmployeeBuilder {

    public static Employee buildEmployee(Row row) {
        Employee employee = new Employee();
        employee.setName(row.getData().get(0));
        employee.setSurname(row.getData().get(1));
        employee.setPosition(row.getData().get(2));
        return employee;
    }

    public static List<Employee> buildEmployees(IndefiniteData indefiniteData) {
        List<Employee> employees = new ArrayList<>();
        for (IndefiniteData.Row row : indefiniteData.getData()) {
            employees.add(buildEmployee(row));
        }
        return employees;
    }

}
