package org.woehlke.simpleworklist.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.ProjectRepository;
import org.woehlke.simpleworklist.services.ProjectService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Inject
    private ProjectRepository projectRepository;

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
        Project p = projectRepository.findOne(projectId);
        if( (p == null) || (p.getUserAccount().getId().longValue() != user.getId().longValue()) ){
            /*
            LOGGER.info("FEHLER");
            LOGGER.info("---------------------");
            LOGGER.info("user: " + user);
            LOGGER.info("---------------------");
            LOGGER.info("found project: "+p);
            LOGGER.info("---------------------");
            */
            return null;
        } else {
            /*
            LOGGER.info("OK");
            LOGGER.info("---------------------");
            LOGGER.info("user: " + user);
            LOGGER.info("---------------------");
            LOGGER.info("found project: "+p);
            LOGGER.info("---------------------");
            */
            return p;
        }
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
        if(thisProject.getUserAccount().getId().longValue() ==user.getId().longValue()){
            Project oldParent = thisProject.getParent();
            if (oldParent != null) {
                oldParent.getChildren().remove(thisProject);
                projectRepository.saveAndFlush(oldParent);
            }
            projectRepository.delete(thisProject);
        }
    }

    @Override
    public List<Project> findAllProjectsByUserAccountAndArea(UserAccount user, Area area) {
        if(user.getId().longValue() == area.getUserAccount().getId().longValue()){
            return projectRepository.findByArea(area);
        } else {
            return new ArrayList<Project>();
        }
    }

    @Override
    public List<Project> findRootProjectsByUserAccountAndArea(UserAccount user, Area area) {
        if(user.getId().longValue() == area.getUserAccount().getId().longValue()){
            return projectRepository.findByParentIsNullAndArea(area);
        } else {
            return new ArrayList<Project>();
        }
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
