package view;

import fileupload.IndefiniteData;
import fileupload.IndefiniteData.Row;
import fileupload.UploadedFileDataReader;
import fileupload.UploadedFileReadException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Employee;
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
        IndefiniteData dataFromUploadedFile = null;
        try {
            dataFromUploadedFile = UploadedFileDataReader.getDataFromUploadedFile(uploadedFile, ';');
        } catch (UploadedFileReadException ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Employee> fromFile = new ArrayList<>();
        for (Row row : dataFromUploadedFile.getData()) {
            Employee employee = new Employee();
            employee.setName(row.getData().get(0));
            employee.setSurname(row.getData().get(1));
            employee.setPosition(row.getData().get(2));
            fromFile.add(employee);
        }

        manager.addEmployees(fromFile);

        System.out.println("finish");
    }

}
