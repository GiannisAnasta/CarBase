package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class TrashManage implements Serializable {

    private final static String[] positions;

    static {
        positions = new String[10];
        positions[0] = "Office manager";
        positions[1] = "Receptionist";
        positions[2] = "Marketing manager";
        positions[3] = "Purchasing manager";
        positions[4] = "Professional staff";
        positions[5] = "Operations manager";
        positions[6] = "Chief Executive Officer ";
        positions[7] = "Chief Operating Officer";
        positions[8] = "Developer";
        positions[9] = "Sales manager";

    }

    public List<String> getEmployeers() {
        return Arrays.asList(positions);
    }

}
