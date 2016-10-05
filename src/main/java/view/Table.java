package view;

import java.io.Serializable;
import java.util.List;
import model.Company;
import org.primefaces.event.RowEditEvent;

import javax.faces.event.AjaxBehaviorEvent;

public interface Table extends Serializable {

    List<Company> getData();

    List<Company> getSelected();

    void setSelected(List<Company> selectedCompanies);

    void deleteSelected();

    void onRowEdit(RowEditEvent event);

    void onRowCancel(RowEditEvent event);

    void remove(Company company);

    //void rowSelect(AjaxBehaviorEvent event);

    boolean checkSelected(int id);
}
