package dao;

import java.util.List;
import javax.ejb.Local;
import model.Company;

@Local

public interface CompanyDAO {

    Company returnCompanyById(Integer companyId);

    List<Company> findAll();

    void save(Company entity);

    void delete(Company entity);

    Company update(Company entity);

    Company find(int entityID);

    Company find(String name);

}
