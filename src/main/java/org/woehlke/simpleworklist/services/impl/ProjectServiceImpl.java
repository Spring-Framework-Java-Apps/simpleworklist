package org.woehlke.simpleworklist.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.ProjectRepository;
import org.woehlke.simpleworklist.repository.TaskRepository;
import org.woehlke.simpleworklist.services.ProjectService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<Project> getBreadcrumb(Project thisProject, UserAccount user) {
        List<Project> breadcrumb = new ArrayList<Project>();
        if(thisProject.getUserAccount().getId().longValue() == user.getId().longValue() ) {
            Stack<Project> stack = new Stack<Project>();
            Project breadcrumbProject = thisProject;
            while (breadcrumbProject != null) {
                stack.push(breadcrumbProject);
                breadcrumbProject = breadcrumbProject.getParent();
            }
            while (!stack.empty()) {
                breadcrumb.add(stack.pop());
            }
        }
        return breadcrumb;
    }

    @Override
    public List<Project> findRootProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByParentIsNullAndUserAccount(userAccount);
    }

    @Override
    public Project findByProjectId(long projectId, UserAccount user) {
        if(projectRepository.existsById(projectId)){
            Project p = projectRepository.getOne(projectId);
            if(p.getUserAccount().getId().longValue() == user.getId().longValue()){
                return p;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project saveAndFlush(Project entity, UserAccount user) {
        if(entity.getUserAccount().getId()==user.getId()){
            return projectRepository.saveAndFlush(entity);
        } else {
            return entity;
        }
    }

    @Override
    public List<Project> findAllProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByUserAccount(userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Project thisProject, UserAccount user) {
        if(thisProject.getUserAccount().getId().longValue() == user.getId().longValue()){
            Project oldParent = thisProject.getParent();
            if (oldParent != null) {
                oldParent.getChildren().remove(thisProject);
                projectRepository.saveAndFlush(oldParent);
            }
            projectRepository.delete(thisProject);
        }
    }

    @Override
    public List<Project> findAllProjectsByUserAccountAndContext(UserAccount user, Context context) {
        if(user.getId().longValue() == context.getUserAccount().getId().longValue()){
            return projectRepository.findByContext(context);
        } else {
            return new ArrayList<Project>();
        }
    }

    @Override
    public List<Project> findRootProjectsByUserAccountAndContext(UserAccount user, Context context) {
        if(user.getId().longValue() == context.getUserAccount().getId().longValue()){
            return projectRepository.findByParentIsNullAndContext(context);
        } else {
            return new ArrayList<Project>();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveProjectToAnotherContext(Project thisProject, Context newContext, UserAccount userAccount) {
        if(thisProject.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
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
                    taskRepository.saveAndFlush(t);
                }
                p.setContext(newContext);
                projectRepository.saveAndFlush(p);
            }
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
                                            Project targetProject, UserAccount user) {
        if(thisProject.getUserAccount().getId().longValue() == user.getId().longValue()) {
            Project oldParent = thisProject.getParent();
            if (oldParent != null) {
                oldParent.getChildren().remove(thisProject);
                projectRepository.saveAndFlush(oldParent);
            }
            thisProject.setParent(targetProject);
            projectRepository.saveAndFlush(thisProject);
        }
    }
}
