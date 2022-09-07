package org.woehlke.java.simpleworklist.domain.db.user.chat;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageForm implements Serializable {

    private static final long serialVersionUID = 1576610181966480168L;

    @NotBlank
    @Length(min=1,max=65535)
    private String messageText;

}