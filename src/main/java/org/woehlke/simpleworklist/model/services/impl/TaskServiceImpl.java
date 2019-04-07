package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.repository.TaskRepository;
import org.woehlke.simpleworklist.model.services.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task findOne(long taskId, UserAccount userAccount) {
        if(taskRepository.existsById(taskId)){
            Task t =  taskRepository.getOne(taskId);
            if(t.getUserAccount().getId().longValue()==userAccount.getId().longValue()){
                return t;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task saveAndFlush(Task entity, UserAccount userAccount) {
        if(entity.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            entity.setLastChangeTimestamp(new Date());
            entity = taskRepository.saveAndFlush(entity);
            LOGGER.info("saved: " + entity.toString());
        }
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.setTaskState(TaskState.TRASHED);
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    public boolean projectHasNoTasks(Project project, UserAccount userAccount) {
        if(project.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            return taskRepository.findByProject(project).isEmpty();
        } else {
            return true;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void undelete(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.switchToLastFocusType();
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void complete(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.setTaskState(TaskState.COMPLETED);
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void incomplete(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.switchToLastFocusType();
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void setFocus(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.setFocus(true);
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void unsetFocus(Task task, UserAccount userAccount) {
        if(task.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            task.setFocus(false);
            task.setLastChangeTimestamp(new Date());
            taskRepository.saveAndFlush(task);
        }
    }

    @Override
    public Page<Task> findByProject(Project thisProject, Pageable request, UserAccount userAccount, Context context) {
        LOGGER.info("findByProject: ");
        LOGGER.info("---------------------------------");
        LOGGER.info("thisProject: "+thisProject);
        LOGGER.info("---------------------------------");
        LOGGER.info("userAccount: "+userAccount);
        LOGGER.info("---------------------------------");
        LOGGER.info("context:        "+ context);
        LOGGER.info("---------------------------------");
        long contextUid = context.getUserAccount().getId().longValue();
        long uid = userAccount.getId().longValue();
        long projectContextId = 0;
        if (thisProject.getContext() != null){
            projectContextId = thisProject.getContext().getId().longValue();
        }
        long contextId = context.getId().longValue();
        if((thisProject == null)||(context ==null)||(contextUid != uid)||(projectContextId!=contextId)){
            return new PageImpl<Task>(new ArrayList<Task>());
        } else {
            return taskRepository.findByProject(thisProject,request);
        }
    }

    @Override
    public Page<Task> findByRootProject(Pageable request, UserAccount userAccount, Context context) {
        if(context.getUserAccount().getId().longValue() != userAccount.getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>());
        } else {
            //TODO: FEHLER: fehlt User!
            return taskRepository.findByProjectIsNullAndContext(context,request);
        }
    }

    @Override
    public Page<Task> findByUser(UserAccount userAccount, Context context, Pageable request) {
        //TODO: FEHLER: fehlt Context!
        return taskRepository.findByUserAccountAndContext(userAccount, context, request);
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
