package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import view.converter.DateConverter;

@Entity
@Table(name = "company3")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    private List<String> site = new ArrayList<>();
    private List<String> email = new ArrayList<>();
    private List<String> telephones = new ArrayList<>();
    private List<String> details = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private Date time = new Date();

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
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

            for (String email1 : this.getEmail()) {
                if (!other.getEmail().contains(email1)) {
                    return false;
                }
            }
        }
        if (this.getTelephones().size() != other.getTelephones().size()) {
            return false;
        }
        if (this.getTelephones().size() == other.getTelephones().size()) {

            for (String tele1 : this.getTelephones()) {
                if (!other.getTelephones().contains(tele1)) {
                    return false;
                }
            }
        }
        if (this.getDetails().size() != other.getDetails().size()) {
            return false;
        }
        if (this.getDetails().size() == other.getDetails().size()) {

            for (String detail1 : this.getDetails()) {
                if (!other.getDetails().contains(detail1)) {
                    return false;
                }
            }
        }
        if (this.getCategories().size() != other.getCategories().size()) {
            return false;
        }
        if (this.getCategories().size() == other.getCategories().size()) {

            for (String category1 : this.getCategories()) {
                if (!other.getCategories().contains(category1)) {
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
        result = key * result + ((categories == null) ? 0 : hashCodeList(categories));
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
