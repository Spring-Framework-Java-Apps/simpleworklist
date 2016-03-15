package org.woehlke.simpleworklist.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;

/**
 * Created by tw on 14.03.16.
 */
public class UserChangeNameFormBean {

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Fullname is compulsory")
    @NotBlank(message = "Fullname is compulsory")
    private String userFullname;

    public UserChangeNameFormBean(){}

    public UserChangeNameFormBean(String userFullname){
        this.userFullname = userFullname;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChangeNameFormBean that = (UserChangeNameFormBean) o;

        return userFullname != null ? userFullname.equals(that.userFullname) : that.userFullname == null;

    }

    @Override
    public int hashCode() {
        return userFullname != null ? userFullname.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserChangeNameFormBean{" +
                "userFullname='" + userFullname + '\'' +
                '}';
    }
}
