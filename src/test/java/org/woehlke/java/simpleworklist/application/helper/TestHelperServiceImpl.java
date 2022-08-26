package org.woehlke.java.simpleworklist.application.helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.project.Project;
import org.woehlke.java.simpleworklist.domain.project.ProjectRepository;
import org.woehlke.java.simpleworklist.domain.user.signup.UserRegistrationRepository;
import org.woehlke.java.simpleworklist.domain.user.passwordrecovery.UserPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.user.signup.UserRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.task.TaskRepository;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccountRepository;
import org.woehlke.java.simpleworklist.domain.user.passwordrecovery.UserPasswordRecoveryRepository;

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
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserPasswordRecoveryRepository userPasswordRecoveryRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllRegistrations() {
        userRegistrationRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllPasswordRecoveries() {
        userPasswordRecoveryRepository.deleteAll();
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
        return userRegistrationRepository.findAll().size();
    }

    @Override
    public int getNumberOfAllPasswordRecoveries() {
        return userPasswordRecoveryRepository.findAll().size();
    }

    @Override
    public UserRegistration findRegistrationByEmail(@Email @NotBlank String email) {
        return userRegistrationRepository.findByEmail(email);
    }

    @Override
    public UserPasswordRecovery findPasswordRecoveryByEmail(@Email @NotBlank String email) {
        return userPasswordRecoveryRepository.findByEmail(email);
    }
}
