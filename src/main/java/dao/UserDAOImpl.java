package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Company find(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> postRoot = cq.from(Company.class);
        cq.select(postRoot);
        Path<Object> get = postRoot.get("name");
        cq.where(cb.equal(get, name));
        List<Company> resultList = entityManager.createQuery(cq).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
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
