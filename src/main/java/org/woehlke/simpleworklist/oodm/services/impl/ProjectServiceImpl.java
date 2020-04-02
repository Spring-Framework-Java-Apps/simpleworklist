package org.woehlke.simpleworklist.oodm.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.oodm.repository.ProjectRepository;
import org.woehlke.simpleworklist.oodm.repository.TaskRepository;
import org.woehlke.simpleworklist.oodm.services.ProjectService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

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
        return projectRepository.findByParentIsNullAndContext(context);
    }

    @Override
    public List<Project> findAllProjectsByContext(Context context) {
        return projectRepository.findByContext(context);
    }

    @Override
    public Project findByProjectId(long projectId) {
        if(projectRepository.existsById(projectId)){
            return projectRepository.getOne(projectId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project saveAndFlush(Project entity) {
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Project thisProject) {
        Project oldParent = thisProject.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisProject);
            projectRepository.saveAndFlush(oldParent);
        }
        projectRepository.delete(thisProject);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveProjectToAnotherContext(Project thisProject, Context newContext) {
            LOGGER.info("----------------------------------------------------");
            LOGGER.info("moveProjectToAnotherContext: Project: "+thisProject.toString());
            LOGGER.info("----------------------------------------------------");
            LOGGER.info("moveProjectToAnotherContext: newContext: "+ newContext.toString());
            LOGGER.info("----------------------------------------------------");
            thisProject.setParent(null);
            projectRepository.saveAndFlush(thisProject);
            List<Project> list = getAllChildrenOfProject(thisProject);
            for(Project p : list){
                List<Task> tasks = taskRepository.findByProject(p);
                for(Task t:tasks){
                    t.setContext(newContext);
                }
                taskRepository.saveAll(tasks);
                p.setContext(newContext);
                projectRepository.saveAndFlush(p);
            }
    }

    private List<Project> getAllChildrenOfProject(Project thisProject) {
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
    public void moveProjectToAnotherProject(Project thisProject,
                                            Project targetProject) {
        Project oldParent = thisProject.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisProject);
            projectRepository.saveAndFlush(oldParent);
        }
        thisProject.setParent(targetProject);
        projectRepository.saveAndFlush(thisProject);
    }
}
