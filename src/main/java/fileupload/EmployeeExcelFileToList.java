package fileupload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Employee;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmployeeExcelFileToList implements Serializable {

    public static List<Employee> EmployeeExcelData(String fileName) {
        List<Employee> employeesList = new ArrayList<>();

        try ( //Create the input stream from the xlsx/xls file
                FileInputStream fis = new FileInputStream(fileName)) {

            //Create Workbook instance for xlsx/xls file input stream
            Workbook workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }

            //Get the number of sheets in the xlsx file
            int numberOfSheets = workbook.getNumberOfSheets();

            //loop through each of the sheets
            for (int i = 0; i < numberOfSheets; i++) {

                //Get the nth sheet from the workbook
                Sheet sheet = workbook.getSheetAt(i);

                //every sheet has rows, iterate over them
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    String name = "";
                    String surname = "";

                    //Get the row object
                    Row row = rowIterator.next();

                    //Every row has columns, get the column iterator and iterate over them
                    Iterator<Cell> cellIterator = row.cellIterator();
                    List<String> rawData = new ArrayList<>();
                    while (cellIterator.hasNext()) {
                        //Get the Cell object
                        Cell cell = cellIterator.next();

                        //check the cell type and process accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:

                                rawData.add(cell.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                rawData.add(String.valueOf(cell.getNumericCellValue()));

                        }
                    } //end of cell iterator

                    Employee columns = new Employee();
                    columns.setName(rawData.get(0));
                    columns.setSurname(rawData.get(1));
                    columns.setPosition(rawData.get(2));
                    employeesList.add(columns);
                } //end of rows iterator

            }
            //close file input stream
            //end of sheets for loop

        } catch (IOException e) {
            e.printStackTrace();
        }

        return employeesList;
    }

}
