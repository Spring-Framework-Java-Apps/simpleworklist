package org.woehlke.simpleworklist.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskRepository;
import org.woehlke.simpleworklist.task.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskMoveServiceImpl implements TaskMoveService, TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskMoveServiceImpl( TaskRepository taskRepository ) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean projectHasNoTasks(Project project) {
        log.info("projectHasNoTasks");
        return taskRepository.findByProject(project).isEmpty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findbyTaskstate(TaskState taskState, Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(taskState, context, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> getEmptyPage(Pageable request){
        return new PageImpl<Task>(new ArrayList<Task>(),request,0L);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByProject(Project thisProject, Pageable request) {
        log.info("findByProject: ");
        log.info("---------------------------------");
        log.info("thisProject: "+thisProject);
        log.info("---------------------------------");
        if(thisProject == null){
            return this.getEmptyPage(request);
        } else {
            return taskRepository.findByProject(thisProject,request);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByRootProject(Context context, Pageable request) {
        log.info("findByRootProject: ");
        if(context == null){
            return this.getEmptyPage(request);
        } else {
            return taskRepository.findByProjectIsNullAndContext(context, request);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task findOne(long taskId) {
        log.info("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getOne(taskId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaTaskstate(Task task) {
        log.info("updatedViaTaskstate");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProject(Task task) {
        log.info("updatedViaProject");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToProject(Task task) {
        log.info("addToProject");
        task.setUuid(UUID.randomUUID().toString());
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToRootProject(Task task) {
        log.info("addToRootProject");
        task.setUuid(UUID.randomUUID().toString());
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToRootProject(Task task) {
        task.setRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(), task.getContext()
        );
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToAnotherProject(Task task, Project project) {
        boolean okContext = task.hasSameContextAs(project);
        if(okContext) {
            task.setProject(project);
            long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(),
                task.getContext()
            );
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToInbox(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.INBOX,
            task.getContext()
        );
        task.setTaskState(TaskState.INBOX);
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to inbox: " + task.toString());
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToToday(Task task) {
        Date now = new Date();
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TODAY,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.TODAY);
        task.setDueDate(now);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to today: " + task.toString());
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToNext(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.NEXT,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.NEXT);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to next: " + task.toString());
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToWaiting(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.WAITING,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.WAITING);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to next: " + task.toString());
        return task;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToSomeday(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.SOMEDAY,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.SOMEDAY);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to someday: " + task.toString());
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToFocus(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.FOCUS,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setFocus(true);
        task = taskRepository.saveAndFlush(task);
        log.info("moved " + task.getId() + " to focus: " + task.toString());
        return task;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToCompleted(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.COMPLETED,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.COMPLETED);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved " + task.getId() + " to completed: " + task.toString());
        return task;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToTrash(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.TRASH);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved " + task.getId() + " to trash: " + task.toString());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveAllCompletedToTrash(Context context) {
        long maxOrderIdTaskState = this.getMaxOrderIdTaskStateFor(
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
    public void emptyTrash(Context context) {
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
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdTaskState(TaskState taskState, Context context) {
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(
            taskState,
            context
        );
        return (task==null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProject(Project project, Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            project,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdTaskState( Task sourceTask, Task destinationTask ) {
        boolean notEqualsId = ! sourceTask.equalsById(destinationTask);
        boolean notEquals = ! sourceTask.equalsByUniqueConstraint(destinationTask);
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameTaskType = sourceTask.hasSameTaskTypetAs(destinationTask);
        boolean srcIsBelowDestinationTask  = sourceTask.isBelowByTaskState(destinationTask);
        if (
                notEqualsId && notEquals &&
                sameContext && sameTaskType
        ) {
            if (srcIsBelowDestinationTask) {
                this.moveTasksDownByTaskState( sourceTask, destinationTask );
            } else {
                this.moveTasksUpByTaskState( sourceTask, destinationTask );
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdRootProject( Task sourceTask, Task destinationTask ) {
        boolean sourceTaskRoot = destinationTask.isInRootProject();
        boolean destinationTaskRoot = destinationTask.isInRootProject();
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameProject = sourceTask.hasSameProjectAs(destinationTask);
        boolean srcIsBelowDestinationTask  = sourceTask.isBelowByProject(destinationTask);
        if (sameContext && sameProject && sourceTaskRoot && destinationTaskRoot) {
            if (srcIsBelowDestinationTask) {
                this.moveTasksDownByProject(sourceTask, destinationTask);
            } else {
                this.moveTasksUpByProject(sourceTask, destinationTask);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdRootProject(Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            null,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdProject( Task sourceTask, Task destinationTask ) {
        Project project = sourceTask.getProject();
        boolean okProject = destinationTask.hasProject(project);
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameProject = sourceTask.hasSameProjectAs(destinationTask);
        boolean srcIsBelowDestinationTask  = sourceTask.isBelowByProject(destinationTask);
        if (sameContext && sameProject && okProject) {
            if (srcIsBelowDestinationTask) {
                this.moveTasksDownByProject(sourceTask, destinationTask);
            } else {
                this.moveTasksUpByProject(sourceTask, destinationTask);
            }
        }
    }

    /**
     * Before: sourceTask is dragged from above down to destinationTask, so sourceTask is above destinationTask.
     * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
     * and destinationTask are moved one position up; destinationTask is the next Task above sourceTask.
     * @param sourceTask
     * @param destinationTask
     */
    private void moveTasksUpByTaskState( Task sourceTask, Task destinationTask ) {
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        long lowerOrderIdTaskState = destinationTask.getOrderIdTaskState();
        long higherOrderIdTaskState = sourceTask.getOrderIdTaskState();
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
            lowerOrderIdTaskState,
            higherOrderIdTaskState,
            taskState,
            context
        );
        for(Task task:tasks){
            task.moveUpByTaskState();
        }
        sourceTask.setOrderIdTaskState( destinationTask.getOrderIdTaskState() );
        destinationTask.moveDownByTaskState();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }


    private long getMaxOrderIdTaskStateFor(TaskState taskState, Context context ){
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(
            taskState,
            context
        );
        return  (task == null) ? 0 : task.getOrderIdTaskState();
    }

    /**
     * Before: sourceTask is dragged from below up to destinationTask, so sourceTask is below destinationTask.
     * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
     * are moved one position down; destinationTask is the next Task below sourceTask.
     * @param sourceTask
     * @param destinationTask
     */
    private void moveTasksDownByTaskState( Task sourceTask, Task destinationTask ) {
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
        for(Task task:tasks){
            task.moveDownByTaskState();
        }
        sourceTask.setOrderIdTaskState(destinationTask.getOrderIdProject());
        destinationTask.moveDownByTaskState();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    private void moveTasksUpByProject( Task sourceTask, Task destinationTask ) {
        Context context = sourceTask.getContext();
        Project project = sourceTask.getProject();
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project,
            context
        );
        for(Task task:tasks){
            task.moveUpByProject();
        }
        sourceTask.setOrderIdProject(destinationTask.getOrderIdProject());
        destinationTask.moveUpByProject();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    private void moveTasksDownByProject( Task sourceTask, Task destinationTask ) {
        Context context = sourceTask.getContext();
        Project project = sourceTask.getProject();
        long lowerOrderIdProject = sourceTask.getOrderIdProject();
        long higherOrderIdProject = destinationTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project,
            context
        );
        for(Task task:tasks){
            task.moveDownByProject();
        }
        sourceTask.setOrderIdProject(destinationTask.getOrderIdProject());
        destinationTask.moveDownByProject();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToInbox(Task task) {
        task.setUuid(UUID.randomUUID().toString());
        task.setRootProject();
        if(task.getDueDate()==null){
            task.setTaskState(TaskState.INBOX);
        } else {
            task.setTaskState(TaskState.SCHEDULED);
        }
        //task.setFocus(false);
        //task.setContext(context);
        long maxOrderIdProject = this.getMaxOrderIdRootProject(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);

        log.info("addToRootProject");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }
}
