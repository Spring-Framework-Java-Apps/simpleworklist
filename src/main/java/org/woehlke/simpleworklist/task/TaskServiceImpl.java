package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.taskstate.TaskState;

import java.util.ArrayList;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task saveAndFlush(Task entity) {
        log.info("saveAndFlush");
        entity = taskRepository.saveAndFlush(entity);
        log.info("saved: " + entity.toString());
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Task task) {
        log.info("delete");
        task.setTaskState(TaskState.TRASH);
        taskRepository.saveAndFlush(task);
    }

    @Override
    public boolean projectHasNoTasks(Project project) {
        log.info("projectHasNoTasks");
        return taskRepository.findByProject(project).isEmpty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void undelete(Task task) {
        log.info("undelete");
        task.switchToLastFocusType();
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void complete(Task task) {
        log.info("complete");
        task.setTaskState(TaskState.COMPLETED);
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void incomplete(Task task) {
        log.info("incomplete");
        task.switchToLastFocusType();
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void setFocus(Task task) {
        log.info("setFocus");
        task.setFocus(true);
        taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void unsetFocus(Task task) {
        log.info("unsetFocus");
        task.setFocus(false);
        taskRepository.saveAndFlush(task);
    }

    @Override
    public Page<Task> findByProject(Project thisProject, Context context, Pageable request) {
        log.info("findByProject: ");
        log.info("---------------------------------");
        log.info("thisProject: "+thisProject);
        log.info("---------------------------------");
        log.info("context:      "+ context);
        log.info("---------------------------------");
        if((thisProject == null)||(context ==null)){
            return new PageImpl<Task>(new ArrayList<Task>());
        } else {
            return taskRepository.findByProject(thisProject,request);
        }
    }

    @Override
    public Page<Task> findByRootProject(Context context, Pageable request) {
        log.info("findByRootProject: ");
        return taskRepository.findByProjectIsNullAndContext(context,request);
    }

    @Override
    public Task findOne(long taskId) {
        log.info("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getOne(taskId);
        } else {
            return null;
        }
    }

}
