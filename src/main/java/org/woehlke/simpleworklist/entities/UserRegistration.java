package org.woehlke.simpleworklist.entities;

import org.hibernate.validator.constraints.Email;
import org.woehlke.simpleworklist.entities.enumerations.UserRegistrationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
    columnNames = {
        "email"
    })
)
public class UserRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String token;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp = new Date();

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRegistrationStatus doubleOptInStatus;

    @Column
    private int numberOfRetries = 0;

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

    public UserRegistrationStatus getDoubleOptInStatus() {
        return doubleOptInStatus;
    }

    public void setDoubleOptInStatus(UserRegistrationStatus doubleOptInStatus) {
        this.doubleOptInStatus = doubleOptInStatus;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public int getNumberOfRetries() {
        return numberOfRetries;
    }

    public void setNumberOfRetries(int numberOfRetries) {
        this.numberOfRetries = numberOfRetries;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegistration)) return false;
        UserRegistration that = (UserRegistration) o;
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
        return "UserRegistration{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", doubleOptInStatus=" + doubleOptInStatus +
                ", numberOfRetries=" + numberOfRetries +
                '}';
    }
}
