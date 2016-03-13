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
import org.woehlke.simpleworklist.entities.enumerations.TaskEnergy;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.enumerations.TaskTime;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "uuid",
                "userAccountId",
                "areaId"
        })
)
@Indexed
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId(name="id")
    private Long id;

    @Column(nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @ManyToOne(optional = true)
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "areaId")
    private Area area;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccountId")
    @IndexedEmbedded(includeEmbeddedObjectId=true)
    private UserAccount userAccount;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(nullable = false)
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String title;

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.SIMPLE_TEXT)
    @NotBlank
    @Length(min=0,max=65535)
    @Column(nullable = false, length = 65535, columnDefinition="text")
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String text;

    @Column(nullable = false)
    private Boolean focus;

    /**
     * The current TaskState;
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskState taskState;

    /**
     * The TaskState before the current TaskState;
     */
    @Enumerated(EnumType.STRING)
    private TaskState lastTaskState;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskEnergy taskEnergy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskTime taskTime;

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = true)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dueDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdTimestamp;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false)
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

    public TaskState getTaskState() {
        return taskState;
    }

    public Boolean getFocus() {
        return focus;
    }

    public void setFocus(Boolean focus) {
        this.focus = focus;
    }

    /**
     * Sets also 'history back' for taskState
     */
    public void setTaskState(TaskState taskState) {
        this.lastTaskState = this.taskState;
        this.taskState = taskState;
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

    public TaskState getLastTaskState() {
        return lastTaskState;
    }

    public void setLastTaskState(TaskState lastTaskState) {
        this.lastTaskState = lastTaskState;
    }

    public TaskEnergy getTaskEnergy() {
        return taskEnergy;
    }

    public void setTaskEnergy(TaskEnergy taskEnergy) {
        this.taskEnergy = taskEnergy;
    }

    public TaskTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(TaskTime taskTime) {
        this.taskTime = taskTime;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (!uuid.equals(task.uuid)) return false;
        if (project != null ? !project.equals(task.project) : task.project != null) return false;
        if (!area.equals(task.area)) return false;
        if (!userAccount.equals(task.userAccount)) return false;
        if (!title.equals(task.title)) return false;
        if (!text.equals(task.text)) return false;
        if (!focus.equals(task.focus)) return false;
        if (taskState != task.taskState) return false;
        if (lastTaskState != task.lastTaskState) return false;
        if (taskEnergy != task.taskEnergy) return false;
        if (taskTime != task.taskTime) return false;
        if (dueDate != null ? !dueDate.equals(task.dueDate) : task.dueDate != null) return false;
        if (!createdTimestamp.equals(task.createdTimestamp)) return false;
        return lastChangeTimestamp != null ? lastChangeTimestamp.equals(task.lastChangeTimestamp) : task.lastChangeTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + uuid.hashCode();
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + area.hashCode();
        result = 31 * result + userAccount.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + focus.hashCode();
        result = 31 * result + taskState.hashCode();
        result = 31 * result + (lastTaskState != null ? lastTaskState.hashCode() : 0);
        result = 31 * result + taskEnergy.hashCode();
        result = 31 * result + taskTime.hashCode();
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
                ", area=" + area +
                ", userAccount=" + userAccount +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", focus=" + focus +
                ", taskState=" + taskState +
                ", lastTaskState=" + lastTaskState +
                ", taskEnergy=" + taskEnergy +
                ", taskTime=" + taskTime +
                ", dueDate=" + dueDate +
                ", createdTimestamp=" + createdTimestamp +
                ", lastChangeTimestamp=" + lastChangeTimestamp +
                '}';
    }

    /**
     * performs 'history back' for taskState
     */
    @Transient
    public void switchToLastFocusType() {
        TaskState old = this.taskState;
        this.taskState = this.lastTaskState;
        this.lastTaskState = old;
    }

}
