package org.woehlke.simpleworklist.taskstate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TaskMoveServiceImpl implements TaskMoveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskMoveServiceImpl.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskMoveServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task moveTaskToRootProject(Task task) {
        task.setRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(), task.getContext()
        );
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public Task moveTaskToAnotherProject(Task task, Project project) {
        boolean okContext = task.hasSameContextAs(project);
        if(okContext) {
            task.setProject(project);
            long maxOrderIdProject = this.getMaxOrderIdProject(
                    task.getProject(), task.getContext()
            );
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Override
    public Task moveTaskToInbox(Task task) {
            long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                    TaskState.INBOX, task.getContext()
            );
            task.setTaskState(TaskState.INBOX);
            task.setOrderIdTaskState(++newOrderIdTaskState);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to inbox: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToToday(Task task) {
            Date now = new Date();
            long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                    TaskState.TODAY, task.getContext()
            );
            task.setOrderIdTaskState(++newOrderIdTaskState);
            task.setTaskState(TaskState.TODAY);
            task.setDueDate(now);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to today: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToNext(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(TaskState.NEXT, task.getContext());
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.NEXT);
        task = taskRepository.saveAndFlush(task);
        LOGGER.info("moved " + task.getId() + " to next: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToWaiting(Task task) {
            long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                    TaskState.WAITING, task.getContext()
            );
            task.setOrderIdTaskState(++newOrderIdTaskState);
            task.setTaskState(TaskState.WAITING);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to next: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToSomeday(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                TaskState.SOMEDAY, task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.SOMEDAY);
        task = taskRepository.saveAndFlush(task);
        LOGGER.info("moved " + task.getId() + " to someday: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToFocus(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                TaskState.FOCUS, task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setFocus(true);
        task = taskRepository.saveAndFlush(task);
        LOGGER.info("moved " + task.getId() + " to focus: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToCompleted(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
                TaskState.COMPLETED, task.getContext()
        );
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.COMPLETED);
        task = taskRepository.saveAndFlush(task);
        LOGGER.debug("moved " + task.getId() + " to completed: " + task.toString());
        return task;
    }

    @Override
    public Task moveTaskToTrash(Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(TaskState.TRASH, task.getContext());
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task.setTaskState(TaskState.TRASH);
        task = taskRepository.saveAndFlush(task);
        LOGGER.debug("moved " + task.getId() + " to trash: " + task.toString());
        return task;
    }

    @Override
    public void deleteAllCompleted(Context context) {
        long maxOrderIdTaskState = this.getMaxOrderIdTaskStateFor(TaskState.TRASH, context);
        long newOrderIdTaskState = maxOrderIdTaskState;
        List<Task> taskListCompleted = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState.COMPLETED, context);
        for (Task task : taskListCompleted) {
            newOrderIdTaskState++;
            task.setOrderIdTaskState(newOrderIdTaskState);
            task.setTaskState(TaskState.TRASH);
        }
        taskRepository.saveAll(taskListCompleted);
    }

    private long getMaxOrderIdTaskStateFor(TaskState taskState, Context context){
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(taskState, context);
        return  (task == null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    public void emptyTrash(Context context) {
        List<Task> taskList = taskRepository.findByTaskStateAndContext(TaskState.TRASH, context);
        taskRepository.deleteInBatch(taskList);
    }

    @Override
    public long getMaxOrderIdTaskState(TaskState taskState, Context context) {
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(taskState, context);
        return (task==null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    public long getMaxOrderIdProject(Project project, Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(project, context);
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdTaskState(Task sourceTask, Task destinationTask) {
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
                this.moveTasksDownByTaskState(sourceTask, destinationTask);
            } else {
                this.moveTasksUpByTaskState(sourceTask, destinationTask);
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
    private void moveTasksUpByTaskState(Task sourceTask, Task destinationTask) {
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        long lowerOrderIdTaskState = destinationTask.getOrderIdTaskState();
        long higherOrderIdTaskState = sourceTask.getOrderIdTaskState();
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
                lowerOrderIdTaskState,higherOrderIdTaskState,taskState,context
        );
        for(Task task:tasks){
            task.moveUpByTaskState();
        }
        sourceTask.setOrderIdTaskState(destinationTask.getOrderIdTaskState());
        destinationTask.moveDownByTaskState();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    /**
     * Before: sourceTask is dragged from below up to destinationTask, so sourceTask is below destinationTask.
     * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
     * are moved one position down; destinationTask is the next Task below sourceTask.
     * @param sourceTask
     * @param destinationTask
     */
    private void moveTasksDownByTaskState(Task sourceTask, Task destinationTask) {
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        long lowerOrderIdTaskState = sourceTask.getOrderIdTaskState();
        long higherOrderIdTaskState = destinationTask.getOrderIdTaskState();
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
                lowerOrderIdTaskState,higherOrderIdTaskState,taskState,context
        );
        for(Task task:tasks){
            task.moveDownByTaskState();
        }
        sourceTask.setOrderIdTaskState(destinationTask);
        destinationTask.moveDownByTaskState();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdRootProject(Task sourceTask, Task destinationTask) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdProject(Task sourceTask, Task destinationTask) {
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

    private void moveTasksUpByProject(Task sourceTask, Task destinationTask) {
        Context context = sourceTask.getContext();
        Project project = sourceTask.getProject();
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
                lowerOrderIdProject,higherOrderIdProject,project,context
        );
        for(Task task:tasks){
            task.moveUpByProject();
        }
        sourceTask.setOrderIdProject(destinationTask);
        destinationTask.moveUpByProject();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

    private void moveTasksDownByProject(Task sourceTask, Task destinationTask) {
        Context context = sourceTask.getContext();
        Project project = sourceTask.getProject();
        long lowerOrderIdProject = sourceTask.getOrderIdProject();
        long higherOrderIdProject = destinationTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
                lowerOrderIdProject,higherOrderIdProject,project,context
        );
        for(Task task:tasks){
            task.moveDownByProject();
        }
        sourceTask.setOrderIdProject(destinationTask);
        destinationTask.moveDownByProject();
        tasks.add(sourceTask);
        tasks.add(destinationTask);
        taskRepository.saveAll(tasks);
    }

}
