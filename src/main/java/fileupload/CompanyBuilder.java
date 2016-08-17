package fileupload;

import fileupload.IndefiniteData.Row;
import java.util.ArrayList;
import java.util.List;
import model.Company;
import org.apache.commons.lang3.StringUtils;

public class CompanyBuilder {

    public static List<Company> buildCompanies(IndefiniteData indefiniteData) {
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        if (!indefiniteData.getData().isEmpty()) {
            Row lastRow = indefiniteData.getData().get(indefiniteData.getData().size() - 1);
            if (!IndefiniteDataUtil.isRowEmpty(lastRow)) {
                indefiniteData.getData().add(new Row(new ArrayList<String>(), 666));
            }
        }

        for (Row row : indefiniteData.getData()) {
            if (IndefiniteDataUtil.isRowEmpty(row)) {
                System.out.println("new company");
                companies.add(company);
                company = new Company();
            } else {

                List<String> data = row.getData();

                String cell = null;
                int index = 0;

                /// name
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.setName(cell);

                }
                //// site  
                index++;
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.getSite().add(cell);
                }
                ////email  
                index++;
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.getEmail().add(cell);
                }
                //telephones
                index++;
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.getTelephones().add(cell);
                }
                ///details
                index++;
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.getDetails().add(cell);

                }

            }

        }

        return companies;
    }

}
