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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    public Project delete(@NotNull Project thisProject) {
        log.info("delete");
        Project oldParent = thisProject.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisProject);
            projectRepository.saveAndFlush(oldParent);
        }
        projectRepository.delete(thisProject);
        return oldParent;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project moveProjectToAnotherContext(@NotNull Project thisProject, @NotNull Context newContext) {
        log.info("----------------------------------------------------");
        log.info("moveProjectToAnotherContext: Project: "+ thisProject.toString());
        log.info("----------------------------------------------------");
        log.info("moveProjectToAnotherContext: newContext: "+ newContext.toString());
        log.info("----------------------------------------------------");
        thisProject.setParent(null);
        thisProject = projectRepository.saveAndFlush(thisProject);
        //TODO: remove Recursion, remove unbounded Recursion and List instead of Page.
        List<Project> listProject = getAllChildrenOfProject(thisProject);
        for(Project childProject : listProject){
            List<Task> tasksOfChildProject = taskRepository.findByProject(childProject);
            for(Task task:tasksOfChildProject){
                task.setContext(newContext);
            }
            childProject.setContext(newContext);
            taskRepository.saveAll(tasksOfChildProject);
            projectRepository.saveAndFlush(childProject);
        }
        return thisProject;
    }

  @Override
  public Project getReferenceById(long projectId) {
    return projectRepository.getReferenceById(projectId);
  }

  //TODO: remove Recursion, remove unbounded Recursion and List instead of Page.
    @Deprecated
    private List<Project> getAllChildrenOfProject(@NotNull Project thisProject) {
        log.info("getAllChildrenOfProject");
        List<Project> childrenOfProject = new ArrayList<>();
        childrenOfProject.add(thisProject);
        for(Project p : thisProject.getChildren()){
            //TODO: remove Recursion, remove unbounded Recursion and List instead of Page.
            List<Project> getNextGeneration = getAllChildrenOfProject(p);
            childrenOfProject.addAll(getNextGeneration);
        }
        return childrenOfProject;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project moveProjectToAnotherProject(
        @NotNull Project thisProject,
        @NotNull Project targetProject
    ) {
        log.info("moveProjectToAnotherProject");
        Project oldParent = thisProject.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisProject);
            projectRepository.saveAndFlush(oldParent);
        }
        thisProject.setParent(targetProject);
        return projectRepository.saveAndFlush(thisProject);
    }
}
