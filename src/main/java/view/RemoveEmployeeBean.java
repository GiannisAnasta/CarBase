package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Employee;
import service.EmployeerManage;

@Named
@SessionScoped
public class RemoveEmployeeBean implements Serializable {

    @Inject
    private EmployeerManage service;

    public void remove(Employee employee) {
        try {
            service.removeEmployee(employee);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
