package service;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Company;

@Named
@SessionScoped
public class CompaniesManage implements Serializable {

    private CompaniesService companyService;

    @EJB
    public void setUserService(CompaniesService userService) {
        this.companyService = userService;
    }

    List<Company> cache;

    public List<Company> getList() {
        if (cache == null) {
            cache = companyService.returnAllCompanies();
        }
        return cache;
    }

    public void addCompany(Company company) {
        companyService.save(company);
        flush();
    }

    public void removeCompany(Company company) {
        companyService.delete(company);
        flush();
    }

    public void addcompanies(List<Company> companies) {
        for (Company c : companies) {
            companyService.save(c);
        }
        flush();
    }

    public void removeCompanies(List<Company> companies) {
        for (Company c : companies) {
            companyService.delete(c);
        }
        flush();
    }

    public void flush() {
        cache = null;
    }
}
