package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.user.UserSessionBean;

import java.util.Locale;

@Slf4j
@Service
public class TaskControllerServiceImpl implements TaskControllerService {

    @Autowired
    protected BreadcrumbService breadcrumbService;

    public String getTaskStatePage(
        TaskState taskState,
        Page<Task> taskPage,
        UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        userSession.setLastTaskState(taskState);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", taskState.name().toLowerCase());
        model.addAttribute("userSession", userSession);
        return taskState.name().toLowerCase();
    }


    //TODO:  String back -> boolean project
    public String getView(Task task, String back){
        if(back != null && back.contentEquals("project")){
            if(task.getProject() != null) {
                return "redirect:/project/" + task.getProject().getId();
            } else {
                //TODO: /project/0" -> /project/root"
                return "redirect:/project/0";
            }
        } else {
            return "redirect:/taskstate/"+task.getTaskState().name().toLowerCase();
        }
    }

}
