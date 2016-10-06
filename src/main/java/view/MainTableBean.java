package view;

import java.util.*;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.primefaces.event.RowEditEvent;
import service.CompaniesService;
import service.Messenger;
import util.LocalizationUtil;

@Named
@SessionScoped
public class MainTableBean implements Table {

    @Inject
    private CompaniesService realservice;
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
        System.out.println("getSelected");
        List<Company> selected = new ArrayList<>();
        for (Company c : data) {
            if (selectedIds.contains(c.getId())) {
                selected.add(c);
            }
        }
        return selected;
    }

    @Override
    public void deleteSelected() {
        for (Company c : getSelected()) {
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

    private Set<Integer> selectedIds = new HashSet<>();

    /*
    @Override
    public void rowSelect(AjaxBehaviorEvent event) {


        //Map<String,String> params =
        //        FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        //String cId = params.get("myId");
        Integer id = (Integer) event.getComponent().getAttributes().get("companyId");
        System.out.println("rowselect" + id);
        selectedIds.add(id);
        for (Company company : data) {
            if (company.getId() == id) {
                selected.add(company);
            }
        }
    }
    */

    public void rowSelect(int id) {
        System.out.println("rowselect2" + id);
        if (!selectedIds.add(id)) {
            selectedIds.remove(id);
        }
    }

    @Override
    public boolean checkSelected(int id) {
        System.out.println("check");
        return selectedIds.contains(id);
    }

    private boolean showOnlySelected = false;

    public boolean isShowOnlySelected() {
        return showOnlySelected;
    }

    public void setShowOnlySelected(boolean showOnlySelected) {
        this.showOnlySelected = showOnlySelected;
    }
}
