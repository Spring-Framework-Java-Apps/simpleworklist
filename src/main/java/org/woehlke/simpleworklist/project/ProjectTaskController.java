package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskEnergy;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.task.TaskTime;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.Locale;


@Slf4j
@Controller
@RequestMapping(path = "/project")
public class ProjectTaskController extends AbstractController {

    private final TaskMoveService taskMoveService;
    private final ProjectControllerService projectControllerService;

    @Autowired
    public ProjectTaskController(TaskMoveService taskMoveService, ProjectControllerService projectControllerService) {
        this.taskMoveService = taskMoveService;
        this.projectControllerService = projectControllerService;
    }



}
