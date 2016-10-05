package view.fileupload;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import model.Company;
import org.primefaces.event.RowEditEvent;
import util.LocalizationUtil;
import view.Table;

public class Preview implements Table {

    public void setData(List<Company> loaded) {
        int i = 0;
        for (Company c : loaded) {
            c.setId(i++);
        }
        this.loaded = loaded;
    }

    private List<Company> loaded;
    private List<Company> selected = new ArrayList<>();

    @Override
    public List<Company> getData() {
        return loaded;
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
            loaded.remove(c);
        }
    }

    @Override
    public void onRowEdit(RowEditEvent event) {
        Company edited = (Company) event.getObject();
        Company fromLoadedSameId = returnCompanyById(edited.getId());

        if (edited.getName().equals(fromLoadedSameId.getName())) {
            String message = LocalizationUtil.getMessage("company_edited");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        } else {
            for (Company c : loaded) {
                if (edited.getName().equals(fromLoadedSameId.getName())) {
                    continue;
                }
                if (c.getName().equals(edited.getName())) {
                    String message = LocalizationUtil.getMessage("such_company_exist");
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(message + ": " + edited.getName()));
                    FacesContext.getCurrentInstance().validationFailed();
                    return;
                }
            }
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

    @Override
    public void remove(Company company) {
        loaded.remove(company);
    }

    private Company returnCompanyById(Integer id) {
        for (Company c : loaded) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    //@Override
    public void rowSelect(AjaxBehaviorEvent event) {

    }

    @Override
    public boolean checkSelected(int id) {
        return false;
    }
}
