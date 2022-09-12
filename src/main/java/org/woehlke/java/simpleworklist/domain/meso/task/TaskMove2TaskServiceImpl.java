package org.woehlke.java.simpleworklist.domain.meso.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskMove2TaskServiceImpl implements TaskMove2TaskService {

  private final TaskService taskService;

  @Autowired
  public TaskMove2TaskServiceImpl(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  public void moveTaskToTaskAndChangeTaskOrderInTaskstate(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
    log.info("-------------------------------------------------------------------------------");
    log.info(" START: moveTaskToTask AndChangeTaskOrder In Taskstate ");
    log.info("        "+sourceTask.getTaskState().name());
    log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
    boolean notEqualsId = ! sourceTask.equalsById(destinationTask);
    boolean notEquals = ! sourceTask.equalsByUniqueConstraint(destinationTask);
    boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
    boolean sameTaskType = sourceTask.hasSameTaskTypetAs(destinationTask);
    boolean go = notEqualsId && notEquals && sameContext && sameTaskType;
    if ( go ) {
      boolean srcIsBelowDestinationTask  = sourceTask.isBelowByTaskState(destinationTask);
      if (srcIsBelowDestinationTask) {
        this.moveTasksDownByTaskState( sourceTask, destinationTask );
      } else {
        this.moveTasksUpByTaskState( sourceTask, destinationTask );
      }
    }
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE: moveTaskToTask AndChangeTaskOrder In Taskstate ");
    log.info("        "+sourceTask.getTaskState().name());
    log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
  }


  @Override
  public void moveTasksUpByProjectRoot(Task sourceTask, Task destinationTask ) {
    log.info("-------------------------------------------------------------------------------");
    log.info(" moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
    log.info("-------------------------------------------------------------------------------");
    Context context = sourceTask.getContext();
    long lowerOrderIdProject = destinationTask.getOrderIdProject();
    long higherOrderIdProject = sourceTask.getOrderIdProject();
    List<Task> tasks = taskService.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE: moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
    log.info("-------------------------------------------------------------------------------");
  }

  @Override
  public void moveTasksDownByProjectRoot(Task sourceTask, Task destinationTask) {
    log.info("-------------------------------------------------------------------------------");
    log.info(" START moveTasks UP By Project Root");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
    Context context = sourceTask.getContext();
    final long lowerOrderIdProject = sourceTask.getOrderIdProject();
    final long higherOrderIdProject = destinationTask.getOrderIdProject();
    List<Task> tasks = taskService.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE moveTasks UP By Project Root");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
  }

  @Override
  public void moveTasksUpByProject(Task sourceTask, Task destinationTask ) {
    Project project = sourceTask.getProject();
    log.info("-------------------------------------------------------------------------------");
    log.info(" START moveTasks UP By Project("+project.out()+"):");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
    long lowerOrderIdProject = destinationTask.getOrderIdProject();
    long higherOrderIdProject = sourceTask.getOrderIdProject();
    List<Task> tasks = taskService.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE moveTasks UP By Project("+project.out()+"):");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");

  }

  @Override
  public void moveTasksDownByProject(Task sourceTask, Task destinationTask) {
    Project project = sourceTask.getProject();
    log.info("-------------------------------------------------------------------------------");
    log.info(" START moveTasks DOWN By Project("+project.out()+"):");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
    final long lowerOrderIdProject = sourceTask.getOrderIdProject();
    final long higherOrderIdProject = destinationTask.getOrderIdProject();
    List<Task> tasks = taskService.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE smoveTasks DOWN By Project("+project.out()+"):");
    log.info(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
  }

  @Override
  public void moveTasksUpByTaskState(Task sourceTask, Task destinationTask ) {
    TaskState taskState = sourceTask.getTaskState();
    Context context = sourceTask.getContext();
    final long lowerOrderIdTaskState = destinationTask.getOrderIdTaskState();
    final long higherOrderIdTaskState = sourceTask.getOrderIdTaskState();
    List<Task> tasks = taskService.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
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
    List<Task> tasks = taskService.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
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
    taskService.saveAll(tasksMoved);
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE: moveTasks DOWN By TaskState("+taskState.name()+"): "+sourceTask.getId() +" -> "+ destinationTask.getId());
    log.info("-------------------------------------------------------------------------------");
  }


}
