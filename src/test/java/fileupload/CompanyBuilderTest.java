package fileupload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import model.Company;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class CompanyBuilderTest {

    @Test
    public void testBuildCompaniesNewLinesSeparated() {

        CompanyBuilder cb = new CompanyBuilder();
        List<Company> expected = new ArrayList<>();
        Company company = new Company();
        company.setName("name 1");
        company.getSite().add("site 1");
        company.getEmail().add("email 1");
        company.getTelephones().add("telephone 1");
        expected.add(company);
        company = new Company();
        company.setName("name 2");
        company.getSite().add("site 2");
        company.getSite().add("site 2.2");
        company.getEmail().add("email 2");
        company.getEmail().add("email 2.2");
        company.getTelephones().add("telephone 2");
        company.getTelephones().add("telephone 2.2");
        expected.add(company);

        IndefiniteData indefiniteData = new IndefiniteData(
                Arrays.asList(
                        Arrays.asList("name 1",
                                "site 1",
                                "email 1",
                                "telephone 1"),
                        Arrays.asList("name 2",
                                "site 2;site 2.2",
                                "email 2;email 2.2",
                                "telephone 2;telephone 2.2")
                )
        );
        List<Company> actual = cb.buildCompaniesNewLinesSeparated(indefiniteData);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Ignore
    @Test
    public void testBuildCompaniesEmptyLinesSeparated() {
        // todo make test
    }

}
