package org.woehlke.java.simpleworklist.domain.db.data.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectRepository;
import org.woehlke.java.simpleworklist.domain.meso.taskworkflow.TaskState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
      this.projectRepository = projectRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean projectHasNoTasks(Project project) {
        log.info("projectHasNoTasks");
        return taskRepository.findByProject(project).isEmpty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findbyTaskstate(
        @NotNull TaskState taskState,
        @NotNull Context context,
        @NotNull Pageable request
    ) {
      switch (taskState){
        case FOCUS:
          return taskRepository.findByFocusAndContext(true, context, request);
        case TRASH:
        case DELETED:
          return taskRepository.findByTaskStateTrashAndContext(context, request);
        default:
          return taskRepository.findByTaskStateAndContext(taskState, context, request);
      }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByProject(
        @NotNull Project thisProject,Pageable request
    ) {
        log.info("findByProject: ");
        log.info("---------------------------------");
        log.info("thisProject: "+thisProject);
        log.info("---------------------------------");
        return taskRepository.findByProject(thisProject,request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByRootProject( Context context,Pageable request) {
        log.info("findByRootProject: ");
        return taskRepository.findByProjectIsNullAndContext(context, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task findOne(@Min(1L) long taskId) {
        log.info("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getReferenceById(taskId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaTaskstate(Task task) {
        log.info("updatedViaTaskstate");
        if(task.getProject() != null){
          Long projectId = task.getProject().getId();
          Project project = projectRepository.getReferenceById(projectId);
          task.setProject(project);
        }
        if(task.getLastProject()!=null){
          Long projectId = task.getLastProject().getId();
          Project project = projectRepository.getReferenceById(projectId);
          task.setLastProject(project);
        }
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProject( @Valid Task task) {
        log.info("updatedViaProject");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProjectRoot( @Valid Task task) {
        log.info("updatedViaProject");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToInbox( @Valid Task task) {
        log.info("addToInbox");
        task.setUuid(UUID.randomUUID());
        task.setRootProject();
        task.unsetFocus();
        task.setTaskState(TaskState.INBOX);
        task.setLastProject(null);
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToProject( @Valid Task task) {
        log.info("addToProject");
        task.setUuid(UUID.randomUUID());
        task.unsetFocus();
        long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(),task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToRootProject( @Valid Task task) {
        log.info("addToRootProject");
        task.setUuid(UUID.randomUUID());
        task.setRootProject();
        task.unsetFocus();
        task.moveToInbox();
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToRootProject( @Valid Task task) {
        task.moveTaskToRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToAnotherProject( @Valid Task task,@Valid Project project) {
        boolean okContext = task.hasSameContextAs(project);
        if(okContext) {
            task.moveTaskToAnotherProject(project);
            long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(),
                task.getContext()
            );
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveAllCompletedToTrash( Context context) {
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            context
        );
        long newOrderIdTaskState = maxOrderIdTaskState;
        List<Task> taskListCompleted = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
            TaskState.COMPLETED,
            context
        );
        for (Task task : taskListCompleted) {
            newOrderIdTaskState++;
            task.setOrderIdTaskState(newOrderIdTaskState);
            task.moveToTrash();
        }
        taskRepository.saveAll(taskListCompleted);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void emptyTrash( Context context) {
        List<Task> taskList = taskRepository.findByTaskStateAndContext(
            TaskState.TRASH,
            context
        );
        List<Task> taskListChanged = new ArrayList<>(taskList.size());
        for(Task task: taskList){
            task.emptyTrash();
            taskListChanged.add(task);
        }
        taskRepository.saveAll(taskListChanged);
        List<Task> taskListDeleted = taskRepository.findByTaskStateAndContext(
          TaskState.DELETED,
          context
        );
        taskRepository.deleteAll(taskListDeleted);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdTaskState( TaskState taskState,Context context) {
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(
            taskState,
            context
        );
        return (task==null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProject( Project project,Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            project,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProjectRoot( Context context) {
        Task task = taskRepository.findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByTaskState(Task sourceTask, Task destinationTask ) {
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        final long lowerOrderIdTaskState = destinationTask.getOrderIdTaskState();
        final long higherOrderIdTaskState = sourceTask.getOrderIdTaskState();
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
            lowerOrderIdTaskState,
            higherOrderIdTaskState,
            taskState,
            context
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByTaskState();
            log.info(task.outTaskstate());
            tasksMoved.add(task);
        }
        destinationTask.moveUpByTaskState();
        log.info(destinationTask.outTaskstate());
        tasksMoved.add(destinationTask);
        sourceTask.setOrderIdTaskState( lowerOrderIdTaskState );
        log.info(sourceTask.outTaskstate());
        tasksMoved.add(sourceTask);
        taskRepository.saveAll(tasksMoved);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByTaskState(Task sourceTask, Task destinationTask ) {
        log.info("-------------------------------------------------------------------------------");
        log.info(" moveTasks DOWN By TaskState: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.info("-------------------------------------------------------------------------------");
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        long lowerOrderIdTaskState = sourceTask.getOrderIdTaskState();
        long higherOrderIdTaskState = destinationTask.getOrderIdTaskState();
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
            lowerOrderIdTaskState,
            higherOrderIdTaskState,
            taskState,
            context
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByTaskState();
            log.info(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdTaskState(higherOrderIdTaskState);
        destinationTask.moveDownByTaskState();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTasks DOWN By TaskState("+taskState.name()+"): "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.info("-------------------------------------------------------------------------------");
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByProjectRoot(Task sourceTask, Task destinationTask ) {
        log.info("-------------------------------------------------------------------------------");
        log.info(" moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.info("-------------------------------------------------------------------------------");
        Context context = sourceTask.getContext();
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            context
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByProject();
            log.info(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(lowerOrderIdProject);
        destinationTask.moveUpByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.info("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByProjectRoot(Task sourceTask, Task destinationTask) {
        log.info("-------------------------------------------------------------------------------");
        log.info(" START moveTasks UP By Project Root");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        Context context = sourceTask.getContext();
        final long lowerOrderIdProject = sourceTask.getOrderIdProject();
        final long higherOrderIdProject = destinationTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            context
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByProject();
            log.info(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(higherOrderIdProject);
        destinationTask.moveDownByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE moveTasks UP By Project Root");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByProject(Task sourceTask, Task destinationTask ) {
        Project project = sourceTask.getProject();
        log.info("-------------------------------------------------------------------------------");
        log.info(" START moveTasks UP By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByProject();
            log.info(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(lowerOrderIdProject);
        destinationTask.moveUpByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE moveTasks UP By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByProject(Task sourceTask, Task destinationTask) {
        Project project = sourceTask.getProject();
        log.info("-------------------------------------------------------------------------------");
        log.info(" START moveTasks DOWN By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        final long lowerOrderIdProject = sourceTask.getOrderIdProject();
        final long higherOrderIdProject = destinationTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project
        );
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByProject();
            log.info(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(higherOrderIdProject);
        destinationTask.moveDownByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE smoveTasks DOWN By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToInbox(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.INBOX,
            task.getContext()
        );
        task.moveToInbox();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to inbox: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToToday(Task task) {
        Date now = new Date();
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TODAY,
            task.getContext()
        );
        task.moveToToday();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to today: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToNext(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.NEXT,
            task.getContext()
        );
        task.moveToNext();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to next: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToWaiting(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.WAITING,
            task.getContext()
        );
        task.moveToWaiting();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to next: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToSomeday(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.SOMEDAY,
            task.getContext()
        );
        task.moveToSomeday();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to someday: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToFocus(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.FOCUS,
            task.getContext()
        );
        task.moveToFocus();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to focus: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToCompleted(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.COMPLETED,
            task.getContext()
        );
        task.moveToCompletedTasks();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to completed: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToTrash(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            task.getContext()
        );
        task.moveToTrash();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved to trash: " + task.outTaskstate());
        return task;
    }
}