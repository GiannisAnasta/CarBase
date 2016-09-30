package service;

import java.util.Arrays;
import java.util.List;
import model.Company;
import org.junit.*;
import static org.junit.Assert.*;

public class CompaniesUtilTest {
////names

    @Test
    public void testGetUniqueCompaniesDiffNames() {
        Company first = new Company();
        Company second = new Company();
        first.setName("111");
        second.setName("222");
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameNames() {
        Company first = new Company();
        Company second = new Company();
        first.setName("111");
        second.setName("111");
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesFirstNameNull() {
        Company first = new Company();
        Company second = new Company();
        first.setName(null);
        second.setName("111");
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSecondNameNull() {
        Company first = new Company();
        Company second = new Company();
        first.setName("111");
        second.setName(null);
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesBothNameNull() {
        Company first = new Company();
        Company second = new Company();
        first.setName(null);
        second.setName(null);
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    /////// sites
    @Test
    public void testGetUniqueCompaniesDiffSites() {
        Company first = new Company();
        Company second = new Company();
        first.setSite(Arrays.asList("111.11", "111.11"));
        second.setSite(Arrays.asList("222.22", "222.22"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameSitesDiffSize() {
        Company first = new Company();
        Company second = new Company();
        first.setSite(Arrays.asList("111.11", "111.11", "00000"));
        second.setSite(Arrays.asList("111.11", "111.11"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameSites() {
        Company first = new Company();
        Company second = new Company();
        first.setSite(Arrays.asList("77777.77", "22222.22"));
        second.setSite(Arrays.asList("77777.77", "22222.22"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesNoSites() {
        Company first = new Company();
        Company second = new Company();
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }
/////// emails

    @Test
    public void testGetUniqueCompaniesDiffEmails() {
        Company first = new Company();
        Company second = new Company();
        first.setEmail(Arrays.asList("koko@ko", "22222@22"));
        second.setEmail(Arrays.asList("2222@22", "22222@22"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameEmails() {
        Company first = new Company();
        Company second = new Company();
        first.setEmail(Arrays.asList("koko@ko", "22222@22"));
        second.setEmail(Arrays.asList("koko@ko", "22222@22"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesNoEmails() {
        Company first = new Company();
        Company second = new Company();
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameEmailDiffSize() {
        Company first = new Company();
        Company second = new Company();
        first.setEmail(Arrays.asList("111@11", "111@11", "--@@--"));
        second.setEmail(Arrays.asList("111@11", "111@11"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }
    //telephones

    @Test
    public void testGetUniqueCompaniesDiffTelephones() {
        Company first = new Company();
        Company second = new Company();
        first.setTelephones(Arrays.asList("08855588", "0545"));
        second.setTelephones(Arrays.asList("08855554444", "08545"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameTelephones() {
        Company first = new Company();
        Company second = new Company();
        first.setTelephones(Arrays.asList("0888888", "0888888"));
        second.setTelephones(Arrays.asList("0888888", "0888888"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesNoTelephones() {
        Company first = new Company();
        Company second = new Company();
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameTelephonesDiffSize() {
        Company first = new Company();
        Company second = new Company();
        first.setTelephones(Arrays.asList("0111111", "011", "--0888--"));
        second.setTelephones(Arrays.asList("0111111", "011"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }
    ///Details

    @Test
    public void testGetUniqueCompaniesDiffDetails() {
        Company first = new Company();
        Company second = new Company();
        first.setDetails(Arrays.asList("022", "0333"));
        second.setDetails(Arrays.asList("033", "06666"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameDetails() {
        Company first = new Company();
        Company second = new Company();
        first.setDetails(Arrays.asList("0sss", "0sss"));
        second.setDetails(Arrays.asList("0sss", "0sss"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesNoDetails() {
        Company first = new Company();
        Company second = new Company();
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameDetailsDiffSize() {
        Company first = new Company();
        Company second = new Company();
        first.setDetails(Arrays.asList("111222333", "01122", "--0888ssss2333--"));
        second.setDetails(Arrays.asList("111222333", "01122"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }
    ///Categories

    @Test
    public void testGetUniqueCompaniesDiffCategories() {
        Company first = new Company();
        Company second = new Company();
        first.setCategories(Arrays.asList("0212", "0311133"));
        second.setCategories(Arrays.asList("03233", "06662336"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameCategories() {
        Company first = new Company();
        Company second = new Company();
        first.setCategories(Arrays.asList("0sswww2s", "0sswww2s"));
        second.setCategories(Arrays.asList("0sswww2s", "0sswww2s"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesNoCategories() {
        Company first = new Company();
        Company second = new Company();
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(1, unique.size());
    }

    @Test
    public void testGetUniqueCompaniesSameCategoriesDiffSize() {
        Company first = new Company();
        Company second = new Company();
        first.setCategories(Arrays.asList("11122233333", "0112232", "--0888sv2333--"));
        second.setCategories(Arrays.asList("11122233333", "0112232"));
        List<Company> notUnique = Arrays.asList(first, second);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);
        assertEquals(2, unique.size());
    }

    ///////full
    @Test
    public void testGetUniqueCompaniesFull() {
        Company first = new Company();
        Company second = new Company();
        Company third = new Company();

        //fill first
        first.setName("bigi");
        second.setName("bigi");
        third.setName("2313");

        first.setSite(Arrays.asList("koko.ko", "iiiii.ko"));
        second.setSite(Arrays.asList("iiiii.ko", "koko.ko"));
        third.setSite(Arrays.asList("77777.77", "22222.ko"));

        first.setEmail(Arrays.asList("koko@ko", "iiiii@ko"));
        second.setEmail(Arrays.asList("iiiii@ko", "koko@ko"));
        third.setEmail(Arrays.asList("k333@@", "i@@@"));

        first.setTelephones(Arrays.asList("000222", "000222", "222"));
        second.setTelephones(Arrays.asList("000222", "222", "000222"));
        third.setTelephones(Arrays.asList("io", "k"));

        first.setDetails(Arrays.asList("112", "111"));
        second.setDetails(Arrays.asList("111", "112"));
        third.setDetails(Arrays.asList("2334", "2334"));

        first.setCategories(Arrays.asList("122ww12", "1aa11"));
        second.setCategories(Arrays.asList("1aa11", "122ww12"));
        third.setCategories(Arrays.asList("aaas", "aaas"));

        List<Company> notUnique = Arrays.asList(first, second, third);
        List<Company> unique = CompaniesUtil.getUniqueCompanies(notUnique);

        assertEquals(2, unique.size());

    }

}
