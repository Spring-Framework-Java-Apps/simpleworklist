package org.woehlke.simpleworklist.entities;

import javax.validation.constraints.Email;
import org.woehlke.simpleworklist.entities.enumerations.UserPasswordRecoveryStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
    name="user_password_recovery",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_user_password_recovery",
            columnNames = { "email" }
        ),
        @UniqueConstraint(
            name="ux_user_password_recovery_token",
            columnNames = { "token" }
        )
    },
    indexes = {
        @Index(
            name = "ix_user_password_recovery_created_timestamp",
            columnList = "created_timestamp"
        )
    }
)
public class UserPasswordRecovery implements Serializable {

    @Id
    @GeneratedValue(generator = "user_password_recovery_generator")
    @SequenceGenerator(
            name = "user_password_recovery_generator",
            sequenceName = "user_password_recovery_sequence",
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
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp", nullable = false)
    private Date createdTimestamp = new Date();

    @NotNull
    @Column(name = "double_optin_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserPasswordRecoveryStatus doubleOptInStatus;

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

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public UserPasswordRecoveryStatus getDoubleOptInStatus() {
        return doubleOptInStatus;
    }

    public void setDoubleOptInStatus(UserPasswordRecoveryStatus doubleOptInStatus) {
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
        if (!(o instanceof UserPasswordRecovery)) return false;
        UserPasswordRecovery that = (UserPasswordRecovery) o;
        return getNumberOfRetries() == that.getNumberOfRetries() &&
                Objects.equals(getId(), that.getId()) &&
                getEmail().equals(that.getEmail()) &&
                getToken().equals(that.getToken()) &&
                getCreatedTimestamp().equals(that.getCreatedTimestamp()) &&
                getDoubleOptInStatus() == that.getDoubleOptInStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getToken(), getCreatedTimestamp(), getDoubleOptInStatus(), getNumberOfRetries());
    }

    @Override
    public String toString() {
        return "UserPasswordRecovery{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", doubleOptInStatus=" + doubleOptInStatus +
                ", numberOfRetries=" + numberOfRetries +
                '}';
    }
}
