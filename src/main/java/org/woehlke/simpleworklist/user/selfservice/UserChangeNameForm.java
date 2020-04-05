package org.woehlke.simpleworklist.user.selfservice;

import javax.validation.constraints.NotBlank;

import lombok.*;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by tw on 14.03.16.
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserChangeNameForm implements Serializable {

    private static final long serialVersionUID = 5120488382888268418L;

    //TODO: Messages i18n
    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotNull(message = "Fullname is compulsory")
    @NotBlank(message = "Fullname is compulsory")
    private String userFullname;

}
