package org.woehlke.simpleworklist.model;

import org.woehlke.simpleworklist.entities.Area;

import javax.validation.constraints.NotNull;

/**
 * Created by Fert on 16.03.2016.
 */
public class UserChangeDefaultAreaFormBean {

    @NotNull
    private Long id;

    @NotNull
    private Area defaultArea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(Area defaultArea) {
        this.defaultArea = defaultArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChangeDefaultAreaFormBean)) return false;

        UserChangeDefaultAreaFormBean that = (UserChangeDefaultAreaFormBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return defaultArea != null ? defaultArea.equals(that.defaultArea) : that.defaultArea == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (defaultArea != null ? defaultArea.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserChangeDefaultAreaFormBean{" +
                "id=" + id +
                ", defaultArea=" + defaultArea +
                '}';
    }
}
