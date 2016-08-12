package fileupload.validation.exceptions;

public class FileContentException extends Exception {

    private final String key;

    public FileContentException(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
