package fileupload;

import java.util.ArrayList;
import java.util.List;

public class IndefiniteData {

    public static class Row {

        private final List<String> data;
        private final int number;

        public Row(List<String> data, int number) {
            this.data = data;
            this.number = number;
        }

        public List<String> getData() {
            return data;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }
            if (anObject instanceof Row) {
                Row another = (Row) anObject;
                return data.equals(another.data);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

    }

    private List<Row> data;
    private boolean headerExists;

    public IndefiniteData(List<List<String>> data) {
        this.data = new ArrayList<>();
        int number = 1;
        for (List<String> row : data) {
            ArrayList<String> trimmed = new ArrayList<>();
            for (String item : row) {
                trimmed.add(item.trim());
            }
            this.data.add(new Row(trimmed, number));
            number++;
        }
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }

    public boolean isHeaderExists() {
        return headerExists;
    }

    public void setHeaderExists(boolean headerExists) {
        this.headerExists = headerExists;
    }

    public Row getFirstLine() {
        return data.get(0);
    }

    public void removeFirstLine() {
        data.remove(0);
    }

}
