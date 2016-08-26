package view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.primefaces.event.RowEditEvent;
import service.CompaniesManage;

@Named
@SessionScoped
public class EditCompanyBean implements Serializable {

    @Inject
    private CompaniesManage entity;

    public void onRowEdit(RowEditEvent event) {
        Company edited = (Company) event.getObject();
//        if (entity.getList().contains(edited)) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Such company exist: " + edited.getName()));
//            FacesContext.getCurrentInstance().validationFailed();
//        } else {
            FacesMessage msg = new FacesMessage("Company Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }

    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
