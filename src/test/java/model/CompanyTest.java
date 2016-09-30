package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import service.CompaniesUtil;

public class CompanyTest {

    @Test
    public void testGetName() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName("name1");
        instance2.setName("name1");
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNameDiff() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName("name1");
        instance2.setName("name2");
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetFirstNameNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName("111");
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetSecondNameNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName("111");
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetBothNameNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName(null);
        assertEquals(instance, instance2);
    }
/////////////// Sites

    @Test
    public void testGetDiffSites() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("111.11", "111.11"));
        instance2.setSite(Arrays.asList("222.22", "222.22"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetSameSitesDiffSize() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("111.11", "111.11", "00000"));
        instance2.setSite(Arrays.asList("111.11", "111.11"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetSameSites() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("77777.77", "22222.22"));
        instance2.setSite(Arrays.asList("77777.77", "22222.22"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetSameSitesDA() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("22222.22", "77777.77"));
        instance2.setSite(Arrays.asList("77777.77", "22222.22"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNoSites() {
        Company instance = new Company();
        Company instance2 = new Company();
//        instance.setSite(Arrays.asList("", ""));
//        instance2.setSite(Arrays.asList("", ""));
        assertEquals(instance, instance2);
    }
/////////////////emails

    @Test
    public void testGetDiffEmails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("koko@ko", "22222@22"));
        instance2.setEmail(Arrays.asList("2222@22", "22222@22"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetSameEmails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("koko@ko", "22222@22"));
        instance2.setEmail(Arrays.asList("koko@ko", "22222@22"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNoEmails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("", ""));
        instance2.setEmail(Arrays.asList("", ""));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetSameEmailDiffSize() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("111@11", "111@11", "--@@--"));
        instance2.setEmail(Arrays.asList("111@11", "111@11"));
        assertNotEquals(instance, instance2);
    }
    //telephones

    @Test
    public void testGetDiffTelephones() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("08855588", "0545"));
        instance2.setTelephones(Arrays.asList("08855554444", "08545"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetSameTelephones() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("0888888", "0888888"));
        instance2.setTelephones(Arrays.asList("0888888", "0888888"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNoTelephones() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("", ""));
        instance2.setTelephones(Arrays.asList("", ""));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetSameTelephonesDiffSize() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("0111111", "011", "--0888--"));
        instance2.setTelephones(Arrays.asList("0111111", "011"));
        assertNotEquals(instance, instance2);
    }
    ///Details

    @Test
    public void testGetDiffDetails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("022", "0333"));
        instance2.setDetails(Arrays.asList("033", "06666"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetUSameDetails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("0sss", "0sss"));
        instance2.setDetails(Arrays.asList("0sss", "0sss"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNoDetails() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("", ""));
        instance2.setDetails(Arrays.asList("", ""));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetSameDetailsDiffSize() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("111222333", "01122", "--0888ssss2333--"));
        instance2.setDetails(Arrays.asList("111222333", "01122"));
        assertNotEquals(instance, instance2);
    }
    ///Categories

    @Test
    public void testGetDiffCategories() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("0222", "0333aaa"));
        instance2.setCategories(Arrays.asList("033", "06666aaa"));
        assertNotEquals(instance, instance2);
    }

    @Test
    public void testGetUSameCategories() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("0sss", "0sss"));
        instance2.setCategories(Arrays.asList("0sss", "0sss"));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetNoCategories() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("", ""));
        instance2.setCategories(Arrays.asList("", ""));
        assertEquals(instance, instance2);
    }

    @Test
    public void testGetSameCategoriesDiffSize() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("1112222333", "011222", "--08828ssss2333--"));
        instance2.setCategories(Arrays.asList("1112222333", "011222"));
        assertNotEquals(instance, instance2);
    }
    ///////full

    @Test
    public void testGetFull() {
        Company instance = new Company();
        Company instance2 = new Company();

        instance.setName("bigi");
        instance2.setName("bigi");

        instance.setSite(Arrays.asList("koko.ko", "iiiii.ko"));
        instance2.setSite(Arrays.asList("iiiii.ko", "koko.ko"));

        instance.setEmail(Arrays.asList("koko@ko", "iiiii@ko"));
        instance2.setEmail(Arrays.asList("iiiii@ko", "koko@ko"));

        instance.setTelephones(Arrays.asList("000222", "000222", "222"));
        instance2.setTelephones(Arrays.asList("000222", "222", "000222"));

        instance.setDetails(Arrays.asList("111", "1121"));
        instance2.setDetails(Arrays.asList("1121", "111"));

        instance.setCategories(Arrays.asList("111ssw", "1121ssw"));
        instance2.setCategories(Arrays.asList("1121ssw", "111ssw"));

        assertEquals(instance, instance2);
    }

    /////////////// hashset Test/////////
    ////////name
    @Test
    public void testGetNamehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName("name1");
        instance2.setName("name1");
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNamehsDiff() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName("name1");
        instance2.setName("name2");
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetFirstNamehsNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName("111");
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSecondNamehsNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName("111");
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetBothNamehsNull() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setName(null);
        instance2.setName(null);
        assertEquals(instance.hashCode(), instance2.hashCode());
    }
///////////////// Sites

    @Test
    public void testGetDiffSiteshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("111.11", "111.11"));
        instance2.setSite(Arrays.asList("222.22", "222.22"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameSitesDiffSizehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("111.11", "111.11", "00000"));
        instance2.setSite(Arrays.asList("111.11", "111.11"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameSiteshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("77777.77", "22222.22"));
        instance2.setSite(Arrays.asList("77777.77", "22222.22"));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNoSiteshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setSite(Arrays.asList("", ""));
        instance2.setSite(Arrays.asList("", ""));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }
///////////////////emails

    @Test
    public void testGetDiffEmailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("koko@ko", "22222@22"));
        instance2.setEmail(Arrays.asList("2222@22", "22222@22"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameEmailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("koko@ko", "22222@22"));
        instance2.setEmail(Arrays.asList("koko@ko", "22222@22"));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNoEmailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("", ""));
        instance2.setEmail(Arrays.asList("", ""));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameEmailDiffSizehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setEmail(Arrays.asList("111@11", "111@11", "--@@--"));
        instance2.setEmail(Arrays.asList("111@11", "111@11"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }
    //telephones

    @Test
    public void testGetDiffTelephoneshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("08855588", "0545"));
        instance2.setTelephones(Arrays.asList("08855554444", "08545"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameTelephoneshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("0888888", "0888888"));
        instance2.setTelephones(Arrays.asList("0888888", "0888888"));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNoTelephoneshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("", ""));
        instance2.setTelephones(Arrays.asList("", ""));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameTelephonesDiffSizehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setTelephones(Arrays.asList("0111111", "011", "--0888--"));
        instance2.setTelephones(Arrays.asList("0111111", "011"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }
    ///Details

    @Test
    public void testGetDiffDetailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("022", "0333"));
        instance2.setDetails(Arrays.asList("033", "06666"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetUSameDetailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("0sss", "0sss"));
        instance2.setDetails(Arrays.asList("0sss", "0sss"));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNoDetailshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("", ""));
        instance2.setDetails(Arrays.asList("", ""));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameDetailsDiffSizehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setDetails(Arrays.asList("111222333", "01122", "--0888ssss2333--"));
        instance2.setDetails(Arrays.asList("111222333", "01122"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }
    ///Categories

    @Test
    public void testGetDiffCategorieshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("02212", "0333232"));
        instance2.setCategories(Arrays.asList("03333", "06666222"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetUSameCategorieshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("0sssw", "0sssw"));
        instance2.setCategories(Arrays.asList("0sssw", "0sssw"));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetNoCategorieshs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("", ""));
        instance2.setCategories(Arrays.asList("", ""));
        assertEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testGetSameCategoriesDiffSizehs() {
        Company instance = new Company();
        Company instance2 = new Company();
        instance.setCategories(Arrays.asList("11111222333", "0111122", "--0888sswwss2333--"));
        instance2.setCategories(Arrays.asList("11111222333", "0111122"));
        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }
    ///////full

    @Test
    public void testGetFullhs() {
        Company instance = new Company();
        Company instance2 = new Company();

        instance.setName("bigi");
        instance2.setName("22i");

        instance.setSite(Arrays.asList("koko.ko", "iiiii.ko"));
        instance2.setSite(Arrays.asList("22.ko", "223.ko"));

        instance.setEmail(Arrays.asList("koko@ko", "iiiii@ko"));
        instance2.setEmail(Arrays.asList("33@ko", "3221@ko"));

        instance.setTelephones(Arrays.asList("000222", "000222", "222"));
        instance2.setTelephones(Arrays.asList("322", "32111", "33222111"));

        instance.setDetails(Arrays.asList("111", "111"));
        instance2.setDetails(Arrays.asList("33", "22"));

        instance.setCategories(Arrays.asList("1aa", "1aa"));
        instance2.setCategories(Arrays.asList("3323", "2232"));

        assertNotEquals(instance.hashCode(), instance2.hashCode());
    }

    @Test
    public void testHashCodeList() {
        List<String> list1 = Arrays.asList("11", "22");
        List<String> list2 = Arrays.asList("22", "11");
        assertEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));

        list1 = Arrays.asList("22", "11");
        list2 = Arrays.asList("22", "11");
        assertEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));

        list1 = Arrays.asList("22", "11", "33");
        list2 = Arrays.asList("22", "11");
        assertNotEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        assertEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));

        list1 = Arrays.asList("22", "11", null);
        list2 = Arrays.asList("22", "11", null);
        assertEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));

        list1 = Arrays.asList("22", "11", null, null);
        list2 = Arrays.asList("22", "11", null);
        assertNotEquals(Company.hashCodeList(list1), Company.hashCodeList(list2));
    }

}
