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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
  public Task saveAndFlush(Task task) {
    return taskRepository.saveAndFlush(task);
  }

  @Override
  public void deleteAll(List<Task> taskListDeleted) {
    taskRepository.deleteAll(taskListDeleted);
  }

  @Override
  public void saveAll(List<Task> taskListChanged) {
    taskRepository.saveAll(taskListChanged);
  }

  @Override
  public List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState completed, Context context) {
    return taskRepository.findByTaskStateAndContextOrderByOrderIdTaskStateAsc(completed, context);
  }

  @Override
  public List<Task> findByTaskStateInbox(Context context) {
    return taskRepository.findByTaskStateInbox(context);
  }

  @Override
  public List<Task> findByTaskStateToday(Context context) {
    return taskRepository.findByTaskStateToday(context);
  }

  @Override
  public List<Task> findByTaskStateNext(Context context) {
    return taskRepository.findByTaskStateNext(context);
  }

  @Override
  public List<Task> findByTaskStateWaiting(Context context) {
    return taskRepository.findByTaskStateWaiting(context);
  }

  @Override
  public List<Task> findByTaskStateScheduled(Context context) {
    return taskRepository.findByTaskStateScheduled(context);
  }

  @Override
  public List<Task> findByTaskStateSomeday(Context context) {
    return taskRepository.findByTaskStateSomeday(context);
  }

  @Override
  public List<Task> findByFocus(Context context) {
    return taskRepository.findByFocusIsTrueAndContext(context);
  }

  @Override
  public List<Task> findByTaskStateCompleted(Context context) {
    return taskRepository.findByTaskStateCompleted(context);
  }

  @Override
  public List<Task> findByTaskStateTrash(Context context) {
    return taskRepository.findByTaskStateTrash(context);
  }

  @Override
  public List<Task> findByTaskStateDeleted(Context context) {
    return taskRepository.findByTaskStateDeleted(context);
  }

  @Override
  public List<Task> findByTaskStateProjects(Context context) {
    return taskRepository.findByTaskStateProjects(context);
  }

  @Override
  public Page<Task> findByTaskStateInbox(Context context, Pageable request) {
    return taskRepository.findByTaskStateInbox(context, request);
  }

  @Override
  public Page<Task> findByTaskStateToday(Context context, Pageable request) {
    return taskRepository.findByTaskStateToday(context, request);
  }

  @Override
  public Page<Task> findByTaskStateNext(Context context, Pageable request) {
    return taskRepository.findByTaskStateNext(context, request);
  }

  @Override
  public Page<Task> findByTaskStateWaiting(Context context, Pageable request) {
    return taskRepository.findByTaskStateWaiting(context, request);
  }

  @Override
  public Page<Task> findByTaskStateScheduled(Context context, Pageable request) {
    return taskRepository.findByTaskStateScheduled(context, request);
  }

  @Override
  public Page<Task> findByTaskStateSomeday(Context context, Pageable request) {
    return taskRepository.findByTaskStateSomeday(context, request);
  }

  @Override
  public Page<Task> findByFocus(Context context, Pageable request) {
    return taskRepository.findByFocusIsTrueAndContext(context, request);
  }

  @Override
  public Page<Task> findByTaskStateCompleted(Context context, Pageable request) {
    return taskRepository.findByTaskStateCompleted(context, request);
  }

  @Override
  public Page<Task> findByTaskStateTrash(Context context, Pageable request) {
    return taskRepository.findByTaskStateTrash(context, request);
  }

  @Override
  public Page<Task> findByTaskStateDeleted(Context context, Pageable request) {
    return taskRepository.findByTaskStateDeleted(context, request);
  }

  @Override
  public Page<Task> findByTaskStateProjects(Context context, Pageable request) {
    return taskRepository.findByTaskStateProjects(context, request);
  }

  @Override
  public Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context) {
    return taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(taskState,context);
  }

  @Override
  public Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context) {
    return taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(project,context);
  }

  @Override
  public Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context) {
    return taskRepository.findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(context);
  }

}
