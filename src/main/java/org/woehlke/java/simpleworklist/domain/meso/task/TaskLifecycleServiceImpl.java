package org.woehlke.java.simpleworklist.domain.meso.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Slf4j
@Service
public class TaskLifecycleServiceImpl implements TaskLifecycleService {

  private final TaskService taskService;
  private final ProjectService projectService;

  @Autowired
  public TaskLifecycleServiceImpl(TaskService taskService, ProjectService projectService) {
    this.taskService = taskService;
    this.projectService = projectService;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task addToInbox(@Valid Task task) {
    log.info("addToInbox");
    task.setUuid(UUID.randomUUID());
    task.setRootProject();
    task.unsetFocus();
    task.setTaskState(TaskState.INBOX);
    task.setLastProject(null);
    long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
    task.setOrderIdProject(++maxOrderIdProject);
    long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
    task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("persisted: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task addToProject( @Valid Task task) {
    log.info("addToProject");
    task.setUuid(UUID.randomUUID());
    task.unsetFocus();
    long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(),task.getContext());
    task.setOrderIdProject(++maxOrderIdProject);
    long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
    task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("persisted: " + task.outProject());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task addToRootProject( @Valid Task task) {
    log.info("addToRootProject");
    task.setUuid(UUID.randomUUID());
    task.setRootProject();
    task.unsetFocus();
    task.moveToInbox();
    long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
    task.setOrderIdProject(++maxOrderIdProject);
    long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
    task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskService.saveAndFlush(task);
    log.info("persisted: " + task.outProject());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public long getMaxOrderIdTaskState(TaskState taskState, Context context) {
    Task task = taskService.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(taskState, context);
    return (task==null) ? 0 : task.getOrderIdTaskState();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public long getMaxOrderIdProject(Project project,Context context) {
    Task task = taskService.findTopByProjectAndContextOrderByOrderIdProjectDesc(project,context);
    return (task==null) ? 0 : task.getOrderIdProject();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public long getMaxOrderIdProjectRoot(Context context) {
    Task task = taskService.findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(context);
    return (task==null) ? 0 : task.getOrderIdProject();
  }

  @Override
  public String transformTaskIntoProjectGet(
    @NotNull Task task, @NotNull UserSessionBean userSession, @NotNull Model model
  ) {
    log.info("transformTaskIntoProjectGet");
    Project thisProject = new Project();
    thisProject.setName(task.getTitle());
    thisProject.setDescription(task.getText());
    thisProject.setContext(task.getContext());
    if (task.getProject() != null) {
      long projectId = task.getProject().getId();
      Project parentProject = projectService.findByProjectId(projectId);
      thisProject.setParent(parentProject);
    }
    thisProject.setContext(task.getContext());
    thisProject = projectService.add(thisProject);
    task.setProject(null);
    task.moveToTrash();
    task.emptyTrash();
    task = this.updatedViaTaskstate(task);
    log.info("tried to transform Task " + task.getId() + " to new Project " + thisProject.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task updatedViaTaskstate(Task task) {
    log.info("updatedViaTaskstate");
    if(task.getProject() != null){
      long projectId = task.getProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setProject(project);
    }
    if(task.getLastProject()!=null){
      long projectId = task.getLastProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setLastProject(project);
    }
    task = taskService.saveAndFlush(task);
    log.info("persisted: " + task.outTaskstate());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task updatedViaProjectRoot( @Valid Task task) {
    log.info("updatedViaProject");
    if(task.getProject() != null){
      long projectId = task.getProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setProject(project);
    }
    if(task.getLastProject()!=null){
      long projectId = task.getLastProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setLastProject(project);
    }
    task = taskService.saveAndFlush(task);
    log.info("persisted Task: " + task.outProject());
    return task;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Task updatedViaProject(Task task) {
    log.info("updatedViaProject");
    if(task.getProject() != null){
      long projectId = task.getProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setProject(project);
    }
    if(task.getLastProject()!=null){
      long projectId = task.getLastProject().getId();
      Project project = projectService.getReferenceById(projectId);
      task.setLastProject(project);
    }
    task = taskService.saveAndFlush(task);
    log.info("persisted Task: " + task.outProject());
    return task;
  }


    public Project addProjectFromTaskToModel(Task task, Model model){
      Project thisProject;
      if (task.getProject() == null || task.getProject().getId() == null || task.getProject().getId() == 0L) {
        thisProject = new Project();
        thisProject.setId(0L);
      } else {
        thisProject = task.getProject();
      }
      model.addAttribute("thisProject", thisProject);
      Project lastProject;
      if (task.getLastProject() == null || task.getLastProject().getId() == null || task.getLastProject().getId() == 0L) {
        lastProject = new Project();
        lastProject.setId(0L);
      } else {
        lastProject = task.getLastProject();
      }
      model.addAttribute("lastProject", lastProject);
      return thisProject;
    }


  public Task addProject(Task task){
      Task persistentTask = taskService.findOne(task.getId());
      if (task.getProject() == null || task.getProject().getId() == null || task.getProject().getId() == 0L) {
        persistentTask.setProject(null);
        if (persistentTask.getProject() == null || persistentTask.getProject().getId() == null || persistentTask.getProject().getId() == 0L) {
          persistentTask.setLastProject(null);
        } else {
          persistentTask.setLastProject(persistentTask.getProject());
        }
      } else {
        persistentTask.setProject(task.getProject());
        if (persistentTask.getProject() == null || persistentTask.getProject().getId() == null || persistentTask.getProject().getId() == 0L) {
          persistentTask.setLastProject(null);
        } else {
          persistentTask.setLastProject(persistentTask.getProject());
        }
      }
      persistentTask.merge(task);
      return persistentTask;
    }

}
