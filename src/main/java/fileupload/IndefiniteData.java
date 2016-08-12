package fileupload;


import fileupload.validation.exceptions.FileContentException;
import fileupload.validation.exceptions.WrongFieldNumberException;
import fileupload.IndefiniteDataUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
            this.data.add(new Row(new ArrayList<>(row), number));
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

//    public void checkEmpty(String messageKey) throws FileContentException {
//        if (data.isEmpty()) {
//            throw new FileContentException(messageKey);
//        }
//    }

//    public void checkEachRowLength() throws WrongFieldNumberException {
//        int etalonRowLength = getFirstLine().getData().size();
//        for (Row row : data) {
//            if (row.getData().size() != etalonRowLength) {
//                throw new WrongFieldNumberException(row.getData().size(), etalonRowLength, row.getNumber());
//            }
//        }
//    }

//    public void preProcess() {
//        trimDuplicatedRows();
//        trimQuotes(true);
//        trimSpaces();
//        trimEmptyRows();
//    }

//    private void trimDuplicatedRows() {
//        data = new ArrayList<>(new LinkedHashSet<>(data));
//    }
//
//    private void trimEmptyRows() {
//        Iterator<Row> it = data.iterator();
//        while (it.hasNext()) {
//            Row row = it.next();
//            if (IndefiniteDataUtil.isRowEmpty(row)) {
//                it.remove();
//            }
//        }
//    }

    /**
     * This method trim quotes in indefiniteData object data.
     *
     * ‘6’;‘6.0’ --&gt; 6;6.0<br>
     * "'`„«“‘"9’”»“`'"';"'`„«“‘"9.0’”»“`'" --&gt; 9;9.0
     *
     * @param ignoreSpaces - stop on spaces between quotes or not.<br>
     * _ means space in example <br>
     * ignoreSpaces = true : "_«9';"9.0’_" --&gt; _9;9.0_ <br>
     * ignoreSpaces = false : "_«9';"9.0’_" --&gt; _«9;9.0'_
     */
//    private void trimQuotes(boolean ignoreSpaces) {
//        final String trimAlphabet = "\'`„«“‘\"’”»“`\'";
//        final String ignoreAlphabet = ignoreSpaces ? "  " : "";
//        for (Row row : data) {
//            for (int i = 0; i < row.getData().size(); i++) {
//                row.getData().set(i, IndefiniteDataUtil.trimSymbols(row.getData().get(i), trimAlphabet, ignoreAlphabet));
//            }
//        }
//    }
//
//    private void trimSpaces() {
//        for (Row row : data) {
//            for (int i = 0; i < row.getData().size(); i++) {
//                row.getData().set(i, row.getData().get(i).trim());
//            }
//        }
//    }

}
