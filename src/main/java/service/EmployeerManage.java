package service;

import fileupload.EmployeeExcelFileToList;
import fileupload.ToDoWriteListToExcelFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.inject.Named;
import model.Employee;

@Named
@SessionScoped
public class EmployeerManage implements Serializable {

    private static final ArrayList<Employee> entities = new ArrayList<>();

    private static final String STORAGE_FILE = "/home/giannis/CarBase/storage/storageDB.xlsx";

    @PostConstruct
    private void onInit() {
        addEmployees(EmployeeExcelFileToList.employeeExcelData(STORAGE_FILE));
    }

    @PreDestroy
    private void onDestroy() {
        saveToStorage();
    }

    public void saveToStorage() {
        try {
            //Todo backup of original file
            ToDoWriteListToExcelFile.writeEmployeeListToFile(STORAGE_FILE, entities);
        } catch (Exception ex) {
            Logger.getLogger(EmployeerManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public void removeEmployees(List<Employee> employees) {
        entities.removeAll(employees);
    }

}
