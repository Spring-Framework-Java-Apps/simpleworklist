package org.woehlke.simpleworklist.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "uuid",
                "userAccountId"
        })
)
@Indexed
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = true)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "userAccountId")
    private UserAccount userAccount;

    @SafeHtml
    @NotBlank
    @Length(min=1,max=255)
    @Column(nullable = false)
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String title;

    @SafeHtml
    @NotBlank
    @Length(min=0,max=65535)
    @Column(nullable = false, length = 65535, columnDefinition="text")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String text;

    @Enumerated(EnumType.STRING)
    private TaskState status;

    @Enumerated(EnumType.STRING)
    private FocusType focusType;

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = true)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dueDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date lastChangeTimestamp;

    @Transient
    public String getTextShortened(){
        StringBuilder sb = new StringBuilder(this.getText());
        if(sb.length() > 50){
            sb.setLength(50);
            sb.trimToSize();
            sb.append("...");
        }
        return sb.toString();
    }

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
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

    public TaskState getStatus() {
        return status;
    }

    public void setStatus(TaskState status) {
        this.status = status;
    }

    public FocusType getFocusType() {
        return focusType;
    }

    public void setFocusType(FocusType focusType) {
        this.focusType = focusType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getLastChangeTimestamp() {
        return lastChangeTimestamp;
    }

    public void setLastChangeTimestamp(Date lastChangeTimestamp) {
        this.lastChangeTimestamp = lastChangeTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task that = (Task) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!uuid.equals(that.uuid)) return false;
        if (project != null ? !project.equals(that.project) : that.project != null) return false;
        if (!userAccount.equals(that.userAccount)) return false;
        if (!title.equals(that.title)) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (status != that.status) return false;
        if (focusType != that.focusType) return false;
        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null) return false;
        if (!createdTimestamp.equals(that.createdTimestamp)) return false;
        return lastChangeTimestamp != null ? lastChangeTimestamp.equals(that.lastChangeTimestamp) : that.lastChangeTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + uuid.hashCode();
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + userAccount.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + focusType.hashCode();
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + createdTimestamp.hashCode();
        result = 31 * result + (lastChangeTimestamp != null ? lastChangeTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", project=" + project +
                ", userAccount=" + userAccount +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", focusType=" + focusType +
                ", dueDate=" + dueDate +
                ", createdTimestamp=" + createdTimestamp +
                ", lastChangeTimestamp=" + lastChangeTimestamp +
                '}';
    }
}
