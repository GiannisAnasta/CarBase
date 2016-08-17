package view;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Company;

import org.primefaces.model.StreamedContent;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import javax.naming.spi.DirStateFactory.Result;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;

@Named
@SessionScoped
public class ExportCompanies implements Serializable {

    class SimpleThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    }

    public StreamedContent exportXlsx(List<Company> companies) {

        final Workbook wb = buildWorkbook(companies);
        final PipedInputStream in = new PipedInputStream();
        try {
            final PipedOutputStream out = new PipedOutputStream(in);
            new SimpleThreadFactory().newThread(
                    new Runnable() {
                @Override
                public void run() {
                    try {
                        wb.write(out);
                    } catch (IOException e) {
                        System.err.println("Error creating xlsx report: ");
                    }
                }

            }).start();
        } catch (IOException e) {
            System.err.println("Error creating xlsx report: ");
        }

        return new DefaultStreamedContent(in,
                "application/xml", "Companies.xlsx");
    }

    public static final int ROWS_IN_XLSX_LIMIT = 500000;

    public static Workbook buildWorkbook(List<Company> companies) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Companies");
        int currentRowNumber = 0;
        int currentCellNumber = 0;

        Row row = sheet.createRow(currentRowNumber);
        row.createCell(currentCellNumber++).setCellValue("Name");
        row.createCell(currentCellNumber++).setCellValue("Site");
        row.createCell(currentCellNumber++).setCellValue("Email");
        row.createCell(currentCellNumber++).setCellValue("Telephone");
        row.createCell(currentCellNumber++).setCellValue("Details");

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFont(font);
        int maxRows = companies.size() > ROWS_IN_XLSX_LIMIT ? ROWS_IN_XLSX_LIMIT : companies.size();

        for (int i = 0; i < maxRows; i++) {
            Company company = companies.get(i);
            List<String> multiRow;
            int returnRowsNum = 0;
            currentRowNumber++;
            int startRowNum = currentRowNumber;
///name
            row = sheet.createRow(currentRowNumber);
            currentCellNumber = 0;
            row.createCell(currentCellNumber).setCellValue(company.getName() + "r" + currentRowNumber + "c" + currentCellNumber);
///site
            currentCellNumber++;
            multiRow = company.getSite();
            for (String item : multiRow) {
                row = sheet.getRow(currentRowNumber);
                if (row == null) {
                    row = sheet.createRow(currentRowNumber);
                }
                row.createCell(currentCellNumber).setCellValue(item + "r" + currentRowNumber + "c" + currentCellNumber);
                currentRowNumber++;
            }
            currentRowNumber = startRowNum;
            returnRowsNum = Math.max(returnRowsNum, multiRow.size());
///email
            currentCellNumber++;
            multiRow = company.getEmail();
            for (String item : multiRow) {
                row = sheet.getRow(currentRowNumber);
                if (row == null) {
                    row = sheet.createRow(currentRowNumber);
                }
                row.createCell(currentCellNumber).setCellValue(item);
                currentRowNumber++;
            }
            currentRowNumber = startRowNum;
            returnRowsNum = Math.max(returnRowsNum, multiRow.size());
///telephone
            currentCellNumber++;
            multiRow = company.getTelephones();
            for (String item : multiRow) {
                row = sheet.getRow(currentRowNumber);
                if (row == null) {
                    row = sheet.createRow(currentRowNumber);
                }
                row.createCell(currentCellNumber).setCellValue(item);
                currentRowNumber++;
            }
            currentRowNumber = startRowNum;
            returnRowsNum = Math.max(returnRowsNum, multiRow.size());
//details            
            currentCellNumber++;
             multiRow = company.getDetails();
            for (String item : multiRow) {
                row = sheet.getRow(currentRowNumber);
                if (row == null) {
                    row = sheet.createRow(currentRowNumber);
                }
                row.createCell(currentCellNumber).setCellValue(item);
                currentRowNumber++;
            }
            currentRowNumber = startRowNum;
            returnRowsNum = Math.max(returnRowsNum, multiRow.size());
//new company
            currentRowNumber = currentRowNumber + returnRowsNum;
        }
        return wb;
    }

}
