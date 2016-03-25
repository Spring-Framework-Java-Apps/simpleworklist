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
                "contextId"
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
    @JoinColumn(name = "contextId")
    private Context context;

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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (uuid != null ? !uuid.equals(task.uuid) : task.uuid != null) return false;
        if (project != null ? !project.equals(task.project) : task.project != null) return false;
        if (context != null ? !context.equals(task.context) : task.context != null) return false;
        if (userAccount != null ? !userAccount.equals(task.userAccount) : task.userAccount != null) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        if (text != null ? !text.equals(task.text) : task.text != null) return false;
        if (focus != null ? !focus.equals(task.focus) : task.focus != null) return false;
        if (taskState != task.taskState) return false;
        if (lastTaskState != task.lastTaskState) return false;
        if (taskEnergy != task.taskEnergy) return false;
        if (taskTime != task.taskTime) return false;
        if (dueDate != null ? !dueDate.equals(task.dueDate) : task.dueDate != null) return false;
        if (createdTimestamp != null ? !createdTimestamp.equals(task.createdTimestamp) : task.createdTimestamp != null)
            return false;
        return lastChangeTimestamp != null ? lastChangeTimestamp.equals(task.lastChangeTimestamp) : task.lastChangeTimestamp == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (focus != null ? focus.hashCode() : 0);
        result = 31 * result + (taskState != null ? taskState.hashCode() : 0);
        result = 31 * result + (lastTaskState != null ? lastTaskState.hashCode() : 0);
        result = 31 * result + (taskEnergy != null ? taskEnergy.hashCode() : 0);
        result = 31 * result + (taskTime != null ? taskTime.hashCode() : 0);
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (createdTimestamp != null ? createdTimestamp.hashCode() : 0);
        result = 31 * result + (lastChangeTimestamp != null ? lastChangeTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", project=" + project +
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
