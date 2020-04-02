package org.woehlke.simpleworklist.oodm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.oodm.repository.TaskRepository;
import org.woehlke.simpleworklist.oodm.services.TaskService;

import java.util.ArrayList;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task saveAndFlush(Task entity) {
        entity = taskRepository.saveAndFlush(entity);
        LOGGER.info("saved: " + entity.toString());
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Task task) {
        task.setTaskState(TaskState.TRASH);
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
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void complete(Task task) {
        task.setTaskState(TaskState.COMPLETED);
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void incomplete(Task task) {
        task.switchToLastFocusType();
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void setFocus(Task task) {
        task.setFocus(true);
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void unsetFocus(Task task) {
        task.setFocus(false);
        taskRepository.saveAndFlush(task);
    }

    @Override
    public Page<Task> findByProject(Project thisProject, Context context, Pageable request) {
        LOGGER.info("findByProject: ");
        LOGGER.info("---------------------------------");
        LOGGER.info("thisProject: "+thisProject);
        LOGGER.info("---------------------------------");
        LOGGER.info("context:      "+ context);
        LOGGER.info("---------------------------------");
        if((thisProject == null)||(context ==null)){
            return new PageImpl<Task>(new ArrayList<Task>());
        } else {
            return taskRepository.findByProject(thisProject,request);
        }
    }

    @Override
    public Page<Task> findByRootProject(Context context, Pageable request) {
            return taskRepository.findByProjectIsNullAndContext(context,request);
    }

    @Override
    public Task findOne(long taskId) {
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getOne(taskId);
        } else {
            return null;
        }
    }

}
