package org.woehlke.java.simpleworklist.domain.db.user.accountselfservice;

import lombok.*;
import org.woehlke.java.simpleworklist.domain.db.data.Context;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 16.03.2016.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeDefaultContextForm implements Serializable {

    private static final long serialVersionUID = -8592295563275083292L;

    @NotNull
    private Long id;

    @NotNull
    private Context defaultContext;

}
