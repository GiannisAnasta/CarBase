package fileupload.xls;

import fileupload.UploadedFileReadException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public abstract class XlsReader {

    public static List<List<String>> readFile(InputStream inputStream) throws IOException, InvalidFormatException, UploadedFileReadException {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (RuntimeException ex) {
            if (ex.getMessage().equals("Unexpected missing row when some rows already present")) {
                throw new UploadedFileReadException("Minor inconsistencies found in the file being uploaded. To eliminate them please save a copy of the file using an xls-authoring application and try again uploading a copy.");
            } else {
                throw new IOException(ex.getMessage());
            }
        }
        Sheet sheet = workbook.getSheetAt(0);

        List<List<String>> data = extractData(sheet);
        return data;

    }

    private static List<List<String>> extractData(Sheet sheet) {
        List<List<String>> data = new ArrayList<>();
        int firstRowNumOfCells = 0;
        for (Row row : sheet) {
            if (firstRowNumOfCells == 0 && row.getPhysicalNumberOfCells() != 0) {
                firstRowNumOfCells = row.getLastCellNum();
            }
            List<String> cellsInRow = new ArrayList<>();
            for (int cellCounter = 0; cellCounter < firstRowNumOfCells || cellCounter < row.getLastCellNum(); cellCounter++) {
                Cell cell = row.getCell(cellCounter);
                if (cell == null) {
                    cellsInRow.add("");
                    continue;
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        String cellToAdd = cell.getStringCellValue();
                        cellToAdd = cellToAdd.replaceFirst("^\'", "");
                        cellsInRow.add(cellToAdd);
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            cell.getDateCellValue();
                        } else {
                            double dVal = cell.getNumericCellValue();

                            if (cellsInRow.isEmpty()) {
                                long longVal = Math.round(dVal);
                                cellsInRow.add(String.valueOf(longVal));
                            } else {
                                Double dblVal = dVal;
                                if (dblVal.floatValue() - dblVal.intValue() > 0) {
                                    cellsInRow.add((new BigDecimal(dblVal)).setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                                } else {
                                    cellsInRow.add(Integer.toString(dblVal.intValue()));
                                }
                            }
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        cellsInRow.add(Boolean.toString(cell.getBooleanCellValue()));
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        try {
                            cellsInRow.add(Boolean.toString(cell.getBooleanCellValue()));
                        } catch (Exception ex) {
//                            log.info("Can't parse to boolean {}", currentCell);
                            cellsInRow.add(cell.getCellFormula());
                        }
                        break;
                    default:
                        cellsInRow.add(cell.toString());
                        break;
                }
            }
            data.add(cellsInRow);
        }
        return data;
    }

    private XlsReader() {
    }
}
