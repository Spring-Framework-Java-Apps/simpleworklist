package org.woehlke.simpleworklist.user.chat;


import lombok.*;
import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User2UserMessageFormBean implements Serializable {

    private static final long serialVersionUID = 1576610181966480168L;

    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=1,max=65535)
    private String messageText;

}
