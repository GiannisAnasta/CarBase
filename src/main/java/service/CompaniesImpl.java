package service;

import dao.CompanyDAO;
import model.Company;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CompaniesImpl implements CompaniesService {

    @Inject
    private Messenger messenger;
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
        getCompanyDAO().save(CompaniesUtil.getComapnyWithNoDuplicates(entity));
        messenger.flushCompanies();
    }

    @Override
    public void delete(Company entity) {
        getCompanyDAO().delete(entity);
        messenger.flushCompanies();
    }

    @Override
    public Company update(Company entity) {
        messenger.flushCompanies();
        return getCompanyDAO().update(CompaniesUtil.getComapnyWithNoDuplicates(entity));
    }

    @Override
    public Company find(int entityID) {
        return getCompanyDAO().find(entityID);
    }

    @Override
    public Company find(String name) {
        return getCompanyDAO().find(name);
    }

}
