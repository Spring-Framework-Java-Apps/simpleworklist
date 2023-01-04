package org.woehlke.java.simpleworklist.domain.meso.breadcrumb;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BreadcrumbItem implements Serializable {

    private static final long serialVersionUID = 8276819198016077167L;

    @NotNull
    private String name;

    @NotNull
    private String url;

    public BreadcrumbItem(@NotNull String name, @NotNull String url) {
        this.name = name;
        this.url = url;
    }

}
