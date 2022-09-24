package org.woehlke.java.simpleworklist.domain.db.user;

import javax.validation.constraints.Email;

import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
    name="user_account_password_recovery",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_account_password_recovery",
            columnNames = { "email" }
        ),
        @UniqueConstraint(
            name="ux_user_account_password_recovery_token",
            columnNames = { "token" }
        )
    },
    indexes = {
        @Index(name = "ix_user_account_password_recovery_uuid", columnList = "uuid"),
        @Index(name = "ix_user_account_password_recovery_row_created_at", columnList = "row_created_at")
    }
)
public class UserAccountPasswordRecovery extends AuditModel implements Serializable,Comparable<UserAccountPasswordRecovery> {

    private static final long serialVersionUID = 6860716425733119940L;

    @Id
    @GeneratedValue(generator = "user_account_password_recovery_generator")
    @SequenceGenerator(
            name = "user_account_password_recovery_generator",
            sequenceName = "user_account_password_recovery_sequence",
            initialValue = 1000
    )
    private Long id;

    @NotNull
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @NotNull
    @Column(name = "double_optin_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserAccountPasswordRecoveryStatus doubleOptInStatus;

    @NotNull
    @Column(name = "number_of_retries", nullable = false)
    private Integer numberOfRetries = 0;

    @Transient
    public void increaseNumberOfRetries() {
        this.numberOfRetries++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserAccountPasswordRecoveryStatus getDoubleOptInStatus() {
        return doubleOptInStatus;
    }

    public void setDoubleOptInStatus(UserAccountPasswordRecoveryStatus doubleOptInStatus) {
        this.doubleOptInStatus = doubleOptInStatus;
    }

    public Integer getNumberOfRetries() {
        return numberOfRetries;
    }

    public void setNumberOfRetries(Integer numberOfRetries) {
        this.numberOfRetries = numberOfRetries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountPasswordRecovery)) return false;
        if (!super.equals(o)) return false;
        UserAccountPasswordRecovery that = (UserAccountPasswordRecovery) o;
        return Objects.equals(getId(), that.getId()) &&
                getEmail().equals(that.getEmail()) &&
                getToken().equals(that.getToken()) &&
                getDoubleOptInStatus() == that.getDoubleOptInStatus() &&
                getNumberOfRetries().equals(that.getNumberOfRetries());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getEmail(), getToken(), getDoubleOptInStatus(), getNumberOfRetries());
    }

    @Override
    public String toString() {
        return "UserPasswordRecovery{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", doubleOptInStatus=" + doubleOptInStatus +
                ", numberOfRetries=" + numberOfRetries +
                ", uuid='" + uuid + '\'' +
                ", rowCreatedAt=" + rowCreatedAt +
                ", rowUpdatedAt=" + rowUpdatedAt +
                '}';
    }

  @Override
  public int compareTo(UserAccountPasswordRecovery o) {
    return this.rowCreatedAt.compareTo(o.rowCreatedAt);
  }
}
