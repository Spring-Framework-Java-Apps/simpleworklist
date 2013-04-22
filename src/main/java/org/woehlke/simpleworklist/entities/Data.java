package org.woehlke.simpleworklist.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"uuid","categoryId"}))
public class Data {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String uuid = UUID.randomUUID().toString();
	
	@ManyToOne
	@JoinColumn(name="categoryId")
	private Category category;
	
	@ManyToOne
	private TimelineDay created;
	
	@ManyToOne
	private TimelineDay changed;
	
	@SafeHtml
	@NotBlank
	@Column(nullable=false)
	private String title;
	
	@SafeHtml
	@NotBlank
	@Column(nullable=false)
	private String text;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public TimelineDay getCreated() {
		return created;
	}

	public void setCreated(TimelineDay created) {
		this.created = created;
	}

	public TimelineDay getChanged() {
		return changed;
	}

	public void setChanged(TimelineDay changed) {
		this.changed = changed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		Data other = (Data) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataLeaf [id=" + id + ", uuid=" + uuid + ", category="
				+ category + ", created=" + created + ", changed=" + changed
				+ ", title=" + title + ", text=" + text + ", hashCode()="
				+ hashCode() + "]";
	}
	
}
