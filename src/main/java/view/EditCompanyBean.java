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
import util.LocalizationUtil;

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
            String message = LocalizationUtil.getMessage("company_edited");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
            companyManager.flush();
        } else {
            for (Company c : companyService.returnAllCompanies()) {
                if (c.getName().equals(edited.getName())) {
                     String message = LocalizationUtil.getMessage("such_company_exist");
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(message +": "+ edited.getName()));
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
            }
            companyService.update(edited);
            String message = LocalizationUtil.getMessage("company_edited");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(message));
            companyManager.flush();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        String message = LocalizationUtil.getMessage("edit_cancelled");
        FacesMessage msg = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        companyManager.flush();
    }
}
