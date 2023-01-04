package org.woehlke.java.simpleworklist.domain.db.user.signup;

import lombok.*;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserAccountRegistrationForm implements Serializable {

    private static final long serialVersionUID = 6864871862706880939L;

    //TODO: Messages i18n
    @NotNull(message = "Email Address is compulsory")
    @NotBlank(message = "Email Address is compulsory")
    @Email(message = "Email Address is not a valid format")
    private String email;

}
