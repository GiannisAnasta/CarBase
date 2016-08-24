package model;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private String name;
    private List<String> site = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> telephones = new ArrayList<>();
    private List<String> details = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSite() {
        return site;
    }

    public void setSite(List<String> site) {
        this.site = site;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<String> telephones) {
        this.telephones = telephones;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Company)) {
            return false;
        }
        Company other = (Company) obj;
        if (this.getName() == null && other.getName() != null) {
            return false;
        }
        if (this.getName() != null && other.getName() == null) {
            return false;
        }
        if (this.getName() != null && other.getName() != null) {
            if (!this.getName().equals(other.getName())) {
                return false;
            }
        }
        if (this.getSite().size() != other.getSite().size()) {
            return false;
        }
        if (this.getSite().size() == other.getSite().size()) {

            for (String site1 : this.getSite()) {
                if (!other.getSite().contains(site1)) {
                    return false;
                }
            }
        }
        if (this.getEmail().size() != other.getEmail().size()) {
            return false;
        }
        if (this.getEmail().size() == other.getEmail().size()) {

            for (String site1 : this.getEmail()) {
                if (!other.getEmail().contains(site1)) {
                    return false;
                }
            }
        }
        if (this.getTelephones().size() != other.getTelephones().size()) {
            return false;
        }
        if (this.getTelephones().size() == other.getTelephones().size()) {

            for (String site1 : this.getTelephones()) {
                if (!other.getTelephones().contains(site1)) {
                    return false;
                }
            }
        }
        if (this.getDetails().size() != other.getDetails().size()) {
            return false;
        }
        if (this.getDetails().size() == other.getDetails().size()) {

            for (String site1 : this.getDetails()) {
                if (!other.getDetails().contains(site1)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int key = 7;
        int result = 1;
        result = key * result + ((name == null) ? 0 : name.hashCode());
        result = key * result + ((site == null) ? 0 : hashCodeList(site));
        result = key * result + ((email == null) ? 0 : hashCodeList(email));
        result = key * result + ((telephones == null) ? 0 : hashCodeList(telephones));
        result = key * result + ((details == null) ? 0 : hashCodeList(details));
        return result;
    }

    protected static int hashCodeList(List list) {
        int hashCode = 1;
        for (Object e : list) {
            hashCode = hashCode + (e == null ? 1 : e.hashCode());
        }
        return hashCode;
    }

}
