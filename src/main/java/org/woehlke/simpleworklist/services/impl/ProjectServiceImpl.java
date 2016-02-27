package org.woehlke.simpleworklist.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.ProjectRepository;
import org.woehlke.simpleworklist.services.ProjectService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    @Inject
    private ProjectRepository projectRepository;

    public List<Project> getBreadcrumb(Project thisProject) {
        List<Project> breadcrumb = new ArrayList<Project>();
        Stack<Project> stack = new Stack<Project>();
        Project breadcrumbProject = thisProject;
        while (breadcrumbProject != null) {
            stack.push(breadcrumbProject);
            breadcrumbProject = breadcrumbProject.getParent();
        }
        while (!stack.empty()) {
            breadcrumb.add(stack.pop());
        }
        return breadcrumb;
    }

    @Override
    public List<Project> findRootProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByParentIsNullAndUserAccount(userAccount);
    }

    @Override
    public Project findByProjectId(long categoryId) {
        return projectRepository.findOne(categoryId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Project saveAndFlush(Project entity) {
        return projectRepository.saveAndFlush(entity);
    }

    @Override
    public List<Project> findAllProjectsByUserAccount(UserAccount userAccount) {
        return projectRepository.findByUserAccount(userAccount);
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
