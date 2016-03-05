package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskService;

import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);


    @Inject
    private TaskRepository taskRepository;

    @Override
    public Page<Task> findByProject(Project thisProject,
                                    Pageable request) {
        return taskRepository.findByProject(thisProject, request);
    }

    @Override
    public Page<Task> findByRootProject(Pageable request, UserAccount userAccount) {
        return taskRepository.findByProjectIsNullAndUserAccount(request, userAccount);
    }

    @Override
    public Task findOne(long dataId) {
        return taskRepository.findOne(dataId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task saveAndFlush(Task entity) {
        entity.setLastChangeTimestamp(new Date());
        entity = taskRepository.saveAndFlush(entity);
        LOGGER.info("saved: "+entity.toString());
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Task task) {
        task.setFocusType(FocusType.TRASHED);
        task.setLastChangeTimestamp(new Date());
        taskRepository.saveAndFlush(task);
    }

    @Override
    public boolean projectHasNoTasks(Project project) {
        return taskRepository.findByProject(project).isEmpty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void undelete(Task task) {
        task.switchToLastFocusType();
        task.setLastChangeTimestamp(new Date());
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void emptyTrash(UserAccount userAccount) {
        List<Task> taskList = taskRepository.findByFocusTypeAndUserAccount(FocusType.TRASHED,userAccount);
        for(Task task:taskList){
            taskRepository.delete(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void complete(Task task) {
        task.setFocusType(FocusType.COMPLETED);
        task.setLastChangeTimestamp(new Date());
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void incomplete(Task task) {
        task.switchToLastFocusType();
        task.setLastChangeTimestamp(new Date());
        taskRepository.saveAndFlush(task);
    }

}
