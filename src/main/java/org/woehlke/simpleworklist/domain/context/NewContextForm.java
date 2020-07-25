package org.woehlke.simpleworklist.domain.context;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
//import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;

/**
 * Created by tw on 15.03.16.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewContextForm implements Serializable {

    private static final long serialVersionUID = -937143305653156981L;

    //@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    private String nameDe;

    //@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min = 1, max = 255)
    private String nameEn;

}
