package view;

import fileupload.CompanyBuilder;
import fileupload.IndefiniteData;
import fileupload.UploadedFileDataReader;
import fileupload.validation.exceptions.UploadedFileReadException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
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
        manager.addcompanies(uniqueCompaniesFromFile);
        System.out.println("finish");
    }

}
