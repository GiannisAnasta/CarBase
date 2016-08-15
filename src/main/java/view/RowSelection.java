package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import service.CompaniesManage;

@Named
@SessionScoped
public class RowSelection implements Serializable {

    @Inject
    private CompaniesManage service;

    private List<Company> selected = new ArrayList<>();

    public List<Company> getSelected() {
        return selected;
    }

    public void setSelected(List<Company> selectedCompanies) {
        this.selected = selectedCompanies;
    }

    public void deleteSelected() {
        service.removeCompanies(selected);
    }

}
