package org.woehlke.java.simpleworklist.domain.db.data.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskRepository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        TaskRepository taskRepository
    ) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Project> findRootProjectsByContext(@NotNull Context context) {
        log.info("findRootProjectsByContext");
        return projectRepository.findByParentIsNullAndContext(context);
    }

    @Override
    public Page<Project> findRootProjectsByContext(@NotNull Context context, Pageable pageRequest) {
        log.info("findRootProjectsByContext");
        return projectRepository.findByParentIsNullAndContext(context,pageRequest);
    }

    @Override
    public List<Project> findAllProjectsByContext(@NotNull Context context) {
        log.info("findAllProjectsByContext");
        return projectRepository.findByContext(context);
    }

    @Override
    public Page<Project> findAllProjectsByContext(Context context, Pageable pageRequest) {
        log.info("findAllProjectsByContext");
        return projectRepository.findByContext(context,pageRequest);
    }

    @Override
    public Project findByProjectId(@Min(1L) long projectId) {
        log.info("findByProjectId");
        if(projectRepository.existsById(projectId)){
            return projectRepository.getReferenceById(projectId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project add(@NotNull Project entity) {
        log.info("saveAndFlush");
        entity.setUuid(UUID.randomUUID());
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project update(@NotNull Project entity) {
        log.info("saveAndFlush");
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(@NotNull Project thisProject) {
        log.info("delete");
        projectRepository.delete(thisProject);
    }

  @Override
  public List<Task> findByProject(Project thisProject) {
    return taskRepository.findByProject(thisProject);
  }


  @Override
  public Project getReferenceById(long projectId) {
    return projectRepository.getReferenceById(projectId);
  }


  @Override
  public List<Project> getAllChildrenOfProject(@NotNull Project thisProject) {
        log.info("getAllChildrenOfProject");
        List<Project> childrenOfProject = new ArrayList<>();
        childrenOfProject.add(thisProject);
        for(Project p : thisProject.getChildren()){
            List<Project> getNextGeneration = getAllChildrenOfProject(p);
            childrenOfProject.addAll(getNextGeneration);
        }
        return childrenOfProject;
    }

}
