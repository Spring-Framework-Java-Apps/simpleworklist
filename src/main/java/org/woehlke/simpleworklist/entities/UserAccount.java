package org.woehlke.simpleworklist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

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

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userFullname;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;

        UserAccount that = (UserAccount) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
        if (userFullname != null ? !userFullname.equals(that.userFullname) : that.userFullname != null) return false;
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
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (lastLoginTimestamp != null ? lastLoginTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", userFullname='" + userFullname + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", lastLoginTimestamp=" + lastLoginTimestamp +
                '}';
    }
}
