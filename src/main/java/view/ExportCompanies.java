package view;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Company;

import org.primefaces.model.StreamedContent;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.Visibility;

@Named
@SessionScoped
public class ExportCompanies implements Serializable {

    private final List<Boolean> list = Arrays.asList(true, true, true, true, true, true, true, true);
    private boolean filtered;

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public void onToggle(ToggleEvent e) {
        list.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
        for (Boolean b : list) {
            System.out.println(b);
        }
        System.out.println("+++++++++++++");
    }

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

    public Workbook buildWorkbook(List<Company> companies) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Companies");
        int currentRowNumber = 0;
        int currentCellNumber = 0;

        Row row = sheet.createRow(currentRowNumber);
        if (!filtered || list.get(1)) {
            row.createCell(currentCellNumber++).setCellValue("Name");
        }
        if (!filtered || list.get(2)) {
            row.createCell(currentCellNumber++).setCellValue("Site");
        }
        if (!filtered || list.get(3)) {
            row.createCell(currentCellNumber++).setCellValue("Email");
        }
        if (!filtered || list.get(4)) {
            row.createCell(currentCellNumber++).setCellValue("Telephone");
        }
        if (!filtered || list.get(5)) {
            row.createCell(currentCellNumber++).setCellValue("Details");
        }

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
            currentCellNumber = -1;//TODO start from zero and fix logic according to 0
///name
            if (!filtered || list.get(1)) {
                currentCellNumber++;
                row = sheet.createRow(currentRowNumber);
                row.createCell(currentCellNumber).setCellValue(company.getName());
            }
///site
            if (!filtered || list.get(2)) {
                currentCellNumber++;
                multiRow = company.getSite();
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
            }
///email
            if (!filtered || list.get(3)) {
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
            }
///telephone
            if (!filtered || list.get(4)) {
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
            }
//details
            if (!filtered || list.get(5)) {
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
            }
//new company
            currentRowNumber = currentRowNumber + returnRowsNum;
            if(returnRowsNum==0){
                currentRowNumber++;
            }
        }
        return wb;
    }

}
