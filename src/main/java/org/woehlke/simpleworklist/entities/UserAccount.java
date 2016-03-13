package org.woehlke.simpleworklist.entities;

import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;

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

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastLoginTimestamp;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userAccount", cascade = { CascadeType.ALL })
    private List<Area> areas = new ArrayList<Area>();

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

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!userEmail.equals(that.userEmail)) return false;
        if (!userPassword.equals(that.userPassword)) return false;
        if (!userFullname.equals(that.userFullname)) return false;
        if (!createdTimestamp.equals(that.createdTimestamp)) return false;
        if (lastLoginTimestamp != null ? !lastLoginTimestamp.equals(that.lastLoginTimestamp) : that.lastLoginTimestamp != null)
            return false;
        return areas.equals(that.areas);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + userEmail.hashCode();
        result = 31 * result + userPassword.hashCode();
        result = 31 * result + userFullname.hashCode();
        result = 31 * result + createdTimestamp.hashCode();
        result = 31 * result + (lastLoginTimestamp != null ? lastLoginTimestamp.hashCode() : 0);
        result = 31 * result + areas.hashCode();
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
