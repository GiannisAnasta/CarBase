package filewrite;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;
import model.Company;

import org.apache.poi.ss.usermodel.Workbook;
import view.ExportCompanies;

public class WriteListToExcelFile implements Serializable {

    public void writeCompanyListToFile(String fileCompany, List<Company> companiesList) throws Exception {
        Workbook workbook = new ExportCompanies().exportEmptyLineFormat(companiesList);

        //lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(fileCompany);
        workbook.write(fos);
        fos.close();
        System.out.println(fileCompany + " written successfully");
    }

}
