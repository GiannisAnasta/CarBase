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
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.Visibility;
import util.CommaSeparatedUtil;
import util.UrlConverterUtil;

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

    public StreamedContent exportXlsxFormats(List<Company> companies, FormatOfData formatOfData) {
        Workbook wb1;
        if (FormatOfData.EMPTY_LINE_SEPARATED.equals(formatOfData)) {
            wb1 = exportEmptyLineFormat(companies);
        } else {
            wb1 = exportNewLineFormat(companies);
        }
        final Workbook wb = wb1;
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

    public Workbook exportEmptyLineFormat(List<Company> companies) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Companies");
        XSSFCreationHelper helper = (XSSFCreationHelper) wb.getCreationHelper();
        int currentRowNumber = -1;
        int currentCellNumber = 0;

        Row row;

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
                    Cell cell = row.createCell(currentCellNumber);
                    cell.setCellValue(item);
                    try {
                        XSSFHyperlink url_link1 = helper.createHyperlink(Hyperlink.LINK_URL);
                        url_link1.setAddress(UrlConverterUtil.normalize(item));
                        cell.setHyperlink(url_link1);
                    } catch (Exception ex) {

                    }
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
                    Cell cell = row.createCell(currentCellNumber);
                    cell.setCellValue(item);
                    try {
                        XSSFHyperlink emailto = helper.createHyperlink(Hyperlink.LINK_EMAIL);
                        emailto.setAddress(item.trim());
                        cell.setHyperlink(emailto);
                    } catch (Exception ex) {
                    }
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
            ///details
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
            ///new company
            currentRowNumber = currentRowNumber + returnRowsNum;
            if (returnRowsNum == 0) {
                currentRowNumber++;
            }
        }
        return wb;
    }

    public Workbook exportNewLineFormat(List<Company> companies) {
        Workbook wb = new XSSFWorkbook();
        XSSFCreationHelper helper = (XSSFCreationHelper) wb.getCreationHelper();
        Sheet sheet = wb.createSheet("Companies");
        int currentRowNumber = 0;

        Row row;
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.GREY_40_PERCENT.index);
        style.setFont(font);

        int maxRows = companies.size() > ROWS_IN_XLSX_LIMIT ? ROWS_IN_XLSX_LIMIT : companies.size();

        for (int i = 0; i < maxRows; i++) {
            Company company = companies.get(i);
            int currentCellNumber = 0;
            row = sheet.createRow(currentRowNumber);
            ///name
            if (!filtered || list.get(1)) {

                row.createCell(currentCellNumber).setCellValue(company.getName());
            }
            currentCellNumber++;
            ///site
            if (!filtered || list.get(2)) {
                String formatted = CommaSeparatedUtil.getAsCommaSeparated(company.getSite());
                row.createCell(currentCellNumber).setCellValue(formatted);
                Cell cell = row.createCell(currentCellNumber);
                cell.setCellValue(formatted);
                try {
                    XSSFHyperlink url_link1 = helper.createHyperlink(Hyperlink.LINK_URL);
                    url_link1.setAddress(UrlConverterUtil.normalize(formatted));
                    cell.setHyperlink(url_link1);
                } catch (Exception ex) {

                }

            }

            currentCellNumber++;
            ///emails
            if (!filtered || list.get(3)) {
                String formatted = CommaSeparatedUtil.getAsCommaSeparated(company.getEmail());
                row.createCell(currentCellNumber).setCellValue(formatted);
            }
            currentCellNumber++;
            ///telephones
            if (!filtered || list.get(4)) {
                String formatted = CommaSeparatedUtil.getAsCommaSeparated(company.getTelephones());
                row.createCell(currentCellNumber).setCellValue(formatted);
            }
            currentCellNumber++;
            ///details
            if (!filtered || list.get(5)) {
                String formatted = CommaSeparatedUtil.getAsCommaSeparated(company.getDetails());
                row.createCell(currentCellNumber).setCellValue(formatted);
            }
            currentRowNumber++;
        }
        return wb;

    }

}
