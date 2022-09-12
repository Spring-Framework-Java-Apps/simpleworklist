package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMove2TaskService;

import javax.validation.constraints.NotNull;

@Slf4j
@Controller
@RequestMapping(path = "/taskstate/task")
public class TaskMove2TaskController extends AbstractController {

  private final TaskMove2TaskService taskMove2TaskService;

  @Autowired
  public TaskMove2TaskController(TaskMove2TaskService taskMove2TaskService) {
    this.taskMove2TaskService = taskMove2TaskService;
  }

  @RequestMapping(path = "/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
  public String changeTaskOrderId(
    @NotNull @PathVariable("taskId") Task sourceTask,
    @NotNull @PathVariable("destinationTaskId") Task destinationTask,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ){
    log.info("------------- changeTaskOrderId -------------");
    log.info("source Task:      "+sourceTask.toString());
    log.info("---------------------------------------------");
    log.info("destination Task: "+destinationTask.toString());
    log.info("---------------------------------------------");
    taskMove2TaskService.moveTaskToTaskAndChangeTaskOrderInTaskstate(sourceTask, destinationTask);
    userSession.setLastTaskState(sourceTask.getTaskState());
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return sourceTask.getTaskState().getUrlPathRedirect();
  }

}
