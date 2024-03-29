package org.woehlke.java.simpleworklist.application.helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectRepository;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationRepository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskRepository;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountRepository;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryRepository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
