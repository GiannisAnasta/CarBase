package view;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Company;

@Named
@RequestScoped
public class CompanyTableFilter implements Serializable {

    private List<Company> filteredCompanies;

    public List<Company> getFilteredCompanies() {
        return filteredCompanies;
    }

    public void setFilteredCompanies(List<Company> filteredCompanies) {
        this.filteredCompanies = filteredCompanies;
    }

}
