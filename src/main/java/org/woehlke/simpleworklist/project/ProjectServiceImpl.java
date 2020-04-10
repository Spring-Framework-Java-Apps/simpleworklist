package org.woehlke.simpleworklist.project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskRepository;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    /*
    @Deprecated
    @Override
    public List<Project> findRootProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByParentIsNullAndUserAccount(userAccount);
    }

    @Deprecated
    @Override
    public List<Project> findAllProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByUserAccount(userAccount);
    }
     */

    @Override
    public List<Project> findRootProjectsByContext(Context context) {
        log.info("findRootProjectsByContext");
        return projectRepository.findByParentIsNullAndContext(context);
    }

    @Override
    public List<Project> findAllProjectsByContext(Context context) {
        log.info("findAllProjectsByContext");
        return projectRepository.findByContext(context);
    }

    @Override
    public Project findByProjectId(long projectId) {
        log.info("findByProjectId");
        if(projectRepository.existsById(projectId)){
            return projectRepository.getOne(projectId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project add(Project entity) {
        log.info("saveAndFlush");
        entity.setUuid(UUID.randomUUID().toString());
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project update(Project entity) {
        log.info("saveAndFlush");
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project delete(Project thisProject) {
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
    public Project moveProjectToAnotherContext(Project thisProject, Context newContext) {
        log.info("----------------------------------------------------");
        log.info("moveProjectToAnotherContext: Project: "+ thisProject.toString());
        log.info("----------------------------------------------------");
        log.info("moveProjectToAnotherContext: newContext: "+ newContext.toString());
        log.info("----------------------------------------------------");
        thisProject.setParent(null);
        thisProject = projectRepository.saveAndFlush(thisProject);
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

    private List<Project> getAllChildrenOfProject(Project thisProject) {
        log.info("getAllChildrenOfProject");
        List<Project> retVal = new ArrayList<>();
        retVal.add(thisProject);
        for(Project p : thisProject.getChildren()){
            List<Project> getNextGeneration = getAllChildrenOfProject(p);
            retVal.addAll(getNextGeneration);
        }
        return retVal;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project moveProjectToAnotherProject(
        Project thisProject,
        Project targetProject
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
