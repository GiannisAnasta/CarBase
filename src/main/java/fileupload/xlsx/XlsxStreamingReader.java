package fileupload.xlsx;

import com.monitorjbl.xlsx.StreamingReader;
import com.monitorjbl.xlsx.impl.StreamingCell;
import fileupload.validation.exceptions.UploadedFileReadException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XlsxStreamingReader {

    private static final List<String> EMPTY_ROW = Collections.emptyList();

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
            int lastIndex = -1;
            for (Cell cell1 : row) {
                lastIndex = Math.max(lastIndex, cell1.getColumnIndex());
            }
            String[] cellsInRow = new String[lastIndex + 1];
            for (int i = 0; i < lastIndex + 1; i++) {
                cellsInRow[i] = "";
            }
            for (Cell cell1 : row) {
                StreamingCell cell = (StreamingCell) cell1;
                if (cell == null) {
                    cellsInRow[cell1.getColumnIndex()] = "";
                    continue;
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:
                        String cellToAdd = cell.getStringCellValue();
                        cellToAdd = cellToAdd.replaceFirst("^\'", "");
                        try {
                            cellsInRow[cell1.getColumnIndex()] = cellToAdd;
                        } catch (IndexOutOfBoundsException e) {

                        }
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            cellsInRow[cell1.getColumnIndex()] = cell.getDateCellValue().toString();
                        } else {
                            double numericValue = cell.getNumericCellValue();
                            if (cellsInRow.length == 0) {
                                cellsInRow[cell1.getColumnIndex()] = String.valueOf(Math.round(numericValue));
                            } else {
                                Double asDouble = numericValue;
                                if (asDouble.floatValue() - asDouble.intValue() > 0) {
                                    cellsInRow[cell1.getColumnIndex()] = (new BigDecimal(asDouble)).setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
                                } else {
                                    cellsInRow[cell1.getColumnIndex()] = Integer.toString(asDouble.intValue());
                                }
                            }
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                    case HSSFCell.CELL_TYPE_FORMULA:
                        final String trueAsString = "1";
                        cellsInRow[cell1.getColumnIndex()]
                                = Boolean.toString(
                                        Boolean.parseBoolean(cell.getStringCellValue())
                                        || cell.getContents() != null
                                        && trueAsString.equals(cell.getStringCellValue())
                                );
                        break;
                    default:
                        cellsInRow[cell1.getColumnIndex()] = cell.getStringCellValue();
                        break;
                }
            }
            while (row.getRowNum() > data.size()) {
                data.add(EMPTY_ROW);
            }
            data.add(Arrays.asList(cellsInRow));
        }
        return data;
    }

    private XlsxStreamingReader() {
    }
}
