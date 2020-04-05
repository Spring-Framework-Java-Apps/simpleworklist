package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskControllerServiceImpl implements TaskControllerService {

    public String getView(Task task,String back){
        if(back != null && back.contentEquals("project")){
            if(task.getProject() != null) {
                return "redirect:/project/" + task.getProject().getId();
            } else {
                return "redirect:/project/0";
            }
        }
        switch (task.getTaskState()) {
            case TODAY:
                return "redirect:/taskstate/today";
            case NEXT:
                return "redirect:/taskstate/next";
            case WAITING:
                return "redirect:/taskstate/waiting";
            case SCHEDULED:
                return "redirect:/taskstate/scheduled";
            case SOMEDAY:
                return "redirect:/taskstate/someday";
            case COMPLETED:
                return "redirect:/taskstate/completed";
            default:
                return "redirect:/taskstate/inbox";
        }
    }

}
