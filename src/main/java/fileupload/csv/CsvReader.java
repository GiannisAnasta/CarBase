package fileupload.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CsvReader {

    public static List<List<String>> readFile(InputStream inputStream, char separator) throws IOException {
        
        List<List<String>> data = new ArrayList<>();

        try (au.com.bytecode.opencsv.CSVReader csvReader = new au.com.bytecode.opencsv.CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8), separator)) {
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                data.add(Arrays.asList(row));
            }
        }

        return data;
    }

    private CsvReader() {
    }
}
