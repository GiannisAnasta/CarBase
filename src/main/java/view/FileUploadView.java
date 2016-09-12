package view;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import fileupload.CompanyBuilder;
import fileupload.IndefiniteData;
import fileupload.UploadedFileDataReader;
import fileupload.validation.exceptions.UploadedFileReadException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.CompaniesManage;
import service.CompaniesUtil;
import util.LocalizationUtil;

@Named
@SessionScoped

public class FileUploadView implements Serializable {

    @Inject
    private CompaniesManage manager;
    private FormatOfData formatOfData = FormatOfData.NEW_LINE_SEPARATED;

    public FormatOfData getFormatOfData() {
        return formatOfData;
    }

    public void setFormatOfData(FormatOfData formatOfData) {
        this.formatOfData = formatOfData;
    }

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("start");

        //get uploaded file from the event
        UploadedFile uploadedFile = (UploadedFile) event.getFile();
        IndefiniteData dataFromUploadedFile = null;
        try {
            dataFromUploadedFile = UploadedFileDataReader.getDataFromUploadedFile(uploadedFile, ';');
            dataFromUploadedFile.removeFirstLine();
        } catch (UploadedFileReadException ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Company> buildedCompanies = null;
        if (FormatOfData.EMPTY_LINE_SEPARATED.equals(formatOfData)) {
            buildedCompanies = CompanyBuilder.buildCompaniesEmptyLinesSeparated(dataFromUploadedFile);
        } else if (FormatOfData.NEW_LINE_SEPARATED.equals(formatOfData)) {
            buildedCompanies = CompanyBuilder.buildCompaniesNewLinesSeparated(dataFromUploadedFile);
        } else {
            System.out.println("Found unknown type !!!!!!!! " + formatOfData);
            return;
        }

        List<Company> uniqueEqualBasedCompaniesFromFile = CompaniesUtil.getUniqueCompanies(buildedCompanies);
        if (buildedCompanies.size() != uniqueEqualBasedCompaniesFromFile.size()) {
            List<Company> dublicates = new ArrayList<>(buildedCompanies);
            for (Company c : uniqueEqualBasedCompaniesFromFile) {
                dublicates.remove(c);
            }
            String message = LocalizationUtil.getMessage("dublicates_detected_inside_the_file_and_removed");
            FacesMessage msg = new FacesMessage(buildedCompanies.size() - uniqueEqualBasedCompaniesFromFile.size() + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            String msg1 = "";
            for (Company c : dublicates) {
                msg1 = msg1 + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg1));
        }

        List<Company> DBlist = manager.getList();
        int size = uniqueEqualBasedCompaniesFromFile.size();
        List<Company> fileNewItemsForDB = new ArrayList<>(uniqueEqualBasedCompaniesFromFile);
        fileNewItemsForDB.removeAll(DBlist);

        if (size != fileNewItemsForDB.size()) {
            String message = LocalizationUtil.getMessage("companies_already_exists_dublicates_removed");
            FacesMessage msg = new FacesMessage(size - fileNewItemsForDB.size() + " " + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);

            List<Company> fileDBDublicates = new ArrayList<>(uniqueEqualBasedCompaniesFromFile);
            for (Company c : fileNewItemsForDB) {
                fileDBDublicates.remove(c);
            }
            String msg1 = "";
            for (Company c : fileDBDublicates) {
                msg1 = msg1 + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg1));
        }
        List<String> names = new ArrayList<>();
        for (Company company : fileNewItemsForDB) {
            try {
                manager.addCompany(company);
            } catch (javax.ejb.EJBException ex) {
                names.add(company.getName());
            }
        }
        if (!names.isEmpty()) {
            String message = LocalizationUtil.getMessage("еrrors_with_those_items");
            for (String name : names) {
                message += name + "\n";
            }
            FacesMessage msg = new FacesMessage(fileNewItemsForDB.size() - names.size() + " " + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (!fileNewItemsForDB.isEmpty()) {
            String message = LocalizationUtil.getMessage("messages_companies_uploaded");
            FacesMessage msg = new FacesMessage(fileNewItemsForDB.size() - names.size() + " " + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
