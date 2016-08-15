package service;

import fileupload.CompanyBuilder;
import filewrite.WriteListToExcelFile;
import fileupload.IndefiniteData;
import fileupload.UploadedFileDataReader;
import fileupload.UploadedFileReadException;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Company;
import view.FileUploadView;

@Named
@SessionScoped
public class CompaniesManage implements Serializable {

    private static final ArrayList<Company> entities = new ArrayList<>();

    private static final String STORAGE_FILE = "/home/giannis/Companies/storage/storageDB.xlsx";

    @PostConstruct
    private void onInit() {

        IndefiniteData dataFromUploadedFile = null;
        try {
            dataFromUploadedFile = UploadedFileDataReader.getDataFromUploadedFile(new File(STORAGE_FILE), ';');
        } catch (UploadedFileReadException ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }

        addcompanies(CompanyBuilder.buildCompanies(dataFromUploadedFile));

    }

    @PreDestroy
    private void onDestroy() {
        saveToStorage();
    }

    public void saveToStorage() {
//        try {
//            //Todo backup of original file
//            WriteListToExcelFile.writeCompanyListToFile(STORAGE_FILE, entities);
//        } catch (Exception ex) {
//            Logger.getLogger(CompaniesManage.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public ArrayList<Company> getList() {
        return entities;
    }

    public void addCompany(Company company) {
        entities.add(company);
    }

    public void removeCompany(Company company) {
        entities.remove(company);
    }

    public void addcompanies(List<Company> companies) {
        entities.addAll(companies);
    }

    public void removeCompanies(List<Company> companies) {
        entities.removeAll(companies);
    }

}
