package service;

import java.util.*;
import model.Company;

import static util.DuplicatesRemoverUtil.*;

public class CompaniesUtil {

    public static List<Company> getUniqueCompanies(List<Company> companies) {
        Set<Company> unique = new LinkedHashSet<>();

        unique.addAll(companies);

        return new ArrayList(unique);
    }

    public static Company getComapnyWithNoDuplicates(Company entity) {
        Company result = new Company();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setSite(removeDuplicates(entity.getSite(), true));
        result.setEmail(removeDuplicates(entity.getEmail(), false));
        result.setTelephones(removeDuplicates(entity.getTelephones(), false));
        result.setDetails(entity.getDetails());
        return result;
    }
}
