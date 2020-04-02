package org.woehlke.simpleworklist.user.account;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;

public class UserAccountForm implements Serializable {

    private static final long serialVersionUID = 9180383385243540190L;

    //TODO: Messages i18n
    @NotNull(message = "Email Address is compulsory")
    @NotBlank(message = "Email Address is compulsory")
    @Email(message = "Email Address is not a valid format")
    private String userEmail;

    //TODO: Messages i18n
    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPassword;

    //TODO: Messages i18n
    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPasswordConfirmation;

    //TODO: Messages i18n
    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Fullname is compulsory")
    @NotBlank(message = "Fullname is compulsory")
    private String userFullname;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    @Transient
    public boolean passwordsAreTheSame() {
        return this.userPassword.compareTo(userPasswordConfirmation) == 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((userEmail == null) ? 0 : userEmail.hashCode());
        result = prime * result
                + ((userFullname == null) ? 0 : userFullname.hashCode());
        result = prime * result
                + ((userPassword == null) ? 0 : userPassword.hashCode());
        result = prime
                * result
                + ((userPasswordConfirmation == null) ? 0
                : userPasswordConfirmation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserAccountForm other = (UserAccountForm) obj;
        if (userEmail == null) {
            if (other.userEmail != null)
                return false;
        } else if (!userEmail.equals(other.userEmail))
            return false;
        if (userFullname == null) {
            if (other.userFullname != null)
                return false;
        } else if (!userFullname.equals(other.userFullname))
            return false;
        if (userPassword == null) {
            if (other.userPassword != null)
                return false;
        } else if (!userPassword.equals(other.userPassword))
            return false;
        if (userPasswordConfirmation == null) {
            if (other.userPasswordConfirmation != null)
                return false;
        } else if (!userPasswordConfirmation
                .equals(other.userPasswordConfirmation))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserAccountForm [userEmail=" + userEmail
                + ", userPassword=" + userPassword
                + ", userPasswordConfirmation=" + userPasswordConfirmation
                + ", userFullname=" + userFullname + "]";
    }

}
