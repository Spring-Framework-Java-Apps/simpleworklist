package org.woehlke.simpleworklist.user.domain.account;

import javax.validation.constraints.NotBlank;

import lombok.*;
//import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by tw on 15.03.16.
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordForm implements Serializable {

    private static final long serialVersionUID = 9149342594823222054L;

    //TODO: Messages i18n
    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String oldUserPassword;

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
