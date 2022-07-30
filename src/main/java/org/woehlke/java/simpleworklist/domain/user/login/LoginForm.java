package org.woehlke.java.simpleworklist.domain.user.login;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.*;
//import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"userPassword"})
public class LoginForm implements Serializable {

    private static final long serialVersionUID = 5936886560348238355L;

    //TODO: Messages i18n
    @NotNull(message = "Email Address is compulsory")
    @NotBlank(message = "Email Address is compulsory")
    @Email(message = "Email Address is not a valid format")
    private String userEmail;

    //TODO: Messages i18n
    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Password is compulsory")
    @NotBlank(message = "Password is compulsory")
    private String userPassword;

}
