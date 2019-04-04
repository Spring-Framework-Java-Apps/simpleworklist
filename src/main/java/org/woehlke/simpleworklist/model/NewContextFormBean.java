package org.woehlke.simpleworklist.model;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Column;

/**
 * Created by tw on 15.03.16.
 */
public class NewContextFormBean {

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(nullable = false)
    private String nameDe;

    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(nullable = false)
    private String nameEn;

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewContextFormBean)) return false;

        NewContextFormBean that = (NewContextFormBean) o;

        if (nameDe != null ? !nameDe.equals(that.nameDe) : that.nameDe != null) return false;
        return nameEn != null ? nameEn.equals(that.nameEn) : that.nameEn == null;

    }

    @Override
    public int hashCode() {
        int result = nameDe != null ? nameDe.hashCode() : 0;
        result = 31 * result + (nameEn != null ? nameEn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewContextFormBean{" +
                "nameDe='" + nameDe + '\'' +
                ", nameEn='" + nameEn + '\'' +
                '}';
    }
}
