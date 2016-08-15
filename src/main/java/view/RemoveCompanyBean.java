package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import service.CompaniesManage;

@Named
@SessionScoped
public class RemoveCompanyBean implements Serializable {

    @Inject
    private CompaniesManage service;

    public void remove(Company company) {
        try {
            service.removeCompany(company);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
