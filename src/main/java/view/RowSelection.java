package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Employee;
import service.EmployeerManage;

@Named
@SessionScoped
public class RowSelection implements Serializable {

    @Inject
    private EmployeerManage service;

    private List<Employee> selected = new ArrayList<>();

    public List<Employee> getSelected() {
        return selected;
    }

    public void setSelected(List<Employee> selectedCars) {
        this.selected = selectedCars;
    }

    public void deleteSelected() {
        service.removeEmployees(selected);
    }

}
