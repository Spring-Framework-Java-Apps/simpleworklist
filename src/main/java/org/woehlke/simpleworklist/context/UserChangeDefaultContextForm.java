package org.woehlke.simpleworklist.context;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 16.03.2016.
 */
public class UserChangeDefaultContextForm implements Serializable {

    private static final long serialVersionUID = -8592295563275083292L;

    @NotNull
    private Long id;

    @NotNull
    private Context defaultContext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Context getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(Context defaultContext) {
        this.defaultContext = defaultContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChangeDefaultContextForm)) return false;

        UserChangeDefaultContextForm that = (UserChangeDefaultContextForm) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return defaultContext != null ? defaultContext.equals(that.defaultContext) : that.defaultContext == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (defaultContext != null ? defaultContext.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserChangeDefaultContextForm{" +
                "id=" + id +
                ", defaultContext=" + defaultContext +
                '}';
    }
}
