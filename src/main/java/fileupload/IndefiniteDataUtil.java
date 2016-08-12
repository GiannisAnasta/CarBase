package fileupload;

import fileupload.IndefiniteData.Row;
import org.apache.commons.lang3.StringUtils;

public final class IndefiniteDataUtil {

    private IndefiniteDataUtil() {
    }

    static boolean isRowEmpty(Row row) {
        final char ZERO_WIDTH_NO_BREAK_SPACE = 65279;
        for (String cell : row.getData()) {
            if (StringUtils.isNotBlank(cell.replace(ZERO_WIDTH_NO_BREAK_SPACE, ' '))) {
                return false;
            }
        }
        return true;
    }

    public static String trimSymbols(String stringValue, String trimAlphabet, String ignoreAlphabet) {
        char value[] = stringValue.toCharArray();
        int startSubstring = 0;
        int endSubstring = value.length;

        StringBuilder suffix = new StringBuilder();
        while (startSubstring < endSubstring) {
            String character = Character.toString(value[startSubstring]);
            if (trimAlphabet.contains(character)) {
                startSubstring++;
            } else if (ignoreAlphabet.contains(character)) {
                startSubstring++;
                suffix.append(character);
            } else {
                break;
            }
        }

        StringBuilder postfix = new StringBuilder();
        while (startSubstring < endSubstring) {
            String character = Character.toString(value[endSubstring - 1]);
            if (trimAlphabet.contains(character)) {
                endSubstring--;
            } else if (ignoreAlphabet.contains(character)) {
                endSubstring--;
                postfix.append(character);
            } else {
                break;
            }
        }

        return suffix
                .append(stringValue.substring(startSubstring, endSubstring))
                .append(postfix)
                .toString();
    }

}
