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
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.TaskService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TaskServiceImpl implements TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);


    @Inject
    private TaskRepository taskRepository;

    @Override
    public Page<Task> findByCategory(Project thisProject,
                                     Pageable request) {
        return taskRepository.findByProject(thisProject, request);
    }

    @Override
    public Page<Task> findByRootCategory(Pageable request) {
        return taskRepository.findByProjectIsNull(request);
    }

    @Override
    public Task findOne(long dataId) {
        return taskRepository.findOne(dataId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task saveAndFlush(Task entity) {
        entity = taskRepository.saveAndFlush(entity);
        LOGGER.info("saved: "+entity.toString());
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Task task) {
        task.setFocusType(FocusType.TRASHED);
        taskRepository.saveAndFlush(task);
    }

    @Override
    public boolean categoryHasNoData(Project project) {
        return taskRepository.findByProject(project).isEmpty();
    }

}
