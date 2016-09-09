package view;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import fileupload.CompanyBuilder;
import fileupload.IndefiniteData;
import fileupload.UploadedFileDataReader;
import fileupload.validation.exceptions.UploadedFileReadException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
            FacesMessage msg = new FacesMessage(buildedCompanies.size() - uniqueEqualBasedCompaniesFromFile.size() + " dublicates detected inside the file and removed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            String message = "";
            for (Company c : dublicates) {
                message = message + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }

        List<Company> DBlist = manager.getList();
        int size = uniqueEqualBasedCompaniesFromFile.size();
        List<Company> fileNewItemsForDB = new ArrayList<>(uniqueEqualBasedCompaniesFromFile);
        fileNewItemsForDB.removeAll(DBlist);

        if (size != fileNewItemsForDB.size()) {
            FacesMessage msg = new FacesMessage(size - fileNewItemsForDB.size() + " companies already exists! Dublicates removed.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            List<Company> fileDBDublicates = new ArrayList<>(uniqueEqualBasedCompaniesFromFile);
            for (Company c : fileNewItemsForDB) {
                fileDBDublicates.remove(c);
            }
            String message = "";
            for (Company c : fileDBDublicates) {
                message = message + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
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
            String message = "Errors with such items:\n";
            for (String name : names) {
                message += name + "\n";
            }
            FacesMessage msg = new FacesMessage(message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (!fileNewItemsForDB.isEmpty()) {
            FacesMessage msg = new FacesMessage(fileNewItemsForDB.size() - names.size() + " companies uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
