package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;


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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TaskMoveServiceImpl implements TaskMoveService {

  private final TaskService taskService;

  @Autowired
  public TaskMoveServiceImpl(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToRootProject(@Valid Task task) {
    task.moveTaskToRootProject();
    long maxOrderIdProject = taskService.getMaxOrderIdProjectRoot(task.getContext());
    task.setOrderIdProject(++maxOrderIdProject);
    return taskService.saveAndFlush(task);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToAnotherProject( @Valid Task task, @Valid Project project) {
    boolean okContext = task.hasSameContextAs(project);
    if(okContext) {
      task.moveTaskToAnotherProject(project);
      long maxOrderIdProject = taskService.getMaxOrderIdProject(
        task.getProject(),
        task.getContext()
      );
      task.setOrderIdProject(++maxOrderIdProject);
      taskService.saveAndFlush(task);
    }
    return task;
  }


  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToInbox(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.INBOX,
      task.getContext()
    );
    task.moveToInbox();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to inbox: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToToday(Task task) {
    Date now = new Date();
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.TODAY,
      task.getContext()
    );
    task.moveToToday();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to today: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToNext(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.NEXT,
      task.getContext()
    );
    task.moveToNext();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to next: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToWaiting(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.WAITING,
      task.getContext()
    );
    task.moveToWaiting();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to next: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToSomeday(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.SOMEDAY,
      task.getContext()
    );
    task.moveToSomeday();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to someday: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToFocus(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.FOCUS,
      task.getContext()
    );
    task.moveToFocus();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to focus: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToCompleted(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.COMPLETED,
      task.getContext()
    );
    task.moveToCompletedTasks();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to completed: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task moveTaskToTrash(Task task) {
    long newOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.TRASH,
      task.getContext()
    );
    task.moveToTrash();
    task.setOrderIdTaskState(++newOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("moved to trash: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public void moveAllCompletedToTrash( Context context) {
    long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(
      TaskState.TRASH,
      context
    );
    long newOrderIdTaskState = maxOrderIdTaskState;
    List<Task> taskListCompleted = taskService.findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
      TaskState.COMPLETED,
      context
    );
    for (Task task : taskListCompleted) {
      newOrderIdTaskState++;
      task.setOrderIdTaskState(newOrderIdTaskState);
      task.moveToTrash();
    }
    taskService.saveAll(taskListCompleted);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public void emptyTrash( Context context) {
    List<Task> taskList = taskService.findByTaskStateAndContext(
      TaskState.TRASH,
      context
    );
    List<Task> taskListChanged = new ArrayList<>(taskList.size());
    for(Task task: taskList){
      task.emptyTrash();
      taskListChanged.add(task);
    }
    taskService.saveAll(taskListChanged);
    List<Task> taskListDeleted = taskService.findByTaskStateAndContext(
      TaskState.DELETED,
      context
    );
    taskService.deleteAll(taskListDeleted);
  }
}
