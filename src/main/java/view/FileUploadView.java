package view;

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
import javax.faces.view.facelets.FaceletContext;
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
        List<Company> buildedCompanies = CompanyBuilder.buildCompanies(dataFromUploadedFile);

        List<Company> uniqueCompaniesFromFile = CompaniesUtil.getUniqueCompanies(buildedCompanies);
        if (buildedCompanies.size() != uniqueCompaniesFromFile.size()) {
            List<Company> dublicates = new ArrayList<>(buildedCompanies);
            for (Company c : uniqueCompaniesFromFile) {
                dublicates.remove(c);
            }
            FacesMessage msg = new FacesMessage(buildedCompanies.size() - uniqueCompaniesFromFile.size() + " dublicates detected inside the file and removed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            String message = "";
            for (Company c : dublicates) {
                message = message + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }

        ArrayList<Company> DBlist = manager.getList();
        int size = uniqueCompaniesFromFile.size();
        List<Company> fileNewItemsForDB = new ArrayList<>(uniqueCompaniesFromFile);
        fileNewItemsForDB.removeAll(DBlist);

        if (size != fileNewItemsForDB.size()) {
            FacesMessage msg = new FacesMessage(size - fileNewItemsForDB.size() + " companies already exists! Dublicates removed.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            List<Company> fileDBDublicates = new ArrayList<>(uniqueCompaniesFromFile);
            for (Company c : fileNewItemsForDB) {
                fileDBDublicates.remove(c);
            }
            String message = "";
            for (Company c : fileDBDublicates) {
                message = message + c.getName() + "\n";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        }

        manager.addcompanies(fileNewItemsForDB);
        if (!fileNewItemsForDB.isEmpty()) {
            FacesMessage msg = new FacesMessage(fileNewItemsForDB.size() + " companies uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
