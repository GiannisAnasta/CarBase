package service;

import java.util.*;
import model.Company;

public class CompaniesUtil {

    public static List<Company> getUniqueCompanies(List<Company> companies) {
        Set<Company> unique = new LinkedHashSet<>();

        unique.addAll(companies);

        return new ArrayList(unique);
    }

}
