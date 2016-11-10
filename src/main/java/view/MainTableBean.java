package view;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.data.PageEvent;
import service.CompaniesService;
import service.Messenger;
import util.LocalizationUtil;

@Named
@SessionScoped
public class MainTableBean implements Table {

    @Inject
    private CompaniesService realservice;
    private List<Company> selected = new ArrayList<>();
    private List<Company> data;

    @Override
    public List<Company> getData() {
        if (data == null) {
            data = realservice.returnAllCompanies();
        }
        return data;

    }

    @Override
    public List<Company> getSelected() {
        return selected;
    }

    @Override
    public void setSelected(List<Company> selectedCompanies) {
        this.selected = selectedCompanies;
    }

    @Override
    public void deleteSelected() {
        for (Company c : selected) {
            realservice.delete(c);
        }
    }

    @Override
    public void onRowEdit(RowEditEvent event) {
        Company edited = (Company) event.getObject();
        Company fromDBSameId = realservice.returnCompanyById(edited.getId());

        if (edited.getName().equals(fromDBSameId.getName())) {
            realservice.update(edited);
            String message = LocalizationUtil.getMessage("company_edited");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        } else {
            for (Company c : realservice.returnAllCompanies()) {
                if (c.getName().equals(edited.getName())) {
                    String message = LocalizationUtil.getMessage("such_company_exist");
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(message + ": " + edited.getName()));
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
            }
            realservice.update(edited);
            String message = LocalizationUtil.getMessage("company_edited");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }
    }

    @Override
    public void onRowCancel(RowEditEvent event) {
        String message = LocalizationUtil.getMessage("edit_cancelled");
        FacesMessage msg = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void remove(Company company) {
        try {
            realservice.delete(company);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listenToMessanger(@Observes Messenger.CompanyFlushEvent event) {
        flush();
    }

    public void flush() {
        data = null;
    }
    protected int first;

    public int getFirst() {
        System.out.println("get first " + first);
        return first;
    }

    public void onPageChange(PageEvent event) {
        first = ((DataTable) event.getSource()).getFirst();
    }

}
