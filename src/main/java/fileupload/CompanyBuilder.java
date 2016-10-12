package fileupload;

import fileupload.IndefiniteData.Row;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Company;
import org.apache.commons.lang3.StringUtils;

public class CompanyBuilder {

    public static List<Company> buildCompaniesNewLinesSeparated(IndefiniteData indefiniteData) {
        List<Company> companies = new ArrayList<>();

        for (Row row : indefiniteData.getData()) {
            List<String> rowData = row.getData();
            if (rowData.isEmpty()) {
                continue;
            }
            Company company = new Company();
            String cell = null;
            int index = 0;
            /// name
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.setName(cell);
            }
            //// site
            index++;
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.getSite().addAll(Arrays.asList(cell.split(";")));
            }
            ////email
            index++;
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.getEmail().addAll(Arrays.asList(cell.split(";")));
            }
            //telephones
            index++;
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.getTelephones().addAll(Arrays.asList(cell.split(";")));
            }
            //details
            index++;
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.getDetails().addAll(Arrays.asList(cell.split(";")));
            }
            //categories
            index++;
            if (rowData.size() == index) {//To prevend index of bound if the lenghts of rows are different.
                if (!company.equals(new Company())) {
                    companies.add(company);
                }
                continue;
            }
            cell = rowData.get(index);
            if (StringUtils.isNotBlank(cell)) {
                company.getCategories().addAll(Arrays.asList(cell.split(";")));
            }

            if (!company.equals(new Company())) {
                companies.add(company);
            }
        }
        return companies;
    }

    public static List<Company> buildCompaniesEmptyLinesSeparated(IndefiniteData indefiniteData) {
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
                if (data.size() == index) {//To prevend index of bound if the lenghts of rows are different.
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
                ///categories
                index++;
                if (data.size() == index) {
                    continue;
                }
                cell = data.get(index);
                if (StringUtils.isNotBlank(cell)) {
                    company.getCategories().add(cell);

                }

            }

        }

        return companies;
    }

}
