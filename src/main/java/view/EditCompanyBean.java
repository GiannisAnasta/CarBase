package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.primefaces.event.RowEditEvent;
import service.CompaniesManage;
import service.CompaniesService;

@Named
@SessionScoped
public class EditCompanyBean implements Serializable {

    @Inject
    private CompaniesService companyService;
    @Inject
    private CompaniesManage companyManager;

    public void onRowEdit(RowEditEvent event) {
        Company edited = (Company) event.getObject();
        Company fromDBSameId = companyService.returnCompanyById(edited.getId());

        if (edited.getName().equals(fromDBSameId.getName())) {
            companyService.update(edited);
            FacesMessage msg = new FacesMessage("Company Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            companyManager.flush();
        } else {
            for (Company c : companyService.returnAllCompanies()) {
                if (c.getName().equals(edited.getName())) {
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage("Such company exist: " + edited.getName()));
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
            }
            companyService.update(edited);
            FacesMessage msg = new FacesMessage("Company Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            companyManager.flush();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        companyManager.flush();
    }
}
