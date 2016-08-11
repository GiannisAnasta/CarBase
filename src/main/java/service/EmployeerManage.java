package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Employee;

@Named
@SessionScoped
public class EmployeerManage implements Serializable {

    private static final ArrayList<Employee> entities = new ArrayList<>();

    public ArrayList<Employee> getList() {
        return entities;
    }

    public void addEmployee(Employee employee) {
        entities.add(employee);
    }

    public void removeEmployee(Employee employee) {
        entities.remove(employee);
    }

    public void addEmployees(List<Employee> employees) {
        entities.addAll(employees);
    }

}
