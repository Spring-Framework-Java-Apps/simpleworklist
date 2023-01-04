package org.woehlke.java.simpleworklist.domain.db.data;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;


import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.application.framework.AuditModel;
import org.woehlke.java.simpleworklist.application.framework.ComparableById;


import static jakarta.persistence.TemporalType.DATE;
import static org.hibernate.annotations.LazyToOneOption.PROXY;


@Entity
@Table(
    name="data_task",
    uniqueConstraints = {
        @UniqueConstraint(
            name="ux_task",
            columnNames = {"uuid", "data_context_id" }
        )
    },
    indexes = {
        //TODO: test all three UniqueConstraints
        @Index(name = "ix_task_uuid", columnList = "uuid"),
        @Index(name = "ix_task_row_created_at", columnList = "row_created_at"),
        @Index(name = "ix_task_title", columnList = "title"),
        @Index(name = "ix_task_order_id_task_state", columnList = "order_id_task_state"),
        @Index(name = "ix_task_order_id_project", columnList = "order_id_project,data_project_id")
    }
)
@NamedQueries({
    @NamedQuery(
        name = "queryGetTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask",
        query = "select t from Task t"
            + " where t.orderIdTaskState > :lowerOrderIdTaskState and t.orderIdTaskState < :higherOrderIdTaskState"
            + " and t.taskState = :taskState and t.context = :context order by t.orderIdTaskState ",
        lockMode = LockModeType.READ
    ),
    @NamedQuery(
        name = "queryGetTasksByOrderIdProjectBetweenLowerTaskAndHigherTask",
        query = "select t from Task t"
            + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
            + " and t.project = :project order by t.orderIdProject DESC ",
        lockMode = LockModeType.READ
    ),
    @NamedQuery(
        name = "queryGetTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask",
        query = "select t from Task t"
            + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
            + " and t.project is null and t.context = :context  order by t.orderIdProject DESC ",
        lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateInbox",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.INBOX " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateToday",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.TODAY " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateNext",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.NEXT " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateWaiting",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.WAITING " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateScheduled",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.SCHEDULED " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateSomeday",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.SOMEDAY " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateCompleted",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.COMPLETED " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateDeleted",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.DELETED " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateTrash",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.TRASH " +
        "or t.taskState =  org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.DELETED " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    ),
    @NamedQuery(
      name = "findByTaskStateProjects",
      query = "select t from Task t " +
        "where t.taskState = org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS " +
        "and t.context = :context order by t.orderIdTaskState DESC ",
      lockMode = LockModeType.READ
    )
})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude="text")
public class Task extends AuditModel implements Serializable, ComparableById<Task>, Comparable<Task> {

    private static final long serialVersionUID = 5247710652586269801L;

    @Id
    @GeneratedValue(generator = "data_task_generator")
    @SequenceGenerator(
            name = "data_task_generator",
            sequenceName = "data_task_sequence",
            initialValue = 1000
    )
    private Long id;

    @Version
    private Long version;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = true,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "data_project_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project project;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = true,
        cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        }
    )
    @JoinColumn(name = "data_project_last_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Project lastProject;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "data_context_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @LazyToOne(PROXY)
    private Context context;


    @NotBlank
    @Length(min=1,max=255)
    @Column(name = "title", nullable = false)
    private String title;

    @Length(min=0,max=65535)
    @Column(name = "description", nullable = false, length = 65535, columnDefinition="text")
    private String text;

    @NotNull
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

    @Temporal(DATE)
    @Column(name = "due_date", nullable = true)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;

    @Column(name = "order_id_project", nullable = false)
    private long orderIdProject;

    @Column(name = "order_id_task_state", nullable = false)
    private long orderIdTaskState;

    @Transient
    public String getTeaser(){
        StringBuilder sb = new StringBuilder(this.getText());
        if(sb.length() > 50){
            sb.setLength(50);
            sb.trimToSize();
            sb.append("...");
        }
        return sb.toString();
    }

    public void delete(){
        pushTaskstate(TaskState.TRASH);
    }

    public void undelete(){
        if( this.taskState == TaskState.TRASH){
            popTaskstate(TaskState.TRASH);
        }
    }

    public void complete(){
        pushTaskstate(TaskState.COMPLETED);
    }

    public void incomplete(){
        if( this.taskState == TaskState.COMPLETED){
            popTaskstate(TaskState.COMPLETED);
        }
    }

    public void setFocus(){
        this.focus = true;
    }

    public void unsetFocus(){
        this.focus = false;
    }

    private void popTaskstate(TaskState oldState){
        this.taskState = this.lastTaskState;
        this.lastTaskState = oldState;
    }

    private void pushTaskstate(TaskState newState){
        this.lastTaskState = this.taskState;
        this.taskState = newState;
    }

    private void pushProject(Project newProject){
        this.lastProject = this.project;
        this.project = newProject;
    }

    public void moveToInbox(){
        pushTaskstate(TaskState.INBOX);
    }

    public void moveToToday(){ pushTaskstate(TaskState.TODAY); }

    public void moveToNext(){
        pushTaskstate(TaskState.NEXT);
    }

    public void moveToWaiting(){
        pushTaskstate(TaskState.WAITING);
    }

    public void moveToScheduled(){
        pushTaskstate(TaskState.SCHEDULED);
    }

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
            pushTaskstate(TaskState.DELETED);
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
        this.pushProject(null);
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
    public String getUrl(){
        return this.taskState.getUrlPathRedirect();
    }

    @Transient
    public String getProjectUrl() {
        if(this.project == null){
            return "redirect:/project/root";
        } else {
            return this.project.getUrl();
        }
    }

    public String outProject(){
        return "Task "+title+" (id:"+id+",orderIdProject:"+orderIdProject+")";
    }

    public String outTaskstate(){
        return "Task "+title+" (id:"+id+",orderIdTaskState:"+orderIdTaskState+")";
    }

    public void merge(Task task) {
        this.setTitle(task.title);
        this.setText(task.text);
        this.setFocus(task.focus);
        this.setTaskState(task.taskState);
        this.setDueDate(task.dueDate);
        this.setTaskEnergy(task.taskEnergy);
        this.setTaskTime(task.taskTime);
    }

    public void moveTaskToRootProject() {
        pushProject(null);
    }

    public void moveTaskToAnotherProject(Project project) {
        pushProject(project);
    }

  @Override
  public int compareTo(Task o) {
    return this.rowCreatedAt.compareTo(o.rowCreatedAt);
  }
}
