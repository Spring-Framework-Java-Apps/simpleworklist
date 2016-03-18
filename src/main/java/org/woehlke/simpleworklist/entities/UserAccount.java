package org.woehlke.simpleworklist.entities;

import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.entities.enumerations.Language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "userEmail"
        })
)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    @Field
    private String userEmail;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @Column(nullable = false)
    private String userPassword;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @Column(nullable = false)
    private String userFullname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Language defaultLanguage;

    @ManyToOne
    private Area defaultArea;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastLoginTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getLastLoginTimestamp() {
        return lastLoginTimestamp;
    }

    public void setLastLoginTimestamp(Date lastLoginTimestamp) {
        this.lastLoginTimestamp = lastLoginTimestamp;
    }

    public Area getDefaultArea() {
        return defaultArea;
    }

    public void setDefaultArea(Area defaultArea) {
        this.defaultArea = defaultArea;
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
        if (!(o instanceof UserAccount)) return false;

        UserAccount that = (UserAccount) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
        if (userFullname != null ? !userFullname.equals(that.userFullname) : that.userFullname != null) return false;
        if (defaultLanguage != that.defaultLanguage) return false;
        if (createdTimestamp != null ? !createdTimestamp.equals(that.createdTimestamp) : that.createdTimestamp != null)
            return false;
        return lastLoginTimestamp != null ? lastLoginTimestamp.equals(that.lastLoginTimestamp) : that.lastLoginTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userFullname != null ? userFullname.hashCode() : 0);
        result = 31 * result + (defaultLanguage != null ? defaultLanguage.hashCode() : 0);
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (lastLoginTimestamp != null ? lastLoginTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userFullname='" + userFullname + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", lastLoginTimestamp=" + lastLoginTimestamp +
                '}';
    }
}
