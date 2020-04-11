package org.woehlke.simpleworklist.application.language;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 15.03.2016.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeLanguageForm implements Serializable {

    private static final long serialVersionUID = 2201123162578113187L;

    @NotNull
    private Language defaultLanguage;

}
