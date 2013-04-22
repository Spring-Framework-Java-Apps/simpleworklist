package org.woehlke.simpleworklist.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"monthId","dayOfMonth"}))
public class TimelineDay {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="monthId")
	private TimelineMonth month;
	
	@Column(nullable=false)
	private int dayOfMonth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TimelineMonth getMonth() {
		return month;
	}

	public void setMonth(TimelineMonth month) {
		this.month = month;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dayOfMonth;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
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
		TimelineDay other = (TimelineDay) obj;
		if (dayOfMonth != other.dayOfMonth)
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimelineDay [id=" + id + ", month=" + month + ", dayOfMonth="
				+ dayOfMonth + "]";
	}
	
}
