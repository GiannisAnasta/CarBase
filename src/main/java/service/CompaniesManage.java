package service;

import fileupload.CompanyBuilder;
import filewrite.WriteListToExcelFile;
import fileupload.IndefiniteData;
import fileupload.UploadedFileDataReader;
import fileupload.validation.exceptions.UploadedFileReadException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Company;
import org.apache.poi.ss.usermodel.Workbook;
import view.ExportCompanies;
import view.FileUploadView;

@Named
@SessionScoped
public class CompaniesManage implements Serializable {

    ////
    private CompaniesService companyService;

    @EJB
    public void setUserService(CompaniesService userService) {
        this.companyService = userService;
    }

// Old Method initializing---
//     @PostConstruct
//    public void init() {
//        User user1 = new User();
//        user1.setUserName("111");
//        userService.save(user1);
//        user = userService.returnUserById(2);
//        list = userService.returnAllUsers();
//    }

//    @Inject
//    private ExportCompanies exportCompanies;
//    private static final ArrayList<Company> entities = new ArrayList<>();
//      private static final String STORAGE_FILE = "/home/giannis/Companies/storage/storageDB.xlsx";
//    @PostConstruct
//    private void onInit() {

//        IndefiniteData dataFromUploadedFile = null;
//        try {
//            dataFromUploadedFile = UploadedFileDataReader.getDataFromUploadedFile(new File(STORAGE_FILE), ';');
//            dataFromUploadedFile.removeFirstLine();
//        } catch (UploadedFileReadException ex) {
//            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        List<Company> buildCompanies = CompanyBuilder.buildCompanies(dataFromUploadedFile);
//        addcompanies(buildCompanies);

//      for(Company c: buildCompanies)
//       companyService.save(c);
//    }
//    @PreDestroy
//    private void onDestroy() {
//        saveToStorage();
//    }

//    public void saveToStorage() {
//        try {
//            exportCompanies.setFiltered(false);
//            Workbook workbook = exportCompanies.buildWorkbook(entities);
//            FileOutputStream fos = new FileOutputStream(STORAGE_FILE);
//            workbook.write(fos);
//            fos.close();
//            System.out.println(STORAGE_FILE + " written successfully");

//        } catch (Exception ex) {
//            Logger.getLogger(CompaniesManage.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
// old method of initializing---

    List<Company> cache;

    public List<Company> getList() {
        if (cache == null) {
            cache = companyService.returnAllCompanies();
        }
        return cache;
    }

    public void addCompany(Company company) {
        companyService.save(company);
        flush();
    }

    public void removeCompany(Company company) {
        companyService.delete(company);
        flush();
    }

    public void addcompanies(List<Company> companies) {
        for (Company c : companies) {
            companyService.save(c);
        }
        flush();
    }

    public void removeCompanies(List<Company> companies) {
        for (Company c : companies) {
            companyService.delete(c);
        }
        flush();
    }

    public void flush() {
        cache = null;
    }
}
