package org.woehlke.simpleworklist.project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.services.TaskRepository;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    //@Override
    public List<Project> findRootProjectsByContext(@NotNull Context context) {
        log.info("findRootProjectsByContext");
        //TODO: #245 change List<Project> to Page<Project>
        return projectRepository.findByParentIsNullAndContext(context);
    }

    @Override
    public Page<Project> findRootProjectsByContext(@NotNull Context context, Pageable pageRequest) {
        log.info("findRootProjectsByContext");
        return projectRepository.findByParentIsNullAndContext(context,pageRequest);
    }

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    //@Override
    public List<Project> findAllProjectsByContext(@NotNull Context context) {
        log.info("findAllProjectsByContext");
        //TODO: #245 change List<Project> to Page<Project>
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
            return projectRepository.getOne(projectId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project add(@NotNull Project entity) {
        log.info("saveAndFlush");
        entity.setUuid(UUID.randomUUID().toString());
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
