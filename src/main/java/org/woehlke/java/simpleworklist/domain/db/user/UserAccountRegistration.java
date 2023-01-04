package org.woehlke.java.simpleworklist.domain.db.user;

import jakarta.validation.constraints.Email;

import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(
    name="user_account_registration",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_account_registration",
            columnNames = { "email" }
        ),
        @UniqueConstraint(
            name="ux_user_account_registration_token",
            columnNames = { "token" }
        )
    },
    indexes = {
        @Index(name = "ix_user_account_registration_uuid", columnList = "uuid"),
        @Index(name = "ix_user_account_registration_row_created_at", columnList = "row_created_at")
    }
)
public class UserAccountRegistration extends AuditModel implements Serializable,Comparable<UserAccountRegistration> {

    @Serial
    private static final long serialVersionUID = -1955967514018161878L;

    @Id
    @GeneratedValue(generator = "user_account_registration_generator")
    @SequenceGenerator(
        name = "user_account_registration_generator",
        sequenceName = "user_account_registration_sequence",
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
    private UserAccountRegistrationStatus doubleOptInStatus;

    @NotNull
    @Column(name = "number_of_retries")
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

    public UserAccountRegistrationStatus getDoubleOptInStatus() {
        return doubleOptInStatus;
    }

    public void setDoubleOptInStatus(UserAccountRegistrationStatus doubleOptInStatus) {
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
        if (!(o instanceof UserAccountRegistration)) return false;
        if (!super.equals(o)) return false;
        UserAccountRegistration that = (UserAccountRegistration) o;
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
        return "UserRegistration{" +
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
  public int compareTo(UserAccountRegistration o) {
    return this.rowCreatedAt.compareTo(o.rowCreatedAt);
  }
}
