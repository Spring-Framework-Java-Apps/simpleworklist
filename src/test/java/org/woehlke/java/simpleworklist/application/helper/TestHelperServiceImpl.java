package org.woehlke.java.simpleworklist.application.helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.project.Project;
import org.woehlke.java.simpleworklist.domain.project.ProjectRepository;
import org.woehlke.java.simpleworklist.domain.user.signup.UserAccountRegistrationRepository;
import org.woehlke.java.simpleworklist.domain.user.passwordrecovery.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.user.signup.UserAccountRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.task.TaskRepository;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccountRepository;
import org.woehlke.java.simpleworklist.domain.user.passwordrecovery.UserAccountPasswordRecoveryRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TestHelperServiceImpl implements TestHelperService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserAccountRegistrationRepository userAccountRegistrationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountPasswordRecoveryRepository userAccountPasswordRecoveryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllRegistrations() {
        userAccountRegistrationRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllPasswordRecoveries() {
        userAccountPasswordRecoveryRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllProjects() {
        List<Project> projects = projectRepository.findAll();
        for(Project project:projects){
            project.setParent(null);
        }
        projectRepository.saveAll(projects);
        projectRepository.flush();
        projectRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteUserAccount() {
        userAccountRepository.deleteAll();
    }

    @Override
    public int getNumberOfAllRegistrations() {
        return userAccountRegistrationRepository.findAll().size();
    }

    @Override
    public int getNumberOfAllPasswordRecoveries() {
        return userAccountPasswordRecoveryRepository.findAll().size();
    }

    @Override
    public UserAccountRegistration findRegistrationByEmail(@Email @NotBlank String email) {
        return userAccountRegistrationRepository.findByEmail(email);
    }

    @Override
    public UserAccountPasswordRecovery findPasswordRecoveryByEmail(@Email @NotBlank String email) {
        return userAccountPasswordRecoveryRepository.findByEmail(email);
    }
}
