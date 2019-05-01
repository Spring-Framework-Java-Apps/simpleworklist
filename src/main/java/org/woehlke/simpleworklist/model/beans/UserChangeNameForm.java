package org.woehlke.simpleworklist.model.beans;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by tw on 14.03.16.
 */
public class UserChangeNameForm implements Serializable {

    private static final long serialVersionUID = 5120488382888268418L;

    //TODO: Messages i18n
    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Fullname is compulsory")
    @NotBlank(message = "Fullname is compulsory")
    private String userFullname;

    public UserChangeNameForm(){}

    public UserChangeNameForm(String userFullname){
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

        UserChangeNameForm that = (UserChangeNameForm) o;

        return userFullname != null ? userFullname.equals(that.userFullname) : that.userFullname == null;

    }

    @Override
    public int hashCode() {
        return userFullname != null ? userFullname.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserChangeNameForm{" +
                "userFullname='" + userFullname + '\'' +
                '}';
    }
}
