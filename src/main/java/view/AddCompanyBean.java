package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private List<String> site = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> telephones = new ArrayList<>();
    private List<String> details = new ArrayList<>();

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

    public List<String> getSite() {
        return site;
    }

    public void setSite(List<String> site) {
        this.site = site;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<String> telephones) {
        this.telephones = telephones;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public void addAction() {
        Company newCompany = new Company();
        newCompany.setName(name);
        newCompany.setSite(site);
        newCompany.setEmail(email);
        newCompany.setTelephones(telephones);
        newCompany.setDetails(details);

        if (entity.getList().contains(newCompany)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Such company exist: " + newCompany.getName()));
        } else {
            entity.addCompany(newCompany);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("New company added: " + newCompany.getName()));
            flush();
        }

    }

    private void flush() {
        name = "";
        site = null;
        email = null;
        telephones = null;
        details = null;

    }

}
