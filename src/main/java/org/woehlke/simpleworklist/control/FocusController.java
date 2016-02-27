package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.services.FocusService;
import org.woehlke.simpleworklist.services.TaskService;

import javax.inject.Inject;

/**
 * Created by tw on 21.02.16.
 */
@Controller
public class FocusController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FocusController.class);

    @Inject
    private FocusService focusService;

    @Inject
    private TaskService taskService;

    @RequestMapping(value = "/focus/inbox", method = RequestMethod.GET)
    public final String inbox(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getInbox(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Inbox");
        return "focus/inbox";
    }

    @RequestMapping(value = "/focus/today", method = RequestMethod.GET)
    public final String today(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getToday(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Today");
        return "focus/today";
    }

    @RequestMapping(value = "/focus/next", method = RequestMethod.GET)
    public final String next(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getNext(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Next");
        return "focus/next";
    }

    @RequestMapping(value = "/focus/waiting", method = RequestMethod.GET)
    public final String waiting(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getWaiting(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Waiting");
        return "focus/waiting";
    }

    @RequestMapping(value = "/focus/scheduled", method = RequestMethod.GET)
    public final String scheduled(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getScheduled(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Scheduled");
        return "focus/scheduled";
    }

    @RequestMapping(value = "/focus/someday", method = RequestMethod.GET)
    public final String someday(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getSomeday(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Someday");
        return "focus/someday";
    }

    @RequestMapping(value = "/focus/completed", method = RequestMethod.GET)
    public final String completed(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getCompleted(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Completed Tasks");
        return "focus/completed";
    }

    @RequestMapping(value = "/focus/trash", method = RequestMethod.GET)
    public final String trash(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createdTimestamp");
        Page<Task> taskPage = focusService.getTrash(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Trash");
        return "focus/trash";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to inbox");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.INBOX);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to inbox: "+task.toString());
        return "redirect:/focus/inbox";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to today");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.TODAY);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to today: "+task.toString());
        return "redirect:/focus/today";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to next");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.NEXT);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        return "redirect:/focus/next";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to waiting");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.WAITING);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        return "redirect:/focus/waiting";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to someday");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.SOMEDAY);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to someday: "+task.toString());
        return "redirect:/focus/someday";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to completed");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.COMPLETED);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to completed: "+task.toString());
        return "redirect:/focus/completed";
    }

    @RequestMapping(value = "/focus/move/{taskId}/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to trash");
        Task task = taskService.findOne(taskId);
        task.setFocusType(FocusType.TRASHED);
        task=taskService.saveAndFlush(task);
        LOGGER.info("dragged and dropped "+taskId+" to trash: "+task.toString());
        return "redirect:/focus/trash";
    }
}