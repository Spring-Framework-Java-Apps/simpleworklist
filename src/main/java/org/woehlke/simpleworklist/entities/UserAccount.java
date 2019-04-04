package org.woehlke.simpleworklist.entities;

import org.hibernate.search.annotations.*;
import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.entities.enumerations.Language;

import java.util.Date;

import javax.persistence.*;
import javax.persistence.Index;
import javax.validation.constraints.NotNull;

@Entity
@Table(
    name="user_account",
    uniqueConstraints = {
        @UniqueConstraint(name="ux_user_account", columnNames = {"user_email"})
    },
    indexes = {
        @Index(name="ix_user_account_user_fullname", columnList = "user_fullname"),
        @Index(name="ix_user_account_created_timestamp", columnList = "created_timestamp"),
        @Index(name="ix_user_account_last_login_timestamp", columnList = "last_login_timestamp")
    }
)
public class UserAccount {

    @Id
    @GeneratedValue(generator = "user_account_generator")
    @SequenceGenerator(
        name = "user_account_generator",
        sequenceName = "user_account_sequence",
        initialValue = 1000
    )
    private Long id;

    @Email
    @Column(name="user_email", nullable = false)
    @Field
    private String userEmail;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @Column(name="user_password", nullable = false)
    private String userPassword;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @Column(name="user_fullname", nullable = false)
    private String userFullname;

    @Column(name="default_language", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language defaultLanguage;

    @ManyToOne
    @JoinColumn(name = "default_context_id")
    private Context defaultContext;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name="created_timestamp", nullable = false)
    private Date createdTimestamp;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name="last_login_timestamp", nullable = false)
    private Date lastLoginTimestamp;

    @NotNull
    @Column(name="account_non_expired", nullable = false)
    private Boolean accountNonExpired=true;

    @NotNull
    @Column(name="account_non_locked", nullable = false)
    private Boolean accountNonLocked=true;

    @NotNull
    @Column(name="credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired=true;

    @NotNull
    @Column(name="enabled", nullable = false)
    private Boolean enabled=true;

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

    public Context getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(Context defaultContext) {
        this.defaultContext = defaultContext;
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
