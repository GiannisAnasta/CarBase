package view;

import fileupload.EmployeeExcelFileToList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Employee;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import service.EmployeerManage;

@Named
@SessionScoped

public class FileUploadView implements Serializable {

    @Inject
    private EmployeerManage manager;

    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("start");

        //get uploaded file from the event
        UploadedFile uploadedFile = (UploadedFile) event.getFile();

        //create an InputStream from the uploaded file
        InputStream inputStr = null;
        try {
            inputStr = uploadedFile.getInputstream();
        } catch (IOException e) {
            //log error
        }

        //create destination File
        String destPath = "/home/giannis/CarBase/storage/";
        File destFile = new File(destPath + uploadedFile.getFileName());

        try {

            FileUtils.copyInputStreamToFile(inputStr, destFile);
            List<Employee> EmployeeExcelData = EmployeeExcelFileToList.employeeExcelData(destPath + uploadedFile.getFileName());
            manager.addEmployees(EmployeeExcelData);
            System.out.println("success");
        } catch (IOException ex) {
            System.out.println("fail");
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("finish");
    }

}
