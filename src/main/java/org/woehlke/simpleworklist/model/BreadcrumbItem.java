package org.woehlke.simpleworklist.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class BreadcrumbItem implements Serializable {

    private static final long serialVersionUID = 8276819198016077167L;

    @NotNull
    private String name;

    @NotNull
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BreadcrumbItem)) return false;
        BreadcrumbItem that = (BreadcrumbItem) o;
        return getName().equals(that.getName()) &&
                getUrl().equals(that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUrl());
    }

    @Override
    public String toString() {
        return "BreadcrumbItem{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public BreadcrumbItem(@NotNull String name, @NotNull String url) {
        this.name = name;
        this.url = url;
    }

    public BreadcrumbItem() {
    }
}
