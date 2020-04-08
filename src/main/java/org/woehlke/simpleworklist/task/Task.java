package org.woehlke.simpleworklist.task;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.Index;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.common.AuditModel;
import org.woehlke.simpleworklist.common.ComparableById;

@Entity
@Table(
    name="task",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_task",
            columnNames = {"uuid", "context_id" }
        )/*,
        @UniqueConstraint(
            name="ux_task_order_id_project",
            columnNames = {"order_id_project", "project_id", "context_id", "user_account_id"}
        ),
        @UniqueConstraint(
            name="ux_task_order_id_task_state",
            columnNames = {"order_id_task_state", "task_state", "project_id", "context_id", "user_account_id"}
        )*/
    },
    indexes = {
        @Index(name = "ix_task_uuid", columnList = "uuid"),
        @Index(name = "ix_task_row_created_at", columnList = "row_created_at"),
        @Index(name = "ix_task_title", columnList = "title")
    }
)
@Getter
@Setter
public class Task extends AuditModel implements Serializable, ComparableById<Task> {

    private static final long serialVersionUID = 5247710652586269801L;

    @Id
    @GeneratedValue(generator = "task_generator")
    @SequenceGenerator(
            name = "task_generator",
            sequenceName = "task_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = true,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project project;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Context context;

    @Deprecated

    @SafeHtml(whitelistType= SafeHtml.WhiteListType.NONE)
    @NotBlank
    @Length(min=1,max=255)
    @Column(name = "title", nullable = false)
    private String title;

    //@SafeHtml(whitelistType= SafeHtml.WhiteListType.RELAXED)
    @NotBlank
    @Length(min=0,max=65535)
    @Column(name = "description", nullable = false, length = 65535, columnDefinition="text")
    private String text;

    @Column(name = "focus", nullable = false)
    private Boolean focus;

    /**
     * The current TaskState;
     */
    @Column(name = "task_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskState taskState;

    /**
     * The TaskState before the current TaskState;
     */
    @Column(name = "last_task_state", nullable = true)
    @Enumerated(EnumType.STRING)
    private TaskState lastTaskState;

    @Column(name = "task_energy", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskEnergy taskEnergy;

    @Column(name = "task_time", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskTime taskTime;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "due_date", nullable = true)
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date dueDate;

    @Column(name = "order_id_project",nullable = false)
    private long orderIdProject;

    @Column(name = "order_id_task_state",nullable = false)
    private long orderIdTaskState;

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

    /**
     * performs 'history back' for taskState
     */
    @Transient
    public void switchToLastFocusType() {
        TaskState old = this.taskState;
        this.taskState = this.lastTaskState;
        this.lastTaskState = old;
    }

    public void delete(){
        this.lastTaskState = this.taskState;
        this.taskState = TaskState.TRASH;
    }

    public void undelete(){
        if( this.taskState == TaskState.TRASH){
            this.taskState = this.lastTaskState;
            this.lastTaskState = TaskState.TRASH;
        }
    }

    public void complete(){
        this.lastTaskState = this.taskState;
        this.taskState = TaskState.COMPLETED;
    }

    public void incomplete(){
        if( this.taskState == TaskState.COMPLETED){
            this.taskState = this.lastTaskState;
            this.lastTaskState = TaskState.COMPLETED;
        }
    }

    public void setFocus(){
        this.focus = true;
    }

    public void unsetFocus(){
        this.focus = false;
    }

    private void pushTaskstate(TaskState newState){
        this.taskState = this.lastTaskState;
        this.lastTaskState = newState;
    }

    //TODO: delete Due Date
    public void moveToInbox(){
        pushTaskstate(TaskState.INBOX);
    }

    //TODO: Due Date = Date of Today
    public void moveToToday(){
        pushTaskstate(TaskState.TODAY);
    }

    //TODO: delete Due Date
    public void moveToNext(){
        pushTaskstate(TaskState.NEXT);
    }

    //TODO: delete Due Date
    public void moveToWaiting(){
        pushTaskstate(TaskState.WAITING);
    }

    //TODO: Due Date = Date of Tomorrow
    public void moveToScheduled(){
        pushTaskstate(TaskState.SCHEDULED);
    }

    //TODO: delete Due Date
    public void moveToSomeday(){
        pushTaskstate(TaskState.SOMEDAY);
    }

    public void moveToFocus(){
        this.focus = true;
    }

    public void moveToCompletedTasks(){
        pushTaskstate(TaskState.COMPLETED);
    }

    public void moveToTrash(){
        pushTaskstate(TaskState.TRASH);
    }

    public void emptyTrash(){
        if(this.taskState == TaskState.TRASH){
            pushTaskstate(TaskState.TRASH);
        }
    }

    @Transient
    public boolean hasSameContextAs(Task otherTask){
        return (this.getContext().equalsById( otherTask.getContext()));
    }

    @Transient
    public boolean hasSameTaskTypetAs(Task otherTask){
        return (this.getTaskState().ordinal() == otherTask.getTaskState().ordinal());
    }

    @Transient
    public boolean isBelowByTaskState(Task otherTask){
        return (this.getOrderIdTaskState() < otherTask.getOrderIdTaskState());
    }

    @Transient
    public void moveUpByTaskState(){
        this.orderIdTaskState++;
    }

    @Transient
    public void moveDownByTaskState(){
        this.orderIdTaskState--;
    }

    @Transient
    public boolean isBelowByProject(Task otherTask){
        return ( this.getOrderIdProject() < otherTask.getOrderIdProject() );
    }

    @Transient
    public boolean hasSameProjectAs(Task destinationTask) {
        if(this.isInRootProject() && destinationTask.isInRootProject()){
            return true;
        } else {
            return ( this.getProject().equalsById(destinationTask.getProject()));
        }
    }

    @Transient
    public boolean hasProject(Project project) {
        return this.getProject().equalsById(project);
    }

    @Transient
    public void moveUpByProject(){
        this.orderIdProject++;
    }

    @Transient
    public void moveDownByProject(){
        this.orderIdProject--;
    }

    @Transient
    public boolean isInRootProject(){
        return ((this.getProject() == null)) || (this.getProject().getId()==0L);
    }

    @Transient
    public boolean hasUser(UserAccount thisUser) {
        boolean viaContextOk = (this.getContext().hasThisUser(thisUser));
        boolean viaProjectOk = true;
        if(!this.isInRootProject()){
            viaProjectOk = (this.getProject().getContext().hasThisUser(thisUser));
        }
        return viaContextOk && viaProjectOk;
    }

    @Transient
    public void setRootProject() {
        this.setProject(null);
    }

    @Transient
    public boolean hasSameContextAs(Project project) {
        return (this.getContext().getId().longValue() == project.getContext().getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsById(Task otherObject) {
        return (this.getId().longValue() == otherObject.getId().longValue());
    }

    @Transient
    @Override
    public boolean equalsByUniqueConstraint(Task otherObject) {
        boolean okUuid = this.equalsByUuid(otherObject);
        boolean contextId = this.getContext().equalsByUniqueConstraint(otherObject.getContext());
        return okUuid && contextId;
    }

    @Transient
    @Override
    public boolean equalsByUuid(Task otherObject) {
        return super.equalsByMyUuid(otherObject);
    }

    public boolean hasContext(Context context) {
        return (this.getContext().getId().longValue() == context.getId().longValue());
    }

    @Transient
    public String getView(boolean project){
        if(project){
            if(this.project == null){
                return "redirect:/project/root";
            } else {
                return this.project.getUrl();
            }
        } else {
            return "redirect:/"+this.taskState.getUrl();
        }
    }

    @Transient
    public String getUrl(){
        return getView(false);
    }

    @Transient
    public String getProjectUrl() {

        return getView(true);
    }
}
