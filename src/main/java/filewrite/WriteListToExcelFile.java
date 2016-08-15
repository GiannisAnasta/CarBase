package filewrite;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import model.Company;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteListToExcelFile implements Serializable {

    public static void writeCompanyListToFile(String fileCompany, List<Company> companiesList) throws Exception {
        Workbook workbook = null;

        if (fileCompany.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (fileCompany.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new Exception("invalid file name, should be xls or xlsx");
        }

        Sheet sheet = workbook.createSheet("Company");

        Iterator<Company> iterator = companiesList.iterator();

//        int rowIndex = 0;
//        while (iterator.hasNext()) {
//           Company company = iterator.next();
//          Row row = sheet.createRow(rowIndex++);
//          Cell cell0 = row.createCell(0);
//          cell0.setCellValue(company.getName());
//            Cell cell1 = row.createCell(1);
//           cell1.setCellValue(company.getSite());
//           Cell cell2 = row.createCell(2);
//           cell2.setCellValue(company.getEmail());
//           Cell cell3 = row.createCell(3);
//           cell3.setCellValue(company.getTelephones());
//            Cell cell4 = row.createCell(4);
//           cell4.setCellValue(company.getDetails());
//
//        }
        //lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(fileCompany);
        workbook.write(fos);
        fos.close();
        System.out.println(fileCompany + " written successfully");
    }

}
