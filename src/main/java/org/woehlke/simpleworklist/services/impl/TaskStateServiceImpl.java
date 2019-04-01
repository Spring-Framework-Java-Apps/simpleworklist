package org.woehlke.simpleworklist.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskStateService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tw on 21.02.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskStateServiceImpl implements TaskStateService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskStateServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


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
    public void deleteAllCompleted(Context context, UserAccount thisUser) {
        if(context == null){
            List<Task> taskList = taskRepository.findByTaskStateAndUserAccountOrderByOrderIdTaskState(TaskState.COMPLETED, thisUser);
            Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState.TRASHED, context);
            long maxOrderIdTaskState = (task==null) ? 0 : task.getOrderIdTaskState();
            for (Task mytask : taskList) {
                maxOrderIdTaskState++;
                mytask.setOrderIdTaskState(maxOrderIdTaskState);
                mytask.setTaskState(TaskState.TRASHED);
                taskRepository.save(mytask);
            }
        } else {
            if (thisUser.getId().longValue() == context.getUserAccount().getId().longValue()) {
                List<Task> taskList = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskState(TaskState.COMPLETED, context);
                Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState.TRASHED, context);
                long maxOrderIdTaskState = (task==null) ? 0 : task.getOrderIdTaskState();
                for (Task mytask : taskList) {
                    maxOrderIdTaskState++;
                    mytask.setOrderIdTaskState(maxOrderIdTaskState);
                    mytask.setTaskState(TaskState.TRASHED);
                    taskRepository.save(mytask);
                }
            }
        }
    }

    @Override
    public Page<Task> getFocus(Context context, UserAccount thisUser, Pageable request) {
        if(context == null){
            return taskRepository.findByFocusAndUserAccount(true, thisUser, request);
        } else {
            return taskRepository.findByFocusAndContext(true, context, request);
        }
    }

    @Override
    public Page<Task> getInbox(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.INBOX, context,request);
        }
    }

    @Override
    public Page<Task> getToday(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.TODAY, context,request);
        }
    }

    @Override
    public Page<Task> getNext(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.NEXT, context,request);
        }
    }

    @Override
    public Page<Task> getWaiting(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.WAITING, context,request);
        }
    }

    @Override
    public Page<Task> getScheduled(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.SCHEDULED, context,request);
        }
    }

    @Override
    public Page<Task> getSomeday(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.SOMEDAY, context,request);
        }
    }

    @Override
    public Page<Task> getCompleted(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.COMPLETED, context,request);
        }
    }

    @Override
    public Page<Task> getTrash(UserAccount thisUser, Context context, Pageable request) {
        if(thisUser.getId().longValue() != context.getUserAccount().getId().longValue()){
            return new PageImpl<Task>(new ArrayList<Task>(),request,0);
        } else {
            return taskRepository.findByTaskStateAndContext(TaskState.TRASHED, context,request);
        }
    }

}
