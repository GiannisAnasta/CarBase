package service;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Employee;

@Named
@SessionScoped
public class EmployeerManage implements Serializable {

    private static final ArrayList<Employee> entity = new ArrayList<>();

    public ArrayList<Employee> getList() {
        return entity;
    }

    public void addEmployee(Employee employee) {
        entity.add(employee);
    }

    public void removeEmployee(Employee employee) {
        entity.remove(employee);
    }

}
