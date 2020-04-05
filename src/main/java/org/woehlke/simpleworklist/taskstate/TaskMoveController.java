package org.woehlke.simpleworklist.taskstate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskControllerService;

public class TaskMoveController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskMoveController.class);

    private final TaskMoveService taskMoveService;
    private final TaskControllerService taskControllerService;

    public TaskMoveController(TaskMoveService taskMoveService, TaskControllerService taskControllerService) {
        this.taskMoveService = taskMoveService;
        this.taskControllerService = taskControllerService;
    }

}
