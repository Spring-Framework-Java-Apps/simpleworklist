package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.oodm.repository.TaskRepository;
import org.woehlke.simpleworklist.model.services.TaskStateService;

import org.springframework.beans.factory.annotation.Autowired;

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
    public Page<Task> getFocus(Context context, Pageable request) {
        return taskRepository.findByFocusAndContext(true, context, request);
    }

    @Override
    public Page<Task> getInbox(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.INBOX, context, request);
    }

    @Override
    public Page<Task> getToday(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.TODAY, context, request);
    }

    @Override
    public Page<Task> getNext(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.NEXT, context, request);
    }

    @Override
    public Page<Task> getWaiting(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.WAITING, context, request);
    }

    @Override
    public Page<Task> getScheduled(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.SCHEDULED, context, request);
    }

    @Override
    public Page<Task> getSomeday(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.SOMEDAY, context, request);
    }

    @Override
    public Page<Task> getCompleted(Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.COMPLETED, context, request);
    }

    @Override
    public Page<Task> getTrash( Context context, Pageable request) {
        return taskRepository.findByTaskStateAndContext(TaskState.TRASH, context, request);
    }

}
