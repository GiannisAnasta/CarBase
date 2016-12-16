package view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Company;
import view.converter.DateConverter;

@Named
@SessionScoped
public class CompanyTableFilter implements Serializable {

    private List<Company> filteredCompanies;

    public List<Company> getFilteredCompanies() {
        return filteredCompanies;
    }

    public void setFilteredCompanies(List<Company> filteredCompanies) {
        this.filteredCompanies = filteredCompanies;
    }

    public boolean filterDate(Object value, Object filter, Locale locale) {
        if (filter == null) {
            return true;
        }
        List<String> filters = (List<String>) filter;
        if (filters.isEmpty()) {
            return true;
        }

        if (value == null) {
            return false;
        }
        String formatedValue = DateConverter.format.format(value);
        for (String f : filters) {
            if (formatedValue.contains(f)) {
                return true;
            }
        }
        return false;
    }

    public boolean multiFilter(Object value, Object filter, Locale locale) {

        if (filter == null) {
            return true;
        }
        List<String> filters = (List<String>) filter;
        if (filters.isEmpty()) {
            return true;
        }

        if (value == null) {
            return false;
        }
        String formatedValue = value.toString();
        for (String f : filters) {
            if (formatedValue.toLowerCase().contains(f.toLowerCase())) {
                return true;
            }
        }
        return false;

    }
}
