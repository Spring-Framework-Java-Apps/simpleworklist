package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

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
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Inject
    private TaskRepository taskRepository;

    @Override
    public Page<Task> findByProject(Project thisProject,
                                    Pageable request, UserAccount userAccount) {
        if(thisProject.getUserAccount().getId().longValue() == userAccount.getId().longValue()){
            return taskRepository.findByProject(thisProject, request);
        } else {
            return null;
        }
    }

    @Override
    public Page<Task> findByRootProject(Pageable request, UserAccount userAccount) {
        return taskRepository.findByProjectIsNullAndUserAccount(request, userAccount);
    }

    @Override
    public Task findOne(long dataId, UserAccount userAccount) {
        Task t =  taskRepository.findOne(dataId);
        if(t.getUserAccount().getId().longValue()==userAccount.getId().longValue()){
            return t;
        } else {
            return null;
        }
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
    public void emptyTrash(UserAccount userAccount) {
        List<Task> taskList = taskRepository.findByTaskStateAndUserAccount(TaskState.TRASHED,userAccount);
        for(Task task:taskList){
            taskRepository.delete(task);
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
            return taskRepository.findByProjectIsNullAndContext(context,request);
        }
    }

    @Override
    public Page<Task> findByUser(UserAccount userAccount, Pageable request) {
        return taskRepository.findByUserAccount(userAccount,request);
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
    public void moveOrderIdTaskState(Task sourceTask, Task destinationTask) {
       long destinationTaskOrderIdTaskState = destinationTask.getOrderIdTaskState();
       if(sourceTask.getOrderIdTaskState()<destinationTask.getOrderIdTaskState()){
           List<Task> tasks = taskRepository.getTasksToReorderByOrderIdTaskState(sourceTask.getOrderIdTaskState(),destinationTask.getOrderIdTaskState());
           for(Task task:tasks){
               task.setOrderIdTaskState(task.getOrderIdTaskState()-1);
               taskRepository.saveAndFlush(task);
           }
           destinationTask.setOrderIdTaskState(destinationTask.getOrderIdTaskState()-1);
           taskRepository.saveAndFlush(destinationTask);
           sourceTask.setOrderIdTaskState(destinationTaskOrderIdTaskState);
           taskRepository.saveAndFlush(sourceTask);
       } else {
           List<Task> tasks = taskRepository.getTasksToReorderByOrderIdTaskState(destinationTask.getOrderIdTaskState(),sourceTask.getOrderIdTaskState());
           for(Task task:tasks){
               task.setOrderIdTaskState(task.getOrderIdTaskState()+1);
               taskRepository.saveAndFlush(task);
           }
           sourceTask.setOrderIdTaskState(destinationTaskOrderIdTaskState+1);
           taskRepository.saveAndFlush(sourceTask);
       }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveOrderIdProject(Task sourceTask, Task destinationTask) {
        long destinationOrderIdProject = destinationTask.getOrderIdProject();
        if(sourceTask.getOrderIdProject()<destinationTask.getOrderIdProject()){
            List<Task> tasks = taskRepository.getTasksToReorderByOrderIdProject(sourceTask.getOrderIdProject(),destinationTask.getOrderIdProject());
            for(Task task:tasks){
                task.setOrderIdProject(task.getOrderIdProject()-1);
                taskRepository.saveAndFlush(task);
            }
            destinationTask.setOrderIdProject(destinationTask.getOrderIdProject()-1);
            taskRepository.saveAndFlush(destinationTask);
            sourceTask.setOrderIdProject(destinationOrderIdProject);
            taskRepository.saveAndFlush(sourceTask);
        } else {
            List<Task> tasks = taskRepository.getTasksToReorderByOrderIdProject(destinationTask.getOrderIdProject(),sourceTask.getOrderIdProject());
            for(Task task:tasks){
                task.setOrderIdProject(task.getOrderIdProject()+1);
                taskRepository.saveAndFlush(task);
            }
            sourceTask.setOrderIdProject(destinationOrderIdProject+1);
            taskRepository.saveAndFlush(sourceTask);
        }
    }

}
