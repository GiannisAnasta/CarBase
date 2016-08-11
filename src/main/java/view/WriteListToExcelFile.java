package view;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Employee;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Named
@SessionScoped
public class WriteListToExcelFile implements Serializable {

    public static void writeEmployeeListToFile(String fileEmployee, List<Employee> employeeList) throws Exception {
        Workbook workbook = null;

        if (fileEmployee.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (fileEmployee.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new Exception("invalid file name, should be xls or xlsx");
        }

        Sheet sheet = workbook.createSheet("Employee");

        Iterator<Employee> iterator = employeeList.iterator();

        int rowIndex = 0;
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            Row row = sheet.createRow(rowIndex++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(employee.getName());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(employee.getName());
        }

        //lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(fileEmployee);
        workbook.write(fos);
        fos.close();
        System.out.println(fileEmployee + " written successfully");
    }

    public static void main(String args[]) throws Exception {
        List<Employee> list = EmployeeExcelFileToList.EmployeeExcelData("Sample.xlsx");
        WriteListToExcelFile.writeEmployeeListToFile("Countries.xls", list);
    }
}
