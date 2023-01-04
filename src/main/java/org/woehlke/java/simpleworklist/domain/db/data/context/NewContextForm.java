package org.woehlke.java.simpleworklist.domain.db.data.context;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

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

    @NotBlank
    @Length(min = 1, max = 255)
    private String nameDe;

    @NotBlank
    @Length(min = 1, max = 255)
    private String nameEn;

}
