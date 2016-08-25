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
            dublicates.removeAll(uniqueCompaniesFromFile);
            FacesMessage msg = new FacesMessage(buildedCompanies.size() - uniqueCompaniesFromFile.size() + " dublicates detected inside the file and removed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            for (Company c : dublicates) {
                FacesMessage msg1 = new FacesMessage("ignored <" + c.getName() + "> company");
                FacesContext.getCurrentInstance().addMessage(null, msg1);
            }
        }

        ArrayList<Company> DBlist = manager.getList();
        int size = uniqueCompaniesFromFile.size();
        List<Company> fileNewItemsForDB = new ArrayList<>(uniqueCompaniesFromFile);
        fileNewItemsForDB.removeAll(DBlist);

        if (size != fileNewItemsForDB.size()) {
            FacesMessage msg = new FacesMessage(size - fileNewItemsForDB.size() + " companies already exists! Dublicates removed.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            List<Company> fileDBDublicates = new ArrayList<>(uniqueCompaniesFromFile);
            fileDBDublicates.removeAll(fileNewItemsForDB);
            for (Company c : fileDBDublicates) {
                FacesMessage msg1 = new FacesMessage("ignored <" + c.getName() + "> company");
                FacesContext.getCurrentInstance().addMessage(null, msg1);
            }
        }

        manager.addcompanies(fileNewItemsForDB);
        if (!fileNewItemsForDB.isEmpty()) {
            FacesMessage msg = new FacesMessage(fileNewItemsForDB.size() + " companies uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
