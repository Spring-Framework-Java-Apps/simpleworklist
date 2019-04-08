package org.woehlke.simpleworklist.entities.entities;

import org.hibernate.search.annotations.*;
import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.simpleworklist.entities.entities.impl.AuditModel;
import org.woehlke.simpleworklist.entities.enumerations.Language;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
        @Index(name="ix_user_account_uuid", columnList = "uuid"),
        @Index(name="ix_user_account_row_created_at", columnList = "row_created_at"),
        @Index(name="ix_user_account_user_fullname", columnList = "user_fullname"),
        @Index(name="ix_user_account_last_login_timestamp", columnList = "last_login_timestamp")
    }
)
public class UserAccount extends AuditModel implements Serializable {

    private static final long serialVersionUID = 7860692526488291439L;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "default_context_id", nullable=false)
    private Context defaultContext;

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
        if (!super.equals(o)) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(getId(), that.getId()) &&
                getUserEmail().equals(that.getUserEmail()) &&
                getUserPassword().equals(that.getUserPassword()) &&
                getUserFullname().equals(that.getUserFullname()) &&
                getDefaultLanguage() == that.getDefaultLanguage() &&
                getDefaultContext().equals(that.getDefaultContext()) &&
                Objects.equals(getLastLoginTimestamp(), that.getLastLoginTimestamp()) &&
                getAccountNonExpired().equals(that.getAccountNonExpired()) &&
                getAccountNonLocked().equals(that.getAccountNonLocked()) &&
                getCredentialsNonExpired().equals(that.getCredentialsNonExpired()) &&
                getEnabled().equals(that.getEnabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getUserEmail(), getUserPassword(), getUserFullname(), getDefaultLanguage(), getDefaultContext(), getLastLoginTimestamp(), getAccountNonExpired(), getAccountNonLocked(), getCredentialsNonExpired(), getEnabled());
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userFullname='" + userFullname + '\'' +
                ", defaultLanguage=" + defaultLanguage +
                ", lastLoginTimestamp=" + lastLoginTimestamp +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }
}
