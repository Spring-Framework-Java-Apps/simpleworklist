package org.woehlke.simpleworklist.control;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.FocusService;

import javax.inject.Inject;

/**
 * Created by tw on 21.02.16.
 */
@Controller
public class FocusController extends AbstractController {

    @Inject
    private FocusService focusService;

    @RequestMapping(value = "/focus/inbox", method = RequestMethod.GET)
    public final String inbox(@RequestParam(defaultValue="1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getInbox(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Inbox");
        return "focus/inbox";
    }

    @RequestMapping(value = "/focus/today", method = RequestMethod.GET)
    public final String today(@RequestParam(defaultValue="1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getToday(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Today");
        return "focus/today";
    }

    @RequestMapping(value = "/focus/next", method = RequestMethod.GET)
    public final String next(@RequestParam(defaultValue="1", required = false) int page,Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getNext(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Next");
        return "focus/next";
    }

    @RequestMapping(value = "/focus/scheduled", method = RequestMethod.GET)
    public final String scheduled(@RequestParam(defaultValue="1", required = false) int page,Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getScheduled(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Scheduled");
        return "focus/scheduled";
    }

    @RequestMapping(value = "/focus/someday", method = RequestMethod.GET)
    public final String someday(@RequestParam(defaultValue="1", required = false) int page,Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getSomeday(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Someday");
        return "focus/someday";
    }

    @RequestMapping(value = "/focus/completed", method = RequestMethod.GET)
    public final String completed(@RequestParam(defaultValue="1", required = false) int page,Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getCompleted(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Completed Tasks");
        return "focus/completed";
    }

    @RequestMapping(value = "/focus/trash", method = RequestMethod.GET)
    public final String trash(@RequestParam(defaultValue="1", required = false) int page,Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getTrash(thisUser,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype","Trash");
        return "focus/trash";
    }

}
