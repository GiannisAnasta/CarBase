package util;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class DuplicatesRemoverUtilTest {

    @Test
    public void removeDuplicatesIgnorecase() {
        List<String> first = Arrays.asList("www.test.com", "koko.ko", "MnDeqw.Ru");
        List<String> second = Arrays.asList("www.test.com", "koko.ko", "KOKO.ko", "Www.Test.com", "MnDeqw.Ru", "koko.ko", "mndeqw.ru");

        List<String> result = DuplicatesRemoverUtil.removeDuplicates(second, true);

        assertEquals(first, result);
    }

    @Test
    public void removeDuplicatesCaseSensitive() {
        List<String> first = Arrays.asList("www.test.com", "koko.ko", "KOKO.ko", "Www.Test.com","MnDeqw.Ru", "Meow@cat.ua", "meoW@Cat.ua");
        List<String> second = Arrays.asList("www.test.com", "koko.ko", "KOKO.ko","KOKO.ko", "Www.Test.com", "MnDeqw.Ru", "koko.ko", "Meow@cat.ua","MnDeqw.Ru", "KOKO.ko", "koko.ko", "meoW@Cat.ua");

        List<String> result = DuplicatesRemoverUtil.removeDuplicates(second, false);

        assertEquals(first, result);
    }
}
