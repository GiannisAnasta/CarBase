package fileupload.validation.exceptions;

public class WrongFieldNumberException extends Exception {

    private final int rowLength;
    private final int formatLength;
    private final int rowNumber;

    public WrongFieldNumberException(int rowLength, int formatLength, int rowNumber) {
        this.rowNumber = rowNumber;
        this.rowLength = rowLength;
        this.formatLength = formatLength;
    }

    public int getRowLength() {
        return rowLength;
    }

    public int getFormatLength() {
        return formatLength;
    }

    public int getRowNumber() {
        return rowNumber;
    }

}
