package service;

import dao.CompanyDAO;
import model.Company;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CompaniesImpl implements CompaniesService {

    private CompanyDAO companyDAO;

    public CompanyDAO getCompanyDAO() {

        return companyDAO;

    }

    @EJB
    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    @Override
    public Company returnCompanyById(Integer companyId) {
        return getCompanyDAO().returnCompanyById(companyId);
    }

    @Override
    public List<Company> returnAllCompanies() {
        return getCompanyDAO().findAll();
    }

    @Override
    public void save(Company entity) {
        getCompanyDAO().save(entity);
    }

    @Override
    public void delete(Company entity) {
        getCompanyDAO().delete(entity);
    }

    @Override
    public Company update(Company entity) {
        return getCompanyDAO().update(entity);
    }

    @Override
    public Company find(int entityID) {
        return getCompanyDAO().find(entityID);
    }

}
