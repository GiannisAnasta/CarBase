package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Employee;
import service.EmployeerManage;

@Named
@SessionScoped
public class AddEmployeeBean implements Serializable {

    @Inject
    private EmployeerManage entity;

    private String name;
    private String surname;
    private String position;

    public EmployeerManage getEntity() {
        return entity;
    }

    public void setEntity(EmployeerManage entity) {
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
        Employee entities = new Employee();
        entities.setName(name);
        entities.setPosition(position);
        entities.setSurname(surname);
        this.entity.addEmployee(entities);
        flush();
    }

    private void flush() {
        name = "";
        surname = "";
        position = "";
       
    }
}
