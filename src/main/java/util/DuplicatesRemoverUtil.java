package util;

import java.util.*;

public class DuplicatesRemoverUtil {
    public static List<String> removeDuplicates(List<String> original, boolean ignoreCase) {
        List<String> result = new ArrayList<>();
        Set<String> uniq = new HashSet<>();
        for (String s : original) {
            if (uniq.add(ignoreCase ? s.toLowerCase().trim() : s.trim())) {
                result.add(s);
            }
        }
        return result;
    }
}
