package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import service.CompaniesManage;

@Named
@SessionScoped
public class AddCompanyBean implements Serializable {

    @Inject
    private CompaniesManage entity;

    private String name;
    private String surname;
    private String position;

    public CompaniesManage getEntity() {
        return entity;
    }

    public void setEntity(CompaniesManage entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void addAction() {
        Company entities = new Company();
        entities.setName(name);
        //todo
//        entities.(position);
//        entities.setSurname(surname);
        this.entity.addCompany(entities);
        flush();
    }

    private void flush() {
        name = "";
        surname = "";
        position = "";

    }
}
