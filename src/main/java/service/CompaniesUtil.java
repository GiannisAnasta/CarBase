package service;

import java.util.ArrayList;
import java.util.List;
import model.Company;

public class CompaniesUtil {

    public static List<Company> getUniqueCompanies(List<Company> companies) {
        List<Company> unique = new ArrayList<>();
        // IMPLEMENTATION

        loop:
        for (Company company : companies) {
            for (Company Ucompany : unique) {
                if (isCompaniesEquals(company, Ucompany)) {
                    continue loop;
                }
            }
            unique.add(company);
        }

        // IMPLEMENTATION
        return unique;
    }

    private static boolean isCompaniesEquals(Company company1, Company company2) {
        //names
        if (company1.getName() == null && company2.getName() != null) {
            return false;
        }
        if (company1.getName() != null && company2.getName() == null) {
            return false;
        }
        if (company1.getName() != null && company2.getName() != null) {
            if (!company1.getName().equals(company2.getName())) {
                return false;
            }
        }

        //sites
        if (company1.getSite().size() != company2.getSite().size()) {
            return false;
        }
        if (company1.getSite().size() == company2.getSite().size()) {

            for (String site1 : company1.getSite()) {
                if (!company2.getSite().contains(site1)) {
                    return false;
                }
            }
        }
        //emails
        if (company1.getEmail().size() != company2.getEmail().size()) {
            return false;
        }
        if (company1.getEmail().size() == company2.getEmail().size()) {

            for (String site1 : company1.getEmail()) {
                if (!company2.getEmail().contains(site1)) {
                    return false;
                }
            }
        }
        //Telephones
        if (company1.getTelephones().size() != company2.getTelephones().size()) {
            return false;
        }
        if (company1.getTelephones().size() == company2.getTelephones().size()) {

            for (String site1 : company1.getTelephones()) {
                if (!company2.getTelephones().contains(site1)) {
                    return false;
                }
            }
        }
        //Details
        if (company1.getDetails().size() != company2.getDetails().size()) {
            return false;
        }
        if (company1.getDetails().size() == company2.getDetails().size()) {

            for (String site1 : company1.getDetails()) {
                if (!company2.getDetails().contains(site1)) {
                    return false;
                }
            }
        }
        return true;
    }

}
