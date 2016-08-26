package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
//import hibernate.Query;
//import hibernate.Session;
//import hibernate.Transaction;
//import hibernate.ejb.EntityManagerImpl;
import model.Company;

@Stateless

public class UserDAOImpl implements CompanyDAO {

    @PersistenceContext(unitName = "myPU")
    EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {

        this.entityManager = entityManager;

    }

    @Override
    public Company returnCompanyById(Integer companyId) {

        Company company = entityManager.getReference(Company.class, companyId);

        return company;

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Company> findAll() {
        CriteriaQuery cq = entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Company.class));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void save(Company entity) {

        entityManager.persist(entity);
    }

    @Override
    public void delete(Company entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    @Override
    public Company update(Company entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Company find(int entityID) {
        return entityManager.find(Company.class, entityID);
    }

}
