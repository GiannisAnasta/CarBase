package service;

import java.util.List;
import javax.ejb.Local;
import java.util.List;
import javax.ejb.Local;
import model.Company;

@Local
public interface CompaniesService {

    Company returnCompanyById(Integer companyId);

    List<Company> returnAllCompanies();

    void save(Company entity);

    void delete(Company entity);

    Company update(Company entity);

    Company find(int entityID);

}
