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
  public List<Task> findByTaskStateAndContext(TaskState trash, Context context) {
    return taskRepository.findByTaskStateAndContext(trash,context);
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
