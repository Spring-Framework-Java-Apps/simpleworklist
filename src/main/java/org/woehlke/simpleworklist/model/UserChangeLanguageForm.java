package org.woehlke.simpleworklist.model;

import org.woehlke.simpleworklist.entities.enumerations.Language;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Fert on 15.03.2016.
 */
public class UserChangeLanguageForm implements Serializable {

    private static final long serialVersionUID = 2201123162578113187L;

    @NotNull
    private Language defaultLanguage;

    public UserChangeLanguageForm(){}

    public UserChangeLanguageForm(Language defaultLanguage){
        this.defaultLanguage=defaultLanguage;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChangeLanguageForm)) return false;

        UserChangeLanguageForm that = (UserChangeLanguageForm) o;

        return defaultLanguage == that.defaultLanguage;

    }

    @Override
    public int hashCode() {
        return defaultLanguage != null ? defaultLanguage.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserChangeLanguageForm{" +
                "defaultLanguage=" + defaultLanguage +
                '}';
    }
}
