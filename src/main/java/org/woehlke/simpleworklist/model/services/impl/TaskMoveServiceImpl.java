package org.woehlke.simpleworklist.model.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.oodm.repository.TaskRepository;
import org.woehlke.simpleworklist.model.services.TaskMoveService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TaskMoveServiceImpl implements TaskMoveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskMoveServiceImpl.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskMoveServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task moveTaskToRootProject(Task task) {
        if(task!=null){
            task.setProject(null);
            long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(),task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Override
    public Task moveTaskToAnotherProject(Task task, Project project) {
        if(task!=null){
            task.setProject(project);
            long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(),task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Override
    public Task moveTaskToInbox(Task task) {
        if(task!=null){
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.INBOX,task.getContext(),task.getContext().getUserAccount());
            task.setTaskState(TaskState.INBOX);
            task.setOrderIdTaskState(++maxOrderId);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() +" to inbox: "+task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToToday(Task task) {
        if(task!=null) {
            Date now = new Date();
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.TODAY,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.TODAY);
            task.setDueDate(now);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to today: " + task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToNext(Task task) {
        if(task!=null) {
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.NEXT, task.getContext(), task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.NEXT);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to next: " + task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToWaiting(Task task) {
        if(task!=null){
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.WAITING,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.WAITING);
            task=taskRepository.saveAndFlush(task);
            LOGGER.info("moved "+ task.getId() +" to next: "+task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToSomeday(Task task) {
        if(task!=null) {
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.SOMEDAY,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.SOMEDAY);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to someday: " + task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToFocus(Task task) {
        if(task!=null) {
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.FOCUS,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setFocus(true);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to focus: " + task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToCompleted(Task task) {
        if(task!=null) {
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.COMPLETED);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to completed: " + task.toString());
        }
        return task;
    }

    @Override
    public Task moveTaskToTrash(Task task) {
        if(task!=null) {
            long maxOrderId = this.getMaxOrderIdTaskState(TaskState.TRASH,task.getContext(),task.getContext().getUserAccount());
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.TRASH);
            task = taskRepository.saveAndFlush(task);
            LOGGER.info("moved " + task.getId() + " to trash: " + task.toString());
        }
        return task;
    }

    @Override
    public void deleteAllCompleted(Context context, UserAccount thisUser) {
        if(context == null) {
            context = thisUser.getDefaultContext();
        }
        if (thisUser.getId().longValue() == context.getUserAccount().getId().longValue()) {
            List<Task> taskList = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskState(TaskState.COMPLETED, context);
            Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState.TRASH, context);
            long maxOrderIdTaskState = (task == null) ? 0 : task.getOrderIdTaskState();
            for (Task mytask : taskList) {
                maxOrderIdTaskState++;
                mytask.setOrderIdTaskState(maxOrderIdTaskState);
                mytask.setTaskState(TaskState.TRASH);
                taskRepository.save(mytask);
            }
        }
    }

    @Override
    public void emptyTrash(UserAccount userAccount, Context context) {
        List<Task> taskList = taskRepository.findByTaskStateAndContext(TaskState.TRASH,context);
        for(Task task:taskList){
            taskRepository.delete(task);
        }
    }

    @Override
    public long getMaxOrderIdTaskState(TaskState inbox, Context context, UserAccount thisUser) {
        long maxOrderIdTaskState = 0;
        if(context.getUserAccount().getId().longValue()==thisUser.getId().longValue()) {
            Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(inbox, context);
            maxOrderIdTaskState = (task==null) ? 0 : task.getOrderIdTaskState();
        }
        return maxOrderIdTaskState;
    }

    @Override
    public long getMaxOrderIdProject(Project project, Context context, UserAccount userAccount) {
        long maxOrderIdProject = 0;
        if(context.getUserAccount().getId().longValue()==userAccount.getId().longValue()) {
            Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(project, context);
            maxOrderIdProject = (task==null) ? 0 : task.getOrderIdProject();
        }
        return maxOrderIdProject;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdTaskState(TaskState taskState, Task sourceTask, Task destinationTask, Context context) {
        long destinationTaskOrderIdTaskState = destinationTask.getOrderIdTaskState();
        boolean down = sourceTask.getOrderIdTaskState()<destinationTask.getOrderIdTaskState();
        List<Task> tasks;
        if(down){
            tasks = taskRepository.getTasksToReorderByOrderIdTaskState(sourceTask.getOrderIdTaskState(), destinationTask.getOrderIdTaskState(), taskState, context);
        } else {
            tasks = taskRepository.getTasksToReorderByOrderIdTaskState(destinationTask.getOrderIdTaskState(), sourceTask.getOrderIdTaskState(), taskState, context);
        }
        this.reorder( down, destinationTaskOrderIdTaskState, tasks, sourceTask, destinationTask );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdProject(Project project, Task sourceTask, Task destinationTask, Context context) {
        long destinationOrderIdProject = destinationTask.getOrderIdProject();
        boolean down = sourceTask.getOrderIdTaskState()<destinationTask.getOrderIdTaskState();
        List<Task> tasks;
        if(down){
            tasks = taskRepository.getTasksToReorderByOrderIdProject(sourceTask.getOrderIdTaskState(), destinationTask.getOrderIdTaskState(), project, context);
        } else {
            tasks = taskRepository.getTasksToReorderByOrderIdProject(destinationTask.getOrderIdTaskState(), sourceTask.getOrderIdTaskState(), project, context);
        }
        this.reorder( down, destinationOrderIdProject, tasks, sourceTask, destinationTask );
    }

    private void reorder(boolean down, long destinationOrderId, List<Task> tasks, Task sourceTask, Task destinationTask){
        for(Task task:tasks){
            long orderId = task.getOrderIdTaskState();
            if(down){
                orderId--;
            } else {
                orderId++;
            }
            task.setOrderIdTaskState(orderId);
            taskRepository.saveAndFlush(task);
        }
        long orderId = destinationTask.getOrderIdTaskState();
        if(down){
            orderId--;
        } else {
            orderId++;
            destinationOrderId++;
        }
        destinationTask.setOrderIdTaskState(orderId);
        taskRepository.saveAndFlush(destinationTask);
        sourceTask.setOrderIdTaskState(destinationOrderId);
        taskRepository.saveAndFlush(sourceTask);
    }
}
