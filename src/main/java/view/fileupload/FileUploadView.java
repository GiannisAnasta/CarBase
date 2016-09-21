package view.fileupload;

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
import service.CompaniesService;
import service.CompaniesUtil;
import util.LocalizationUtil;
import view.FormatOfData;

@Named
@SessionScoped

public class FileUploadView implements Serializable {

    @Inject
    private CompaniesService service;
    private FormatOfData formatOfData = FormatOfData.NEW_LINE_SEPARATED;
    @Inject
    private Preview preview;

    public FormatOfData getFormatOfData() {
        return formatOfData;
    }

    public void setFormatOfData(FormatOfData formatOfData) {
        this.formatOfData = formatOfData;
    }

    public Preview getPreview() {
        return preview;
    }

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("start");

        //get uploaded file from the event
        UploadedFile uploadedFile = (UploadedFile) event.getFile();
        IndefiniteData dataFromUploadedFile = null;
        try {
            dataFromUploadedFile = UploadedFileDataReader.getDataFromUploadedFile(uploadedFile, ';');
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
        preview.setData(buildedCompanies);
    }

    public void cancel() {
        preview.getData().clear();
    }

    public void loadToBD() {

        List<String> names = new ArrayList<>();
        for (Company company : preview.getData()) {
            try {
                service.save(company);
            } catch (javax.ejb.EJBException ex) {
                names.add(company.getName());
                Company find = service.find(company.getName());
                find.getSite().addAll(company.getSite());
                find.getEmail().addAll(company.getEmail());
                find.getDetails().addAll(company.getDetails());
                find.getTelephones().addAll(company.getTelephones());
                service.update(find);
            }
        }
        preview.getData().clear();

        if (!names.isEmpty()) {
            String message = LocalizationUtil.getMessage("merged_items");
            for (String name : names) {
                message += name + "\n";
            }
            FacesMessage msg = new FacesMessage(names.size() + " " + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (!preview.getData().isEmpty()) {
            String message = LocalizationUtil.getMessage("messages_companies_uploaded");
            FacesMessage msg = new FacesMessage(preview.getData().size() + " " + message);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
