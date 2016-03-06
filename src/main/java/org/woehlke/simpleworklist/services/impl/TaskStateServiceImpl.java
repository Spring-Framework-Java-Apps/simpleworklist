package org.woehlke.simpleworklist.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskStateService;

import javax.inject.Inject;
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
}
