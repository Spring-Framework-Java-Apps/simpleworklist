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
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProject(@NotNull Task task) {
        log.info("updatedViaProject");
        long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(), task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProjectRoot(@NotNull Task task) {
        log.info("updatedViaProject");
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        task = taskRepository.saveAndFlush(task);
        log.info("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToInbox(@NotNull Task task) {
        log.info("addToInbox");
        task.setUuid(UUID.randomUUID().toString());
        task.setRootProject();
        task.unsetFocus();
        task.setTaskState(TaskState.INBOX);
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
    public Task addToProject(@NotNull Task task) {
        log.info("addToProject");
        task.setUuid(UUID.randomUUID().toString());
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
    public Task addToRootProject(@NotNull Task task) {
        log.info("addToRootProject");
        task.setUuid(UUID.randomUUID().toString());
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
    public Task moveTaskToRootProject(@NotNull Task task) {
        task.moveTaskToRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToAnotherProject(@NotNull Task task, @NotNull Project project) {
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
    public void moveAllCompletedToTrash(@NotNull Context context) {
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProjectRoot(@NotNull Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectIsNullDesc(
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksUpByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksDownByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksUpByProjectRoot(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksDownByProjectRoot(@NotNull Task sourceTask, @NotNull Task destinationTask) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksUpByProject(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        Project project = sourceTask.getProject();
        log.info("-------------------------------------------------------------------------------");
        log.info(" START moveTasks UP By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        Context context = sourceTask.getContext();
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project,
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
        log.info(" DONE moveTasks UP By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTasksDownByProject(@NotNull Task sourceTask, @NotNull Task destinationTask) {
        Context context = sourceTask.getContext();
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
            project,
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
        log.info(" DONE smoveTasks DOWN By Project("+project.out()+"):");
        log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToInbox(@NotNull Task task) {
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
    public Task moveTaskToToday(@NotNull Task task) {
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
    public Task moveTaskToNext(@NotNull Task task) {
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
    public Task moveTaskToWaiting(@NotNull Task task) {
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
    public Task moveTaskToSomeday(@NotNull Task task) {
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
    public Task moveTaskToFocus(@NotNull Task task) {
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
    public Task moveTaskToCompleted(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.COMPLETED,
            task.getContext()
        );
        task.moveToCompletedTasks();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to completed: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToTrash(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            task.getContext()
        );
        task.moveToTrash();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to trash: " + task.outTaskstate());
        return task;
    }
}
