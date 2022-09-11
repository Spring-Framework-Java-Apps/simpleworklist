package org.woehlke.java.simpleworklist.domain.meso.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskStateTabControllerServiceImpl implements TaskStateTabControllerService {

  private final BreadcrumbService breadcrumbService;
  private final TaskService taskService;

  @Autowired
  public TaskStateTabControllerServiceImpl(BreadcrumbService breadcrumbService, TaskService taskService) {
    this.breadcrumbService = breadcrumbService;
    this.taskService = taskService;
  }

  @Override
  public String getTaskStatePageInbox(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.INBOX;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateInbox(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageToday(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ) {
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.TODAY;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateToday(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageNext(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ) {
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.NEXT;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateNext(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageWaiting(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ) {
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.WAITING;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateWaiting(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageScheduled(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ) {
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.SCHEDULED;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateScheduled(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageFocus( Context context, Pageable pageRequest, UserSessionBean userSession, Locale locale, Model model) {
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.FOCUS;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByFocus( context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageSomeday(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.SOMEDAY;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateSomeday(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageCompleted(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.COMPLETED;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateCompleted(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageTrash(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.TRASH;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateTrash(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageDeleted(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.DELETED;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateDeleted(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

  @Override
  public String getTaskStatePageProjects(
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    TaskState taskState = TaskState.PROJECTS;
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findByTaskStateProjects(context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getSlug() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }
}
