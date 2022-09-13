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
    public Page<Task> findByProjectId(
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
    public Page<Task> findByProjectRoot(Context context, Pageable request) {
        log.info("findByRootProject: ");
        return taskRepository.findByProjectIsNullAndContext(context, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task findById(@Min(1L) long taskId) {
        log.info("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getReferenceById(taskId);
        } else {
            return null;
        }
    }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task saveAndFlush(Task task) {
    return taskRepository.saveAndFlush(task);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public void deleteAll(List<Task> taskListDeleted) {
    taskRepository.deleteAll(taskListDeleted);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
  public List<Task> findByProjectId(Project thisProject) {
    return taskRepository.findByProject(thisProject);
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
  public Page<Task> findByTaskStateAll(Context context, Pageable pageRequest) {
    return taskRepository.findByContext(context, pageRequest);
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

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(long lowerOrderIdTaskState, long higherOrderIdTaskState, TaskState taskState, Context context) {
    return taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask( lowerOrderIdTaskState, higherOrderIdTaskState, taskState, context);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(long lowerOrderIdProject, long higherOrderIdProject, Context context) {
    return taskRepository.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(lowerOrderIdProject,higherOrderIdProject,context);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<Task> getTasksByOrderIdProjectIdBetweenLowerTaskAndHigherTask(long lowerOrderIdProject, long higherOrderIdProject, Project project) {
    return taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(lowerOrderIdProject, higherOrderIdProject, project);
  }

}
