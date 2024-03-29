package org.woehlke.java.simpleworklist.domain.db.user.account;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
//import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userPassword"})
public class UserAccountForm implements Serializable {

    private static final long serialVersionUID = 9180383385243540190L;

    //TODO: Messages i18n
    @NotNull(message = "Email Address is compulsory")
    @NotBlank(message = "Email Address is compulsory")
    @Email(message = "Email Address is not a valid format")
    private String userEmail;

    //TODO: Messages i18n
    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Fullname is compulsory")
    @NotBlank(message = "Fullname is compulsory")
    private String userFullname;

    //TODO: Messages i18n
    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPassword;

    //TODO: Messages i18n
    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPasswordConfirmation;

    @Transient
    public boolean passwordsAreTheSame() {
        return this.userPassword.compareTo(userPasswordConfirmation) == 0;
    }

}
