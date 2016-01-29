package org.woehlke.simpleworklist.entities;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "uuid",
                "categoryId"
        })
)
public class ActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    private TimelineDay created;

    @ManyToOne
    private TimelineDay changed;

    @SafeHtml
    @NotBlank
    @Length(min=1,max=255)
    @Column(nullable = false)
    private String title;

    @SafeHtml
    @NotBlank
    @Length(min=0,max=65535)
    @Column(nullable = false, length = 65535, columnDefinition="text")
    private String text;

    @Enumerated(EnumType.STRING)
    private ActionState status;

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

    public ActionState getStatus() {
        return status;
    }

    public void setStatus(ActionState status) {
        this.status = status;
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
        ActionItem other = (ActionItem) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ActionItem{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", category=" + category +
                ", created=" + created +
                ", changed=" + changed +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", status=" + status +
                '}';
    }
}
