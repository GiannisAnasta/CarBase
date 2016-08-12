package fileupload.xlsx;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingCell;
import fileupload.UploadedFileReadException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XlsxStreamingReader {

    public static List<List<String>> readFile(InputStream inputStream) throws IOException, InvalidFormatException, UploadedFileReadException {
        Workbook workbook = null;
        try {
            workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(inputStream);
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
    public static List<List<String>> readFile(File file) throws IOException, InvalidFormatException, UploadedFileReadException {
        Workbook workbook = null;
        try {
            workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(file);
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
        for (Row row : sheet) {
            List<String> cellsInRow = new ArrayList<>();
            for (Cell cell1 : row) {
                StreamingCell cell = (StreamingCell) cell1;
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
                            cellsInRow.add(cell.getDateCellValue().toString());
                        } else {
                            double numericValue = cell.getNumericCellValue();
                            if (cellsInRow.isEmpty()) {
                                cellsInRow.add(String.valueOf(Math.round(numericValue)));
                            } else {
                                Double asDouble = numericValue;
                                if (asDouble.floatValue() - asDouble.intValue() > 0) {
                                    cellsInRow.add((new BigDecimal(asDouble)).setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
                                } else {
                                    cellsInRow.add(Integer.toString(asDouble.intValue()));
                                }
                            }
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                    case HSSFCell.CELL_TYPE_FORMULA:
                        final String trueAsString = "1";
                        cellsInRow.add(
                                Boolean.toString(
                                        Boolean.parseBoolean(cell.getStringCellValue())
                                        || cell.getContents() != null
                                        && trueAsString.equals(cell.getStringCellValue())
                                ));
                        break;
                    default:
                        cellsInRow.add(cell.getStringCellValue());
                        break;
                }
            }
            data.add(cellsInRow);
        }
        return data;
    }

    private XlsxStreamingReader() {
    }
}
