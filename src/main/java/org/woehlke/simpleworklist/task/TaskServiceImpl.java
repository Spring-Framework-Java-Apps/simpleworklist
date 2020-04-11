package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository ) {
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
    public Page<Task> findbyTaskstate(
        @NotNull TaskState taskState,
        @NotNull Context context,
        @NotNull Pageable request
    ) {
        if(taskState == TaskState.FOCUS){
            return taskRepository.findByFocusAndContext(true,context,request);
        }else {
            return taskRepository.findByTaskStateAndContext(taskState, context, request);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByProject(
        @NotNull Project thisProject, @NotNull Pageable request
    ) {
        log.info("findByProject: ");
        log.info("---------------------------------");
        log.info("thisProject: "+thisProject);
        log.info("---------------------------------");
        return taskRepository.findByProject(thisProject,request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByRootProject(@NotNull Context context, @NotNull Pageable request) {
        log.info("findByRootProject: ");
        return taskRepository.findByProjectIsNullAndContext(context, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task findOne(@Min(1L) long taskId) {
        log.info("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getOne(taskId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaTaskstate(@NotNull Task task) {
        log.info("updatedViaTaskstate");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProject(@NotNull Task task) {
        log.info("updatedViaProject");
        task = taskRepository.saveAndFlush(task);
        log.info("persisted Task: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToProject(@NotNull Task task) {
        log.info("addToProject");
        task.setUuid(UUID.randomUUID().toString());
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToRootProject(@NotNull Task task) {
        log.info("addToRootProject");
        task.setUuid(UUID.randomUUID().toString());
        task.unsetFocus();
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToRootProject(@NotNull Task task) {
        task.setRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(), task.getContext()
        );
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToAnotherProject(@NotNull Task task, @NotNull Project project) {
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
    public Task moveTaskToInbox(@NotNull Task task) {
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
    public Task moveTaskToToday(@NotNull Task task) {
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
    public Task moveTaskToNext(@NotNull Task task) {
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
    public Task moveTaskToWaiting(@NotNull Task task) {
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
    public Task moveTaskToSomeday(@NotNull Task task) {
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
    public Task moveTaskToFocus(@NotNull Task task) {
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
    public Task moveTaskToCompleted(@NotNull Task task) {
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
    public Task moveTaskToTrash(@NotNull Task task) {
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
    public void moveAllCompletedToTrash(@NotNull Context context) {
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
    public void emptyTrash(@NotNull Context context) {
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
    public long getMaxOrderIdTaskState(@NotNull TaskState taskState, @NotNull Context context) {
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(
            taskState,
            context
        );
        return (task==null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProject(@NotNull Project project, @NotNull Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            project,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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
    public void moveOrderIdRootProject(@NotNull Task sourceTask,@NotNull Task destinationTask ) {
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
    public long getMaxOrderIdRootProject(@NotNull Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            null,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdProject(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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
    private void moveTasksUpByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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


    private long getMaxOrderIdTaskStateFor(@NotNull TaskState taskState, @NotNull Context context ){
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
    private void moveTasksDownByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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

    private void moveTasksUpByProject(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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

    private void moveTasksDownByProject(@NotNull Task sourceTask, @NotNull Task destinationTask) {
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
    public Task addToInbox(@NotNull Task task) {
        log.info("addToInbox");
        task.setUuid(UUID.randomUUID().toString());
        task.setRootProject();
        task.unsetFocus();
        task.setTaskState(TaskState.INBOX);
        /*
        if(task.getDueDate()==null){
            task.setTaskState(TaskState.INBOX);
        } else {
            task.setTaskState(TaskState.SCHEDULED);
        }
        */
        //task.setFocus(false);
        //task.setContext(context);
        long maxOrderIdProject = this.getMaxOrderIdRootProject(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.getId());
        return task;
    }
}
