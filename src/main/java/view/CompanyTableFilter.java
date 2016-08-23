package view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
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

    public boolean filterExact(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return value.toString().equalsIgnoreCase(filterText);
    }

}
