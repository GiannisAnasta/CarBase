package fileupload;

import fileupload.xls.XlsReader;
import fileupload.xlsx.XlsxStreamingReader;
import fileupload.csv.CsvReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.primefaces.model.UploadedFile;

public abstract class UploadedFileDataReader {

    public static IndefiniteData getDataFromUploadedFile(UploadedFile file, char csvDelimiter) throws UploadedFileReadException {
        try (InputStream is = file.getInputstream()) {
            String fileName = file.getFileName();
            switch (extension(fileName)) {
                case "xls":
                    return new IndefiniteData(XlsReader.readFile(is));
                case "xlsx":
                    return new IndefiniteData(XlsxStreamingReader.readFile(is));
                case "csv":
                    return new IndefiniteData(CsvReader.readFile(is, csvDelimiter));
                default:
                    throw new UploadedFileReadException(fileName + " Wrong file Extension. Must be .csv or .xls(x).");
            }
        } catch (IOException ex) {
            throw new UploadedFileReadException("error reading input stream");
        } catch (InvalidFormatException ex) {
            throw new UploadedFileReadException("error reading file");
        }
    }

    public static IndefiniteData getDataFromUploadedFile(File file, char csvDelimiter) throws UploadedFileReadException {

        try {
            switch (extension(file.getName())) {
                case "xls":
                    throw new UnsupportedOperationException("xls not supported yet");
                case "xlsx":

                    return new IndefiniteData(XlsxStreamingReader.readFile(file));

                case "csv":
                    throw new UnsupportedOperationException("xls not supported yet");
                default:
                    throw new UploadedFileReadException(" Wrong file Extension. Must be .csv or .xls(x).");
            }
        } catch (IOException ex) {
            throw new UploadedFileReadException("error reading input stream");
        } catch (InvalidFormatException ex) {
            throw new UploadedFileReadException("error reading file");
        }
    }

    private static String extension(String fullPath) {
        int dotPosition = fullPath.lastIndexOf('.');
        return fullPath.substring(dotPosition + 1);
    }

    private UploadedFileDataReader() {
    }
}
