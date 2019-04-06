package org.woehlke.simpleworklist.model;

import org.woehlke.simpleworklist.entities.Context;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 16.03.2016.
 */
public class UserChangeDefaultContextFormBean implements Serializable {

    private static final long serialVersionUID = 312962745980506820L;

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
        if (!(o instanceof UserChangeDefaultContextFormBean)) return false;

        UserChangeDefaultContextFormBean that = (UserChangeDefaultContextFormBean) o;

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
        return "UserChangeDefaultContextFormBean{" +
                "id=" + id +
                ", defaultContext=" + defaultContext +
                '}';
    }
}
