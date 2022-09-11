package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class TaskMove2TaskServiceImpl implements TaskMove2TaskService {

  private final TaskService taskService;

  @Autowired
  public TaskMove2TaskServiceImpl(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
        this.taskService.moveTasksDownByTaskState( sourceTask, destinationTask );
      } else {
        this.taskService.moveTasksUpByTaskState( sourceTask, destinationTask );
      }
    }
    log.info("-------------------------------------------------------------------------------");
    log.info(" DONE: moveTaskToTask AndChangeTaskOrder In Taskstate ");
    log.info("        "+sourceTask.getTaskState().name());
    log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
    log.info("-------------------------------------------------------------------------------");
  }

}
