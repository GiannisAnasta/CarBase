package view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Company;
import view.converter.DateConverter;

@Named
@RequestScoped
public class CompanyTableFilter implements Serializable {

    private List<Company> filteredCompanies;

    public List<Company> getFilteredCompanies() {
        System.out.println("getfiltered " + filteredCompanies.size());

        return filteredCompanies;
    }

    public void setFilteredCompanies(List<Company> filteredCompanies) {
        System.out.println("SЕТfiltered " + filteredCompanies.size());
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
