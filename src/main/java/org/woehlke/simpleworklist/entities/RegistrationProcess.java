package org.woehlke.simpleworklist.entities;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "email","registrationProcessType"
        })
)
public class RegistrationProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String token;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp = new Date();

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private RegistrationProcessStatus doubleOptInStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationProcessType registrationProcessType;

    @Column
    private int numberOfRetries = 0;

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

    public RegistrationProcessStatus getDoubleOptInStatus() {
        return doubleOptInStatus;
    }

    public void setDoubleOptInStatus(RegistrationProcessStatus doubleOptInStatus) {
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

    public RegistrationProcessType getRegistrationProcessType() {
        return registrationProcessType;
    }

    public void setRegistrationProcessType(RegistrationProcessType registrationProcessType) {
        this.registrationProcessType = registrationProcessType;
    }

    @Transient
    public void increaseNumberOfRetries() {
        this.numberOfRetries++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationProcess)) return false;

        RegistrationProcess that = (RegistrationProcess) o;

        if (!email.equals(that.email)) return false;
        if (registrationProcessType != that.registrationProcessType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + registrationProcessType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RegistrationProcess{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", doubleOptInStatus=" + doubleOptInStatus +
                ", registrationProcessType=" + registrationProcessType +
                ", numberOfRetries=" + numberOfRetries +
                '}';
    }
}
