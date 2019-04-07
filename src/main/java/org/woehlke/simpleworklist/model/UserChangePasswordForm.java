package org.woehlke.simpleworklist.model;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by tw on 15.03.16.
 */
public class UserChangePasswordForm implements Serializable {

    private static final long serialVersionUID = 9149342594823222054L;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String oldUserPassword;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPassword;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPasswordConfirmation;

    @Transient
    public boolean passwordsAreTheSame() {
        return this.userPassword.compareTo(userPasswordConfirmation) == 0;
    }

    public String getOldUserPassword() {
        return oldUserPassword;
    }

    public void setOldUserPassword(String oldUserPassword) {
        this.oldUserPassword = oldUserPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPasswordConfirmation() {
        return userPasswordConfirmation;
    }

    public void setUserPasswordConfirmation(String userPasswordConfirmation) {
        this.userPasswordConfirmation = userPasswordConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChangePasswordForm that = (UserChangePasswordForm) o;

        if (oldUserPassword != null ? !oldUserPassword.equals(that.oldUserPassword) : that.oldUserPassword != null)
            return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
        return userPasswordConfirmation != null ? userPasswordConfirmation.equals(that.userPasswordConfirmation) : that.userPasswordConfirmation == null;

    }

    @Override
    public int hashCode() {
        int result = oldUserPassword != null ? oldUserPassword.hashCode() : 0;
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userPasswordConfirmation != null ? userPasswordConfirmation.hashCode() : 0);
        return result;
    }

}
