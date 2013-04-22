package org.woehlke.simpleworklist.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;



@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"email"}))
public class RegistrationProcess {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false,unique=true)
	private String email;
	
	@Column(nullable=false,unique=true)
	private String token;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date createdTimestamp = new Date();
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private RegistrationProcessStatus doubleOptInStatus;

	private int numberOfRetries=0;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrationProcess other = (RegistrationProcess) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegistrationProcess [id=" + id + ", email=" + email
				+ ", token=" + token + ", createdTimestamp=" + createdTimestamp
				+ ", doubleOptInStatus=" + doubleOptInStatus
				+ ", numberOfRetries=" + numberOfRetries + "]";
	}
	
}
