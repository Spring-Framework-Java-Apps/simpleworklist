package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

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
public class TaskStateTabControllerServiceImpl implements TaskStateTabControllerService {

  private final BreadcrumbService breadcrumbService;
  private final TaskService taskService;

  @Autowired
  public TaskStateTabControllerServiceImpl(BreadcrumbService breadcrumbService, TaskService taskService) {
    this.breadcrumbService = breadcrumbService;
    this.taskService = taskService;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public String getTaskStatePage(
    @NotNull TaskState taskState,
    @NotNull Context context,
    @NotNull Pageable pageRequest,
    @NotNull UserSessionBean userSession,
    @NotNull Locale locale,
    @NotNull Model model
  ){
    log.info("getTaskStatePage");
    userSession.setLastTaskState(taskState);
    Page<Task> taskPage = taskService.findbyTaskstate(taskState, context, pageRequest);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("taskPage", taskPage);
    model.addAttribute("taskstateType", taskState.getType() );
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return taskState.getTemplate();
  }

}
