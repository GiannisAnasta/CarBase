package view;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Employee;

@Named
@RequestScoped
public class EmployeersTableFilter implements Serializable {

    private List<Employee> filteredEmployeers;

    public List<Employee> getFilteredEmployeers() {
        return filteredEmployeers;
    }

    public void setFilteredEmployeers(List<Employee> filteredEmployeers) {
        this.filteredEmployeers = filteredEmployeers;
    }
}
