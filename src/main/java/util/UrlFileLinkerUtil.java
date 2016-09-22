package util;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

public class UrlFileLinkerUtil {

    private Workbook workbook;

    private static UrlValidator urlValidator = new UrlValidator(new String[] {"http", "https"}, UrlValidator.ALLOW_2_SLASHES);

    public UrlFileLinkerUtil(Workbook workbook) {
        this.workbook = workbook;
    }

    public void linkCellToUrl(String site, Cell cell) {
        String url = null;
        if (!site.toLowerCase().matches("^\\w+://.*")) {
            url = "http://" + site;
        }
        if (urlValidator.isValid(url)) {
            XSSFHyperlink link = new XSSFHyperlink(workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL));
            link.setAddress(url);
            cell.setHyperlink(link);
        }
    }
}

