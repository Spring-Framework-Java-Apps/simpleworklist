package org.woehlke.simpleworklist.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.FocusService;

import javax.inject.Inject;

/**
 * Created by tw on 21.02.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FocusServiceImpl implements FocusService {

    @Inject
    private TaskRepository taskRepository;


    @Override
    public Page<Task> getInbox(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusTypeAndUserAccount(FocusType.INBOX, thisUser, request);
    }

    @Override
    public Page<Task> getToday(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusTypeAndUserAccount(FocusType.TODAY, thisUser, request);
    }

    @Override
    public Page<Task> getNext(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusTypeAndUserAccount(FocusType.NEXT, thisUser, request);
    }

    @Override
    public Page<Task> getScheduled(UserAccount thisUser, Pageable request) {
        return taskRepository.findByDueDateIsNotNullAndUserAccount(thisUser, request);
    }

    @Override
    public Page<Task> getSomeday(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusTypeAndUserAccount(FocusType.SOMEDAY, thisUser, request);
    }

    @Override
    public Page<Task> getCompleted(UserAccount thisUser, Pageable request) {
        return taskRepository.findByStatusAndUserAccount(TaskState.DONE, thisUser, request);
    }

    @Override
    public Page<Task> getTrash(UserAccount thisUser, Pageable request) {
        return taskRepository.findByFocusTypeAndUserAccount(FocusType.TRASHED, thisUser, request);
    }
}
