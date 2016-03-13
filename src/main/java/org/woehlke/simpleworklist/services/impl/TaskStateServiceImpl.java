package org.woehlke.simpleworklist.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskStateService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 21.02.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskStateServiceImpl implements TaskStateService {

    @Inject
    private TaskRepository taskRepository;


    @Override
    public Page<Task> getInbox(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.INBOX, thisUser, request);
    }

    @Override
    public Page<Task> getToday(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.TODAY, thisUser, request);
    }

    @Override
    public Page<Task> getNext(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.NEXT, thisUser, request);
    }

    @Override
    public Page<Task> getWaiting(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.WAITING, thisUser, request);
    }

    @Override
    public Page<Task> getScheduled(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.SCHEDULED, thisUser, request);
    }

    @Override
    public Page<Task> getSomeday(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.SOMEDAY, thisUser, request);
    }

    @Override
    public Page<Task> getCompleted(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.COMPLETED, thisUser, request);
    }

    @Override
    public Page<Task> getTrash(UserAccount thisUser, Pageable request) {
        return taskRepository.findByTaskStateAndUserAccount(TaskState.TRASHED, thisUser, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllCompleted(UserAccount thisUser) {
        List<Task> taskList =  taskRepository.findByTaskStateAndUserAccount(TaskState.COMPLETED, thisUser);
        for(Task task: taskList){
            task.setTaskState(TaskState.TRASHED);
            taskRepository.save(task);
        }
    }

    @Override
    public Page<Task> getFocus(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusAndUserAccount(true, thisUser, request);
    }

    @Override
    public Page<Task> getInbox(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.INBOX,area,request);
        }
    }

    @Override
    public Page<Task> getToday(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.TODAY,area,request);
        }
    }

    @Override
    public Page<Task> getNext(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.NEXT,area,request);
        }
    }

    @Override
    public Page<Task> getWaiting(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.WAITING,area,request);
        }
    }

    @Override
    public Page<Task> getScheduled(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.SCHEDULED,area,request);
        }
    }

    @Override
    public Page<Task> getSomeday(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.SOMEDAY,area,request);
        }
    }

    @Override
    public Page<Task> getCompleted(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.COMPLETED,area,request);
        }
    }

    @Override
    public Page<Task> getTrash(UserAccount thisUser, Area area, Pageable request) {
        if(thisUser.getId()!=area.getUserAccount().getId()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndArea(TaskState.TRASHED,area,request);
        }
    }

}
