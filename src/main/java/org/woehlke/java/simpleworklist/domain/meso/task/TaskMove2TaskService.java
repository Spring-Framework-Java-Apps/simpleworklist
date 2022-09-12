package org.woehlke.java.simpleworklist.domain.meso.task;

import org.woehlke.java.simpleworklist.domain.db.data.Task;

public interface TaskMove2TaskService {

  void moveTaskToTaskAndChangeTaskOrderInTaskstate(Task sourceTask, Task destinationTask);

  void moveTasksUpByProjectRoot(Task sourceTask, Task destinationTask);
  void moveTasksDownByProjectRoot(Task sourceTask, Task destinationTask);

  void moveTasksUpByProject(Task sourceTask, Task destinationTask);
  void moveTasksDownByProject(Task sourceTask, Task destinationTask);

  /**
   * Before: sourceTask is dragged from above down to destinationTask, so sourceTask is above destinationTask.
   * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
   * and destinationTask are moved one position up; destinationTask is the next Task above sourceTask.
   * @param sourceTask Task
   * @param destinationTask Task
   */
  void moveTasksUpByTaskState(Task sourceTask, Task destinationTask);

  /**
   * Before: sourceTask is dragged from below up to destinationTask, so sourceTask is below destinationTask.
   * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
   * are moved one position down; destinationTask is the next Task below sourceTask.
   * @param sourceTask Task
   * @param destinationTask Task
   */
  void moveTasksDownByTaskState(Task sourceTask, Task destinationTask);
}
